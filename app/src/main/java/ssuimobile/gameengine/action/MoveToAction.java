package ssuimobile.gameengine.action;


/**
 *
 */
public class MoveToAction extends XYAction {
	/**
	 * @param x
	 * @param y
	 */
	public MoveToAction(float x, float y) {
		super(FSMActionType.MOVE_TO, x, y);
	}

}
