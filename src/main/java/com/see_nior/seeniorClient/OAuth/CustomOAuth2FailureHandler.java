package com.see_nior.seeniorClient.OAuth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler  {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("oauth 로그인 실패 ---- {}", exception.getMessage());
		
		if (exception instanceof OAuth2UserNotFoundException) {
            OAuth2UserNotFoundException ex = (OAuth2UserNotFoundException) exception;
            OAuth2Response oAuth2Response = ex.getOAuth2Response();

            String u_social_id = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
            
            // OAuth2Response 정보를 쿼리 파라미터로 변환
            String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/OAuth2Result")
            		.queryParam("result", "fail")
                    .queryParam("u_social_id", u_social_id)
                    .encode()
                    .toUriString();

            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("http://localhost:3000/OAuth2Result?result=error");
        }
		
	}
	
}
