package ssuimobile.gameengine.event;


/**
 *
 */
public abstract class XYEvent extends FSMEvent {

	protected float _x = 0;
	public float getX() {return _x;}
	
	protected float _y = 0;
	public float getY() {return _y;}
	
	public void offset(float dx, float dy) {
		_x += dx;
		_y += dy;
	}
	
	/**
	 */
	public XYEvent(int evtType, float x, float y) {
		super(evtType);
		_x = x; 
		_y = y;
	}
}
