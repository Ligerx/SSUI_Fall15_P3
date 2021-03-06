package ssuimobile.gameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import ssuimobile.gameengine.event.ButtonPressedEvent;
import ssuimobile.gameengine.event.DragEndEvent;
import ssuimobile.gameengine.event.DragMoveEvent;
import ssuimobile.gameengine.event.FSMEvent;
import ssuimobile.gameengine.event.TouchMoveEvent;
import ssuimobile.gameengine.event.TouchPressEvent;
import ssuimobile.gameengine.event.TouchReleaseEvent;
import ssuimobile.gameengine.event.XYEvent;

public class GameEngineBase extends GameEnginePreBase {

	public GameEngineBase(int xmlFileID, Context ctx){
		super(xmlFileID, ctx);
		sendInitMessages();
	}
	
	@Override
	protected List<GameCharacter> charactersUnder(RectF area) {
		ArrayList<GameCharacter> charsUnder = new ArrayList<GameCharacter>();

		// iterate through characters backwards (reverse draw order)
		for(int i = _characters.length - 1; i >= 0; i--) {
			GameCharacter character = _characters[i];

			if(isOverlapping(area, character)) {
				charsUnder.add(character);
			}
		}

		Log.d("GameEngineBase", "charactersUnder # of characters: " + charsUnder.size());

		return charsUnder;
	}

	/**
	 * Use RectF's built in intersects() method to test overlap
	 *
	 * @param area overlapping area to test
	 * @param c character
	 * @return boolean that says if it is or isn't overlapping
	 */
	private boolean isOverlapping(RectF area, GameCharacter c) {
		float right = c.getX() + c.getW();
		float bottom = c.getY() + c.getH();

		boolean overlapping = area.intersects(c.getX(), c.getY(), right, bottom);

		Log.d("GameEngineBaes", "----- isOverlapping -----");
		Log.d("GameEngineBaes", "character# " + c.getCharacterIndex());
		Log.d("GameEngineBase", "isOverlapping: " + overlapping);
		Log.d("GameEngineBase", "char x " + c.getX() + " - " + right + ", y " + c.getY() + " - " + bottom);
		Log.d("GameEngineBase", "area x " + area.left + " - " + area.right + ", y " + area.top + " - " + area.bottom);

		return overlapping;
	}

	/**
	 * This method dispatches to a single point
	 */
	@Override
	protected boolean dispatchPositionally(XYEvent evt) {
		// this charactersUnder method finds all chars at a certain point
		List<GameCharacter> characters = charactersUnder(new PointF(evt.getX(), evt.getY()));

		return dispatchPositionallyBase(characters, evt);
	}

	/**
	 * This method dispatches to any character overlapping a given rectangle
	 */
	@Override
	protected boolean dispatchPositionally(RectF inArea, FSMEvent evt) {
		// this charactersUnder method finds all chars overlapping the rect
		List<GameCharacter> characters = charactersUnder(inArea);

		return dispatchPositionallyBase(characters, evt);
	}

	/**
	 * Common code between the two dispatchPositionally methods
	 */
	private boolean dispatchPositionallyBase(List<GameCharacter> characters, FSMEvent event) {
		boolean eventConsumed = false; // if event was consumed by any character

		for(GameCharacter character : characters) {
			if(character.deliverEvent(event)) {
				Log.d("GameEngineBase", "dispatchPositionallyBase event was consumed by char# " + character.getCharacterIndex());

				eventConsumed = true; // mark true
				break; // stop trying to dispatch if consumed
			}
		}

		return eventConsumed;
	}
	
	@Override
	protected boolean dispatchDirect(int toChar, FSMEvent evt) {
		Log.d("GameEngineBase", "dispatchDirect to character # " + toChar);

		GameCharacter character = getCharacterAt(toChar);

		// This check is needed in case an animation gets ended early, setting character # to -1
		if(character == null) {
			Log.d("GameEngineBase", "dispatchDirect character was null, end dispatch. Possibly end of animation?");
			return false;
		}

		return character.deliverEvent(evt);
	}
	
	@Override
	protected boolean dispatchToAll(FSMEvent evt) {
		return dispatchAllBase(evt, false);
	}
	
	@Override
	protected boolean dispatchTryAll(FSMEvent evt) {
		return dispatchAllBase(evt, true);
	}

