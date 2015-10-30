package ssuimobile.gameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import ssuimobile.gameengine.action.RunAnimAction;
import ssuimobile.gameengine.event.AnimEndEvent;
import ssuimobile.gameengine.event.AnimMoveEvent;
import ssuimobile.gameengine.event.AnimStartEvent;
import ssuimobile.gameengine.event.FSMEvent;
import ssuimobile.gameengine.event.MessageEvent;
import ssuimobile.gameengine.event.XYEvent;

/**
 * Abstract class describing the game engine. This implements code for 
 * animations and dispatching touch events. Students should extend this class
 * and implement the abstract methods.
 * @author sauvik
 *
 */
public abstract class GameEnginePreBase extends View {

	public static final int NO_CHARACTER = -1;
	
	/**
	 * Array of all characters in this game, in drawing order
	 */
	protected GameCharacter[] _characters = new GameCharacter[0];
	
	/**
	 * Array of all buttons in this game
	 */
	protected Button [] _buttons = new Button[0];
	
	/**
	 * Dictionary that looks up the array ID of a button given its name
	 */
	protected HashMap<String, Integer> _buttonDict = 
		new HashMap<String, Integer>();
	
	/**
	 * Convenience method to get the ID for a button
	 * @param s button name
	 * @return button id
	 */
	public int getButtonIDFromName(String s){
		if(_buttonDict != null){
			return _buttonDict.get(s);
		}
		return NO_CHARACTER;
	}
	
	/**
	 * Convenience method to see if a given index is a character in the game
	 * @param indx character index to check
	 * @return true if is character in the game otherwise false
	 */
	public boolean isCharacter(int indx) {
		return _characters != null && indx >= 0 && indx < _characters.length;
	}
	
	public int numCharacters() {return _characters.length;}
	
	public GameCharacter getCharacterAt(int indx) {
//		if (!isCharacter(indx)) return null; else return _characters[indx];
		Log.d("GameEnginePreBase", "getCharacterAt # " + indx);
		Log.d("GameEnginePreBase", "getCharacterAt isCharacter?" + isCharacter(indx));

		if (!isCharacter(indx)) {
			return null;
		}
		else {
			return _characters[indx];
		}

	}
	
	/**
	 * Marks a character as invalidated so that the game board is redrawn
	 * @param indx the damaged character
	 */
	public void damageCharacter(int indx) {
		if (isCharacter(indx)) 
			invalidate();
	}
	
	/**
	 * Find and return the list of characters whose bounds overlap the given
	 * rectangular area.  The characters (if any) in the list should be in 
	 * reverse drawing order.  That is the characters drawn later should appear
	 * earlier in the list.  
	 * @param area Rectangle we are testing for overlap against.
	 * @return A list of character objects whose bounds overlap the given 
	 *         rectangle (or an empty list if there are none).
	 */
	protected abstract List<GameCharacter> charactersUnder(RectF area);
	
	/**
	 * Find and return the list of characters whose bounds contain the given
	 * Point. The characters (if any) in the list should be in reverse drawing 
	 * order. That is the characters drawn later should appear earlier in the list.  
	 * @param hitPoint Point we are testing against.
	 * @return A list of character objects whose bounds contain the given 
	 *         point (or an empty list if there are none).
	 */
	protected List<GameCharacter> charactersUnder(PointF hitPoint) {
		return charactersUnder(new RectF(hitPoint.x,hitPoint.y,
				                         hitPoint.x+1,hitPoint.y+1));
	}
	
	/**
	 * Dispatch the given event to one character under the given x,y position.  
	 * When multiple characters are under the position we offer it to them
	 * in reverse drawing order.  As soon as a character takes the event 
	 * (returns true from its deliverEvent() method) we stop offering it to 
	 * others so that only one character gets the event.
	 * @param evt the event to attempt to dispatch
	 * @return true if some character took the event
	 */
	protected abstract boolean dispatchPositionally(XYEvent evt);
	
