package com.see_nior.seeniorClient.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.see_nior.seeniorClient.dto.UserAccountDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	// 회원 가입 양식
	@GetMapping("/sign_up_form")
	public String signUpForm() {
		log.info("signUpForm()");
		
		String nextPage = "user/sign_up_form";
		
		return nextPage;
	}
	
	// 회원 가입 확인
	@PostMapping("/sign_up_confirm")
	public String signUpConfirm(UserAccountDto userAccountDto, Model model) {
		log.info("signUpConfirm()");
		
		String signUpResult = userService.signUpConfirm(userAccountDto);
		
		model.addAttribute("signUpResult", signUpResult);
		
		String nextPage = "user/sign_up_result";
		
		return nextPage;
	}
	
	// 아이디 중복 여부 확인 (비동기)
	@GetMapping("/is_account")
	public Object isAccount(@RequestParam("u_id") String u_id) {
		log.info("isAccount()");
		
		boolean result = userService.isAccount(u_id);
		
		return result;
	}
	
	// 로그인 양식
	@GetMapping("/sign_in_form")
	public String signInForm() {
		log.info("signInForm()");
		
		String nextPage = "user/sign_in_form";
		
		return nextPage;
	}
	
	// 로그인 결과 확인
	@GetMapping("/sign_in_result")
	public String signInResult(Model model) {
		log.info("signInResult()");
		
		String nextPage = "user/sign_in_result";
		
		return nextPage;
	}
	
	
	// 내 정보 수정 양식
	
	// 내 정보 가져오기 (비동기)
	
	// 내 정보 수정 양식 가기 전 비밀번호 확인 (비동기)

	// 내 정보 수정 확인 (비동기)
	
	// 회원 탈퇴 확인 (비동기)
	
	// 비밀번호 초기화 (비동기)
	
	
}
