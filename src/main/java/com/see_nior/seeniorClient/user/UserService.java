package com.see_nior.seeniorClient.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.entity.UserAccountEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	// 회원 가입 확인
	public String signUpConfirm(UserAccountDto userAccountDto) {
		log.info("signUpConfirm()");
		
		UserAccountEntity saveEntity = UserAccountEntity.builder()
				.u_id(userAccountDto.getU_id())
				.u_pw(passwordEncoder.encode(userAccountDto.getU_pw()))
				.u_name(userAccountDto.getU_name())
				.u_phone(userAccountDto.getU_phone())
				.u_nickname(userAccountDto.getU_nickname())
				.u_gender(userAccountDto.getU_gender())
				.u_birth(userAccountDto.getU_birth())
				.u_address(userAccountDto.getU_address())
				.build();
		
		UserAccountEntity userAccountEntity = userRepository.save(saveEntity);
		
		return userAccountEntity.getU_id();
	}

	// 아이디 중복 확인
	public boolean isAccount(String u_id) {
		log.info("isAccount()");
		
		return false;
	}

}
