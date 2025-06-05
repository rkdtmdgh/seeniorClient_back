package com.see_nior.seeniorClient.schedule;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.see_nior.seeniorClient.dto.MedicineRequestDto;
import com.see_nior.seeniorClient.dto.ScheduleDto;
import com.see_nior.seeniorClient.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

	final private ScheduleService scheduleService;
	final private UserService userService;
	
	// 개별 일정 등록하기
	@PostMapping("/create_schedule_confirm")
	public boolean createScheduleConfirm(ScheduleDto scheduleDto, Principal principal) {
		log.info("createScheduleConfirm()");
		
		int u_no = getLoginedNo(principal.getName());
		
		scheduleDto.setS_user_no(u_no);
		
		return scheduleService.createScheduleConfirm(scheduleDto);
	}
	
	// 개별 복용약 등록하기
	@PostMapping("/create_medicine_confirm")
	public boolean createMedicineConfirm(@RequestBody MedicineRequestDto requestDto, 
			Principal principal) {
		log.info("createMedicineConfirm() ------ requestDto : {}", requestDto);
		
		int u_no = getLoginedNo(principal.getName());
		
		return scheduleService.createMedicineConfirm(requestDto, u_no);
	}
	
	// 월별 일정 가져오기
	@GetMapping("/get_schedule_for_month")
	public Object getScheduleForMonth(@RequestParam("search_date") LocalDate search_date, 
			Principal principal) {
		log.info("getScheduleForMonth()");
		
		int u_no = getLoginedNo(principal.getName());
		
		scheduleService.getScheduleForMonth(u_no, search_date);
		
		return null;
	}
	
	// 일별 일정 가져오기 
	@GetMapping("/get_schedule_for_date")
	public Object getScheduleForDate(ScheduleDto scheduleDto, Principal principal) {
		log.info("getScheduleForDate()");

		int u_no = getLoginedNo(principal.getName());
		
		scheduleDto.setS_user_no(u_no);
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<ScheduleDto> scheduleDtos = 
				scheduleService.getScheduleForDate(scheduleDto);
		
		responseMap.put("scheduleDtos", scheduleDtos);
		
		return responseMap;
	}
	
	// 개별 일정 가져오기
	@PostMapping("/get_schedule")
	public String getSchedule(@RequestBody String entity) {
		log.info("getSchedule()");
		
		
		return entity;
	}
	
	
	
	
	// 개별 일정 수정하기 
	
	// 개별 일정 삭제하기
	
	// 로그인한 no 가져오기
	private int getLoginedNo(String u_id) {
		log.info("getLoginedNo()");
		
		return userService.selectUserNoById(u_id);
	}
	
}
