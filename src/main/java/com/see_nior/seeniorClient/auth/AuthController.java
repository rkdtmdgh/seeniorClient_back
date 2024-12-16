package com.see_nior.seeniorClient.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

	@GetMapping("/check")
	public ResponseEntity<String> checkAuthentication(Authentication authentication) {
		log.info("checkAuthentication() ----- {}", authentication.getName());
	
		if (authentication != null && authentication.isAuthenticated()) {
			return ResponseEntity.ok("Logged in");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
		}
		
	}
	
}
