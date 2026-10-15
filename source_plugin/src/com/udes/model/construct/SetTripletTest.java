package com.udes.model.construct;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.udes.model.astd.base.ASTD;
import com.udes.model.astd.items.Action;
import com.udes.model.il.terms.Bool;
import com.udes.parser.ASTDParser;

class SetTripletTest {

	ASTD root;
	ArrayList<ASTD> allASTD;

	ASTD setUp() {
		File file = new File("example\\test1.json");
		Bool timed = new Bool(false);
		String specPath = file.getAbsolutePath();
		ASTDParser p = new ASTDParser(specPath, null);
		ASTD a = p.parse(timed);
		
		return a;
	}
	
	ArrayList<ASTD> setUp2() {
		return root.getAllASTD();
	}

	@Test
	void testExpand() {
		root = this.setUp();
		allASTD = this.setUp2();
		SetTriplet st = new SetTriplet();
		
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		sgac.add(new GuardActionCouple(null, new Action("y := y+1")));
		boolean b = false;
		HashSet<ASTD> s = new HashSet<ASTD>();
		s.add(allASTD.get(2));
		Triplet t = new Triplet(sgac,b,s);
		

		GuardActionCoupleSeq sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac1.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgac1.add(new GuardActionCouple(null, null));
		boolean b1 = true;
		HashSet<ASTD> s1 = new HashSet<ASTD>();
		s1.add(allASTD.get(0));
		s1.add(allASTD.get(1));
		Triplet t1 = new Triplet(sgac1,b1,s1);
		
		
		GuardActionCoupleSeq sgac2 = new GuardActionCoupleSeq();
		sgac2.add(new GuardActionCouple(null, new Action("x := 3; y := 6x+1")));
		sgac2.add(new GuardActionCouple("x = 3 & not(x = 4)", null));
		boolean b2 = false;
		HashSet<ASTD> s2 = new HashSet<ASTD>();
		s2.add(allASTD.get(0));
		s2.add(allASTD.get(1));
		Triplet t2 = new Triplet(sgac2,b2,s2);
		
		st.add(t1);
		st.add(t2);
		st.expand(t);
		
		

		SetTriplet stexp = new SetTriplet();
		
		GuardActionCoupleSeq sgac1exp = new GuardActionCoupleSeq();
		sgac1exp.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac1exp.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgac1exp.add(new GuardActionCouple(null, null));
		sgac1exp.add(new GuardActionCouple(null, new Action("y := y+1")));
		boolean b1exp = true;
		HashSet<ASTD> s1exp = new HashSet<ASTD>();
		s1exp.add(allASTD.get(0));
		s1exp.add(allASTD.get(1));
		s1exp.add(allASTD.get(2));
		Triplet t1exp = new Triplet(sgac1exp,b1exp,s1exp);
		
		
		GuardActionCoupleSeq sgac2exp = new GuardActionCoupleSeq();
		sgac2exp.add(new GuardActionCouple(null, new Action("x := 3; y := 6x+1")));
		sgac2exp.add(new GuardActionCouple("x = 3 & not(x = 4)", null));
		boolean b2exp = false;
		HashSet<ASTD> s2exp = new HashSet<ASTD>();
		s2exp.add(allASTD.get(0));
		s2exp.add(allASTD.get(1));
		s2exp.add(allASTD.get(2));
		Triplet t2exp = new Triplet(sgac2exp,b2exp,s2exp);
		
		stexp.add(t1exp);
		stexp.add(t2exp);
		
		GuardActionCoupleSeq sgac3 = new GuardActionCoupleSeq();
		sgac3.add(new GuardActionCouple(null, new Action("x := 3; y := 6x+1")));
		sgac3.add(new GuardActionCouple("x = 3 & not(x = 4)", null));
		boolean b3 = false;
		HashSet<ASTD> s3 = new HashSet<ASTD>();
		s3.add(allASTD.get(0));
		s3.add(allASTD.get(1));
		Triplet t3 = new Triplet(sgac3,b3,s3);
		SetTriplet st3 = new SetTriplet();
		st3.add(t3);
		GuardActionCoupleSeq sgac4 = new GuardActionCoupleSeq();
		sgac4.add(new GuardActionCouple(null, new Action("x := 3; y := 6x+1")));
		sgac4.add(new GuardActionCouple("x = 3 & not(x = 4)", null));
		boolean b4 = false;
		HashSet<ASTD> s4 = new HashSet<ASTD>();
		s4.add(allASTD.get(0));
		s4.add(allASTD.get(1));
		Triplet t4 = new Triplet(sgac4,b4,s4);
		SetTriplet st4 = new SetTriplet();
		st4.add(t4);
		
		assertEquals(st3,st4);
		
		//assertEquals(st, stexp);
	}

	@Test
	void testExpandGuard() {
		fail("Not yet implemented");
	}

	@Test
	void testGeneratePOFromTriplets() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllOutStates() {
		fail("Not yet implemented");
	}

	@Test
	void testAllTransitionTriplets() {
		fail("Not yet implemented");
	}

	@Test
	void testNonTransitionTriplet() {
		fail("Not yet implemented");
	}

}
