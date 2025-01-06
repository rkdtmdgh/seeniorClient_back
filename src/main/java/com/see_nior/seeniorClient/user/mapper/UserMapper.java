package com.see_nior.seeniorClient.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorClient.dto.UserAccountDto;

@Mapper
public interface UserMapper {

	public int insertNewUser(UserAccountDto userAccountDto);

	public boolean isAccount(String u_id);

	public UserAccountDto selectUserAccountById(String u_id);

	public boolean isNickname(String u_nickname);

	public boolean updateUserAccount(UserAccountDto userAccountDto);
	
}
