package kc.web.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import kc.framework.tenant.ITenantResolver;
import kc.service.constants.OpenIdConnectConstants;
import kc.web.multitenancy.IdSrvAuthenticationProvider;
import kc.web.multitenancy.IdSrvAuthorizationCodeTokenResponseClient;
import kc.web.multitenancy.IdSrvAuthorizationRequestResolver;
import kc.web.multitenancy.IdSrvGrantedAuthority;
import kc.web.multitenancy.IdSrvOidcUser;
import kc.web.multitenancy.IdSrvOidcUserService;
import kc.web.multitenancy.IdSrvSecurityInterceptor;

@lombok.extern.slf4j.Slf4j
public class SpringSecurityUtil {

	public static void securityConfig(HttpSecurity http, ITenantResolver tenantResolver,
			IdSrvSecurityInterceptor securityFilter, IdSrvAuthorizationCodeTokenResponseClient idTokenResponseClient,
			ClientRegistrationRepository clientRegistrationRepository) throws Exception {
		http
			.headers()
//				.contentSecurityPolicy("connect-src * default-src 'none' style-src 'self' 'unsafe-inline';")
//				.reportOnly()
//				.and()
				.frameOptions()
				//.sameOrigin()
				//.httpStrictTransportSecurity()
				.disable()

			.and()
			.addFilterBefore(securityFilter, FilterSecurityInterceptor.class)
			//.addFilterBefore(securityMethodFilter, FilterSecurityInterceptor.class)
			.authenticationProvider(new IdSrvAuthenticationProvider(idTokenResponseClient, oidcUserService()))
			.authorizeRequests()
			.anyRequest()
				.authenticated()
				// 修改授权相关逻辑
				.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
					public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {

						// 覆盖SecurityMetadataSource
						// fsi.setSecurityMetadataSource(fsi.getSecurityMetadataSource());

						// 覆盖AccessDecisionManager
						//fsi.setAccessDecisionManager(new IdSvrAccessDecisionManager());

						// 增加投票项
						//AccessDecisionManager accessDecisionManager = fsi.getAccessDecisionManager();
						//if (accessDecisionManager instanceof AbstractAccessDecisionManager) {
						//	((AbstractAccessDecisionManager) accessDecisionManager).getDecisionVoters()
						//			.add(new IdSvrAccessDecisionVoter());
						//}

						return fsi;
					}
				})
				.and()
					// 默认为 /logout
					.logout()
					.deleteCookies("JSESSIONID")
					// 无效会话
					.invalidateHttpSession(true)
					// 清除身份验证
					.clearAuthentication(true)
					.permitAll()
				.and()
				.oauth2Login()
				//.successHandler(successHandler)
				//.failureHandler(failureHandler)
				.authorizationEndpoint()
					//.authorizationRequestRepository(cookieAuthorizationRequestRepository())
					// 1. 配置自定义OAuth2AuthorizationRequestResolver
					.authorizationRequestResolver(new IdSrvAuthorizationRequestResolver(tenantResolver, clientRegistrationRepository))
					.and()
				.tokenEndpoint()
					.accessTokenResponseClient(idTokenResponseClient)
					.and()
				.userInfoEndpoint()
					.customUserType(IdSrvOidcUser.class, OpenIdConnectConstants.ClientAuthScheme)
					.userAuthoritiesMapper(userAuthoritiesMapper())
					.oidcUserService(oidcUserService())
		;
	}

	private static OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		final IdSrvOidcUserService delegate = new IdSrvOidcUserService();
		return (userRequest) -> {
			return delegate.loadUser(userRequest);
		};
	}

	private static GrantedAuthoritiesMapper userAuthoritiesMapper() {
		return (authorities) -> {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

			authorities.forEach(authority -> {
				if (authority instanceof IdSrvGrantedAuthority) {
					IdSrvGrantedAuthority oidcUserAuthority = (IdSrvGrantedAuthority) authority;
					OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

					Map<String, Object> claims = userInfo.getClaims();
					if (oidcUserAuthority.getIdToken() != null
							&& oidcUserAuthority.getIdToken().getClaims().size() > 0) {
						claims = oidcUserAuthority.getIdToken().getClaims();
					}
					for (String key : claims.keySet()) {
						Object claim = claims.get(key);
						if (key.equalsIgnoreCase(OpenIdConnectConstants.ClaimTypes_AuthorityIds)) {
							if (claim instanceof String) {
								GrantedAuthority idAuthority = new IdSrvGrantedAuthority(claim.toString(),
										oidcUserAuthority.getIdToken(), userInfo);
								mappedAuthorities.add(idAuthority);
							} else if (claim instanceof List<?>) {
								@SuppressWarnings("unchecked")
								List<String> claimList = (List<String>) claim;
								for (String ca : claimList) {
									GrantedAuthority idAuthority = new IdSrvGrantedAuthority(ca,
											oidcUserAuthority.getIdToken(), userInfo);
									mappedAuthorities.add(idAuthority);
								}
							}
						}
					}
				} else if (authority instanceof OidcUserAuthority) {
					OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
					OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

					if (userInfo.hasClaim("role")) {
						String roleName = "ROLE_" + (String) userInfo.getClaimAsString("role");
						mappedAuthorities.add(new SimpleGrantedAuthority(roleName));
					}
				} else if (authority instanceof OAuth2UserAuthority) {
					OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
					Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

					if (userAttributes.containsKey("role")) {
						String roleName = "ROLE_" + (String) userAttributes.get("role");
						mappedAuthorities.add(new SimpleGrantedAuthority(roleName));
					}
				}
			});

//			StringBuilder sb = new StringBuilder();
//			for (GrantedAuthority authority : mappedAuthorities) {
//				sb.append(authority.getAuthority() + ", ");
//			}
//			log.debug("----userAuthoritiesMapper mappedAuthorities: " + sb.toString());
			return mappedAuthorities;
		};
	}
}
