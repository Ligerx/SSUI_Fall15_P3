package ssuimobile.gameengine.action;


public abstract class StringAction extends FSMAction {
	protected String _message = "";
	public String getMessage() {return _message;}
	
	public StringAction(int type, String message) {
		super(type);
		if (message == null) message = "";
		_message = message;
	}
}
