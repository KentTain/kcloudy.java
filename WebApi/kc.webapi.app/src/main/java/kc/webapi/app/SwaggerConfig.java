package kc.webapi.app;


import static springfox.documentation.builders.PathSelectors.ant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kc.framework.tenant.ApplicationConstant;
import kc.service.constants.OpenIdConnectConstants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private static final String VERSION = "1.0";

	@Value("${swagger.title:API Gateway}")
	private String title;
	@Value("${swagger.description:}")
	private String description;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(false)
				.select()
				.apis(RequestHandlerSelectors.basePackage("kc.webapi.controller"))
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(Collections.singletonList(oauth()))
				.securityContexts(Collections.singletonList(securityContext()))
				;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(title)
				.description(description)
				.license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.termsOfServiceUrl("")
				.version(VERSION)
				.build();
	}

	@Bean
    SecurityScheme apiKey() {
		return new ApiKey("apiKey", "Authorization", "header");
    }

	@Bean
	SecurityContext securityContext() {
		//AuthorizationScope[] scopes = new AuthorizationScope[] { new AuthorizationScope("userinfo", "用户信息") };

		SecurityReference securityReference = SecurityReference.builder().reference("oauth2").scopes(scopes()).build();

		return SecurityContext.builder().securityReferences(Arrays.asList(securityReference)).forPaths(ant("/api/**"))
				.build();
	}

	@Value("${GlobalConfig.SSOWebDomain}")
	private String ssoWebDomain;
	@Value("${swagger.clientId}")
	private String clientId;
	@Value("${swagger.clientSecret}")
	private String clientSecret;

	@Bean
	SecurityScheme oauth() {
		return new OAuthBuilder()
				.name("OAuth2")
				.grantTypes(grantTypes())
				.scopes(Arrays.asList(scopes()))
				.build();
	}

	private AuthorizationScope[] scopes() {
		AuthorizationScope[] scops = { new AuthorizationScope(ApplicationConstant.OpenIdScope, "OpenId"),
				new AuthorizationScope(ApplicationConstant.ProfileScope, "Profile"),
				new AuthorizationScope(ApplicationConstant.CfgScope, "Config") };
		return scops;
	}

	private List<GrantType> grantTypes() {
		List<GrantType> grantTypes = new ArrayList<>();
		TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(
				ssoWebDomain + OpenIdConnectConstants.OAuth2_Authorize_Action, clientId, clientSecret);
		TokenEndpoint tokenEndpoint = new TokenEndpoint(ssoWebDomain + OpenIdConnectConstants.OAuth2_Token_Action,
				"access_token");
		grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
		return grantTypes;
	}

	@Bean
	public SecurityConfiguration securityInfo() {
		Map<String, Object> formParameters = new HashMap<String, Object>();
		formParameters.put(OpenIdConnectConstants.ClaimTypes_TenantName, "cDba");
		
		return SecurityConfigurationBuilder.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.realm(ssoWebDomain)
				.appName(ApplicationConstant.ConfigAppName)
				.scopeSeparator(" ")
				.additionalQueryStringParams(formParameters)
				.useBasicAuthenticationWithAccessCodeGrant(false)
				.build();
	}
}
