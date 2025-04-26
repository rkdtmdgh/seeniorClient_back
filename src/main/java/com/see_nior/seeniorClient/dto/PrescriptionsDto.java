package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionsDto {

	private int p_no;
	private String p_title;	
	private String p_comment;
	private boolean p_alarm_check;
	private String p_alarm_time_1;
	private boolean p_taking_it_or_not_1;
	private String p_alarm_time_2;
	private boolean p_taking_it_or_not_2;  
	private String p_alarm_time_3;
	private boolean p_taking_it_or_not_3;
	private String p_alarm_time_4;
	private boolean p_taking_it_or_not_4;
	private String p_alarm_time_5;
	private boolean p_taking_it_or_not_5;
	private boolean p_before_after;
    private String p_start_date;
    private String p_end_date;
    private boolean p_is_deleted;
	private String p_reg_date;
	private String p_mod_date;
	
	private int p_user_no;
	private int p_care_list_no;
	
}
