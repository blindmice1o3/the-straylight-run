package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.CollidingOrbit;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.BugCatchingNet;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.QuestManager;

public class Player extends Creature {
    private static Player uniqueInstance;
    private Form form;
    private QuestManager questManager;

    private Player(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
        form = new PoohForm();
//        form = new MotherForm(MotherForm.DAUGHTER);
        questManager = new QuestManager();
    }

    private int indexOfForm = -1;

    public void toggleForm() {
        indexOfForm++;
        if (indexOfForm > 2) {
            indexOfForm = 0;
        }

        if (indexOfForm == 0) {
            form = new PoohForm();
        } else if (indexOfForm == 1) {
            form = new MotherForm(MotherForm.Type.DAUGHTER);
        } else if (indexOfForm == 2) {
            form = new MotherForm(MotherForm.Type.MOTHER);
        }
        form.init(game);
    }

    public static Player getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Player(
                    (2 * Tile.WIDTH), (3 * Tile.HEIGHT));
        }
        return uniqueInstance;
    }

    public void incrementCurrency(float amountToIncrement) {
        game.incrementCurrency(amountToIncrement);
    }

    public boolean alreadyHaveQuest(String questTAG) {
        return questManager.alreadyHaveQuest(questTAG);
    }

    @Override
    protected boolean skipEntityCollisionCheck(Entity e) {
        return super.skipEntityCollisionCheck(e) ||
                (e instanceof CollidingOrbit);
    }

    @Override
    public void init(Game game) {
        super.init(game);
        form.init(game);

        game.setItemInButtonHolderListener(new Game.ItemInButtonHolderListener() {
            @Override
            public void onChangeItemInButtonHolder(Item itemButtonHolderA, Item itemButtonHolderB) {
                // CollidingOrbit currently off.
                if (game.getSceneManager().getCurrentScene().getEntityManager().getCollidingOrbit() == null) {
                    if ((itemButtonHolderA != null && itemButtonHolderA instanceof BugCatchingNet) ||
                            (itemButtonHolderB != null && itemButtonHolderB instanceof BugCatchingNet)) {
                        // Add CollidingOrbital to EntityManager
                        CollidingOrbit collidingOrbit = new CollidingOrbit(
                                (int) x, (int) (y + (2 * Tile.HEIGHT)),
                                Player.this);
                        collidingOrbit.init(game);
                        game.getSceneManager().getCurrentScene().getEntityManager().addEntity(collidingOrbit);
                    }
                }
                // CollidingOrbit currently on.
                else {
                    if ((itemButtonHolderA == null && itemButtonHolderB == null) ||
                            (itemButtonHolderA == null &&
                                    (itemButtonHolderB != null && !(itemButtonHolderB instanceof BugCatchingNet))) ||
                            (itemButtonHolderB == null &&
                                    (itemButtonHolderA != null && !(itemButtonHolderA instanceof BugCatchingNet))) ||
                            (itemButtonHolderA != null && !(itemButtonHolderA instanceof BugCatchingNet)) &&
                                    (itemButtonHolderB != null && !(itemButtonHolderB instanceof BugCatchingNet))) {
                        // Remove CollidingOrbit from EntityManager.
                        game.getSceneManager().getCurrentScene().getEntityManager().removeCollidingOrbit();
                    }
                }
            }
        });
    }

    @Override
    public void update(long elapsed) {
        form.update(elapsed);
    }

    @Override
    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        super.draw(canvas, paintLightingColorFilter);

        form.draw(canvas, paintLightingColorFilter);
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

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public void setQuestManager(QuestManager questManager) {
        this.questManager = questManager;
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