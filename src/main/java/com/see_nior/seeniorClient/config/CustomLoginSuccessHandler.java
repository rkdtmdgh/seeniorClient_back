package com.see_nior.seeniorClient.config;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.see_nior.seeniorClient.jwt.JwtUtil;
import com.see_nior.seeniorClient.redis.RedisService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final RedisService redisService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("onAuthenticationSuccess()");
		
		// 유저 정보
		String u_id = authentication.getName();
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority authority = iterator.next();
		String role = authority.getAuthority();
		
		// 토큰 생성
		String accessToken = jwtUtil.createJwt("access", u_id, role, 10 * 60 * 1000L);
		String refreshToken = jwtUtil.createJwt("refresh", u_id, role, 24 * 60 * 60 * 1000L);
		
		redisService.setValues(u_id, refreshToken, Duration.ofMillis(24 * 60 * 60 * 1000L));
		
		//응답 설정
	    response.setHeader("access", "Bearer " + accessToken);
	    response.addCookie(createCookie("refresh", refreshToken));
	    response.setStatus(HttpStatus.OK.value());
	    
	    Collection<String> cookies = response.getHeaders("Set-Cookie");
        for (String cookie : cookies) {
			log.info("Response Set-Cookie: {}", cookie);
		}
	}
	
	private Cookie createCookie(String key, String value) {

	    Cookie cookie = new Cookie(key, value);
	    cookie.setMaxAge(24*60*60);
	    //cookie.setSecure(true);
	    //cookie.setPath("/");
	    cookie.setHttpOnly(true);

	    return cookie;
	}
}
