package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Cheese;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Egg;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.EntityCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Fodder;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Milk;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.BedTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneHouseLevel01 extends Scene {
    public static final String TAG = SceneHouseLevel01.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 14;

    private boolean leftHouseToday; // initialize to false if game starts if game starts in SceneHouseLevel01.
    private boolean slept;

    private static SceneHouseLevel01 uniqueInstance;

    private SceneHouseLevel01() {
        super();
        List<Entity> entitiesForHouseLevel01 = createEntitiesForHouseLevel01();
        entityManager.loadEntities(entitiesForHouseLevel01);
        List<Item> itemsForHouseLevel01 = createItemsForHouseLevel01();
        itemManager.loadItems(itemsForHouseLevel01);
        leftHouseToday = true; // started game in SceneFarm.
        slept = false;
    }

    public void onExitToFarm() {
        leftHouseToday = true;

        if (slept) {
            ///////////////////
            game.startNewDay();
            ///////////////////

            slept = false;
        }
    }

    public void onBedTileClicked() {
        if (leftHouseToday) {
            slept = true;
            leftHouseToday = false;
        }
    }

    public static SceneHouseLevel01 getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneHouseLevel01();
        }
        return uniqueInstance;
    }

    public static void setInstance(SceneHouseLevel01 sceneHouseLevel01) {
        uniqueInstance = sceneHouseLevel01;
    }

    public void startNewDay() {
        Log.e(TAG, "startNewDay()");

        for (Entity e : entityManager.getEntities()) {
            if (e instanceof AimlessWalker) {
                if (((AimlessWalker) e).getType() == AimlessWalker.Type.CHICK) {
                    ((AimlessWalker) e).incrementAgeInDays();
                }
            }
        }
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForHouseLevel01 = createAndInitTilesForHouseLevel01(game);
        tileManager.loadTiles(tilesForHouseLevel01);
        Map<String, Rect> transferPointsForHouseLevel01 = createTransferPointsForHouseLevel01();
        tileManager.loadTransferPoints(transferPointsForHouseLevel01); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
    }

    @Override
    protected void doJustPressedButtonA() {
        super.doJustPressedButtonA();

        Player player = Player.getInstance();
        Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();
        Item itemCurrentlyFacing = player.getItemCurrentlyFacing();
        Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();

        // holding vs not holding
        if (player.hasCarryable()) {
            Log.d(TAG, "player.hasCarryable()");

            // holding Carryable (place down or place in shipping bin)

            // ITEM CHECK
            if (itemCurrentlyFacing == null) {
                Log.d(TAG, "itemCurrentlyFacing == null");
                // ENTITY CHECK
                if (entityCurrentlyFacing == null) {
                    Log.d(TAG, "entityCurrentlyFacing == null");
                    // TILE CHECK
                    if (tileCurrentlyFacing == null) {
                        Log.e(TAG, "tileCurrentlyFacing == null");
                    } else {
                        Log.d(TAG, "tileCurrentlyFacing != null");

                        if (tileCurrentlyFacing.isWalkable()) {
                            Log.d(TAG, "tileCurrentlyFacing.isWalkable()");

                            if (player.getCarryable() instanceof AimlessWalker) {
                                ((AimlessWalker) player.getCarryable()).changeToWalk();
                            }

                            ///////////////////
                            player.placeDown();
                            ///////////////////
                        } else {
                            Log.e(TAG, "NOT tileCurrentlyFacing.isWalkable()");
                        }
                    }
                } else {
                    Log.d(TAG, "entityCurrentlyFacing != null");
                }
            } else {
                Log.d(TAG, "itemCurrentlyFacing != null");
            }
        } else {
            Log.d(TAG, "NOT player.hasCarryable()");

            // not holding Carryable (pick up)

            // ITEM CHECK
            if (itemCurrentlyFacing == null) {
                Log.d(TAG, "itemCurrentlyFacing == null");
                // ENTITY CHECK
                if (entityCurrentlyFacing == null) {
                    Log.d(TAG, "entityCurrentlyFacing == null");
                    // TILE CHECK
                    if (tileCurrentlyFacing == null) {
                        Log.e(TAG, "tileCurrentlyFacing == null");
                    } else {
                        Log.d(TAG, "tileCurrentlyFacing != null");

                        if (tileCurrentlyFacing instanceof BedTile) {
                            Log.d(TAG, "tileCurrentlyFacing instanceof BedTile");

                            onBedTileClicked();
                        } else {
                            // *** STATS_DISPLAYER_FRAGMENT'S BUTTON HOLDER CHECK ***
                            if (game.getItemStoredInButtonHolderA() instanceof TileCommandOwner) {
                                TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderA();
                                TileCommand tileCommand = tileCommandOwner.getTileCommand();

                                Log.d(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                                tileCommand.setTile(tileCurrentlyFacing);
                                tileCommand.execute();
                            } else if (game.getItemStoredInButtonHolderA() instanceof EntityCommandOwner) {
                                if (entityCurrentlyFacing != null) {
                                    EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderA();
                                    EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                                    Log.d(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                                    entityCommand.setEntity(entityCurrentlyFacing);
                                    entityCommand.execute();
                                }
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "entityCurrentlyFacing != null");

                    // check for harvestable plants (change tile to untilled)
                    if (entityCurrentlyFacing instanceof Plant &&
                            ((Plant) entityCurrentlyFacing).isHarvestable()) {
                        Log.d(TAG, "entityCurrentlyFacing instanceof Plant && ((Plant) entityCurrentlyFacing).isHarvestable()");

                        /////////////////////////////////////
                        player.pickUp(entityCurrentlyFacing);
                        /////////////////////////////////////

                        // TILE CHECK
                        if (tileCurrentlyFacing instanceof GrowableTile) {
                            Log.d(TAG, "tileCurrentlyFacing instanceof GrowableTile");

                            ((GrowableTile) tileCurrentlyFacing).changeToUntilled();
                        } else {
                            Log.e(TAG, "tileFacing NOT instanceof GrowableTile");
                        }
                    }
                    // check for aimless walkers (change walker to off)
                    else if (entityCurrentlyFacing instanceof AimlessWalker) {
                        Log.d(TAG, "entityCurrentlyFacing instanceof AimlessWalker");

                        ((AimlessWalker) entityCurrentlyFacing).changeToOff();

                        /////////////////////////////////////
                        player.pickUp(entityCurrentlyFacing);
                        /////////////////////////////////////
                    } else {
                        // *** STATS_DISPLAYER_FRAGMENT'S BUTTON HOLDER CHECK ***
                        if (game.getItemStoredInButtonHolderA() instanceof TileCommandOwner) {
                            TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderA();
                            TileCommand tileCommand = tileCommandOwner.getTileCommand();

                            Log.d(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                            tileCommand.setTile(tileCurrentlyFacing);
                            tileCommand.execute();
                        } else if (game.getItemStoredInButtonHolderA() instanceof EntityCommandOwner) {
                            if (entityCurrentlyFacing != null) {
                                EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderA();
                                EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                                Log.d(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                                entityCommand.setEntity(entityCurrentlyFacing);
                                entityCommand.execute();
                            }
                        }
                    }
                }
            } else {
                Log.d(TAG, "itemCurrentlyFacing != null");

                // check for fodder, egg, milk, and cheese
                if (itemCurrentlyFacing instanceof Fodder ||
                        itemCurrentlyFacing instanceof Egg ||
                        itemCurrentlyFacing instanceof Milk ||
                        itemCurrentlyFacing instanceof Cheese) {
                    player.pickUp(itemCurrentlyFacing);
                }
                // everything else goes into backpack (default response)
                else {
                    // put item into backpack
                    boolean successfullyAddedToBackpack = player.respondToItemCollisionViaClick(
                            itemCurrentlyFacing
                    );

                    if (successfullyAddedToBackpack) {
                        // do nothing.
                    } else {
                        // do nothing.
                    }
                }
            }
        }
    }

    @Override
    protected void doJustPressedButtonB() {
        super.doJustPressedButtonB();

        Player player = Player.getInstance();
        Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();
        Item itemCurrentlyFacing = player.getItemCurrentlyFacing();
        Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();

        if (player.hasCarryable()) {
            // do nothing.
        } else {
            // *** STATS_DISPLAYER_FRAGMENT'S BUTTON HOLDER CHECK ***
            if (game.getItemStoredInButtonHolderB() instanceof TileCommandOwner) {
                TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderB();
                TileCommand tileCommand = tileCommandOwner.getTileCommand();

                Log.d(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                tileCommand.setTile(tileCurrentlyFacing);
                tileCommand.execute();
            } else if (game.getItemStoredInButtonHolderB() instanceof EntityCommandOwner) {
                if (entityCurrentlyFacing != null) {
                    EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderB();
                    EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                    Log.d(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                    entityCommand.setEntity(entityCurrentlyFacing);
                    entityCommand.execute();
                }
            }
        }
    }

    @Override
    public void enter(List<Object> args) {
        super.enter(args);
        game.getTimeManager().setIsPaused(true);

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_SPAWN_INDEX_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
        }
        GameCamera.getInstance().update(0L);
    }

    private Tile[][] createAndInitTilesForHouseLevel01(Game game) {
        String houseLevel01LoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_house_level_01);
        Tile[][] houseLevel01 = TileManagerLoader.convertStringToTiles(houseLevel01LoadedAsString);
        Bitmap imageHouseLevel01 = cropImageHouseLevel01(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in houseLevel01.
        for (int y = 0; y < houseLevel01.length; y++) {
            for (int x = 0; x < houseLevel01[0].length; x++) {
                int xInPixel = x * (Tile.WIDTH * 4);
                int yInPixel = y * (Tile.HEIGHT * 4);
                int widthInPixel = (Tile.WIDTH * 4);
                int heightInPixel = (Tile.HEIGHT * 4);

                Tile tile = houseLevel01[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHouseLevel01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHouseLevel01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //BedTile
                else if (tile.getId().equals("b")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHouseLevel01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    houseLevel01[y][x] = new BedTile(BedTile.TAG);
                    houseLevel01[y][x].init(game, x, y, tileSprite);
                    houseLevel01[y][x].setWalkable(false);
                } else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return houseLevel01;
    }

    private Bitmap cropImageHouseLevel01(Resources resources) {
        Log.d(TAG, "SceneHouseLevel01.cropImageHouseLevel01(Resources resources)");

//        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
//        Bitmap houseLevel01 = null;
//
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        houseLevel01 = Bitmap.createBitmap(indoorsFarmHM2, 6, 6, 160, 192);
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        Log.d(TAG, "Bitmap houseLevel01's (width, height): " + houseLevel01.getWidth() + ", " + houseLevel01.getHeight());
//
//        return houseLevel01;

        Bitmap houseLevel01 = BitmapFactory.decodeResource(resources, R.drawable.scene_house_level_01);
        return houseLevel01;
    }

    private Map<String, Rect> createTransferPointsForHouseLevel01() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (16 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (4 * Tile.WIDTH), (16 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForHouseLevel01() {
        List<Entity> entities = new ArrayList<Entity>();
        return entities;
    }

    private List<Item> createItemsForHouseLevel01() {
        List<Item> items = new ArrayList<Item>();
        return items;
    }
}