package com.see_nior.seeniorClient.user;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserAccountDetailsService implements UserDetailsService {

	final private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername() ----- {}", username);
		
		UserAccountDto userAccountDto = userMapper.selectUserAccountById(username);
		log.info("userAccountDto ------- {}", userAccountDto);
		
		if (userAccountDto == null) {
			throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
		}
		
		if (!userAccountDto.isU_is_blocked()) {
			throw new DisabledException("해당 계정은 차단되었습니다.");
		}
		
		return new UserAccountDetails(userAccountDto);
		
	}

}
