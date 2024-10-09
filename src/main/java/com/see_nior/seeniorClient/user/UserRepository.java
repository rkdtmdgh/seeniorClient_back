package com.see_nior.seeniorClient.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.see_nior.seeniorClient.entity.UserAccountEntity;

public interface UserRepository extends JpaRepository<UserAccountEntity, Integer> {
												// Entity , PK값 자료형 
	
	
}
