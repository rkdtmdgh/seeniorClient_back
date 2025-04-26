package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineScheduleDto {

	private int ms_no;
	private String ms_dosing_period;
    private String ms_meal_relation;
    private String ms_repeat_day;
	private boolean ms_is_deleted;
    private String ms_reg_date;
    private String ms_mod_date;
    
    private int ms_medicine_no;
	
}
