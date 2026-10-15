package com.udes.model.construct;

public class Pred {
	private String predicate;
	
	public Pred() {
		this.predicate = null;
	}
	
	public Pred(String predicate) {
		this.predicate = predicate;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
}
