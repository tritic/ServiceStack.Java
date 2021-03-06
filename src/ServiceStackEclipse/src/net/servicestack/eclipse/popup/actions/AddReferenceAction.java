package net.servicestack.eclipse.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import net.servicestack.eclipse.wizard.AddReferenceWizard;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jdt.core.IPackageFragment;

public class AddReferenceAction implements IObjectActionDelegate {

	private Shell shell;
	private IFolder _selection;
	private IPackageFragment _packageFragment;
	private ISelectionService selectionService;
	/**
	 * Constructor for Action1.
	 */
	public AddReferenceAction() {
		super();
	}
	
	/**
	 * @see IObjectActionDelegate
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		targetPart.getSite().getWorkbenchWindow().getSelectionService();
		selectionService = targetPart.getSite().getWorkbenchWindow().getSelectionService();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (_selection != null) {
			IStructuredSelection packageSelection = (IStructuredSelection) selectionService
					.getSelection("org.eclipse.jdt.ui.PackageExplorer");
			Object firstElement = packageSelection.getFirstElement();
			if(firstElement instanceof IPackageFragment) {
				_packageFragment = (IPackageFragment)firstElement;
			}
			AddReferenceWizard generationWizard = new AddReferenceWizard(_selection, _packageFragment);
			WizardDialog dialog = new WizardDialog(shell, generationWizard);
			if (dialog.open() == WizardDialog.OK){
//				MessageDialog.openInformation(shell, "CTE tree generation", "CTE trees are being generated, checking the process view for details!");
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection structured = (IStructuredSelection)selection;
			Object firstElement = structured.getFirstElement();
			if(firstElement instanceof IFolder) {
				_selection = (IFolder) firstElement;	
			}
		}
	}
	
}
