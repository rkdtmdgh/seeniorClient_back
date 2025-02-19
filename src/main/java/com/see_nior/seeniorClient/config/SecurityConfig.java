package com.see_nior.seeniorClient.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.see_nior.seeniorClient.jwt.JwtFilter;
import com.see_nior.seeniorClient.jwt.JwtUtil;
import com.see_nior.seeniorClient.jwt.LoginFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	// AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
	
	@Bean PasswordEncoder passwordEncoder() {
		log.info("passwordEncoder()");
		
		return new BCryptPasswordEncoder();
	}
	
	// AuthenticationManager Bean 등록
	@Bean AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
	}
	
	@Bean SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
		log.info("clientFilterChain()");
		
		http
			.cors(cors -> cors
					.configurationSource(new CorsConfigurationSource() {
						
						@Override
						public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
							
							CorsConfiguration configuration = new CorsConfiguration();
							
							configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
		                    configuration.setAllowedMethods(Collections.singletonList("*"));
		                    configuration.setAllowCredentials(true);
		                    configuration.setAllowedHeaders(Collections.singletonList("*"));
		                    configuration.setMaxAge(3600L);
		                    
							configuration.setExposedHeaders(Collections.singletonList("Authorization"));
							
							return configuration;
						}
					}));
			
		http
			.csrf(csrf -> csrf.disable());
		
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
							"/",
							"/login", 
							"/user/sign_up_confirm",
							"/user/is_account",
							"/user/is_nickname"
							).permitAll()
					.anyRequest().authenticated());
		
//		http
//			.formLogin(login -> login
//					.loginProcessingUrl("/user/sign_in_confirm")
//					.usernameParameter("u_id")
//					.passwordParameter("u_pw")
//					.successHandler(new LoginSuccessHandler())
//					.failureHandler(new LoginFailureHandler())
//					.permitAll());
		
		http
			.formLogin(login -> login.disable());
		
		http
        	.httpBasic((auth) -> auth.disable());
		
//		http
//			.oauth2Login((oauth2) -> oauth2
//					.userInfoEndpoint());
		
//		http
//			.logout(logout -> logout
//					.logoutUrl("/user/sign_out_confirm")
//					.logoutSuccessHandler((request, response, authentication) -> {
//						log.info("sign_out_confirm success ----- {}", authentication.getName()); 
//						
//						response.setStatus(HttpServletResponse.SC_OK);
//						response.setContentType("application/json;charset=UTF-8");
//						response.getWriter().write("{\"signOutResult\": true}");
//						
//					})
//					.invalidateHttpSession(true)		// 세션 무효화
//					.permitAll());
		
//		http
//			.sessionManagement(sess -> sess
//				.maximumSessions(1)		
//				.maxSessionsPreventsLogin(false))
//			.sessionManagement(sess -> sess
//				.sessionFixation().newSession());
		
		//JWTFilter 등록
        http
            .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
		
		// 필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
        	.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		http
        	.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
	
//	@Bean CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.addAllowedOrigin("*");
//		configuration.addAllowedMethod("*");
//		configuration.addAllowedHeader("*");
//		configuration.setAllowCredentials(true);
//		
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		
//		return source;
//	}
	
}
