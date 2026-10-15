package com.udes.model.astd.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.udes.model.astd.base.ASTD;
import com.udes.model.astd.base.BinaryASTD;
import com.udes.model.astd.items.Action;
import com.udes.model.astd.items.Constant;
import com.udes.model.astd.items.Event;
import com.udes.model.astd.items.Variable;
import com.udes.model.astd.tojson.ToJson;
import com.udes.model.construct.GuardActionCoupleSeq;
import com.udes.model.construct.SetTriplet;
import com.udes.model.construct.Triplet;
import com.udes.model.il.conditions.AndCondition;
import com.udes.model.il.conditions.CallCondition;
import com.udes.model.il.conditions.Condition;
import com.udes.model.il.conditions.OrCondition;
import com.udes.model.il.containers.Entry;
import com.udes.model.il.conventions.Conventions;
import com.udes.model.il.record.Enum;
import com.udes.model.il.record.Record;
import com.udes.model.il.statements.AssignStatement;
import com.udes.model.il.statements.CallStatement;
import com.udes.model.il.statements.DeclStatement;
import com.udes.model.il.statements.IFFIStatement;
import com.udes.model.il.statements.SeqStatement;
import com.udes.model.il.statements.Statement;
import com.udes.model.il.terms.Bool;
import com.udes.model.il.terms.Term;
import com.udes.parser.ExecSchemaParser;
import com.udes.translator.ILTranslator;
import com.udes.utils.Constants;
import com.udes.utils.Utils;

public class Flow extends BinaryASTD {

    public Flow(String name, String invariant, List<Variable> attributes, List<Variable> params,
                Action astdAction, ASTD parent, ASTD left, ASTD right) {
        super(name, invariant, attributes, params, astdAction, parent, left, right);
    }

    public Flow(String name, String invariant, ASTD parent, ASTD left, ASTD right) {
        super(name, invariant, parent, left, right);
    }

    public Flow() {
        super();
    }

