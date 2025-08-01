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

public class SceneCowBarn extends Scene {
    public static final String TAG = SceneCowBarn.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;

    private static SceneCowBarn uniqueInstance;

    private ShippingBinTile.IncomeListener shippingBinIncomeListener;

    private SceneCowBarn() {
        super();
        List<Entity> entitiesForCowBarn = createEntitiesForCowBarn();
        entityManager.loadEntities(entitiesForCowBarn);
        List<Item> itemsForCowBarn = createItemsForCowBarn();
        itemManager.loadItems(itemsForCowBarn);

        shippingBinIncomeListener = new ShippingBinTile.IncomeListener() {
            @Override
            public void incrementCurrency(float amountToIncrement) {
                game.incrementCurrency(amountToIncrement);
            }
        };
    }

    public static SceneCowBarn getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneCowBarn();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForCowBarn = createAndInitTilesForCowBarn(game);
        tileManager.loadTiles(tilesForCowBarn);
        Map<String, Rect> transferPointsForCowBarn = createTransferPointsForCowBarn();
        tileManager.loadTransferPoints(transferPointsForCowBarn); // transferPoints are transient and should be reloaded everytime.
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
        Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
        Item itemCurrentlyFacing = player.getItemCurrentlyFacing();
        Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();

        // TILE CHECK
        if (tileCurrentlyFacing instanceof FodderStashTile) {
            if (!player.hasCarryable()) {
                Fodder fodderToBeCarried = ((FodderStashTile) tileCurrentlyFacing).generateFodder();
                if (fodderToBeCarried != null) {
                    fodderToBeCarried.init(game);

                    player.pickUp(fodderToBeCarried);
                } else {
                    Log.e(TAG, "fodderToBeCarried == null");
                }
            } else {
                Log.e(TAG, "player.hasCarryable()");
            }
        } else if (tileCurrentlyFacing instanceof FeedingStallTile) {
            if (player.hasCarryable()) {
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
            } else {
                Log.e(TAG, "NOT player.hasCarryable()");
            }
        }

        // ITEM CHECK
        if (itemCurrentlyFacing != null) {
            Log.e(TAG, "itemCurrentlyFacing != null");

            // There an item in front of player.
            if (itemCurrentlyFacing instanceof Fodder) {
                if (!player.hasCarryable()) {
                    // pick up itemCurrentlyFacing
                    player.pickUp(itemCurrentlyFacing);
                } else {
                    Log.e(TAG, "player.hasCarryable()");
                }
            }
            // The item in front of player will receive
            // default response (putting item into backpack).
            else {
                Log.e(TAG, "itemCurrentlyFacing NOT instanceof Fodder");

                // put itemCurrentlyFacing into backpack
                player.respondToItemCollisionViaClick(
                        itemCurrentlyFacing
                );
            }

            return;
        }
        // itemCurrentlyFacing == null
        else {
            if (player.hasCarryable()) {
                if (entityCurrentlyFacing == null &&
                        tileCurrentlyFacing.isWalkable()) {
                    player.placeDown();
                }

                return;
            }
        }

        // ENTITY CHECK (no item in front of player)
        if (player.hasCarryable() && entityCurrentlyFacing == null) {
            Log.e(TAG, "has carryable and entityFacing is null");

            if (tileCurrentlyFacing instanceof ShippingBinTile) {
                Log.e(TAG, "tileCurrentlyFacing instanceof ShippingBinTile");
                if (player.getCarryable() instanceof Sellable) {
                    Log.e(TAG, "carryable is Sellable");
                    player.placeInShippingBin();
                }
            } else if (tileCurrentlyFacing.isWalkable()) {
                Log.e(TAG, "tileCurrentlyFacing.isWalkable()");
                if (player.getCarryable() instanceof AimlessWalker) {
                    ((AimlessWalker) player.getCarryable()).changeToWalk();
                }

                player.placeDown();
            }
        } else if (entityCurrentlyFacing != null &&
                entityCurrentlyFacing instanceof Plant &&
                ((Plant) entityCurrentlyFacing).isHarvestable()) {
            player.pickUp(entityCurrentlyFacing);

            Tile tileFacing = player.checkTileCurrentlyFacing();
            if (tileFacing instanceof GrowableTile) {
                ((GrowableTile) tileFacing).changeToUntilled();
            } else {
                Log.e(TAG, "tileFacing NOT instanceof GrowableTile");
            }
        } else if (entityCurrentlyFacing != null &&
                entityCurrentlyFacing instanceof AimlessWalker) {
            ((AimlessWalker) entityCurrentlyFacing).changeToOff();

            player.pickUp(entityCurrentlyFacing);
        }
        // check item occupying StatsDisplayerFragment's button holder.
        else if (game.getItemStoredInButtonHolderA() instanceof TileCommandOwner) {
            TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderA();
            TileCommand tileCommand = tileCommandOwner.getTileCommand();

            Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
            tileCommand.setTile(tileCurrentlyFacing);
            tileCommand.execute();
        } else if (game.getItemStoredInButtonHolderA() instanceof EntityCommandOwner) {
            if (entityCurrentlyFacing != null) {
                EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderA();
                EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                Log.e(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                entityCommand.setEntity(entityCurrentlyFacing);
                entityCommand.execute();
            }
        }
    }

