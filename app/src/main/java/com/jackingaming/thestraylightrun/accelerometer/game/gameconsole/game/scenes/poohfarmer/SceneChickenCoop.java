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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Egg;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.EntityCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Fodder;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.FeedingStallTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.FodderStashTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneChickenCoop extends Scene {
    public static final String TAG = SceneChickenCoop.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;
    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 64;

    private static SceneChickenCoop uniqueInstance;

    private ShippingBinTile.IncomeListener shippingBinIncomeListener;
    private List<FeedingStallTile> feedingStallTiles;

    private SceneChickenCoop() {
        super();
        List<Entity> entitiesForChickenCoop = createEntitiesForChickenCoop();
        entityManager.loadEntities(entitiesForChickenCoop);
        List<Item> itemsForChickenCoop = createItemsForChickenCoop();
        itemManager.loadItems(itemsForChickenCoop);

        shippingBinIncomeListener = new ShippingBinTile.IncomeListener() {
            @Override
            public void incrementCurrency(float amountToIncrement) {
                game.incrementCurrency(amountToIncrement);
            }
        };

        feedingStallTiles = new ArrayList<>();
    }

    public static SceneChickenCoop getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneChickenCoop();
        }
        return uniqueInstance;
    }

    public void startNewDay() {
        Log.e(TAG, "startNewDay()");

        int numberOfFodderInFeedingStall = 0;
        for (FeedingStallTile feedingStallTile : feedingStallTiles) {
            if (feedingStallTile.isOccupied()) {
                numberOfFodderInFeedingStall++;
            }

            ///////////////////////////////
            feedingStallTile.startNewDay();
            ///////////////////////////////
        }

        int numberOfChickenInChickenCoop = 0;
        for (Entity entityInChickenCoop : entityManager.getEntities()) {
            if (entityInChickenCoop instanceof AimlessWalker) {
                if (((AimlessWalker) entityInChickenCoop).getType() == AimlessWalker.Type.CHICKEN) {
                    numberOfChickenInChickenCoop++;
                }
            }
        }

        Log.e(TAG, "numberOfChickenInChickenCoop: " + numberOfChickenInChickenCoop);
        Log.e(TAG, "numberOfFodderInFeedingStall: " + numberOfFodderInFeedingStall);

        // limited by chicken
        if (numberOfFodderInFeedingStall >= numberOfChickenInChickenCoop) {
            for (int i = 0; i < numberOfChickenInChickenCoop; i++) {
                generateEggToRandomWalkableTile();
            }
        }
        // limited by fodder
        else {
            for (int i = 0; i < numberOfFodderInFeedingStall; i++) {
                generateEggToRandomWalkableTile();
            }
        }
    }

    private void generateEggToRandomWalkableTile() {
        Egg egg = new Egg();
        egg.init(game);

        Tile[][] tiles = tileManager.getTiles();
        boolean lookingForRandomWalkableTile = true;

        while (lookingForRandomWalkableTile) {
            int xRandom = (int) (Math.random() * tiles[0].length);
            int yRandom = (int) (Math.random() * tiles.length);

            Log.e(TAG, "xRandom: " + xRandom);
            Log.e(TAG, "yRandom: " + yRandom);

            if (tiles[yRandom][xRandom].isWalkable()) {
                egg.setPosition(
                        (xRandom * Tile.WIDTH),
                        (yRandom * Tile.HEIGHT)
                );
                itemManager.addItem(egg);

                ///////////////////////////////
                lookingForRandomWalkableTile = false;
                ///////////////////////////////
            }
        }
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForChickenCoop = createAndInitTilesForChickenCoop(game);
        tileManager.loadTiles(tilesForChickenCoop);
        Map<String, Rect> transferPointsForChickenCoop = createTransferPointsForChickenCoop();
        tileManager.loadTransferPoints(transferPointsForChickenCoop); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);

        aimlessWalker1.changeToWalk();
        aimlessWalker2.changeToWalk();
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

                        if (tileCurrentlyFacing instanceof FeedingStallTile) {
                            if (player.getCarryable() instanceof Fodder) {
                                if (!((FeedingStallTile) tileCurrentlyFacing).isOccupied()) {
                                    ((FeedingStallTile) tileCurrentlyFacing).acceptFodder(
                                            (Fodder) (player.getCarryable())
                                    );

                                    player.removeCarryable();
                                }
                            } else {
                                Log.e(TAG, "player.getCarryable() NOT instanceof Fodder");
                            }
                        } else if (tileCurrentlyFacing instanceof ShippingBinTile) {
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
                                ((AimlessWalker) player.getCarryable()).changeToWalk();
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

                        if (tileCurrentlyFacing instanceof FodderStashTile) {
                            Log.d(TAG, "tileCurrentlyFacing instanceof FodderStashTile");

                            Fodder fodderToBeCarried = ((FodderStashTile) tileCurrentlyFacing).generateFodder();
                            if (fodderToBeCarried != null) {
                                fodderToBeCarried.init(game);

                                player.pickUp(fodderToBeCarried);
                            } else {
                                Log.e(TAG, "fodderToBeCarried == null");
                            }
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

                // check for fodder
                if (itemCurrentlyFacing instanceof Fodder) {
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

    private Tile[][] createAndInitTilesForChickenCoop(Game game) {
        String chickenCoopLoadedAsString = TileManagerLoader.loadFileAsString(
                game.getContext().getResources(), R.raw.tile_chicken_coop
        );
        Tile[][] chickenCoop = TileManagerLoader.convertStringToTiles(chickenCoopLoadedAsString);
        Bitmap imageChickenCoop = cropImageChickenCoop(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in chickenCoop.
        for (int y = 0; y < chickenCoop.length; y++) {
            for (int x = 0; x < chickenCoop[0].length; x++) {
                int xInPixel = x * TILE_WIDTH;
                int yInPixel = y * TILE_HEIGHT;
                int widthInPixel = TILE_WIDTH;
                int heightInPixel = TILE_HEIGHT;

                Tile tile = chickenCoop[y][x];
                //(GenericWalkableTile)
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //(GenericSolidTile)
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //(SignPostTile)
                else if (tile.getId().equals("a")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //(EggIncubatorTile)
                else if (tile.getId().equals("g")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //FodderStashTile
                else if (tile.getId().equals("h")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Tile tileFodderStash = new FodderStashTile(FodderStashTile.TAG);
                    tileFodderStash.init(game, x, y, tileSprite);
                    tileFodderStash.setWalkable(false);

                    chickenCoop[y][x] = tileFodderStash;
                }
                //FeedingStallTile
                else if (tile.getId().equals("i")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    FeedingStallTile tileFeedingStall = new FeedingStallTile(FeedingStallTile.TAG);
                    tileFeedingStall.init(game, x, y, tileSprite);
                    tileFeedingStall.setWalkable(false);

                    chickenCoop[y][x] = tileFeedingStall;

                    feedingStallTiles.add(tileFeedingStall);
                }
                //ShippingBinTile
                else if (tile.getId().equals("c")) {
                    Bitmap shippingBinQ1 = Assets.shippingBinQuadrantTopLeft;
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ1 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ1);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ1, 0, 0, null);

                    Tile shippingBinTileTopLeft = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_LEFT,
                            shippingBinIncomeListener);
                    shippingBinTileTopLeft.init(game, x, y, tileSpriteAndShippingBinQ1);
                    shippingBinTileTopLeft.setWalkable(false);
                    chickenCoop[y][x] = shippingBinTileTopLeft;
                } else if (tile.getId().equals("d")) {
                    Bitmap shippingBinQ2 = Assets.shippingBinQuadrantTopRight;
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ2 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ2);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ2, 0, 0, null);

                    Tile shippingBinTileTopRight = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_RIGHT,
                            shippingBinIncomeListener);
                    shippingBinTileTopRight.init(game, x, y, tileSpriteAndShippingBinQ2);
                    shippingBinTileTopRight.setWalkable(false);
                    chickenCoop[y][x] = shippingBinTileTopRight;
                } else if (tile.getId().equals("e")) {
                    Bitmap shippingBinQ3 = Assets.shippingBinQuadrantBottomLeft;
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ3 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ3);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ3, 0, 0, null);

                    Tile shippingBinTileBottomLeft = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_LEFT,
                            shippingBinIncomeListener);
                    shippingBinTileBottomLeft.init(game, x, y, tileSpriteAndShippingBinQ3);
                    shippingBinTileBottomLeft.setWalkable(false);
                    chickenCoop[y][x] = shippingBinTileBottomLeft;
                } else if (tile.getId().equals("f")) {
                    Bitmap shippingBinQ4 = Assets.shippingBinQuadrantBottomRight;
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ4 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ4);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ4, 0, 0, null);

                    Tile shippingBinTileBottomRight = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT,
                            shippingBinIncomeListener);
                    shippingBinTileBottomRight.init(game, x, y, tileSpriteAndShippingBinQ4);
                    shippingBinTileBottomRight.setWalkable(false);
                    chickenCoop[y][x] = shippingBinTileBottomRight;
                }
                //default
                else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return chickenCoop;
    }

    private Bitmap cropImageChickenCoop(Resources resources) {
        Log.d(TAG, "SceneChickenCoop.cropImageChickenCoop(Resources resources)");

        Bitmap chickenCoopEmpty = BitmapFactory.decodeResource(resources, R.drawable.scene_chicken_coop);
//        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
//        Bitmap chickenCoopEmpty = null;
//
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        chickenCoopEmpty = Bitmap.createBitmap(indoorsFarmHM2, 78, 603, 240, 256);
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        Log.d(TAG, "Bitmap chickenCoopEmpty's (width, height): " + chickenCoopEmpty.getWidth() + ", " + chickenCoopEmpty.getHeight());

        return chickenCoopEmpty;
    }

    private Map<String, Rect> createTransferPointsForChickenCoop() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (15 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (4 * Tile.WIDTH), (15 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private AimlessWalker aimlessWalker1, aimlessWalker2;

    private List<Entity> createEntitiesForChickenCoop() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        aimlessWalker1 = new AimlessWalker(AimlessWalker.Type.CHICKEN,
                (3 * Tile.WIDTH),
                (6 * Tile.HEIGHT));
        aimlessWalker2 = new AimlessWalker(AimlessWalker.Type.CHICKEN,
                (4 * Tile.WIDTH),
                (6 * Tile.HEIGHT));

        entities.add(aimlessWalker1);
        entities.add(aimlessWalker2);

        return entities;
    }

    private List<Item> createItemsForChickenCoop() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}