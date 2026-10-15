package com.udes.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.udes.model.astd.base.ASTD;
import com.udes.model.astd.base.QuantifiedASTD;
import com.udes.model.astd.items.Action;
import com.udes.model.astd.items.Arrow;
import com.udes.model.astd.items.Constant;
import com.udes.model.astd.items.Event;
import com.udes.model.astd.items.FromSub;
import com.udes.model.astd.items.Local;
import com.udes.model.astd.items.ToSub;
import com.udes.model.astd.items.Transition;
import com.udes.model.astd.items.Variable;
import com.udes.model.astd.types.Automaton;
import com.udes.model.astd.types.Choice;
import com.udes.model.astd.types.Flow;
import com.udes.model.astd.types.Guard;
import com.udes.model.astd.types.Interleave;
import com.udes.model.astd.types.Interleaving;
import com.udes.model.astd.types.Kleene;
import com.udes.model.astd.types.QChoice;
import com.udes.model.astd.types.QFlow;
import com.udes.model.astd.types.QInterleave;
import com.udes.model.astd.types.QInterleaving;
import com.udes.model.astd.types.QSynchronization;
import com.udes.model.astd.types.Sequence;
import com.udes.model.astd.types.Synchronization;
import com.udes.model.il.ILModel;
import com.udes.model.il.conditions.Condition;
import com.udes.model.il.conventions.Conventions;
import com.udes.model.il.methods.Function;
import com.udes.model.il.record.Enum;
import com.udes.model.il.record.Record;
import com.udes.parser.ExecSchemaParser;
import com.udes.translator.ILTranslator;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.analysis.AnalysisAdapter;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.AIdentifierExpression;
import de.be4.classicalb.core.parser.node.PExpression;
import de.be4.classicalb.core.parser.node.PPredicate;
import de.be4.classicalb.core.parser.node.Start;
import de.be4.classicalb.core.parser.util.PrettyPrinter;


public class Utils {

    public static HashMap<String, Condition> qvarCond;
    public static Hashtable<String, Event> qvarDic;
    public static String topLevelASTDIndex;
    public static List<QuantifiedASTD> qASTDList;
    public static boolean attributeUpdated;
    public static List<String> quotedStr;

    public static String[][] mask = {{"std",  String.valueOf(0x0AFFFF)},
                                     {"endl", String.valueOf(0x1AFFFF)},
                                     {"cout", String.valueOf(0x2AFFFF)}
                                     // Add it if necessary
                                     /*,
                                     {"map", "!$"}, {"vector", "!&"}, {"list", "!#"},
                                     {"set", "#$"}, {"deque", "#&"}, {"ordered", "##"}
                                     */
                                     };
    public enum OSType {
        WINDOWS, MACOS, LINUX, SOLARIS, OTHER
    };

