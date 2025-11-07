package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.AimlessWalkerAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.Random;

public class AimlessWalker extends Creature {
    public static final String TAG = AimlessWalker.class.getSimpleName();
    private static final long DEFAULT_MOVEMENT_DURATION = 1000L;
    private static final long RUNNING_MOVEMENT_DURATION = 500L;

    public enum Type {CHICK, CHICKEN, SHEEP, COW;}

    public enum State {OFF, WALK, RUN;}

    private AimlessWalkerAnimationManager aimlessWalkerAnimationManager;
    transient private ObjectAnimator movementAnimator;

    private Type type;
    private State state;
    private Random random;
    private int ageInDays;

    public AimlessWalker(Type type, int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        direction = Direction.DOWN;
        ///////////////////////////////////////////////////////////////////////////
        aimlessWalkerAnimationManager = new AimlessWalkerAnimationManager(type);
        ///////////////////////////////////////////////////////////////////////////

        this.type = type;
        state = State.OFF;
        random = new Random();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        aimlessWalkerAnimationManager.init(game);

        movementAnimator =
                ObjectAnimator.ofFloat(this, "x", x - Tile.WIDTH);
        movementAnimator.setDuration(RUNNING_MOVEMENT_DURATION);
        movementAnimator.setInterpolator(new LinearInterpolator());
        movementAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                if (carryable != null) {
                    // CARRYABLE
                    if (carryable != null) {
                        moveCarryable();
                    }
                }
            }
        });
    }

    @Override
    public void update(long elapsed) {
        aimlessWalkerAnimationManager.update(elapsed);

        switch (state) {
            case OFF:
                break;
            case WALK:
            case RUN:
                if (!movementAnimator.isRunning()) {
                    determineNextMove();
                    move();
                }
                break;
        }

        determineNextImage();
    }

    private String propertyName = null;
    private float valueStart = 0;
    private float valueEnd = 0;

    public void prepareMoveDown() {
        yMove = (1 * Tile.HEIGHT);
        propertyName = "y";
        valueStart = y;
        valueEnd = y + yMove;
    }

    public void prepareMoveLeft() {
        xMove = -(1 * Tile.WIDTH);
        propertyName = "x";
        valueStart = x;
        valueEnd = x + xMove;
    }

    public void prepareMoveUp() {
        yMove = -(1 * Tile.HEIGHT);
        propertyName = "y";
        valueStart = y;
        valueEnd = y + yMove;
    }

    public void prepareMoveRight() {
        xMove = (1 * Tile.WIDTH);
        propertyName = "x";
        valueStart = x;
        valueEnd = x + xMove;
    }

    @Override
    public boolean performMove() {
        boolean collision = false;
        if (propertyName.equals("x")) {
            collision = checkEntityCollision(xMove, 0);
        } else if (propertyName.equals("y")) {
            collision = checkEntityCollision(0, yMove);
        }

        if (!collision) {
            movementAnimator.setPropertyName(propertyName);
            movementAnimator.setFloatValues(valueStart, valueEnd);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    movementAnimator.start();
                }
            });

            return true;
        }

        return false;
    }

    private void determineNextMove() {
        switch (state) {
            case OFF:
                direction = Direction.DOWN;
                break;
            case WALK:
            case RUN:
                // change direction 10% of time.
                if (random.nextInt(10) == 1) {
                    // randomly determine direction.
                    int moveDir = random.nextInt(4);

                    // DOWN
                    if (moveDir == 0) {
                        direction = Direction.DOWN;
                        prepareMoveDown();
                    }
                    // LEFT
                    else if (moveDir == 1) {
                        direction = Direction.LEFT;
                        prepareMoveLeft();
                    }
                    // UP
                    else if (moveDir == 2) {
                        direction = Direction.UP;
                        prepareMoveUp();
                    }
                    // RIGHT
                    else if (moveDir == 3) {
                        direction = Direction.RIGHT;
                        prepareMoveRight();
                    } else {
                        Log.e(TAG, "determineNextMove() else-clause moveDir.");
                    }
                }
                // do NOT change direction 90% of time.
                else {
                    switch (direction) {
                        case UP:
                            prepareMoveUp();
                            break;
                        case DOWN:
                            prepareMoveDown();
                            break;
                        case LEFT:
                            prepareMoveLeft();
                            break;
                        case RIGHT:
                            prepareMoveRight();
                            break;
                        default:
                            break;
                    }
                }
                break;
        }
    }

    private void determineNextImage() {
        image = aimlessWalkerAnimationManager.getCurrentFrame(direction, state, xMove, yMove);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        if (e instanceof Plant || e instanceof CollidingOrbit) {
            return false;
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

    @Override
    public boolean isCarryable() {
        return true;
    }

    public void changeToOff() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                movementAnimator.cancel();
            }
        });
        state = State.OFF;
    }

    public void changeToWalk() {
        movementAnimator.setDuration(DEFAULT_MOVEMENT_DURATION);
        state = State.WALK;
    }

    public void changeToRun() {
        movementAnimator.setDuration(RUNNING_MOVEMENT_DURATION);
        state = State.RUN;
    }

    public State getState() {
        return state;
    }

    ////////////////////////////////////////////////////////////////////////////////

