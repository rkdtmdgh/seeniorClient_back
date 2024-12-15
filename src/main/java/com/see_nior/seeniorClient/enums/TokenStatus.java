package com.see_nior.seeniorClient.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenStatus {
	
    AUTHENTICATED,
    EXPIRED,
    INVALID
    
}
