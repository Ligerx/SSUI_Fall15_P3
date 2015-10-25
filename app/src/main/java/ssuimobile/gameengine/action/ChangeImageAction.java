package ssuimobile.gameengine.action;

import android.graphics.Bitmap;

/**
 *
 */
public class ChangeImageAction extends FSMAction {
	
	protected Bitmap _image = null;
	/**
	 * 
	 * @return the image that should be shown. If null, no image should be displayed
	 */
	public Bitmap getImage() {return _image;}
	
	/**
	 * 
	 * @param image The image that should be shown
	 */
	public ChangeImageAction(Bitmap image) {
		super(FSMActionType.CHANGE_IMAGE);
		_image = image;
	}

}
