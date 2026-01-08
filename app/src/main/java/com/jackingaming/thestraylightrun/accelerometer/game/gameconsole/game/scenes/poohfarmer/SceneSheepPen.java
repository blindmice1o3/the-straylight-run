package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.SeedGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableIndoorTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.WaterGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.FeedingStallTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.FodderStashTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneSheepPen extends Scene {
    public static final String TAG = SceneSheepPen.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;

    private static SceneSheepPen uniqueInstance;

    private SceneSheepPen() {
        super();
        List<Entity> entitiesForSheepPen = createEntitiesForSheepPen();
        entityManager.loadEntities(entitiesForSheepPen);
        List<Item> itemsForSheepPen = createItemsForSheepPen();
        itemManager.loadItems(itemsForSheepPen);
    }

    public static SceneSheepPen getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneSheepPen();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForSheepPen = createAndInitTilesForSheepPen(game);
        tileManager.loadTiles(tilesForSheepPen);
        Map<String, Rect> transferPointsForSheepPen = createTransferPointsForSheepPen();
        tileManager.loadTransferPoints(transferPointsForSheepPen); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
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

                                if (tileCommand instanceof TillGrowableTileCommand || tileCommand instanceof TillGrowableIndoorTileCommand) {
                                    game.playSFX(SoundManager.sfxShovel);
                                } else if (tileCommand instanceof SeedGrowableTileCommand) {
                                    game.playSFX(SoundManager.sfxSow);
                                } else if (tileCommand instanceof WaterGrowableTileCommand) {
                                    game.playSFX(SoundManager.sfxBubbles);
                                }

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

                            if (tileCommand instanceof TillGrowableTileCommand || tileCommand instanceof TillGrowableIndoorTileCommand) {
                                game.playSFX(SoundManager.sfxShovel);
                            } else if (tileCommand instanceof SeedGrowableTileCommand) {
                                game.playSFX(SoundManager.sfxSow);
                            } else if (tileCommand instanceof WaterGrowableTileCommand) {
                                game.playSFX(SoundManager.sfxBubbles);
                            }

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

        // check item occupying StatsDisplayerFragment's button holder.
        if (game.getItemStoredInButtonHolderB() instanceof TileCommandOwner) {
            TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderB();
            TileCommand tileCommand = tileCommandOwner.getTileCommand();

            if (tileCommand instanceof TillGrowableTileCommand || tileCommand instanceof TillGrowableIndoorTileCommand) {
                game.playSFX(SoundManager.sfxShovel);
            } else if (tileCommand instanceof SeedGrowableTileCommand) {
                game.playSFX(SoundManager.sfxSow);
            } else if (tileCommand instanceof WaterGrowableTileCommand) {
                game.playSFX(SoundManager.sfxBubbles);
            }

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

    private Tile[][] createAndInitTilesForSheepPen(Game game) {
        String sheepPenLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_sheep_pen);
        Tile[][] sheepPen = TileManagerLoader.convertStringToTiles(sheepPenLoadedAsString);
        Bitmap imageSheepPen = cropImageSheepPen(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in sheepPen.
        for (int y = 0; y < sheepPen.length; y++) {
            for (int x = 0; x < sheepPen[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = sheepPen[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageSheepPen, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageSheepPen, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //default
                else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return sheepPen;
    }

    private Bitmap cropImageSheepPen(Resources resources) {
        Log.d(TAG, "SceneSheepPen.cropImageSheepPen(Resources resources)");

        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
        Bitmap sheepPenEmpty = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        sheepPenEmpty = Bitmap.createBitmap(indoorsFarmHM2, 8, 902, 240, 256);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap sheepPenEmpty's (width, height): " + sheepPenEmpty.getWidth() + ", " + sheepPenEmpty.getHeight());

        return sheepPenEmpty;
    }

    private Map<String, Rect> createTransferPointsForSheepPen() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (13 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (3 * Tile.WIDTH), (13 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    @Override
    public void drawCurrentFrame(Canvas canvas) {
        super.drawCurrentFrame(canvas);
//        Rect screenRectOfTransferPoint = GameCamera.getInstance().convertInGameRectToScreenRect(tileManager.getTransferPointBounds("FARM"));
//        Paint paint = new Paint();
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(screenRectOfTransferPoint, paint);
//
//        Rect screenRectOfPlayer = GameCamera.getInstance().convertInGameRectToScreenRect(Player.getInstance().getCollisionBounds(0, 0));
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(screenRectOfPlayer, paint);
    }

    private List<Entity> createEntitiesForSheepPen() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForSheepPen() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}