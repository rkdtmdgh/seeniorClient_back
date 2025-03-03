package com.see_nior.seeniorClient.jwt;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
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
		
		// 클라이언트에서 보낸 쿠키 확인
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
		
		// refresh token 쿠키에서 찾기
		Optional<Cookie> refreshCookie = Arrays.stream(cookies)
				.filter(cookie -> "refresh".equals(cookie.getName()))
				.findFirst();
		
		// refresh token 유무 확인
		if(!refreshCookie.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
		
		// refreshToken 값이 비어 있는지 확인
		String refreshToken = refreshCookie.get().getValue();
		if(refreshToken == null || refreshToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

		// refreshToken을 이용해 Redis 키 값 (u_id) 찾기
        String key = jwtUtil.getU_id(refreshToken);

        // Redis에 저장된 refreshToken 확인
        if(redisService.getValues(key) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Redis에서 refreshToken 삭제
        redisService.deleteValues(key);

        // 클라이언트의 refresh 쿠키 삭제
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        SecurityContextHolder.clearContext();
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.addCookie(cookie);
	}
	
}
