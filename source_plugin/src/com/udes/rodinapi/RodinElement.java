package com.udes.rodinapi;

public class RodinElement {
	private String label;
	private String comment;
	private String content;
	
	public RodinElement(String label, String comment, String content) {
		this.label = label;
		this.comment = comment;
		this.content = content;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}