package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarePersonDiseaseDto {

	private int cpd_no;
	private int cpd_care_list_no;
	private String cpd_disease_no;
	private boolean cpd_is_deleted;
	private String cpd_reg_date;
	private String cpd_mod_date;
	
	private DiseaseDto diseaseDto;
	
}
