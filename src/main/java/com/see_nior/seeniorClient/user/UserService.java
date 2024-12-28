package com.see_nior.seeniorClient.user;

import java.util.HashMap;
import java.util.Map;

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
	
	// 회원 가입 확인
	public Object signUpConfirm(UserAccountDto userAccountDto) {
		log.info("signUpConfirm()");
		
		// return 
		// result = boolean
		// reason = u_id, u_nickname, fail
		
		Map<String, Object> resultMap = new HashMap<>();
		
		// 아이디 중복 검사
		boolean isAccount = 
				userMapper.isAccount(userAccountDto.getU_id());
		
		if (isAccount) {
			resultMap.put("result", SqlResult.FAIL.getValue());
			resultMap.put("reason", "u_id");
			return resultMap;
		}
		
		// 닉네임 중복 검사
		boolean isNickname =
				userMapper.isNickname(userAccountDto.getU_nickname());
		
		if (isNickname) {
			resultMap.put("result", SqlResult.FAIL.getValue());
			resultMap.put("reason", "u_nickname");
			return resultMap;
		}
		
		userAccountDto.setU_pw(passwordEncoder.encode(userAccountDto.getU_pw()));
		
		int signUpResult = userMapper.insertNewUser(userAccountDto);
		
		if (signUpResult >= 0) {
			resultMap.put("result", SqlResult.SUCCESS.getValue());
			return resultMap;
		} else {
			resultMap.put("result", SqlResult.FAIL.getValue());
			resultMap.put("reason", "fail");
			return resultMap;
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

	// u_nickname 중복 확인 
	public boolean isNickname(String u_nickname) {
		log.info("isNickname()");
		
		return userMapper.isNickname(u_nickname);
	}

	// 정보 수정 확인
	public boolean modifyConfirm(UserAccountDto userAccountDto) {
		log.info("modifyConfirm() ------- {}", userAccountDto.getU_id());
		
		boolean result = 
				userMapper.isNickname(userAccountDto.getU_nickname());
		
		if (result) {
			return SqlResult.FAIL.getValue();
		}
		
		boolean modifyResult = userMapper.updateUserAccount(userAccountDto);
		
		return modifyResult;
	}

}
