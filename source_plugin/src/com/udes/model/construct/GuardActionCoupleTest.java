package com.udes.model.construct;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.udes.model.astd.items.Action;

class GuardActionCoupleTest {

	@Test
	void testPredIsNull() {
		GuardActionCouple gact = new GuardActionCouple(null, null);
		assertTrue(gact.predIsNull());
		
		GuardActionCouple gacf = new GuardActionCouple("btrue", null);
		assertFalse(gacf.predIsNull());
	}

	@Test
	void testActIsNull() {
		GuardActionCouple gact = new GuardActionCouple(null, null);
		assertTrue(gact.actIsNull());
		
		GuardActionCouple gacf = new GuardActionCouple(null, new Action("x := x+1"));
		assertFalse(gacf.actIsNull());
	}

	@Test
	void testPredNonTrivial() {
		GuardActionCouple gacn = new GuardActionCouple(null, null);
		assertFalse(gacn.predNonTrivial());

		GuardActionCouple gacv = new GuardActionCouple("", null);
		assertFalse(gacv.predNonTrivial());
		
		GuardActionCouple gact = new GuardActionCouple("btrue", null);
		assertFalse(gact.predNonTrivial());

		GuardActionCouple gac = new GuardActionCouple("x = 2", null);
		assertTrue(gac.predNonTrivial());
	}

	@Test
	void testActNonTrivial() {
		GuardActionCouple gacn = new GuardActionCouple(null, null);
		assertFalse(gacn.actNonTrivial());
		
		GuardActionCouple gact = new GuardActionCouple(null, new Action(null));
		assertFalse(gact.actNonTrivial());
		
		GuardActionCouple gac = new GuardActionCouple(null, new Action("x := x+2"));
		assertTrue(gac.actNonTrivial());
	}

}
