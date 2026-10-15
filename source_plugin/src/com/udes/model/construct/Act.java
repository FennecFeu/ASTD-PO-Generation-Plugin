package com.udes.model.construct;

public class Act {
	
	private String code;
		
	public Act() {
		this.code = null;
	}
		
	public Act(String code) {
		this.code = code;
	}

	public String getAct() {
		return code;
	}

	public void setPredicate(String code) {
		this.code = code;
	}
}