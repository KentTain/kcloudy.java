package kc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.ITenantResolver;
import kc.service.constants.OpenIdConnectConstants;
import kc.web.multitenancy.IdSrvAuthorizationCodeTokenResponseClient;
import kc.web.multitenancy.IdSrvAuthorizationRequestResolver;
import kc.web.multitenancy.IdSrvSecurityInterceptor;
import kc.web.util.SpringSecurityUtil;

@Configuration
@lombok.extern.slf4j.Slf4j
public class WebSecurityConfig {

	// @Bean
	// public Connector connector() {
	// Connector connector = new
	// Connector("org.apache.coyote.http11.Http11NioProtocol");
	// connector.setScheme("http");
	// connector.setPort(80);
	// connector.setSecure(false);
	// connector.setRedirectPort(443);
	// connector.setAttribute("relaxedQueryChars", "[]|{}");
	// connector.setAttribute("relaxedPathChars", "[]|");
	// return connector;
	// }

	@Bean
	public TomcatServletWebServerFactory tomcatServletWebServerFactory(/* Connector connector */) {
		// 修改内置的 tomcat 容器配置
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			// @Override
			// protected void postProcessContext(Context context) {
			// SecurityConstraint securityConstraint = new SecurityConstraint();
			// securityConstraint.setUserConstraint("CONFIDENTIAL");
			// SecurityCollection collection = new SecurityCollection();
			// collection.addPattern("/*");
			// securityConstraint.addCollection(collection);
			// context.addConstraint(securityConstraint);
			// }
		};
		// tomcat.addAdditionalTomcatConnectors(connector);
		tomcat.addConnectorCustomizers(
				(TomcatConnectorCustomizer) customer -> customer.setProperty("relaxedQueryChars", "|[]{}"));
		return tomcat;
	}

	/**
	 * OpenId Connect安全设置
	 * 
	 * @author 田长军
	 *
	 */
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private ClientRegistrationRepository clientRegistrationRepository;

		@Autowired
		private ITenantResolver tenantResolver;

		// @Autowired
		// private IdSrvAuthenticationSuccessHandler successHandler;
		// @Autowired
		// private IdSrvAuthenticationFailureHandler failureHandler;

		@Autowired
		private IdSrvSecurityInterceptor securityFilter;

		@Autowired
		private IdSrvAuthorizationCodeTokenResponseClient idTokenResponseClient;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			SpringSecurityUtil.securityConfig(http, tenantResolver, securityFilter, idTokenResponseClient,
					clientRegistrationRepository);
		}
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
		log.info(String.format("===WebSecurityConfig idsvrClientRegistration: %s in sso domain: %s to redirect: %s",
				OpenIdConnectConstants.ClientAuthScheme, ssoWebDomain, redirectUrl));

		return ClientRegistration.withRegistrationId(OpenIdConnectConstants.ClientAuthScheme).clientId("Y0RiYQ==")
				.clientName("cDba").clientSecret("MmJmNWIyM2Q5ZjY4OWU5YzFmYWVkZTUwNzY2ZWJkNTg=")
				// .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.scope(OidcScopes.OPENID, OidcScopes.PROFILE, ApplicationConstant.AccScope)
				.userNameAttributeName(IdTokenClaimNames.SUB)
				// .redirectUriTemplate("{baseUrl}/oidc/signin-callback/{registration_id}")
				.redirectUri(redirectUrl)
				.authorizationUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_Authorize_Action)
				.tokenUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_Token_Action)
				.userInfoUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_UserInfo_Action)
				.jwkSetUri(ssoWebDomain + OpenIdConnectConstants.OAuth2_JwkSet_Action).build();
	}
}
