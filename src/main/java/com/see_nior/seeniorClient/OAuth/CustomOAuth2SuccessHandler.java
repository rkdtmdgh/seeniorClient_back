package com.see_nior.seeniorClient.OAuth;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.see_nior.seeniorClient.enums.UrlPath;
import com.see_nior.seeniorClient.jwt.JwtUtil;
import com.see_nior.seeniorClient.redis.RedisService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final RedisService redisService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();
		
		// 토큰 생성시에 사용자명과 권한이 필요하니 준비
        String u_name = customUserDetail.getU_name();
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        
        // accessToken과 refreshToken 생성
        String refreshToken = jwtUtil.createJwt("refresh", u_name, role, 86400000L);
		
        // redis에 insert (key = u_name / value = refreshToken)
        redisService.setValues(u_name, refreshToken, Duration.ofMillis(86400000L));
        
        // 응답
        response.addCookie(createCookie("refresh", refreshToken));
        
        // 리다이렉트 URL
        String redirectUrl = UriComponentsBuilder.fromUriString(UrlPath.OATUTH2_LOGIN_REDIRECT_URI.getValue())
                .queryParam("result", "success")
                .encode()
                .toUriString();

        // 리다이렉트 실행
        response.sendRedirect(redirectUrl);
	
	}
	
	private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);     // 쿠키가 살아있을 시간
        /*cookie.setSecure();*/         // https에서만 동작할것인지 (로컬은 http 환경이라 안먹음)
        /*cookie.setPath("/");*/        // 쿠키가 전역에서 동작
        cookie.setHttpOnly(true);       // http에서만 쿠키가 동작할 수 있도록 (js와 같은곳에서 가져갈 수 없도록)

        return cookie;
    }
	
}
