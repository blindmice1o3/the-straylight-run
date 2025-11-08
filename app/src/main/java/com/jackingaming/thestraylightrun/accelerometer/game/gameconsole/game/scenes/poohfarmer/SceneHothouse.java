package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Assets;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Cheese;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Egg;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.EntityCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Fodder;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.GrowingPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Milk;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableIndoorTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneHothouse extends Scene {
    public static final String TAG = SceneHothouse.class.getSimpleName();

    public interface LootListener {
        void onLootDropped();
    }

    private LootListener lootListener;

    public void setLootListener(LootListener lootListener) {
        this.lootListener = lootListener;
    }

    public static final int X_SPAWN_INDEX_DEFAULT = 5;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;

    private static SceneHothouse uniqueInstance;

    private List<GrowableTile> growableTiles;
    private ShippingBinTile.IncomeListener shippingBinIncomeListener;

    private SceneHothouse() {
        super();
        List<Entity> entitiesForHothouse = createEntitiesForHothouse();
        entityManager.loadEntities(entitiesForHothouse);
        List<Item> itemsForHothouse = createItemsForHothouse();
        itemManager.loadItems(itemsForHothouse);

        growableTiles = new ArrayList<>();

        shippingBinIncomeListener = new ShippingBinTile.IncomeListener() {
            @Override
            public void incrementCurrency(float amountToIncrement) {
                game.incrementCurrency(amountToIncrement);
            }
        };
    }

    public void startNewDay() {
        Log.e(TAG, "startNewDay()");

        for (GrowableTile growableTile : growableTiles) {
            growableTile.startNewDay();
        }
    }

    public static SceneHothouse getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneHothouse();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForHothouse = createAndInitTilesForHothouse(game);
        tileManager.loadTiles(tilesForHothouse);
        Map<String, Rect> transferPointsForHothouse = createTransferPointsForHothouse();
        tileManager.loadTransferPoints(transferPointsForHothouse); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        for (int y = 0; y < tilesForHothouse.length; y++) {
            for (int x = 0; x < tilesForHothouse[y].length; x++) {
                Tile tile = tilesForHothouse[y][x];
                if (tile instanceof GrowableTile) {
                    growableTiles.add(((GrowableTile) tile));
                }
            }
        }
        Log.e(TAG, "growableTiles.size() is " + growableTiles.size());

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

                        if (tileCurrentlyFacing instanceof ShippingBinTile) {
                            Log.d(TAG, "tileCurrentlyFacing instanceof ShippingBinTile");

                            if (player.getCarryable() instanceof Sellable) {
                                Log.d(TAG, "player.getCarryable() instanceof Sellable | placeInShippingBin()");

                                ////////////////////////////
                                player.placeInShippingBin();
                                ////////////////////////////
                            } else {
                                Log.d(TAG, "player.getCarryable() NOT instanceof Sellable");
                            }
                        } else if (tileCurrentlyFacing.isWalkable()) {
                            Log.d(TAG, "tileCurrentlyFacing NOT instanceof ShippingBinTile and tileCurrentlyFacing.isWalkable()");

                            if (player.getCarryable() instanceof AimlessWalker) {
                                AimlessWalker aimlessWalker = (AimlessWalker) player.getCarryable();
                                aimlessWalker.placeDown();
                                ////////////////////////////////////////////////////
                                if (lootListener != null) {
                                    lootListener.onLootDropped();
                                }
                                ////////////////////////////////////////////////////
                                aimlessWalker.changeToWalk();
                            }

                            ///////////////////
                            player.placeDown();
                            ///////////////////
                        } else {
                            Log.e(TAG, "tileCurrentlyFacing NOT instanceof ShippingBinTile and NOT tileCurrentlyFacing.isWalkable()");
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

                        // *** STATS_DISPLAYER_FRAGMENT'S BUTTON HOLDER CHECK ***
                        if (game.getItemStoredInButtonHolderA() instanceof TileCommandOwner) {
                            TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderA();
                            TileCommand tileCommand = tileCommandOwner.getTileCommand();

                            Log.d(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                            tileCommand.setTile(tileCurrentlyFacing);
                            boolean wasSuccessful = tileCommand.execute();

                            // decrement quantity.
                            if (wasSuccessful &&
                                    game.getItemStoredInButtonHolderA() instanceof GrowingPot) {
                                game.removeItemFromBackpack(
                                        game.getItemStoredInButtonHolderA()
                                );
                            }
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
                            boolean wasSuccessful = tileCommand.execute();

                            // decrement quantity.
                            if (wasSuccessful &&
                                    game.getItemStoredInButtonHolderA() instanceof GrowingPot) {
                                game.removeItemFromBackpack(
                                        game.getItemStoredInButtonHolderA()
                                );
                            }
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

        // check item occupying StatsDisplayerFragment's button holder.
        if (game.getItemStoredInButtonHolderB() instanceof TileCommandOwner) {
            TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderB();
            TileCommand tileCommand = tileCommandOwner.getTileCommand();

            Log.d(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
            tileCommand.setTile(tileCurrentlyFacing);
            boolean wasSuccessful = tileCommand.execute();

            // decrement quantity.
            if (wasSuccessful &&
                    game.getItemStoredInButtonHolderB() instanceof GrowingPot) {
                game.removeItemFromBackpack(
                        game.getItemStoredInButtonHolderB()
                );
            }
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

    public void registerWaterChangeListenerForAllGrowableTile(GrowableIndoorTile.IndoorWaterChangeListener waterChangeListener) {
        Tile[][] tiles = tileManager.getTiles();
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[0].length; column++) {
                if (tiles[row][column] instanceof GrowableIndoorTile) {
                    ((GrowableIndoorTile) tiles[row][column]).setIndoorWaterChangeListener(
                            waterChangeListener
                    );
                }
            }
        }
    }

    public void unregisterWaterChangeListenerForAllGrowableTile() {
        Tile[][] tiles = tileManager.getTiles();
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[0].length; column++) {
                if (tiles[row][column] instanceof GrowableIndoorTile) {
                    ((GrowableIndoorTile) tiles[row][column]).setIndoorWaterChangeListener(
                            null
                    );
                }
            }
        }
    }

    private Tile[][] createAndInitTilesForHothouse(Game game) {
        String hothouseLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_hothouse);
        Tile[][] hothouse = TileManagerLoader.convertStringToTiles(hothouseLoadedAsString);
        Bitmap imageHothouse = cropImageHothouse(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in hothouse.
        for (int y = 0; y < hothouse.length; y++) {
            for (int x = 0; x < hothouse[0].length; x++) {
                int xInPixel = x * (Tile.WIDTH * 4);
                int yInPixel = y * (Tile.HEIGHT * 4);
                int widthInPixel = (Tile.WIDTH * 4);
                int heightInPixel = (Tile.HEIGHT * 4);

                Tile tile = hothouse[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //SignPostTile
                else if (tile.getId().equals("a")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //GrowableIndoorTile
                else if (tile.getId().equals("p")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    hothouse[y][x] = new GrowableIndoorTile(GrowableIndoorTile.TAG, new GrowableTile.EntityListener() {
                        @Override
                        public void addEntityToScene(Entity entityToAdd) {
                            entityManager.addEntity(entityToAdd);
                        }
                    });
                    hothouse[y][x].init(game, x, y, tileSprite);
                    hothouse[y][x].setWalkable(false);
                }
                //ShippingBinTile
                else if (tile.getId().equals("c")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    Bitmap shippingBinQ1 = Assets.shippingBinQuadrantTopLeft;

                    Bitmap tileSpriteAndShippingBinQ1 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ1);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ1, 0, 0, null);

                    hothouse[y][x] = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_LEFT,
                            shippingBinIncomeListener);
                    hothouse[y][x].init(game, x, y, tileSpriteAndShippingBinQ1);
                    hothouse[y][x].setWalkable(false);
                } else if (tile.getId().equals("d")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    Bitmap shippingBinQ2 = Assets.shippingBinQuadrantTopRight;

                    Bitmap tileSpriteAndShippingBinQ2 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ2);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ2, 0, 0, null);

                    hothouse[y][x] = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_RIGHT,
                            shippingBinIncomeListener);
                    hothouse[y][x].init(game, x, y, tileSpriteAndShippingBinQ2);
                    hothouse[y][x].setWalkable(false);
                } else if (tile.getId().equals("e")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    Bitmap shippingBinQ3 = Assets.shippingBinQuadrantBottomLeft;

                    Bitmap tileSpriteAndShippingBinQ3 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ3);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ3, 0, 0, null);

                    hothouse[y][x] = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_LEFT,
                            shippingBinIncomeListener);
                    hothouse[y][x].init(game, x, y, tileSpriteAndShippingBinQ3);
                    hothouse[y][x].setWalkable(false);
                } else if (tile.getId().equals("f")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    Bitmap shippingBinQ4 = Assets.shippingBinQuadrantBottomRight;

                    Bitmap tileSpriteAndShippingBinQ4 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ4);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ4, 0, 0, null);

                    hothouse[y][x] = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT,
                            shippingBinIncomeListener);
                    hothouse[y][x].init(game, x, y, tileSpriteAndShippingBinQ4);
                    hothouse[y][x].setWalkable(false);
                }
                //default
                else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return hothouse;
    }

    private Bitmap cropImageHothouse(Resources resources) {
        Log.d(TAG, "SceneHothouse.cropImageHothouse(Resources resources)");

//        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
//        Bitmap hothouseEmpty = null;
//
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        hothouseEmpty = Bitmap.createBitmap(indoorsFarmHM2, 223, 1200, 192, 256);
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        Log.d(TAG, "Bitmap hothouseEmpty's (width, height): " + hothouseEmpty.getWidth() + ", " + hothouseEmpty.getHeight());
        Bitmap hotHouse = BitmapFactory.decodeResource(resources, R.drawable.scene_hot_house);

//        return hothouseEmpty;
        return hotHouse;
    }

    private Map<String, Rect> createTransferPointsForHothouse() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (16 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (4 * Tile.WIDTH), (16 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForHothouse() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForHothouse() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}