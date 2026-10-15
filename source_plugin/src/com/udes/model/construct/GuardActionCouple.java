package com.udes.model.construct;

import com.udes.model.astd.items.Action;
import com.udes.utils.Utils;

public class GuardActionCouple {
	
	private String predicate;
	private Action action;
	
	public GuardActionCouple() {
		this.predicate = null;
		this.action = null;
	}
	
	public GuardActionCouple(String predicate, Action action) {
		this.predicate = predicate;
		this.action = action;
	}
	
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
	/** Return true iff the predicate is null
	 * @return
	 */
	public boolean predIsNull() {
		return this.predicate == null;
	}

	/** Return true iff the action is null
	 * @return
	 */
	public boolean actIsNull() {
		return this.action == null;
	}

	/** Return true iff the predicate can be false
	 * @return
	 */
	public boolean predNonTrivial() {
		return this.predicate != null && !this.predicate.equals("") && !this.predicate.equals("btrue");
	}
	
	/** Return true iff the action applies a modification
	 * @return
	 */
	public boolean actNonTrivial() {
		return this.action != null && !this.action.getCode().equals(Utils.skip().getCode());
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final GuardActionCouple other = (GuardActionCouple) obj;
        if ((this.predicate == null) ? (other.predicate != null) : !this.predicate.equals(other.predicate)) {
            return false;
        }
        if ((this.action == null) ? (other.action != null) : !this.action.equals(other.action)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.predicate != null ? this.predicate.hashCode() : 0);
        hash = 53 * hash + (this.action != null ? this.action.hashCode() : 0);
        return hash;
    }
	
}
