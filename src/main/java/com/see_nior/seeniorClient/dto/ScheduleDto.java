package com.see_nior.seeniorClient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {

	private int s_no;
	private String s_title;
	private String s_comment;
	private boolean s_alarm_check;
	private LocalDateTime s_alarm_time;
	private LocalDate s_start_date;
	private LocalDate s_end_date;
	private boolean s_is_deleted;
	private LocalDate s_reg_date;
	private LocalDate s_mod_date;
	
	private int s_user_no;
	private int s_care_list_no;
		
	private CareListDto careListDto;
	
}
