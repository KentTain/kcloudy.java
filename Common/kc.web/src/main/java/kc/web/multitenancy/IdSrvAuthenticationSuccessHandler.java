package kc.web.multitenancy;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class IdSrvAuthenticationSuccessHandler  implements AuthenticationSuccessHandler {
	private Logger logger = LoggerFactory.getLogger(IdSrvAuthenticationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		//addSameSiteCookieAttribute(response);    // add SameSite=strict to Set-Cookie attribute
		Object  obj = authentication.getPrincipal();
		if(OidcUser.class.isInstance(obj))
		{
			OidcUser user = (OidcUser) obj; 
			String account = user.getName(); 
			logger.debug("----IdSvrAuthenticationSuccessHandler onAuthenticationSuccess user: " + account);
		}
	}

	//cookie启用samesite: https://cloud.tencent.com/developer/ask/147830
	private void addSameSiteCookieAttribute(HttpServletResponse response) {
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for (String header : headers) { // there can be multiple Set-Cookie attributes
			if (firstHeader) {
				response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict"));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict"));
		}
	}
 
}
