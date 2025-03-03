package com.see_nior.seeniorClient.enums;

public enum UrlPath {

	OATUTH2_LOGIN_REDIRECT_URI("http://localhost:3000/oAuth2Result");
	
	private String value;
	
	private UrlPath(String value) {

		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
