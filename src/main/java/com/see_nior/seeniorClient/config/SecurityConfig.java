package com.see_nior.seeniorClient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
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
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable());
		
		http
			.authorizeHttpRequests(auth -> auth
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
					.successHandler(new LoginSuccessHandler())
					.failureHandler(new LoginFailureHandler())
					.permitAll());
		
		http
			.logout(logout -> logout
					.logoutUrl("/user/sign_out_confirm")
					.logoutSuccessHandler((request, response, authentication) -> {
						log.info("sign_out_confirm success ----- {}", authentication.getName()); 
						
						response.setStatus(HttpServletResponse.SC_OK);
						response.setContentType("application/json;charset=UTF-8");
						response.getWriter().write("{\"signOutResult\": true}");
						
					})
					.invalidateHttpSession(true)		// 세션 무효화
					.permitAll());
		
		http
			.sessionManagement(sess -> sess
				.maximumSessions(1)		
				.maxSessionsPreventsLogin(false))
			.sessionManagement(sess -> sess
				.sessionFixation().newSession());
		
		return http.build();
	}
	
	@Bean CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:3000");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
	
}
