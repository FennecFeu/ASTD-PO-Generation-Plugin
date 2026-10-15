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

class TripletTest {
	
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
	void testConc() {
		root = this.setUp();
		allASTD = this.setUp2();
		GuardActionCoupleSeq sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac1.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgac1.add(new GuardActionCouple(null, null));
		boolean b1 = false;
		HashSet<ASTD> s1 = new HashSet<ASTD>();
		s1.add(allASTD.get(0));
		s1.add(allASTD.get(1));
		Triplet t1 = new Triplet(sgac1,b1,s1);
		
		GuardActionCoupleSeq sgac2 = new GuardActionCoupleSeq();
		sgac2.add(new GuardActionCouple("y > 2", new Action("x := 3")));
		sgac2.add(new GuardActionCouple(null, new Action("y := 10 / (y-2)")));
		boolean b2 = true;
		HashSet<ASTD> s2 = new HashSet<ASTD>();
		s2.add(allASTD.get(3));
		s2.add(allASTD.get(4));
		Triplet t2 = new Triplet(sgac2,b2,s2);
		
		t1.conc(t2);
		
		GuardActionCoupleSeq sgacexp = new GuardActionCoupleSeq();
		sgacexp.add(new GuardActionCouple(null, new Action("x := 3")));
		sgacexp.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgacexp.add(new GuardActionCouple(null, null));
		sgacexp.add(new GuardActionCouple("y > 2", new Action("x := 3")));
		sgacexp.add(new GuardActionCouple(null, new Action("y := 10 / (y-2)")));
		boolean bexp = true;
		HashSet<ASTD> sexp = new HashSet<ASTD>();
		sexp.add(allASTD.get(0));
		sexp.add(allASTD.get(1));
		sexp.add(allASTD.get(3));
		sexp.add(allASTD.get(4));
		Triplet texp = new Triplet(sgacexp,bexp,sexp);
		
		assertEquals(t1, texp);
		
		GuardActionCoupleSeq sgacexp2 = new GuardActionCoupleSeq();
		sgacexp2.add(new GuardActionCouple(null, new Action("x := 3")));
		sgacexp2.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgacexp2.add(new GuardActionCouple(null, null));
		sgacexp2.add(new GuardActionCouple("y > 1", new Action("x := 3")));
		sgacexp2.add(new GuardActionCouple(null, new Action("y := 10 / (y-2)")));
		boolean bexp2 = true;
		HashSet<ASTD> sexp2 = new HashSet<ASTD>();
		sexp2.add(allASTD.get(0));
		sexp2.add(allASTD.get(1));
		sexp2.add(allASTD.get(3));
		sexp2.add(allASTD.get(4));
		Triplet texp2 = new Triplet(sgacexp2,bexp2,sexp2);

		GuardActionCoupleSeq sgacexp3 = new GuardActionCoupleSeq();
		sgacexp3.add(new GuardActionCouple(null, new Action("x := 3")));
		sgacexp3.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgacexp3.add(new GuardActionCouple(null, null));
		sgacexp3.add(new GuardActionCouple("y > 2", new Action("x := 3")));
		sgacexp3.add(new GuardActionCouple(null, new Action("y := 10 / (y-2)")));
		boolean bexp3 = false;
		HashSet<ASTD> sexp3 = new HashSet<ASTD>();
		sexp3.add(allASTD.get(0));
		sexp3.add(allASTD.get(1));
		sexp3.add(allASTD.get(3));
		sexp3.add(allASTD.get(4));
		Triplet texp3 = new Triplet(sgacexp3,bexp3,sexp3);

		GuardActionCoupleSeq sgacexp4 = new GuardActionCoupleSeq();
		sgacexp4.add(new GuardActionCouple(null, new Action("x := 3")));
		sgacexp4.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgacexp4.add(new GuardActionCouple(null, null));
		sgacexp4.add(new GuardActionCouple("y > 2", new Action("x := 3")));
		sgacexp4.add(new GuardActionCouple(null, new Action("y := 10 / (y-2)")));
		boolean bexp4 = true;
		HashSet<ASTD> sexp4 = new HashSet<ASTD>();
		sexp4.add(allASTD.get(0));
		sexp4.add(allASTD.get(2));
		sexp4.add(allASTD.get(3));
		sexp4.add(allASTD.get(4));
		Triplet texp4 = new Triplet(sgacexp4,bexp4,sexp4);

		assertNotEquals(t1, texp2);
		assertNotEquals(t1, texp3);
		assertNotEquals(t1, texp4);
	}

