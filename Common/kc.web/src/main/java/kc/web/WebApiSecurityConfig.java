package kc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

import kc.framework.tenant.ITenantResolver;
import kc.service.constants.OpenIdConnectConstants;
import kc.web.multitenancy.IdSrvAuthorizationCodeTokenResponseClient;
import kc.web.multitenancy.IdSrvAuthorizationRequestResolver;
import kc.web.multitenancy.IdSrvSecurityInterceptor;
import kc.web.util.SpringSecurityUtil;

@Configuration
@EnableResourceServer
// @EnableWebSecurity
@lombok.extern.slf4j.Slf4j
public class WebApiSecurityConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private ITenantResolver tenantResolver;

	@Autowired
	private IdSrvSecurityInterceptor securityFilter;

	@Autowired
	private IdSrvAuthorizationCodeTokenResponseClient idTokenResponseClient;

	@Lazy
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		super.configure(resources);
	}

	/**
	 * 用于配置对受保护的资源的访问规则 默认情况下所有不在/oauth/**下的资源都是受保护的资源
	 * {@link OAuth2WebSecurityExpressionHandler}
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.anonymous().disable().requestMatchers().antMatchers("/api/**");

		SpringSecurityUtil.securityConfig(http, tenantResolver, securityFilter, idTokenResponseClient,
				clientRegistrationRepository);
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		return new InMemoryClientRegistrationRepository(this.idsvrClientRegistration());
	}

	@Value("${GlobalConfig.SSOWebDomain}")
	private String ssoWebDomain;

	private ClientRegistration idsvrClientRegistration() {
		// String redirectUrl = String.format("{baseUrl}/{%s}/signin-callback",
		// IdSrvAuthorizationRequestResolver.REGISTRATION_ID_URI_VARIABLE_NAME);
		String redirectUrl = String.format("{baseUrl}/login/oauth2/code/{%s}",
				IdSrvAuthorizationRequestResolver.REGISTRATION_ID_URI_VARIABLE_NAME);
		log.info(String.format("===WebApiSecurityConfig idsvrClientRegistration: %s in sso domain: %s to redirect: %s",
				OpenIdConnectConstants.ClientAuthScheme, ssoWebDomain, redirectUrl));

		return ClientRegistration.withRegistrationId(OpenIdConnectConstants.ClientAuthScheme).clientId("Y0RiYQ==")
				.clientName("cDba").clientSecret("MmJmNWIyM2Q5ZjY4OWU5YzFmYWVkZTUwNzY2ZWJkNTg=")
				// .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.scope(OidcScopes.OPENID, OidcScopes.PROFILE).userNameAttributeName(IdTokenClaimNames.SUB)
				// .redirectUriTemplate("{baseUrl}/oidc/signin-callback/{registration_id}")
				.redirectUri(redirectUrl)
				.authorizationUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_Authorize_Action)
				.tokenUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_Token_Action)
				.userInfoUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_UserInfo_Action)
				.jwkSetUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_JwkSet_Action).build();
	}
}
