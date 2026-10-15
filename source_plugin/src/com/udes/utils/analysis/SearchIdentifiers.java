package com.udes.utils.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import de.be4.classicalb.core.parser.analysis.AnalysisAdapter;
import de.be4.classicalb.core.parser.node.*;

public class SearchIdentifiers extends AnalysisAdapter
{
	private ArrayList<String> validId = new ArrayList<String>();
	private ArrayList<String> invalidId = new ArrayList<String>();
	
	public ArrayList<String> getValidId() {
		return validId;
	}
	
	@Override
	public void caseStart(final Start node) {
		node.getPParseUnit().apply(this);
	}
	
	@Override
	public void caseAPredicateParseUnit(final APredicateParseUnit node) {
		node.getPredicate().apply(this);
	}
	
	@Override
	public void caseAExpressionParseUnit(final AExpressionParseUnit node) {
		node.getExpression().apply(this);
	}
	
	// ALL PREDICATE NODES
	
	@Override
	public void caseAConjunctPredicate(AConjunctPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
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
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseAEqualPredicate(AEqualPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseAEquivalencePredicate(AEquivalencePredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseAExistsPredicate(AExistsPredicate pre) {
		LinkedList<PExpression> dontTakeThose = pre.getIdentifiers();
		if (dontTakeThose == null || dontTakeThose.isEmpty()) {
			pre.getPredicate().apply(this);
		}
		else {
			ArrayList<String> ToRemove = new ArrayList<String>();
			Iterator<PExpression> it = dontTakeThose.iterator();
			while (it.hasNext()) {
				PExpression exp = it.next();
				if (exp.getClass() == (new AIdentifierExpression()).getClass()) {
					LinkedList<TIdentifierLiteral> idList = ((AIdentifierExpression)exp).getIdentifier();
					for (TIdentifierLiteral id : idList) {
						ToRemove.add(id.getText());
						invalidId.add(id.getText());
					}
				}
			}
			pre.getPredicate().apply(this);
			Iterator<String> it2 = ToRemove.iterator();
			while (it2.hasNext()) {
				invalidId.remove(it2.next());
			}
		}
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
		LinkedList<PExpression> dontTakeThose = pre.getIdentifiers();
		if (dontTakeThose == null || dontTakeThose.isEmpty()) {
			pre.getImplication().apply(this);
		}
		else {
			ArrayList<String> ToRemove = new ArrayList<String>();
			Iterator<PExpression> it = dontTakeThose.iterator();
			while (it.hasNext()) {
				PExpression exp = it.next();
				if (exp.getClass() == (new AIdentifierExpression()).getClass()) {
					LinkedList<TIdentifierLiteral> idList = ((AIdentifierExpression)exp).getIdentifier();
					for (TIdentifierLiteral id : idList) {
						ToRemove.add(id.getText());
						invalidId.add(id.getText());
					}
				}
			}
			pre.getImplication().apply(this);
			Iterator<String> it2 = ToRemove.iterator();
			while (it2.hasNext()) {
				invalidId.remove(it2.next());
			}
		}
		return;
	}

	@Override
	public void caseAGreaterEqualPredicate(AGreaterEqualPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseAGreaterPredicate(AGreaterPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseAIfPredicatePredicate(AIfPredicatePredicate pre) {
		return;
	}

	@Override
	public void caseAImplicationPredicate(AImplicationPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseALabelPredicate(ALabelPredicate pre) {
		return;
	}

	@Override
	public void caseALessEqualPredicate(ALessEqualPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseALessPredicate(ALessPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseALetPredicatePredicate(ALetPredicatePredicate pre) {
		return;
	}

	@Override
	public void caseAMemberPredicate(AMemberPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseANegationPredicate(ANegationPredicate pre) {
		pre.getPredicate().apply(this);
		return;
	}

	@Override
	public void caseANotEqualPredicate(ANotEqualPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
		return;
	}

	@Override
	public void caseANotMemberPredicate(ANotMemberPredicate pre) {
		pre.getLeft().apply(this);
		pre.getRight().apply(this);
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

	
	
	// ALL EXPRESSIONS
	
	@Override
	public void caseAIdentifierExpression(AIdentifierExpression inv) {
		for (TIdentifierLiteral id : inv.getIdentifier()) {
			if (!invalidId.contains(id.getText()) && !validId.contains(id.getText())) {
				validId.add(id.getText());
			}
		}
		return;
	}
	
	@Override
	public void caseAAddExpression(AAddExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
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
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseACartesianProductExpression(ACartesianProductExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
		return;
	}
	
	@Override
	public void caseAClosureExpression(AClosureExpression inv) {
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseACompositionExpression(ACompositionExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
		return;
	}
	
	@Override
	public void caseAComprehensionSetExpression(AComprehensionSetExpression inv) {
		LinkedList<PExpression> dontTakeThose = inv.getIdentifiers();
		if (dontTakeThose == null || dontTakeThose.isEmpty()) {
			inv.getPredicates().apply(this);
		}
		else {
			ArrayList<String> ToRemove = new ArrayList<String>();
			Iterator<PExpression> it = dontTakeThose.iterator();
			while (it.hasNext()) {
				PExpression exp = it.next();
				if (exp.getClass() == (new AIdentifierExpression()).getClass()) {
					LinkedList<TIdentifierLiteral> idList = ((AIdentifierExpression)exp).getIdentifier();
					for (TIdentifierLiteral id : idList) {
						ToRemove.add(id.getText());
						invalidId.add(id.getText());
					}
				}
			}
			inv.getPredicates().apply(this);
			Iterator<String> it2 = ToRemove.iterator();
			while (it2.hasNext()) {
				invalidId.remove(it2.next());
			}
		}
		return;
	}
	
	@Override
	public void caseAConcatExpression(AConcatExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
		return;
	}
	
	@Override
	public void caseAConstExpression(AConstExpression inv) {
		return;
	}
	
	@Override
	public void caseAConvertBoolExpression(AConvertBoolExpression inv) {
		inv.getPredicate().apply(this);
		return;
	}
	
	@Override
	public void caseAConvertIntCeilingExpression(AConvertIntCeilingExpression inv) {
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseAConvertIntFloorExpression(AConvertIntFloorExpression inv) {
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseAConvertRealExpression(AConvertRealExpression inv) {
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseACoupleExpression(ACoupleExpression inv) {
		for (PExpression exp : inv.getList()) {
			exp.apply(this);
		}
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
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
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
	
//	@Override
//	public void caseADomainSubtractionExpression(ADomainSubtractionExpression inv) {
//		return;
//	}
	
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
	
//	@Override
//	public void caseAFlooredDivExpression(AFlooredDivExpression inv) {
//		return;
//	}
	
	@Override
	public void caseAFrontExpression(AFrontExpression inv) {
		return;
	}
	
	@Override
	public void caseAFunctionExpression(AFunctionExpression inv) {
		inv.getIdentifier().apply(null);
		for (PExpression exp : inv.getParameters()) {
			exp.apply(this);
		}
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
		LinkedList<PExpression> dontTakeThose = inv.getIdentifiers();
		if (dontTakeThose == null || dontTakeThose.isEmpty()) {
			inv.getPredicates().apply(this);
		}
		else {
			ArrayList<String> ToRemove = new ArrayList<String>();
			Iterator<PExpression> it = dontTakeThose.iterator();
			while (it.hasNext()) {
				PExpression exp = it.next();
				if (exp.getClass() == (new AIdentifierExpression()).getClass()) {
					LinkedList<TIdentifierLiteral> idList = ((AIdentifierExpression)exp).getIdentifier();
					for (TIdentifierLiteral id : idList) {
						ToRemove.add(id.getText());
						invalidId.add(id.getText());
					}
				}
			}
			inv.getPredicates().apply(this);
			inv.getExpression().apply(this);
			Iterator<String> it2 = ToRemove.iterator();
			while (it2.hasNext()) {
				invalidId.remove(it2.next());
			}
		}
		return;
	}
	
	@Override
	public void caseAGeneralSumExpression(AGeneralSumExpression inv) {
		LinkedList<PExpression> dontTakeThose = inv.getIdentifiers();
		if (dontTakeThose == null || dontTakeThose.isEmpty()) {
			inv.getPredicates().apply(this);
		}
		else {
			ArrayList<String> ToRemove = new ArrayList<String>();
			Iterator<PExpression> it = dontTakeThose.iterator();
			while (it.hasNext()) {
				PExpression exp = it.next();
				if (exp.getClass() == (new AIdentifierExpression()).getClass()) {
					LinkedList<TIdentifierLiteral> idList = ((AIdentifierExpression)exp).getIdentifier();
					for (TIdentifierLiteral id : idList) {
						ToRemove.add(id.getText());
						invalidId.add(id.getText());
					}
				}
			}
			inv.getPredicates().apply(this);
			inv.getExpression().apply(this);
			Iterator<String> it2 = ToRemove.iterator();
			while (it2.hasNext()) {
				invalidId.remove(it2.next());
			}
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
		inv.getLeftBorder().apply(this);
		inv.getRightBorder().apply(this);
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
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseAMaxIntExpression(AMaxIntExpression inv) {
		// Done
		return;
	}
	
	@Override
	public void caseAMinExpression(AMinExpression inv) {
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseAMinIntExpression(AMinIntExpression inv) {
		// Done
		return;
	}
	
	@Override
	public void caseAMinusExpression(AMinusExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
		return;
	}
	
	@Override
	public void caseAMinusOrSetSubtractExpression(AMinusOrSetSubtractExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
		return;
	}
	
	@Override
	public void caseAMirrorExpression(AMirrorExpression inv) {
		return;
	}
	
	@Override
	public void caseAModuloExpression(AModuloExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
		return;
	}
	
	@Override
	public void caseAMultiplicationExpression(AMultiplicationExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
		return;
	}
	
	@Override
	public void caseAMultOrCartExpression(AMultOrCartExpression inv) {
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
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
		inv.getLeft().apply(this);
		inv.getRight().apply(this);
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
		inv.getExpression().apply(this);
		return;
	}
	
	@Override
	public void caseAUnionExpression(AUnionExpression inv) {
		return;
	}
	
	
}