package com.see_nior.seeniorClient.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.see_nior.seeniorClient.user.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("attemptAuthentication() --- {}", request.getServletPath());
		
		// 클라이언트 요청에서 id, pw 추출
		String u_id = request.getParameter("u_id");
		String u_pw = request.getParameter("u_pw");
		
		// 스프링 시큐리티에서 u_id과 u_pw를 검증하기 위해서는 token에 담아야 함
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(u_id, u_pw);
		
		//token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
	}
	
	// 로그인 성공 시 
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		log.info("login success");
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
	
		String u_id = customUserDetails.getUsername();
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority authority = iterator.next();
		
		String role = authority.getAuthority();
		
		String token = jwtUtil.createJwt(u_id, role, 60*60*10L);
		
		response.addHeader("Authorization", "Bearer " + token);
	}
	
	// 로그인 실패 시 
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info("login fail");
		
		response.setStatus(401);
	}
	
}
