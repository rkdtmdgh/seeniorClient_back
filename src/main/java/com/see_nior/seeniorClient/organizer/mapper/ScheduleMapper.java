package com.see_nior.seeniorClient.organizer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorClient.dto.ScheduleDto;

@Mapper
public interface ScheduleMapper {

	int insertNewSchedule(ScheduleDto scheduleDto);
	
	List<ScheduleDto> selectScheduleForMonth(ScheduleDto scheduleDto);

	List<ScheduleDto> selectScheduleForDate(ScheduleDto scheduleDto);
	
}
