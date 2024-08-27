package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.CollidingOrbit;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.Eel;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.seedshop.SeedShopDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.time.TimeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneFarm extends Scene {
    public static final String TAG = SceneFarm.class.getSimpleName();

    private static final int X_INDEX_SPAWN_PLAYER_DEFAULT = 4;
    private static final int Y_INDEX_SPAWN_PLAYER_DEFAULT = 4;
    private static final int X_INDEX_SPAWN_ROBOT = 7;
    private static final int Y_INDEX_SPAWN_ROBOT = 4;
    private static final int X_INDEX_SPAWN_EEL_NEAR_COWBARN = 11;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_COWBARN = 7;
    private static final int X_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP = 18;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP = 9;
    private static final int X_INDEX_SPAWN_EEL_NEAR_SEEDSHOP = 6;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_SEEDSHOP = 15;
    private static final int X_INDEX_SPAWN_EEL_NEAR_HOTHOUSE = 20;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_HOTHOUSE = 17;
    private static final int PATROL_LENGTH_EEL = 3 * Tile.WIDTH;
    private static final int X_INDEX_SPAWN_COLLIDING_ORBIT = X_INDEX_SPAWN_PLAYER_DEFAULT + 2;
    private static final int Y_INDEX_SPAWN_COLLIDING_ORBIT = Y_INDEX_SPAWN_PLAYER_DEFAULT;
    private static SceneFarm uniqueInstance;

    private boolean inSeedShopState;
    private SeedShopDialogFragment seedShopDialogFragment;

    private List<GrowableTile> growableTiles;
    private ShippingBinTile.IncomeListener shippingBinIncomeListener;

    private SceneFarm() {
        super();
        List<Entity> entitiesForFarm = createEntitiesForFarm();
        entityManager.loadEntities(entitiesForFarm);
        List<Item> itemsForFarm = createItemsForFarm();
        itemManager.loadItems(itemsForFarm);

        inSeedShopState = false;
        seedShopDialogFragment = new SeedShopDialogFragment();

        growableTiles = new ArrayList<>();

        shippingBinIncomeListener = new ShippingBinTile.IncomeListener() {
            @Override
            public void incrementCurrency(float amountToIncrement) {
                game.incrementCurrency(amountToIncrement);
            }
        };
    }

    private boolean newDay = false;

    public void startNewDay() {
        Log.e(TAG, "startNewDay()");
        newDay = true;

        for (GrowableTile growableTile : growableTiles) {
            growableTile.startNewDay();
        }
    }

    public boolean isInSeedShopState() {
        return inSeedShopState;
    }

    public void setInSeedShopState(boolean inSeedShopState) {
        this.inSeedShopState = inSeedShopState;
    }

    public SeedShopDialogFragment getSeedShopDialogFragment() {
        return seedShopDialogFragment;
    }

    public static SceneFarm getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneFarm();
        }
        return uniqueInstance;
    }

    public static void setInstance(SceneFarm sceneFarm) {
        uniqueInstance = sceneFarm;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                addSwarmOfEel();
            }
        }, 7, 0, false);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                removeSwarmOfEel();
            }
        }, 9, 0, false);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                addSwarmOfEel();
            }
        }, 4, 0, true);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                removeSwarmOfEel();
            }
        }, 6, 0, true);

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForFarm = createAndInitTiles(game);
        tileManager.loadTiles(tilesForFarm);
        Map<String, Rect> transferPointsForFarm = createTransferPointsForFarm();
        tileManager.loadTransferPoints(transferPointsForFarm); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        // Set up GrowableTile in front of player's house with a [plant].
        Tile tileInitializedForHarvesting1 = tilesForFarm[17][11];
        if (tileInitializedForHarvesting1 instanceof GrowableTile) {
            ((GrowableTile) tileInitializedForHarvesting1).changeToSeeded(MysterySeed.TAG);
            ((GrowableTile) tileInitializedForHarvesting1).germinateSeed();
        }
        Tile tileInitializedForHarvesting2 = tilesForFarm[17][12];
        if (tileInitializedForHarvesting2 instanceof GrowableTile) {
            ((GrowableTile) tileInitializedForHarvesting2).changeToSeeded(MysterySeed.TAG);
            ((GrowableTile) tileInitializedForHarvesting2).germinateSeed();
        }
        Tile tileInitializedForHarvesting3 = tilesForFarm[17][13];
        if (tileInitializedForHarvesting3 instanceof GrowableTile) {
            ((GrowableTile) tileInitializedForHarvesting3).changeToSeeded(MysterySeed.TAG);
            ((GrowableTile) tileInitializedForHarvesting3).germinateSeed();
        }

        for (int y = 0; y < tilesForFarm.length; y++) {
            for (int x = 0; x < tilesForFarm[y].length; x++) {
                Tile tile = tilesForFarm[y][x];
                if (tile instanceof GrowableTile) {
                    growableTiles.add(((GrowableTile) tile));
                }
            }
        }
        Log.e(TAG, "growableTiles.size() is " + growableTiles.size());

        entityManager.init(game);
        itemManager.init(game);

        // Age the [plant] so it's almost harvestable
        // (has to be done after Entity.init(), which sets ageInDays to 0).
        if (tileInitializedForHarvesting1 instanceof GrowableTile) {
            Plant plant1 = (Plant) ((GrowableTile) tileInitializedForHarvesting1).getEntity();
            Plant plant2 = (Plant) ((GrowableTile) tileInitializedForHarvesting2).getEntity();
            Plant plant3 = (Plant) ((GrowableTile) tileInitializedForHarvesting3).getEntity();
            for (int i = 0; i < 6; i++) {
                Log.e(TAG, "incrementing age: " + i);
                plant1.incrementAgeInDays();
                plant2.incrementAgeInDays();
                plant3.incrementAgeInDays();
            }
        }

        seedShopDialogFragment.init(game);

        if (needDisplaySeedShopFragment) {
            needDisplaySeedShopFragment = false;
            showSeedShopFragment();
        }
    }

    @Override
    public void update(long elapsed) {
        super.update(elapsed);

        if (newDay) {
            Log.e(TAG, "NEW DAY");
            newDay = false;

            removeSwarmOfEel();
        }
    }

    public void showSeedShopFragment() {
        game.getTimeManager().setIsPaused(true);
        inSeedShopState = true;

        game.getViewportListener().showFragmentAndHideSurfaceView(seedShopDialogFragment);
    }

    public void removeSeedShopFragment() {
        game.getTimeManager().setIsPaused(false);
        inSeedShopState = false;
        game.getViewportListener().showSurfaceView();
    }

    private boolean needDisplaySeedShopFragment;

    public boolean isNeedDisplaySeedShopFragment() {
        return needDisplaySeedShopFragment;
    }

    public void setNeedDisplaySeedShopFragment(boolean needDisplaySeedShopFragment) {
        this.needDisplaySeedShopFragment = needDisplaySeedShopFragment;
    }

    @Override
    public List<Object> exit() {
        List<Object> args = super.exit();
        args.add(1, TAG);
        return args;
    }

    @Override
    public void enter(List<Object> args) {
        super.enter(args);
        game.getTimeManager().setIsPaused(false);

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_INDEX_SPAWN_PLAYER_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_INDEX_SPAWN_PLAYER_DEFAULT * Tile.HEIGHT);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
        }
        GameCamera.getInstance().update(0L);
    }

    private Bitmap applyTwilightLightingColorFilter(Bitmap bitmapDaylight) {
        Paint paintTintTwilight = new Paint();
        paintTintTwilight.setColorFilter(new LightingColorFilter(0xFFFFF000, 0x00000000));
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap twilightVersion = Bitmap.createBitmap(bitmapDaylight.getWidth(), bitmapDaylight.getHeight(), Bitmap.Config.ARGB_8888);
        ////////////////////////////////////////////////////////////////////////////////////////////
        Canvas canvasTwilight = new Canvas(twilightVersion);
        canvasTwilight.drawBitmap(bitmapDaylight, 0, 0, paintTintTwilight);
        return twilightVersion;
    }

    private Bitmap applyNightLightingColorFilter(Bitmap bitmapDaylight) {
        Paint paintTintNight = new Paint();
        paintTintNight.setColorFilter(new LightingColorFilter(0xFF00FFFF, 0x00000000));
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap nightVersion = Bitmap.createBitmap(bitmapDaylight.getWidth(), bitmapDaylight.getHeight(), Bitmap.Config.ARGB_8888);
        ////////////////////////////////////////////////////////////////////////////////////////////
        Canvas canvasNight = new Canvas(nightVersion);
        canvasNight.drawBitmap(bitmapDaylight, 0, 0, paintTintNight);
        return nightVersion;
    }

    private Bitmap backgroundDaylight, backgroundTwilight, backgroundNight;
    private Bitmap shippingBinQ1Daylight, shippingBinQ1Twilight, shippingBinQ1Night;
    private Bitmap shippingBinQ2Daylight, shippingBinQ2Twilight, shippingBinQ2Night;
    private Bitmap shippingBinQ3Daylight, shippingBinQ3Twilight, shippingBinQ3Night;
    private Bitmap shippingBinQ4Daylight, shippingBinQ4Twilight, shippingBinQ4Night;

    private void initBackgroundImages(TimeManager.Season season) {
        Log.e(TAG, "initBackgroundImages()");

        //DAYLIGHT BACKGROUND
        /////////////////////////////////////////////////////////////////////////////
        backgroundDaylight = cropImageFarm(game.getContext().getResources(), season);
        /////////////////////////////////////////////////////////////////////////////

        //TWILIGHT BACKGROUND
        //////////////////////////////////////////////////////////////////////////
        backgroundTwilight = applyTwilightLightingColorFilter(backgroundDaylight);
        //////////////////////////////////////////////////////////////////////////

        //NIGHT BACKGROUND
        ////////////////////////////////////////////////////////////////////
        backgroundNight = applyNightLightingColorFilter(backgroundDaylight);
        ////////////////////////////////////////////////////////////////////
    }

    public void updateTilesBySeason(TimeManager.Season season, TimeManager.ModeOfDay modeOfDay) {
        initBackgroundImages(season);
        updateTilesByModeOfDay(modeOfDay);
    }

    public void updateTilesByModeOfDay(TimeManager.ModeOfDay modeOfDay) {
        Bitmap imageFarm = null;
        Bitmap imageShippingBinQ1 = null;
        Bitmap imageShippingBinQ2 = null;
        Bitmap imageShippingBinQ3 = null;
        Bitmap imageShippingBinQ4 = null;
        switch (modeOfDay) {
            case DAYLIGHT:
                imageFarm = backgroundDaylight;
                imageShippingBinQ1 = shippingBinQ1Daylight;
                imageShippingBinQ2 = shippingBinQ2Daylight;
                imageShippingBinQ3 = shippingBinQ3Daylight;
                imageShippingBinQ4 = shippingBinQ4Daylight;
                break;
            case TWILIGHT:
                imageFarm = backgroundTwilight;
                imageShippingBinQ1 = shippingBinQ1Twilight;
                imageShippingBinQ2 = shippingBinQ2Twilight;
                imageShippingBinQ3 = shippingBinQ3Twilight;
                imageShippingBinQ4 = shippingBinQ4Twilight;
                break;
            case NIGHT:
                imageFarm = backgroundNight;
                imageShippingBinQ1 = shippingBinQ1Night;
                imageShippingBinQ2 = shippingBinQ2Night;
                imageShippingBinQ3 = shippingBinQ3Night;
                imageShippingBinQ4 = shippingBinQ4Night;
                break;
        }

        Tile[][] tiles = tileManager.getTiles();
        int rows = tiles.length;
        Log.e(TAG, "rows: " + rows);
        int columns = tiles[0].length;
        Log.e(TAG, "columns: " + columns);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Tile tile = tiles[y][x];

                Bitmap tileSprite = null;
                if (tile instanceof ShippingBinTile) {
                    if (x == xIndexShippingBinQ1 && y == yIndexShippingBinQ1) {
                        tileSprite = imageShippingBinQ1;
                    } else if (x == xIndexShippingBinQ2 && y == yIndexShippingBinQ2) {
                        tileSprite = imageShippingBinQ2;
                    } else if (x == xIndexShippingBinQ3 && y == yIndexShippingBinQ3) {
                        tileSprite = imageShippingBinQ3;
                    } else if (x == xIndexShippingBinQ4 && y == yIndexShippingBinQ4) {
                        tileSprite = imageShippingBinQ4;
                    }

                    tile.setImage(tileSprite);
                } else {
                    int xInPixel = x * Tile.WIDTH;
                    int yInPixel = y * Tile.HEIGHT;
                    int widthInPixel = Tile.WIDTH;
                    int heightInPixel = Tile.HEIGHT;
                    tileSprite = Bitmap.createBitmap(imageFarm, xInPixel, yInPixel, widthInPixel, heightInPixel);

                    if (tile instanceof GrowableTile) {
                        ((GrowableTile) tile).updateImageForStateUntilled(tileSprite);
                    } else {
                        tile.setImage(tileSprite);
                    }
                }
            }
        }
    }

    private int xIndexShippingBinQ1, yIndexShippingBinQ1;
    private int xIndexShippingBinQ2, yIndexShippingBinQ2;
    private int xIndexShippingBinQ3, yIndexShippingBinQ3;
    private int xIndexShippingBinQ4, yIndexShippingBinQ4;

    private Tile[][] createAndInitTiles(Game game) {
        //rgbTileMapFarm is an image where each pixel represents a tile.
        Bitmap rgbTileMapFarm = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.tile_map_farm);
        int columns = rgbTileMapFarm.getWidth();            //Always need.
        int rows = rgbTileMapFarm.getHeight();              //Always need.

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Tile[][] tiles = new Tile[rows][columns];           //Always need.
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        initBackgroundImages(game.getTimeManager().getSeason());
        Bitmap imageFarm = backgroundDaylight;
        //DEFINE EACH ELEMENT.
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;
                Bitmap tileSprite = Bitmap.createBitmap(imageFarm, xInPixel, yInPixel, widthInPixel, heightInPixel);

                int pixel = rgbTileMapFarm.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                if (pixel == Color.BLACK) {
                    Tile tile = new Tile("black");
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new GenericSolidTile(gameCartridge, x, y);
                } else if (pixel == Color.WHITE) {
//                    Tile tile = new Tile("white");
                    Tile tile = new GrowableTile(GrowableTile.TAG, game, new GrowableTile.EntityListener() {
                        @Override
                        public void addEntityToScene(Entity entityToAdd) {
                            entityManager.addEntity(entityToAdd);
                        }
                    });
                    tile.init(game, x, y, tileSprite);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new GrowableGroundTile(gameCartridge, x, y);
                    //tiles[y][x] = new GenericWalkableTile(gameCartridge, x, y);
                } else if (pixel == Color.RED) {
                    Tile tile = new Tile("red");
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new SignPostTile(gameCartridge, x, y);
                } else if (pixel == Color.GREEN) {
                    Tile tile = new Tile("green");
                    tile.init(game, x, y, tileSprite);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new TransferPointTile(gameCartridge, x, y);
                }
                //SHIPPING_BIN_TILE
                else if ((red == 255) && (green == 255) && (blue == 1)) {
                    xIndexShippingBinQ1 = x;
                    yIndexShippingBinQ1 = y;

                    Bitmap shippingBinQ1 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.TOP_LEFT);
                    shippingBinQ1Daylight = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(shippingBinQ1Daylight);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ1, 0, 0, null);

                    shippingBinQ1Twilight = applyTwilightLightingColorFilter(shippingBinQ1Daylight);
                    shippingBinQ1Night = applyNightLightingColorFilter(shippingBinQ1Daylight);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_LEFT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, shippingBinQ1Daylight);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                } else if ((red == 255) && (green == 255) && (blue == 2)) {
                    xIndexShippingBinQ2 = x;
                    yIndexShippingBinQ2 = y;

                    Bitmap shippingBinQ2 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.TOP_RIGHT);
                    shippingBinQ2Daylight = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(shippingBinQ2Daylight);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ2, 0, 0, null);

                    shippingBinQ2Twilight = applyTwilightLightingColorFilter(shippingBinQ2Daylight);
                    shippingBinQ2Night = applyNightLightingColorFilter(shippingBinQ2Daylight);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_RIGHT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, shippingBinQ2Daylight);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                } else if ((red == 255) && (green == 255) && (blue == 3)) {
                    xIndexShippingBinQ3 = x;
                    yIndexShippingBinQ3 = y;

                    Bitmap shippingBinQ3 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.BOTTOM_LEFT);
                    shippingBinQ3Daylight = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(shippingBinQ3Daylight);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ3, 0, 0, null);

                    shippingBinQ3Twilight = applyTwilightLightingColorFilter(shippingBinQ3Daylight);
                    shippingBinQ3Night = applyNightLightingColorFilter(shippingBinQ3Daylight);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_LEFT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, shippingBinQ3Daylight);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                } else if ((red == 255) && (green == 255) && (blue == 4)) {
                    xIndexShippingBinQ4 = x;
                    yIndexShippingBinQ4 = y;

                    Bitmap shippingBinQ4 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT);
                    shippingBinQ4Daylight = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(shippingBinQ4Daylight);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ4, 0, 0, null);

                    shippingBinQ4Twilight = applyTwilightLightingColorFilter(shippingBinQ4Daylight);
                    shippingBinQ4Night = applyNightLightingColorFilter(shippingBinQ4Daylight);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, shippingBinQ4Daylight);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                }
