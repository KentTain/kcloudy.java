package kc.web.multitenancy;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.*;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.framework.tenant.TenantContext;
import kc.service.constants.OpenIdConnectConstants;

@Component
@lombok.extern.slf4j.Slf4j
public class IdSrvAuthorizationCodeTokenResponseClient
		implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
	private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";

	private RestOperations restOperations;

	@Autowired
	private kc.framework.tenant.ITenantResolver tenantResolver;

	public IdSrvAuthorizationCodeTokenResponseClient() {
		RestTemplate restTemplate = new RestTemplate(
				Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		this.restOperations = restTemplate;
	}

	/**
	 * 获取IdentityServer的返回的Token（AccessToken）
	 */
	@Override
	public OAuth2AccessTokenResponse getTokenResponse(
			OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		Assert.notNull(authorizationCodeGrantRequest, "authorizationCodeGrantRequest cannot be null");

		ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
		if (clientRegistration == null) {
			throw new IllegalArgumentException(
					"IdSrvAuthorizationCodeTokenResponseClient Invalid Token Client Registration with getGrantType: "
							+ authorizationCodeGrantRequest.getGrantType());
		}

		Tenant tenant = null;
		OAuth2AuthorizationExchange exchange = authorizationCodeGrantRequest.getAuthorizationExchange();
		if (exchange != null && exchange.getAuthorizationRequest() != null) {
			OAuth2AuthorizationRequest authorizationRequest = exchange.getAuthorizationRequest();
			Map<String, Object> parameterMap = authorizationRequest.getAdditionalParameters();
			String tenantName = (String) parameterMap.get(OpenIdConnectConstants.ClaimTypes_TenantName);

			tenant = tenantResolver.Resolve(tenantName);
			if (tenant == null)
				throw new IllegalArgumentException("未找到相关租户信息，权限访问地址：" + authorizationRequest.getRedirectUri());
			TenantContext.setCurrentTenant(tenant);

//			log.debug(String.format("-----IdSrvAuthorizationCodeTokenResponseClient get tenant 【%s】 from name: 【%s】",
//					tenant.getTenantName(), tenantName));
		}

		RequestEntity<?> request = convert(authorizationCodeGrantRequest, tenant);

		log.debug(String.format(
				"-----IdSrvAuthorizationCodeTokenResponseClient getTokenResponse tenant: %s, requestUrl=%s",
				tenant.getTenantName(), request.getUrl()));

		ResponseEntity<OAuth2AccessTokenResponse> response;
		try {
			response = this.restOperations.exchange(request, OAuth2AccessTokenResponse.class);
		} catch (RestClientException ex) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_TOKEN_RESPONSE_ERROR_CODE,
					"An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: "
							+ ex.getMessage(),
					null);
			throw new OAuth2AuthorizationException(oauth2Error, ex);
		}

		OAuth2AccessTokenResponse tokenResponse = response.getBody();

		if (CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
			// As per spec, in Section 5.1 Successful Access Token Response
			// https://tools.ietf.org/html/rfc6749#section-5.1
			// If AccessTokenResponse.scope is empty, then default to the scope
			// originally requested by the client in the Token Request
			tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse)
					.scopes(authorizationCodeGrantRequest.getClientRegistration().getScopes()).build();
		}

		return tokenResponse;
	}

	/**
	 * 根据认证请求，组装http请求对象
	 *
	 * @param authorizationCodeGrantRequest OAuth2中的Code授权方式的请求
	 * @param tenant                        相关租户
	 * @return
	 */
	private RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest, Tenant tenant) {
		ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();

		HttpHeaders headers = getTokenRequestHeaders(clientRegistration, tenant);
		MultiValueMap<String, String> formParameters = this.createParameters(authorizationCodeGrantRequest, tenant);
		String tokenUrl = replaceHostInUrl(clientRegistration.getProviderDetails().getTokenUri(),
				tenant.getTenantName());
		URI uri = UriComponentsBuilder.fromUriString(tokenUrl).build().toUri();

		return new RequestEntity<>(formParameters, headers, HttpMethod.POST, uri);
	}

	/**
	 * 添加IdentityServer认证服务器所需的认证参数：grant_type、Code、redirect_uri、clientId、clientsecret
	 * 系统默认：OAuth2AuthorizationCodeGrantRequestEntityConverter
	 * @param authorizationCodeGrantRequest 需要重写的OAuth2中的Code授权方式的请求
	 * @param tenant                        相关租户
	 * @return
	 */
	private MultiValueMap<String, String> createParameters(
			OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest, Tenant tenant) {
		ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
		OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest.getAuthorizationExchange();

		MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
		formParameters.add(OAuth2ParameterNames.GRANT_TYPE, authorizationCodeGrantRequest.getGrantType().getValue());
		formParameters.add(OAuth2ParameterNames.CODE, authorizationExchange.getAuthorizationResponse().getCode());

		String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
		String codeVerifier = authorizationExchange.getAuthorizationRequest()
				.getAttribute(PkceParameterNames.CODE_VERIFIER);
		if (redirectUri != null) {
			formParameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
		}
		if (!ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientRegistration.getClientAuthenticationMethod())
				&& !ClientAuthenticationMethod.BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
			//formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
			formParameters.add(OAuth2ParameterNames.CLIENT_ID, TenantConstant.GetClientIdByTenant(tenant));
		}
		if (ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(clientRegistration.getClientAuthenticationMethod())
				|| ClientAuthenticationMethod.POST.equals(clientRegistration.getClientAuthenticationMethod())) {
			//formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
			formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, TenantConstant.GetClientSecretByTenant(tenant));
		}
		if (codeVerifier != null) {
			formParameters.add(PkceParameterNames.CODE_VERIFIER, codeVerifier);
		}

		return formParameters;
	}

	/**
	 * 获取相关租户的Http请求的Authorization Basic的设置
	 * 
	 * @param clientRegistration 访问IdeneityServer的客户端注册信息
	 * @param tenant             相关租户
	 * @return
	 */
	private HttpHeaders getTokenRequestHeaders(ClientRegistration clientRegistration, Tenant tenant) {
		HttpHeaders headers = new HttpHeaders();
		HttpHeaders defaultHeader = getDefaultTokenRequestHeaders();
		headers.addAll(defaultHeader);
		if (ClientAuthenticationMethod.BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
			headers.setBasicAuth(TenantConstant.GetClientIdByTenant(tenant),
					TenantConstant.GetClientSecretByTenant(tenant));
		}

//		log.debug(String.format(
//				"-----IdSrvAuthorizationCodeTokenResponseClient set tenant: 【%s】 by usign clientId: 【%s】 clientSecret: 【%s】 encryp with key:【%s】 GlobalConfig: 【%s】",
//				tenant.getTenantName(), TenantConstant.GetClientIdByTenant(tenant),
//				TenantConstant.GetClientSecretByTenant(tenant), tenant.getPrivateEncryptKey(),
//				kc.framework.GlobalConfig.EncryptKey));
		return headers;
	}

	/**
	 * 获取Http请求的AcceptHeader、ContentTypeHeader
	 * 
	 * @return
	 */
	private HttpHeaders getDefaultTokenRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
		final MediaType contentType = MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
		headers.setContentType(contentType);
		return headers;
	}

	public static String replaceHostInUrl(String originalURL, String tenantName) {
		try {
			if (originalURL.contains("?")) {
				originalURL += "&acr_values=idp%3A" + OpenIdConnectConstants.ClientAuthScheme + "&acr_values=tenant%3A"
						+ tenantName + "&" + OpenIdConnectConstants.ClaimTypes_TenantName + "=" + tenantName;
			} else {
				originalURL += "?acr_values=idp%3A" + OpenIdConnectConstants.ClientAuthScheme + "&acr_values=tenant%3A"
						+ tenantName + "&" + OpenIdConnectConstants.ClaimTypes_TenantName + "=" + tenantName;
			}

			URI uri = new URI(originalURL);
			uri = new URI(uri.getScheme().toLowerCase(), tenantName + "." + uri.getAuthority(), uri.getPath(),
					uri.getQuery(), uri.getFragment());

			return uri.toString();
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Sets the {@link RestOperations} used when requesting the OAuth 2.0 Access
	 * Token Response.
	 *
	 * <p>
	 * <b>NOTE:</b> At a minimum, the supplied {@code restOperations} must be
	 * configured with the following:
	 * <ol>
	 * <li>{@link HttpMessageConverter}'s - {@link FormHttpMessageConverter} and
	 * {@link OAuth2AccessTokenResponseHttpMessageConverter}</li>
	 * <li>{@link ResponseErrorHandler} -
	 * {@link OAuth2ErrorResponseErrorHandler}</li>
	 * </ol>
	 *
	 * @param restOperations the {@link RestOperations} used when requesting the
	 *                       Access Token Response
	 */
	public void setRestOperations(RestOperations restOperations) {
		Assert.notNull(restOperations, "restOperations cannot be null");
		this.restOperations = restOperations;
	}
}
