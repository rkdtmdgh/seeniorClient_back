package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareListCategoryDto {
	
	private int clc_no;
	private int clc_user_no;
	private String clc_name;
	private int clc_item_cnt;
	private int clc_is_deleted;
	private String clc_reg_date;
	private String clc_mod_date;

}
