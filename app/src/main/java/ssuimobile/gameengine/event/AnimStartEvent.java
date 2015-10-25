package ssuimobile.gameengine.event;



/**
 *
 */
public class AnimStartEvent extends XYEvent {
	
	/**
	 */
	public AnimStartEvent(float x, float y) {
		super(FSMEventType.ANIM_START, x,y);
	}

}
