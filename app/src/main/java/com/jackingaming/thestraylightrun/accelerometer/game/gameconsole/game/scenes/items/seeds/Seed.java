package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.SeedGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;

public abstract class Seed extends Item
        implements TileCommandOwner {
    public static final String TAG = Seed.class.getSimpleName();

    protected TileCommand tileCommand;

    @Override
    public void init(Game game) {
        super.init(game);

        tileCommand = new SeedGrowableTileCommand(null, name);
        tileCommand.init(game);

        Paint paintText = new Paint();
        paintText.setColor(Color.RED);
        paintText.setTextSize(72);
        paintText.setAntiAlias(true);
        Bitmap itemSpriteAndText = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(itemSpriteAndText);
        canvas.drawBitmap(image, 0, 0, paintText);
        canvas.drawText(name,
                (int) (image.getWidth() * 0.17),
                (int) (image.getHeight() * 0.42),
                paintText);

        image = itemSpriteAndText;
    }

    @Override
    protected void initImage() {
        image = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.item_seeds);
//        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
//        image = Bitmap.createBitmap(spriteSheet, 717, 363, 242, 272);

    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}
