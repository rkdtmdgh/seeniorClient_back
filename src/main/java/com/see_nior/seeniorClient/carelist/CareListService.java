package com.see_nior.seeniorClient.carelist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorClient.carelist.mapper.CareListMapper;
import com.see_nior.seeniorClient.dto.CareListCategoryDto;
import com.see_nior.seeniorClient.dto.CareListDto;
import com.see_nior.seeniorClient.enums.SqlResult;
import com.see_nior.seeniorClient.user.UserService;
import com.see_nior.seeniorClient.util.CareListPagingUtil;
import com.see_nior.seeniorClient.util.ImageFileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CareListService {
	
	final private CareListMapper careListMapper;
	final private ImageFileService imageFileService;
	final private UserService userService;

/////////////////////////////////////////////////////// 케어리스트 카테고리	
	
	// 케어리스트 카테고리명 중복 확인
	public boolean isCareListCategory(String clc_name, String u_id) {
		log.info("isCareListCategory()");
		
		Map<String, Object> isCareListParams = new HashMap<>();
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		isCareListParams.put("clc_name", clc_name);
		isCareListParams.put("u_no", u_no);
		
		boolean isCareListCategory = careListMapper.isCareListCategory(isCareListParams);
		
		return isCareListCategory;
		
	}
	
/*
	// 케어리스트 카테고리 등록하기
	public boolean createCategoryConfirm(String clc_name, String u_id) {
		log.info("createCategoryConfirm()");
		
		// return
		// result = boolean
		// reason = limit
		
		Map<String, Object> insertParams = new HashMap<>();
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		insertParams.put("clc_name", clc_name);
		insertParams.put("u_no", u_no);
		
		int createCategoryResult = careListMapper.insertNewCareListCategory(insertParams);
		
		// DB에 입력 실패
		if (createCategoryResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
		
	}
*/
	
	// 케어리스트 카테고리 등록하기
	public Object createCategoryConfirm(String clc_name, String u_id) {
		log.info("createCategoryConfirm()");
		
		Map<String, Object> insertParams = new HashMap<>();
		
		// return
		// result = boolean
		// reason = limit
		Map<String, Object> resultMap = new HashMap<>();
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		// 로그인한 회원의 케어리스트 카테고리 개수 조회
		int careListCategoryListCnt = careListMapper.getAllCareListCategoryCnt(u_no);
		log.info("careListCategoryListCnt -------> {}", careListCategoryListCnt);
		
		if (careListCategoryListCnt >= 50) {
			resultMap.put("result", SqlResult.FAIL.getValue());
			resultMap.put("reason", "limit");
			return resultMap;
		} 
		
		insertParams.put("clc_name", clc_name);
		insertParams.put("u_no", u_no);
		
		int createCategoryResult = careListMapper.insertNewCareListCategory(insertParams);
		
		// DB에 입력 성공
		if (createCategoryResult >= 0) {
			resultMap.put("result", SqlResult.SUCCESS.getValue());
			return resultMap;
	
		} else {
			resultMap.put("result", SqlResult.FAIL.getValue());
			resultMap.put("reason", "fail");
			return resultMap;
			
		}
		
	}
	
	// 모든 케어리스트 카테고리 가져오기 (케어리스트에서 <select> 박스)
	public Map<String, Object> getCategoryList(String u_id) {
		log.info("getCategoryList()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		List<CareListCategoryDto> careListCategoryDtos = (List<CareListCategoryDto>) careListMapper.getCareListCategoryList(u_no);
		
		responseMap.put("careListCategoryDtos", careListCategoryDtos);
		
		return responseMap;
	}	

/*
	//페이지 번호에 따른 케어리스트 카테고리 리스트들 가져오기
	public Map<String, Object> getCareListCategoryListWithPage(String sortValue, String order, String u_id) {
		log.info("getCareListCategoryListWithPage()");
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<CareListCategoryDto> careListCategoryDtos = careListMapper.getCareListCategoryListWithPage(CareListPagingUtil.allListParams(sortValue, order, u_no));
		
		pagingList.put("careListCategoryDtos", careListCategoryDtos);
		
		return pagingList;
		
	}
*/

/*
	// 케어리스트 카테고리의 총 페이지 개수 구하기
	public Map<String, Object> getCareListCategoryListPageNum(String u_id) {
		log.info("getCareListCategoryListPageNum()");
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		// 전체 리스트 개수 조회
		int careListCategoryListCnt = careListMapper.getAllCareListCategoryCnt(u_no);
		
		return CareListPagingUtil.pageNum(page_limit, "careListCategoryListCnt", careListCategoryListCnt, page, u_no);
		
	}
*/
	
	// 케어리스트 카테고리 한 개 가져오기
	public CareListCategoryDto getCareListCategory(int clc_no) {
		log.info("getCareListCategory()");
		
		CareListCategoryDto careListCategoryDto = careListMapper.getCareListCategory(clc_no);
		if (careListCategoryDto == null) throw new RuntimeException("careListCategoryDto is null!!");
		
		return careListCategoryDto;
		
	}
	

	// 케어리스트 카테고리 수정하기
	public boolean modifyCategoryConfirm(CareListCategoryDto careListCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		int modifyCategoryResult = careListMapper.updateCareListCategory(careListCategoryDto);
		
		// DB에 입력 실패
		if (modifyCategoryResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();	
		
	}

	// 케어리스트 카테고리 삭제하기
	public boolean deleteCategoryConfirm(int clc_no) {
		log.info("deleteCategoryConfirm()");
		
		int deleteCategoryResult = careListMapper.deleteCareListCategory(clc_no);
		
		// DB에 입력 실패
		if (deleteCategoryResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();	
		
	}

/////////////////////////////////////////////////////// 케어리스트	
	
	// 케어리스트 등록하기 (수정)
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean createConfirm(List<MultipartFile> files, CareListDto careListDto, List<Integer> d_nos, String u_id) {
		log.info("createConfirm()");
		
		try {
		
			// u_id 값으로 u_no 가져오기
			int u_no = userService.selectUserNoById(u_id);
			// u_no를 cl_user_no에 할당
			careListDto.setCl_user_no(u_no);
			
			int createResult = 0;
			
			// 케어리스트 테이블에 정보 등록하기
			createResult = careListMapper.insertNewCareList(careListDto);
			
			// 케어리스트 테이블에 정보 등록 실패 시
			if (createResult <= 0) throw new RuntimeException("CARE_LIST TABLE INSERT FAIL!!");
		
			// 등록 성공 시 CARE_PERSON_DISEASE 테이블에 케어리스트의 질병 정보 등록하기
			
			// 마지막에 등록된 cl_no 가져오기
			int last_cl_no = careListMapper.getCareListMaxNo();
			
			// last_cl_no를 기준으로 CARE_PERSON_DISEASE 테이블 업데이트 하기
			for (int d_no : d_nos) {
				Map<String, Object> insertParams = new HashMap<>();
				insertParams.put("last_cl_no", last_cl_no);
				insertParams.put("d_no", d_no);
				
				int cpdCreateResult = careListMapper.insertNewCarePersonDisease(insertParams);
				
				if (cpdCreateResult <= 0) throw new RuntimeException("CARE_PERSON_DISEAE TABLE INSERT FAIL!!");
				
			}
			
			// 이미지 첨부를 안했을 시 여기서  반환
			if (files == null || files.isEmpty()) return SqlResult.SUCCESS.getValue();
			
		
			// 이미지 서버에 요청할 파일 저장 경로 생성
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = dateFormat.format(now);
			
			String filePath = "\\careList\\" + u_no + "\\" + last_cl_no + "\\" + date;
			
			// 이미지 저장 요청
			ResponseEntity<String> savedFile = imageFileService.uploadFiles(files, filePath);
			
			// 이미지 서버에 저장 실패 시 즉시 롤백 후 FAIL 반환
			if (savedFile == null) throw new RuntimeException("uploadFile FAIL!!");
			
			log.info("uploadFile SUCCESS!!");
			
			ObjectMapper objectMapper = new ObjectMapper();
				
			Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody() , new TypeReference<Map<String, Object>>() {} );
			String savedFileName = ((List<String>) savedFileObj.get("savedFileNames")).get(0);
			
			// 디렉토리명과 이미지 URL을 CARE_LIST 테이블에 업데이트
			Map<String, Object> updateImgColumnParams = new HashMap<>();
			
			updateImgColumnParams.put("last_cl_no", last_cl_no);
			updateImgColumnParams.put("cl_dir_name", date);
			updateImgColumnParams.put("cl_img", savedFileName);
			
			createResult = careListMapper.updateImgColumn(updateImgColumnParams);
			
			if (createResult <= 0) throw new RuntimeException("updateImgColumn FAIL!!");
			
			return SqlResult.SUCCESS.getValue();
				
		} catch(Exception e) {
			
			log.info("Exception 발생: {}", e.getMessage());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return SqlResult.FAIL.getValue();
			
		}
			
	}
	
	// 케어리스트 즐겨찾기 ON / OFF
	public boolean favorites_confirm(int cl_no) {
		log.info("favorites_confirm()");
		
		int favoritesResult = careListMapper.updateCareListFavorites(cl_no);
		
		// DB에 입력 실패
		if (favoritesResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();	
		
	}
	
	
	// 페이지 번호에 따른 모든 케어리스트 가져오기
	public Map<String, Object> getCareListWithPage(int page_limit, int page, String sortValue,
			String order, String u_id) {
		log.info("getCareListWithPage()");
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<CareListDto> careListDtos = careListMapper.getCareListWithPage(CareListPagingUtil.pagingParams(page_limit, sortValue, order, page, u_no));		
		pagingList.put("careListDtos", careListDtos);
		
		return pagingList;
		
	}

	// 모든 케어리스트 총 페이지 개수 가져오기
	public Map<String, Object> getCareListPageNum(int page_limit, int page, String u_id) {
		log.info("getCareListPageNum()");
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		// 전체 리스트 개수 조회
		int careListCnt = careListMapper.getAllCareListCnt(u_no);
		
		return CareListPagingUtil.pageNum(page_limit, "careListCnt", careListCnt, page, u_no);

	}

	// 페이지 번호에 따른 카테고리별 케어리스트 가져오기
	public Map<String, Object> getCareListByCategoryWithPage(int page_limit, int page, String sortValue, String order, Integer infoNo, String u_id) {
		log.info("getCareListByCategoryWithPage()");
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<CareListDto> careListDtos = careListMapper.getCareListByCategoryWithPage(CareListPagingUtil.pagingParamsForSelectBox(page_limit, sortValue, order, page, infoNo, u_no));
		pagingList.put("careListDtos", careListDtos);
		
		return pagingList;
		
	}

	// 카테고리별 케어리스트 페이지 개수 가져오기
	public Map<String, Object> getCareListByCategoryPageNum(int page_limit, int page, Integer infoNo, String u_id) {
		log.info("getCareListPageNum()");
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		
		Map<String, Object> selectParams = new HashMap<>();
		
		selectParams.put("u_no", u_no);
		selectParams.put("infoNo", infoNo);
		
		// 전체 리스트 개수 조회
		int careListByCategoryCnt = careListMapper.getCareListByCategoryCnt(selectParams);
		
		return CareListPagingUtil.pageNum(page_limit, "careListByCategoryCnt", careListByCategoryCnt, page, u_no);
		
	}
	
	// 케어리스트 한 개 가져오기
	public CareListDto getCareListByNo(int cl_no) {
		log.info("getCareListByNo()");
		
		CareListDto careListDto = careListMapper.getCareListByNo(cl_no);
		if (careListDto == null) throw new RuntimeException("careListDto is null!!");
		
		return careListDto;
		
	}
	
/*
	// 케어리스트 수정하기
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean modifyCareListConfirm(CareListDto careListDto, List<MultipartFile> files,
			List<Integer> d_nos) {
		log.info("modifyCareListConrifm()");
		
		try {
			
			int modifyResult = 0;
			
			// 케어리스트 테이블에 정보 수정하기
			modifyResult = careListMapper.updateCareList(careListDto);
			
			// 케어리스트 테이블에 정보 수정 실패 시
			if (modifyResult <= 0) throw new RuntimeException("CARE_LIST TABLE MODIFY FAIL!!");
		
			// 수정 성공 시 CARE_PERSON_DISEASE 테이블에 케어리스트의 질병 정보 수정하기
			
			// last_cl_no를 기준으로 CARE_PERSON_DISEASE 테이블 업데이트 하기
			for (int d_no : d_nos) {
				Map<String, Object> insertParams = new HashMap<>();
				insertParams.put("last_cl_no", careListDto.getCl_no());
				insertParams.put("d_no", d_no);
				
				int cpdCreateResult = careListMapper.insertNewCarePersonDisease(insertParams);
				
				if (cpdCreateResult <= 0) throw new RuntimeException("CARE_PERSON_DISEAE TABLE INSERT FAIL!!");
				
			}
			
			// 이미지 첨부를 안했을 시 여기서  반환
			if (files == null || files.isEmpty()) return SqlResult.SUCCESS.getValue();
			
			// 이미지 서버에 요청할 파일 저장 경로 생성
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = dateFormat.format(now);
			
			String filePath = "\\careList\\" + last_cl_no + "\\" + date;
			
			// 이미지 저장 요청
			ResponseEntity<String> savedFile = imageFileService.uploadFiles(files, filePath);
			
			// 이미지 서버에 저장 실패 시 즉시 롤백 후 FAIL 반환
			if (savedFile == null) throw new RuntimeException("uploadFile FAIL!!");
			
			log.info("uploadFile SUCCESS!!");
			
			ObjectMapper objectMapper = new ObjectMapper();
				
			Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody() , new TypeReference<Map<String, Object>>() {} );
			String savedFileName = ((List<String>) savedFileObj.get("savedFileNames")).get(0);
			
			// 디렉토리명과 이미지 URL을 CARE_LIST 테이블에 업데이트
			Map<String, Object> updateImgColumnParams = new HashMap<>();
			
			updateImgColumnParams.put("last_cl_no", last_cl_no);
			updateImgColumnParams.put("cl_dir_name", date);
			updateImgColumnParams.put("cl_img", savedFileName);
			
			modifyResult = careListMapper.updateImgColumn(updateImgColumnParams);
			
			if (modifyResult <= 0) throw new RuntimeException("updateImgColumn FAIL!!");
			
			return SqlResult.SUCCESS.getValue();
				
		} catch(Exception e) {
			
			log.info("Exception 발생: {}", e.getMessage(), e);
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}
*/

	// 케어리스트 삭제하기
	@Transactional
	public boolean deleteCareListConfirm(int cl_no) {
		log.info("deleteCareListConfirm()");
		
		try {
			
			int deleteResult = careListMapper.deleteCareList(cl_no);
			
			// DB에 입력 실패
			if (deleteResult <= 0) throw new RuntimeException("deleteCareList FAIL!!");
			
			CareListDto deleteCareListDto = careListMapper.getCareListByNo(cl_no);
			
			List<String> deleteFolderPath = new ArrayList<>();
			
			String folderPath = "\\careList\\" + deleteCareListDto.getCl_no();
			deleteFolderPath.add(folderPath);
			
			ResponseEntity<String> deleteFolderResult = imageFileService.deleteFolders(deleteFolderPath);
			
			// 이미지 서버에서 deleteFolder요청이 실패한 경우
			if (!deleteFolderResult.getBody().equals("1")) throw new RuntimeException("deleteFolder FAIL!!");
			
		} catch(Exception e) {
			
			log.info("Exception 발생: {}", e.getMessage(), e);
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return SqlResult.FAIL.getValue();
			
		}
		
		return SqlResult.SUCCESS.getValue();
		
	}

	

}
