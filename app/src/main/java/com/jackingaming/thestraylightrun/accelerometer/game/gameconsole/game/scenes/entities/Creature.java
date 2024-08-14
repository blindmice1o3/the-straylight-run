package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

public abstract class Creature extends Entity {
    public static final String TAG = Creature.class.getSimpleName();

    public static final float MOVE_SPEED_DEFAULT = 1f;

    public enum Direction {UP, DOWN, LEFT, RIGHT, CENTER, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT;}

    protected Direction direction;
    protected float moveSpeed;
    protected float xMove;
    protected float yMove;

    public Creature(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
        direction = Direction.CENTER;
        moveSpeed = MOVE_SPEED_DEFAULT;
        xMove = 0f;
        yMove = 0f;
    }

    public boolean performMove() {
        switch (direction) {
            case UP:
            case DOWN:
                y += yMove;
                return true;
            case LEFT:
            case RIGHT:
                x += xMove;
                return true;
            case CENTER:
                return true;
            case UP_LEFT:
            case UP_RIGHT:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                x += xMove;
                y += yMove;
                return true;
            default:
                Log.e(TAG, "performMove() switch's default.");
                return false;
        }
    }

    public boolean move() {
        TileManager tileManager = game.getSceneManager().getCurrentScene().getTileManager();
        int xFutureLeft = 0;
        int xFutureRight = 0;
        int yFutureTop = 0;
        int yFutureBottom = 0;
        switch (direction) {
            case LEFT:
                // to_move: LEFT
                xFutureLeft = (int) (x + xMove);
                xFutureRight = (int) ((x + width) + xMove - 1);
                yFutureTop = (int) (y);
                yFutureBottom = (int) (y + height - 1);

                // CHECKING tile collision: LEFT-TOP and LEFT-BOTTOM
                if (!tileManager.isSolid(xFutureLeft, yFutureTop) &&
                        !tileManager.isSolid(xFutureLeft, yFutureBottom)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(xMove, 0f) &&
                            !checkItemCollision(xMove, 0f, false)) {

                        if (checkTransferPointCollision(xMove, 0f)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            case RIGHT:
                // to_move: RIGHT
                xFutureLeft = (int) (x + xMove);
                xFutureRight = (int) ((x + width) + xMove - 1);
                yFutureTop = (int) (y);
                yFutureBottom = (int) (y + height - 1);

                // CHECKING tile collision: RIGHT-TOP and RIGHT-BOTTOM
                if (!tileManager.isSolid(xFutureRight, yFutureTop) &&
                        !tileManager.isSolid(xFutureRight, yFutureBottom)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(xMove, 0f) &&
                            !checkItemCollision(xMove, 0f, false)) {

                        if (checkTransferPointCollision(xMove, 0f)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            case UP:
                // to_move: UP
                yFutureTop = (int) (y + yMove);
                yFutureBottom = (int) ((y + height) + yMove - 1);
                xFutureLeft = (int) (x);
                xFutureRight = (int) (x + width - 1);

                // CHECKING tile collision: TOP-LEFT and TOP-RIGHT
                if (!tileManager.isSolid(xFutureLeft, yFutureTop) &&
                        !tileManager.isSolid(xFutureRight, yFutureTop)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(0f, yMove) &&
                            !checkItemCollision(0f, yMove, false)) {

                        if (checkTransferPointCollision(0f, yMove)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            case DOWN:
                // to_move: DOWN
                yFutureTop = (int) (y + yMove);
                yFutureBottom = (int) ((y + height) + yMove - 1);
                xFutureLeft = (int) (x);
                xFutureRight = (int) (x + width - 1);

                // CHECKING tile collision: BOTTOM-LEFT and BOTTOM-RIGHT
                if (!tileManager.isSolid(xFutureLeft, yFutureBottom) &&
                        !tileManager.isSolid(xFutureRight, yFutureBottom)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(0f, yMove) &&
                            !checkItemCollision(0f, yMove, false)) {

                        if (checkTransferPointCollision(0f, yMove)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            case CENTER:
                // NO MOVEMENT
                return false;
            case UP_LEFT:
                // to_move: UP_LEFT
                xFutureLeft = (int) (x + xMove);
                xFutureRight = (int) ((x + width) + xMove - 1);
                yFutureTop = (int) (y + yMove);
                yFutureBottom = (int) ((y + height) + yMove - 1);

                // CHECKING tile collision: LEFT-TOP and LEFT-BOTTOM and RIGHT-TOP
                if (!tileManager.isSolid(xFutureLeft, yFutureTop) &&
                        !tileManager.isSolid(xFutureLeft, yFutureBottom) &&
                        !tileManager.isSolid(xFutureRight, yFutureTop)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(xMove, yMove) &&
                            !checkItemCollision(xMove, yMove, false)) {

                        if (checkTransferPointCollision(xMove, yMove)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            case UP_RIGHT:
                // to_move: UP_RIGHT
                xFutureLeft = (int) (x + xMove);
                xFutureRight = (int) ((x + width) + xMove - 1);
                yFutureTop = (int) (y + yMove);
                yFutureBottom = (int) ((y + height) + yMove - 1);

                // CHECKING tile collision: RIGHT-TOP and RIGHT-BOTTOM and LEFT-TOP
                if (!tileManager.isSolid(xFutureRight, yFutureTop) &&
                        !tileManager.isSolid(xFutureRight, yFutureBottom) &&
                        !tileManager.isSolid(xFutureLeft, yFutureTop)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(xMove, yMove) &&
                            !checkItemCollision(xMove, yMove, false)) {

                        if (checkTransferPointCollision(xMove, yMove)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            case DOWN_LEFT:
                // to_move: DOWN_LEFT
                xFutureLeft = (int) (x + xMove);
                xFutureRight = (int) ((x + width) + xMove - 1);
                yFutureTop = (int) (y + yMove);
                yFutureBottom = (int) ((y + height) + yMove - 1);

                // CHECKING tile collision: LEFT-TOP and LEFT-BOTTOM and RIGHT_BOTTOM
                if (!tileManager.isSolid(xFutureLeft, yFutureTop) &&
                        !tileManager.isSolid(xFutureLeft, yFutureBottom) &&
                        !tileManager.isSolid(xFutureRight, yFutureBottom)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(xMove, yMove) &&
                            !checkItemCollision(xMove, yMove, false)) {

                        if (checkTransferPointCollision(xMove, yMove)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            case DOWN_RIGHT:
                // to_move: DOWN_RIGHT
                xFutureLeft = (int) (x + xMove);
                xFutureRight = (int) ((x + width) + xMove - 1);
                yFutureTop = (int) (y + yMove);
                yFutureBottom = (int) ((y + height) + yMove - 1);

                // CHECKING tile collision: RIGHT-TOP and RIGHT-BOTTOM and LEFT-BOTTOM
                if (!tileManager.isSolid(xFutureRight, yFutureTop) &&
                        !tileManager.isSolid(xFutureRight, yFutureBottom) &&
                        !tileManager.isSolid(xFutureLeft, yFutureBottom)) {
                    // CHECKING entity collision AND item collision
                    if (!checkEntityCollision(xMove, yMove) &&
                            !checkItemCollision(xMove, yMove, false)) {

                        if (checkTransferPointCollision(xMove, yMove)) {
                            return true; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        /////////////////////
                        return performMove();
                        /////////////////////
                    }
                }
                return false;
            default:
                Log.e(TAG, "move()'s switch's default.");
                return false;
        }
    }

    public Entity getEntityCurrentlyFacing() {
        Log.e(TAG, "getEntityCurrentlyFacing()");

        Entity tempEntityReturner = null;

        int creatureCenterX = (int) (x + (width / 2));
        int creatureCenterY = (int) (y + (height / 2));

        Rect entityCollisionBox = new Rect();
        switch (direction) {
            case DOWN:
                entityCollisionBox.left = (creatureCenterX - (Tile.WIDTH / 4));
                entityCollisionBox.top = (creatureCenterY + (Tile.HEIGHT / 2) + ((int) (0.3) * Tile.HEIGHT));
                entityCollisionBox.right = (creatureCenterX - (Tile.WIDTH / 4)) +
                        (Tile.WIDTH / 2);
                entityCollisionBox.bottom = (creatureCenterY + (Tile.HEIGHT / 2) + ((int) (0.3) * Tile.HEIGHT)) +
                        (Tile.HEIGHT / 2);
                break;
            case UP:
                entityCollisionBox.left = (creatureCenterX - (Tile.WIDTH / 4));
                entityCollisionBox.top = (creatureCenterY - ((int) (1.4) * Tile.HEIGHT));
                entityCollisionBox.right = (creatureCenterX - (Tile.WIDTH / 4)) +
                        (Tile.WIDTH / 2);
                entityCollisionBox.bottom = (creatureCenterY - ((int) (1.4) * Tile.HEIGHT)) +
                        (Tile.HEIGHT / 2);
                break;
            case LEFT:
                entityCollisionBox.left = (creatureCenterX - ((int) (1.4) * Tile.WIDTH));
                entityCollisionBox.top = (creatureCenterY - (Tile.HEIGHT / 4));
                entityCollisionBox.right = (creatureCenterX - ((int) (1.4) * Tile.WIDTH)) +
                        (Tile.WIDTH / 2);
                entityCollisionBox.bottom = (creatureCenterY - (Tile.HEIGHT / 4)) +
                        (Tile.HEIGHT / 2);
                break;
            case RIGHT:
                entityCollisionBox.left = (creatureCenterX + (Tile.WIDTH / 2) + ((int) (0.3) * Tile.WIDTH));
                entityCollisionBox.top = (creatureCenterY - (Tile.HEIGHT / 4));
                entityCollisionBox.right = (creatureCenterX + (Tile.WIDTH / 2) + ((int) (0.3) * Tile.WIDTH)) +
                        (Tile.WIDTH / 2);
                entityCollisionBox.bottom = (creatureCenterY - (Tile.HEIGHT / 4)) +
                        (Tile.HEIGHT / 2);
                break;
            default:
                break;
        }

        for (Entity e : game.getSceneManager().getCurrentScene().getEntityManager().getEntities()) {
            if (e.equals(this)) {
                continue;
            }

            if (entityCollisionBox.intersect(e.getCollisionBounds(0, 0))) {
                tempEntityReturner = e;
            }
        }

        if (tempEntityReturner != null) {
            Log.e(TAG, "entity: " + tempEntityReturner.toString());
        } else {
            Log.e(TAG, "entity is null");
        }

        return tempEntityReturner;
    }

    public Tile checkTileCurrentlyFacing() {
        Tile[][] tiles = game.getSceneManager().getCurrentScene().getTileManager().getTiles();

        float xCenter = x + (Tile.WIDTH / 2);
        float yCenter = y + (Tile.HEIGHT / 2);

        int xIndex = (int) ((xCenter) / Tile.WIDTH);
        int yIndex = (int) ((yCenter) / Tile.HEIGHT);
        switch (direction) {
            case UP:
                yIndex = (int) ((yCenter - (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case DOWN:
                yIndex = (int) ((yCenter + (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case LEFT:
                xIndex = (int) ((xCenter - (1 * Tile.WIDTH)) / Tile.WIDTH);
                break;
            case RIGHT:
                xIndex = (int) ((xCenter + (1 * Tile.WIDTH)) / Tile.WIDTH);
                break;
            case CENTER:
//                xIndex = (int) ((xCenter) / Tile.WIDTH);
//                yIndex = (int) ((yCenter) / Tile.HEIGHT);
                break;
            case UP_LEFT:
                xIndex = (int) ((xCenter - (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter - (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case UP_RIGHT:
                xIndex = (int) ((xCenter + (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter - (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case DOWN_LEFT:
                xIndex = (int) ((xCenter - (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter + (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case DOWN_RIGHT:
                xIndex = (int) ((xCenter + (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter + (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
        }

        if ((yIndex < 0) || (yIndex > (tiles.length - 1)) ||
                (xIndex < 0) || (xIndex > (tiles[0].length - 1))) {
            Tile nonWalkableTile = new Tile("x");
            nonWalkableTile.setWalkable(false);
            return nonWalkableTile;
        }

        return tiles[yIndex][xIndex];
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getxMove() {
        return xMove;
    }

    public void setxMove(float xMove) {
        this.xMove = xMove;
    }

    public float getyMove() {
        return yMove;
    }

    public void setyMove(float yMove) {
        this.yMove = yMove;
    }
}