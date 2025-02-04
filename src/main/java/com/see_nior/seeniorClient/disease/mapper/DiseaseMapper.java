package com.see_nior.seeniorClient.disease.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorClient.dto.DiseaseCategoryDto;
import com.see_nior.seeniorClient.dto.DiseaseDto;

@Mapper
public interface DiseaseMapper {

//////////////////////////////////////////////////// 질환 카테고리
	
	// 모든 질환 카테고리 가져오기 (질환 리스트에서 <select>박스)
	List<DiseaseCategoryDto> getDiseaseCategoryList();

//////////////////////////////////////////////////// 질환
	
	// 카테고리별 질환 가져오기(케어리스트 등록창에서 <select>박스)
	List<DiseaseDto> getDiseaseByCategorySelect(int infoNo);
	
	// 검색한 질환 가져오기(케어리스트 등록 창)
	List<DiseaseDto> getSearchDiseaseListSelect(Map<String, Object> searchParams);
	
	// 페이지 번호에 따른 카테고리별 질환 가져오기
	List<DiseaseDto> getDiseaseListByCategoryWithPage(Map<String, Object> pagingParamsForSelectBox);

	

	

}
