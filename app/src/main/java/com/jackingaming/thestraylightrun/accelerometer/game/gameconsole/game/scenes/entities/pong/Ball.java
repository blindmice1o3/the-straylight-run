package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.pong;

import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.Random;

public class Ball extends Creature {
    public Ball(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
        width = (Tile.WIDTH / 2);
        height = (Tile.HEIGHT / 2);
        moveSpeed = 0.5f;
    }

    @Override
    public void init(Game game) {
        super.init(game);

        image = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.corgi_crusade_editted);

        // Set initial position to center of screen.
        int widthScene = game.getSceneManager().getCurrentScene().getTileManager().getWidthScene();
        int heightScene = game.getSceneManager().getCurrentScene().getTileManager().getHeightScene();
        x = (widthScene / 2) - (width / 2);
        y = (heightScene / 2) - (height / 2);

        // Randomly choose 1 or -1 (randomly have the ball start moving right or left).
        Random random = new Random();
        xMove = random.nextInt(2) * 2 - 1;
        yMove = random.nextInt(2) * 2 - 1;
    }

    private void determineDirection() {

        Rect screenRect = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));
        //PREVENT MOVING OFF-SCREEN (horizontally)
        if (screenRect.left <= 0) {
            xMove = moveSpeed;
        } else if (screenRect.right >= game.getWidthViewport()) {
            xMove = -moveSpeed;
        }
        //PREVENT MOVING OFF-SCREEN (vertically)
        if (screenRect.top <= 0) {
            yMove = moveSpeed;
        } else if (screenRect.bottom >= game.getHeightViewport()) {
            yMove = -moveSpeed;
        }

        if (xMove == 0 && yMove < 0) {
            direction = Direction.UP;
        } else if (xMove == 0 && yMove > 0) {
            direction = Direction.DOWN;
        } else if (xMove < 0 && yMove == 0) {
            direction = Direction.LEFT;
        } else if (xMove > 0 && yMove == 0) {
            direction = Direction.RIGHT;
        } else if (xMove < 0 && yMove < 0) {
            direction = Direction.UP_LEFT;
        } else if (xMove < 0 && yMove > 0) {
            direction = Direction.DOWN_LEFT;
        } else if (xMove > 0 && yMove < 0) {
            direction = Direction.UP_RIGHT;
        } else if (xMove > 0 && yMove > 0) {
            direction = Direction.DOWN_RIGHT;
        }
    }

    @Override
    public void update(long elapsed) {
        // Determine values of [offset-of-next-step]... potential movement
        determineDirection();

        // MOVEMENT (check tile, item, entity, and transfer point collisions... actual movement)
        move();
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        float xEntityCenter = e.getX() + (e.getWidth() / 2);
        float yEntityCenter = e.getY() + (e.getHeight() / 2);

        float xBallCenter = x + (width / 2);
        float yBallCenter = y + (height / 2);

        // Reverse direction.
        if (xBallCenter > xEntityCenter) {
            xMove = moveSpeed;
        } else if (xBallCenter < xEntityCenter) {
            xMove = -moveSpeed;
        }
        if (yBallCenter > yEntityCenter) {
            yMove = moveSpeed;
        } else if (yBallCenter < yEntityCenter) {
            yMove = -moveSpeed;
        }

        return true;
    }

    @Override
    public boolean respondToItemCollisionViaClick(Item item) {
        return false;
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }
}