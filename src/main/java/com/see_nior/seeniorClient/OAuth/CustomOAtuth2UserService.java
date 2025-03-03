package com.see_nior.seeniorClient.OAuth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorClient.dto.GoogleResponse;
import com.see_nior.seeniorClient.dto.KakaoResponse;
import com.see_nior.seeniorClient.dto.NaverResponse;
import com.see_nior.seeniorClient.dto.OAuth2Response;
import com.see_nior.seeniorClient.dto.UserAccountDto;
import com.see_nior.seeniorClient.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAtuth2UserService extends DefaultOAuth2UserService{

	private final UserMapper userMapper;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("loadUser()");
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		String registration = userRequest.getClientRegistration().getRegistrationId();
		log.info("registration ------ {}", registration);

		OAuth2Response oAuth2Response = null;
		
		if ("kakao".equals(registration)) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else if ("naver".equals(registration)) {
			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else if ("google".equals(registration)) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else {
			throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 Provider입니다.");
		}
		
		String u_social_id = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
		log.info("u_social_id ------- {}", u_social_id);
		
		boolean existData = userMapper.isSocialId(u_social_id);
		
		if (existData) {
			log.info("existData is not null -- login");
			// u_social_id 으로 가입된 정보가 있으면 로그인 처리
			UserAccountDto userAccountDto = userMapper.selectUserAccountBySocialId(u_social_id);
			
			return new CustomOAuth2User(userAccountDto);
		} else {
			log.info("existData is null -- sign up");
			log.info("oAuth2Response ------ {}", oAuth2Response);
			// u_social_id 으로 가입된 정보가 없다면 oAuth2Response를 프런트로 넘겨서 회원가입 유도 
			throw new OAuth2UserNotFoundException(oAuth2Response);
		}
		
	}
	
}

