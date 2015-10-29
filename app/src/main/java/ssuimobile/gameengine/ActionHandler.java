package ssuimobile.gameengine;

import android.graphics.Bitmap;
import android.util.Log;

import ssuimobile.gameengine.action.ChangeImageAction;
import ssuimobile.gameengine.action.DebugAction;
import ssuimobile.gameengine.action.FSMAction;
import ssuimobile.gameengine.action.FSMActionType;
import ssuimobile.gameengine.action.MoveIncAction;
import ssuimobile.gameengine.action.MoveToAction;
import ssuimobile.gameengine.action.RunAnimAction;
import ssuimobile.gameengine.action.SendMessageAction;
import ssuimobile.gameengine.event.FSMEvent;
import ssuimobile.gameengine.event.XYEvent;

public class ActionHandler {

    public void handleAction(FSMAction action, GameCharacter character, FSMEvent event) {
        Log.d("ssui handleAction", "action type num is: " + action.getType());
        Log.d("ssui handleAction", "action type name is: " + FSMActionType.nameFromIndex(action.getType()));


        if (action.getType() == FSMActionType.CHANGE_IMAGE) {
            changeImage(character, ((ChangeImageAction) action).getImage());
        }
        else if(action.getType() == FSMActionType.MOVE_TO) {
            moveTo((MoveToAction) action, character);
        }
        else if(action.getType() == FSMActionType.MOVE_INC) {
            moveInc((MoveIncAction) action, character);
        }
        else if(action.getType() == FSMActionType.FOLLOW_EVENT_POSITION) {
            followEventPosition(character, event);
        }
        else if(action.getType() == FSMActionType.GET_DRAG_FOCUS) {
            getDragFocus(character, event);
        }
        else if(action.getType() == FSMActionType.DROP_DRAG_FOCUS) {
            dropDragFocus(character);
        }
        else if(action.getType() == FSMActionType.SEND_MESSAGE) {
            sendMessage((SendMessageAction) action, character);
        }
        else if(action.getType() == FSMActionType.DEBUG_MESSAGE) {
            debugMessage((DebugAction) action);
        }
        else if(action.getType() == FSMActionType.RUN_ANIM) {
            runAnim((RunAnimAction) action, character);
        }
    }





    private void changeImage(GameCharacter character, Bitmap image) {
        character.setImage(image);
        redraw(character);
    }

    private void moveTo(MoveToAction action, GameCharacter character) {
        Log.d("ssui action moveTo", "character original coordinates are ("
                + character.getX() + ", " + character.getY() + ")");

        character.setX(action.getX());
        character.setY(action.getY());

        redraw(character);

        Log.d("ssui action moveTo", "character new coordinates are ("
                + character.getX() + ", " + character.getY() + ")");
    }

    private void moveInc(MoveIncAction action, GameCharacter character) {
        Log.d("ssui action moveInc", "character original coordinates are ("
                + character.getX() + ", " + character.getY() + ")");

        Log.d("ssui action moveInc", "incrementing by: ("
                + action.getX() + ", " + action.getY() + ")");

        character.setX(character.getX() + action.getX());
        character.setY(character.getY() + action.getY());
        redraw(character);

        Log.d("ssui action moveInc", "character new coordinates are ("
                + character.getX() + ", " + character.getY() + ")");
    }

    private void followEventPosition(GameCharacter character, FSMEvent event) {
        // I'm under the assumption that this particular event MUST be a subclass of XYEvent
        XYEvent xy = (XYEvent) event;
        character.setX(xy.getX());
        character.setY(xy.getY());
        redraw(character);
    }

    private void getDragFocus(GameCharacter character, FSMEvent event) {
//        character.getOwner()._dragFocus = character.getCharacterIndex();
//        int charIndex = character.getCharacterIndex();

        // Assuming that the event is a XYEvent if we're talking about drag
        // In GameEngineBase dispatchDragFocus, I already
        // offset the grabPoint before delivering the event.
//        XYEvent xy = (XYEvent) event;

//        character.getOwner().requestDragFocus(charIndex, xy.getX(), xy.getY());
//        character.getOwner().requestDragFocus(charIndex, 0, 0);


        // FIXME
        // I THINK what I need to do now is:
        // Find coordinates of the click, position of the (0,0) of the character
        // Then calculate the drag offset, and request drag focus

        XYEvent xy = (XYEvent) event;

        int charIndex = character.getCharacterIndex();

        float xOffset = xy.getX() - character.getX();
        float yOffset = xy.getY() - character.getY();

        character.getOwner().requestDragFocus(charIndex, xOffset, yOffset);

        Log.d("ssui action getDrag", "getDragFocus character position: (" + character.getX() + ", " + character.getY() + ")");
        Log.d("ssui action getDrag", "getDragFocus event position: (" + xy.getX() + ", " + xy.getY() + ")");
        Log.d("ssui action getDrag", "getDragFocus xOffset: " + xOffset + ", yOffset: " + yOffset);
        Log.d("ssui action getDrag", "getDragFocus char # is " + charIndex + ", drag focus is now "+ character.getOwner()._dragFocus);
    }

    private void dropDragFocus(GameCharacter character) {
        Log.d("ssui dropDragFocus", "char # " + character.getCharacterIndex() + " is no longer the drag focus");
        character.getOwner().releaseDragFocus();
    }

    private void sendMessage(SendMessageAction action, GameCharacter character) {
        character.getOwner().sendMessage(character.getCharacterIndex(), action.getMessage());
    }

    private void debugMessage(DebugAction action) {
        Log.d("ssui", action.getMessage());
    }

    private void runAnim(RunAnimAction action, GameCharacter character) {
        character.getOwner().startNewAnimation(action);
    }

    /**
     * Convenience method to force the canvas to redraw
     */
    private void redraw(GameCharacter character) {
        Log.d("ssui char/board redraw", "damaging character, redrawing board");
        character.getOwner().damageCharacter(character.getCharacterIndex());
    }
}