    public static String readFile(String filename) {
        String out = "";
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                out += scanner.nextLine() + "\n";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
        }
        return out;
    }

    public static Object copyObject(Object objSource) {
        Object objDest = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(objSource);
            oos.flush();
            oos.close(); bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            try {
                objDest = new ObjectInputStream(bais).readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objDest;
    }

    public static void writeFile(String text, String path) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(
                         new OutputStreamWriter(
                             new FileOutputStream(path), "utf-8"));
            writer.write(text);
        } catch (IOException ex) {}
        finally {
            try {writer.close();} catch (Exception ex) {}
        }
    }

    public static String capitalize(String name) {
        if(name != null) {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
        return null;
    }

    public static String javaCapStyle(String name) {
        if(name != null) {
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        return null;
    }

    public static String getOperatingSystemType() {
        String curr_os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        return curr_os;
    }

    public static String maskKeywords(String code) {
        String code1 = code;
        Pattern pattern1 = Pattern.compile("\\b"+Utils.mask[0][0]+"\\b");
        Matcher m1 = pattern1.matcher(code1);
        if(m1.find()){
            code1 = m1.replaceAll(Utils.mask[0][1]);
        }

        Pattern pattern2 = Pattern.compile("\\b"+Utils.mask[1][0]+"\\b");
        Matcher m2 = pattern2.matcher(code1);
        if(m2.find()){
            code1 = m2.replaceAll(Utils.mask[1][1]);
        }

        Pattern pattern3 = Pattern.compile("\\b"+Utils.mask[2][0]+"\\b");
        Matcher m3 = pattern3.matcher(code1);
        if(m3.find()){
            code1 = m3.replaceAll(Utils.mask[2][1]);
        }
        // Add it if necessary

        return code1;
    }

    public static String unmaskKeywords(String code) {
        String code1;
        code1 = code.replace(Utils.mask[0][1], Utils.mask[0][0]);
        code1 = code1.replace(Utils.mask[1][1], Utils.mask[1][0]);
        code1 = code1.replace(Utils.mask[2][1], Utils.mask[2][0]);
        // Add it if necessary
        return code1;
    }

    public static String maskQuotes(String code) {
        quotedStr = new ArrayList<>();
        String code1 = code;
        Matcher _m = Pattern.compile("\"([^\"]+)\"").matcher(code1);
        int k = 0;
        while (_m.find()) {
            quotedStr.add(_m.group(1));
            code1 = code1.replace("\""+quotedStr.get(k)+"\"", String.valueOf(0x00CFFF << k));
            k++;
        }
        return code1;
    }

    public static String unmaskQuotes(String code) {
        String code1 = code;
        int k = 0;
        for(String item : quotedStr) {
            code1 = code1.replace(String.valueOf(0x00CFFF << k), "\""+item+"\"");
            k++;
        }
        return code1;
    }

    public static String maskAttributes(String code, List<Variable> tmp, Variable out) {
        String code1 = code;
        int k = 0;
        Iterator<Variable> it = tmp.iterator();
        while(it.hasNext()) {
            Variable v = it.next();
            if(out.getName().compareTo(v.getName()) != 0) {
                Pattern pattern = Pattern.compile("\\b"+v.getName()+"\\b");
                Matcher m = pattern.matcher(code1);
                if(m.find()){
                    code1 = m.replaceAll(String.valueOf(1000000000 + k));
                }
            }
            k++;
        }
        return code1;
    }

    public static String maskParams(String code, List<Variable> tmp, Variable out) {
        String code1 = code;
        int k = 0;
        Iterator<Variable> it = tmp.iterator();
        while(it.hasNext()) {
            Variable v = it.next();
            if(out.getName().compareTo(v.getName()) != 0) {
                Pattern pattern = Pattern.compile("\\b"+v.getName()+"\\b");
                Matcher m = pattern.matcher(code1);
                if(m.find()){
                    code1 = m.replaceAll(String.valueOf(0xBBBFFF << k));
                }
            }
            k++;
        }
        return code1;
    }

    public static String maskTimerFunctions(String code, Variable out) {
        String code1 = code;
        int k = 0;
        List<String> tmp = new ArrayList<>();
        tmp.add(Conventions.CLOCK_TIMED_INTERRUPT);
        tmp.add(Conventions.EXPIRED);
        tmp.add(Conventions.LETS);
        Iterator<String> it = tmp.iterator();
        while(it.hasNext()) {
            String v = it.next();
            if(out.getName().compareTo(v) != 0) {
                Pattern pattern = Pattern.compile("\\b"+v+"\\b");
                Matcher m = pattern.matcher(code1);
                if(m.find()){
                    code1 = m.replaceAll(String.valueOf(0xBBBBFF << k));
                }
            }
            k++;
        }
        return code1;
    }

    public static String maskASTDNames(String code, List<ASTD> tmp) {
        String code1 = code;
        int k = 0;
        Iterator<ASTD> it = tmp.iterator();
        while(it.hasNext()) {
            ASTD v = it.next();
            Pattern pattern = Pattern.compile("\\b"+v.getName()+"\\b");
            Matcher m = pattern.matcher(code1);
            if(m.find()){
                code1 = m.replaceAll(String.valueOf(2000000000 + k));
            }
            k++;
        }
        return code1;
    }

    public static String maskText(String code, HashMap<String, String> mapString) {
        String code1 = code;
        int k = 0;
        Pattern pattern = Pattern.compile("(\".*?\")");
        Matcher m = pattern.matcher(code1);
        while(m.find()) {
            mapString.put(String.valueOf(300000000 + k), m.group(0));
            code1 = code1.replace(m.group(0), String.valueOf(300000000 + k));
            k++;
        }
        return code1;
    }

    public static String maskFunction(String code, HashMap<String, ArrayList<String>> mapString) {
        String code1 = code;
        int k = mapString.size();
        Pattern pattern = Pattern.compile(Constants.FUNC_PARAMS);
        Matcher m = pattern.matcher(code1);
        while(m.find()) {
            ArrayList<String> params = new ArrayList<>();
            params.add(m.group(1));
            params.add(m.group(2));
            mapString.put(String.valueOf(400000000 + k), params);
            code1 = code1.replace(m.group(0), String.valueOf(400000000 + k));
            k++;
        }
        return code1;
    }

    public static String maskFunctionWithoutParams(String code, HashMap<String, String> mapString) {
        String code1 = code;
        int k = mapString.size();
        Pattern pattern = Pattern.compile(Constants.FUNC_SANS_PARAMS);
        Matcher m = pattern.matcher(code1);
        while(m.find()) {
            mapString.put(String.valueOf(600000000 + k), m.group(0));
            code1 = code1.replace(m.group(0), String.valueOf(600000000 + k));
            k++;
        }
        return code1;
    }

    public static String unmaskFunctionWithoutParams(String code, HashMap<String, String> mapString) {
        String code1 = code;
        //order is necessary
        for(int i = 0; i < mapString.size(); i++){
            code1 = code1.replace(String.valueOf(600000000 + i), mapString.get(String.valueOf(600000000 + i)));
        }
        return code1;
    }

    public static String unmaskFunction(String code, HashMap<String, String> mapString) {
        String code1 = code;
        //order is necessary
        for(int i = 0; i < mapString.size(); i++){
            code1 = code1.replace(String.valueOf(400000000 + i), mapString.get(String.valueOf(400000000 + i)));
        }
        return code1;
    }

    public static String unmaskText(String code, HashMap<String, String> mapString) {
        String code1 = code;
        for(Map.Entry<String, String> entry : mapString.entrySet()){
            code1 = code1.replace(entry.getKey(), entry.getValue());
        }
        return code1;
    }

    public static String unmaskASTDNames(String code, List<ASTD> tmp) {
        String code1 = code;
        Iterator<ASTD> it = tmp.iterator();
        int k = 0;
        while(it.hasNext()) {
            ASTD v = it.next();
            code1 = code1.replace(String.valueOf(2000000000 + k), v.getName());
            k++;
        }
        return code1;
    }

    public static String maskCallAttributes(String code, List<Variable> tmp) {
        String code1 = code;
        int k = 0;
        Iterator<Variable> it = tmp.iterator();
        while(it.hasNext()) {
            Variable v = it.next();
            code1 = code1.replace(v.getName(), String.valueOf(0x0FFFFF << k));
            k++;
        }
        return code1;
    }

    public static String unmaskAttributes(String code, List<Variable> tmp, Variable out) {
        String code1 = code;
        Iterator<Variable> it = tmp.iterator();
        int k = 0;
        while(it.hasNext()) {
            Variable v = it.next();
            if(out.getName().compareTo(v.getName()) != 0)
                code1 = code1.replace(String.valueOf(1000000000 + k), v.getName());

            k++;
        }
        return code1;
    }

    public static String unmaskParams(String code, List<Variable> tmp, Variable out) {
        String code1 = code;
        Iterator<Variable> it = tmp.iterator();
        int k = 0;
        while(it.hasNext()) {
            Variable v = it.next();
            if(out.getName().compareTo(v.getName()) != 0)
                code1 = code1.replace(String.valueOf(0xBBBFFF << k), v.getInit().toString());

            k++;
        }
        return code1;
    }

    public static String unmaskTimerFunctions(String code, Variable out) {
        String code1 = code;
        List<String> tmp = new ArrayList<>();
        tmp.add(Conventions.CLOCK_TIMED_INTERRUPT);
        tmp.add(Conventions.EXPIRED);
        tmp.add(Conventions.LETS);
        Iterator<String> it = tmp.iterator();
        int k = 0;
        while(it.hasNext()) {
            String v = it.next();
            if(out.getName().compareTo(v) != 0)
                code1 = code1.replace(String.valueOf(0xBBBBFF << k), v);

            k++;
        }
        return code1;
    }

    public static String unmaskAttributesCall(String code, List<Variable> tmp, Variable out) {
        String code1 = code;
        Iterator<Variable> it = tmp.iterator();
        int k = 0;
        while(it.hasNext()) {
            Variable v = it.next();
            if(out.getName().compareTo(v.getName()) != 0)
                code1 = code1.replace(String.valueOf(0xBBBBBB << k), v.getInit().toString());

            k++;
        }
        return code1;
    }

    public static String unmaskCallAttributes(String code, List<Variable> tmp) {
        String code1 = code;
        Iterator<Variable> it = tmp.iterator();
        int k = 0;
        while(it.hasNext()) {
            Variable v = it.next();
            code1 = code1.replace(String.valueOf(0x0FFFFF << k), v.getName());
            k++;
        }
        return code1;
    }

    public static String generateNameIfNotExists(String name) {
        if((name != null) && (!name.isEmpty())) {
            return name.replaceAll(Constants.EXCEPT_SPECIAL_CHAR,"_");
        }
        else {
            SecureRandom rand = new SecureRandom();
            byte[] bytes = new byte[2];
            rand.nextBytes(bytes);
            return Constants.PREFIX_UNKNOW_NAME + DatatypeConverter.printHexBinary(bytes);
        }
    }
    
    public static String formatType(String type) {
    	switch (type) {
        case "int":
            return "INT"; 
        case "bool":
            return "BOOL";
        case "string":
            return "STRING";
        case "float":
        	return "FLOAT";
        default:
            return "";
    	}
    }
    
    public static String formatInvariant(String invariant) {
        if((invariant != null) && (!invariant.isEmpty())) {
            return invariant;
        }
        else {
            return "btrue";
        }
    }
    
    public static String formatConcatInvariant(String inv1, String inv2) {
    	String result = new String();
        if((inv1 != null) && (!inv1.isBlank()) && (!inv1.equals("btrue"))) {
        	if((inv2 != null) && (!inv2.isBlank()) && (!inv2.equals("btrue"))) {
        		if(inv1.equals("bfalse") || inv2.equals("bfalse")) {
        			result = "bfalse";
        		}
        		else {
        			result = "(" + inv1 + ") & (" + inv2 + ")";
        		}
        	}
        	else {
        		result = inv1;
        	}
        }
        else {
        	if((inv2 != null) && (!inv2.isBlank())) {
        		result = inv2;
        	}
        	else {
        		result = "btrue";
        	}
        }
        return result;
    }
    
    public static String formatDisjonctionInvariant(String inv1, String inv2) {
        if((inv1 == null) || (inv1.isBlank()) || inv1.equals("bfalse")) {
        	return inv2;
        }
        else if (inv2== null || inv2.isBlank() || inv2.equals("bfalse")) {
        	return inv1;
        }
        else if (inv1.equals("btrue") || inv2.equals("btrue")) {
        	return "btrue";
        }
        else {
        	return "(" + inv1 + ") or (" + inv2 + ")";
        }
    }
    
    public static Action formatActionFromVariable(Variable v) {
    	return new Action(null);
    }
    
    public static Action skip() {
    	return new Action(null);
    }
    
    public static ArrayList<Action> skiplist() {
    	return new ArrayList<Action>();
    }
    
//    public static String replaceVariable(Action act, String inv) {
//    	if(inv != null && inv != "" && inv != "true" && act.isSubstitution()) {
//    		String x = act.getSubstitued();
//    		String y = act.getSubstituer();
//    		String new_inv = new String("");
//    		int index_inv = 0;
//    		while(inv.length() - index_inv > x.length()) {
//    			int i = inv.indexOf(x, index_inv);
//    			if (i == -1) {
//    				new_inv.concat(inv.substring(index_inv));
//    			}
//    			else if (i + x.length() == inv.length() || inv.charAt(i + x.length() + 1).matches("[^a-zA-Z]") ){
//    				
//    			}
//    		}
//    	}
//    	else {
//    		return inv;
//    	}
//    }
    
    public static Start generateTreeFromInv(String inv) throws BCompoundException {
    	BParser b = new BParser();
    	System.out.println(inv);
        return b.parsePredicate(inv);
    }
    
    public static Start generateTreeFromSub(String sub) throws BCompoundException {
    	BParser b = new BParser();
    	return b.parseSubstitution(sub);
    }
    
    
    public static Action concatActions(Action act1, Action act2) {
    	if(act1 == null || act1.getCode() == null || act1.getCode().equals("skip")) {
    		return act2;
    	}
    	if(act2 == null || act2.getCode() == null || act2.getCode().equals("skip")) {
    		return act1;
    	}
    	//String a = act1.getCode().substring(0, act1.getCode().length()-1);
    	//String b = act2.getCode().substring(1, act2.getCode().length());
    	
    	Action act = new Action(act1.getCode() + ";" + act2.getCode());
    	//act.setCode(a.concat(b));
    	return act;
    }
    
    
    public static String generatePO(String hyp, ArrayList<Action> act, String inv) {
		try {
			String po = new String();
	    	if (inv != null && inv != "" && inv != "btrue") {
	    		String newInv = "";
	    		if (inv == "bfalse") {
		    		newInv = "bfalse";
	    		}
	    		else {
		    		newInv = Astmodifier.applyActionsOnPredicate(act, inv);
		    	}
	    		
	    		if (hyp != null && hyp != "" && hyp != "btrue") {
	    			po = "((" + hyp + ") => (" + newInv + "))";
	    		}
	    		else {
	    			po = "(" + newInv + ")";
	    		}
	    	}
	    	else {
	    		if (hyp != null && hyp != "" && hyp != "btrue") {
	    			po = "((" + hyp + ") => btrue)";
	    		}
	    		else {
	    			po = "btrue";
	    		}
	    	}
	    	return Astmodifier.addQuantifiers(po);
		} catch(BCompoundException e) {
			e.printStackTrace(System.out);
			return "error in generation";
		}
    }
    
    public static String substituteGuard(ArrayList<Action> act, String guard) {
		try {
			String po = new String();
			System.out.println("guard vaut : " + guard);
			if (!guard.equals(null) && !guard.equals("") && !guard.equals("btrue")) {
	    		String newInv = "";
	    		if (guard.equals("bfalse")) {
		    		newInv = "bfalse";
	    		}
	    		else {
		    		newInv = Astmodifier.applyActionsOnPredicate(act, guard);
		    	}
	    		po = "(" + newInv + ")";
	    	}
	    	else {
	    		po = "btrue";
	    	}
	    	return po;
		} catch(BCompoundException e) {
			e.printStackTrace(System.out);
			return "error in generation";
		}
    }
    


	public static boolean hasBeenSeen(ArrayList<HashSet<ASTD>> SEEN, ArrayList<HashSet<ASTD>> NEXT,
			HashSet<ASTD> outState) {
		Iterator<HashSet<ASTD>> itseen = SEEN.iterator();
		while(itseen.hasNext()) {
			HashSet<ASTD> s = itseen.next();
			if(s.containsAll(outState) && outState.containsAll(s)) {
				return true;
			}
		}
		Iterator<HashSet<ASTD>> itnext = NEXT.iterator();
		while(itnext.hasNext()) {
			HashSet<ASTD> n = itnext.next();
			if(n.containsAll(outState) && outState.containsAll(n)) {
				return true;
			}
		}
		return false;
	}
    
    
    

    public static String get_input_stream(String msg) {
        System.out.println(msg);
        System.out.println("Did you want to run the transition between two local states (Y/N)?");
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }

    public static String replaceLast(String str, String from, String to) {
        StringBuilder strb = new StringBuilder(str);
        int index = strb.lastIndexOf(from);
        strb.replace(index, from.length() + index, to);
        return strb.toString();
    }

    public static Set<String> distinct;

    public static ArrayList<Event> mergeEvents(Set<Event> evt_collections) {
        ArrayList<Event> allEvents = new ArrayList<>(evt_collections);

        ArrayList<String> testedEvents = new ArrayList<>();
        ArrayList<Event> distinctEvents = new ArrayList<>();

        for(int i = 0; i < allEvents.size(); i++){
            Event e1 = allEvents.get(i);
            if(!testedEvents.contains(e1.getName())){
                testedEvents.add(e1.getName());
                distinctEvents.add(e1);
            }
            else{
                for (int j = 0; j < allEvents.size(); j++) {
                    if (i != j) {
                        Event e2 = allEvents.get(j);
                        if (e1.getName().equals(e2.getName())) {
                            if (e1.getParams().size() != e2.getParams().size()) {
                                System.out.println("[Error] Not allowed to use the same event name with different signatures !!!");
                                System.exit(0);
                            } else {
                                if (!e1.getParams().isEmpty()) {
                                    for (int k = 0; k < e1.getParams().size(); k++) {
                                        if (
                                                e1.getParams().get(k).getType() != null &&
                                                e2.getParams().get(k).getType() != null &&
                                                !e1.getParams().get(k).getType().equals(e2.getParams().get(k).getType())) {
                                            System.out.println("[Error] Not allowed to use the same event name with different signatures !!!");
                                            System.exit(0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return distinctEvents;
    }

    public static String jsonAttributeBuilder(String prfx, Variable v) {

        String type = v.getType(),
                out  = "\"{\\\"\" + "
                        + ExecSchemaParser.NAME.toUpperCase()
                        + " + \"\\\" : \\\"" + v.getName()
                        + "\\\", \\\"\" + "
                        + ExecSchemaParser.TYPE.toUpperCase()
                        + " + \"\\\" : \\\""
                        + v.getType() + "\\\", \\\"\" + "
                        + ExecSchemaParser.CURRENT_VALUE.toUpperCase()
                        + " + \"\\\" : \\\"\" + ";

        if(type.contains(Conventions.MAP.toLowerCase())
                || type.contains(Conventions.LIST.toLowerCase())
                || type.contains(Conventions.VECTOR.toLowerCase())
                || type.contains(Conventions.SET.toLowerCase())) {
            out = out + prfx + v.getName();

            return Conventions.JSON_PARSE.replace(ILTranslator.USYMBOL_1, out + " + \"\\\"}\"");
        }
        else if(type.contains(Conventions.STRING) || type.contains(Conventions.INT)
                || type.contains(Conventions.BOOL_TYPE1) || type.contains(Conventions.FLOAT)
                || type.contains(Conventions.DOUBLE) || type.contains(Conventions.SHORT)
                || type.contains(Conventions.LONG)) {
            out = out + Conventions.TO_STRING.replace(ILTranslator.USYMBOL_1, prfx + v.getName());

        }
        else {
            out = out + prfx + v.getName() + ".dump()";
        }

        return Conventions.JSON_PARSE.replace(ILTranslator.USYMBOL_1, out + " + \"\\\"}\"");

    }

    public static void copyFolder(File source, File destination)
    {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            String files[] = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);
                copyFolder(srcFile, destFile);
            }
        }
        else {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(source);
                out = new FileOutputStream(destination);

                byte[] buffer = new byte[1024];

                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            catch (Exception e) {
                try {
                    in.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    out.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void print(Object obj) {
        if (obj == null)
            return;

        if (obj instanceof ILModel) {
            ILModel ilm = (ILModel) obj;
            System.out.println("IL model: " + ilm.getName());
            List<Record> recordList = ilm.getTypedecls();
            if (recordList != null) {
                System.out.println("types: [");
                for (Record record : recordList) {
                    if (record instanceof Enum) {
                        System.out.print("Enum( " + record.getName() + ", ");
                        List<String> props = record.getProperties();
                        if (props != null) {
                            for (String p : props) {
                                System.out.print(p + ", ");
                            }
                            System.out.print("), \n");
                        } else {
                            System.out.print("null ), \n");
                        }
                    } else {
                        System.out.print("Struct( " + record.getName() + ", ");
                        List<Variable> props = record.getProperties();
                        if (props != null) {
                            for (Variable p : props) {
                                System.out.print("Property(" + p.getName() + "," + p.getType() + "), ");
                            }
                            System.out.print("), \n");
                        } else {
                            System.out.print("null ), \n");
                        }
                    }
                }
                System.out.println("]");
            }
            List<Variable> vardecls = ilm.getVardecls();
            if (vardecls != null) {
                System.out.println("vardecls: [");
                for (Variable v : vardecls) {
                    if (v != null) {
                        if (v instanceof Constant)
                            System.out.println("Constant(" + v.getName() + "," + v.getType()
                                    + "," + v.getInit() + "), ");
                        else
                            System.out.println("Variable(" + v.getName() + "," + v.getType()
                                    + "," + v.getInit() + "), ");
                    }
                }
                System.out.println("]");
            }
            List<Function> funcs = ilm.getFunctions();
            if (funcs != null) {
                System.out.println("functions: [");
                for (Function f : funcs) {
                    System.out.print("Function(" + f.getName() + ", Parameters(");
                    List<Variable> props = f.getParams();
                    if (props != null) {
                        for (Variable p : props) {
                            System.out.print("Parameter(" + p.getName() + "," + p.getType() + "), ");
                        }
                        System.out.print("), ");
                    } else {
                        System.out.print("null ), ");
                    }
                    System.out.print("Type(" + f.getType() + "), Statement(" + f.getBlock() + ")),\n");
                }
                System.out.println("]");
            }
        } else if (obj instanceof ASTD) {
            ASTD astd = (ASTD) obj;
            if (astd instanceof Automaton) {
                Automaton autASTD = (Automaton) astd;
                System.out.println("object name: " + autASTD.getName());
                System.out.println("type: Automaton");
                List<Variable> attrs = autASTD.getAttributes();
                if (attrs != null) {
                    System.out.println("attributes: [");
                    for (Variable v : attrs) {
                        System.out.println("Variable(" + v.getName() + ","
                                + v.getType() + "," + v.getInit().toString() + "), ");
                    }
                    System.out.println("]");
                }
                List<Variable> params = autASTD.getParams();
                if (params != null) {
                    System.out.println("params: [");
                    for (Variable v : params) {
                        System.out.println("Parameter(" + v.getName() + ","
                                + v.getType() + "," + v.getInit().toString() + "), ");
                    }
                    System.out.println("]");
                }

                Action astdAction = autASTD.getAstdAction();
                if (astdAction != null)
                    System.out.println("astdAction: " + astdAction.getCode());

                System.out.println("states: " + autASTD.getStateNames());
                System.out.println("event labels: " + autASTD.getEventNames());

                List<Transition> trans = autASTD.getTransitions();
                if (trans == null)
                    trans = new ArrayList<>();
                System.out.println("transitions: [");
                for (Transition t : trans) {
                    Arrow arrow = t.getArrow();
                    if (arrow instanceof Local) {
                        Local loc = (Local) arrow;
                        System.out.print("Transition ( Local(" + loc.getS1() + "," + loc.getS2() + "), ");
                    } else if (arrow instanceof ToSub) {
                        ToSub tsub = (ToSub) arrow;
                        System.out.print("Transition ( ToSub(" + tsub.getS1() + "," + tsub.getS2b()
                                + "," + tsub.getS2() + "), ");
                    } else {
                        FromSub fsub = (FromSub) arrow;
                        System.out.print("Transition ( ToSub(" + fsub.getS1() + "," + fsub.getS1b()
                                + "," + fsub.getS2() + "), ");
                    }
                    Event e = t.getEvent();
                    System.out.print("Event(" + e.getName() + ", Param(" + e.getParams() + ")), ");
                    System.out.print("Guard(" + t.getGuard() + "), ");
                    Action tr = t.getAction();
                    if (tr != null)
                        System.out.print("Action(" + tr.getCode() + "), ");
                    System.out.print("final(" + t.isFinal() + ")), \n");
                }
                System.out.println("]");
                System.out.println("Map<State, ASTD>: [");
                Map<String, ASTD> stateToASTDs = autASTD.getStatesToASTDs();
                if (stateToASTDs != null) {
                    for (Map.Entry<String, ASTD> stateASTD : stateToASTDs.entrySet()) {
                        if (stateASTD.getValue() != null)
                            print(stateASTD.getValue());
                        System.out.println(",");
                    }
                    System.out.println("]");
                }
                System.out.println("shallow_final_states: " + autASTD.getShallowFinalStates());
                System.out.println("deep_final_states: " + autASTD.getDeepFinalStates());

            } else if (astd instanceof Kleene) {
                Kleene kleeneASTD = (Kleene) astd;
                System.out.println("object name: " + kleeneASTD.getName());
                System.out.println("type: Kleene");
                print(kleeneASTD.getBody());
            } else if (astd instanceof Guard) {
                Guard gASTD = (Guard) astd;
                System.out.println("object name: " + gASTD.getName());
                System.out.println("type: Guard");
                print(gASTD.getBody());
            } else if (astd instanceof Sequence) {
                Sequence sASTD = (Sequence) astd;
                System.out.println("object name: " + sASTD.getName());
                System.out.println("type: Sequence");
                print(sASTD.getRight());
                print(sASTD.getLeft());
            } else if (astd instanceof Choice) {
                Choice cASTD = (Choice) astd;
                System.out.println("object name: " + cASTD.getName());
                System.out.println("type: Choice");
                print(cASTD.getRight());
                print(cASTD.getLeft());
            } else if (astd instanceof Interleave) {
                Interleave iASTD = (Interleave) astd;
                System.out.println("object name: " + iASTD.getName());
                System.out.println("type: Interleave");
                print(iASTD.getRight());
                print(iASTD.getLeft());
            } else if (astd instanceof Interleaving) {
                Interleaving iASTD = (Interleaving) astd;
                System.out.println("object name: " + iASTD.getName());
                System.out.println("type: Interleaving");
                print(iASTD.getRight());
                print(iASTD.getLeft());
            } else if (astd instanceof Flow) {
                Flow fASTD = (Flow) astd;
                System.out.println("object name: " + fASTD.getName());
                System.out.println("type: Flow");
                print(fASTD.getRight());
                print(fASTD.getLeft());
            } else if (astd instanceof Synchronization) {
                Synchronization sASTD = (Synchronization) astd;
                System.out.println("object name: " + sASTD.getName());
                System.out.println("synchro set : " + sASTD.getDelta());
                System.out.println("type: Synchronization");
                print(sASTD.getRight());
                print(sASTD.getLeft());
            } else if (astd instanceof QSynchronization) {
                QSynchronization qASTD = (QSynchronization) astd;
                System.out.println("object name: " + qASTD.getName());
                System.out.println("qsynchro set : " + qASTD.getDelta());
                System.out.println("type: QSynchronization");
                print(qASTD.getBody());
            } else if (astd instanceof QInterleave) {
                QInterleave qASTD = (QInterleave) astd;
                System.out.println("object name: " + qASTD.getName());
                System.out.println("type: QInterleave");
                print(qASTD.getBody());
            } else if (astd instanceof QInterleaving) {
                QInterleaving qASTD = (QInterleaving) astd;
                System.out.println("object name: " + qASTD.getName());
                System.out.println("type: QInterleaving");
                print(qASTD.getBody());
            } else if (astd instanceof QChoice) {
                QChoice qASTD = (QChoice) astd;
                System.out.println("object name: " + qASTD.getName());
                System.out.println("type: QChoice");
                print(qASTD.getBody());
            } else if (astd instanceof QFlow) {
                QFlow qASTD = (QFlow) astd;
                System.out.println("object name: " + qASTD.getName());
                System.out.println("type: QFlow");
                print(qASTD.getBody());
            }
        }
    }

	public static String getNot(String guard) {
		String no = new String();
		if(guard == null || guard.equals("btrue")) {
			no = "bfalse";
		}
		else {
			no = "not(" + guard + ")";
		}
		return no;
	}
    
//    public static boolean notEquivalent(String a, String b) {
//    	try {
//        	Start as = Utils.generateTreeFromInv(a);
//        	Start bs = Utils.generateTreeFromInv(b);
//        	PrettyPrinter ppa = new PrettyPrinter();
//            as.apply(ppa);
//        	PrettyPrinter ppb = new PrettyPrinter();
//            bs.apply(ppb);
//            return ppa.getPrettyPrint() != ppb.getPrettyPrint();
//    	} catch (BCompoundException e) {
//    		return true;
//    	}
//    }
//    
//    public static boolean notSkipList(ArrayList<Action> a) {
//    	if (a == null || a.isEmpty()) {
//    		return false;
//    	}
//    	else {
//    		for (Action act : a) {
//    			if (act != null && act.getCode() != null && !act.getCode().isBlank() && act.getCode() != "skip") {
//    				return true;
//    			}
//    		}
//			return false;
//    	}
//    }
}
