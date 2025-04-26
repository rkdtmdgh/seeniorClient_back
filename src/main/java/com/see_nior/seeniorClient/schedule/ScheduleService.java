package com.see_nior.seeniorClient.schedule;

import java.util.List;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.dto.ScheduleDto;
import com.see_nior.seeniorClient.enums.SqlResult;
import com.see_nior.seeniorClient.organizer.mapper.ScheduleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ScheduleService {

	final private ScheduleMapper scheduleMapper;

	public boolean createScheduleConfirm(ScheduleDto scheduleDto) {
		log.info("createScheduleConfirm()");
		
		int result = scheduleMapper.insertNewSchedule(scheduleDto);
		
		if (result > 0) 
			return true;
		
		return false;
	} 
	
	public List<ScheduleDto> getScheduleForMonth(ScheduleDto scheduleDto) {
		log.info("getScheduleForMonth() ----- u_no : {}", scheduleDto.getS_user_no());
		log.info("getScheduleForMonth() ----- s_month : {}", scheduleDto.getS_start_date());
		log.info("getScheduleForMonth() ----- s_month : {}", scheduleDto.getS_end_date());
		
		return scheduleMapper.selectScheduleForMonth(scheduleDto);
	}

	public List<ScheduleDto> getScheduleForDate(ScheduleDto scheduleDto) {
		log.info("getScheduleForDate() ----- u_no : {}", scheduleDto.getS_user_no());
		log.info("getScheduleForMonth() ----- s_month : {}", scheduleDto.getS_start_date());
		log.info("getScheduleForMonth() ----- s_month : {}", scheduleDto.getS_end_date());
		
		return scheduleMapper.selectScheduleForDate(scheduleDto);
	}

	
	
	
	
	
}