	/**
	 * Dispatch the given event to one character whose bounds overlap the given
	 * rectangle. When multiple characters are overlapped we offer it to them
	 * in reverse drawing order.  As soon as a character takes the event 
	 * (returns true from its deliverEvent() method) we stop offering it to 
	 * others so that only one character gets the event.
	 * @param evt the event to attempt to dispatch
	 * @return true if some character took the event
	 */
	protected abstract boolean dispatchPositionally(RectF inArea, FSMEvent evt);
	
	/**
	 * Dispatch the given event directly to the given character.
	 * @param evt the event to attempt to dispatch
	 * @return true if the character took the event
	 */
	protected abstract boolean dispatchDirect(int toChar, FSMEvent evt);
	
	/**
	 * Dispatch the given event to all characters in reverse drawing order.  
	 * This dispatch does not stop after the first character accepts the event,
	 * but instead always continues through the list of all characters.
	 * @param evt the event to attempt to dispatch
	 * @return true if some character took the event
	 */
	protected abstract boolean dispatchToAll(FSMEvent evt);
	
	/**
	 * Attempt to dispatch the given event to all characters in reverse drawing 
	 * order stopping as soon as some character takes the event (returns true 
	 * from its deliverEvent() method).
	 * @param evt the event to attempt to dispatch
	 * @return true if some character took the event
	 */
	protected abstract boolean dispatchTryAll(FSMEvent evt);
	
	/**
	 * The character which is currently the drag focus (if any).
	 */
	protected int _dragFocus = NO_CHARACTER;
	
	/**
	 * The offset from x position that the currently dragged object was 
	 * pressed at (as passed to requestDragFocus) to the left edge of the 
	 * dragged object.  All drag events are modified to be offset by this 
	 * amount so that they are all indicating where the top left corner
	 * should be placed during the drag.  This value only has meaning when
	 * _dragFocus is a valid character index.
	 */
	protected float _grabPointX = 0.0f;
	
	/**
	 * The offset from y position that the currently dragged object was 
	 * pressed at (as passed to requestDragFocus) to the top edge of the 
	 * dragged object.  All drag events are modified to be offset by this 
	 * amount so that they are all indicating where the top left corner
	 * should be placed during the drag.  This value only has meaning when
	 * _dragFocus is a valid character index.
	 */
	protected float _grabPointY = 0.0f;
	
	/**
	 * Switch the current drag focus to the given character.  The offset values
	 * given indicate where inside the given character the cursor was when the
	 * drag was started.  This offset will be applied (negatively) to all 
	 * drag events later delivered.  This will allow each of those events to 
	 * reflect where the top-left corner of the dragged character should be 
	 * placed, rather than where the cursor currently is.
	 * 
	 * @param dragChar character that is to be the new drag focus.
	 * @param grabX x distance from the left of the character that the cursor
	 *              was when the drag was started.
	 * @param grabY y distance from the top of the character that the cursor
	 *              was when the drag was started.
	 */
	public void requestDragFocus(int dragChar, float grabX, float grabY) {
		if (isCharacter(dragChar)) {
			_dragFocus = dragChar;
			_grabPointX = grabX;
			_grabPointY = grabY;
		}
	}
	
	/**
	 * Clear the current drag focus.
	 */
	public void releaseDragFocus() {_dragFocus = NO_CHARACTER;}
	
	/** 
	 * Dispatch the given event to the current drag focus object (if any).
	 * If there is no current drag focus or the current drag focus object
	 * rejects the event (returns false from its deliverEvent() method), this 
	 * method returns false.  All events which contain an x,y position (i.e.,
	 * subclasses of XYEvent) will have their x,y position adjusted by 
	 * (-_grabPointX, -grabPointY) prior to being delivered (or more correctly
	 * a copy of the event will be adjusted and delivered).  In this way the 
	 * position indicated in the event will reflect where the top-left corner
	 * of the dragged character should be placed, rather than where the cursor 
	 * was (which will normally be inside the character; specifically at a 
	 * distance of (_grabPointX, _grabPointY) from the top-left of the object).
	 * 
	 * @param evt the event being dispatched
	 * @return true if the event was consumed by the drag focus
	 */
	protected abstract boolean dispatchDragFocus(FSMEvent evt);
	
