package com.see_nior.seeniorClient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;
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
		
		http
			.cors(cors -> cors.disable())
			.csrf(csrf -> csrf.disable());
		
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
							"/",
							"/css/**",
							"/js/**",
							"/quillEditor/**",
							"/error/**",
							"/image/**",
							"/user/sign_up_form",
							"/user/sign_up_confirm",
							"/user/sign_in_form",
							"/user/sign_in_confirm",
							"/user/sign_in_result/**",
							"/user/is_account"
							).permitAll()
					.anyRequest().authenticated());
		
		http
			.formLogin(login -> login
					.loginPage("/user/sign_in_form")
					.loginProcessingUrl("/user/sign_in_confirm")
					.usernameParameter("u_id")
					.passwordParameter("u_pw")
					.successHandler((request, response, authentication) -> {
						log.info("user sign in successHanleder");
						
						response.sendRedirect("/user/sign_in_result?result=" + true);
						
					})
					.failureHandler((request, response, exception) -> {
						log.info("user sign in failureHandler");
						
						response.sendRedirect("/user/sign_in_result?result=" + false);
						
					})
					);
		
		http
			.logout(logout -> logout
					.logoutUrl("/user/sign_out_confirm")
					.logoutSuccessHandler((request, response, authentication) -> {
						log.info("logoutSuccessHandler()");
						
						HttpSession session = request.getSession(false);
						
						if (session != null) session.invalidate();
						
						response.sendRedirect("/");
						
					}));
		
		http
			.sessionManagement(sess -> sess
				.maximumSessions(1)		
				.maxSessionsPreventsLogin(false))
			.sessionManagement(sess -> sess
				.sessionFixation().newSession());
		
		return http.build();
	}
	
}
