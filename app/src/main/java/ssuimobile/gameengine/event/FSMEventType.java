package ssuimobile.gameengine.event;

import java.util.HashMap;

/**
 * @author Scott Hudson
 *
 */
public class FSMEventType {
	public static final int NO_EVENT        = 0;
	public static final int BUTTON_PRESSED  = 1;
	public static final int ANIM_START      = 2;
	public static final int ANIM_MOVE       = 3;
	public static final int ANIM_END        = 4;
	public static final int MESSAGE_ARRIVED = 5;
	public static final int TOUCH_PRESS     = 6;
	public static final int TOUCH_MOVE      = 7;
	public static final int TOUCH_RELEASE   = 8;
	public static final int DRAG_MOVE       = 9;
	public static final int DRAG_END        = 10;
	
	protected static String[] _typeNames = {
	  "NO_EVENT", "BUTTON_PRESSED", 
	  "ANIM_START", "ANIM_MOVE", "ANIM_END", 
	  "MESSAGE_ARRIVED",
	  "TOUCH_PRESS", "TOUCH_MOVE", "TOUCH_RELEASE",
	  "DRAG_MOVE", "DRAG_END" 
	  };
	
	protected static HashMap<String, Integer> _nameDict = 
		new HashMap<String, Integer>(_typeNames.length);
	
	static { 
		for (int i = 0; i< _typeNames.length; i++) {
			_nameDict.put(_typeNames[i], i);
		}
	}
	
	public static int indexFromName(String typeName) {
		if (typeName == null) return -1;
		Integer result = _nameDict.get(typeName);
		if (result == null) return -1;
		return result;
	}
	
	public static String nameFromIndex(int indx) {
		if (!isValid(indx)) return null;
		else return _typeNames[indx];
	}
	
	public static boolean isValid(int indx) {
		return indx < 0 || indx >= _typeNames.length;
	}
}
