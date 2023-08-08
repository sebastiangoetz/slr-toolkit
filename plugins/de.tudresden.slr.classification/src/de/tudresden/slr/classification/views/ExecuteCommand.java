package de.tudresden.slr.classification.views;

import org.eclipse.emf.common.command.AbstractCommand;

public abstract class ExecuteCommand extends AbstractCommand {

	@Override
	public boolean prepare() {
		return true;
	}

	@Override
	public void redo() {
		execute();
	}
}