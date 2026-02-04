package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.frogger;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.frogger.Car;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.frogger.Log;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneFrogger extends Scene {
    public static final String TAG = SceneFrogger.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 10;
    public static final int Y_SPAWN_INDEX_DEFAULT = 14;

    private static SceneFrogger uniqueInstance;

    transient private Bitmap spriteSheetFrogger, backgroundFrogger, winningRow, startingRow,
            logLarge, logMedium, logSmall, parrotRight, carPinkLeft, carWhiteRight, carYellowLeft,
            seaLionRight, bigRigLeft;
    transient private Bitmap[] frogRight, frogLeft, frogUp, frogDown, turtleLeft, crocRight, snowPlowRight,
            snakeLeft, frogNPCRight, frogNPCLeft;

    private int controllerForNextEntityInstantiation;
    private int numOfCarLanes;
    private int numOfRiverLanes;
    private int chanceToInstantiate;

    private SceneFrogger() {
        super();
        List<Entity> entitiesForFrogger = createEntitiesForFrogger();
        entityManager.loadEntities(entitiesForFrogger);
        List<Item> itemsForFrogger = createItemsForFrogger();
        itemManager.loadItems(itemsForFrogger);

        controllerForNextEntityInstantiation = 0;
        numOfCarLanes = 5;
        numOfRiverLanes = 4;
        chanceToInstantiate = 0;
    }

    public static SceneFrogger getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneFrogger();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        initFroggerSprites(game.getContext().getResources());

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForFrogger = createAndInitTilesForFrogger(game);
        tileManager.loadTiles(tilesForFrogger);
        Map<String, Rect> transferPointsForFrogger = createTransferPointsForFrogger();
        tileManager.loadTransferPoints(transferPointsForFrogger); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
    }

    @Override
    public void enter(List<Object> args) {
        super.enter(args);

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_SPAWN_INDEX_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
        }
        GameCamera.getInstance().update(0L);
    }

    @Override
    public void update(long elapsed) {
        // super will call entityManager.update(elapsed).
        super.update(elapsed);

        if (entityManager.getEntities().size() < 12) {
            chanceToInstantiate = (int) (Math.random() * 100) + 1;
            android.util.Log.d(TAG, "chanceToInstantiate: " + chanceToInstantiate);

            //ONLY INSTANTIATE 5% of the time UPDATE(long) IS CALLED.
            if (chanceToInstantiate <= 5) {
                controllerForNextEntityInstantiation = (int) (Math.random() * (numOfCarLanes + numOfRiverLanes)) + 1;
                android.util.Log.d(TAG, "controllerForNextEntityInstantiation: " + controllerForNextEntityInstantiation);
                int x = 0;
                int y = 0;
                int width = 0;
                int height = 0;

                switch (controllerForNextEntityInstantiation) {
                    case 1:
                        //Direction.RIGHT
                        x = 0;
                        y = (8 * Tile.HEIGHT) + 15;               //LANE01 (CLOSEST TO TOP)
                        width = Tile.WIDTH;
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Car.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new right Car instance.
                        Bitmap carPinkRight = Animation.flipImageHorizontally(carPinkLeft);
                        Car carPinkMovingRight = new Car(x, y, Car.Type.PINK,
                                Creature.Direction.RIGHT, carPinkRight);
                        carPinkMovingRight.init(game);
                        entityManager.addEntity(carPinkMovingRight);

                        break;
                    case 2:
                        //Direction.LEFT
                        x = 19 * Tile.WIDTH;
                        y = (9 * Tile.HEIGHT) + 15;               //LANE02
                        width = Tile.WIDTH;
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Car.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new left Car instance.
                        Car carPinkMovingLeft = new Car(x, y, Car.Type.PINK,
                                Creature.Direction.LEFT, carPinkLeft);
                        carPinkMovingLeft.init(game);
                        entityManager.addEntity(carPinkMovingLeft);

                        break;
                    case 3:
                        //Direction.RIGHT
                        x = 0;
                        y = (10 * Tile.HEIGHT) + 15;              //LANE03 (MIDDLE)
                        width = Tile.WIDTH;
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Car.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new right Car instance.
                        Car carWhiteMovingRight = new Car(x, y, Car.Type.WHITE,
                                Creature.Direction.RIGHT, carWhiteRight);
                        carWhiteMovingRight.init(game);
                        entityManager.addEntity(carWhiteMovingRight);

                        break;
                    case 4:
                        //Direction.LEFT
                        x = 19 * Tile.WIDTH;
                        y = (11 * Tile.HEIGHT) + 15;          //LANE04
                        width = Tile.WIDTH;
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Car.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new left Car instance.
                        Bitmap carWhiteLeft = Animation.flipImageHorizontally(carWhiteRight);
                        Car carWhiteMovingLeft = new Car(x, y, Car.Type.WHITE,
                                Creature.Direction.LEFT, carWhiteLeft);
                        carWhiteMovingLeft.init(game);
                        entityManager.addEntity(carWhiteMovingLeft);

                        break;
                    case 5:
                        //Direction.RIGHT
                        x = 0;
                        y = (12 * Tile.HEIGHT) + 15;                      //LANE05 (CLOSEST TO BOTTOM)
                        width = Tile.WIDTH;
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Car.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new right Car instance.
                        Bitmap carYellowRight = Animation.flipImageHorizontally(carYellowLeft);
                        Car carYellowMovingRight = new Car(x, y, Car.Type.YELLOW,
                                Creature.Direction.RIGHT, carYellowRight);
                        carYellowMovingRight.init(game);
                        entityManager.addEntity(carYellowMovingRight);

                        break;
                    case 6:
                        //Direction.RIGHT
                        x = 0;
                        y = (3 * Tile.HEIGHT) + 15;               //RIVER (CLOSEST TO TOP)
                        width = logSmall.getWidth(); //Log.Size.SMALL
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Log.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new right, small Log instance.
                        Log logSmallRight = new Log(x, y, Log.Size.SMALL,
                                Creature.Direction.RIGHT, logSmall);
                        logSmallRight.init(game);
                        entityManager.addEntity(logSmallRight);

                        break;
                    case 7:
                        //Direction.LEFT
                        x = (19 * Tile.WIDTH);
                        y = (4 * Tile.HEIGHT) + 15;
                        width = logMedium.getWidth(); //Log.Size.MEDIUM
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Log.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new left, medium Log instance.
                        Log logMediumLeft = new Log(x, y, Log.Size.MEDIUM,
                                Creature.Direction.LEFT, logMedium);
                        logMediumLeft.init(game);
                        entityManager.addEntity(logMediumLeft);

                        break;
                    case 8:
                        //Direction.RIGHT
                        x = 0;
                        y = (5 * Tile.HEIGHT) + 15;
                        width = logLarge.getWidth(); //Log.Size.LARGE
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Log.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new right, large Log instance.
                        Log logLargeRight = new Log(x, y, Log.Size.LARGE,
                                Creature.Direction.RIGHT, logLarge);
                        logLargeRight.init(game);
                        entityManager.addEntity(logLargeRight);

                        break;
                    case 9:
                        //Direction.LEFT
                        x = (19 * Tile.WIDTH);
                        y = (6 * Tile.HEIGHT) + 15;
                        width = logSmall.getWidth(); //Log.Size.SMALL
                        height = Tile.HEIGHT;

                        //checking for overlap before instantiating new Log.
                        for (Entity e : entityManager.getEntities()) {
                            if (e.getCollisionBounds(0, 0).intersect(new Rect(x, y, x + width, y + height))) {
                                return;
                            }
                        }

                        //add new left, small Log instance.
                        Log logSmallLeft = new Log(x, y, Log.Size.SMALL,
                                Creature.Direction.LEFT, logSmall);
                        logSmallLeft.init(game);
                        entityManager.addEntity(logSmallLeft);

                        break;
                    default:
                        android.util.Log.d(TAG, getClass().getSimpleName() + ".update(long elapsed), switch (controllerForNextEntityInstantiation) construct's default block.");
                        break;
                }
            }
        }
    }

    private Tile[][] createAndInitTilesForFrogger(Game game) {
        Bitmap imageFrogger = cropImageFrogger(game.getContext().getResources());
        int columns = imageFrogger.getWidth() / (3 * Tile.WIDTH);
        int rows = imageFrogger.getHeight() / (3 * Tile.HEIGHT);
        Tile[][] frogger = new Tile[rows][columns];

        // Initialize the tiles (provide image and define walkable)
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int xInPixel = x * (3 * Tile.WIDTH);
                int yInPixel = y * (3 * Tile.HEIGHT);
                int widthInPixel = (3 * Tile.WIDTH);
                int heightInPixel = (3 * Tile.HEIGHT);
                Bitmap tileSprite = Bitmap.createBitmap(imageFrogger, xInPixel, yInPixel, widthInPixel, heightInPixel);

                Tile tile = new Tile("x");
                tile.init(game, x, y, tileSprite);
                frogger[y][x] = tile;
            }
        }

        return frogger;
    }

    private Map<String, Rect> createTransferPointsForFrogger() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        return transferPoints;
    }

    private List<Entity> createEntitiesForFrogger() {
        List<Entity> entities = new ArrayList<Entity>();
        return entities;
    }

    private List<Item> createItemsForFrogger() {
        List<Item> items = new ArrayList<Item>();
        return items;
    }

    private Bitmap cropImageFrogger(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.frogger_background);
    }

    private void initFroggerSprites(Resources resources) {
        android.util.Log.d(TAG, getClass().getSimpleName() + ".initFroggerSprites(Resources resources) START...");

        spriteSheetFrogger = BitmapFactory.decodeResource(resources, R.drawable.frogger_entities);

        winningRow = Bitmap.createBitmap(spriteSheetFrogger, 0, 55, 399, 52);
        startingRow = Bitmap.createBitmap(spriteSheetFrogger, 0, 119, 399, 34);
        logLarge = Bitmap.createBitmap(spriteSheetFrogger, 7, 166, 177, 21);
        logMedium = Bitmap.createBitmap(spriteSheetFrogger, 7, 198, 116, 21);
        logSmall = Bitmap.createBitmap(spriteSheetFrogger, 7, 230, 84, 21);
        parrotRight = Bitmap.createBitmap(spriteSheetFrogger, 140, 236, 16, 16);
        carPinkLeft = Bitmap.createBitmap(spriteSheetFrogger, 10, 267, 28, 20);
        carWhiteRight = Bitmap.createBitmap(spriteSheetFrogger, 46, 265, 24, 24);
        carYellowLeft = Bitmap.createBitmap(spriteSheetFrogger, 82, 264, 24, 26);
        seaLionRight = Bitmap.createBitmap(spriteSheetFrogger, 116, 271, 32, 18);
        bigRigLeft = Bitmap.createBitmap(spriteSheetFrogger, 106, 302, 46, 18);

        frogRight = new Bitmap[2];
        frogRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 13, 334, 17, 23);
        frogRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 43, 335, 25, 22);
        frogLeft = new Bitmap[2];
        frogLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 83, 335, 17, 23);
        frogLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 112, 338, 25, 22);
        frogUp = new Bitmap[2];
        frogUp[0] = Bitmap.createBitmap(spriteSheetFrogger, 12, 369, 23, 17);
        frogUp[1] = Bitmap.createBitmap(spriteSheetFrogger, 46, 366, 22, 25);
        frogDown = new Bitmap[2];
        frogDown[0] = Bitmap.createBitmap(spriteSheetFrogger, 80, 369, 23, 17);
        frogDown[1] = Bitmap.createBitmap(spriteSheetFrogger, 114, 366, 22, 25);
        turtleLeft = new Bitmap[5];
        turtleLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 15, 406, 31, 22);
        turtleLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 54, 407, 31, 22);
        turtleLeft[2] = Bitmap.createBitmap(spriteSheetFrogger, 94, 408, 29, 19);
        turtleLeft[3] = Bitmap.createBitmap(spriteSheetFrogger, 134, 408, 29, 21);
        turtleLeft[4] = Bitmap.createBitmap(spriteSheetFrogger, 179, 408, 26, 21);
        crocRight = new Bitmap[2];
        crocRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 156, 332, 89, 29);
        crocRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 156, 373, 89, 21);
        snowPlowRight = new Bitmap[3];
        snowPlowRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 11, 301, 23, 21);
        snowPlowRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 42, 301, 23, 21);
        snowPlowRight[2] = Bitmap.createBitmap(spriteSheetFrogger, 73, 301, 23, 21);
        snakeLeft = new Bitmap[4];
        snakeLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 184, 226, 38, 10);
        snakeLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 185, 251, 37, 13);
        snakeLeft[2] = Bitmap.createBitmap(spriteSheetFrogger, 184, 276, 38, 16);
        snakeLeft[3] = Bitmap.createBitmap(spriteSheetFrogger, 185, 304, 37, 13);
        frogNPCRight = new Bitmap[2];
        frogNPCRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 236, 407, 20, 24);
        frogNPCRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 270, 409, 27, 24);
        frogNPCLeft = new Bitmap[2];
        frogNPCLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 315, 407, 19, 24);
        frogNPCLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 348, 409, 28, 23);

        android.util.Log.d(TAG, getClass().getSimpleName() + ".initFroggerSprites(Resources resources) FINISH!!!");
    }
}