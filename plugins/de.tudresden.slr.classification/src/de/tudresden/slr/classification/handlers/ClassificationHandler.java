package de.tudresden.slr.classification.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.classification.wizards.GenerateStandardTaxonomyWizard;

import org.eclipse.jface.wizard.WizardDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ClassificationHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerUtil.getActiveWorkbenchWindowChecked(event);
		GenerateStandardTaxonomyWizard wiz = new GenerateStandardTaxonomyWizard();
		(new WizardDialog(wiz.getShell(),wiz)).open();
		return null;
	}
}
