package com.see_nior.seeniorClient.user;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.enums.SqlResult;
import com.see_nior.seeniorClient.user.mapper.UserMapper;
import com.see_nior.seeniorClient.util.ImageFileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	private final ImageFileService imageFileService;
	
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
	private static final SecureRandom RANDOM = new SecureRandom();
	
	public static String generateRandomPassword(int length) {
		StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
	}
	
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
	
	public Object oauth2SignUpConfirm(UserAccountDto userAccountDto) {
		log.info("oauth2SignUpConfirm() ----- {}", userAccountDto.getU_social_id());
		
		String u_pw = generateRandomPassword(10);
		
		userAccountDto.setU_id(userAccountDto.getU_social_id());
		userAccountDto.setU_pw(passwordEncoder.encode(u_pw));
		
		Map<String, Object> resultMap = new HashMap<>();
		
		// 닉네임 중복 검사
		boolean isNickname =
				userMapper.isNickname(userAccountDto.getU_nickname());
		
		if (isNickname) {
			resultMap.put("result", SqlResult.FAIL.getValue());
			resultMap.put("reason", "u_nickname");
			return resultMap;
		}
		
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

	// 내 정보 가져오기 (전체 정보) by u_id 
	public UserAccountDto getAccountInfoById(String u_id) {
		log.info("getAccountInfoById()");
		
		return userMapper.selectUserAccountById(u_id);
	}
	
	// 내 정보 가져오기 (전체 정보) by u_social_id 
	public UserAccountDto getAccountInfoBySocialId(String u_social_id) {
		log.info("getAccountInfoById()");
		
		return userMapper.selectUserAccountBySocialId(u_social_id);
	}
	
	// 내 u_no 가져오기 by u_id
	public int selectUserNoById(String u_id) {
		log.info("selectUserNoById()");
		
		return userMapper.selectUserNoById(u_id);
	}

	// u_nickname 중복 확인 
	public boolean isNickname(String u_nickname) {
		log.info("isNickname()");
		
		return userMapper.isNickname(u_nickname);
	}

	// 정보 수정 확인
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean modifyConfirm(List<MultipartFile> files, UserAccountDto userAccountDto) {
		log.info("modifyConfirm() ------- {}", userAccountDto.getU_id());
		
		try {
			
			// 닉네임 중복검사
			boolean result = 
					userMapper.isNickname(userAccountDto.getU_nickname());
			
			if (result) {
				return SqlResult.FAIL.getValue();
			}
			
			boolean modifyResult = userMapper.updateUserAccount(userAccountDto);
			
			if(!modifyResult) 
				throw new RuntimeException("updateUserAccount fail");
			
			// 프로필 이미지가 없는 경우
			if (files == null || files.isEmpty()) {
				
				return modifyResult;
			}
			
			// 프로필 이미지가 있는 경우
			// 이미지 파일 저장 경로
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = dateFormat.format(now);
			
			String filePath = "\\user\\" + userAccountDto.getU_no() + "\\" + date;
			
			// 이미지 저장 요청
			ResponseEntity<String> savedFile = imageFileService.uploadFiles(files, filePath);
			
			// 이미지 서버에 저장 실패
			if (savedFile == null) {
				throw new RuntimeException("uploadFile fail");
			}
			
			log.info("uploadFile success");
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody() , new TypeReference<Map<String, Object>>() {} );
			String savedFileName = ((List<String>) savedFileObj.get("savedFileNames")).get(0);
			
			userAccountDto.setU_profile_img(savedFileName);
			userAccountDto.setU_img_dir_name(filePath);
			
			boolean updateImgResult = userMapper.updateUserAccountProfileImg(userAccountDto); 
			
			if (!updateImgResult) 
				throw new RuntimeException("updateUserAccountProfileImg fail");
			
			return SqlResult.SUCCESS.getValue();
			
		} catch (Exception e) {
			log.info("modifyConfirm() error ------ {}", e.getMessage());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return SqlResult.FAIL.getValue();
		}
		
	}

	// 비밀번호 확인
	public boolean checkPw(String u_id, String u_pw) {
		log.info("checkPw()");
		
		UserAccountDto userAccountDto = 
				userMapper.selectUserAccountById(u_id);
		
		return passwordEncoder.matches(u_pw, userAccountDto.getU_pw());
	}

	// 비밀번호 변경 확인
	public boolean modifyPwConfirm(String u_pw, String u_id) {
		log.info("modifyPwConfirm()");
		
		return userMapper.updateUserPw(u_id, passwordEncoder.encode(u_pw));
	}
	
	// u_social_id 존재 확인
	public boolean isSocialId(String u_social_id) {
		log.info("isSocialId() ---- {}", u_social_id);
		
		return userMapper.isSocialId(u_social_id);
	}

}
