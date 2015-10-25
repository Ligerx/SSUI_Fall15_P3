package ssuimobile.gameengine.event;



/**
 *
 */
public class TouchReleaseEvent extends XYEvent {
	/**
	 * @param x
	 * @param y
	 */
	public TouchReleaseEvent(float x, float y) {
		super(FSMEventType.TOUCH_RELEASE, x, y);
	}
}
