package com.see_nior.seeniorClient.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ScheduleResponseDto {

    private LocalDate calDate;   // 달력 날짜 (2025-05-01 등)
	private List<ScheduleDto> scheduleDtos;
}
