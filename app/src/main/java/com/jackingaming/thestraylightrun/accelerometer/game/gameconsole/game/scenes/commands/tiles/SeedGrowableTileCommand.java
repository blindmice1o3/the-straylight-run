package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

public class SeedGrowableTileCommand
        implements TileCommand {
    public static final String TAG = SeedGrowableTileCommand.class.getSimpleName();

    private Game game;
    private Tile tile;
    private String idSeed;

    public SeedGrowableTileCommand(Tile tile, String idSeed) {
        this.tile = tile;
        if (idSeed != null) {
            this.idSeed = idSeed;
        }
    }

    @Override
    public void init(Game game) {
        this.game = game;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public boolean execute() {
        if (tile instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tile;
            Log.e(TAG, "growableTile has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.TILLED) {

                switch (game.getTimeManager().getSeason()) {
                    case SPRING:
                        if (idSeed.equals(game.getContext().getString(R.string.text_seed_banana)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_guava)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_longan)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_papaya)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_mystery)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_tomato)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_onion)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_bitter_melon)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_strawberry))) {
                            growableTile.changeToUnwatered();
                            growableTile.changeToSeeded(idSeed);
                            game.playSFX(SoundManager.sfxSow);
                            return true;
                        } else {
                            Log.e(TAG, "SPRING is the wrong season to plant these seeds: " + idSeed);
                        }
                        break;
                    case SUMMER:
                        if (idSeed.equals(game.getContext().getString(R.string.text_seed_banana)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_guava)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_longan)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_papaya)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_mystery)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_tomato)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_carrot)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_radish)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_corn)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_lemongrass))) {
                            growableTile.changeToUnwatered();
                            growableTile.changeToSeeded(idSeed);
                            game.playSFX(SoundManager.sfxSow);
                            return true;
                        } else {
                            Log.e(TAG, "SUMMER is the wrong season to plant these seeds: " + idSeed);
                        }
                        break;
                    case FALL:
                        if (idSeed.equals(game.getContext().getString(R.string.text_seed_banana)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_guava)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_longan)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_papaya)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_mystery)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_tomato)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_garlic)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_peanut)) ||
                                idSeed.equals(game.getContext().getString(R.string.text_seed_eggplant))) {
                            growableTile.changeToUnwatered();
                            growableTile.changeToSeeded(idSeed);
                            game.playSFX(SoundManager.sfxSow);
                            return true;
                        } else {
                            Log.e(TAG, "FALL is the wrong season to plant these seeds: " + idSeed);
                        }
                        break;
                    case WINTER:
                        Log.e(TAG, "WINTER is a season where you CANNOT plant seeds outdoor.");
                        break;
                }
            }
        }

        return false;
    }

    public void setIdSeed(String idSeed) {
        this.idSeed = idSeed;
    }
}