	/**
	 * Send a message with the text "$INIT$" to every character.  This should
	 * be called once from the constructor after everything else is initialized.
	 */
	protected void sendInitMessages() {
		dispatchToAll(new MessageEvent("$INIT$"));
	}
	
	/**
	 * Sends a message to the character at the specified index
	 * @param toCharacterIndx index of the target character for the message
	 * @param message message to be sent
	 * @return whether or not the message was received
	 */
	public boolean sendMessage(int toCharacterIndx, String message) {
		if (isCharacter(toCharacterIndx)) {
			return dispatchDirect(toCharacterIndx, new MessageEvent(message));
		} else {
			return false;
		}
	}
		
	protected List<AnimationRecord> _activeAnimations = 
		new Vector<AnimationRecord>();
	
	protected void endAnimation(AnimationRecord anim, float x, float y) {
		// sanity check
		if (anim == null) return;

		// send the end animation event to the moving character
		dispatchDirect(anim.getCharacterToMove(),new AnimEndEvent(x,y));
		
		// send the notification to the target
		dispatchDirect(anim.getCharacterToNotify(), 
					   new MessageEvent(anim.getNotifyMessage()));
	}
	
	/**
	 * Starts the animation that is specified in this action
	 * @param action that describes the animation that is to be run
	 */
	protected void startNewAnimation(RunAnimAction action) {
		float startx, starty, endx, endy;
		
		// sanity check
		if (action == null) return;
		
		// pull out parts of the action so we have short names
		GameCharacter mv = getCharacterAt(action.getMovingCharacter());
		GameCharacter targ = getCharacterAt(action.getTargetCharacter());
		
		// figure out start location
		if (mv != null) {
			startx = mv.getX();
			starty = mv.getY();
		} else {
			startx = starty = 0;
		}
		
		// figure out end location
		if (targ != null) {
			endx = targ.getX() + action.getTargetOffsetX();
			endy = targ.getY() + action.getTargetOffsetY();
		} else {
			// if there is no target object then offset is global coords
			endx = action.getTargetOffsetX();
			endy = action.getTargetOffsetY();
		}
		
		// create a record to remember the details of this animation
		AnimationRecord newAnim = new AnimationRecord(
				action.getMovingCharacter(), 
				action.getTargetCharacter(),
				action.getEndMessage(), 
				action.getPassOverMessage(), 
				startx, starty, endx, endy, 
				action.getAnimDurationMSec());
		
		// send the start animation event to the moving character
		if (mv != null) {
			dispatchDirect(action.getMovingCharacter(), 
					new AnimStartEvent(startx, starty));
		}
		
		// if there was no duration finish the animation here
		if (action.getAnimDurationMSec() <= 0) {
			endAnimation(newAnim, endx, endy);
		} else {
			// otherwise, queue the new animation to play out in the future
			_activeAnimations.add(newAnim);
		}
	}
	
	
	protected static long lastTime = SystemClock.uptimeMillis();
	
	
	protected void doAnimationWork() {
		long now = SystemClock.uptimeMillis();
		int sinceLast = (int)(now - lastTime);
		
		// only do work if at least 10msec has transpired
		if (sinceLast < 10) return;
		
		// remember this time as when the last work was done
		lastTime = now;
		
		// deliver an animation step for each one
		for (AnimationRecord anim : _activeAnimations) {
			anim.advanceMSec(sinceLast);
			GameCharacter mv = getCharacterAt(anim.getCharacterToMove());
			if (mv != null) {
				// deliver move event
				dispatchDirect(mv.getCharacterIndex(), 
					new AnimMoveEvent(anim.getCurrentX(), anim.getCurrentY()));
				
				// are we doing pass-over events?
				String pMsg = anim.getPassOverMessage();
				if (pMsg != null && pMsg.length() > 0) {
					// try to dispatch pass-over message to objects we are over
					RectF mvRect = new RectF(mv.getX(), mv.getY(), 
							         mv.getX()+mv.getW(), mv.getY()+mv.getH());
					boolean disp = 
						dispatchPositionally(mvRect, new MessageEvent(pMsg));
					
					// if somebody took that event we end the animation here
					if (disp) {
						// set time left to 0, so it gets cleaned up below
						anim.setMSecLeft(0);
						// also kill the target index so we don't send an 
						// ending event there
						anim.setCharacterToNotify(NO_CHARACTER);
					}
				}
			}
		}
		
		// finish off animations that have no time left
		Iterator<AnimationRecord> animIter;
		for (animIter = _activeAnimations.iterator(); animIter.hasNext(); ) {
			AnimationRecord anim = animIter.next();
			// is this one expired?
			if (anim.getMSecLeft() <= 0) {
				// deliver ending events and remove the record
				endAnimation(anim, anim.getCurrentX(), anim.getCurrentY());
				animIter.remove();
			}
		}
	}
	
