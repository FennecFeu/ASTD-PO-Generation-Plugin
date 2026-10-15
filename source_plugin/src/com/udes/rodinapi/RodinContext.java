package com.udes.rodinapi;

import java.util.ArrayList;
import org.rodinp.core.IRodinFile;

public class RodinContext {
	
	private String name;
	private String extendedContext ;
	private ArrayList<String> sets ;
	private ArrayList<String> constants ;
	private ArrayList<RodinElement> axioms ;
	private ArrayList<RodinElement> theorems ;
	
	public RodinContext(String name) {
		this.name = name;
		this.extendedContext = null;
		this.sets = new ArrayList<String>();
		this.constants = new ArrayList<String>();
		this.axioms = new ArrayList<RodinElement>();
		this.theorems = new ArrayList<RodinElement>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addExtendedContext(String extendedContext) {
		this.extendedContext = extendedContext;
	}
	
	public void addSet(String set) {
		this.sets.add(set);
	}
	
	public void addConstant(String constant) {
		this.constants.add(constant);
	}
	
	public void addAxiom(RodinElement axiom) {
		this.axioms.add(axiom);
	}
	
	public void addTheorem(RodinElement theorem) {
		this.theorems.add(theorem);
	}
	
	public void toIRodinFile(IRodinFile rodinFile) {
		if(this.extendedContext != null) RodinCreation.addExtendsContext(this.extendedContext,rodinFile,"");
		
		for(String setName : this.sets)
			RodinCreation.addSet(setName, rodinFile, "");
		
		for(String consName : this.constants)
			RodinCreation.addConstant(consName, rodinFile, "");
		
		int i = 1;
		for(RodinElement axExpression : this.axioms) {
			RodinCreation.addAxiom(axExpression.getContent(), "axm" + i, rodinFile, axExpression.getComment());
			i++;
		}
		
		int j = 1;
		for(RodinElement thExpression : this.theorems) {
			RodinCreation.addTheorem(thExpression.getContent(), "th" + j, rodinFile, thExpression.getComment());
			j++;
		}
	}
}