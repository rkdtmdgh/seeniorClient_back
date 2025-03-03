package com.see_nior.seeniorClient.OAuth;

import org.springframework.security.core.AuthenticationException;

import com.see_nior.seeniorClient.dto.OAuth2Response;

@SuppressWarnings("serial")
public class OAuth2UserNotFoundException extends AuthenticationException {

	private final OAuth2Response oAuth2Response;
	
	public OAuth2UserNotFoundException(OAuth2Response oAuth2Response) {
		super("OAuth2 회원 가입 필요");
		this.oAuth2Response = oAuth2Response;
	}
	
	public OAuth2Response getOAuth2Response() {
		return oAuth2Response;
	}

}
