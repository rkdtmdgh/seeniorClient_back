package com.see_nior.seeniorClient.util;

import java.util.HashMap;
import java.util.Map;

public class CareListPagingUtil {

	// pageNum 계산
	public static Map<String, Object> pageNum(int page_limit, String listName, int listCnt, int page, int u_no) {
		
		Map<String, Object> pageNum = new HashMap<>();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) listCnt / page_limit));
		
		pageNum.put(listName, listCnt);
		pageNum.put("page", page);
		pageNum.put("maxPage", maxPage);
		pageNum.put("pageLimit", page_limit);
		pageNum.put("u_no", u_no);
		return pageNum;
		
	}
	
	// 리스트 가져오기 params
	public static Map<String, Object> pagingParams(int page_limit, String sortValue, String order, int page, int u_no) {
		
		Map<String, Object> pagingParams = new HashMap<>();
		
		int pagingStart = (page - 1) * page_limit;
		
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", page_limit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		pagingParams.put("u_no", u_no);
		
		return pagingParams;
		
	}
	
	// 검색 리스트 가져오기 params
	public static Map<String, Object> searchPagingParams(
			int page_limit, String searchPart, String searchString, String sortValue, String order, int page, int u_no) {
		
		Map<String, Object> searchPagingParams = new HashMap<>();
		
		int pagingStart = (page - 1) * page_limit;
		
		searchPagingParams.put("start", pagingStart);
		searchPagingParams.put("limit", page_limit);
		searchPagingParams.put("sortValue", sortValue);
		searchPagingParams.put("order", order);
		searchPagingParams.put("searchPart", searchPart);
		searchPagingParams.put("searchString", searchString);
		searchPagingParams.put("u_no", u_no);
		
		return searchPagingParams;
		
	}
	
	// select 리스트 가져오기 params
	public static Map<String, Object> pagingParamsForSelectBox(int page_limit, String sortValue, String order, int page, Object info_no, int u_no) {
		
		Map<String, Object> pagingParams = new HashMap<>();
		
		int pagingStart = (page - 1) * page_limit;
		
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", page_limit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		pagingParams.put("infoNo", info_no);
		
		return pagingParams;
		
	}

	
}
