package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemManager
        implements Serializable {
    transient private Game game;

    private List<Item> items;

    public ItemManager() {
        items = new ArrayList<Item>();
    }

    public void loadItems(List<Item> itemsToBeLoaded) {
        items.clear();
        items.addAll(itemsToBeLoaded);
    }

    public void init(Game game) {
        this.game = game;

        for (Item item : items) {
            item.init(game);
        }
    }

    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        for (Item item : items) {
            item.draw(canvas, paintLightingColorFilter);
        }
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }
}
