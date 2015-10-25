package ssuimobile.gameengine.eventmatch;

import ssuimobile.gameengine.event.FSMEvent;
import ssuimobile.gameengine.event.FSMEventType;
import ssuimobile.gameengine.event.MessageEvent;

/**
 *
 */
public class MessageMatch extends TypeMatch {
	protected String _messageToMatch = "";
	public String getMessageToMatch() {return _messageToMatch;}
	
	@Override
	public boolean match(FSMEvent evt) {
		return super.match(evt) && evt instanceof MessageEvent &&
		((MessageEvent)evt).getMessage().equals(getMessageToMatch());
	}
	
	public MessageMatch(String msg) {
		super(FSMEventType.MESSAGE_ARRIVED);
		if (msg == null) msg="";
		_messageToMatch = msg;
	}
}
