package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

import java.util.Map;

public class Player extends Entity {
    public interface PartyMovementListener {
        void onPartyLeaderMove(Direction direction);
    }

    private PartyMovementListener partyMovementListener;

    public void setPartyMovementListener(PartyMovementListener partyMovementListener) {
        this.partyMovementListener = partyMovementListener;
    }

    public Player(Map<Direction, AnimationDrawable> animationsByDirection) {
        super(animationsByDirection, null, null);

        startAnimations();
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update(Handler handler) {

    }

    public void updateViaSensorEvent(Handler handler, float xDelta, float yDelta) {
        if (animatorMovement.isRunning()) {
            return;
        }
        // DIRECTION
        if (Math.abs(yDelta) >= Math.abs(xDelta)) {
            // more vertical than horizontal (or equal)
            direction = (yDelta < 0) ? Direction.UP : Direction.DOWN;
        } else {
            // more horizontal than vertical
            direction = (xDelta < 0) ? Direction.LEFT : Direction.RIGHT;
        }

        doMoveBasedOnDirection(handler);
    }

    @Override
    protected void moveAfterValidation(Handler handler, String propertyName, float valueStart, float valueEnd) {
        if (partyMovementListener != null) {
            partyMovementListener.onPartyLeaderMove(direction);
        }
        super.moveAfterValidation(handler, propertyName, valueStart, valueEnd);
    }
}
