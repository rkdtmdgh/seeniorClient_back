package com.see_nior.seeniorClient.advertisement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorClient.dto.AdvertisementDto;

@Mapper
public interface AdvertisementMapper {

	// 홈 화면에서 보여질 광고 가져오기(위치별 광고)
	List<AdvertisementDto> getAdvertisementListForMainByCategory(int page_limit);

}
