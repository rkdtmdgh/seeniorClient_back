package com.see_nior.seeniorClient.disease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.disease.mapper.DiseaseMapper;
import com.see_nior.seeniorClient.dto.DiseaseCategoryDto;
import com.see_nior.seeniorClient.dto.DiseaseDto;
import com.see_nior.seeniorClient.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class DiseaseService {
	
	final private DiseaseMapper diseaseMapper;
	
/////////////////////////////////////////////// 질환 카테고리
	
	// 모든 질환 카테고리 가져오기 (질환 리스트에서 <select>박스)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> diseaseCategoryDtos = new HashMap<>();
		
		List<DiseaseCategoryDto> diseaseCategoryDto = (List<DiseaseCategoryDto>) diseaseMapper.getDiseaseCategoryList();
		
		diseaseCategoryDtos.put("diseaseCategoryDtos", diseaseCategoryDto);
		
		return diseaseCategoryDtos;
		
	}


	
/////////////////////////////////////////////// 질환
		
	// 카테고리별 질환 가져오기(케어리스트 등록창에서 <select>박스)
	public Map<String, Object> getDiseaseListByCategorySelect(int infoNo) {
		log.info("getDiseaseListByCategorySelect()");
		
		Map<String, Object> diseaseDtos = new HashMap<>();
		
		List<DiseaseDto> diseaseDto = (List<DiseaseDto>) diseaseMapper.getDiseaseByCategorySelect(infoNo);
		
		diseaseDtos.put("diseaseDtos", diseaseDto);
		
		return diseaseDtos;
		
	}

/*
	// 검색한 질환 가져오기(케어리스트 등록 창)
	public Map<String, Object> getSearchDiseaseListSelect(String searchPart, String searchString, String sortValue,	String order) {
		log.info("getSearchDiseaseListSelect()");
		
		Map<String, Object> diseaseDtos = new HashMap<>();
		
		Map<String, Object> searchParams = new HashMap<>();
		
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		searchParams.put("sortValue", sortValue);
		searchParams.put("order", order);
		
		List<DiseaseDto> diseaseDto = (List<DiseaseDto>) diseaseMapper.getSearchDiseaseListSelect(searchParams);
		
		diseaseDtos.put("diseaseDto", diseaseDto);
		
		return diseaseDtos;
		
	}
*/
	
	// 페이지에 따른 질환 가져오기(검색한 질환)
	public Map<String, Object> getSearchDiseaseListWithPage(int page_limit, String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("getSearchDiseaseListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<DiseaseDto> searchDiseaseDtos = diseaseMapper.getSearchDisease(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
		pagingList.put("diseaseDtos", searchDiseaseDtos);
		
		return pagingList;
		
	}

	// 질환의 총 페이지 개수 구하기(검색한 질환)
	public Map<String, Object> getSearchDiseaseListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("getSearchDiseaseListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchDiseaseListCnt = diseaseMapper.getSearchDiseaseListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchDiseaseListCnt", searchDiseaseListCnt, page);
		
	}
	
/*	
	// 페이지 번호에 따른 카테고리별 질환 가져오기
	public Map<String, Object> getDiseaseListByCategoryWithPage(int page_limit, int page, String sortValue,String order, Integer infoNo) {
		log.info("getDiseaseListByCategoryWithPage(");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<DiseaseDto> diseaseDtos = diseaseMapper.getDiseaseListByCategoryWithPage(PagingUtil.pagingParamsForSelectBox(page_limit, sortValue, order, page, infoNo));
		pagingList.put("diseaseDtos", diseaseDtos);
		
		return pagingList;
		
	}

	// 카테고리별 질환 페이지 개수 가져오기
	public Map<String, Object> getDiseaseListByCategoryPageNum(int page_limit, int page, Integer infoNo) {
		log.info("getDiseaseListByCategoryPageNum(");
		
		// 전체 리스트 개수 조회
		int diseaseListByCategoryCnt = diseaseMapper.getDiseaseByCategoryCnt(infoNo);
		
		return PagingUtil.pageNum(page_limit, page_limit, "diseaseListByCategoryCnt", diseaseListByCategoryCnt, page);
		
	}
*/

}
