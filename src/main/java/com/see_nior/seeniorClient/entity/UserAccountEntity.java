package com.see_nior.seeniorClient.entity;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user_account")
@EntityListeners(AuditingEntityListener.class)
public class UserAccountEntity {

	@Id		// PK 
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// auto increament
	private int u_no;
	
	// nullable null 값 허용 여부. default = true
	// DB에서의 컬럼의 데이터 타입과 같은 타입으로 해야한다. 
	@Column(length = 200, nullable = false)		// length default = 500  --> varchar(255)
	private String u_id;
	
	@Column(length = 400, nullable = false)
	private String u_pw;
	
	@Column(length = 200, nullable = false)
	private String u_name;
	
	@Column(length = 200, nullable = false)
	private String u_phone;
	
	@Column(nullable = false)
	private String u_nickname;
	
	// 1글자 데이터 타입이 없기 때문에 columnDefinition = "char" 라고 DB에서의 타입을 명시해줘야 한다. 
	@Column(columnDefinition = "char", nullable = false)
	private String u_gender;
	
	@Column(nullable = false)
	private LocalDate u_birth;
	
	@Column
	private String u_address;
	
	@Column
	private String u_profile_img;
	
	@Column(length = 400)
	private String u_company;
	
	// tinyint 의 경우 boolean 형으로 사용할 수 있지만 columnDefinition = "tinyint" 명시해야 한다.
	@Column(columnDefinition = "tinyint")
	private boolean u_is_personal;					// true = 개인, false = 기관
	
	@Column(length = 400)
	private String u_social_id;
	
	@Column(columnDefinition = "tinyint")
	private boolean u_is_blocked;					// 계정 정지 여부. 		true = 정지 X, false = 정지
	
	@Column(columnDefinition = "tinyint")
	private boolean u_isaccountnonexpired;			// 계정 만료 유무. 		true = 만료 X, false = 만료
	
	@Column(columnDefinition = "tinyint")
	private boolean u_isaccountnonlocked;			// 계정 잠김 유무. 		true = 잠김 X, false = 만료
	
	@Column(columnDefinition = "tinyint")
	private boolean u_iscredentialsnonexpired;		// 계정 자격 증명 유무. true = 만료 X, false = 만료
	
	@Column(columnDefinition = "tinyint")
	private boolean u_isenabled;					// 계정 사용 가능 유무. true = 사용 가능, false = 사용 X
	
	@Column(columnDefinition = "tinyint")
	private boolean u_is_deleted;					// 계정 삭제 유무. 		true = 삭제 X, false = 삭제
	
}
