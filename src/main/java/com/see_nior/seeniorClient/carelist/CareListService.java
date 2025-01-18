package com.see_nior.seeniorClient.carelist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
			log.info("50개 초과!!");
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
	public boolean createConfirm(CareListDto careListDto) {
		log.info("createConfirm()");
		
		
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	

	

	
	
	

}
