
package ssuimobile.gameengine;

/**
 * This class holds all of the information about a particular animation that is 
 * occurring in the system.
 */
public class AnimationRecord {
	protected int _characterToMove = 0;
	public int getCharacterToMove() {return _characterToMove;}
	public void setCharacterToMove(int ch) {_characterToMove = ch;}
	
	protected int _characterToNotify = 0;
	public int getCharacterToNotify() {return _characterToNotify;}
	public void setCharacterToNotify(int ch) {_characterToNotify = ch;}
	
	protected String _notifyMessage = "";
	public String getNotifyMessage() {return _notifyMessage;}
	public void getNotifyMessage(String msg) {_notifyMessage = msg;}
	
	protected String _passOverMessage = "";
	public String getPassOverMessage() {return _passOverMessage;}
	public void getPassOverMessage(String msg) {_passOverMessage = msg;}
	
	protected float _currentX = 0;
	public float getCurrentX() {return _currentX;}
	public void setCurrentX(float x) {_currentX = x;}
	
	protected float _currentY = 0;
	public float getCurrentY() {return _currentY;}
	public void setCurrentY(float y) {_currentY = y;}
	
	protected float _xVelocity = 0; // in pixels per msec
	public float getXVelocity() {return _xVelocity;}
	public void setXVelocity(float xv) {_xVelocity = xv;}
	
	protected float _yVelocity = 0;
	public float getYVelocity() {return _yVelocity;}
	public void setYVelocity(float yv) {_yVelocity = yv;}
	
	protected int   _msecLeft = 0;
	public int getMSecLeft() {return _msecLeft;}
	public void setMSecLeft(int msec) {
		if (msec < 0) msec = 0;
		_msecLeft = msec;
	}
	
	public boolean advanceMSec(int numMSec) {
		
		// don't advance if we are already done
		if (_msecLeft == 0) return true;
		
		// don't go past the end time
		if (numMSec > _msecLeft) numMSec = _msecLeft;
		
		// move the point
		_currentX += _xVelocity*(float)numMSec;
		_currentY += _yVelocity*(float)numMSec;
		
		// count off the time and return whether we are done
		_msecLeft -= numMSec;
		if (_msecLeft < 0) _msecLeft = 0;
		return _msecLeft == 0;
	}
	
	public AnimationRecord(
			int charToMove, int charToNotif,
			String notifMsg,
			String passOverMsg,
			float startX, float startY,
			float endX, float endY,
			int durationMSec) 
	{
		_currentX = startX; 
		_currentY = startY;
		if (durationMSec < 0) durationMSec = 0;
		_msecLeft = durationMSec;
		if (_msecLeft == 0) {
			_xVelocity = _yVelocity = 0;
		} else {
			_xVelocity = (endX-startX)/(float)durationMSec;
			_yVelocity = (endY-startY)/(float)durationMSec;
		}
		_characterToMove = charToMove;
		_characterToNotify = charToNotif;
		_notifyMessage = notifMsg;
		_passOverMessage = passOverMsg;
	}
}