	@Test
	void testAppend() {
		root = this.setUp();
		allASTD = this.setUp2();
		GuardActionCoupleSeq sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac1.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgac1.add(new GuardActionCouple(null, null));
		boolean b1 = false;
		HashSet<ASTD> s1 = new HashSet<ASTD>();
		s1.add(allASTD.get(0));
		s1.add(allASTD.get(1));
		Triplet t1 = new Triplet(sgac1,b1,s1);
		
		GuardActionCoupleSeq sgac2 = new GuardActionCoupleSeq();
		sgac2.add(new GuardActionCouple("y > 2", new Action("x := 3")));
		sgac2.add(new GuardActionCouple(null, new Action("y := 10 / (y-2)")));
		boolean b2 = true;
		HashSet<ASTD> s2 = new HashSet<ASTD>();
		s2.add(allASTD.get(3));
		s2.add(allASTD.get(4));
		Triplet t2 = new Triplet(sgac2,b2,s2);
		
		GuardActionCoupleSeq sgac3 = new GuardActionCoupleSeq();
		sgac3.add(new GuardActionCouple(null, new Action("y := y + 1")));
		boolean b3 = false;
		HashSet<ASTD> s3 = new HashSet<ASTD>();
		Triplet t3 = new Triplet(sgac3,b3,s3);
		
		
		
		GuardActionCoupleSeq sgac4 = new GuardActionCoupleSeq();
		sgac4.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac4.add(new GuardActionCouple("x = 3", new Action("x := x + 2")));
		sgac4.add(new GuardActionCouple(null, null));
		boolean b4 = false;
		HashSet<ASTD> s4 = new HashSet<ASTD>();
		s4.add(allASTD.get(0));
		s4.add(allASTD.get(1));
		Triplet t4 = new Triplet(sgac4,b4,s4);
		
		GuardActionCoupleSeq sgac5 = new GuardActionCoupleSeq();
		sgac5.add(new GuardActionCouple("y > 2", new Action("x := 3")));
		sgac5.add(new GuardActionCouple(null, new Action("y := 10 / (y-2)")));
		sgac5.add(new GuardActionCouple(null, new Action("y := y + 1")));
		boolean b5 = true;
		HashSet<ASTD> s5 = new HashSet<ASTD>();
		s5.add(allASTD.get(3));
		s5.add(allASTD.get(4));
		Triplet t5 = new Triplet(sgac5,b5,s5);
		
		t1.append(t3);
		t2.append(t3);
		
		assertEquals(t1, t4);
		assertEquals(t2, t5);
	}

	@Test
	void testFlattenAND() {
		root = this.setUp();
		allASTD = this.setUp2();
		GuardActionCoupleSeq sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac1.add(new GuardActionCouple("x = 3", new Action("y := x + 2")));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple("y >= 5 & y != 12", new Action("z := 5")));
		boolean b1 = false;
		HashSet<ASTD> s1 = new HashSet<ASTD>();
		s1.add(allASTD.get(0));
		s1.add(allASTD.get(1));
		Triplet t1 = new Triplet(sgac1,b1,s1);
		t1.flattenAND();
		
		GuardActionCoupleSeq sgacexp = new GuardActionCoupleSeq();
		sgacexp.add(new GuardActionCouple(null, new Action("x := 3;y := x + 2;z := 5")));
		sgacexp.add(new GuardActionCouple("(x = 3) & (y >= 5 & y != 12)", null));
		boolean bexp = false;
		HashSet<ASTD> sexp = new HashSet<ASTD>();
		sexp.add(allASTD.get(0));
		sexp.add(allASTD.get(1));
		Triplet texp = new Triplet(sgacexp,bexp,sexp);
		
		GuardActionCoupleSeq sgacexp2 = new GuardActionCoupleSeq();
		sgacexp2.add(new GuardActionCouple(null, new Action("y := x + 2;x := 3;z := 5")));
		sgacexp2.add(new GuardActionCouple("(x = 3) & (y >= 5 & y != 12)", null));
		boolean bexp2 = false;
		HashSet<ASTD> sexp2 = new HashSet<ASTD>();
		sexp2.add(allASTD.get(0));
		sexp2.add(allASTD.get(1));
		Triplet texp2 = new Triplet(sgacexp2,bexp2,sexp2);
		
