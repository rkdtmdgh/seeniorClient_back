package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineRequestDto {

	private MedicineDto medicineDto;
	private MedicineScheduleDto medicineScheduleDto;
	
}
