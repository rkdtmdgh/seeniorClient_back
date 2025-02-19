package com.see_nior.seeniorClient.jwt;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.see_nior.seeniorClient.redis.RedisService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
public class LogoutController {

	private final JwtUtil jwtUtil;
	private final RedisService redisService;
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		log.info("logout()");
		
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
		
		Optional<Cookie> refreshCookie = Arrays.stream(cookies)
				.filter(cookie -> "refresh".equals(cookie.getName()))
				.findFirst();
		
		if(!refreshCookie.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
		
		String refreshToken = refreshCookie.get().getValue();
		
	}
	
}
