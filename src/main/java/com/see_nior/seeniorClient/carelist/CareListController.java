package com.see_nior.seeniorClient.carelist;

import java.security.Principal;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.see_nior.seeniorClient.dto.CareListCategoryDto;
import com.see_nior.seeniorClient.dto.CareListDto;

import jakarta.servlet.http.HttpServletRequest;
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
	public boolean isCareListCategory(@RequestParam(value = "clc_name") String clc_name, Principal principal) {
		log.info("isCareListCategory()");
		log.info("principal ---------> {}", principal);
		
		boolean isCareListCategory = careListService.isCareListCategory(clc_name, principal.getName());
		
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
	public Object getCategoryListSelect(Principal principal) {
		log.info("getCategoryListSelect()");
		
		Map<String, Object> careListCategoryDtos = careListService.getCategoryList(principal.getName());
		
		return careListCategoryDtos;
		
	}
	
	// 모든 케어리스트 카테고리 가져오기(페이지네이션)
	@GetMapping("/cate_info/get_category_list")
	public Object getCategoryList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "clc_name") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String order, Principal principal) {
		log.info("getCategoryList()");
		
		//페이지 번호에 따른 케어리스트 카테고리 리스트들 가져오기
		Map<String, Object> careListCategoryListWithPage = careListService.getCareListCategoryListWithPage(page_limit, sortValue, order, page, principal.getName());
		
		// 케어리스트 카테고리 총 페이지 개수 가져오기
		Map<String, Object> careListCategoryListPageNum = careListService.getCareListCategoryListPageNum(page_limit, page, principal.getName());
		
		careListCategoryListWithPage.put("careListCategoryListPageNum", careListCategoryListPageNum);
		careListCategoryListWithPage.put("sortValue", sortValue);
		careListCategoryListWithPage.put("order", order);
		
		return careListCategoryListWithPage;
		
	}
	
	// 케어리스트 카테고리 한 개 가져오기
	@GetMapping("/cate_info/get_category")
	public Object getCategory(@RequestParam(value = "clc_no") int clc_no) {
		log.info("getCategory()");
		
		CareListCategoryDto careListCategoryDto = careListService.getCareListCategory(clc_no);	
				
		return careListCategoryDto;
		
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
		
		boolean deleteCategoryResult = careListService.deleteCategoryConfirm(clc_no);

		return deleteCategoryResult;
		
	}
	
//////////////////////////////////////////////////// 케어리스트

	// 케어리스트 등록하기
	@PostMapping("/info/create_confirm")
	public boolean createConfirm(CareListDto careListDto, MultipartFile multipartFile) {
		log.info("createConfirm()");
		
		boolean createResult = careListService.createConfirm(careListDto);
		
		return createResult;
		
	}

	
/*
	// 모든 케어리스트 가져오기
	@GetMapping("/info/get_care_list")
	public Object getCareList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "cl_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order, Principal principal) {
		log.info("getCareList()");
		
		// 페이지 번호에 따른 케어리스트 가져오기
		Map<String, Object> careListWithPage = careListService.getCareListWithPage(page_limit, page, sortValue, order, principal.getName());
		
		// 케어리스트 총 페이지 개수 가져오기
		Map<String, Object> careListPageNum = careListService.getCareListPageNum(page_limit, page, principal.getName());
		
		careListWithPage.put("careListPageNum", careListPageNum);
		careListWithPage.put("sortValue", sortValue);
		careListWithPage.put("order", order);
		
		return careListWithPage;
		
	}
*/
	
	// 카테고리별 케어리스트 가져오기
	@GetMapping("/info/get_care_list_by_category")
	public Object getCareListByCategory(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "cl_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "infoNo", required = false, defaultValue = "null") Integer infoNo, Principal principal) {
		log.info("getCareListByCategory()");
		
		// 페이지 번호에 따른 카테고리별 케어리스트 가져오기
		Map<String, Object> careListByCategoryWithPage = careListService.getCareListByCategoryWithPage(page_limit, page, sortValue, order, infoNo, principal.getName());
		
		// 카테고리별 케어리스트 총 페이지 개수 가져오기
		Map<String, Object> careListByCategoryPageNum = careListService.getCareListByCategoryPageNum(page_limit, page, infoNo, principal.getName());
		
		careListByCategoryWithPage.put("careListByCategoryPageNum", careListByCategoryPageNum);
		careListByCategoryWithPage.put("sortValue", sortValue);
		careListByCategoryWithPage.put("order", order);
		careListByCategoryWithPage.put("infoNo", infoNo);
		
		return careListByCategoryWithPage;
		
	}
	
	// 케어리스트 한 개 가져오기
	@GetMapping("/info/get_care_list_by_no")
	public Object getCareListByNo(@RequestParam(value = "cl_no") int cl_no) {
		log.info("getCareListByNo()");
		
		CareListDto careListDto = careListService.getCareListByNo(cl_no);
		
		return careListDto;
		
	}
	
	
	// 케어리스트 수정하기
	
	
/*
	// 케어리스트 삭제하기
	@PostMapping("/info/delete_care_list_confirm")
	public boolean deleteCareListConfirm(@RequestParam(value = "cl_no") int cl_no) {
		log.info("deleteCareListConfirm()");
		
		boolean deleteCareListResult = careListService.deleteCareListConfirm(cl_no);
		
		return deleteCareListResult;
		
	}
*/
	
	
}
