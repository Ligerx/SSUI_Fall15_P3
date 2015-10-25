package ssuimobile.gameengine.action;


/**
 *
 */
public class SendMessageAction extends StringAction {

	protected int _targetCharacter = -1;
	public int getTargetCharacter() {return _targetCharacter;}
	
	/**
	 * @param message
	 */
	public SendMessageAction(int targetCharacter, String message) {
		super(FSMActionType.SEND_MESSAGE, message);
		_targetCharacter = targetCharacter;
	}

}