    @Override
    public Statement init(ArrayList<ASTD> callList, String lets) {
        ArrayList<ASTD> ASTDTree = new ArrayList<>();
        ASTDTree.addAll(callList);
        ASTDTree.add(this);
        List<Statement> seqList = new ArrayList<>();
        String prfx = ASTDTree.get(0).prefixTree(ASTDTree) + ".";
        // String prfx = getParent().prefix(getName()) + ".";
        List<Variable> vList = getAttributes();
        List<Variable> pList = getParams();
        if(vList != null) {
            vList.forEach( v -> {
                String init;
                Boolean b = false;
                if(pList != null && !pList.isEmpty()){
                    for(Variable var: pList){
                        if(var.getName().equals(v.getName())){
                            b = true;
                        }
                    }
                }
                if(!b){
                    init = prefixAction(new Action(v.getInit().toString()), ASTDTree).getCode();
                    if(!init.equals("")){
                        init = init.substring(0, init.length()-1);
                    }
                }
                else{
                    init = v.getInit().toString();
                }
                if( init != null && init.isEmpty())
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()), new Term("\"\"")));
                    //it's a json init value
                else if(init.contains("{") && init.contains(":") && init.contains("}"))
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()),
                            new Term(Conventions.JSON_PARSE
                                    .replace(ILTranslator.USYMBOL_1,
                                            "\""+init+"\""))));
                else if ( init.contains("(") && init.contains(")")) {
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()),
                            new Term(init)));
                }
                else if(v.getType().compareTo(Conventions.STRING) == 0)
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()), new Term("\""+init+"\"")));
                else if(v.getType().compareTo(Conventions.CLOCK) == 0) {
                    seqList.add(new CallStatement(Conventions.CLOCK_RESET, Arrays.asList(prfx + v.getName(), lets)));
                }
                else if(v.getType().compareTo(Conventions.TIME_TYPE3) == 0 && Constants.TIMED_SIMULATION){
                    seqList.add(new CallStatement(Conventions.CLOCK_RESET, Arrays.asList(prfx + v.getName(), lets)));
                }
                else
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()), new Term(init)));
            });
        }
        // init left
        seqList.add(getLeft().init(ASTDTree, lets));
        // init right
        seqList.add(getRight().init(ASTDTree, lets));

        return new SeqStatement(seqList);
    }

    @Override
    public Statement initforsub(ArrayList<ASTD> callList, Event e, Bool timed, String lets, boolean forFinal) {
        ArrayList<ASTD> ASTDTree = new ArrayList<>();
        ASTDTree.addAll(callList);
        ASTDTree.add(this);
        List<Statement> seqList = new ArrayList<>();
        String prfx = ASTDTree.get(0).prefixTree(ASTDTree) + ".";
        // String prfx = getParent().prefix(getName()) + ".";
        List<Variable> vList = getAttributes();
        List<Variable> pList = getParams();
        if(vList != null) {
            vList.forEach( v -> {
                String init;
                Boolean b = false;
                if(pList != null && !pList.isEmpty()){
                    for(Variable var: pList){
                        if(var.getName().equals(v.getName())){
                            b = true;
                        }
                    }
                }
                if(!b){
                    init = prefixAction(new Action(v.getInit().toString()), ASTDTree).getCode();
                    if(!init.equals("")){
                        init = init.substring(0, init.length()-1);
                    }
                }
                else{
                    init = v.getInit().toString();
                }
                if( init != null && init.isEmpty())
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()), new Term("\"\"")));
                    //it's a json init value
                else if(init.contains("{") && init.contains(":") && init.contains("}"))
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()),
                            new Term(Conventions.JSON_PARSE
                                    .replace(ILTranslator.USYMBOL_1,
                                            "\""+init+"\""))));
                else if ( init.contains("(") && init.contains(")")) {
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()),
                            new Term(init)));
                }else if(v.getType().compareTo(Conventions.STRING) == 0)
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()), new Term("\""+init+"\"")));
                else if(v.getType().compareTo(Conventions.CLOCK) == 0) {
                    seqList.add(new CallStatement(Conventions.CLOCK_RESET, Arrays.asList(prfx + v.getName(), lets)));
                }
                else if(v.getType().compareTo(Conventions.TIME_TYPE3) == 0 && Constants.TIMED_SIMULATION){
                    seqList.add(new CallStatement(Conventions.CLOCK_RESET, Arrays.asList(prfx + v.getName(), lets)));
                }
                else
                    seqList.add(new AssignStatement(new Term(prfx + v.getName()), new Term(init)));
            });
        }
        // init left
        seqList.add(getLeft().initforsub(ASTDTree, e, timed, lets, forFinal));
        // init right
        seqList.add(getRight().initforsub(ASTDTree, e, timed, lets, forFinal));

        return new SeqStatement(seqList);
    }

    @Override
    public Condition _final(ArrayList<ASTD> callList) {
        ArrayList<ASTD> ASTDTree = new ArrayList<>();
        ASTDTree.addAll(callList);
        ASTDTree.add(this);
        // Left and right astd should be final
        return new AndCondition(Arrays.asList(getLeft()._final(ASTDTree), getRight()._final(ASTDTree)));
    }

    @Override
    public Condition _finalForSub(ArrayList<ASTD> callList) {
        ArrayList<ASTD> ASTDTree = new ArrayList<>();
        ASTDTree.addAll(callList);
        ASTDTree.add(this);
        // Left and right astd should be final
        return new AndCondition(Arrays.asList(getLeft()._finalForSub(ASTDTree), getRight()._finalForSub(ASTDTree)));
    }

    @Override
    public Entry<List<Enum>, List<Record>> trans_type() {
        List<Record> recordList = new ArrayList<>(),
                enumList   = new ArrayList<>();
        // struct name
        String structName = Conventions.getStructName(getName());
        List<Variable> props = new ArrayList<>();
        // astd attributes
        List<Variable> attrs = getAttributes();
        if(attrs != null)
            props.addAll(attrs);
        // ignore elem ASTD for optimization
        if(!Conventions.isElem(getLeft())) {
            props.add(new Variable(Conventions.getStructVar(getLeft().getName()),
                    Conventions.getStructName(getLeft().getName()), null, getName()));
        }
        //right struct var
        // ignore elem ASTD for optimization
        if(!Conventions.isElem(getRight())) {
            props.add(new Variable(Conventions.getStructVar(getRight().getName()),
                    Conventions.getStructName(getRight().getName()), null, getName()));
        }
        if(Constants.COND_OPT_OPTS) {
            for (int i = 1; i < 3; i++) {
                props.add(new Variable("cond_" + i, Conventions.BOOL_TYPE, null, getName()));
            }
        }
        recordList.add(new Record(structName, props));
        // left struct
        Entry<List<Enum>, List<Record>> recordLeft = getLeft().trans_type();
        if (!recordLeft.getKey().isEmpty())
            enumList.addAll(recordLeft.getKey());
        if (!recordLeft.getValue().isEmpty())
            recordList.addAll(recordLeft.getValue());
        // right struct
        Entry<List<Enum>, List<Record>> recordRight = getRight().trans_type();
        if (!recordRight.getKey().isEmpty())
            enumList.addAll(recordRight.getKey());
        if (!recordRight.getValue().isEmpty())
            recordList.addAll(recordRight.getValue());

        return new Entry(enumList, recordList);
    }

    @Override
    public Entry<Condition, Statement> trans_event(Event e, Bool timed, ArrayList<ASTD> callList, String lets) {
        ArrayList<ASTD> ASTDTree = new ArrayList<>();
        ASTDTree.addAll(callList);
        ASTDTree.add(this);
        Entry<Condition, Statement> evtBody = new Entry();
        Action astdAction = prefixAction(getAstdAction(), ASTDTree);
        if(astdAction != null && Constants.DEBUG) {
            String debugMsg = ";\n" + Constants.LOGGER_MSG.replace(ILTranslator.USYMBOL_1, astdAction.getCode());
            astdAction = new Action(astdAction.getCode() + debugMsg);
        }
        String prfx = ASTDTree.get(0).prefixTree(ASTDTree) + ".";
        //String prfx = getParent().prefix(getName()) + ".";
        Entry<Condition, Statement> eventLeft, eventRight;
        if(e.getName().equals("Step") && (getLeft() instanceof QInterleaving ||
                getLeft() instanceof QSynchronization ||
                getRight() instanceof QInterleaving ||
                getRight() instanceof  QSynchronization || 
                getLeft() instanceof QChoice || 
                getRight() instanceof QChoice ||
                getLeft() instanceof QFlow ||
                getRight() instanceof QFlow)){
            // event body left
            eventLeft = getLeft().trans_event_step(e, timed, ASTDTree, prfx+Conventions.LEFT_CLOCK+"_"+getLeft().getName());
            // event body right
            eventRight = getRight().trans_event_step(e, timed, ASTDTree, Conventions.RIGHT_CLOCK+"_"+getRight().getName());
        }
        else{
            // event body left
            eventLeft = getLeft().trans_event(e, timed, ASTDTree, prfx+Conventions.LEFT_CLOCK+"_"+getLeft().getName());
            // event body right
            eventRight = getRight().trans_event(e, timed, ASTDTree, prfx+Conventions.RIGHT_CLOCK+"_"+getRight().getName());
        }

        // changing clock variables
        if(timed.getValue()){
            if(!eventLeft.isEmpty()){
                eventLeft.getKey().substitute(new AssignStatement(new Term(Conventions.LETS),new Term(prfx+Conventions.LEFT_CLOCK+"_"+getLeft().getName())), ASTDTree);
            }
            if(!eventRight.isEmpty()) {
                eventRight.getKey().substitute(new AssignStatement(new Term(Conventions.LETS),new Term(prfx+Conventions.RIGHT_CLOCK+"_"+getRight().getName())), ASTDTree);
            }
        }


        if(!eventLeft.isEmpty() && !eventRight.isEmpty()) {
            // if-fi left block
            Condition c_l, c_r;

                c_l = eventLeft.getKey();
                c_r = eventRight.getKey();

            evtBody.setKey(new OrCondition(Arrays.asList(c_l, c_r)));
            List<Statement> stmtList = new ArrayList<>();
            if(Constants.COND_OPT_OPTS) {
                if(isRoot()) {
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_1"), c_l));
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_2"), c_r));
                }

                    c_l = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_1", eventLeft.getKey()));
                    c_r = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_2", eventRight.getKey()));
                    evtBody.setKey(new OrCondition(Arrays.asList(c_l, c_r)));
                    c_l = new Bool(prfx + "cond_1");
                    c_r = new Bool(prfx + "cond_2");

            }

            //global condition of the event function
            evtBody.setKey(new OrCondition(Arrays.asList(c_l, c_r)));
            // statement of the event function
            Entry<Condition, Statement> iffiLeft;
            Entry<Condition, Statement> iffiRight;

            iffiLeft = new Entry<>(c_l, new SeqStatement(Arrays.asList(eventLeft.getValue())));
            iffiRight = new Entry<>(c_r, new SeqStatement(Arrays.asList(eventRight.getValue())));
            if(astdAction != null) {
                //if(exec){
                //  do flow ASTD action;
                //}
                IFFIStatement actionSeq = new IFFIStatement(Arrays.asList(
                        new Entry<>(new Bool(Conventions.EXEC), astdAction)
                ));
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                }
            }
            else {
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                }
            }
        }
        else if(!eventLeft.isEmpty() && eventRight.isEmpty()) {
            // if-fi left block
            Condition c_l;

                c_l = eventLeft.getKey();

            //global condition of the event function
            List<Statement> stmtList = new ArrayList<>();
            if(Constants.COND_OPT_OPTS) {
                if(isRoot()) {
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_1"), c_l));
                }

                    c_l = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_1", eventLeft.getKey()));
                    evtBody.setKey(new OrCondition(Arrays.asList(c_l)));
                    c_l = new Bool(prfx + "cond_1");

                evtBody.setKey(new OrCondition(Arrays.asList(c_l)));
                c_l = new Bool(prfx + "cond_1");
            }
            //global condition on event function
            evtBody.setKey(new OrCondition(Arrays.asList(c_l)));
            // statement of the event function
            Entry<Condition, Statement> iffiLeft;
            iffiLeft = new Entry<>(c_l, new SeqStatement(Arrays.asList(eventLeft.getValue())));
            if(astdAction != null) {
                //if(exec){
                //  do flow ASTD action;
                //}
                IFFIStatement actionSeq = new IFFIStatement(Arrays.asList(
                        new Entry<>(new Bool(Conventions.EXEC), astdAction)
                ));
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            actionSeq)));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            actionSeq)));
                }
            }
            else {
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)))));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)))));
                }
            }
        }
        else if(eventLeft.isEmpty() && !eventRight.isEmpty()) {
            // if-fi left block
            Condition c_r;

                c_r = eventRight.getKey();

            //global condition of the event function
            List<Statement> stmtList = new ArrayList<>();
            if(Constants.COND_OPT_OPTS) {
                if(isRoot()) {
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_2"), c_r));
                }
                    c_r = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_2", eventRight.getKey()));
                    evtBody.setKey(c_r);
                    c_r = new Bool(prfx + "cond_2");


            }

            //global condition on event function
            evtBody.setKey(new OrCondition(Arrays.asList(c_r)));
            // statement of the event function
            Entry<Condition, Statement> iffiRight;
            iffiRight = new Entry<>(c_r, new SeqStatement(Arrays.asList(eventRight.getValue())));
            if(astdAction != null) {
                //if(exec){
                //  do flow ASTD action;
                //}
                IFFIStatement actionSeq = new IFFIStatement(Arrays.asList(
                        new Entry<>(new Bool(Conventions.EXEC), astdAction)
                ));
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                }
            }
            else {
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                }
            }
        }



        return evtBody;
    }

    @Override
    public Entry<Condition, Statement> trans_event_step(Event e, Bool timed, ArrayList<ASTD> callList, String lets) {
        ArrayList<ASTD> ASTDTree = new ArrayList<>();
        ASTDTree.addAll(callList);
        ASTDTree.add(this);
        Entry<Condition, Statement> evtBody = new Entry();
        Action astdAction = prefixAction(getAstdAction(), ASTDTree);
        if(astdAction != null && Constants.DEBUG) {
            String debugMsg = ";\n" + Constants.LOGGER_MSG.replace(ILTranslator.USYMBOL_1, astdAction.getCode());
            astdAction = new Action(astdAction.getCode() + debugMsg);
        }
        String prfx = ASTDTree.get(0).prefixTree(ASTDTree) + ".";
        // event body left
        Entry<Condition, Statement> eventLeft = getLeft().trans_event_step(e, timed, ASTDTree, prfx+Conventions.LEFT_CLOCK+"_"+getLeft().getName());
        // event body right
        Entry<Condition, Statement> eventRight = getRight().trans_event_step(e, timed, ASTDTree, prfx+Conventions.RIGHT_CLOCK+"_"+getRight().getName());

        // changing clock variables
        if(timed.getValue()){
            if(!eventLeft.isEmpty()){
                eventLeft.getKey().substitute(new AssignStatement(new Term(Conventions.LETS),new Term(prfx+Conventions.LEFT_CLOCK+"_"+getLeft().getName())), ASTDTree);
            }
            if(!eventRight.isEmpty()) {
                eventRight.getKey().substitute(new AssignStatement(new Term(Conventions.LETS),new Term(prfx+Conventions.RIGHT_CLOCK+"_"+getRight().getName())), ASTDTree);
            }
        }

        if(!eventLeft.isEmpty() && !eventRight.isEmpty()) {
            // if-fi left block
            Condition c_l, c_r;

                c_l = eventLeft.getKey();
                c_r = eventRight.getKey();

            evtBody.setKey(new OrCondition(Arrays.asList(c_l, c_r)));
            List<Statement> stmtList = new ArrayList<>();
            if(Constants.COND_OPT_OPTS) {
                if(isRoot()) {
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_1"), c_l));
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_2"), c_r));
                }

                    c_l = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_1", eventLeft.getKey()));
                    c_r = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_2", eventRight.getKey()));
                    evtBody.setKey(new OrCondition(Arrays.asList(c_l, c_r)));
                    c_l = new Bool(prfx + "cond_1");
                    c_r = new Bool(prfx + "cond_2");

            }

            //global condition of the event function
            evtBody.setKey(new OrCondition(Arrays.asList(c_l, c_r)));
            // statement of the event function
            Entry<Condition, Statement> iffiLeft;
            Entry<Condition, Statement> iffiRight;
            iffiLeft = new Entry<>(c_l, new SeqStatement(Arrays.asList(eventLeft.getValue())));
            iffiRight = new Entry<>(c_r, new SeqStatement(Arrays.asList(eventRight.getValue())));
            if(astdAction != null) {
                //if(exec){
                //  do flow ASTD action;
                //}
                IFFIStatement actionSeq = new IFFIStatement(Arrays.asList(
                        new Entry<>(new Bool(Conventions.EXEC), astdAction)
                ));
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                }
            }
            else {
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                }
            }
        }
        else if(!eventLeft.isEmpty() && eventRight.isEmpty()) {
            // if-fi left block
            Condition c_l;

                c_l = eventLeft.getKey();

            //global condition of the event function
            List<Statement> stmtList = new ArrayList<>();
            if(Constants.COND_OPT_OPTS) {
                if(isRoot()) {
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_1"), c_l));
                }

                    c_l = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_1", eventLeft.getKey()));
                    evtBody.setKey(new OrCondition(Arrays.asList(c_l)));
                    c_l = new Bool(prfx + "cond_1");

                evtBody.setKey(new OrCondition(Arrays.asList(c_l)));
                c_l = new Bool(prfx + "cond_1");
            }
            //global condition on event function
            evtBody.setKey(new OrCondition(Arrays.asList(c_l)));
            // statement of the event function
            Entry<Condition, Statement> iffiLeft;
            iffiLeft = new Entry<>(c_l, new SeqStatement(Arrays.asList(eventLeft.getValue())));
            if(astdAction != null) {
                //if(exec){
                //  do flow ASTD action;
                //}
                IFFIStatement actionSeq = new IFFIStatement(Arrays.asList(
                        new Entry<>(new Bool(Conventions.EXEC), astdAction)
                ));
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            actionSeq)));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)),
                            actionSeq)));
                }
            }
            else {
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)))));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiLeft)))));
                }
            }
        }
        else if(eventLeft.isEmpty() && !eventRight.isEmpty()) {
            // if-fi left block
            Condition c_r;

                c_r = eventRight.getKey();

            //global condition of the event function
            List<Statement> stmtList = new ArrayList<>();
            if(Constants.COND_OPT_OPTS) {
                if(isRoot()) {
                    stmtList.add(new AssignStatement(new Term(prfx + "cond_2"), c_r));
                }
                c_r = new CallCondition(-1, Conventions.EQUALS1, Arrays.asList(prfx + "cond_2", eventRight.getKey()));
                    evtBody.setKey(c_r);
                    c_r = new Bool(prfx + "cond_2");

            }

            //global condition on event function
            evtBody.setKey(new OrCondition(Arrays.asList(c_r)));
            // statement of the event function
            Entry<Condition, Statement> iffiRight;
            iffiRight = new Entry<>(c_r, new SeqStatement(Arrays.asList(eventRight.getValue())));
            if(astdAction != null) {
                //if(exec){
                //  do flow ASTD action;
                //}
                IFFIStatement actionSeq = new IFFIStatement(Arrays.asList(
                        new Entry<>(new Bool(Conventions.EXEC), astdAction)
                ));
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)),
                            actionSeq)));
                }
            }
            else {
                if(isRoot()) {
                    stmtList.add(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                    evtBody.setValue(new SeqStatement(stmtList));
                }
                else {
                    evtBody.setValue(new SeqStatement(Arrays.asList(
                            new IFFIStatement(Collections.singletonList(iffiRight)))));
                }
            }
        }



        return evtBody;
    }

    @Override
    public String getInitialStateValue() {
        return "";
    }

    @Override
    public Statement propertyMapping() {

        List<Statement> stmtList = new ArrayList<>();
        // generate common ASTD properties
        Statement commonProps = super.propertyMapping();
        if(commonProps != null)
            stmtList.add(commonProps);

        // generate Flow properties
        List<String> flowProps = Arrays.asList(ExecSchemaParser.LEFT, ExecSchemaParser.RIGHT);

        flowProps.forEach(prop -> {
            DeclStatement decStmt = new DeclStatement(new Constant((this.getName() + "_" + prop).toUpperCase(),
                    Conventions.STRING, prop, getName()));

            stmtList.add(decStmt);
        });

        Constants.EXEC_SCHEMA_PROPS = Constants.EXEC_SCHEMA_PROPS.replace(ILTranslator.USYMBOL_1, Flow.class.getSimpleName());

        Statement left = getLeft().propertyMapping();
        stmtList.add(left);

        Statement right = getRight().propertyMapping();
        stmtList.add(right);

        return new SeqStatement(stmtList);
    }

    @Override
    public Statement currentStateToJson() {
        List<Statement> stmtList = new ArrayList<>();
        ToJson tojson = toJson();
        Statement stmt;
        if(isRoot()) {
            stmt = topLevelStateToJson();
            tojson.setNodeIndex(Utils.topLevelASTDIndex);
        }
        else {
            stmt = fillJSONState(tojson.getNodeIndex());
        }
        if(stmt != null) {
            stmtList.add(stmt);
        }
        //TODO CHANGE FOR PREFIXTREE
        String prfx = getParent().prefix(getName()) + ".";
        stmtList.add(fillFlowProperties(tojson.getNodeIndex(), prfx));

        List<String> subIndexes = updateSubIndexes(tojson);
        if(subIndexes != null) {
            tojson.setSubNodeIndex(subIndexes);
            setToJson(tojson);
        }

        ASTD leftASTD = getLeft();
        ToJson leftToJson = leftASTD.toJson();
        if(tojson.getSubNodeIndex() != null) {
            leftToJson.setNodeIndex(Conventions.ARRAY_ELEM
                    .replace(ILTranslator.USYMBOL_2, tojson.getSubNodeIndex().get(0))
                    .replace(ILTranslator.USYMBOL_1, (this.getName() + "_" + ExecSchemaParser.LEFT).toUpperCase()));
        }
        List<String> leftIdx = leftASTD.updateSubIndexes(leftToJson);
        if(leftIdx != null) {
            leftToJson.setSubNodeIndex(leftIdx);
            leftASTD.setToJson(leftToJson);
        }
        Statement leftStmt = leftASTD.currentStateToJson();
        if(leftStmt != null)
            stmtList.add(leftStmt);

        ASTD rightASTD = getRight();
        ToJson rightToJson = rightASTD.toJson();
        if(tojson.getSubNodeIndex() != null) {
            rightToJson.setNodeIndex(Conventions.ARRAY_ELEM
                    .replace(ILTranslator.USYMBOL_2, tojson.getSubNodeIndex().get(1))
                    .replace(ILTranslator.USYMBOL_1, (this.getName() + "_" + ExecSchemaParser.RIGHT).toUpperCase()));
        }
        List<String> rightIdx = rightASTD.updateSubIndexes(rightToJson);
        if(rightIdx != null) {
            rightToJson.setSubNodeIndex(rightIdx);
            rightASTD.setToJson(rightToJson);
        }
        Statement rightStmt = rightASTD.currentStateToJson();
        if(rightStmt != null)
            stmtList.add(rightStmt);

        return new SeqStatement(stmtList);
    }


    private Statement fillFlowProperties(String index, String prfx) {
        return new AssignStatement(new Term(Conventions.ARRAY_ELEM.replace(ILTranslator.USYMBOL_2, index)
                .replace(ILTranslator.USYMBOL_1, ExecSchemaParser.TYPE.toUpperCase())),
                new Term("\""+ ExecSchemaParser.FLOW +"\""));
    }
    
    @Override
    public ArrayList<String> generatePOInitialisationFromASTD(String invariants, String hypotheses, ArrayList<Action> actInit, ArrayList<Action> actions, boolean isNotRedundant) {
    	String newInv = Utils.formatConcatInvariant(invariants, getInvariant());
    	actInit.addAll(getAstdInitialisation());
    	actions.add(getAstdAction());
    	ArrayList<String> po = new ArrayList<String>();
    	ArrayList<Action> acts = actInit;
    	po.add(Utils.generatePO(hypotheses, acts, getInvariant()));
    	po.addAll(getLeft().generatePOInitialisationFromASTD(newInv, hypotheses, actInit, actions, isNotRedundant));
    	po.addAll(getRight().generatePOInitialisationFromASTD(newInv, hypotheses, actInit, actions, isNotRedundant));
    	return po;
    }

    @Override
    public HashSet<ASTD> getInitialPosition() {
    	HashSet<ASTD> temp = new HashSet<ASTD>();
    	temp.add(this);
    	temp.addAll(this.getLeft().getInitialPosition());
    	temp.addAll(this.getRight().getInitialPosition());
    	return temp;
    }
    
    private SetTriplet fusionFlow(SetTriplet left, SetTriplet right) {
    	SetTriplet fusion = new SetTriplet();
    	SetTriplet trleft = left.allTransitionTriplets();
    	SetTriplet trright = right.allTransitionTriplets();

    	Triplet a = left.nonTransitionTriplet();
    	Triplet b = right.nonTransitionTriplet();
    	
    	Iterator<Triplet> itr1 = trright.getSet().iterator();
    	while(itr1.hasNext()) {
        	Iterator<Triplet> itl1 = trleft.getSet().iterator();
    		Triplet y = new Triplet(itr1.next());
    		while(itl1.hasNext()) {
    			Triplet x = new Triplet(itl1.next());
    			Triplet n = new Triplet(y);
    			n.conc(x);
    			fusion.add(n);
    		}
    		String temp = a.getGuardActionSeq().getPredicatesInConj();
    		if(temp == null || !temp.equals("bfalse")) {
    			Triplet n = new Triplet(y);
    			n.conc(a);
    			fusion.add(n);
    		}
    	}
    	Iterator<Triplet> itl2 = trleft.getSet().iterator();
    	while(itl2.hasNext()) {
        	Iterator<Triplet> itr2 = trright.getSet().iterator();
    		Triplet x = new Triplet(itl2.next());
    		while(itr2.hasNext()) {
    			Triplet y = new Triplet(itr2.next());
    			Triplet n = new Triplet(x);
    			n.conc(y);
    			fusion.add(n);
    		}
    		String temp = b.getGuardActionSeq().getPredicatesInConj();
    		if(temp == null || !temp.equals("bfalse")) {
    			Triplet n = new Triplet(x);
    			n.conc(b);
    			fusion.add(n);
    		}
    	}
    	
    	a.conc(b);
    	a.flattenAND();
    	fusion.add(a);
    	return fusion;
    	
    	/*
    	Iterator<Triplet> itr1 = trright.getSet().iterator();
    	while(itr1.hasNext()) {
        	Iterator<Triplet> itl1 = left.getSet().iterator();
    		Triplet y = new Triplet(itr1.next());
    		while(itl1.hasNext()) {
    			Triplet x = new Triplet(itl1.next());
    			Triplet n = new Triplet(y);
    			n.conc(x);
    			fusion.add(n);
    		}
    	}
    	Iterator<Triplet> itl2 = trleft.getSet().iterator();
    	while(itl2.hasNext()) {
        	Iterator<Triplet> itr2 = right.getSet().iterator();
    		Triplet x = new Triplet(itl2.next());
    		while(itr2.hasNext()) {
    			Triplet y = new Triplet(itr2.next());
    			Triplet n = new Triplet(x);
    			n.conc(y);
    			fusion.add(n);
    		}
    	}
    	
    	a.conc(b);
    	a.flattenAND();
    	fusion.add(a);
    	return fusion;
    	*/
    }
    
    @Override
    public SetTriplet getAllTriplets(HashSet<ASTD> actualState, Event e) {
    	
    	SetTriplet allTripletsLeft = this.getLeft().getAllTriplets(actualState, e);
    	SetTriplet allTripletsRight = this.getRight().getAllTriplets(actualState, e);
    	
    	SetTriplet allTriplets = this.fusionFlow(allTripletsLeft, allTripletsRight);
    	
    	GuardActionCoupleSeq newSGAC = new GuardActionCoupleSeq();
    	newSGAC.add(null, this.getAstdAction());
    	HashSet<ASTD> newState =  new HashSet<ASTD>();
    	newState.add(this);
    	Triplet toExpand = new Triplet(newSGAC, false, newState);
    	allTriplets.expand(toExpand);
    	return allTriplets;
    }
    
    @Override
	public String generateInvariantOfState(HashSet<ASTD> state) {
		String inv = null;
		if(state.contains(this.getLeft()) && state.contains(this.getRight())) {
			inv = Utils.formatConcatInvariant(inv, this.getLeft().generateInvariantOfState(state));
			inv = Utils.formatConcatInvariant(inv, this.getRight().generateInvariantOfState(state));
		}
		return Utils.formatConcatInvariant(this.getInvariant(), inv);
	}
    
    @Override
    public boolean isFinalIn(HashSet<ASTD> actualState) {
    	return this.getLeft().isFinalIn(actualState) && this.getRight().isFinalIn(actualState);
    }

	public Triplet ActInitEndTriplet() {
		HashSet<ASTD> a = new HashSet<ASTD>();
		a.add(this);
		Triplet result = new Triplet(this.getGacInit(), false, a);
		Triplet left = this.getLeft().ActInitEndTriplet();
		left.conc(this.getRight().ActInitEndTriplet());
		result.conc(left);
		return result;
	}

}
