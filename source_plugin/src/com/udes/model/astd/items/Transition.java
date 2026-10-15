package com.udes.model.astd.items;

import java.util.ArrayList;

import com.udes.model.astd.base.ASTD;
import com.udes.utils.Utils;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;

public class Transition {

    private Arrow arrow;
    private Event event;
    private String guard;
    private Action action;
    private boolean isfinal;

    public Transition(Arrow arrow, Event event, String guard, Action action, boolean isfinal) {
        this.arrow = arrow;
        this.event = event;
        this.guard = guard;
        this.action = action;
        this.isfinal = isfinal;
    }

    public Transition() {}

    public Arrow getArrow() {
        return arrow;
    }

    public void setArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getGuard() {
        if(guard == null || guard.equals("")) {
        	return "btrue";
        }
        else {
        	return guard;
        }
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isFinal() {
        return isfinal;
    }

    public void setFinal(boolean isfinal) {
        this.isfinal = isfinal;
    }
    
    public ArrayList<String> invalidBElements(String autName) {
    	ArrayList<String> answer = new ArrayList<String>();
    	if (arrow instanceof Local) {
    		Local ar = (Local) arrow;
        	try {
        		if (guard != null && !guard.isBlank()) {
        			Utils.generateTreeFromInv(guard);
        		}
        	} catch (Exception e) {
        		answer.add("Invalid guard in astd " + autName + " for transition " + event.getName() + " between " + ar.getS1() + " to " + ar.getS2());
        	}
        	try {
        		if (action != null && action.getCode() != null && !action.getCode().isBlank()) {
        			Utils.generateTreeFromSub(action.getCode());
        		}
        	} catch (Exception e) {
        		answer.add("Invalid action in astd " + autName + " for transition " + event.getName() + " between " + ar.getS1() + " to " + ar.getS2());
        	}
    	}
    	else {

        	try {
        		if (guard != null && !guard.isBlank()) {
        			Utils.generateTreeFromInv(guard);
        		}
        	} catch (Exception e) {
        		answer.add("Invalid guard in astd " + autName + " for transition" + event.getName());
        	}
        	try {
        		if (action != null && action.getCode() != null && !action.getCode().isBlank()) {
        			Utils.generateTreeFromSub(action.getCode());
        		}
        	} catch (Exception e) {
        		answer.add("Invalid action in astd " + autName + " for transition " + event.getName());
        	}
    	}
    	return answer;
    }

	public boolean startFrom(String name) {
		return this.getArrow().startFrom(name);
	}

	public String getTarget() {
		return this.getArrow().getTarget();
	}

	public boolean isLoop() {
		return this.getArrow().isLoop();
	}

	public String getSource() {
		return this.getArrow().getSource();
	}
}
