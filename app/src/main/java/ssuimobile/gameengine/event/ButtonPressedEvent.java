package ssuimobile.gameengine.event;


/**

 *
 */
public class ButtonPressedEvent extends FSMEvent {

	protected int _buttonNumber = -1;
	public int getButtonNumber() {return _buttonNumber;}
	
	/**
	 * 
	 */
	public ButtonPressedEvent(int whichButton) {
		super(FSMEventType.BUTTON_PRESSED);
		_buttonNumber = whichButton;
	}
}
