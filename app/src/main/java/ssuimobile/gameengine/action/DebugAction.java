package ssuimobile.gameengine.action;


/**
 * 
 *
 */
public class DebugAction extends StringAction {
	
	public DebugAction(String message) {
		super(FSMActionType.DEBUG_MESSAGE, message);
		if (message == null) message = "";
		_message = message;
	}
	
	/**
	 * 
	 */
	public DebugAction() {
		this("Ping...");
	}

}
