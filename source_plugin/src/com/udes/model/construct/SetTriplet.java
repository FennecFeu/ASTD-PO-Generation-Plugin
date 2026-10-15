package com.udes.model.construct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.udes.model.astd.base.ASTD;

public class SetTriplet {
	HashSet<Triplet> set;
	
	public SetTriplet() {
		this.set = new HashSet<Triplet>();
	}
	
	public SetTriplet(Triplet t) {
		this.set = new HashSet<Triplet>();
		this.set.add(t);
	}
	
	public HashSet<Triplet> getSet() {
		return set;
	}
	public void setSet(HashSet<Triplet> a) {
		this.set = a;
	}
	


	public void add(Triplet t) {
		this.set.add(t);
	}

	public void addAll(SetTriplet s) {
		this.set.addAll(s.getSet());
	}
	
	/*
	 * Append t to all triplet in the set of triplets.
	 */
	public void expand(Triplet t) {
		Iterator<Triplet> it = set.iterator();
		while(it.hasNext()) {
			it.next().append(t);
		}
	}
	
	public void expandGuard(String guard) {
		Iterator<Triplet> it = set.iterator();
		while(it.hasNext()) {
			it.next().appendGuard(guard);
		}
	}
	
	public ArrayList<String> generatePOFromTriplets(ASTD rootASTD, HashSet<ASTD> actualState) {
		ArrayList<String> proofObligations = new ArrayList<String>();
		Iterator<Triplet> it = set.iterator();
		while(it.hasNext()) {
			Triplet t = it.next();
			proofObligations.add(t.generatePOFromTriplet(rootASTD, actualState));
		}
		return proofObligations;
	}

	public ArrayList<HashSet<ASTD>> getAllOutStates() {
		ArrayList<HashSet<ASTD>> OutStates = new ArrayList<HashSet<ASTD>>();
		Iterator<Triplet> it = set.iterator();
		while(it.hasNext()) {
			Triplet t = it.next();
			OutStates.add(t.getTargetStates());
		}
		return OutStates;
	}
	
	public SetTriplet allTransitionTriplets() {
		SetTriplet allTrueTriplets = new SetTriplet();
		Iterator<Triplet> it = set.iterator();
		while(it.hasNext()) {
			Triplet t = it.next();
			if(t.hasTriggeredTrans()) {
				allTrueTriplets.add(t);
			}
		}
		return allTrueTriplets;
	}
	
	public Triplet nonTransitionTriplet() {
		Iterator<Triplet> it = set.iterator();
		while(it.hasNext()) {
			Triplet t = it.next();
			if(!t.hasTriggeredTrans()) {
				return t;
			}
		}
		return null;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final SetTriplet other = (SetTriplet) obj;
        if ((this.set == null) ? (other.set != null) : !this.set.equals(other.set)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.set != null ? this.set.hashCode() : 0);
        return hash;
    }
}
