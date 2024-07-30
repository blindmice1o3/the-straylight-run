package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

public abstract class Creature extends Entity {
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

    public void move() {
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        x += xMove;
                        ///////////
                    }
                }
                break;
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        x += xMove;
                        ///////////
                    }
                }
                break;
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        y += yMove;
                        ///////////
                    }
                }
                break;
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        y += yMove;
                        ///////////
                    }
                }
                break;
            case CENTER:
                // NO MOVEMENT
                break;
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        x += xMove;
                        y += yMove;
                        ///////////
                    }
                }
                break;
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        x += xMove;
                        y += yMove;
                        ///////////
                    }
                }
                break;
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        x += xMove;
                        y += yMove;
                        ///////////
                    }
                }
                break;
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
                            return; // Entity.respondToTransferPointCollision(String key) was triggered.
                        }

                        ///////////
                        x += xMove;
                        y += yMove;
                        ///////////
                    }
                }
                break;
        }
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