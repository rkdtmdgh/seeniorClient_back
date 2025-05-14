package com.see_nior.seeniorClient.schedule;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.see_nior.seeniorClient.dto.MedicineDto;
import com.see_nior.seeniorClient.dto.MedicineRequestDto;
import com.see_nior.seeniorClient.dto.MedicineScheduleDto;
import com.see_nior.seeniorClient.dto.ScheduleDto;
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
	
	@Transactional
	public boolean createMedicineConfirm(MedicineRequestDto requestDto) {
		log.info("createMedicineConfirm()");
		
		try {
			MedicineDto medicineDto = requestDto.getMedicineDto();
			MedicineScheduleDto medicineScheduleDto = requestDto.getMedicineScheduleDto();
			
			// 약 정보 저장
			scheduleMapper.insertMedicine(medicineDto);
			
			// 저장된 약 정보 no 가져오기
			int m_no = medicineDto.getM_no();
			
			// 약 스케쥴 설정
			medicineScheduleDto.setMs_medicine_no(m_no);
			scheduleMapper.insertMedicineSchedule(medicineScheduleDto);
			
			return true;
		} catch (Exception e) {
			log.error("약 등록 중 오류 ---- {}", e);
			
			throw new RuntimeException("약 등록 실패", e);
		}
	}
	
	public Object getScheduleForMonth(int s_user_no, LocalDate search_date) {
		log.info("getScheduleForMonth() ----- s_user_no : {}", s_user_no);
		log.info("getScheduleForMonth() ----- search_date : {}", search_date);
		
		Map<String, Object> params = new HashMap<>();
		params.put("s_user_no", s_user_no);
		params.put("search_yaer", search_date);
		
		List<ScheduleDto> scheduleDtos = scheduleMapper.selectScheduleForMonth(params);
		List<MedicineDto> medicineDtos = scheduleMapper.selectMedicineForMonth(params);
		
		return null;
	}

	public List<ScheduleDto> getScheduleForDate(ScheduleDto scheduleDto) {
		log.info("getScheduleForDate() ----- u_no : {}", scheduleDto.getS_user_no());
		log.info("getScheduleForMonth() ----- s_month : {}", scheduleDto.getS_start_date());
		log.info("getScheduleForMonth() ----- s_month : {}", scheduleDto.getS_end_date());
		
		return scheduleMapper.selectScheduleForDate(scheduleDto);
	}


	
}
