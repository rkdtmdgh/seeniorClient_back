package com.see_nior.seeniorClient.advertisement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.advertisement.mapper.AdvertisementMapper;
import com.see_nior.seeniorClient.dto.AdvertisementDto;
import com.see_nior.seeniorClient.enums.ImgUrlPath;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdvertisementService {
	
	final private AdvertisementMapper advertisementMapper;
	
	// 이미지 서버 경로
//		final private String advertisementImgServerPath = "http://127.0.0.1:8091/seeniorUploadImg/advertisement/";
		final private String advertisementImgServerPath = "http://" + ImgUrlPath.ADVERTISEMENT_PATH.getValue();

	// 홈 화면에서 보여질 광고 가져오기(위치별 광고)
	public Object getAdvertisementListForMainByCategory(int ad_category_no) {
		log.info("getAdvertisementListForMainByCategory()");
		
		Map<String, Object> responseMap = new HashMap<>();
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListForMainByCategory(ad_category_no);
		
		if (advertisementDtos.size() <= 0) responseMap.put("advertisementDtos", null);
		
		else {
			
			responseMap.put("advertisementDtos", advertisementDtos);
			responseMap.put("advertisementImgServerPath", advertisementImgServerPath);
			
		}

		log.info("responseMap -------->{}", responseMap);
		
		return responseMap;
		
	}

}
