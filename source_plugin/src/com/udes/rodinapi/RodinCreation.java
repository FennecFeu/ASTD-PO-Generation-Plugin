package com.udes.rodinapi;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.rodinp.core.*;
import org.eventb.core.*;


public class RodinCreation {

	private static IRodinProject rodinProject = null;
	private static IRodinFile rodinFile = null;

	public static IRodinProject createRodinProject(String name) throws RodinDBException {

		RodinCore.run(new IWorkspaceRunnable() {
			public void run(final IProgressMonitor monitor) throws CoreException {
				monitor.beginTask("", 10);
				rodinProject = RodinApi.createRodinProject(name);
				rodinProject.save(monitor, true);
				monitor.done();
			}
		}, null);

		return rodinProject;

	}

	public static void creatRodinContext(RodinContext rodinContext, IRodinProject rp) throws RodinDBException {

		RodinCore.run(new IWorkspaceRunnable() {
			public void run(final IProgressMonitor monitor) throws CoreException {
				monitor.beginTask("", 10);
				rodinFile = RodinApi.createRodinConstruct(rodinContext.getName() + ".buc", rp, "");
				rodinContext.toIRodinFile(rodinFile);
				rodinFile.save(monitor, true);
				monitor.done();
			}
		}, null);
	}

	public static void creatRodinMachine(String name, IRodinProject rp) throws RodinDBException {

		RodinCore.run(new IWorkspaceRunnable() {
			public void run(final IProgressMonitor monitor) throws CoreException {
				monitor.beginTask("", 10);
				rodinFile = RodinApi.createRodinConstruct(name + ".bum", rp, "");
				rodinFile.save(monitor, true);
				monitor.done();
			}
		}, null);

	}

	// rajouter un contexte etendu ----------------------------------------------
	public static void addExtendsContext(String contextName, IRodinFile rodinFile, String c) {
		try {
			IExtendsContext xtends = (IExtendsContext) RodinApi.createRodinElement(IExtendsContext.ELEMENT_TYPE,
					contextName, rodinFile, c);
			xtends.setAbstractContextName(contextName, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// rajouter un ensemble abstrait
	// ----------------------------------------------------
	public static void addSet(String setName, IRodinFile rodinFile, String c) {
		try {
			RodinApi.createRodinElement(ICarrierSet.ELEMENT_TYPE, setName, rodinFile, c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// rajouter une constante
	// -------------------------------------------------------
	public static void addConstant(String consName, IRodinFile rodinFile, String c) {
		try {
			RodinApi.createRodinElement(IConstant.ELEMENT_TYPE, consName, rodinFile, c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// rajouter un axiome
	// -------------------------------------------------------------
	public static void addAxiom(String axExpression, String et, IRodinFile rodinFile, String c) {
		try {
			IAxiom axiom = (IAxiom) RodinApi.createRodinElement(IAxiom.ELEMENT_TYPE, et, rodinFile, c);
			axiom.setPredicateString(axExpression, null);
			axiom.setTheorem(false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// rajouter un theoreme
	// -------------------------------------------------------------
	public static void addTheorem(String thExpression, String et, IRodinFile rodinFile, String c) {
		try {
			IAxiom axiom = (IAxiom) RodinApi.createRodinElement(IAxiom.ELEMENT_TYPE, et, rodinFile, c);
			axiom.setPredicateString(thExpression, null);
			axiom.setTheorem(true, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static IRodinProject getCurrentProject() {
		return rodinProject;
	}

	public static IRodinFile getLastComponent() {
		return rodinFile;
	}

}