    private Tile[][] createAndInitTilesForCowBarn(Game game) {
        String cowBarnLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_cow_barn);
        Tile[][] cowBarn = TileManagerLoader.convertStringToTiles(cowBarnLoadedAsString);
        Bitmap imageCowBarn = cropImageCowBarn(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in cowBarn.
        int tileWidth = 64;
        int tileHeight = 64;
        for (int y = 0; y < cowBarn.length; y++) {
            for (int x = 0; x < cowBarn[0].length; x++) {
                int xInPixel = x * tileWidth;
                int yInPixel = y * tileHeight;
                int widthInPixel = tileWidth;
                int heightInPixel = tileHeight;

                Tile tile = cowBarn[y][x];
                //(GenericWalkableTile)
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //(GenericSolidTile)
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //(SignPostTile)
                else if (tile.getId().equals("a")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //(CabbageFermenterTile)
                else if (tile.getId().equals("g")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //FodderStashTile
                else if (tile.getId().equals("h")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Tile tileFodderStash = new FodderStashTile(FodderStashTile.TAG);
                    tileFodderStash.init(game, x, y, tileSprite);
                    tileFodderStash.setWalkable(false);

                    cowBarn[y][x] = tileFodderStash;
                }
                //FeedingStallTile
                else if (tile.getId().equals("i")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Tile tileFeedingStall = new FeedingStallTile(FeedingStallTile.TAG);
                    tileFeedingStall.init(game, x, y, tileSprite);
                    tileFeedingStall.setWalkable(false);

                    cowBarn[y][x] = tileFeedingStall;
                }
                //ShippingBinTile
                else if (tile.getId().equals("c")) {
                    Bitmap shippingBinQ1 = Assets.shippingBinQuadrantTopLeft;
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ1 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ1);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ1, 0, 0, null);

                    Tile shippingBinTileTopLeft = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_LEFT,
                            shippingBinIncomeListener);
                    shippingBinTileTopLeft.init(game, x, y, tileSpriteAndShippingBinQ1);
                    shippingBinTileTopLeft.setWalkable(false);
                    cowBarn[y][x] = shippingBinTileTopLeft;
                } else if (tile.getId().equals("d")) {
                    Bitmap shippingBinQ2 = Assets.shippingBinQuadrantTopRight;
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ2 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ2);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ2, 0, 0, null);

                    Tile shippingBinTileTopRight = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_RIGHT,
                            shippingBinIncomeListener);
                    shippingBinTileTopRight.init(game, x, y, tileSpriteAndShippingBinQ2);
                    shippingBinTileTopRight.setWalkable(false);
                    cowBarn[y][x] = shippingBinTileTopRight;
                } else if (tile.getId().equals("e")) {
                    Bitmap shippingBinQ3 = Assets.shippingBinQuadrantBottomLeft;
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ3 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ3);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ3, 0, 0, null);

                    Tile shippingBinTileBottomLeft = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_LEFT,
                            shippingBinIncomeListener);
                    shippingBinTileBottomLeft.init(game, x, y, tileSpriteAndShippingBinQ3);
                    shippingBinTileBottomLeft.setWalkable(false);
                    cowBarn[y][x] = shippingBinTileBottomLeft;
                } else if (tile.getId().equals("f")) {
                    Bitmap shippingBinQ4 = Assets.shippingBinQuadrantBottomRight;
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    Bitmap tileSpriteAndShippingBinQ4 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ4);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ4, 0, 0, null);

                    Tile shippingBinTileBottomRight = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT,
                            shippingBinIncomeListener);
                    shippingBinTileBottomRight.init(game, x, y, tileSpriteAndShippingBinQ4);
                    shippingBinTileBottomRight.setWalkable(false);
                    cowBarn[y][x] = shippingBinTileBottomRight;
                }
                //default
                else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return cowBarn;
    }

    private Bitmap cropImageCowBarn(Resources resources) {
        Log.d(TAG, "SceneCowBarn.cropImageCowBarn(Resources resources)");

        Bitmap cowBarnEmpty = BitmapFactory.decodeResource(resources, R.drawable.scene_cow_barn);
//        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
//        Bitmap cowBarnEmpty = null;
//
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        cowBarnEmpty = Bitmap.createBitmap(indoorsFarmHM2, 8, 304, 240, 256);
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        Log.d(TAG, "Bitmap cowBarnEmpty's (width, height): " + cowBarnEmpty.getWidth() + ", " + cowBarnEmpty.getHeight());

        return cowBarnEmpty;
    }

    private Map<String, Rect> createTransferPointsForCowBarn() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (15 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (4 * Tile.WIDTH), (15 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForCowBarn() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForCowBarn() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}