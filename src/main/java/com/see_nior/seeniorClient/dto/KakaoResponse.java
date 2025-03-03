package com.see_nior.seeniorClient.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

	private final Map<String, Object> attributes;
	
	public KakaoResponse(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getEmail() {
		
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		
        return kakaoAccount != null ? kakaoAccount.get("email").toString() : null;
	}

	@Override
	public String getName() {
		
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		
		return profile != null ? profile.get("nickname").toString() : null;
	}

	@Override
	public String getProfileImage() {
		
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		
        return profile != null ? profile.get("profile_image_url").toString() : null;
	}
	
}
