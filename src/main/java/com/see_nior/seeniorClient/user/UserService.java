package com.see_nior.seeniorClient.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.enums.SqlResult;
import com.see_nior.seeniorClient.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
	final static public boolean USER_SIGN_UP_SUCCESS = true;
	final static public boolean USER_SIGN_UP_FAIL = false;
	
	// 회원 가입 확인
	public boolean signUpConfirm(UserAccountDto userAccountDto) {
		log.info("signUpConfirm()");
		
		// 아이디 중복 검사
		boolean isAccount = 
				userMapper.isAccount(userAccountDto.getU_id());
		
		if (!isAccount) {
			
			userAccountDto.setU_pw(passwordEncoder.encode(userAccountDto.getU_pw()));
			
			int signUpResult = userMapper.insertNewUser(userAccountDto);
			
			if (signUpResult >= 0) 
				return SqlResult.SUCCESS.getValue();
			else 
				return SqlResult.FAIL.getValue();
			
		} else {
			
			return SqlResult.FAIL.getValue();
			
		}
	}

	// 아이디 중복 확인
	public boolean isAccount(String u_id) {
		log.info("isAccount()");
		
		return userMapper.isAccount(u_id);
	}

	// 내 정보 가져오기
	public UserAccountDto getAccountInfoById(String u_id) {
		log.info("getAccountInfoById()");
		
		return userMapper.selectUserAccountById(u_id);
	}

}
