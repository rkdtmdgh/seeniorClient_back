package com.see_nior.seeniorClient.carelist;

import java.security.Principal;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.see_nior.seeniorClient.dto.CareListCategoryDto;
import com.see_nior.seeniorClient.dto.CareListDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/care_list")
@RequiredArgsConstructor
public class CareListController {

	final private CareListService careListService;
	
//////////////////////////////////////////////////// 케어리스트 카테고리
	
	// 케어리스트 카테고리명 중복 확인
	@GetMapping("/cate_info/is_carelist_category")
	public boolean isCareListCategory(@RequestParam(value = "clc_name") String clc_name) {
		log.info("isCareListCategory()");
		
		boolean isCareListCategory = careListService.isCareListCategory(clc_name);
		
		return isCareListCategory;
		
	}
	
	// 케어리스트 카테고리 등록하기
	@PostMapping("/cate_info/create_category_confirm")
	public boolean createCategoryConfirm(@RequestParam(value = "clc_name") String clc_name, Principal principal) {
		log.info("createCategoryConfirm()");
		
		boolean createCategoryResult = careListService.createCategoryConfirm(clc_name, principal.getName());
		
		return createCategoryResult;
		
	}
	
	// 모든 케어리스트 카테고리 가져오기 (케어리스트에서 <select> 박스)
	@GetMapping("/cate_info/get_category_list_select")
	public Object getCategoryListSelect() {
		log.info("getCategoryListSelect()");
		
		Map<String, Object> careListCategoryDtos = careListService.getCategoryList();
		
		return careListCategoryDtos;
		
	}
	
	// 모든 케어리스트 카테고리 가져오기(페이지네이션)
	@GetMapping("/cate_info/get_category_list")
	public Object getCategoryList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "block_limit") int block_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "clc_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getCategoryList()");
		
		//페이지 번호에 따른 케어리스트 카테고리 리스트들 가져오기
		Map<String, Object> careListCategoryListWithPage = careListService.getCareListCategoryListWithPage(page_limit, sortValue, order, page);
		
		// 케어리스트 카테고리 총 페이지 개수 가져오기
		Map<String, Object> careListCategoryListPageNum = careListService.getCareListCategoryListPageNum(page_limit, block_limit, page);
		
		careListCategoryListWithPage.put("careListCategoryListPageNum", careListCategoryListPageNum);
		careListCategoryListWithPage.put("sortValue", sortValue);
		careListCategoryListWithPage.put("order", order);
		
		return careListCategoryListWithPage;
		
	}
	
	// 케어리스트 카테고리 수정하기
	@PostMapping("/cate_info/modify_category_confirm")
	public boolean modifyCategoryConfirm(CareListCategoryDto careListCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		boolean modifyCategoryResult = careListService.modifyCategoryConfirm(careListCategoryDto);
		
		return modifyCategoryResult;
		
	}
	
	// 케어리스트 카테고리 삭제하기
	@PostMapping("/cate_info/delete_category_confirm") 
	public boolean deleteCategoryConfirm(@RequestParam(value = "clc_no") int clc_no) {
		log.info("deleteCategoryConfirm()");
		
		boolean deleteCategoryConfirm = careListService.deleteCategoryConfirm(clc_no);

		return deleteCategoryConfirm;
		
	}
	
//////////////////////////////////////////////////// 케어리스트
	
	// 케어리스트 등록하기
	@PostMapping("info/create_confirm")
	public boolean createConfirm(CareListDto careListDto) {
		log.info("createConfirm()");
		
//		boolean createResult = careListService.createConfirm(careListDto);
//		
//		return createResult;
		
		return false;
		
	}
	
	
	// 케어리스트 불러오기
	
	
	// 케어리스트 수정하기
	
	
	// 케어리스트 삭제하기
	
}
