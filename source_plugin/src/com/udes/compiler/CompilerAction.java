package com.udes.compiler;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
//import java.util.HashMap;
//import java.util.HashSet;
import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.rodinp.core.IRodinProject;

//import com.udes.args.CmdManager;
import com.udes.model.astd.base.ASTD;
import com.udes.model.astd.items.Action;
import com.udes.model.astd.items.Event;
//import com.udes.model.astd.items.Constant;
//import com.udes.model.astd.items.Event;
import com.udes.model.astd.items.Variable;
import com.udes.model.construct.SetTriplet;
import com.udes.model.construct.Triplet;
//import com.udes.model.astd.types.Automaton;
//import com.udes.model.astd.types.QChoice;
//import com.udes.model.astd.types.QFlow;
//import com.udes.model.astd.types.QInterleaving;
//import com.udes.model.astd.types.QSynchronization;
import com.udes.model.il.terms.Bool;
import com.udes.parser.ASTDParser;
import com.udes.rodinapi.RodinContext;
import com.udes.rodinapi.RodinCreation;
import com.udes.rodinapi.RodinElement;
//import com.udes.track.ExecSchema;
//import com.udes.utils.Astmodifier;
//import com.udes.utils.Constants;
import com.udes.utils.Utils;
import com.udes.utils.analysis.PrettyPrinterASCII;
import com.udes.utils.analysis.PrettyPrinterUnicode;

//import de.be4.classicalb.core.parser.node.AConjunctPredicate;
//import de.be4.classicalb.core.parser.node.ADisjunctPredicate;
//import de.be4.classicalb.core.parser.node.AEqualPredicate;
//import de.be4.classicalb.core.parser.node.AEquivalencePredicate;
//import de.be4.classicalb.core.parser.node.AForallPredicate;
//import de.be4.classicalb.core.parser.node.AImplicationPredicate;
//import de.be4.classicalb.core.parser.node.APredicateParseUnit;
import de.be4.classicalb.core.parser.node.Start;
//import de.be4.classicalb.core.parser.util.PrettyPrinter;

public class CompilerAction implements IObjectActionDelegate{
	
	protected ISelection selection;
	
	public CompilerAction() {
		super();
	}
	
    private MessageConsole findConsole(String name) {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++)
           if (name.equals(existing[i].getName()))
              return (MessageConsole) existing[i];
        //no console found, so create a new one
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[]{myConsole});
        return myConsole;
     }
	
	@Override
	public void run(IAction action) {
		List<IFile> files = (selection instanceof IStructuredSelection) ? ((IStructuredSelection) selection).toList()
				: Collections.EMPTY_LIST;

		if (files.size() > 0) {
			IFile file = files.get(0);
			String fileName = file.getName().split("\\.")[0];
			IProject iProject = file.getProject();
	        Bool timed = new Bool(false);
	        
	        MessageConsole myConsole = findConsole("CONSOLE_Plugin_ASTD_PO_Generation");
	        MessageConsoleStream out = myConsole.newMessageStream();

			if (file.getFileExtension().equals("json")) {
				
				out.println("==== Model construction ====");
				
				String specPath = file.getLocationURI().toString().substring(5);
				ASTDParser p = new ASTDParser(specPath, null);
				ASTD rootASTD = p.parse(timed);
				
				if (rootASTD == null) {
					out.println("Wrong ASTD entry, the program will stop");
					return;
				}
				
                out.println("==== Invariant verification ====");
                
                ArrayList<String> invalidBElements = rootASTD.invalidBElements();
                if (!invalidBElements.isEmpty()) {
                	for (String s : invalidBElements) {
                		out.println(s);
                	}
            		out.println("The generation of proof obligations is aborted");
                	return;
                }
                
                out.println("==== Generation of the proof obligation for the initialisation ====");
                
                ArrayList<String> proofObligations = new ArrayList<String>();
                proofObligations.addAll(rootASTD.generatePOInitialisationFromASTD(null, null, Utils.skiplist(), Utils.skiplist(), true));
                

                out.println("==== Generation of all triplets with sequences of guard/action ====");
                
                ArrayList<HashSet<ASTD>> SEEN = new ArrayList<HashSet<ASTD>>();
                ArrayList<HashSet<ASTD>> NEXT = new ArrayList<HashSet<ASTD>>();
                
                HashSet<ASTD> initialState = rootASTD.getInitialPosition();
                NEXT.add(initialState);
                
                while(!NEXT.isEmpty()) {
                	HashSet<ASTD> actualState = NEXT.remove(NEXT.size()-1);
                	SEEN.add(actualState);
                	
                	HashSet<Event> outingEvents = rootASTD.getAllOutingEvents(actualState);
                	Iterator<Event> it = outingEvents.iterator();
                	
                	while(it.hasNext()) {
                		Event actualEvent = it.next();
                		SetTriplet actualTriplets = rootASTD.getAllTriplets(actualState, actualEvent).allTransitionTriplets();
                		

                		proofObligations.addAll(actualTriplets.generatePOFromTriplets(rootASTD, actualState));
                		
                		// We verify if reached states must be added to the pool
                		ArrayList<HashSet<ASTD>> OUT = actualTriplets.getAllOutStates();
                		Iterator<HashSet<ASTD>> itout = OUT.iterator();
                		while(itout.hasNext()) {
                			HashSet<ASTD> outState = itout.next();
                			if(!Utils.hasBeenSeen(SEEN, NEXT, outState)) {
                				NEXT.add(outState);
                			}
                		} // end verification
                	}

                    
                }
                
                
                
                
                /*
                out.println("==== Generation of proof obligations ====");
                
                ArrayList<String> proofObligations = new ArrayList<String>();
                proofObligations.addAll(rootASTD.generatePOInitialisationFromASTD(null, null, Utils.skiplist(), Utils.skiplist(), true));
                proofObligations.addAll(rootASTD.generatePOTransitionFromASTD(null, Utils.skiplist()));
                
				*/
				
				out.println("==== Generation of Rodin context ====");
				
				try {
					
                IRodinProject projet = RodinCreation.createRodinProject(iProject.getName());
                
                RodinContext context = new RodinContext(fileName);            
                
                
                int count = 0;
	                for (String po : proofObligations) {
	                	count++;
	                	Start temp = Utils.generateTreeFromInv(po);
	                	PrettyPrinterUnicode pp = new PrettyPrinterUnicode();
	                    temp.apply(pp);
	                    RodinElement theoreme = new RodinElement("th" + count, "", pp.getPrettyPrint());
	                    context.addTheorem(theoreme);
	                }
                
                RodinCreation.creatRodinContext(context, projet);
                
                out.println("Rodin context has been generated successfully");
				}
				catch (Exception e) {
					out.println("An error has occured!");
					e.printStackTrace();
				}
				
			}
			else {
				out.println("Wrong file extension");
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
}
