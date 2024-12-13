package com.see_nior.seeniorClient.advertisement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.advertisement.mapper.AdvertisementMapper;
import com.see_nior.seeniorClient.dto.AdvertisementDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdvertisementService {
	
	final private AdvertisementMapper advertisementMapper;

	// 홈 화면에서 보여질 광고 가져오기(위치별 광고)
	public Object getAdvertisementListForMainByCategory(int ad_category_no) {
		log.info("getAdvertisementListForMainByCategory()");
		
		Map<String, Object> responseMap = new HashMap<>();
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListForMainByCategory(ad_category_no);
		
		if (advertisementDtos.size() <= 0) responseMap.put("advertisementDtos", null);
		
		else responseMap.put("advertisementDtos", advertisementDtos);

		log.info("responseMap -------->{}", responseMap);
		
		return responseMap;
		
	}

}
