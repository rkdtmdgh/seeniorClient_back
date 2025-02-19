package com.see_nior.seeniorClient.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.user.CustomUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("doFilterInternal()");
		
		// 요청 헤더에서 access 를 찾음
		String accessToken = request.getHeader("access");
		
		// 요청 헤더에 access 가 없는 경우 
		if (accessToken == null) {
            log.info("token null");
            
            filterChain.doFilter(request, response);
            return;
        }
		
		// Bearer 제거 <- oAuth2를 이용했다고 명시적으로 붙여주는 타입인데 JWT를 검증하거나 정보를 추출 시 제거해줘야한다.
		String originToken = accessToken.substring(7);
		
		// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
		try {
			if (jwtUtil.isExpired(originToken)) {
			
			    PrintWriter writer = response.getWriter();
			    writer.print("access token expired");

			    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    return;
			}
		} catch (ExpiredJwtException e) {

		    PrintWriter writer = response.getWriter();
		    writer.print("access token expired");

		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
		}
		
		// accessToken인지 refreshToken인지 확인
		String category = jwtUtil.getCategory(originToken);
		
		// JWTFilter는 요청에 대해 accessToken만 취급하므로 access인지 확인
		if (!category.equals("access")) {

		    PrintWriter writer = response.getWriter();
		    writer.print("invalid access token");

		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
		}
        
		// 사용자명과 권한을 accessToken에서 추출
        String u_id = jwtUtil.getU_id(originToken);
        String role = jwtUtil.getRole(originToken);
        
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setU_id(u_id);
        userAccountDto.setU_authority_role(role);
        
        CustomUserDetails customUserDetails = 
        		new CustomUserDetails(userAccountDto);
        
        Authentication authentication = 
        		new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        filterChain.doFilter(request, response);
        
	}
	
}