//    private static final int DAYS_UNHAPPY_DUE_TO_MISSED_FEEDING = 3;
//    private static final int DAYS_ALIVE_THRESHOLD_FOR_ADULT_STAGE = 4;
//
//    public enum Stage {BABY, ADULT;}
//
//    private float widthPixelToViewportRatio;
//    private float heightPixelToViewportRatio;
//
//    private Stage stage;
//    private int daysAlive;
//    private int daysUnhappy;
//
//    transient private Map<Direction, Animation> animationWalkBaby;
//    transient private Map<Direction, Animation> animationWalkAdult;
//
//    public Chicken(GameCartridge gameCartridge, float xCurrent, float yCurrent, Stage stage) {
//        super(gameCartridge, xCurrent, yCurrent);
//
//        this.stage = stage;
//        daysAlive = 0;
//        daysUnhappy = 0;
//
//        init(gameCartridge);
//    }
//
//    @Override
//    public void init(GameCartridge gameCartridge) {
//        Log.d(MainActivity.DEBUG_TAG, "Chicken.init(GameCartridge)");
//        this.gameCartridge = gameCartridge;
//
//        GameCamera gameCamera = gameCartridge.getGameCamera();
//        widthPixelToViewportRatio = ((float) gameCartridge.getWidthViewport()) /
//                gameCamera.getWidthClipInPixel();
//        heightPixelToViewportRatio = ((float) gameCartridge.getHeightViewport()) /
//                gameCamera.getHeightClipInPixel();
//
//        initImage(gameCartridge.getContext().getResources());
//        initBounds();
//    }
//
//    private void initImage(Resources resources) {
//
//    }
//
//    @Override
//    public void initBounds() {
//        Log.d(MainActivity.DEBUG_TAG, "Chicken.initBounds()");
//        bounds = new Rect(0, 0, width, height);
//    }
//
//    @Override
//    public void updatePosition(float xCurrent, float yCurrent) {
//        this.xCurrent = xCurrent;
//        this.yCurrent = yCurrent;
//    }
//
//    @Override
//    public boolean drop(Tile tile) {
//        if ((tile instanceof GrowableGroundTile) || (tile instanceof GenericWalkableTile)) {
//            int xCurrentTile = tile.getxIndex() * TileMap.TILE_WIDTH;
//            int yCurrentTile = tile.getyIndex() * TileMap.TILE_HEIGHT;
//
//            ////////////////////////
//            xCurrent = xCurrentTile;
//            yCurrent = yCurrentTile;
//            ////////////////////////
//
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public void update(long elapsed) {
//        xMove = 0f;
//        yMove = 0f;
//
//        for (Animation anim : animationWalkBaby.values()) {
//            anim.update(elapsed);
//        }
//        for (Animation anim : animationWalkAdult.values()) {
//            anim.update(elapsed);
//        }
//
//        decideWalkRandomDirection();
//    }
//
//    private void decideWalkRandomDirection() {
//        Random random = new Random();
//
//        if (random.nextInt(100) == 1) {
//            int moveDir = random.nextInt(5);
//
//            switch (moveDir) {
//                case 0:
//                    move(Direction.LEFT);
//                    break;
//                case 1:
//                    move(Direction.RIGHT);
//                    break;
//                case 2:
//                    move(Direction.UP);
//                    break;
//                case 3:
//                    move(Direction.DOWN);
//                    break;
//                default:
//                    xMove = 0;
//                    yMove = 0;
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void render(Canvas canvas) {
//        Bitmap currentFrame = determineCurrentAnimationFrame();
//        GameCamera gameCamera = gameCartridge.getGameCamera();
//
//        Rect rectOfImage = new Rect(0, 0, currentFrame.getWidth(), currentFrame.getHeight());
//        Rect rectOnScreen = new Rect(
//                (int) ((xCurrent - gameCamera.getX()) * widthPixelToViewportRatio),
//                (int) ((yCurrent - gameCamera.getY()) * heightPixelToViewportRatio),
//                (int) (((xCurrent - gameCamera.getX()) + width) * widthPixelToViewportRatio),
//                (int) (((yCurrent - gameCamera.getY()) + height) * heightPixelToViewportRatio));
//
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        canvas.drawBitmap(currentFrame, rectOfImage, rectOnScreen, null);
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    }
//
//    private Bitmap determineCurrentAnimationFrame() {
//        Bitmap currentFrame = null;
//
//        if (stage == Stage.BABY) {
//            switch (direction) {
//                case UP:
//                    currentFrame = animationWalkBaby.get(Direction.UP).getCurrentFrame();
//                    break;
//                case DOWN:
//                    currentFrame = animationWalkBaby.get(Direction.DOWN).getCurrentFrame();
//                    break;
//                case LEFT:
//                    currentFrame = animationWalkBaby.get(Direction.LEFT).getCurrentFrame();
//                    break;
//                case RIGHT:
//                    currentFrame = animationWalkBaby.get(Direction.RIGHT).getCurrentFrame();
//                    break;
//            }
//
//        } else if (stage == Stage.ADULT) {
//            switch (direction) {
//                case UP:
//                    currentFrame = animationWalkAdult.get(Direction.UP).getCurrentFrame();
//                    break;
//                case DOWN:
//                    currentFrame = animationWalkAdult.get(Direction.DOWN).getCurrentFrame();
//                    break;
//                case LEFT:
//                    currentFrame = animationWalkAdult.get(Direction.LEFT).getCurrentFrame();
//                    break;
//                case RIGHT:
//                    currentFrame = animationWalkAdult.get(Direction.RIGHT).getCurrentFrame();
//                    break;
//            }
//        }
//
//        return currentFrame;
//    }
//
//    public void incrementDaysAlive() {
//        daysAlive++;
//
//        if ((stage == Stage.BABY) && (daysAlive >= DAYS_ALIVE_THRESHOLD_FOR_ADULT_STAGE)) {
//            stage = Stage.ADULT;
//        }
//    }
//
//    public Stage getStage() {
//        return stage;
//    }
//
//    public void becomeUnhappyDueToMissedFeeding() {
//        daysUnhappy = DAYS_UNHAPPY_DUE_TO_MISSED_FEEDING;
//    }
//
//    public void decrementDaysUnhappy() {
//        daysUnhappy--;
//
//        if (daysUnhappy < 0) {
//            daysUnhappy = 0;
//        }
//    }
//
//    public int getDaysUnhappy() {
//        return daysUnhappy;
//    }
//
//    public int getDaysAlive() {
//        return daysAlive;
//    }
//
//    public void layEgg() {
//        SceneChickenCoop sceneChickenCoop = (SceneChickenCoop) gameCartridge.getSceneManager().getScene(Scene.Id.CHICKEN_COOP);
//        TileMap tileMap = sceneChickenCoop.getTileMap();
//
//        boolean hasLaidEgg = false;
//        while (!hasLaidEgg) {
//            //find RANDOM, walkable, unoccupied tile-space to instantiate egg.
//            Random random = new Random();
//            int numberOfColumns = tileMap.getWidthSceneMax() / tileMap.getTileWidth();
//            int numberOfRows = tileMap.getHeightSceneMax() / tileMap.getTileHeight();
//            int xIndexRandom = random.nextInt(numberOfColumns);
//            int yIndexRandom = random.nextInt(numberOfRows);
//            int xPositionRandom = xIndexRandom * tileMap.getTileWidth();
//            int yPositionRandom = yIndexRandom * tileMap.getTileHeight();
//            //EggEntity's bounding rectangle.
//            Rect collisionBoundsRandom = new Rect(xPositionRandom, yPositionRandom,
//                    xPositionRandom + tileMap.getTileWidth(), yPositionRandom + tileMap.getTileHeight());
//
//            //TILES (check for zero-tile-collision).
//            boolean isWalkable = (tileMap.getTile(xIndexRandom, yIndexRandom).getWalkability() == Tile.Walkability.WALKABLE);
//            if (isWalkable) {
//                boolean isUnoccupied = true;
//                for (Entity e : sceneChickenCoop.getEntityManager().getEntities()) {
//                    if (e.getCollisionBounds(0f, 0f).intersect(collisionBoundsRandom)) {
//                        isUnoccupied = false;
//                        break;
//                    }
//                }
//                //ENTITIES (check for zero-entity-collision).
//                if (isUnoccupied) {
//                    //LAY EGG.
//                    EggEntity eggEntity = new EggEntity(gameCartridge, xPositionRandom, yPositionRandom);
//                    sceneChickenCoop.getEntityManager().addEntity(eggEntity);
//                    hasLaidEgg = true;
//                }
//            }
//        }
//    }


    public Type getType() {
        return type;
    }
}
