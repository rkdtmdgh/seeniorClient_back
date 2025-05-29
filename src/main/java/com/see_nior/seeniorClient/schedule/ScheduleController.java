package com.see_nior.seeniorClient.schedule;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.see_nior.seeniorClient.dto.MedicineRequestDto;
import com.see_nior.seeniorClient.dto.ScheduleDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

	final private ScheduleService scheduleService;
	
	// 개별 일정 등록하기
	@PostMapping("/create_schedule_confirm")
	public boolean createScheduleConfirm(ScheduleDto scheduleDto) {
		log.info("createScheduleConfirm()");
		
		return scheduleService.createScheduleConfirm(scheduleDto);
	}
	
	// 개별 복용약 등록하기
	@PostMapping("/create_medicine_confirm")
	public ResponseEntity<Boolean> createMedicineConfirm(@RequestBody MedicineRequestDto requestDto) {
		log.info("createMedicineConfirm() ------ requestDto : {}", requestDto);
		
		boolean result = scheduleService.createMedicineConfirm(requestDto);
		
		return ResponseEntity.ok(result);
	}
	
	// 월별 일정 가져오기
	@GetMapping("/get_schedule_for_month")
	public Object getScheduleForMonth(@RequestParam("s_user_no") int s_user_no, 
			@RequestParam("search_date") LocalDate search_date) {
		log.info("getScheduleForMonth()");
		
		scheduleService.getScheduleForMonth(s_user_no, search_date);
		
		return null;
	}
	
	// 일별 일정 가져오기 
	@GetMapping("/get_schedule_for_date")
	public Object getScheduleForDate(ScheduleDto scheduleDto) {
		log.info("getScheduleForDate()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<ScheduleDto> scheduleDtos = 
				scheduleService.getScheduleForDate(scheduleDto);
		
		responseMap.put("scheduleDtos", scheduleDtos);
		
		return responseMap;
	}
	
	// 개별 일정 가져오기
	@PostMapping("/")
	public String postMethodName(@RequestBody String entity) {
		//TODO: process POST request
		
		return entity;
	}
	
	
	
	
	// 개별 일정 수정하기 
	
	// 개별 일정 삭제하기
	
}
