package ssuimobile.gameengine.event;


/**
 *
 */
public class FSMEvent implements Cloneable {
	protected int _type = FSMEventType.NO_EVENT;
	public int getType() {return _type;}
	public FSMEvent copy() {
		try {
			return (FSMEvent)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public FSMEvent(int typ) {
		_type = typ;
	}
}
