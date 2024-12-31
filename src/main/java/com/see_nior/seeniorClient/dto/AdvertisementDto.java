package com.see_nior.seeniorClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementDto {
	
	private int ad_no;
	private int ad_category_no;
	private String ad_img;
	private String ad_alt;
	private String ad_dir_name;
	private int ad_idx;
	private String ad_url;
	private String ad_start_date;
	private String ad_end_date;
	private String ad_client;
	private boolean ad_state;
	private boolean ad_is_deleted;
	private String ad_reg_date;
	private String ad_mod_date;
	
	private AdvertisementCategoryDto advertisementCategoryDto;
	
}
