package ssuimobile.gameengine.action;


/**
 *
 */
public abstract class XYAction extends FSMAction {
	protected float _x = 0.0f;
	public float getX() {return _x;}
	
	protected float _y = 0.0f;
	public float getY() {return _y;}
	
	/**
	 */
	public XYAction(int type, float x, float y) {
		super(type);
		_x = x;
		_y = y;
	}

}
