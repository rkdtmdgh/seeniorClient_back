package com.see_nior.seeniorClient.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

	public static Cookie createCookie(String key, String value) {
		
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);     	// 쿠키가 살아있을 시간
//        cookie.setSecure(false);     		// https에서만 동작할것인지 (로컬은 http 환경이라 안먹음)
        cookie.setPath("/");       	 		// 쿠키가 전역에서 동작
        cookie.setHttpOnly(true);       	// http에서만 쿠키가 동작할 수 있도록 (js와 같은곳에서 가져갈 수 없도록)
//        cookie.setAttribute("SameSite", "None");
//        cookie.setDomain("localhost");
		
        return cookie;
	}
	
}
