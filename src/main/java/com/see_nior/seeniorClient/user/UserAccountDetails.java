package com.see_nior.seeniorClient.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.see_nior.seeniorClient.dto.UserAccountDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@SuppressWarnings("serial")
public class UserAccountDetails implements UserDetails {

	final private UserAccountDto userAccountDto;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		log.info("getAuthorities()");
		
		Collection<GrantedAuthority> collection = new ArrayList<>();
		
		collection.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return "ROLE_".concat(userAccountDto.getU_authority_role());
			}
		});
		
		return collection;
	}

	@Override
	public String getPassword() {
		return userAccountDto.getU_pw();
	}

	@Override
	public String getUsername() {
		return userAccountDto.getU_id();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return userAccountDto.isU_isaccountnonexpired();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return userAccountDto.isU_isaccountnonlocked();
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return userAccountDto.isU_iscredentialsnonexpired();
	}
	
	@Override
	public boolean isEnabled() {
		return userAccountDto.isU_isenabled();
	}
	
}
