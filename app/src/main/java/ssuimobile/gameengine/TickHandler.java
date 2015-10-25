package ssuimobile.gameengine;

import android.os.Handler;
import android.os.Message;

/**
 *
 */
public class TickHandler extends Handler {
	protected GameEnginePreBase _notifyObj;
	public GameEnginePreBase getNotifyObj() {return _notifyObj;}
	
	protected boolean _running = true;
	public boolean getRunning() {return _running;}
	public void stop() {_running = false;}
	public void start() {_running = true;}
	
	public static final long TICK_INTERVAL_MSEC = 15;
	
	@Override 
	public void handleMessage(Message msg) {
		if (_notifyObj != null) {
			_notifyObj.doAnimationWork();
			if (getRunning()) 
				sendEmptyMessageDelayed(0, TICK_INTERVAL_MSEC);
		}
	}
	
	public TickHandler(GameEnginePreBase notifyObj) {
		super();
		_notifyObj = notifyObj;
		sendEmptyMessageDelayed(0, TICK_INTERVAL_MSEC);
	}

}
