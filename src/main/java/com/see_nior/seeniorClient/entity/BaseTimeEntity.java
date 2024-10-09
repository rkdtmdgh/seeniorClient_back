package com.see_nior.seeniorClient.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

	@CreatedDate	// Entity 생성 시간
	@Column(name = "u_reg_date")
	private LocalDateTime u_reg_date;
	
	@LastModifiedDate	// Entity 수정 시간
	@Column(name = "u_mod_date")
	private LocalDateTime u_mod_date;
	
    @PrePersist
    public void prePersist() {
        this.u_reg_date = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.u_mod_date = LocalDateTime.now();
    }
	
}
