package com.udes.model.astd.items;

import com.udes.utils.Utils;

public class Arrow {


	public String getSource() {
		return null;
	}
	
	public String getTarget() {
		return null;
	}

	public boolean isLoop() {
		return false;
	}

	public boolean startFrom(String name) {
		return this.getSource().equals(name);
	}
}
