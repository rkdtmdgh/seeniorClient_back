package com.see_nior.seeniorClient.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	// 월별 일정 가져오기
	@GetMapping("/get_schedule_for_month")
	public Object getScheduleForMonth(ScheduleDto scheduleDto) {
		log.info("getScheduleForMonth()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<ScheduleDto> scheduleDtos = 
				scheduleService.getScheduleForMonth(scheduleDto);
		
		responseMap.put("scheduleDtos", scheduleDtos);
		
		return responseMap;
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
