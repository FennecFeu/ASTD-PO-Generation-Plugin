package com.udes.model.construct;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import com.udes.model.astd.base.ASTD;
import com.udes.model.astd.items.Action;
import com.udes.utils.Utils;

public class Triplet {
	
	private GuardActionCoupleSeq guardActionSeq;
	private Boolean triggeredTrans;
	private HashSet<ASTD> targetStates;
	
	public Triplet(GuardActionCoupleSeq a, Boolean b, HashSet<ASTD> c) {
		guardActionSeq = a;
		triggeredTrans = b;
		targetStates = c;
	}
	
	public Triplet(Triplet t) {
		guardActionSeq = new GuardActionCoupleSeq(t.getGuardActionSeq());
		triggeredTrans = t.hasTriggeredTrans();
		targetStates = new HashSet<ASTD>(t.getTargetStates());
	}

	public void set(Triplet t) {
		guardActionSeq = t.getGuardActionSeq();
		triggeredTrans = t.hasTriggeredTrans();
		targetStates = t.getTargetStates();
	}
	
	public GuardActionCoupleSeq getGuardActionSeq() {
		return guardActionSeq;
	}
	public void setGuardActionSeq(GuardActionCoupleSeq guardActionSeq) {
		this.guardActionSeq = guardActionSeq;
	}
	
	public Boolean hasTriggeredTrans() {
		return triggeredTrans;
	}
	public void setTriggeredTrans(Boolean triggeredTrans) {
		this.triggeredTrans = triggeredTrans;
	}
	
	public HashSet<ASTD> getTargetStates() {
		return targetStates;
	}
	public void setTargetStates(HashSet<ASTD> targetStates) {
		this.targetStates = targetStates;
	}
	
	public boolean hasOnlyNullPredicate() {
		return this.getGuardActionSeq().hasOnlyNullPredicate();
	}
	
	/** Concatenate two triplets according to the rule of concatenation for triplets*/
	public void conc(Triplet t) {
		guardActionSeq.addAll(t.getGuardActionSeq());
		triggeredTrans = triggeredTrans || t.hasTriggeredTrans();
		targetStates.addAll(t.getTargetStates());
	}
	
	/** Concatenate two triplets if the first one corresponds to a transition
	 * else it just merges states
	 * @param t
	 */
	public void append(Triplet t) {
		GuardActionCoupleSeq a = new GuardActionCoupleSeq();
		if(triggeredTrans) {
			a = t.getGuardActionSeq();
		}
		this.conc(new Triplet(a, triggeredTrans, t.getTargetStates()));
	}
	
	/**
	 * Transform a triplet into another triplet.
	 * The first element of the new triplet is a sequence with only two pairs.
	 * The first pair contains the concatenation of all actions of the old triplet.
	 * The second pair contains the conjunction of all predicates of the old triplet.
	 */
	public void flattenAND() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		
		sgac.add(null, this.getGuardActionSeq().getActionsInSeq());
		
		sgac.add(this.getGuardActionSeq().getPredicatesInConj(), null);
		
		this.setGuardActionSeq(sgac);
	}
	
	/**
	 * Transform a triplet into another triplet.
	 * The first element of the new triplet is a sequence with only two pairs.
	 * The first pair contains the concatenation of all actions of the old triplet.
	 * The second pair contains the disjunction of all predicates of the old triplet.
	 */
	public void flattenOR() {
		GuardActionCoupleSeq sgac = new GuardActionCoupleSeq();
		
		sgac.add(null, this.getGuardActionSeq().getActionsInSeq());
		
		sgac.add(this.getGuardActionSeq().getPredicatesInDisj(), null);
		
		this.setGuardActionSeq(sgac);
	}
	
	/**
	 * Add the guard as the first predicate in the triplet's sequence.
	 * If the triplet does not correspond to an execution of a transition, the predicate must change accordingly to the situation.
	 * @param guard
	 */
	public void appendGuard(String guard) {
		if(this.hasTriggeredTrans()) {
			GuardActionCoupleSeq sgac = new GuardActionCoupleSeq(guard, null);
			sgac.addAll(this.getGuardActionSeq());
			this.setGuardActionSeq(sgac);
		}
		else if(!this.hasOnlyNullPredicate()) {
			this.flattenAND();
			Triplet t = new Triplet(new GuardActionCoupleSeq(Utils.getNot(guard), null), false, new HashSet<ASTD>());
			t.conc(this);
			t.flattenOR();
			this.set(t);
		}
	}
	
	/**
	 * Generate a proof obligation according to the triplet and a starting state
	 * @param rootASTD
	 * @param actualState the starting states of the sequence of actions
	 * @return
	 */
	public String generatePOFromTriplet(ASTD rootASTD, HashSet<ASTD> actualState) {

		String hypInv = rootASTD.generateInvariantOfState(actualState);
		
		String hypGuard = this.getGuardActionSeq().generateAllHypGuard();
		
		String hyp = Utils.formatConcatInvariant(hypInv, hypGuard);
		
		ArrayList<Action> actions = this.getGuardActionSeq().generateAllActions();
		
		String outInv = rootASTD.generateInvariantOfState(this.targetStates);
		
		return Utils.generatePO(hyp, actions, outInv);
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Triplet other = (Triplet) obj;
        if ((this.guardActionSeq == null) ? (other.guardActionSeq != null) : !this.guardActionSeq.equals(other.guardActionSeq)) {
            return false;
        }
        if ((this.targetStates == null) ? (other.targetStates != null) : !this.targetStates.equals(other.targetStates)) {
            return false;
        }

        return this.triggeredTrans == other.triggeredTrans;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.guardActionSeq != null ? this.guardActionSeq.hashCode() : 0);
        hash = 53 * hash + (this.triggeredTrans != null ? this.triggeredTrans.hashCode() : 0);
        hash = 53 * hash + (this.targetStates != null ? this.targetStates.hashCode() : 0);
        return hash;
    }
	
}
