package com.see_nior.seeniorClient.enums;

public enum SqlResult {

	SUCCESS(true),
	FAIL(false);
	
	private boolean value;
	
	private SqlResult(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
}
