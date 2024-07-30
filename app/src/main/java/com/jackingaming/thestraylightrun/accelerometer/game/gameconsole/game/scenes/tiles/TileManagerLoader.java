package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Modified copy of [TileMapLoader] class from package [gameboycolor]; the
 * [TileMapLoader] class is actually a modified copy of [Util] class from previous
 * IntelliJ project (desktop pc version) of PocketCritters-SerialCritterNabbing.
 * <p>
 * Modifying for this Android project (e.g. instead of loading from a
 * String path, need to load from an int resId).
 */
public class TileManagerLoader {
    public static final String TAG = TileManagerLoader.class.getSimpleName();

    public static String loadFileAsString(Resources resources, int resId) {
        Log.d(TAG, "TileLoaderWorldMapPart01.loadFileAsString(Resources resources, int resId)");

        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            ///////////////////////////////////////////////////////////
            InputStream inputStream = resources.openRawResource(resId);
            ///////////////////////////////////////////////////////////
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            // As long as the next line is NOT null, append the
            // line (plus a newline character [\n]) to StringBuilder.
            while ((line = bufferedReader.readLine()) != null) {
                ///////////////////////
                sb.append(line + "\n");
                ///////////////////////
            }
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
            Log.d(TAG, "Resources.NotFoundException - Unable to openRawResource(resId): " + resId);
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d(TAG, "IOException - Unable to read file (resId): " + resId);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOException Unable to close file (resId): " + resId);
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d(TAG, "NullPointerException BufferedReader is null. File was probably never opened (resId): " + resId);
            }
        }

        return sb.toString();
    }

    /**
     * Responsible for converting the result of [loadFileAsString(Resources resources, int resId)]
     * into a multi-dimensional array of [Tile] that represents the scene.
     * <p>
     * In the [tiles_world_map.txt] file, each element (which represents a type of tile)
     * is separated by a [space] or a [newline character].
     * <p>
     * We'll call [split()] on the String loaded from the [tiles_world_map.txt] file and
     * store each element in an array of String named [tokens].
     * <p>
     * To split on any amount of white space, use ["\\s+"] as the argument to [split()].
     */
    public static Tile[][] convertStringToTiles(String stringOfTiles) {
        Log.d(TAG, "TileLoaderWorldMapPart01.convertStringToTiles(String stringOfTiles)");

        String[] tokens = stringOfTiles.split("\\s+");
        // Set the [columnsFullWorldMap] and [rowsFullWorldMap] of our world/scene,
        // in terms of number of tiles (NOT PIXELS).
        int columnsFullWorldMap = Integer.parseInt(tokens[0]);
        int rowsFullWorldMap = Integer.parseInt(tokens[1]);

        // Now every single number after this is actual world-data. We have to read all of this
        // data into a Tile[][] and return it to the TileManager class (or whoever's calling).
        Tile[][] fullWorldMap = new Tile[rowsFullWorldMap][columnsFullWorldMap];
        for (int y = 0; y < rowsFullWorldMap; y++) {
            for (int x = 0; x < columnsFullWorldMap; x++) {
                // Here's where it gets a bit tricky.
                //
                // The [tokens] array is 1-D, while [tiles] nested-array is 2-D.
                //
                // We have to convert the x and y for-loop position into the proper position of
                // the [tokens] array. Almost like "carrying-over-to-a-new-place-value" when adding
                // two numbers, [((y * columns) + x)] will appropriately convert the x and y of the
                // for-loop into the 1-dimensional array index.
                //
                // BUT ALSO HAVE TO [+ 2] because we are setting the first 2 elements in the
                // [tiles_world_map.txt] file (array indexes [0] and [1]) as [columns] and [rows] values.
                String id = tokens[((y * columnsFullWorldMap) + x) + 2];
                fullWorldMap[y][x] = new Tile(id);
            }
        }

        return fullWorldMap;
    }

    public static Tile[][] cropTilesFromFullWorldMap(Resources resources,
                                                     int xStartTileIndex, int yStartTileIndex,
                                                     int xEndTileIndex, int yEndTileIndex) {
        Log.d(TAG, "TileLoaderWorldMapPart01.cropTilesFromFullWorldMap(Resources resources, int xStartTileIndex, int yStartTileIndex, int xEndTileIndex, int yEndTileIndex)");
        // Start with FullWorldMap
        String fullWorldMapLoadedAsString = loadFileAsString(resources, R.raw.tiles_world_map);
        Tile[][] fullWorldMap = convertStringToTiles(fullWorldMapLoadedAsString);

        // Crop according to specified x0, y0, x1, y1 values.
        int columnsCropped = xEndTileIndex - xStartTileIndex;
        int rowsCropped = yEndTileIndex - yStartTileIndex;
        Tile[][] worldMapPart01 = new Tile[rowsCropped][columnsCropped];
        for (int y = yStartTileIndex; y < yEndTileIndex; y++) {
            // Arrays.copyOfRange()'s "from" is inclusive while "to" is exclusive.
            worldMapPart01[y - yStartTileIndex] = Arrays.copyOfRange(fullWorldMap[y], xStartTileIndex, xEndTileIndex);
        }
        return worldMapPart01;
    }

    public static Bitmap cropImageFromFullWorldMap(Resources resources,
                                                   int xStartTileIndex, int yStartTileIndex,
                                                   int xEndTileIndex, int yEndTileIndex) {
        Log.d(TAG, "TileLoaderWorldMapPart01.cropImageFromFullWorldMap(Resources resources, int xStartTileIndex, int yStartTileIndex, int xEndTileIndex, int yEndTileIndex)");

        Bitmap fullWorldMap = BitmapFactory.decodeResource(resources, R.drawable.pokemon_gsc_kanto);
        Bitmap croppedWorldMapPart01 = null;

        // In terms of PIXELS.
        int x = xStartTileIndex * Tile.WIDTH;
        int y = yStartTileIndex * Tile.HEIGHT;
        int widthScene = (xEndTileIndex - xStartTileIndex) * Tile.WIDTH;
        int heightScene = (yEndTileIndex - yStartTileIndex) * Tile.HEIGHT;
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        croppedWorldMapPart01 = Bitmap.createBitmap(fullWorldMap, x, y, widthScene, heightScene);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap croppedWorldMapPart01's (width, height): " + croppedWorldMapPart01.getWidth() + ", " + croppedWorldMapPart01.getHeight());

        return croppedWorldMapPart01;
    }
}