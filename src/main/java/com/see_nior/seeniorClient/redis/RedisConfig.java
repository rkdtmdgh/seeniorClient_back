package com.see_nior.seeniorClient.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@EnableRedisRepositories
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
	
	// RedisProperties --- spring.data.redis.* 속성을 자동으로 바인딩하는 클래스 (application.properties에 있는 설정을 읽음)
	private final RedisProperties redisProperties;

	@Bean RedisConnectionFactory redisConnectionFactory() {
		
		// Redis를 단일 인스턴스(Standalone 모드)로 연결하기 위한 설정 객체
		RedisStandaloneConfiguration redisStandaloneConfiguration = 
				new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
		
		// Redis 연결 생성
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}
	
	@Bean RedisTemplate<String, Object> redisTemplate() {
		
		// RedisTemplate --- Redis에서 데이터를 저장하고 조회할 때 사용하는 Spring 제공 템플릿
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		// Key 값을 문자열(String)로 저장하도록 설정 (Redis의 기본 key-value는 byte[]이므로 변환 필요)
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// Value도 문자열(String)로 저장
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // 위에서 만든 redisConnectionFactory를 이용해 Redis와 연결
        redisTemplate.setConnectionFactory(redisConnectionFactory());
		
        return redisTemplate;
	}
	
}
