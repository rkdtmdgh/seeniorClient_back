package com.see_nior.seeniorClient.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;

    // TTL 설정 X
//    public void setValues(String key, String data) {
//        ValueOperations<String, Object> values = redisTemplate.opsForValue();
//        values.set(key, data);
//    }

    // refrshToken TTL 설정 O
    public void setValues(String key, String data, Duration duration) {
    	log.info("setValues()");
    	
    	redisTemplate.delete(key);
    	
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    // redis에 저장된 refreshToken 삭제
    public void deleteValues(String key) {
    	log.info("deleteValues()");
    	
        redisTemplate.delete(key);
    }

    // redis에 저장된 refreshToken 조회
    public String getValues(String key) {
    	log.info("getValues()");
    	
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return (String) values.get(key);
    }

    public boolean checkExistsValue(String value) {
    	log.info("checkExistsValue()");
    	
        return value == null;
    }
    
}
