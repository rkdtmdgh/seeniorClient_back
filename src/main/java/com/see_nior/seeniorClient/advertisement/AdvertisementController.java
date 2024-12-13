package com.see_nior.seeniorClient.advertisement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
public class AdvertisementController {
	
	final private AdvertisementService advertisementService;
	
	// 홈 화면에서 보여질 광고 가져오기(위치별 광고)
	@GetMapping("/main/get_advertisement_list")
	public Object getAdvertisementListForMainByCategory(@RequestParam(value = "ad_category_no") int ad_category_no) {
		log.info("getAdvertisementListForMainByCategory()");
		
		return advertisementService.getAdvertisementListForMainByCategory(ad_category_no);
		
	}
	
	
	
}