//                //TODO: handle special tiles (stashWood, flowerPlot, hotSpring)
                else if (pixel == Color.BLUE) {
                    Tile tile = new Tile("blue");
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new GenericSolidTile(gameCartridge, x, y);
                } else {
                    Tile tile = new Tile("default");
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                }
            }
        }

        return tiles;
    }

    public static Bitmap cropImageFarm(Resources resources, TimeManager.Season season) {
        Log.d(TAG, "SceneFarm.cropImageFarm(Resources resources, TimeManager.Season season)");

        Bitmap farmSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.gbc_hm3_farm);
        Bitmap croppedImageFarm = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        switch (season) {
            case SPRING:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 14, 39, 384, 400);
                break;
            case SUMMER:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 410, 40, 384, 400);
                break;
            case FALL:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 806, 40, 384, 400);
                break;
            case WINTER:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 1202, 40, 384, 400);
                break;
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap croppedImageFarm's (width, height): " + croppedImageFarm.getWidth() + ", " + croppedImageFarm.getHeight());

        return croppedImageFarm;
    }

    public static Bitmap cropImageShippingBinTile(Resources resources, ShippingBinTile.Quadrant quadrant) {
        Log.d(TAG, "SceneFarm.cropImageShippingBinTile(Resources, ShippingBinTile.Quadrant)");

        Bitmap customTilesSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.custom_hm_tile_sprites_sheet);
        Bitmap shippingBinTile = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        switch (quadrant) {
            case TOP_LEFT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 0, 112, 16, 16);
                break;
            case TOP_RIGHT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 16, 112, 16, 16);
                break;
            case BOTTOM_LEFT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 0, 128, 16, 16);
                break;
            case BOTTOM_RIGHT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 16, 128, 16, 16);
                break;
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "shippingBinTile: " + shippingBinTile.getWidth() + ", " + shippingBinTile.getHeight());

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //May be redundant because local variable.
        customTilesSpriteSheet = null;
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "SceneFarm.cropImageShippingBinTile(Resources, ShippingBinTile.Quadrant)... customTilesSpriteSheet is null? " + customTilesSpriteSheet);

        return shippingBinTile;
    }

    private Map<String, Rect> createTransferPointsForFarm() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("HOUSE_LEVEL_01", new Rect((12 * Tile.WIDTH), (15 * Tile.HEIGHT),
                (12 * Tile.WIDTH) + (1 * Tile.WIDTH), (15 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        transferPoints.put("HOTHOUSE", new Rect((17 * Tile.WIDTH), (15 * Tile.HEIGHT),
                (17 * Tile.WIDTH) + (1 * Tile.WIDTH), (15 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));

        transferPoints.put("SHEEP_PEN", new Rect((21 * Tile.WIDTH), (7 * Tile.HEIGHT),
                (21 * Tile.WIDTH) + (1 * Tile.WIDTH), (7 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        transferPoints.put("CHICKEN_COOP", new Rect((18 * Tile.WIDTH), (5 * Tile.HEIGHT),
                (18 * Tile.WIDTH) + (1 * Tile.WIDTH), (5 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        transferPoints.put("COW_BARN", new Rect((10 * Tile.WIDTH), (4 * Tile.HEIGHT),
                (10 * Tile.WIDTH) + (1 * Tile.WIDTH), (4 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));

        transferPoints.put("SEEDS_SHOP", new Rect((5 * Tile.WIDTH), (11 * Tile.HEIGHT),
                (5 * Tile.WIDTH) + (1 * Tile.WIDTH), (11 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForFarm() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        entities.add(
                new Robot((X_INDEX_SPAWN_ROBOT * Tile.WIDTH),
                        (Y_INDEX_SPAWN_ROBOT * Tile.HEIGHT))
        );
        entities.add(
                new CollidingOrbit((X_INDEX_SPAWN_COLLIDING_ORBIT * Tile.WIDTH),
                        (Y_INDEX_SPAWN_COLLIDING_ORBIT * Tile.HEIGHT),
                        Player.getInstance())
        );
        return entities;
    }

    public void addSwarmOfEel() {
        Eel eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_COWBARN * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_COWBARN * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
        eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
        eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_SEEDSHOP * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_SEEDSHOP * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
        eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_HOTHOUSE * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_HOTHOUSE * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
    }

    public void removeSwarmOfEel() {
        for (Entity e : entityManager.getEntities()) {
            if (e instanceof Eel) {
                e.setActive(false);
            }
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                game.getViewportListener().stopBlinkingBorder();
            }
        });
    }

    private List<Item> createItemsForFarm() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}