	/**
	 * Common code between dispatchToAll and dispatchTryAll
	 */
	private boolean dispatchAllBase(FSMEvent event, boolean stopWhenConsumed) {
		boolean consumed = false;

		for(GameCharacter character : _characters) {
			// Only stop dispatch if stopWhenConsumed is true
			if(character.deliverEvent(event)) {
				Log.d("GameEngineBase", "dispatchAllBase event was consumed by char # " + character.getCharacterIndex());
				consumed = true;
				if(stopWhenConsumed) break; //stop
			}
		}

		return consumed;
	}
	
	
	@Override 
	protected boolean dispatchDragFocus(FSMEvent evt) {
		Log.d("GameEngineBase", "in dispatchDragFocus");

		GameCharacter character = getCharacterAt(_dragFocus);
		if(character == null) return false;

		Log.d("GameEngineBase", "dispatchDragFocus delivering to char # " + character.getCharacterIndex());

		// just adjusted event in case event has an x,y position that needs to be moved
		FSMEvent adjustedEvent = adjustEventPosition(evt);
		return character.deliverEvent(adjustedEvent);
	}

	/**
	 * If the event is a subclass of XYEvent (has an x,y position),
	 * adjust the position to be the top left corner of the character instead
	 * of where the character is being dragged.
	 */
	private FSMEvent adjustEventPosition(FSMEvent event) {
		if(event instanceof XYEvent) {
			// COPY THE ORIGINAL EVENT and then cast event into XYEvent
			XYEvent xyEvent = (XYEvent) (event.copy());
			xyEvent.offset(-_grabPointX, -_grabPointY); // adjust to top left

			return xyEvent; // auto casted back into an FSMEvent
		}

		return event; // If it's not an XYEvent, just return the original object
	}

	@Override
	protected void onDraw(Canvas canv) {
		for(GameCharacter character : _characters) {
			character.draw(canv);
		}
	}

	@Override
	protected void buttonHit(int buttonNum) {
		Log.d("buttonHit", "in buttonHit. buttonNum is: " + buttonNum);

		ButtonPressedEvent event = new ButtonPressedEvent(buttonNum);

//		dispatchToAll(event);
		dispatchTryAll(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		// Skeleton code provided below. Your job is to
		// implement the empty code blocks in the if/else
		// tree. Hint: you need to dispatch the correct
		// event using one of the dispatch methods you
		// implemented above, and by instantiating the
		// appropriate FSMEvent (e.g., TouchReleaseEvent).
		
		float x = evt.getX();
		float y = evt.getY();

		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d("onTouchEvent press", "TouchPress event at (" + x + ", " + y + ")");
			TouchPressEvent press = new TouchPressEvent(x, y);

//			return dispatchTryAll(press);
//			return dispatchToAll(press);
			return dispatchPositionally(new TouchPressEvent(x, y));

		} else if (evt.getAction() == MotionEvent.ACTION_MOVE) {
			Log.d("onTouchEvent move", "TouchMove event at (" + x + ", " + y + ")");

			// Also handle drag move here
			if(_dragFocus != NO_CHARACTER) {
				Log.d("onTouchEvent move", "Also dragMove event");
				DragMoveEvent dragMove = new DragMoveEvent(x, y);
				dispatchDragFocus(dragMove);
			}

			TouchMoveEvent move = new TouchMoveEvent(x, y);
//			return dispatchTryAll(move);
//			return dispatchToAll(move);
			return dispatchPositionally(move);

		} else if (evt.getAction() == MotionEvent.ACTION_UP) {
			Log.d("onTouchEvent up", "TouchRelease event at (" + x + ", " + y + ")");

			// Also handle drag end here
			if(_dragFocus != NO_CHARACTER) {
				Log.d("onTouchEvent up", "Also dragEnd event");
				DragEndEvent dragEnd = new DragEndEvent(x, y);
				dispatchDragFocus(dragEnd);
			}

			TouchReleaseEvent release = new TouchReleaseEvent(x, y);
//			return dispatchTryAll(release);
			return dispatchToAll(release); // FIXME Is this right? Should it be TryAll, ToAll, or positionally?

		} else {
			// not an event we understand...
			Log.d("onTouchEvent", "no action matched...");
			return false;
		}
	}
}
