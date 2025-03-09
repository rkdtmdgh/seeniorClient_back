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
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.see_nior.seeniorClient.OAuth.CustomOAtuth2UserService;
import com.see_nior.seeniorClient.OAuth.CustomOAuth2FailureHandler;
import com.see_nior.seeniorClient.OAuth.CustomOAuth2SuccessHandler;
import com.see_nior.seeniorClient.jwt.JwtFilter;
import com.see_nior.seeniorClient.jwt.JwtUtil;
import com.see_nior.seeniorClient.redis.RedisService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	// AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final CustomOAtuth2UserService custumOAtuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    
    @Bean HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
    	return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
    
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
	                    
	                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
						configuration.setExposedHeaders(Collections.singletonList("access"));
						
						return configuration;
					}
				}));

		http
			.csrf(csrf -> csrf.disable());

		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
						"/", 
						"/reissue", 
						"/login", 
						"/user/sign_up_confirm",
						"/user/is_account",
						"/user/is_nickname", 
						"/disease/cate_info/get_category_list_select",
						"/disease/info/get_disease_list_by_category_select",
						"/disease/info/search_disease_list", 
						"/advertisement/main/get_advertisement_list"
						).permitAll()
				.anyRequest().authenticated());

		http
			.formLogin(login -> login
				.loginProcessingUrl("/login")
				.usernameParameter("u_id")
				.passwordParameter("u_pw")
				.successHandler(new CustomLoginSuccessHandler(jwtUtil, redisService))
				.failureHandler(new CustomLoginFailureHandler())
				.permitAll());

		http
        	.httpBasic((auth) -> auth.disable());

        //JWTFilter 등록
        http
        	.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		// oauth2
        http
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(custumOAtuth2UserService))
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler)
            );

		http
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessHandler(new CustomLogoutSuccessHandler(jwtUtil, redisService))
				.invalidateHttpSession(false)
				.permitAll());
		
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
