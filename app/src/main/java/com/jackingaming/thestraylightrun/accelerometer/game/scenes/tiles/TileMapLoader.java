package com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Modified copy of Util class from previous IntelliJ project (desktop pc version)
 * of PocketCritters-SerialCritterNabbing.
 * <p>
 * Modifying for this Android project (e.g. instead of loading from a
 * String path, need to load from an int resId).
 */
public class TileMapLoader {
    public static final String TAG = TileMapLoader.class.getSimpleName();

    public static String loadFileAsString(Resources resources, int resId) {
        Log.d(TAG, "loadFileAsString(Resources, int)");

        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        ///////////////////////////////////////////////////////////
        InputStream inputStream = resources.openRawResource(resId);
        ///////////////////////////////////////////////////////////

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            // As long as the next line is NOT null, append the
            // line (plus a newline character "\n") to StringBuilder.
            while ((line = bufferedReader.readLine()) != null) {
                ///////////////////////
                sb.append(line + "\n");
                ///////////////////////
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d(TAG, "Unable to read file (resId): " + resId);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Unable to close file (resId): " + resId);
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d(TAG, "BufferedReader is null. File was probably never opened (resId): " + resId);
            }
        }

        return sb.toString();
    }


    /**
     * Responsible for converting the result of "loadFileAsString(Resources, int)" into
     * a multi-dimensional array of tile-IDs that represents the scene.
     * <p>
     * In the "tiles_world_map.txt" file, we separated each tile sprite by
     * a "space" or a "newline character".
     * <p>
     * We'll call "split()" on the String loaded from the "tiles_world_map.txt" file and
     * store each tile-sprite-representation in a String[] name "tokens".
     * <p>
     * To split on any amount of white space, use "\\s+" as the argument to "split()".
     */
    public static Tile[][] convertStringToTileIDs(String stringOfTiles, Bitmap fullWorldMap) {
        Log.d(TAG, "convertStringToTileIDs(String)");

        String[] tokens = stringOfTiles.split("\\s+");

        // Set the width and height of our world, in terms of number of tiles (NOT PIXELS).
        int columns = Integer.parseInt(tokens[0]);
        int rows = Integer.parseInt(tokens[1]);

        // Now every single number after this is actual world-data. We have to read all
        // of this data into a Tile[][] and return it to the TileMap class.
        Tile[][] tiles = new Tile[columns][rows];

        // Here's where it gets a bit tricky. The "tokens" array is 1-D while "tiles" is 2-D.
        //
        // We have to convert the x and y for-loop position into the proper position of
        // our "tokens" array. Almost like "carrying-over-to-a-new-place-value" when adding
        // two numbers, "((y * columns) + x)" will appropriately convert the x and y of the
        // for-loop into the 1-dimensional array index.
        //
        // BUT ALSO HAVE TO "+ 2" because we are setting the first 2 elements in the
        // "tiles_world_map.txt" file (array indexes [0] and [1]) as width and height values.
        int xOffsetInit = 0;
        int heightTile = 16;
        int widthTile = 16;
        int yOffset = 0;
        int xOffset = xOffsetInit;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                // TODO: grab subimage
                Bitmap spriteTile = Bitmap.createBitmap(fullWorldMap,
                        xOffset, yOffset, widthTile, heightTile);

                // UniqueSolidTile (television)
                if (tokens[((y * columns) + x) + 2].equals("3")) {
                    tiles[x][y] = new UniqueSolidTile(UniqueSolidTile.TELEVISION, spriteTile);
                }
                // UniqueSolidTile (computer)
                else if (tokens[((y * columns) + x) + 2].equals("4")) {
                    tiles[x][y] = new UniqueSolidTile(UniqueSolidTile.COMPUTER, spriteTile);
                }
                // UniqueSolidTile (game console)
                else if (tokens[((y * columns) + x) + 2].equals("5")) {
                    tiles[x][y] = new UniqueSolidTile(UniqueSolidTile.GAME_CONSOLE, spriteTile);
                }
                // SolidTile
                else if (tokens[((y * columns) + x) + 2].equals("1") ||
                        tokens[((y * columns) + x) + 2].equals("9")) {
                    tiles[x][y] = new SolidTile(spriteTile);
                }
                // WalkableTile
                else if (tokens[((y * columns) + x) + 2].equals("0") ||
                        tokens[((y * columns) + x) + 2].equals("2")) {
                    tiles[x][y] = new WalkableTile(spriteTile);
                }

                // TODO: increment yOffset
                xOffset += widthTile;
            }
            // TODO: increment xOffset, reset yOffset
            xOffset = xOffsetInit;
            yOffset += heightTile;
        }

        return tiles;
    }
}
