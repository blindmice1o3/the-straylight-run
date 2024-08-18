package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class ShippingBinTile extends Tile {
    public static final String TAG = ShippingBinTile.class.getSimpleName();

    public enum Quadrant {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;}

    public interface IncomeListener {
        void incrementCurrency(float amountToIncrement);
    }

    private static IncomeListener listener;

    private Quadrant quadrant;

    private static List<Sellable> stash = new ArrayList<Sellable>();

    public ShippingBinTile(String id, Quadrant quadrant, IncomeListener listener) {
        super(id);
        this.quadrant = quadrant;
        this.listener = listener;
    }

    public void addSellable(Sellable sellable) {
        stash.add(sellable);
    }

    public static void sellStash() {
        /////////////////////////////////////////////////////////////////
        int incomeFromStash = ShippingBinTile.calculateIncomeFromStash();
        /////////////////////////////////////////////////////////////////

        listener.incrementCurrency(incomeFromStash);

        //////////////
        stash.clear();
        //////////////
        Log.e(TAG, "sellStash()... POST ShippingBinTile.stash.clear()!!!");
    }

    private static int calculateIncomeFromStash() {
        int total = 0;
        for (Sellable sellable : stash) {
            Log.e(TAG, "calculateIncomeFromStash()... " + sellable.getPrice());
            total += sellable.getPrice();
        }
        Log.e(TAG, "calculateIncomeFromStash()... TOTAL: " + total);
        return total;
    }
}
