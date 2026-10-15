package com.udes.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import com.udes.model.astd.items.Action;
import com.udes.rodinapi.RodinElement;
import com.udes.utils.analysis.SearchIdentifiers;

import de.be4.classicalb.core.parser.analysis.AnalysisAdapter;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.*;
import de.be4.classicalb.core.parser.util.PrettyPrinter;

public class Astmodifier {
	
	public static AIdentifierExpression getIdentifierFromAssignation(Start assign) {
		return (AIdentifierExpression)(((AAssignSubstitution)((ASubstitutionParseUnit) assign.getPParseUnit()).getSubstitution()).getLhsExpression()).getFirst();
	}
	
	public static PExpression getExpressionFromAssignation(Start assign) {
		return (((AAssignSubstitution)((ASubstitutionParseUnit) assign.getPParseUnit()).getSubstitution()).getRhsExpressions()).getFirst();
	}

	static boolean isSameIdentifierExpression(AIdentifierExpression x, PPredicate pre) {
		return false;
	}
	
	static boolean isSameListIdentifier(LinkedList<TIdentifierLiteral> i1, LinkedList<TIdentifierLiteral> i2) {
		if (i1 == null || i1.isEmpty()) {
			return i2 == null || i2.isEmpty();
		}
		if (i2 == null || i2.isEmpty()) {
			return false;
		}
		if (i1.size() == i2.size()) {
			ListIterator<TIdentifierLiteral> it1 = i1.listIterator();
			ListIterator<TIdentifierLiteral> it2 = i2.listIterator();
			while (it1.hasNext() && it2.hasNext()) {
				if (it1.next().getText() != it2.next().getText()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	static boolean isSameIdentifierExpression(AIdentifierExpression x, PExpression exp) {
		if (x.getClass() == exp.getClass()) {
			AIdentifierExpression exp2 = (AIdentifierExpression) exp;
			return isSameListIdentifier(x.getIdentifier(), exp2.getIdentifier());
		}
		return false;
	}
	
    static void makeSubstitutionInExpression(PExpression inv, AIdentifierExpression x, PExpression exp) {
    	inv.apply(new AnalysisAdapter() {
    		@Override
    		public void caseAIdentifierExpression(AIdentifierExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAAddExpression(AAddExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAArityExpression(AArityExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseABinExpression(ABinExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseABooleanFalseExpression(ABooleanFalseExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseABooleanTrueExpression(ABooleanTrueExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseABoolSetExpression(ABoolSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseABtreeExpression(ABtreeExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseACardExpression(ACardExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseACartesianProductExpression(ACartesianProductExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAClosureExpression(AClosureExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseACompositionExpression(ACompositionExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAComprehensionSetExpression(AComprehensionSetExpression inv) {
    			LinkedList<PExpression> temp = new LinkedList<PExpression>();
    			for (PExpression e : inv.getIdentifiers()) {
    				if (isSameIdentifierExpression(x, e)) {
    					temp.add(exp.clone());
    				}
    				else {
    					PExpression e2 = e.clone();
    					makeSubstitutionInExpression(e2, x, exp);
    					temp.add(e2);
    				}
    			}
    			inv.setIdentifiers(temp);
    			makeSubstitutionInPredicate(inv.getPredicates(), x, exp);
    			return;
    		}
    		
    		@Override
    		public void caseAConcatExpression(AConcatExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAConstExpression(AConstExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAConvertBoolExpression(AConvertBoolExpression inv) {
    			makeSubstitutionInPredicate(inv.getPredicate(), x, exp);
    			return;
    		}
    		
    		@Override
    		public void caseAConvertIntCeilingExpression(AConvertIntCeilingExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAConvertIntFloorExpression(AConvertIntFloorExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAConvertRealExpression(AConvertRealExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseACoupleExpression(ACoupleExpression inv) {
    			LinkedList<PExpression> temp = new LinkedList<PExpression>();
    			for (PExpression e : inv.getList()) {
    				if (isSameIdentifierExpression(x, e)) {
    					temp.add(exp.clone());
    				}
    				else {
    					PExpression e2 = e.clone();
    					makeSubstitutionInExpression(e2, x, exp);
    					temp.add(e2);
    				}
    			}
    			inv.setList(temp);
    			return;
    		}
    		
    		@Override
    		public void caseADefinitionExpression(ADefinitionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseADescriptionExpression(ADescriptionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseADirectProductExpression(ADirectProductExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseADivExpression(ADivExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseADomainExpression(ADomainExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseADomainRestrictionExpression(ADomainRestrictionExpression inv) {
    			return;
    		}
    		
//    		@Override
//    		public void caseADomainSubtractionExpression(ADomainSubtractionExpression inv) {
//    			return;
//    		}
    		
    		@Override
    		public void caseAEmptySequenceExpression(AEmptySequenceExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAEmptySetExpression(AEmptySetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAEventBComprehensionSetExpression(AEventBComprehensionSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAEventBFirstProjectionExpression(AEventBFirstProjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAEventBFirstProjectionV2Expression(AEventBFirstProjectionV2Expression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAEventBIdentityExpression(AEventBIdentityExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAEventBSecondProjectionExpression(AEventBSecondProjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAEventBSecondProjectionV2Expression(AEventBSecondProjectionV2Expression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAExtendedExprExpression(AExtendedExprExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFatherExpression(AFatherExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFin1SubsetExpression(AFin1SubsetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFiniteExpression(AFiniteExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFinSubsetExpression(AFinSubsetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFirstExpression(AFirstExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFirstProjectionExpression(AFirstProjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFloatSetExpression(AFloatSetExpression inv) {
    			return;
    		}
    		
//    		@Override
//    		public void caseAFlooredDivExpression(AFlooredDivExpression inv) {
//    			return;
//    		}
    		
    		@Override
    		public void caseAFrontExpression(AFrontExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAFunctionExpression(AFunctionExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getIdentifier())) {
    				inv.setIdentifier(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getIdentifier(), x, exp);
    			}
    			
    			LinkedList<PExpression> temp = new LinkedList<PExpression>();
    			for (PExpression e : inv.getParameters()) {
    				if (isSameIdentifierExpression(x, e)) {
    					temp.add(exp.clone());
    				}
    				else {
    					PExpression e2 = e.clone();
    					makeSubstitutionInExpression(e2, x, exp);
    					temp.add(e2);
    				}
    			}
    			inv.setParameters(temp);
    			return;
    		}
    		
    		@Override
    		public void caseAFlooredDivExpression(AFlooredDivExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAGeneralConcatExpression(AGeneralConcatExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAGeneralIntersectionExpression(AGeneralIntersectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAGeneralProductExpression(AGeneralProductExpression inv) {
    			makeSubstitutionInPredicate(inv.getPredicates(), x, exp);
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAGeneralSumExpression(AGeneralSumExpression inv) {
    			makeSubstitutionInPredicate(inv.getPredicates(), x, exp);
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAGeneralUnionExpression(AGeneralUnionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAHexIntegerExpression(AHexIntegerExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIdentityExpression(AIdentityExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIfElsifExprExpression(AIfElsifExprExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIfThenElseExpression(AIfThenElseExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAImageExpression(AImageExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAInfixExpression(AInfixExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAInsertFrontExpression(AInsertFrontExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAInsertTailExpression(AInsertTailExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIntegerExpression(AIntegerExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIntegerSetExpression(AIntegerSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIntersectionExpression(AIntersectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIntervalExpression(AIntervalExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeftBorder())) {
    				inv.setLeftBorder(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeftBorder(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRightBorder())) {
    				inv.setRightBorder(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRightBorder(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAIntSetExpression(AIntSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIseq1Expression(AIseq1Expression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIseqExpression(AIseqExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAIterationExpression(AIterationExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseALambdaExpression(ALambdaExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseALastExpression(ALastExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseALeftExpression(ALeftExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseALetExpressionExpression(ALetExpressionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAMaxExpression(AMaxExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAMaxIntExpression(AMaxIntExpression inv) {
    			// Done
    			return;
    		}
    		
    		@Override
    		public void caseAMinExpression(AMinExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAMinIntExpression(AMinIntExpression inv) {
    			// Done
    			return;
    		}
    		
    		@Override
    		public void caseAMinusExpression(AMinusExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAMinusOrSetSubtractExpression(AMinusOrSetSubtractExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAMirrorExpression(AMirrorExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAModuloExpression(AModuloExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAMultiplicationExpression(AMultiplicationExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAMultOrCartExpression(AMultOrCartExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseANat1SetExpression(ANat1SetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseANatSetExpression(ANatSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseANatural1SetExpression(ANatural1SetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseANaturalSetExpression(ANaturalSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAOperationCallExpression(AOperationCallExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAOperatorExpression(AOperatorExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAOverwriteExpression(AOverwriteExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAParallelProductExpression(AParallelProductExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPartialBijectionExpression(APartialBijectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPartialFunctionExpression(APartialFunctionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPartialInjectionExpression(APartialInjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPartialSurjectionExpression(APartialSurjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPermExpression(APermExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPostfixExpression(APostfixExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPow1SubsetExpression(APow1SubsetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPowerOfExpression(APowerOfExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getLeft())) {
    				inv.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, inv.getRight())) {
    				inv.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getRight(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAPowSubsetExpression(APowSubsetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPredecessorExpression(APredecessorExpression inv) {
    			// Done
    			return;
    		}
    		
    		@Override
    		public void caseAPrefixExpression(APrefixExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAPrimedIdentifierExpression(APrimedIdentifierExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAQuantifiedIntersectionExpression(AQuantifiedIntersectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAQuantifiedUnionExpression(AQuantifiedUnionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARangeExpression(ARangeExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARangeRestrictionExpression(ARangeRestrictionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARangeSubtractionExpression(ARangeSubtractionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARankExpression(ARankExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARealExpression(ARealExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARealSetExpression(ARealSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARecExpression(ARecExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARecordFieldExpression(ARecordFieldExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAReflexiveClosureExpression(AReflexiveClosureExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARelationsExpression(ARelationsExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARestrictFrontExpression(ARestrictFrontExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARestrictTailExpression(ARestrictTailExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAReverseExpression(AReverseExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARevExpression(ARevExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARightExpression(ARightExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseARingExpression(ARingExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASecondProjectionExpression(ASecondProjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASeq1Expression(ASeq1Expression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASeqExpression(ASeqExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASequenceExtensionExpression(ASequenceExtensionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASetExtensionExpression(ASetExtensionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASetSubtractionExpression(ASetSubtractionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASizeExpression(ASizeExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASizetExpression(ASizetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASonExpression(ASonExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASonsExpression(ASonsExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAStringExpression(AStringExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAStringSetExpression(AStringSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAStructExpression(AStructExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASubtreeExpression(ASubtreeExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASuccessorExpression(ASuccessorExpression inv) {
    			// Done
    			return;
    		}
    		
    		@Override
    		public void caseASurjectionRelationExpression(ASurjectionRelationExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASymbolicCompositionExpression(ASymbolicCompositionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASymbolicComprehensionSetExpression(ASymbolicComprehensionSetExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASymbolicLambdaExpression(ASymbolicLambdaExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseASymbolicQuantifiedUnionExpression(ASymbolicQuantifiedUnionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATailExpression(ATailExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATopExpression(ATopExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATotalBijectionExpression(ATotalBijectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATotalFunctionExpression(ATotalFunctionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATotalInjectionExpression(ATotalInjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATotalRelationExpression(ATotalRelationExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATotalSurjectionExpression(ATotalSurjectionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATotalSurjectionRelationExpression(ATotalSurjectionRelationExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATransFunctionExpression(ATransFunctionExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATransRelationExpression(ATransRelationExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATreeExpression(ATreeExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseATypeofExpression(ATypeofExpression inv) {
    			return;
    		}
    		
    		@Override
    		public void caseAUnaryMinusExpression(AUnaryMinusExpression inv) {
    			if (isSameIdentifierExpression(x, inv.getExpression())) {
    				inv.setExpression(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(inv.getExpression(), x, exp);
    			}
    			return;
    		}
    		
    		@Override
    		public void caseAUnionExpression(AUnionExpression inv) {
    			return;
    		}
    	});
    }
    
    public static void makeSubstitutionInPredicate(Start s, AIdentifierExpression x, PExpression exp) {
    	PPredicate pre = ((APredicateParseUnit) s.getPParseUnit()).getPredicate();
    	makeSubstitutionInPredicate(pre, x, exp);
    	return;
    }
    
    static void makeSubstitutionInPredicate(PPredicate pre, AIdentifierExpression x, PExpression exp) {
    	pre.apply(new AnalysisAdapter() {
    		@Override
    		public void caseAConjunctPredicate(AConjunctPredicate pre) {
    			makeSubstitutionInPredicate(pre.getLeft(), x, exp);
    			makeSubstitutionInPredicate(pre.getRight(), x, exp);
    			return;
    		}

    		@Override
    		public void caseADefinitionPredicate(ADefinitionPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseADescriptionPredicate(ADescriptionPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseADisjunctPredicate(ADisjunctPredicate pre) {
    			makeSubstitutionInPredicate(pre.getLeft(), x, exp);
    			makeSubstitutionInPredicate(pre.getRight(), x, exp);
    			return;
    		}

    		@Override
    		public void caseAEqualPredicate(AEqualPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseAEquivalencePredicate(AEquivalencePredicate pre) {
    			makeSubstitutionInPredicate(pre.getLeft(), x, exp);
    			makeSubstitutionInPredicate(pre.getRight(), x, exp);
    			return;
    		}

    		@Override
    		public void caseAExistsPredicate(AExistsPredicate pre) {
    			makeSubstitutionInPredicate(pre.getPredicate(), x, exp);
    			return;
    		}

    		@Override
    		public void caseAExtendedPredPredicate(AExtendedPredPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseAFalsityPredicate(AFalsityPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseAForallPredicate(AForallPredicate pre) {
    			makeSubstitutionInPredicate(pre.getImplication(), x, exp);
    			return;
    		}

    		@Override
    		public void caseAGreaterEqualPredicate(AGreaterEqualPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseAGreaterPredicate(AGreaterPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseAIfPredicatePredicate(AIfPredicatePredicate pre) {
    			return;
    		}

    		@Override
    		public void caseAImplicationPredicate(AImplicationPredicate pre) {
    			makeSubstitutionInPredicate(pre.getLeft(), x, exp);
    			makeSubstitutionInPredicate(pre.getRight(), x, exp);
    			return;
    		}

    		@Override
    		public void caseALabelPredicate(ALabelPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseALessEqualPredicate(ALessEqualPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseALessPredicate(ALessPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseALetPredicatePredicate(ALetPredicatePredicate pre) {
    			return;
    		}

    		@Override
    		public void caseAMemberPredicate(AMemberPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseANegationPredicate(ANegationPredicate pre) {
    			makeSubstitutionInPredicate(pre.getPredicate(), x, exp);
    			return;
    		}

    		@Override
    		public void caseANotEqualPredicate(ANotEqualPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseANotMemberPredicate(ANotMemberPredicate pre) {
    			if (isSameIdentifierExpression(x, pre.getLeft())) {
    				pre.setLeft(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getLeft(), x, exp);
    			}
    			if (isSameIdentifierExpression(x, pre.getRight())) {
    				pre.setRight(exp.clone());
    			}
    			else {
    				makeSubstitutionInExpression(pre.getRight(), x, exp);
    			}
    			return;
    		}

    		@Override
    		public void caseANotSubsetPredicate(ANotSubsetPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseANotSubsetStrictPredicate(ANotSubsetStrictPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseAOperatorPredicate(AOperatorPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseAPartitionPredicate(APartitionPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseASubsetPredicate(ASubsetPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseASubsetStrictPredicate(ASubsetStrictPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseASubstitutionPredicate(ASubstitutionPredicate pre) {
    			return;
    		}

    		@Override
    		public void caseATruthPredicate(ATruthPredicate pre) {
    			return;
    		}
    	});
    }
    
    static ArrayList<String> transformIntoSubstitutionList(PSubstitution act) {
    	ArrayList<String> ListOfSingleSubstitutions = new ArrayList<String>();
    	if (act instanceof AAssignSubstitution) {
            AAssignSubstitution assignAct = (AAssignSubstitution)act;
            PrettyPrinter pp = new PrettyPrinter();
            assignAct.apply(pp);
            ListOfSingleSubstitutions.add(pp.getPrettyPrint());
	    } else if (act instanceof ASequenceSubstitution) {
            ASequenceSubstitution sequenceAct = (ASequenceSubstitution)act;
            LinkedList<PSubstitution> acts = sequenceAct.getSubstitutions();
            if (acts != null && !acts.isEmpty()) {
            	ListIterator<PSubstitution> it = acts.listIterator();
            	while (it.hasNext()) {
            		ListOfSingleSubstitutions.addAll(transformIntoSubstitutionList(it.next()));
            	}
            }
	    } // If act instanceof ASkipSubstitution => do nothing
    	return ListOfSingleSubstitutions;
    }
    
    static ArrayList<String> transformIntoSubstitutionList(Start act) {
    	return transformIntoSubstitutionList(((ASubstitutionParseUnit) act.getPParseUnit()).getSubstitution());
    }
    
    static ArrayList<String> transformIntoSubstitutionList(ArrayList<Action> actList) throws BCompoundException {
    	ArrayList<String> ListOfSingleSubstitutions = new ArrayList<String>();
    	for (Action a : actList) {
    		if (a != null) {
	    		Start act = Utils.generateTreeFromSub(a.getCode());
	    		ListOfSingleSubstitutions.addAll(transformIntoSubstitutionList(act));
    		}
    	}
//    	ListIterator<Action> it = actList.listIterator();
//    	while (it.hasNext()) {
//    		Start act = Utils.generateTreeFromSub(it.next().getCode());
//    		ListOfSingleSubstitutions.addAll(transformIntoSubstitutionList(act));
//    	}
    	return ListOfSingleSubstitutions;
    }
    
    public static String applyActionsOnPredicate(ArrayList<Action> act, String inv) throws BCompoundException {
    	Start astInv = Utils.generateTreeFromInv(inv);
        if (act == null || act.isEmpty()) {
        	return inv;
        }
        ArrayList<String> listOfSubstitutions = transformIntoSubstitutionList(act);
        ListIterator<String> it = listOfSubstitutions.listIterator(listOfSubstitutions.size());
        while (it.hasPrevious()) {
        	Start actToDo = Utils.generateTreeFromSub(it.previous());
        	AIdentifierExpression x = getIdentifierFromAssignation(actToDo);
        	PExpression exp = getExpressionFromAssignation(actToDo);
        	makeSubstitutionInPredicate(astInv, x, exp);
        }
        PrettyPrinter pp = new PrettyPrinter();
        astInv.apply(pp);
    	return pp.getPrettyPrint();
    }
    
    
    
    
    public static String addQuantifiers(String po) throws BCompoundException {
    	Start astPO = Utils.generateTreeFromInv(po);
        SearchIdentifiers si = new SearchIdentifiers();
        astPO.apply(si);
        ArrayList<String> quantToAdd = si.getValidId();
        
        if (quantToAdd == null || quantToAdd.isEmpty()) {
        	return po;
        }
        else if (quantToAdd.size() == 1) {
        	return "!" + quantToAdd.get(0) + ".(" + po + ")";
        }
        else {
        	ListIterator<String> it = quantToAdd.listIterator();
        	String newPO = new String("!" + it.next());
        	while(it.hasNext()) {
        		newPO = newPO + "," + it.next();
        	}
        	return newPO + ".(" + po + ")";
        }
    }
	
}