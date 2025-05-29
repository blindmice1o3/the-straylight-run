package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import java.io.Serializable;

public class ItemStackable
        implements Serializable {
    public static final String TAG = ItemStackable.class.getSimpleName();

    private Item item;
    private int quantity;

    public ItemStackable(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public void increment() {
        quantity++;
    }

    public void decrement() {
        quantity--;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}
