package com.udes.rodinapi;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ICommentedElement;
import org.eventb.core.IConfigurationElement;
import org.eventb.core.IIdentifierElement;
import org.eventb.core.ILabeledElement;
import org.eventb.core.IRefinesEvent;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IInternalElementType;
import org.rodinp.core.IRodinDB;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.IRodinProject;
import org.rodinp.core.RodinCore;

public class RodinApi {
	
	/*
	 * creation du projet RODIN en donnant le nom en parametre
	 */
	public static IRodinProject createRodinProject(final String projectName) {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
		try {
			final IRodinDB rodinDB = RodinCore.valueOf(workspace.getRoot());
			final IRodinProject rodinProject = rodinDB.getRodinProject(projectName);
			final IProject project = rodinProject.getProject();
			if (!project.exists()) project.create(null);
			project.open(null);

			// add the rodin nature if it isn't already there
			final IProjectDescription description = project.getDescription();
			final String[] natures = description.getNatureIds();
			boolean alreadyRodin = false;
			for (int i = 0; i < natures.length; ++i)
				if (RodinCore.NATURE_ID.equals(natures[i])) {
					alreadyRodin = true;
					break;
				}
			if (!alreadyRodin) {     // Add the Rodin nature
				final String[] newNatures = new String[natures.length + 1];
				System.arraycopy(natures, 0, newNatures, 0, natures.length);
				newNatures[natures.length] = RodinCore.NATURE_ID;
				description.setNatureIds(newNatures);
				project.setDescription(description, null);
			}
			project.setDerived(true);  // mark the project as derived (this could be used to distinguish user editable projects
			return rodinProject;
		} catch (final CoreException e) {
			e.printStackTrace();
			return null ;
		}
	}
	/*
	 * creation d'un composant Rodin (CONTEXT ou MACHINE)
	 */
	public static IRodinFile createRodinConstruct(final String filename, final IRodinProject rProject, final String comment) {
		if (rProject == null)
			return null;
		try {
			final IRodinFile rodinFile = rProject.getRodinFile(filename);
			rodinFile.create(true, null);
			rodinFile.getResource().setDerived(true); // mark the file as derived
			if (rodinFile.getRoot() instanceof IConfigurationElement) {
				((IConfigurationElement)rodinFile.getRoot()).setConfiguration(IConfigurationElement.DEFAULT_CONFIGURATION, null);
			}
			if (rodinFile.getRoot() instanceof ICommentedElement) {
				if (comment != null && !comment.trim().equals(""))
					((ICommentedElement) rodinFile.getRoot()).setComment(comment, null);
			}
			return rodinFile;
		} catch (final Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	/*
	 * creation d'un élément d'un composant
	 * SETS, CONSTANTS, AXIOMS, THEOREMS, VARIABLES, INVARIANTS, EVENTS ...
	 */
	public static IInternalElement createRodinElement(
			final IInternalElementType type, final String name, final IRodinFile rodinFile,
			final String comment) {
		if (rodinFile == null)
			return null;
		try {
			final String uniqueName = name;
			final IInternalElement rodinEl = createRodinElement(rodinFile.getRoot().getInternalElement(type, uniqueName), name, comment);
			return rodinEl;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static IInternalElement createRodinElement(
			final IInternalElementType type, final String name, final IInternalElement parent,
			final String comment, final IRodinFile rodinFile) {
		if (parent == null)
			return null;
		try {
			final String uniqueName = name;
			final IInternalElement rodinEl = createRodinElement(parent.getInternalElement(type, uniqueName), name, comment);
			return rodinEl;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static IInternalElement createRodinElement(final IInternalElement rodinEl, String name, final String comment)
			throws Exception {
		try {
			rodinEl.create(null, null);
			if (rodinEl instanceof ILabeledElement)
				((ILabeledElement) rodinEl).setLabel(name, null);
			if (rodinEl instanceof IIdentifierElement)
				((IIdentifierElement) rodinEl).setIdentifierString(name, null);
			if (rodinEl instanceof ICommentedElement)
					((ICommentedElement) rodinEl).setComment(comment,
							null);
			if (rodinEl instanceof IRefinesEvent)
				((IRefinesEvent) rodinEl).setAbstractEventLabel(name, null);

			return rodinEl;
		} catch (final Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
}