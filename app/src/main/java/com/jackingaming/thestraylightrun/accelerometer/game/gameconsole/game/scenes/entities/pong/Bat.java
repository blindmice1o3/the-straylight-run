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

public class Bat extends Creature {

    private Random random;
    private Ball ball;

    public Bat(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
        width = (Tile.WIDTH / 2);
        height = (2 * Tile.HEIGHT);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        image = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.pc_yoko_tileset);

        // Set initial position to right of screen (vertically centered).
        int widthScene = game.getSceneManager().getCurrentScene().getTileManager().getWidthScene();
        int heightScene = game.getSceneManager().getCurrentScene().getTileManager().getHeightScene();
        x = widthScene - width - Tile.WIDTH;
        y = (heightScene / 2) - (height / 2);

        // Opponent's simulated intelligence (randomly start moving up or down).
        random = new Random();
        yMove = random.nextInt(2) * 2 - 1;

        for (Entity e : game.getSceneManager().getCurrentScene().getEntityManager().getEntities()) {
            if (e instanceof Ball) {
                ball = (Ball) e;
                return;
            }
        }
    }

    private void determineDirection() {
        Rect screenRectBat = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));
        Rect screenRectBall = GameCamera.getInstance().convertInGameRectToScreenRect(ball.getCollisionBounds(0, 0));

        //SIMULATED INTELLIGENCE
        int decision = random.nextInt(20);
        //OPPONENT'S POSSIBLE MOVES (no movement, random movement, towards ball movement)
        if (decision == 0) {
            //Stop movement: 1 out of every 20 times.
            yMove = 0;
            direction = Direction.CENTER;
        } else if (decision == 1) {
            //Randomly set yMove to either 1 or -1: 1 out of every 20 times.
            yMove = (random.nextInt(2) * 2 - 1) * moveSpeed;
            direction = (yMove < 0) ? Direction.UP : Direction.DOWN;
        } else if (decision < 6) {
            //Move towards the ball: 4 out of every 20 times.
            //if vertical center of ball is less than vertical center of opponent, move opponent upward.
            if (screenRectBall.centerY() < screenRectBat.centerY()) {
                yMove = -moveSpeed;
                direction = Direction.UP;
            }
            //the ball is lower on the screen than the bat, move opponent downward.
            else {
                yMove = moveSpeed;
                direction = Direction.DOWN;
            }
        }

        //PREVENT MOVING OFF-SCREEN (vertically)
        if (screenRectBat.top <= 0) {
            yMove = moveSpeed;
            direction = Direction.DOWN;
        } else if (screenRectBat.bottom >= game.getHeightViewport()) {
            yMove = -moveSpeed;
            direction = Direction.UP;
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