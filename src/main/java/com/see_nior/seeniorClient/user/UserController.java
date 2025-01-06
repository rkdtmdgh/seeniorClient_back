package com.see_nior.seeniorClient.user;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.see_nior.seeniorClient.dto.UserAccountDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	// 신규 회원 가입
	@PostMapping("/sign_up_confirm")
	public Object signUpConfirm(@RequestBody UserAccountDto userAccountDto) {
		log.info("signUpConfirm()");
		
		// 회원 가입 성공 return = true, 실패 or 아이디 중복 return = false
		return userService.signUpConfirm(userAccountDto);
	}
	
	// 아이디 중복 여부 확인
	@GetMapping("/is_account")
	public boolean isAccount(@RequestParam("u_id") String u_id) {
		log.info("isAccount()");
		
		return userService.isAccount(u_id);
	}
	
	// 닉네임 중복 여부 확인
	@GetMapping("/is_nickname")
	@ResponseBody
	public boolean isNickname(@RequestParam("u_nickname") String u_nickname) {
		log.info("isNickname()");
		
		return userService.isNickname(u_nickname);
	}

	// 내 정보 가져오기 
	@GetMapping("/get_account_info")
	public Object getAccountInfo(Principal principal) {
		log.info("getAccountInfo()");
	
		UserAccountDto userAccountDto = 
				userService.getAccountInfoById(principal.getName());
		
		userAccountDto.setU_pw("");
		
		return userAccountDto;
	}
	
	// 내 정보 수정 확인 
	@PostMapping("/modify_confirm")
	public boolean modifyConfirm(UserAccountDto userAccountDto) {
		log.info("modifyConfirm()");
		
		return userService.modifyConfirm(userAccountDto);
	}
	
	
	// 회원 탈퇴 확인 
	@PostMapping("/delete_confirm")
	public boolean deleteConfirm() {
		log.info("deleteConfirm()");
		
		return false;
	}
	
	// 비밀번호 변경 전 비밀 번호 확인 
	@PostMapping("/check_pw")
	public boolean checkPw(@RequestParam("u_pw") String u_pw, Principal principal) {
		log.info("checkPw()");
		
		return userService.checkPw(principal.getName(), u_pw);
	}
	
	// 비밀번호 변경
	@PostMapping("/modify_pw_confirm")
	public boolean modifyPwConfirm(@RequestParam("u_pw") String u_pw, Principal principal) {
		log.info("modifyPwConfirm()");
		
		return userService.modifyPwConfirm(u_pw, principal.getName());
	}
	
	
}
