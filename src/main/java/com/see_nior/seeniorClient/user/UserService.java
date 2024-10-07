package com.see_nior.seeniorClient.user;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.dto.UserAccoountDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
	
	
	// 회원 가입 확인
	public int signUpConfirm(UserAccoountDto userAccoountDto) {
		log.info("signUpConfirm()");
		
		return 0;
	}

	// 아이디 중복 확인
	public boolean isAccount(String u_id) {
		log.info("isAccount()");
		
		return false;
	}

}
