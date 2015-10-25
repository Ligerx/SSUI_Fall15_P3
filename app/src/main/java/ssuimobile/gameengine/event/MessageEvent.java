package ssuimobile.gameengine.event;


/**
 *
 */
public class MessageEvent extends FSMEvent {
	protected String _message = "";
	public String getMessage() {return _message;}
	
	public MessageEvent(String message) {
		super(FSMEventType.MESSAGE_ARRIVED);
		if (message == null) 
			message = "";
		_message = message;
	}
}
