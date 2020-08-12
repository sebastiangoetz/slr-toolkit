package de.tudresden.slr.classification.validators;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.classification.ui.MalformedTermNameDialog;

public class MalformedTermNameHandlerImpl implements IMalformedTermNameHandler {

	public static final String DEFAULT_PREFIX = "T ";
	public final static String EXP_NONALPHANUMERIC = "[^A-Za-z0-9 ]";
	
	private boolean useDefault;
	private IInputValidator validator;
	
	public MalformedTermNameHandlerImpl() {
		validator = new TermNameValidatorImpl();
		useDefault = true;
	}
	
	public MalformedTermNameHandlerImpl(boolean useDefault, IInputValidator validator) {
		this.useDefault = useDefault;
		this.validator = validator;
	}
	
	public boolean getUseDefault() {
		return useDefault;
	}
	
	public void setUseDefault(boolean useDefault) {
		this.useDefault = useDefault;
	}
	
	
	/**
	 * Transforms a string that would be invalid when used as term names into a valid one.
	 * If useDefault is false, the user will be prompted by a dialog to supply a valid term name manually.
	 * 
	 * @param name  Term name to be transformed
	 * @return Valid term name
	 */
	public String handleMalformedTermName(String name) {
		if(validator.isValid(name) == null) return name;

		name = name.replaceAll(EXP_NONALPHANUMERIC, "");
		String returnName = DEFAULT_PREFIX + name;
		if (!useDefault) {
			MalformedTermNameDialog dlg = new MalformedTermNameDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Invalid Term name", "Please enter a new term name that starts with a letter.", returnName,
					validator);
			returnName = (dlg.open() == 0) ? name = dlg.getValue() : returnName;
			returnName.replaceAll(EXP_NONALPHANUMERIC, "");
			useDefault = dlg.getUseDefault();
		}
		return returnName;
	}

}
