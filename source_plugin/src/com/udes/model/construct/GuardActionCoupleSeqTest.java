package com.udes.model.construct;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.udes.model.astd.items.Action;
import com.udes.utils.Utils;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.Start;
import de.be4.classicalb.core.parser.util.PrettyPrinter;

class GuardActionCoupleSeqTest {

	@Test
	void testHasOnlyNullPredicate() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		assertFalse(sgac.hasOnlyNullPredicate());
		
		sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac.add(new GuardActionCouple(null, new Action("x := x + 2")));
		sgac.add(new GuardActionCouple(null, null));
		assertTrue(sgac.hasOnlyNullPredicate());
		
		sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgac.add(new GuardActionCouple(null, null));
		assertFalse(sgac.hasOnlyNullPredicate());
	}

	@Test
	void testGenerateAllHypGuard() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action("x := 2")));
		
		Start astRes = new Start();
		try {
			astRes = Utils.generateTreeFromInv(sgac.generateAllHypGuard());
		} catch(BCompoundException e) {
			e.printStackTrace(System.out);
			fail("Error in generation 1");
		}
		PrettyPrinter pp = new PrettyPrinter();
        astRes.apply(pp);
    	
    	Start astExp = new Start();
		try {
			astExp = Utils.generateTreeFromInv("x = 2 & 3 = 3");
		} catch(BCompoundException e) {
			e.printStackTrace(System.out);
			fail("Error in generation 2");
		}
		PrettyPrinter pp2 = new PrettyPrinter();
        astExp.apply(pp2);
    
        assertEquals(pp.getPrettyPrint(), pp2.getPrettyPrint());
        
        
        
        sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac.add(new GuardActionCouple(null, new Action("x := 2")));
		
		astRes = new Start();
		try {
			astRes = Utils.generateTreeFromInv(sgac.generateAllHypGuard());
		} catch(BCompoundException e) {
			e.printStackTrace(System.out);
			fail("Error in generation 1");
		}
		pp = new PrettyPrinter();
        astRes.apply(pp);
    	
    	astExp = new Start();
		try {
			astExp = Utils.generateTreeFromInv("btrue");
		} catch(BCompoundException e) {
			e.printStackTrace(System.out);
			fail("Error in generation 2");
		}
		pp2 = new PrettyPrinter();
        astExp.apply(pp2);
    
        assertEquals(pp.getPrettyPrint(), pp2.getPrettyPrint());
		
		
    	
	}

	@Test
	void testGenerateAllActions() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action(null)));
		sgac.add(new GuardActionCouple(null, new Action("x := 2")));
		
		ArrayList<Action> res = sgac.generateAllActions();
		ArrayList<Action> exp = new ArrayList<Action>();
		exp.add(new Action("x := 3"));
		exp.add(new Action("x := 2"));
		
		assertEquals(res, exp);
		
		sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action(null)));
		sgac.add(new GuardActionCouple(null, new Action("x := 2")));
		
		res = sgac.generateAllActions();
		exp = new ArrayList<Action>();
		exp.add(new Action("x := 2"));
		exp.add(new Action("x := 3"));
		
		assertNotEquals(res, exp);		
	}

	@Test
	void testGetActionsInSeq() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action(null)));
		sgac.add(new GuardActionCouple(null, new Action("x := 2")));
		
		Action res = sgac.getActionsInSeq();
		Action exp1 = new Action("x := 3");
		Action exp2 = new Action("x := 2");
		Action exp = Utils.concatActions(exp1, exp2);
		
		assertEquals(res, exp);
		
		sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action(null)));
		sgac.add(new GuardActionCouple(null, new Action("x := 2")));
		
		res = sgac.getActionsInSeq();
		exp1 = new Action("x := 3");
		exp2 = new Action(null);
		exp = Utils.concatActions(exp1, exp2);
		
		assertNotEquals(res, exp);
	}

	@Test
	void testGetPredicatesInConj() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action(null)));
		sgac.add(new GuardActionCouple(null, new Action("x := 2")));
		
		String res = sgac.getPredicatesInConj();
		String exp1 = "x = 2";
		String exp2 = "x = 3";
		String exp = Utils.formatConcatInvariant(exp1, exp2);
		
		assertEquals(res, exp);
		
		exp1 = "x = 2";
		exp2 = null;
		exp = Utils.formatConcatInvariant(exp1, exp2);
		
		assertNotEquals(res, exp);
	}

	@Test
	void testGetPredicatesInDisj() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple("x = 2", new Action("x := 3")));
		sgac.add(new GuardActionCouple("x = 3", new Action(null)));
		sgac.add(new GuardActionCouple(null, new Action("x := 2")));
		
		String res = sgac.getPredicatesInDisj();
		String exp1 = "x = 2";
		String exp2 = "x = 3";
		String exp = Utils.formatDisjonctionInvariant(exp1, exp2);
		
		assertEquals(res, exp);
		
		exp1 = "x = 2";
		exp2 = null;
		exp = Utils.formatDisjonctionInvariant(exp1, exp2);
		
		assertNotEquals(res, exp);
	}

}
