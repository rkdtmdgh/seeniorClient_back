package com.see_nior.seeniorClient.reissue;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorClient.jwt.JwtUtil;
import com.see_nior.seeniorClient.redis.RedisService;
import com.see_nior.seeniorClient.util.CookieUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {

	private final JwtUtil jwtUtil;
	private final RedisService redisService;
	
	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
		log.info("reissue()");
		
		// 쿠키에서 refresh token 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
		
        // 검증 시작
        // refresh token이 없는 경우
        if (refresh == null) {
        	log.info("refreshToken is null");
        	
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }
        
        // 유효시간 확인
        try {
        	if(jwtUtil.isExpired(refresh)) {
        		log.info("refreshToken is expired");
        		
            	return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);	
            }
        } catch (ExpiredJwtException e) {
        	log.info("reissue error");
        	
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }
		
        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String u_id = jwtUtil.getU_id(refresh);
        String role = jwtUtil.getRole(refresh);

        // Redis내에 존재하는 refreshToken인지 확인
        String redisRefreshToken = redisService.getValues(u_id);
        if(redisService.checkExistsValue(redisRefreshToken)) {
            return new ResponseEntity<>("no exists in redis refresh token", HttpStatus.BAD_REQUEST);
        }
        
        // 받은 refreshToken과 Redis에 저장된 refreshToken 비교
        if (!redisRefreshToken.equals(refresh)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        
        // make new JWT
        String newAccessToken = jwtUtil.createJwt("access", u_id, role, 10 * 60 * 1000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", u_id, role, 24 * 60 * 60 * 1000L);

        // update refreshToken to Redis
        redisService.setValues(u_id, newRefreshToken, Duration.ofMillis(24 * 60 * 60 * 1000L));
        
        // response
        response.setHeader("access", "Bearer " + newAccessToken);
        response.addCookie(CookieUtil.createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
	}
}
