package com.udes.model.construct;

import java.util.ArrayList;
import java.util.Iterator;

import com.udes.model.astd.items.Action;
import com.udes.utils.Utils;

public class GuardActionCoupleSeq {
	private ArrayList<GuardActionCouple> seq;
	
	public GuardActionCoupleSeq() {
		this.seq = new ArrayList<GuardActionCouple>();
	}
	
	public GuardActionCoupleSeq(String predicate, Action action) {
		this.seq = new ArrayList<GuardActionCouple>();
		this.seq.add(new GuardActionCouple(predicate, action));
	}
	
	public GuardActionCoupleSeq(GuardActionCoupleSeq ga) {
		this.seq = new ArrayList<GuardActionCouple>(ga.getSeq());
	}

	public ArrayList<GuardActionCouple> getSeq() {
		return this.seq;
	}
	
	public void setSeq(ArrayList<GuardActionCouple> seq) {
		this.seq = seq;
	}
	
	public void add(GuardActionCouple gac) {
		this.seq.add(gac);
	}

	public void add(String predicate, Action action) {
		this.seq.add(new GuardActionCouple(predicate, action));
	}

	public void addAll(GuardActionCoupleSeq guardActionSeq) {
		this.seq.addAll(guardActionSeq.getSeq());
	}
	
	/** Return true iff all predicates of the sequence are null
	 * @return
	 */
	public boolean hasOnlyNullPredicate() {
		Iterator<GuardActionCouple> it = this.seq.iterator();
		while(it.hasNext()) {
			GuardActionCouple gac = it.next();
			if(!gac.predIsNull()) {
				return false;
			}
		}
		return true;
	}
	
	/** Generate a predicate that corresponds to the concatenation of all predicates in the sequence.
	 * We make substitutions in predicates for each action located before the predicate.
	 * @return
	 */
	public String generateAllHypGuard() {
		String hyp = "btrue";
		ArrayList<Action> actions = new ArrayList<Action>();
		Iterator<GuardActionCouple> it = this.seq.iterator();
		while(it.hasNext()) {
			GuardActionCouple gac = it.next();
			
			if(gac.predNonTrivial()) {
				hyp = Utils.formatConcatInvariant(hyp, Utils.substituteGuard(actions, gac.getPredicate()));
			}
			if(gac.actNonTrivial()) {
				actions.add(gac.getAction());
			}
		}
		return hyp;
	}

	/** Generate the list of action in the order of appearance of actions in the sequence.
	 * @return
	 */
	public ArrayList<Action> generateAllActions() {
		ArrayList<Action> actions = new ArrayList<Action>();
		Iterator<GuardActionCouple> it = this.seq.iterator();
		while(it.hasNext()) {
			GuardActionCouple gac = it.next();
			if(gac.actNonTrivial()) {
				actions.add(gac.getAction());
			}
		}
		return actions;
	}

	/** Concatenate all actions of a GuardActionCoupleSeq in a single action
	 * @return
	 */
	public Action getActionsInSeq() {
		Action act = new Action(null);
		Iterator<GuardActionCouple> it = this.seq.iterator();
		while(it.hasNext()) {
			GuardActionCouple gac = it.next();
			if(!gac.actIsNull()) {
				act = Utils.concatActions(act, gac.getAction());
			}
		}
		return act;
	}

	/** Concatenate all predicate of a GuardActionCoupleSeq in a single predicate.
	 * We concatenate with AND operator
	 * @return
	 */
	public String getPredicatesInConj() {
		String pred = null;
		Iterator<GuardActionCouple> it = this.seq.iterator();
		while(it.hasNext()) {
			GuardActionCouple gac = it.next();
			if(!gac.predIsNull()) {
				pred = Utils.formatConcatInvariant(pred, gac.getPredicate());
			}
		}
		return pred;
	}
	
	/** Concatenate all predicate of a GuardActionCoupleSeq in a single predicate.
	 * We concatenate with OR operator
	 * @return
	 */
	public String getPredicatesInDisj() {
		String pred = null;
		Iterator<GuardActionCouple> it = this.seq.iterator();
		while(it.hasNext()) {
			GuardActionCouple gac = it.next();
			if(!gac.predIsNull()) {
				pred = Utils.formatDisjonctionInvariant(pred, gac.getPredicate());
			}
		}
		return pred;
	}
	
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final GuardActionCoupleSeq other = (GuardActionCoupleSeq) obj;
        if ((this.seq == null) ? (other.seq != null) : !this.seq.equals(other.seq)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.seq != null ? this.seq.hashCode() : 0);
        return hash;
    }
}
