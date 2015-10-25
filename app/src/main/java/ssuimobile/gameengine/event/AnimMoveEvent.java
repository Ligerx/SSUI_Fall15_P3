package ssuimobile.gameengine.event;



/**
 *
 */
public class AnimMoveEvent extends XYEvent {
	public AnimMoveEvent(float x, float y) {
		super(FSMEventType.ANIM_MOVE, x,y);
	}
}
