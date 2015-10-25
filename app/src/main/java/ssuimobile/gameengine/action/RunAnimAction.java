package ssuimobile.gameengine.action;


/**
 */
public class RunAnimAction extends FSMAction {
	protected int _movingCharacter = -1;
	public int getMovingCharacter() {return _movingCharacter;}
	
	protected int _targetCharacter = -1;
	public int getTargetCharacter() {return _targetCharacter;}
	
	protected float _targetOffsetX = 0.0f;
	public float getTargetOffsetX() {return _targetOffsetX;}
	
	protected float _targetOffsetY = 0.0f;
	public float getTargetOffsetY() {return _targetOffsetY;}
	
	protected int _animDurationMSec = 0;
	public int getAnimDurationMSec() {return _animDurationMSec;}
	
	protected String _endMessage = "";
	public String getEndMessage() {return _endMessage;}
	
	protected String _passOverMessage = "";
	public String getPassOverMessage() {return _passOverMessage;}
	
	/**
	 */
	public RunAnimAction(
			int movingCharacter, 
			int targetCharacter, 
			int duration, 
			float targetOffsetX,
			float targetOffsetY,
			String endMessage,
			String passOverMessage) 
	{
		super(FSMActionType.RUN_ANIM);
		_movingCharacter = movingCharacter;
		_targetCharacter = targetCharacter;
		_targetOffsetX = targetOffsetX;
		_targetOffsetY = targetOffsetY;
		_animDurationMSec = duration;
		_endMessage = endMessage;
		_passOverMessage = passOverMessage;
	}

}
