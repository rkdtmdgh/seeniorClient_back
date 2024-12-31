package com.see_nior.seeniorClient.carelist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.carelist.mapper.CareListMapper;
import com.see_nior.seeniorClient.dto.CareListCategoryDto;
import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.enums.SqlResult;
import com.see_nior.seeniorClient.user.UserService;
import com.see_nior.seeniorClient.util.ImageFileService;
import com.see_nior.seeniorClient.util.PagingUtil;

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
	public boolean isCareListCategory(String clc_name) {
		log.info("isCareListCategory()");
		
		boolean isCareListCategory = careListMapper.isCareListCategory(clc_name);
		
		return isCareListCategory;
		
	}
	
	// 케어리스트 카테고리 등록하기
	public boolean createCategoryConfirm(String clc_name, String u_id) {
		log.info("createCategoryConfirm()");
		
		Map<String, Object> insertParams = new HashMap<>();
		
		// u_id 값으로 u_no 가져오기
		UserAccountDto loginedUserDto = userService.getAccountInfoById(u_id);
		
		insertParams.put("clc_name", clc_name);
		insertParams.put("u_no", loginedUserDto.getU_no());
		
		int createCategoryResult = careListMapper.insertNewCareListCategory(insertParams);
		
		// DB에 입력 실패
		if (createCategoryResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
		
	}
	
	// 모든 케어리스트 카테고리 가져오기 (케어리스트에서 <select> 박스)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<CareListCategoryDto> careListCategoryDtos = (List<CareListCategoryDto>) careListMapper.getCareListCategoryList();
		
		responseMap.put("careListCategoryDtos", careListCategoryDtos);
		
		return responseMap;
	}

	//페이지 번호에 따른 케어리스트 카테고리 리스트들 가져오기
	public Map<String, Object> getCareListCategoryListWithPage(int page_limit, String sortValue, String order,
			int page) {
		log.info("getCareListCategoryListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<CareListCategoryDto> careListCategoryDtos = careListMapper.getCareListCategoryListWithPage(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		
		pagingList.put("careListCategoryDtos", careListCategoryDtos);
		
		return pagingList;
		
	}

	// 케어리스트 카테고리의 총 페이지 개수 구하기
	public Map<String, Object> getCareListCategoryListPageNum(int page_limit, int block_limit, int page) {
		log.info("getCareListCategoryListPageNum()");
		
		// 전체 리스트 개수 조회
		int careListCategoryListCnt = careListMapper.getAllCareListCategoryCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "careListCategoryListCnt", careListCategoryListCnt, page);
		
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
	

}