	/**
	 * Utility routine to unpack a measurement specification and apply it 
	 * in conjunction with a desired value.  The measurement spec can basically
	 * apply an override of the local object (ask for an "exact" value it 
	 * specifies), impose a maximum value, or say that this object can do 
	 * whatever it wants.  
	 * @param forMeasureSpec  encoded measurement instructions 
	 *                        (optional-value and flag) as imposed by parent
	 * @param desiredValue    the value the local object want if it's not
	 *                        modified by instructions for the parent
	 * @return the size value we get from applying the measurement spec to 
	 *         the desired value.
	 */
	protected int doSimpleMeasure(int forMeasureSpec, int desiredValue) {
		int result = desiredValue;  
		int specMode = MeasureSpec.getMode(forMeasureSpec);
		int specSize = MeasureSpec.getSize(forMeasureSpec);
		
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST){
			result = Math.min(result, specSize);
		} 
		return result;
	}
	
	/**
	 * Draw the current appearance of the game on the given canvas.
	 * (Students write this method)
	 * @param canv the Canvas object that drawing occurs on.
	 */
	@Override
	protected abstract void onDraw(Canvas canv); 
	
	/**
	 * This method is called when a game action button is pressed.  It should
	 * dispatch a corresponding event.  (Students write this method)
	 * @param buttonNum the index of the game action button that was pressed.
	 */
	protected abstract void buttonHit(int buttonNum); 
	
	/**
	 * Handles touch events to this view
	 * @param evt the motionEvent that has occured
	 * @return whether or not the event has been consumed
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		return false;
	}
	
	protected TickHandler _tickHandler = null;
	
	/**
	 * Sets up the buttons from the button array
	 * @return a layout with all of the buttons in it
	 */
	protected LinearLayout setupButtons(){
		LinearLayout ll = new LinearLayout(getContext());
		ll.setBackgroundColor(Color.BLACK);
		
		for(Button b : _buttons){
			ll.addView(b);
			b.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						buttonHit(getButtonIDFromName(((Button)v).getText().toString()));
					}
					return true;
				}
			});
		}
		
		ll.setOrientation(LinearLayout.HORIZONTAL);
		
		return ll;
		
	}
	
	/**
	 * Handles final UI setup
	 * @return the view that contains buttons and the gameboard
	 */
	public View finalInit() {
		setBackgroundColor(Color.WHITE);
		
		RelativeLayout rl = new RelativeLayout(getContext());
		rl.setLayoutParams(new RelativeLayout.LayoutParams(
		        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		
		View buttons = setupButtons();
		
		RelativeLayout.LayoutParams buttonparams = new RelativeLayout.LayoutParams(
		        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		buttonparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		RelativeLayout.LayoutParams viewparams = new RelativeLayout.LayoutParams(
		        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		viewparams.addRule(RelativeLayout.ABOVE, buttons.getId());
		viewparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		//viewparams.se

		rl.addView(this, viewparams);
		rl.addView(buttons, buttonparams);
		
		
		return rl;
	}
	
	/**
	 * Constructor for the GameEngine. All Subclasses must call through to this
	 * @param xmlFileID the R.raw.*** id of the xml file that describes this game
	 * @param context
	 */
	public GameEnginePreBase(int xmlFileID, Context context) {
		super(context);
		_characters = FSMxmlTranslator.parseXML(xmlFileID, this, context);

		_tickHandler = new TickHandler(this);
	}

}
