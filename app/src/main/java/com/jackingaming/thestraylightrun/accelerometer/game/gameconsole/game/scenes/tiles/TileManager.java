package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileManager
        implements Serializable {
    transient private Game game;
//    public static final int NUMBER_OF_COLUMNS_DEFAULT = 40;
//    public static final int NUMBER_OF_ROWS_DEFAULT = 40;

    private int numberOfColumns;
    private int numberOfRows;
    private Tile[][] tiles;
    transient private Map<String, Rect> transferPoints;

    public TileManager() {
//        numberOfColumns = NUMBER_OF_COLUMNS_DEFAULT;
//        numberOfRows = NUMBER_OF_ROWS_DEFAULT;
//        tiles = new Tile[numberOfRows][numberOfColumns];
//
//        for (int yIndex = 0; yIndex < numberOfRows; yIndex++) {
//            for (int xIndex = 0; xIndex < numberOfColumns; xIndex++) {
//                // walkable is true by default (no image).
//                tiles[yIndex][xIndex] = new Tile();
//            }
//        }
    }

    private boolean isWithinBounds(int xIndex, int yIndex) {
        return ((xIndex >= 0) && (xIndex < tiles[0].length) &&
                (yIndex >= 0) && (yIndex < tiles.length));
    }

    public List<Tile> determineWalkableNeighbors(Tile tile, Tile tileDest) {
        List<Tile> tileNeighbors = new ArrayList<>();
        int xIndexUp = tile.getxIndex();
        int yIndexUp = tile.getyIndex() - 1;
        int xIndexDown = tile.getxIndex();
        int yIndexDown = tile.getyIndex() + 1;
        int xIndexLeft = tile.getxIndex() - 1;
        int yIndexLeft = tile.getyIndex();
        int xIndexRight = tile.getxIndex() + 1;
        int yIndexRight = tile.getyIndex();

        if (isWithinBounds(xIndexUp, yIndexUp)) {
            Tile tileUp = tiles[yIndexUp][xIndexUp];
            if (tileUp.equals(tileDest)) {
                tileNeighbors.add(tileUp);
            } else if (tileUp.isWalkable()) {
                boolean isFreeOfEntityCollision = true;
                List<Entity> entities = game.getSceneManager().getCurrentScene().getEntityManager().getEntities();
                for (Entity e : entities) {
                    if (e instanceof Plant) {
                        continue;
                    }

                    int xIndex = (int) e.getX() / Tile.WIDTH;
                    int yIndex = (int) e.getY() / Tile.HEIGHT;

                    if (xIndex == tileUp.getxIndex() && yIndex == tileUp.getyIndex()) {
                        isFreeOfEntityCollision = false;
                    }
                }

                if (isFreeOfEntityCollision) {
                    tileNeighbors.add(tileUp);
                }
            }
        }

        if (isWithinBounds(xIndexDown, yIndexDown)) {
            Tile tileDown = tiles[yIndexDown][xIndexDown];
            if (tileDown.equals(tileDest)) {
                tileNeighbors.add(tileDown);
            } else if (tileDown.isWalkable()) {
                boolean isFreeOfEntityCollision = true;
                List<Entity> entities = game.getSceneManager().getCurrentScene().getEntityManager().getEntities();
                for (Entity e : entities) {
                    if (e instanceof Plant) {
                        continue;
                    }

                    int xIndex = (int) e.getX() / Tile.WIDTH;
                    int yIndex = (int) e.getY() / Tile.HEIGHT;

                    if (xIndex == tileDown.getxIndex() && yIndex == tileDown.getyIndex()) {
                        isFreeOfEntityCollision = false;
                    }
                }

                if (isFreeOfEntityCollision) {
                    tileNeighbors.add(tileDown);
                }
            }
        }

        if (isWithinBounds(xIndexLeft, yIndexLeft)) {
            Tile tileLeft = tiles[yIndexLeft][xIndexLeft];
            if (tileLeft.equals(tileDest)) {
                tileNeighbors.add(tileLeft);
            } else if (tileLeft.isWalkable()) {
                boolean isFreeOfEntityCollision = true;
                List<Entity> entities = game.getSceneManager().getCurrentScene().getEntityManager().getEntities();
                for (Entity e : entities) {
                    if (e instanceof Plant) {
                        continue;
                    }

                    int xIndex = (int) e.getX() / Tile.WIDTH;
                    int yIndex = (int) e.getY() / Tile.HEIGHT;

                    if (xIndex == tileLeft.getxIndex() && yIndex == tileLeft.getyIndex()) {
                        isFreeOfEntityCollision = false;
                    }
                }

                if (isFreeOfEntityCollision) {
                    tileNeighbors.add(tileLeft);
                }
            }
        }

        if (isWithinBounds(xIndexRight, yIndexRight)) {
            Tile tileRight = tiles[yIndexRight][xIndexRight];
            if (tileRight.equals(tileDest)) {
                tileNeighbors.add(tileRight);
            } else if (tileRight.isWalkable()) {
                boolean isFreeOfEntityCollision = true;
                List<Entity> entities = game.getSceneManager().getCurrentScene().getEntityManager().getEntities();
                for (Entity e : entities) {
                    if (e instanceof Plant) {
                        continue;
                    }

                    int xIndex = (int) e.getX() / Tile.WIDTH;
                    int yIndex = (int) e.getY() / Tile.HEIGHT;

                    if (xIndex == tileRight.getxIndex() && yIndex == tileRight.getyIndex()) {
                        isFreeOfEntityCollision = false;
                    }
                }

                if (isFreeOfEntityCollision) {
                    tileNeighbors.add(tileRight);
                }
            }
        }

        return tileNeighbors;
    }

    // TODO: account for entity-collision.
    public List<Tile> doesExistPath(Tile src, Tile dest) {
        List<List<Tile>> queueSearch = new ArrayList<>();
        List<Tile> visited = new ArrayList<>();
        List<Tile> pathToDest = new ArrayList<>();
        pathToDest.add(src);
        queueSearch.add(pathToDest);

        while (!queueSearch.isEmpty()) {
            pathToDest = queueSearch.get(0);
            // get last element the current list stored in queueSearch.
            Tile tile = pathToDest.get(pathToDest.size() - 1);

            if (tile.getxIndex() == dest.getxIndex() &&
                    tile.getyIndex() == dest.getyIndex()) {
                Log.e("TileManager", "FOUND!!!");

                for (Tile tilePath : pathToDest) {
                    Log.e("tilePath: ", tilePath.getxIndex() + ", " + tilePath.getyIndex());
                }

                return pathToDest;
            } else {
                Log.e("TileManager", "adding neighbors");
                List<Tile> tilesNeighbor = determineWalkableNeighbors(tile, dest);
                Log.e("TileManager", "tilesNeighbor.size(): " + tilesNeighbor.size());

                for (Tile tileNeighbor : tilesNeighbor) {
                    if (!visited.contains(tileNeighbor)) {
                        visited.add(tileNeighbor);

                        List<Tile> pathToNextTile = new ArrayList<>();
                        pathToNextTile.addAll(pathToDest);
                        pathToNextTile.add(tileNeighbor);
                        queueSearch.add(pathToNextTile);
                    }
                }
            }

            Log.e("TileManager", "removing: " + tile.xIndex + ", " + tile.getyIndex());
            queueSearch.remove(0);
        }

        Log.e("TileManager", "NOT FOUND!!!");
        return pathToDest;
    }

    public void loadTransferPoints(Map<String, Rect> transferPointsToBeLoaded) {
        transferPoints = transferPointsToBeLoaded;
    }

    public void loadTiles(Tile[][] tilesToBeLoaded) {
        tiles = tilesToBeLoaded;
    }

    public void init(Game game) {
        this.game = game;

        // If tile instantiation is dependent on external files
        // (e.g. pocket critters world map scenes), instantiate in
        // init(Game) instead of TileManager's constructor.
        //
        // -This may lead to issues where it'll always use default
        // tile configurations when loading (only matters for game where
        // tile state is important [e.g. pooh farmer]).
//        tiles = TileLoaderWorldMapPart01.createWorldMapPart01(game);
//        transferPoints = TileLoaderWorldMapPart01.createWorldMapPart01TransferPoints();

//        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.custom_hm_tile_sprites_sheet);
//        Bitmap fence = Bitmap.createBitmap(spriteSheet, 1 * Tile.WIDTH, 5 * Tile.HEIGHT, Tile.WIDTH, Tile.HEIGHT);
//
//        for (int yIndex = 0; yIndex < numberOfRows; yIndex++) {
//            for (int xIndex = 0; xIndex < numberOfColumns; xIndex++) {
//                Tile tile = tiles[yIndex][xIndex];
//                // Edge tiles have their walkable set to false (have image).
//                if ((yIndex == 0) || (yIndex == (numberOfRows - 1)) || (xIndex == 0) || (xIndex == (numberOfColumns - 1))) {
//                    tile.setWalkable(false);
//                    //////////////////////////////////////////
//                    tile.init(game, xIndex, yIndex, fence);
//                    //////////////////////////////////////////
//                } else {
//                    ////////////////////////////////////////////////
//                    tile.init(game, xIndex, yIndex, null);
//                    ////////////////////////////////////////////////
//                }
//            }
//        }
    }

    public void draw(Canvas canvas) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // RENDERING EFFICIENCY from youtube's CodeNMore NEW Beginner 2D Game Programming series. //
        ////////////////////////////////////////////////////////////////////////////////////////////
        int columns = tiles[0].length;
        int rows = tiles.length;
        int xStart = (int) Math.max(0, GameCamera.getInstance().getX() / Tile.WIDTH);
        int xEnd = (int) Math.min(columns, ((GameCamera.getInstance().getX() +
                GameCamera.getInstance().getClipWidthInPixel()) / Tile.WIDTH) + 2);     //+2 fixes visual glitch.
        int yStart = (int) Math.max(0, GameCamera.getInstance().getY() / Tile.HEIGHT);
        int yEnd = (int) Math.min(rows, ((GameCamera.getInstance().getY() +
                GameCamera.getInstance().getClipHeightInPixel()) / Tile.HEIGHT) + 2);   //+2 fixes visual glitch.
        ////////////////////////////////////////////////////////////////////////////////////////////
        // RENDERING EFFICIENCY from youtube's CodeNMore NEW Beginner 2D Game Programming series. //
        ////////////////////////////////////////////////////////////////////////////////////////////

        // RENDER TILES
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                tiles[y][x].draw(canvas);
            }
        }
    }

    public boolean isSolid(int xInPixel, int yInPixel) {
        int widthScene = tiles[0].length * Tile.WIDTH;
        int heightScene = tiles.length * Tile.HEIGHT;
        //CHECK BEYOND SCENE BOUND (e.g. moving off map)
        if ((xInPixel < 0) || (xInPixel >= widthScene) ||
                (yInPixel < 0) || (yInPixel >= heightScene)) {
            return true;
        }

        int yIndex = yInPixel / Tile.HEIGHT;
        int xIndex = xInPixel / Tile.WIDTH;
        //CHECK Tile.walkable
        Tile tile = tiles[yIndex][xIndex];
        if (tile.isWalkable()) {
            return false;
        }

        //DEFAULT IS SOLID (walkable is false)
        return true;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getWidthScene() {
        return (tiles[0].length * Tile.WIDTH);
    }

    public int getHeightScene() {
        return (tiles.length * Tile.HEIGHT);
    }

    public Rect getTransferPointBounds(String key) {
        Rect transferPointBounds = null;
        if (transferPoints.keySet().contains(key)) {
            // Create new object to prevent transfer-point-flattening bug:
            // Rect.intersect(Rect) - "If the specified rectangle intersects this rectangle,
            // return true and set this rectangle to that intersection, otherwise
            // return false and do not change this rectangle."
            transferPointBounds = new Rect(transferPoints.get(key));
        }
        return transferPointBounds;
    }

    public Set<String> getTransferPointsKeySet() {
        return transferPoints.keySet();
    }
}