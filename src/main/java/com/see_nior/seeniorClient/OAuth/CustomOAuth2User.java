package com.see_nior.seeniorClient.OAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.see_nior.seeniorClient.dto.UserAccountDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
	
	private final UserAccountDto userAccountDto;
	
	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collection = new ArrayList<>();
		
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userAccountDto.getU_authority_role();
			}
		});
		
		return collection;
	}

	@Override
	public String getName() {
		return userAccountDto.getU_id();
	}

	public String getU_name() {
		return userAccountDto.getU_name();
}
	
	
}
