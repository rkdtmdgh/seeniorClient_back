package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareListDto {
	
	private int cl_no;
	private int cl_user_no;
	private int cl_category_no;
	private String cl_name;
	private String cl_img;
	private String cl_dir_name;
	private String cl_gender;
	private int cl_age;
	private String cl_address;
	private String cl_blood_type;
	private String cl_phone;
	private String cl_emergency_contact_1;
	private String cl_emergency_contact_2;
	private String cl_emergency_contact_3;
	private String cl_emergency_contact_4;
	private String cl_favor_food;
	private String cl_hate_food;
	private int cl_diabetic_food;
	private String cl_medications;
	private String cl_hospital;
	private String cl_doctor;
	private String cl_hospital_tel;
	private int cl_walk_state;
	private String cl_assistive_device;
	private int cl_washing_assistance;
	private int cl_toilet_assistance;
	private int cl_mental_state;
	private int cl_social_state;
	private String cl_etc;
	private int cl_favorites;
	private int cl_is_deleted;
	private String cl_reg_date;
	private String cl_mod_date;
	
	private CareListCategoryDto careListCategoryDto;

}
