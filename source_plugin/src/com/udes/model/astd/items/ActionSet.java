package com.udes.model.astd.items;

import java.util.ArrayList;

import com.udes.model.astd.base.ASTD;
import com.udes.utils.Utils;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;

public class ActionSet {
    private Action entry;
    private Action stay;
    private Action exit;

    public ActionSet() {}
    public ActionSet(Action entry, Action stay, Action exit) {
        this.stay = stay;
        this.entry = entry;
        this.exit = exit;
    }

    public Action getEntry() {
        return entry;
    }

    public void setEntry(Action entry) {
        this.entry = entry;
    }

    public Action getExit() {
        return exit;
    }

    public void setExit(Action exit) {
        this.exit = exit;
    }

    public Action getStay() {
        return stay;
    }

    public void setStay(Action stay) {
        this.stay = stay;
    }
    
    public ArrayList<String> invalidBElements(String autName, String elemName) {
    	ArrayList<String> answer = new ArrayList<String>();
    	try {
    		if (entry != null && entry.getCode() != null && !entry.getCode().isBlank()) {
        		Utils.generateTreeFromSub(entry.getCode());
    		}
    	} catch (Exception e) {
    		answer.add("Invalid entry action for " + elemName + " in astd " + autName);
    	}
    	try {
    		if (stay != null && stay.getCode() != null && !stay.getCode().isBlank()) {
        		Utils.generateTreeFromSub(stay.getCode());
    		}
    	} catch (Exception e) {
    		answer.add("Invalid stay action for " + elemName + " in astd " + autName);
    	}
    	try {
    		if (exit != null && exit.getCode() != null && !exit.getCode().isBlank()) {
        		Utils.generateTreeFromSub(exit.getCode());
    		}
    	} catch (Exception e) {
    		answer.add("Invalid exit action for " + elemName + " in astd " + autName);
    	}
    	return answer;
    }
}
