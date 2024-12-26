package com.see_nior.seeniorClient.carelist;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.see_nior.seeniorClient.dto.CareListDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/care-list")
@RequiredArgsConstructor
public class CareListController {

	final private CareListService careListService;
	
	// 케어리스트 등록하기
	@PostMapping("/create_confirm")
	public boolean createConfirm(CareListDto careListDto) {
		log.info("createConfirm()");
		
//		boolean createResult = careListService.createConfirm(careListDto);
//		
//		return createResult;
		
		return false;
		
	}
	
	
	// 케어리스트 불러오기
	
	
	// 케어리스트 수정하기
	
	
	// 케어리스트 삭제하기
	
}
