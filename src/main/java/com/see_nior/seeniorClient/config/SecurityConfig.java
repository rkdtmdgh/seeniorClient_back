package com.see_nior.seeniorClient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean PasswordEncoder passwordEncoder() {
		log.info("passwordEncoder()");
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
		log.info("clientFilterChain()");
		
		http
			.cors(cors -> cors.disable())
			.csrf(csrf -> csrf.disable());
		
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
							HttpMethod.OPTIONS, "/**"
							).permitAll()
					.requestMatchers(
							"/**",
							"/",
							"/user/sign_up_confirm",
							"/user/sign_in_confirm",
							"/user/is_account",
							"/user/is_nickname"
							).permitAll()
					.anyRequest().authenticated());
		
		http
			.formLogin(login -> login
					.loginProcessingUrl("/user/sign_in_confirm")
					.usernameParameter("u_id")
					.passwordParameter("u_pw")
					.defaultSuccessUrl("/") 				// 로그인 성공 시 url
					.failureUrl("/user/sign_in_error")		// 로그인 실패 시 url 
					.permitAll());
		
		http
			.logout(logout -> logout
					.logoutUrl("/user/sign_out_confirm")
					.logoutSuccessUrl("/user/sign_out_success")		// 로그아웃 성공 시 url 
					.invalidateHttpSession(true)					// 세션 무효화
					.permitAll());
		
		http
			.sessionManagement(sess -> sess
				.maximumSessions(1)		
				.maxSessionsPreventsLogin(false))
			.sessionManagement(sess -> sess
				.sessionFixation().newSession());
		
		return http.build();
	}
	
}
