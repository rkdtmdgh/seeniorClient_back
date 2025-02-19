package com.see_nior.seeniorClient.disease;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/disease")
@RequiredArgsConstructor
public class DiseaseController {
	
	final private DiseaseService diseaseService;
	
/////////////////////////////////////////////// 질환 카테고리
	
	// 모든 질환 카테고리 가져오기 (질환 리스트에서 <select>박스)
	@GetMapping("/cate_info/get_category_list_select")
	public Object getCategoryListSelect() {
		log.info("getCategoryListSelect()");
		
		Map<String, Object> diseaseCategoryDtos = diseaseService.getCategoryList();
		
		return diseaseCategoryDtos;
		
	}
	
/////////////////////////////////////////////// 질환
	
	// 카테고리별 질환 가져오기(케어리스트 등록창에서 <select>박스)
	@GetMapping("/info/get_disease_list_by_category_select")
	public Object getDiseaseListByCategorySelect(@RequestParam(value = "infoNo", defaultValue = "null") int infoNo) {
		log.info("getDiseaseListByCategorySelect()");
		
		Map<String, Object> diseaseDtos = diseaseService.getDiseaseListByCategorySelect(infoNo);
		
		return diseaseDtos;
		
	}
	
/*
	// 검색한 질환 가져오기(케어리스트 등록 창)
	@GetMapping("/info/search_disease_list_select")
	public Object searchDiseaseListSelect(
			@RequestParam(value = "searchPart", defaultValue = "d_name") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "sortValue", required = false, defaultValue = "d_name") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
		log.info("searchDiseaseListSelect");
		
		Map<String, Object> searchDiseaseListSelect = diseaseService.getSearchDiseaseListSelect(searchPart, searchString, sortValue, order);
		
		searchDiseaseListSelect.put("searchPart", searchPart);
		searchDiseaseListSelect.put("searchString", searchString);
		searchDiseaseListSelect.put("sortValue", sortValue);
		searchDiseaseListSelect.put("order", order);
		
		return searchDiseaseListSelect;
		
	}
*/
	
	// 검색한 질환 가져오기
	@GetMapping("/info/search_disease_list")
	public Object searchDiseaseList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "searchPart", defaultValue = "d_name") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "sortValue", required = false, defaultValue = "d_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchDiseaseList()");
		
		// 페이지 번호에 따른 검색 질환 리스트들 가져오기
		Map<String, Object> searchDiseaseListWithPage = diseaseService.getSearchDiseaseListWithPage(page_limit, searchPart, searchString, sortValue, order, page);
		
		// 검색 질환 총 페이지 개수 가져오기
		Map<String, Object> searchDiseaseListPageNum = diseaseService.getSearchDiseaseListPageNum(page_limit, searchPart, searchString, page);
		
		searchDiseaseListWithPage.put("searchDiseaseListPageNum", searchDiseaseListPageNum);
		searchDiseaseListWithPage.put("searchPart", searchPart);
		searchDiseaseListWithPage.put("searchString", searchString);
		searchDiseaseListWithPage.put("sortValue", sortValue);
		searchDiseaseListWithPage.put("order", order);
		
		return searchDiseaseListWithPage;
		
	}
	
	
/*
	// 카테고리별 질환 가져오기
	@GetMapping("info/get_disease_list_by_category")
	public Object getDiseaseListByCategory(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "d_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "infoNo", required = false, defaultValue = "null") Integer infoNo) {
		log.info("getDiseaseListByCategory()");
		
		// 페이지 번호에 따른 카테고리별 질환 가져오기
		Map<String, Object> diseaseListByCategoryWithPage = diseaseService.getDiseaseListByCategoryWithPage(page_limit, page, sortValue, order, infoNo);
		
		// 카테고리별 질환 총 페이지 개수 가져오기
		Map<String, Object> diseaseListByCategoryPageNum = diseaseService.getDiseaseListByCategoryPageNum(page_limit, page, infoNo);
		
		diseaseListByCategoryWithPage.put("diseaseListByCategoryPageNum", diseaseListByCategoryPageNum);
		diseaseListByCategoryWithPage.put("sortValue", sortValue);
		diseaseListByCategoryWithPage.put("order", order);
		diseaseListByCategoryWithPage.put("infoNo", infoNo);
		
		return diseaseListByCategoryWithPage;
			
	}
*/

}
