package ssuimobile.gameengine.event;



public class DragEndEvent extends XYEvent {
	/**
	 * @param x
	 * @param y
	 */
	public DragEndEvent(float x, float y) {
		super(FSMEventType.DRAG_END, x, y);
	}
}
