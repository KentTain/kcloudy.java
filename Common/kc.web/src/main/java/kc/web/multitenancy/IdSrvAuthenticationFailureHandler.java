package kc.web.multitenancy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class IdSrvAuthenticationFailureHandler implements AuthenticationFailureHandler{
	private Logger logger = LoggerFactory.getLogger(IdSrvAuthenticationFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		logger.error(exception.getMessage());
		
	}

}
