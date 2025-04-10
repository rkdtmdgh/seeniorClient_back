package com.see_nior.seeniorClient.user;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
	
	// 정보 수정 확인 -- 기존 프로필 이미지 삭제
	public boolean delImgModifyConfirm(UserAccountDto userAccountDto) {
		log.info("delImgModifyConfirm() ---- {}", userAccountDto.getU_no());
		
		String del_u_img_dir_name = "\\userProfileImg\\" + userAccountDto.getU_no() + "\\" + userAccountDto.getU_img_dir_name();
		log.info("delImgModifyConfirm() 삭제할 파일 경로 ---- {}", del_u_img_dir_name);
		
		userAccountDto.setU_profile_img(null);
		userAccountDto.setU_img_dir_name(null);
		
		boolean modifyResult = userMapper.updateUserAccount(userAccountDto);
		
		// DB 업데이트 실패 시
		if(!modifyResult) 
			return SqlResult.FAIL.getValue();
		
		// DB 업데이트 이후 기존 프로필 이미지 삭제
		ResponseEntity<String> deleteFolderResult = 
				deleteImgFolder(del_u_img_dir_name);
		
		if (deleteFolderResult.getBody().equals("1")) {
			log.info("profile img deleted success");
			
			return SqlResult.SUCCESS.getValue();
		} else {
			log.info("profile img deleted fail");
			
			// 프로필 이미지 삭제 실패 테이블 업데이트
			userMapper.imgDeleteFail(del_u_img_dir_name);
			
			return SqlResult.SUCCESS.getValue();
		}
		
	}

	// 정보 수정 확인
	@SuppressWarnings("unchecked")
	public boolean fileUploadAndModifyConfirm(List<MultipartFile> files, UserAccountDto userAccountDto) {
		log.info("fileUploadAndModifyConfirm() ------- {}", userAccountDto.getU_name());
		
		try {
			
			// 기존 이미지 파일 경로 (기존 이미지 저장 파일 삭제 시 사용)
			String del_u_img_dir_name = userAccountDto.getU_img_dir_name();
			String del_dir_path = "\\userProfileImg\\" + userAccountDto.getU_no() + "\\" +  del_u_img_dir_name;
			log.info("fileUploadAndModifyConfirm() 기존 이미지 파일 경로 --- {}", del_u_img_dir_name);
			
			// 이미지 파일 저장 경로
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = dateFormat.format(now);
			
			String filePath = "\\userProfileImg\\" + userAccountDto.getU_no() + "\\" + date;
			
			// 이미지 저장 요청
			ResponseEntity<String> savedFile = 
					imageFileService.uploadFiles(files, filePath);
			
			// 이미지 서버에 저장 실패
			if (savedFile == null || !savedFile.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("uploadFile fail");
			}
			
			log.info("uploadFile success");
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			Map<String, Object> savedFileObj = 
					objectMapper.readValue(savedFile.getBody() , new TypeReference<Map<String, Object>>() {});
			
			String savedFileName;
			
			try {
				savedFileName = ((List<String>) savedFileObj.get("savedFileNames")).get(0);
			} catch (Exception e) {
				throw new RuntimeException("Invalid response from image server", e);
			}
			
			userAccountDto.setU_profile_img(savedFileName);
			userAccountDto.setU_img_dir_name(date);
			
			// 새로운 이미지 파일 이름 & 경로 추가해서 DB 업데이트
			boolean updateImgResult = userMapper.updateUserAccount(userAccountDto); 
			
			// DB 업데이트 실패 시 
			if (!updateImgResult) {
				log.info("updateUserAccount --- fail");
				
				// 새로 업로드한 이미지 삭제
				ResponseEntity<String> deleteFolderResult = 
						deleteImgFolder(filePath);
				
				if (deleteFolderResult.getBody().equals("1")) {
					log.info("profile img folder delete success");
					
					return SqlResult.FAIL.getValue();
				} else {
					log.info("profile img folder delete fail");
					// 프로필 이미지 삭제 실패 테이블 업데이트
					boolean imgDeleteFailResult = userMapper.imgDeleteFail(filePath);
					
					if (!imgDeleteFailResult) 
						log.info("userMapper.imgDeleteFail ----- fail ");
					
					return SqlResult.FAIL.getValue();
				}
				
			}
			
			// 기존 이미지가 있다면 삭제
			if (del_u_img_dir_name != null && !del_u_img_dir_name.isBlank()) {
				
				ResponseEntity<String> deleteFolderResult = 
						deleteImgFolder(del_dir_path);
				
				if (deleteFolderResult.getBody().equals("1")) {
					log.info("profile img folder delete success");
					
					return SqlResult.SUCCESS.getValue();
				} else {
					log.info("profile img folder delete fail");
					
					// 프로필 이미지 삭제 실패 테이블 업데이트
					userMapper.imgDeleteFail(del_u_img_dir_name);
					
					return SqlResult.SUCCESS.getValue();
				}
			}
			
			return SqlResult.SUCCESS.getValue();
			
		} catch (Exception e) {
			log.info("modifyConfirm() error ------ {}", e);

			return SqlResult.FAIL.getValue();
		}
		
	}
	
	public boolean modifyConfirm(UserAccountDto userAccountDto) {
		log.info("modifyConfirm() ----- {}", userAccountDto.getU_id());
		
		return userMapper.updateUserAccount(userAccountDto);
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
		
		Map<String, Object> param = new HashMap<>();
		param.put("u_id", u_id);
		param.put("u_pw", passwordEncoder.encode(u_pw));
		
		return userMapper.updateUserPw(param);
	}
	
	// u_social_id 존재 확인
	public boolean isSocialId(String u_social_id) {
		log.info("isSocialId() ---- {}", u_social_id);
		
		return userMapper.isSocialId(u_social_id);
	}

	public boolean deleteConfirm(String u_id) {
		log.info("deleteConfirm() --- {}", u_id);
		
		return userMapper.deleteUserAccountById(u_id);
	}
	
	public ResponseEntity<String> deleteImgFolder(String folderPath) {
		log.info("deleteProfileImgFile() ----- {}", folderPath);
		
		List<String> deleteFolderPath = new ArrayList<>();
		deleteFolderPath.add(folderPath);
		
		return imageFileService.deleteFolders(deleteFolderPath);
	}

	public boolean isNicknameMod(UserAccountDto userAccountDto) {
		log.info("isNicknameMod()");
		
		return userMapper.isNicknameMod(userAccountDto);
	}
	
}
