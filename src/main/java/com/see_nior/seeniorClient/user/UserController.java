package com.see_nior.seeniorClient.user;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.enums.ImgUrlPath;

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
		if (userAccountDto.getU_social_id() == null || userAccountDto.getU_social_id().equals("")) 
			return userService.signUpConfirm(userAccountDto);
		else 
			return userService.oauth2SignUpConfirm(userAccountDto);
		
	}
	
	// 아이디 중복 여부 확인
	@GetMapping("/is_account")
	public boolean isAccount(@RequestParam String u_id) {
		log.info("isAccount()");
		
		return userService.isAccount(u_id);
	}
	
	// 닉네임 중복 여부 확인
	@GetMapping("/is_nickname")
	@ResponseBody
	public boolean isNickname(@RequestParam String u_nickname) {
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
		
		Map<String, Object> responseMap = new HashMap<>();
		
		responseMap.put("userAccountDto", userAccountDto);
		responseMap.put("userProfileImgServerPath", ImgUrlPath.USER_PROFILE_PATH.getValue());
		
		return responseMap;
	}
	
	// 내 정보 수정 확인 
	@PostMapping("/modify_confirm")
	public boolean modifyConfirm(@RequestBody UserAccountDto userAccountDto, 
			@RequestParam(required = false) List<MultipartFile> files, 
			@RequestParam boolean deleted_profile) {
		log.info("modifyConfirm()");
		
		if (deleted_profile) {
			return userService.delImgModifyConfirm(userAccountDto);
		}

		if (files == null || files.isEmpty()) {
			return userService.modifyConfirm(userAccountDto);
		}
		
		return userService.fileUploadAndModifyConfirm(files, userAccountDto);
	}
	
	
	// 회원 탈퇴 확인 
	@PostMapping("/delete_confirm")
	public boolean deleteConfirm(Principal principal) {
		log.info("deleteConfirm()");
		
		return userService.deleteConfirm(principal.getName());
	}
	
	// 비밀번호 변경 전 비밀 번호 확인 
	@PostMapping("/check_pw")
	public boolean checkPw(@RequestParam String u_pw, Principal principal) {
		log.info("checkPw()");
		
		return userService.checkPw(principal.getName(), u_pw);
	}
	
	// 비밀번호 변경
	@PostMapping("/modify_pw_confirm")
	public boolean modifyPwConfirm(@RequestParam String u_pw, Principal principal) {
		log.info("modifyPwConfirm()");
		
		return userService.modifyPwConfirm(u_pw, principal.getName());
	}
	
	// id & profileImgPath 가져오기
	@GetMapping("/get_profile")
	public Object getProfile(Principal principal) {
		log.info("getProfile() -------- {}", principal.getName());
		
		UserAccountDto userAccountDto = 
				userService.getAccountInfoById(principal.getName());
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("u_no", userAccountDto.getU_no());
		responseMap.put("u_id", userAccountDto.getU_id());
		responseMap.put("u_img_dir_name", userAccountDto.getU_img_dir_name());
		responseMap.put("u_profile_img", userAccountDto.getU_profile_img());
		responseMap.put("userProfileImgServerPath", ImgUrlPath.USER_PROFILE_PATH.getValue());
		
		return responseMap;
	}
	
	
}
