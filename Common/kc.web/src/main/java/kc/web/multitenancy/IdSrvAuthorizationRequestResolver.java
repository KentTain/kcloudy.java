package kc.web.multitenancy;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import kc.framework.tenant.ITenantResolver;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.framework.tenant.TenantContext;
import kc.service.constants.OpenIdConnectConstants;

@Component
@lombok.extern.slf4j.Slf4j
public class IdSrvAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	public final static String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";

	public final static String DEFAULT_AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization";

	private static final char PATH_DELIMITER = '/';

	private final ClientRegistrationRepository clientRegistrationRepository;
	private final AntPathRequestMatcher authorizationRequestMatcher;
	private final StringKeyGenerator stateGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder());

	private final StringKeyGenerator secureKeyGenerator = new Base64StringKeyGenerator(
			Base64.getUrlEncoder().withoutPadding(), 96);

	private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer = (customizer) -> {
	};

	private final kc.framework.tenant.ITenantResolver tenantResolver;
	
	public IdSrvAuthorizationRequestResolver(ITenantResolver tenantResolver, ClientRegistrationRepository clientRegistrationRepository) {
		this.tenantResolver = tenantResolver;
		this.clientRegistrationRepository = clientRegistrationRepository;
    	this.authorizationRequestMatcher = new AntPathRequestMatcher(
    			DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    	String registrationId = this.resolveRegistrationId(request);
		//log.debug(String.format("-----IdSrvAuthorizationRequestResolver resolve Request's registrationId: %s from %s" ,registrationId, request.getRequestURL()));
    	if (registrationId == null) {
			return null;
		}
		String redirectUriAction = getAction(request, "login");
		return resolve(request, registrationId, redirectUriAction);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {
		//log.debug("-----IdSrvAuthorizationRequestResolver resolve registrationId: " + registrationId);
    	if (registrationId == null) {
			return null;
		}
		String redirectUriAction = getAction(request, "authorize");
		return resolve(request, registrationId, redirectUriAction);
    }

    private OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId, String redirectUriAction) {
    	if (registrationId == null) {
			return null;
		}

		ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);
		if (clientRegistration == null) {
			throw new IllegalArgumentException("Invalid Client Registration with Id: " + registrationId);
		}

		String url = request.getScheme() + "://" + request.getServerName();
		Tenant tenant = tenantResolver.Resolve(url);
		if (tenant == null)
			throw new IllegalArgumentException(String.format("未找到相关租户信息，租户访问域名为：%s", url));
		TenantContext.setCurrentTenant(tenant);

//		log.debug(String.format("-----IdSrvAuthorizationRequestResolver get tenant 【%s】 from domain: 【%s】",
//				tenant.getTenantName(), url));

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(OAuth2ParameterNames.REGISTRATION_ID, clientRegistration.getRegistrationId());
        OAuth2AuthorizationRequest.Builder builder = getBuilder(clientRegistration, attributes);

		String tenantName = tenant.getTenantName();
		String redirectUriStr = expandRedirectUri(request, clientRegistration, redirectUriAction);
		String authUriStr = replaceHostInUrl(clientRegistration.getProviderDetails().getAuthorizationUri(), tenantName);
		log.debug("-----IdSvrAuthorizationRequestResolver resolve tenant: 【" + tenantName
				+ "】, redirectUriStr=【" + redirectUriStr
				+ "】, authUriStr=【" + authUriStr + "】");
        
		//将自定义参数添加到现有OAuth2AuthorizationRequest.additionalParameters
		Map<String, Object> additionalParameters = new HashMap<>();
		additionalParameters.put(OAuth2ParameterNames.REGISTRATION_ID, clientRegistration.getRegistrationId());
		if (registrationId.equalsIgnoreCase(OpenIdConnectConstants.ClientAuthScheme)) {
        	//additionalParameters.put("acr_values", "idp:" + registrationId);
        	additionalParameters.put("acr_values", "tenant:" + tenantName);
        	additionalParameters.put(OpenIdConnectConstants.ClaimTypes_TenantName, tenantName);
        }

		return builder
				.clientId(TenantConstant.GetClientIdByTenant(tenant))
				.authorizationUri(authUriStr)
				.redirectUri(redirectUriStr)
				.scopes(clientRegistration.getScopes())
				.state(this.stateGenerator.generateKey())
				.additionalParameters(additionalParameters)
				.attributes(attributes)
				.build();
    }

	/**
	 * Sets the {@code Consumer} to be provided the
	 * {@link OAuth2AuthorizationRequest.Builder} allowing for further customizations.
	 * @param authorizationRequestCustomizer the {@code Consumer} to be provided the
	 * {@link OAuth2AuthorizationRequest.Builder}
	 * @since 5.3
	 */
	public void setAuthorizationRequestCustomizer(
			Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer) {
		Assert.notNull(authorizationRequestCustomizer, "authorizationRequestCustomizer cannot be null");
		this.authorizationRequestCustomizer = authorizationRequestCustomizer;
	}
    
    private String getAction(HttpServletRequest request, String defaultAction) {
		String action = request.getParameter("action");
		if (null == action) {
			return defaultAction;
		}
		return action;
	}

	private OAuth2AuthorizationRequest.Builder getBuilder(ClientRegistration clientRegistration,
														  Map<String, Object> attributes) {
		if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
			OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode();
			Map<String, Object> additionalParameters = new HashMap<>();
			if (!CollectionUtils.isEmpty(clientRegistration.getScopes())
					&& clientRegistration.getScopes().contains(OidcScopes.OPENID)) {
				// Section 3.1.2.1 Authentication Request -
				// https://openid.net/specs/openid-connect-core-1_0.html#AuthRequest scope
				// REQUIRED. OpenID Connect requests MUST contain the "openid" scope
				// value.
				addNonceParameters(attributes, additionalParameters);
			}
			if (ClientAuthenticationMethod.NONE.equals(clientRegistration.getClientAuthenticationMethod())) {
				addPkceParameters(attributes, additionalParameters);
			}
			builder.additionalParameters(additionalParameters);
			return builder;
		}
		if (AuthorizationGrantType.IMPLICIT.equals(clientRegistration.getAuthorizationGrantType())) {
			return OAuth2AuthorizationRequest.implicit();
		}
		throw new IllegalArgumentException(
				"Invalid Authorization Grant Type (" + clientRegistration.getAuthorizationGrantType().getValue()
						+ ") for Client Registration with Id: " + clientRegistration.getRegistrationId());
	}
    
    
    private String resolveRegistrationId(HttpServletRequest request) {
		RequestMatcher.MatchResult matchResult = this.authorizationRequestMatcher.matcher(request);
		if (matchResult.isMatch()) {
			return matchResult.getVariables().get(REGISTRATION_ID_URI_VARIABLE_NAME);
		}
		return null;
	}

	private static String expandRedirectUri(HttpServletRequest request, ClientRegistration clientRegistration,
											String action) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("registrationId", clientRegistration.getRegistrationId());
		// @formatter:off
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
				.replacePath(request.getContextPath())
				.replaceQuery(null)
				.fragment(null)
				.build();
		// @formatter:on
		String scheme = uriComponents.getScheme();
		uriVariables.put("baseScheme", (scheme != null) ? scheme : "");
		String host = uriComponents.getHost();
		uriVariables.put("baseHost", (host != null) ? host : "");
		// following logic is based on HierarchicalUriComponents#toUriString()
		int port = uriComponents.getPort();
		uriVariables.put("basePort", (port == -1) ? "" : ":" + port);
		String path = uriComponents.getPath();
		if (StringUtils.hasLength(path)) {
			if (path.charAt(0) != PATH_DELIMITER) {
				path = PATH_DELIMITER + path;
			}
		}
		uriVariables.put("basePath", (path != null) ? path : "");
		uriVariables.put("baseUrl", uriComponents.toUriString());
		uriVariables.put("action", (action != null) ? action : "");
		return UriComponentsBuilder.fromUriString(clientRegistration.getRedirectUri()).buildAndExpand(uriVariables)
				.toUriString();
	}

	private static String getUrlHostWithoutPort(String originalURL)
	{
		try {
			URI uri = new URI(originalURL);
			int port = uri.getPort();
			if(port == -1)
				return uri.getHost();
			
			return uri.getHost();
		} catch (URISyntaxException e) {
			log.error("Method getUrlHostWithoutPort throw exception: " + e.getMessage());
			return null;
		}
	}

	private static String getUrlHostWithPort(String originalURL)
	{
		try {
			URI uri = new URI(originalURL);
			int port = uri.getPort();
			if(port == -1)
				return uri.getHost();
			
			return uri.getHost() + ":" + port;
		} catch (URISyntaxException e) {
			log.error("Method getUrlHostWithPort throw exception: " + e.getMessage());
			return null;
		}
	}

	private static String replaceHostInUrl(String originalURL, String tenantName) {
		try {
			URI uri = new URI(originalURL);
			uri = new URI(uri.getScheme().toLowerCase(), tenantName + "." + uri.getAuthority(), uri.getPath(), uri.getQuery(), uri.getFragment());
			
			return uri.toString();
		} catch (URISyntaxException e) {
			log.error("Method replaceHostInUrl throw exception: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Creates nonce and its hash for use in OpenID Connect 1.0 Authentication Requests.
	 * @param attributes where the {@link OidcParameterNames#NONCE} is stored for the
	 * authentication request
	 * @param additionalParameters where the {@link OidcParameterNames#NONCE} hash is
	 * added for the authentication request
	 *
	 * @since 5.2
	 * @see <a target="_blank" href=
	 * "https://openid.net/specs/openid-connect-core-1_0.html#AuthRequest">3.1.2.1.
	 * Authentication Request</a>
	 */
	private void addNonceParameters(Map<String, Object> attributes, Map<String, Object> additionalParameters) {
		try {
			String nonce = this.secureKeyGenerator.generateKey();
			String nonceHash = createHash(nonce);
			attributes.put(OidcParameterNames.NONCE, nonce);
			additionalParameters.put(OidcParameterNames.NONCE, nonceHash);
		}
		catch (NoSuchAlgorithmException e) {
			log.error("Method addNonceParameters throw exception: " + e.getMessage());
		}
	}

	/**
	 * Creates and adds additional PKCE parameters for use in the OAuth 2.0 Authorization
	 * and Access Token Requests
	 * @param attributes where {@link PkceParameterNames#CODE_VERIFIER} is stored for the
	 * token request
	 * @param additionalParameters where {@link PkceParameterNames#CODE_CHALLENGE} and,
	 * usually, {@link PkceParameterNames#CODE_CHALLENGE_METHOD} are added to be used in
	 * the authorization request.
	 *
	 * @since 5.2
	 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7636#section-1.1">1.1.
	 * Protocol Flow</a>
	 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7636#section-4.1">4.1.
	 * Client Creates a Code Verifier</a>
	 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7636#section-4.2">4.2.
	 * Client Creates the Code Challenge</a>
	 */
	private void addPkceParameters(Map<String, Object> attributes, Map<String, Object> additionalParameters) {
		String codeVerifier = this.secureKeyGenerator.generateKey();
		attributes.put(PkceParameterNames.CODE_VERIFIER, codeVerifier);
		try {
			String codeChallenge = createHash(codeVerifier);
			additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, codeChallenge);
			additionalParameters.put(PkceParameterNames.CODE_CHALLENGE_METHOD, "S256");
		}
		catch (NoSuchAlgorithmException ex) {
			log.error("Method addPkceParameters throw exception: " + ex.getMessage());
			additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, codeVerifier);
		}
	}

	private static String createHash(String value) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
		return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
	}
}

