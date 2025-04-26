package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineIntakeLogDto {

	private int mil_no;
	private String mil_date;
    private String mil_state;
    private boolean mil_is_deleted;
    private String mil_reg_date;
    private String mil_mod_date;
    
    private int mil_medicine_schedule_no;
	
}
