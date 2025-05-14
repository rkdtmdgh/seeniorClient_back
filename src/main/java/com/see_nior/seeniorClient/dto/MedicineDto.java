package com.see_nior.seeniorClient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {

	private int m_no;
	private String m_name;
	private LocalDate m_start_date;
	private LocalDate m_end_date;
	private boolean m_is_deleted;
	private LocalDateTime m_reg_date;
	private LocalDateTime m_mod_date;
	
	private int m_user_no;
	private int m_care_list_no;
	
}
