package de.tudresden.slr.model.taxonomy.ui.views;

import org.eclipse.emf.common.command.AbstractCommand;

public abstract class ExecuteCommand extends AbstractCommand implements
		Executable {

	@Override
	public boolean prepare() {
		return true;
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public void execute() {
		toExecute();
	}
}
