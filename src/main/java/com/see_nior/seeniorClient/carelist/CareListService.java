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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorClient.carelist.mapper.CareListMapper;
import com.see_nior.seeniorClient.disease.mapper.DiseaseMapper;
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
	final private DiseaseMapper diseaseMapper;
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
	
	// 케어리스트 등록하기
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean createConfirm(List<MultipartFile> files, CareListDto careListDto, List<Integer> d_nos, String u_id) {
		log.info("createConfirm()");
		
		// u_id 값으로 u_no 가져오기
		int u_no = userService.selectUserNoById(u_id);
		careListDto.setCl_user_no(u_no);
		
		int createResult = 0;
		
		// 케어리스트 사진을 등록 할 시
		if (files != null && files.size() != 0 && files.get(0).getSize() != 0) {
			
			// 이미지 서버에 요청할 파일 저장 경로 생성
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = dateFormat.format(now);
			log.info("date ----------> {}", date);
			
			
			// careList 테이블에서 maxNo값 가져오기
			Integer maxNo = careListMapper.getCareListMaxNo();
			if (maxNo == null) maxNo = 0;
			
			String filePath = "\\careList\\" + (maxNo + 1) + "\\" + date;
			
			// 이미지 저장 요청
			ResponseEntity<String> savedFile = imageFileService.uploadFiles(files, filePath);
			
			// 이미지 서버에 저장 실패 시 즉시 롤백 후 FAIL 반환
			if (savedFile == null) {
				log.info("uploadFIle FAIL!!");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return SqlResult.FAIL.getValue();
				
			}
			
			log.info("uploadFile SUCCESS!!");
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			try {
				
				Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody(), new TypeReference<Map<String, Object>>() {});
				String savedFileName = ((List<String>) savedFileObj.get("savedFileNames")).get(0);
				
				// 디렉토리명과 이미지 URL 세팅
				careListDto.setCl_dir_name(date);
				careListDto.setCl_img(savedFileName);
				
				createResult = careListMapper.insertNewCareList(careListDto);
				
				// DB에 입력 실패
				if (createResult <= 0) {
					log.info("insertNewCareList() error!!");
					
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return SqlResult.FAIL.getValue();
					
				} 
				
				// DB에 입력 성공
				else return SqlResult.SUCCESS.getValue();
				
			} catch (JsonMappingException e) {
				log.info("JsonMappingException()");
				e.printStackTrace();
				
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				
				return SqlResult.FAIL.getValue();
				
			} catch (JsonProcessingException e) {
				log.info("JsonProcessingException!!");
				e.printStackTrace();
				
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				
				return SqlResult.FAIL.getValue();
				
			}
			
		} else {
			createResult = careListMapper.insertNewCareList(careListDto);
			
			// DB에 입력 실패
			if (createResult <= 0) {
				log.info("insertNewCareList() error!!");
				
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return SqlResult.FAIL.getValue();
				
			// DB에 입력 성공
			} else {
				
				// d_nos의 길이가 0이라면 바로 return 처리
				if (d_nos.size() <= 0) return SqlResult.SUCCESS.getValue();
				
				else {
					
					// 방금 저장된 케어리스트의 cl_no를 가져오기
					int last_cl_no = careListMapper.getCareListMaxNo();
					
					// last_cl_no를 기준으로 CARE_PERSON_DISEASE 테이블 업데이트 하기
					for (int d_no : d_nos) {
						Map<String, Object> insertParams = new HashMap<>();
						insertParams.put("last_cl_no", last_cl_no);
						insertParams.put("d_no", d_no);
						
						int cpdCreateResult = diseaseMapper.insertNewCarePersonDisease(insertParams);
						
						if (cpdCreateResult <= 0) {
							log.info("carePersonDisease insert error!");
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							return SqlResult.FAIL.getValue();
							
						}
						
					}
					
					return SqlResult.SUCCESS.getValue();
					
				}
				
			}
			
		}
		
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
	
	
	

	// 케어리스트 삭제하기
	public boolean deleteCareListConfirm(int cl_no) {
		log.info("deleteCareListConfirm()");
		
		CareListDto deleteCareListDto = careListMapper.getCareListByNo(cl_no);
		
		List<String> deleteFolderPath = new ArrayList<>();
		
		String folderPath = "\\careList\\" + deleteCareListDto.getCl_no();
		deleteFolderPath.add(folderPath);
		
		ResponseEntity<String> deleteFolderResult = imageFileService.deleteFolders(deleteFolderPath);
		
		// 이미지 서버에서 deleteFolder요청이 성공한 경우
		if (deleteFolderResult.getBody().equals("1")) {
			log.info("deleteFolder SUCCESS!!");
			
			int deleteResult = careListMapper.deleteCareList(cl_no);
			
			// DB에 입력 실패
			if (deleteResult <= 0) {
				log.info("케어리스트 DB데이터 삭제 실패!!");
				
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				
				return SqlResult.FAIL.getValue();
				
			}
			// DB에 입력 성공
			else return SqlResult.SUCCESS.getValue();
			
		} else {
			log.info("deleteFolder FAIL!!");
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}

	

	

	
	
	

}
