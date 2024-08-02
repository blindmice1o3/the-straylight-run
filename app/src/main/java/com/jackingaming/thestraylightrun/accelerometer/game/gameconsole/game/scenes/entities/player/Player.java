package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Player extends Creature {
    private static Player uniqueInstance;
    private Form form;

    private Player(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
        form = new PoohForm();
    }

    public static Player getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Player(
                    (2 * Tile.WIDTH), (3 * Tile.HEIGHT));
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        super.init(game);
        form.init(game);
    }

    @Override
    public void update(long elapsed) {
        form.update(elapsed);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        form.draw(canvas);
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        form.respondToTransferPointCollision(key);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        return form.respondToEntityCollision(e);
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {
        form.respondToItemCollisionViaClick(item);
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        form.respondToItemCollisionViaMove(item);
    }

    public void doCheckItemCollisionViaClick() {
        int xOffset = 0;
        int yOffset = 0;
        switch (direction) {
            case UP:
                xOffset = 0;
                yOffset = -height;
                break;
            case DOWN:
                xOffset = 0;
                yOffset = height;
                break;
            case LEFT:
                xOffset = -width;
                yOffset = 0;
                break;
            case RIGHT:
                xOffset = width;
                yOffset = 0;
                break;
            case CENTER:
                xOffset = 0;
                yOffset = 0;
                break;
            case UP_LEFT:
                xOffset = -width;
                yOffset = -height;
                break;
            case UP_RIGHT:
                xOffset = width;
                yOffset = -height;
                break;
            case DOWN_LEFT:
                xOffset = -width;
                yOffset = height;
                break;
            case DOWN_RIGHT:
                xOffset = width;
                yOffset = height;
                break;
            default:
                return;
        }

        checkItemCollision(xOffset, yOffset, true);
    }

    public Tile checkTileCurrentlyFacing() {
        Tile[][] tiles = game.getSceneManager().getCurrentScene().getTileManager().getTiles();

        float xCenter = x + (Tile.WIDTH / 2);
        float yCenter = y + (Tile.HEIGHT / 2);

        int xIndex = (int) ((xCenter) / Tile.WIDTH);
        int yIndex = (int) ((yCenter) / Tile.HEIGHT);
        switch (direction) {
            case UP:
                yIndex = (int) ((yCenter - (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case DOWN:
                yIndex = (int) ((yCenter + (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case LEFT:
                xIndex = (int) ((xCenter - (1 * Tile.WIDTH)) / Tile.WIDTH);
                break;
            case RIGHT:
                xIndex = (int) ((xCenter + (1 * Tile.WIDTH)) / Tile.WIDTH);
                break;
            case CENTER:
//                xIndex = (int) ((xCenter) / Tile.WIDTH);
//                yIndex = (int) ((yCenter) / Tile.HEIGHT);
                break;
            case UP_LEFT:
                xIndex = (int) ((xCenter - (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter - (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case UP_RIGHT:
                xIndex = (int) ((xCenter + (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter - (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case DOWN_LEFT:
                xIndex = (int) ((xCenter - (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter + (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
            case DOWN_RIGHT:
                xIndex = (int) ((xCenter + (1 * Tile.WIDTH)) / Tile.WIDTH);
                yIndex = (int) ((yCenter + (1 * Tile.HEIGHT)) / Tile.HEIGHT);
                break;
        }

        if ((yIndex < 0) || (yIndex > (tiles.length - 1)) ||
                (xIndex < 0) || (xIndex > (tiles[0].length - 1))) {
            Tile nonWalkableTile = new Tile("x");
            nonWalkableTile.setWalkable(false);
            return nonWalkableTile;
        }

        return tiles[yIndex][xIndex];
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public boolean canAffordToBuy(float price) {
        return game.getCurrency() >= price;
    }

    public void buyItem(Item item) {
        receiveItem(item);
        game.decrementCurrencyBy(item.getPrice());
    }

    public void receiveItem(Item item) {
        game.addItemToBackpack(item);
    }
}