		assertEquals(t1, texp);
		assertNotEquals(t1, texp2);
	}

	@Test
	void testFlattenOR() {
		root = this.setUp();
		allASTD = this.setUp2();
		GuardActionCoupleSeq sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac1.add(new GuardActionCouple("x = 3", new Action("y := x + 2")));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple("y >= 5 & y != 12", new Action("z := 5")));
		boolean b1 = false;
		HashSet<ASTD> s1 = new HashSet<ASTD>();
		s1.add(allASTD.get(0));
		s1.add(allASTD.get(1));
		Triplet t1 = new Triplet(sgac1,b1,s1);
		t1.flattenOR();
		
		GuardActionCoupleSeq sgacexp = new GuardActionCoupleSeq();
		sgacexp.add(new GuardActionCouple(null, new Action("x := 3;y := x + 2;z := 5")));
		sgacexp.add(new GuardActionCouple("(x = 3) or (y >= 5 & y != 12)", null));
		boolean bexp = false;
		HashSet<ASTD> sexp = new HashSet<ASTD>();
		sexp.add(allASTD.get(0));
		sexp.add(allASTD.get(1));
		Triplet texp = new Triplet(sgacexp,bexp,sexp);
		
		GuardActionCoupleSeq sgacexp2 = new GuardActionCoupleSeq();
		sgacexp2.add(new GuardActionCouple(null, new Action("x := 3;y := x + 2;z := 5")));
		sgacexp2.add(new GuardActionCouple("(y >= 5 & y != 12) or (x = 3)", null));
		boolean bexp2 = false;
		HashSet<ASTD> sexp2 = new HashSet<ASTD>();
		sexp2.add(allASTD.get(0));
		sexp2.add(allASTD.get(1));
		Triplet texp2 = new Triplet(sgacexp2,bexp2,sexp2);
		
		assertEquals(t1, texp);
		assertNotEquals(t1, texp2);
	}

	@Test
	void testAppendGuard() {
		root = this.setUp();
		allASTD = this.setUp2();
		GuardActionCoupleSeq sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple(null, new Action("x := 3")));
		sgac1.add(new GuardActionCouple("x = 3", new Action("y := x + 2")));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple("y >= 5 & y != 12", new Action("z := 5")));
		boolean b1 = true;
		HashSet<ASTD> s1 = new HashSet<ASTD>();
		s1.add(allASTD.get(0));
		s1.add(allASTD.get(1));
		Triplet t1 = new Triplet(sgac1,b1,s1);
		
		String guard = "c < 100";
		t1.appendGuard(guard);
		
		GuardActionCoupleSeq sgacexp = new GuardActionCoupleSeq();
		sgacexp.add(new GuardActionCouple("c < 100", null));
		sgacexp.add(new GuardActionCouple(null, new Action("x := 3")));
		sgacexp.add(new GuardActionCouple("x = 3", new Action("y := x + 2")));
		sgacexp.add(new GuardActionCouple(null, null));
		sgacexp.add(new GuardActionCouple("y >= 5 & y != 12", new Action("z := 5")));
		boolean bexp = true;
		HashSet<ASTD> sexp = new HashSet<ASTD>();
		sexp.add(allASTD.get(0));
		sexp.add(allASTD.get(1));
		Triplet texp = new Triplet(sgacexp,bexp,sexp);
		
		assertEquals(t1, texp);
		
		
		GuardActionCoupleSeq sgac2 = new GuardActionCoupleSeq();
		sgac2.add(new GuardActionCouple(null, new Action("x := 3;y := x + 2;z := 5")));
		sgac2.add(new GuardActionCouple("(y >= 5 & y != 12) or (x = 3)", null));
		boolean b2 = false;
		HashSet<ASTD> s2 = new HashSet<ASTD>();
		s2.add(allASTD.get(0));
		s2.add(allASTD.get(1));
		Triplet t2 = new Triplet(sgac2,b2,s2);
		t2.appendGuard(guard);
		
		GuardActionCoupleSeq sgacexp2 = new GuardActionCoupleSeq();
		sgacexp2.add(new GuardActionCouple(null, new Action("x := 3;y := x + 2;z := 5")));
		sgacexp2.add(new GuardActionCouple("(not(c < 100)) or ((y >= 5 & y != 12) or (x = 3))", null));
		boolean bexp2 = false;
		HashSet<ASTD> sexp2 = new HashSet<ASTD>();
		sexp2.add(allASTD.get(0));
		sexp2.add(allASTD.get(1));
		Triplet texp2 = new Triplet(sgacexp2,bexp2,sexp2);
		
		assertEquals(t2, texp2);
		
		
		GuardActionCoupleSeq sgac3 = new GuardActionCoupleSeq();
		sgac3.add(new GuardActionCouple(null, new Action("x := 3;y := x + 2;z := 5")));
		sgac3.add(new GuardActionCouple(null, null));
		boolean b3 = false;
		HashSet<ASTD> s3 = new HashSet<ASTD>();
		s3.add(allASTD.get(0));
		s3.add(allASTD.get(1));
		Triplet t3 = new Triplet(sgac3,b3,s3);
		t3.appendGuard(guard);
		
		GuardActionCoupleSeq sgacexp3 = new GuardActionCoupleSeq();
		sgacexp3.add(new GuardActionCouple(null, new Action("x := 3;y := x + 2;z := 5")));
		sgacexp3.add(new GuardActionCouple(null, null));
		boolean bexp3 = false;
		HashSet<ASTD> sexp3 = new HashSet<ASTD>();
		sexp3.add(allASTD.get(0));
		sexp3.add(allASTD.get(1));
		Triplet texp3 = new Triplet(sgacexp3,bexp3,sexp3);
		
		assertEquals(t3, texp3);
		
	}

	@Test
	void testGeneratePOFromTriplet() {
		root = this.setUp();
		allASTD = this.setUp2();
		GuardActionCoupleSeq sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple("Idle = 1", new Action("Idle := 0")));
		sgac1.add(new GuardActionCouple(null, new Action("T := T + 25")));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple(null, new Action("w := 0")));
		sgac1.add(new GuardActionCouple(null, new Action("y := T-25")));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple(null, null));
		boolean b1 = true;
		HashSet<ASTD> sa1 = new HashSet<ASTD>();
		sa1.add(allASTD.get(0));
		sa1.add(allASTD.get(1));
		sa1.add(allASTD.get(2));
		sa1.add(allASTD.get(3));
		sa1.add(allASTD.get(4));
		sa1.add(allASTD.get(6));
		sa1.add(allASTD.get(7));
		Triplet t1 = new Triplet(sgac1,b1,sa1);
		HashSet<ASTD> sd1 = new HashSet<ASTD>();
		sd1.add(allASTD.get(0));
		sd1.add(allASTD.get(9));
		
		String po1 = t1.generatePOFromTriplet(root, sd1);
		
		String poexp = "!T,Idle,p.((((((((T : INT) & (Idle : INT)) & (p : INT)) & (T >= 0 & T < 213 & Idle : 0..1 & p : 0..1)) & (T : 0..25 & Idle = 1)) & ((Idle = 1))) => (T+25:INT & 0:INT & p:INT & (T+25>=0 & T+25<213 & 0:0..1 & p:0..1) & (T+25>=25 & (0:INT & 0>=0 & (T+25-25:INT & T+25-25:-1..25 & T+25-25=T+25-25))))))";
		
		assertEquals(po1, poexp);
		
		
		sgac1 = new GuardActionCoupleSeq();
		sgac1.add(new GuardActionCouple("Idle = 1", new Action("Idle := 0")));
		sgac1.add(new GuardActionCouple(null, new Action("T := T + 25")));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple(null, new Action("w := 0")));
		sgac1.add(new GuardActionCouple(null, new Action("y := T-25")));
		sgac1.add(new GuardActionCouple(null, null));
		sgac1.add(new GuardActionCouple("y = 0", null));
		sgac1.add(new GuardActionCouple(null, null));
		b1 = true;
		sa1 = new HashSet<ASTD>();
		sa1.add(allASTD.get(0));
		sa1.add(allASTD.get(1));
		sa1.add(allASTD.get(2));
		sa1.add(allASTD.get(3));
		sa1.add(allASTD.get(4));
		sa1.add(allASTD.get(6));
		sa1.add(allASTD.get(7));
		t1 = new Triplet(sgac1,b1,sa1);
		sd1 = new HashSet<ASTD>();
		sd1.add(allASTD.get(0));
		sd1.add(allASTD.get(9));
		
		po1 = t1.generatePOFromTriplet(root, sd1);
		
		poexp = "!T,Idle,p.((((((((T : INT) & (Idle : INT)) & (p : INT)) & (T >= 0 & T < 213 & Idle : 0..1 & p : 0..1)) & (T : 0..25 & Idle = 1)) & (((Idle = 1)) & ((T+25-25=0)))) => (T+25:INT & 0:INT & p:INT & (T+25>=0 & T+25<213 & 0:0..1 & p:0..1) & (T+25>=25 & (0:INT & 0>=0 & (T+25-25:INT & T+25-25:-1..25 & T+25-25=T+25-25))))))";
		
		assertEquals(po1, poexp);
	}

}
