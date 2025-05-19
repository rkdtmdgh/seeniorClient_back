package com.see_nior.seeniorClient.organizer.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.see_nior.seeniorClient.dto.MedicineDto;
import com.see_nior.seeniorClient.dto.MedicineScheduleDto;
import com.see_nior.seeniorClient.dto.ScheduleDto;

@Mapper
public interface ScheduleMapper {

	int insertNewSchedule(ScheduleDto scheduleDto);
	
	List<ScheduleDto> selectScheduleForMonth(Map<String, Object> params);
	
	List<MedicineDto> selectMedicineForMonth(Map<String, Object> params);

	List<ScheduleDto> selectScheduleForDate(ScheduleDto scheduleDto);

	void insertMedicine(MedicineDto medicineDto);
	
	void insertMedicineSchedule(MedicineScheduleDto medicineScheduleDto);

	
}
