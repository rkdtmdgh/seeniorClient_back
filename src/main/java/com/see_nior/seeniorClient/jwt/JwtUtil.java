package com.see_nior.seeniorClient.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtUtil {

	private SecretKey secretKey;
	
	public JwtUtil(@Value("${spring.jwt.secret}")String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
	
	public String getU_id(String token) {

        return Jwts
        		.parser()
        		.verifyWith(secretKey)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload()
        		.get("u_id", String.class);
    }
	
	public String getRole(String token) {

        return Jwts.parser()
        		.verifyWith(secretKey)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload()
        		.get("role", String.class);
    }
	
	public Boolean isExpired(String token) {

        return Jwts
        		.parser()
        		.clockSkewSeconds(60)		// 60초 차이 허용
        		.verifyWith(secretKey)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload()
        		.getExpiration()
        		.before(new Date());
    }
	
	public String createJwt(String u_id, String role, Long expiredMs) {
		log.info("createJwt() --------{}", u_id);
		log.info("createJwt() --------{}", role);
		log.info("createJwt() --------{}", expiredMs);
		
        return Jwts.builder()
                .claim("u_id", u_id)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
	
}
