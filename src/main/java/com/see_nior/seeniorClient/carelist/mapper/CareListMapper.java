package com.see_nior.seeniorClient.carelist.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorClient.dto.CareListCategoryDto;
import com.see_nior.seeniorClient.dto.CareListDto;

@Mapper
public interface CareListMapper {

///////////////////////////////////// 케어리스트 카테고리
	
	// 케어리스트 카테고리명 중복 확인
	public boolean isCareListCategory(Map<String, Object> isCareListParams);
	
	// 케어리스트 카테고리 등록하기
	public int insertNewCareListCategory(Map<String, Object> insertParams);

	// 모든 케어리스트 카테고리 가져오기 (케어리스트에서 <select> 박스)
	public List<CareListCategoryDto> getCareListCategoryList(int u_no);
	
	// 페이지 번호에 따른 케어리스트 카테고리 리스트들 가져오기
	public List<CareListCategoryDto> getCareListCategoryListWithPage(Map<String, Object> pagingParams);

	// 케어리스트 카테고리의 총 페이지 개수 구하기
	public int getAllCareListCategoryCnt(int u_no);

	// 케어리스트 카테고리 수정하기
	public int updateCareListCategory(CareListCategoryDto careListCategoryDto);

	// 케어리스트 카테고리 삭제하기
	public int deleteCareListCategory(int clc_no);

///////////////////////////////////// 케어리스트
	
	// 페이지 번호에 따른 모든 케어리스트 가져오기
	public List<CareListDto> getCareListWithPage(Map<String, Object> pagingParams);

	// 모든 케어리스트 총 개수 가져오기
	public int getAllCareListCnt(int u_no);

	// 페이지 번호에 따른 카테고리별 케어리스트 가져오기
	public List<CareListDto> getCareListByCategoryWithPage(Map<String, Object> pagingParamsForSelectBox);

	// 카테고리별 케어리스트 총 개수 가져오기
	public int getCareListByCategoryCnt(Map<String, Object> selectParams);
	
	
	
	
}
