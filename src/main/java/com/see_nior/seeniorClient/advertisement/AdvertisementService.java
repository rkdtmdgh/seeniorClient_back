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

	// 홈 화면에서 보여질 광고 가져오기
	public Object getAdvertisementListForMain(int page_limit) {
		log.info("getAdvertisementListForMain()");
		
		Map<String, Object> responseMap = new HashMap<>();
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListForMain(page_limit);
		responseMap.put("advertisementDtos", advertisementDtos);

		return responseMap;
		
	}

}
