package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.Map;

public class Assets {
    // FONT (from pokemon yellow sprite sheet for battle)
    public static Bitmap fontSpriteSheet;
    public static Bitmap pokeballToken;
    public static Map<String, Bitmap> fontHashMap;

    // SPRITE SHEETS
    public static Bitmap spriteSheetChapterIntroAndWorldMap, spriteSheetChapter1Creatures, spriteSheetStartMenu;

    // BACKGROUND - IntroState
    public static Bitmap chapter1Intro, chapter2Intro, chapter3Intro, chapter4Intro, chapter5Intro;

    // BACKGROUND - ChapterState
    public static Bitmap chapter1Chapter, chapter2Chapter, chapter3Chapter, chapter4Chapter, chapter5Chapter;

    // BACKGROUND - WorldMapState
    public static Bitmap chapter1WorldMap, chapter2WorldMap, chapter3WorldMap, chapter4WorldMap, chapter5WorldMap;

    // TRANSPARENT LAYER - WAVE
    public static Bitmap chapter1Wave, chapter2Wave, chapter3Wave, chapter4Wave, chapter5Wave;
    public static Bitmap[] waveAnimationArray;

    // OVERWORLD - TOKEN
    public static Bitmap upOverworld0, upOverworld1, downOverworld0, downOverworld1,
            leftOverworld0, leftOverworld1, rightOverworld0, rightOverworld1;

    // ENTITIES.MOVEABLE - Chapter 1: FISH HEAD (AND ATTACHMENTS)
    public static Bitmap[][][][][] eatFrames, biteFrames, hurtFrames;

    // ENTITIES.MOVEABLE - Chapter 1: FISH BODY (AND ATTACHMENTS)
    public static Bitmap[][][][] tailOriginal, tailCoelafish, tailTeratisu, tailZinichthy, tailKuraselache;

    // BACKGROUND - MainMenuState
    public static Bitmap mainMenu, mainMenuEVOLUTION;

    // GameStage("") - BACKGROUND
    public static Bitmap chapter1GameStage, brickGreen, coralPink1, coralPink2, coralPink3, coinGameObject;

    // GameStage("buying_lighter")
    public static Bitmap spriteSheetFrogger, backgroundFrogger, winningRow, startingRow,
            logLarge, logMedium, logSmall, parrotRight, carPinkLeft, carWhiteRight, carYellowLeft,
            seaLionRight, bigRigLeft;
    public static Bitmap[] frogRight, frogLeft, frogUp, frogDown, turtleLeft, crocRight, snowPlowRight,
            snakeLeft, frogNPCRight, frogNPCLeft;

    // ENTITIES.NON_MOVEABLE - Chapter 1: KELP
    public static Bitmap[] kelpSolid;

    // ENTITIES.MOVEABLE - Chapter 1: ENEMIES
    public static Bitmap[] seaJelly, eel;

    // ITEMS - Chapter 1: MEAT
    public static Bitmap meat;

    // Initialization of sprites.
    public static void init(Game game) {
        // SPRITE SHEETS
        spriteSheetChapterIntroAndWorldMap = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_maps_and_chapter_images);
        spriteSheetChapter1Creatures = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
        spriteSheetStartMenu = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_select_menu);
        spriteSheetFrogger = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.frogger_entities);
        backgroundFrogger = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.frogger_background);

        // GameStage(Identifier.FROGGER)
        winningRow = Bitmap.createBitmap(spriteSheetFrogger, 0, 55, 399, 52);
        startingRow = Bitmap.createBitmap(spriteSheetFrogger, 0, 119, 399, 34);
        logLarge = Bitmap.createBitmap(spriteSheetFrogger, 7, 166, 177, 21);
        logMedium = Bitmap.createBitmap(spriteSheetFrogger, 7, 198, 116, 21);
        logSmall = Bitmap.createBitmap(spriteSheetFrogger, 7, 230, 84, 21);
        parrotRight = Bitmap.createBitmap(spriteSheetFrogger, 140, 236, 16, 16);
        carPinkLeft = Bitmap.createBitmap(spriteSheetFrogger, 10, 267, 28, 20);
        carWhiteRight = Bitmap.createBitmap(spriteSheetFrogger, 46, 265, 24, 24);
        carYellowLeft = Bitmap.createBitmap(spriteSheetFrogger, 82, 264, 24, 26);
        seaLionRight = Bitmap.createBitmap(spriteSheetFrogger, 116, 271, 32, 18);
        bigRigLeft = Bitmap.createBitmap(spriteSheetFrogger, 106, 302, 46, 18);

        frogRight = new Bitmap[2];
        frogRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 13, 334, 17, 23);
        frogRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 43, 335, 25, 22);
        frogLeft = new Bitmap[2];
        frogLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 83, 335, 17, 23);
        frogLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 112, 338, 25, 22);
        frogUp = new Bitmap[2];
        frogUp[0] = Bitmap.createBitmap(spriteSheetFrogger, 12, 369, 23, 17);
        frogUp[1] = Bitmap.createBitmap(spriteSheetFrogger, 46, 366, 22, 25);
        frogDown = new Bitmap[2];
        frogDown[0] = Bitmap.createBitmap(spriteSheetFrogger, 80, 369, 23, 17);
        frogDown[1] = Bitmap.createBitmap(spriteSheetFrogger, 114, 366, 22, 25);
        turtleLeft = new Bitmap[5];
        turtleLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 15, 406, 31, 22);
        turtleLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 54, 407, 31, 22);
        turtleLeft[2] = Bitmap.createBitmap(spriteSheetFrogger, 94, 408, 29, 19);
        turtleLeft[3] = Bitmap.createBitmap(spriteSheetFrogger, 134, 408, 29, 21);
        turtleLeft[4] = Bitmap.createBitmap(spriteSheetFrogger, 179, 408, 26, 21);
        crocRight = new Bitmap[2];
        crocRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 156, 332, 89, 29);
        crocRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 156, 373, 89, 21);
        snowPlowRight = new Bitmap[3];
        snowPlowRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 11, 301, 23, 21);
        snowPlowRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 42, 301, 23, 21);
        snowPlowRight[2] = Bitmap.createBitmap(spriteSheetFrogger, 73, 301, 23, 21);
        snakeLeft = new Bitmap[4];
        snakeLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 184, 226, 38, 10);
        snakeLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 185, 251, 37, 13);
        snakeLeft[2] = Bitmap.createBitmap(spriteSheetFrogger, 184, 276, 38, 16);
        snakeLeft[3] = Bitmap.createBitmap(spriteSheetFrogger, 185, 304, 37, 13);
        frogNPCRight = new Bitmap[2];
        frogNPCRight[0] = Bitmap.createBitmap(spriteSheetFrogger, 236, 407, 20, 24);
        frogNPCRight[1] = Bitmap.createBitmap(spriteSheetFrogger, 270, 409, 27, 24);
        frogNPCLeft = new Bitmap[2];
        frogNPCLeft[0] = Bitmap.createBitmap(spriteSheetFrogger, 315, 407, 19, 24);
        frogNPCLeft[1] = Bitmap.createBitmap(spriteSheetFrogger, 348, 409, 28, 23);


        // ENTITIES.MOVEABLE - Chapter 1: FISH HEAD (AND ATTACHMENTS)
        eatFrames = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.Jaws.values().length]
                [FishStateManager.ActionState.values().length - 1] //minus 1 because ActionState.NONE doesn't count.
                [3];
        biteFrames = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.Jaws.values().length]
                [FishStateManager.ActionState.values().length - 1]
                [3];
        hurtFrames = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.Jaws.values().length]
                [FishStateManager.ActionState.values().length - 1]
                [2];

        // ENTITIES.MOVEABLE - Chapter 1: FISH BODY (AND ATTACHMENTS)
        tailOriginal = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.FinPectoral.values().length]
                //[FishStateManager.Tail.values().length]
                [3];

        tailCoelafish = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.FinPectoral.values().length]
                //[FishStateManager.Tail.values().length]
                [3];

        tailTeratisu = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.FinPectoral.values().length]
                //[FishStateManager.Tail.values().length]
                [3];

        tailZinichthy = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.FinPectoral.values().length]
                //[FishStateManager.Tail.values().length]
                [3];

        tailKuraselache = new Bitmap[FishStateManager.BodySize.values().length]
                [FishStateManager.BodyTexture.values().length]
                [FishStateManager.FinPectoral.values().length]
                //[FishStateManager.Tail.values().length]
                [3];

        //DECREASE-SLICK-ORIGINAL
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SLICK,
                FishStateManager.Jaws.ORIGINAL, 2, 166, 15, 15);
        //DECREASE-SLICK-KURASELACHES
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SLICK,
                FishStateManager.Jaws.KURASELACHES, 2, 211, 15, 15);
        //DECREASE-SLICK-ZINICHTHY
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SLICK,
                FishStateManager.Jaws.ZINICHTHY, 2, 256, 15, 15);
        //DECREASE-SLICK-SWORDFISH
        // TODO: DECREASE-SLICK-SWORDFISH jaws have much longer width, to-figure-out.

        //DECREASE-SCALY-ORIGINAL
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SCALY,
                FishStateManager.Jaws.ORIGINAL, 80, 166, 15, 15);
        //DECREASE-SCALY-KURASELACHES
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SCALY,
                FishStateManager.Jaws.KURASELACHES, 80, 211, 15, 15);
        //DECREASE-SCALY-ZINICHTHY
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SCALY,
                FishStateManager.Jaws.ZINICHTHY, 80, 256, 15, 15);
        //DECREASE-SCALY-SWORDFISH
        // TODO: DECREASE-SCALY-SWORDFISH jaws have much longer width, to-figure-out.

        //DECREASE-SHELL-ORIGINAL
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SHELL,
                FishStateManager.Jaws.ORIGINAL, 158, 166, 15, 15);
        //DECREASE-SHELL-KURASELACHES
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SHELL,
                FishStateManager.Jaws.KURASELACHES, 158, 211, 15, 15);
        //DECREASE-SHELL-ZINICHTHY
        pullFishHeadImageSubset(FishStateManager.BodySize.DECREASE, FishStateManager.BodyTexture.SHELL,
                FishStateManager.Jaws.ZINICHTHY, 158, 256, 15, 15);
        //DECREASE-SHELL-SWORDFISH
        // TODO: DECREASE-SHELL-SWORDFISH jaws have much longer width, to-figure-out.


        //INCREASE-SLICK-ORIGINAL
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SLICK,
                FishStateManager.Jaws.ORIGINAL, 236, 153, 15, 15);
        //INCREASE-SLICK-KURASELACHES
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SLICK,
                FishStateManager.Jaws.KURASELACHES, 236, 198, 15, 15);
        //INCREASE-SLICK-ZINICHTHY
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SLICK,
                FishStateManager.Jaws.ZINICHTHY, 281, 153, 15, 15);
        //INCREASE-SLICK-SWORDFISH
        // TODO: INCREASE-SHELL-SWORDFISH jaws have much longer width, to-figure-out.

        //INCREASE-SCALY-ORIGINAL
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SCALY,
                FishStateManager.Jaws.ORIGINAL, 338, 153, 15, 15);
        //INCREASE-SCALY-KURASELACHES
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SCALY,
                FishStateManager.Jaws.KURASELACHES, 338, 198, 15, 15);
        //INCREASE-SCALY-ZINICHTHY
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SCALY,
                FishStateManager.Jaws.ZINICHTHY, 383, 153, 15, 15);
        //INCREASE-SCALY-SWORDFISH
        // TODO: INCREASE-SHELL-SWORDFISH jaws have much longer width, to-figure-out.

        //INCREASE-SHELL-ORIGINAL
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SHELL,
                FishStateManager.Jaws.ORIGINAL, 440, 153, 15, 15);
        //INCREASE-SHELL-KURASELACHES
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SHELL,
                FishStateManager.Jaws.KURASELACHES, 440, 198, 15, 15);
        //INCREASE-SHELL-ZINICHTHY
        pullFishHeadImageSubset(FishStateManager.BodySize.INCREASE, FishStateManager.BodyTexture.SHELL,
                FishStateManager.Jaws.ZINICHTHY, 485, 153, 15, 15);
        //INCREASE-SHELL-SWORDFISH
        // TODO: INCREASE-SHELL-SWORDFISH jaws have much longer width, to-figure-out.


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // ENTITIES.MOVEABLE - Chapter 1: FISH BODY (AND ATTACHMENTS)
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //DECREASE-SLICK-ORIGINAL
        pullFishBodyImageSubsetDecreaseOriginal(FishStateManager.BodyTexture.SLICK, 6, 358);
        //DECREASE-SLICK-COELAFISH
        pullFishBodyImageSubsetDecreaseCoelafish(FishStateManager.BodyTexture.SLICK, 2, 457);
        //DECREASE-SLICK-TACKLE
        pullFishBodyImageSubsetDecreaseTackle(FishStateManager.BodyTexture.SLICK, 6, 566);

        //DECREASE-SCALY-ORIGINAL
        pullFishBodyImageSubsetDecreaseOriginal(FishStateManager.BodyTexture.SCALY, 84, 358);
        //DECREASE-SCALY-COELAFISH
        pullFishBodyImageSubsetDecreaseCoelafish(FishStateManager.BodyTexture.SCALY, 80, 457);
        //DECREASE-SCALY-TACKLE
        pullFishBodyImageSubsetDecreaseTackle(FishStateManager.BodyTexture.SCALY, 84, 566);

        //DECREASE-SHELL-ORIGINAL
        pullFishBodyImageSubsetDecreaseOriginal(FishStateManager.BodyTexture.SHELL, 163, 358);
        //DECREASE-SHELL-COELAFISH
        pullFishBodyImageSubsetDecreaseCoelafish(FishStateManager.BodyTexture.SHELL, 159, 457);
        //DECREASE-SHELL-TACKLE
        pullFishBodyImageSubsetDecreaseTackle(FishStateManager.BodyTexture.SHELL, 163, 566);


        //INCREASE-SLICK-ORIGINAL
        pullFishBodyImageSubsetIncreaseOriginal(FishStateManager.BodyTexture.SLICK, 237, 298);
        //INCREASE-SLICK-COELAFISH
        pullFishBodyImageSubsetIncreaseCoelafish(FishStateManager.BodyTexture.SLICK, 237, 412);
        //INCREASE-SLICK-TACKLE
        pullFishBodyImageSubsetIncreaseTackle(FishStateManager.BodyTexture.SLICK, 237, 541);

        //INCREASE-SCALY-ORIGINAL
        pullFishBodyImageSubsetIncreaseOriginal(FishStateManager.BodyTexture.SCALY, 339, 298);
        //INCREASE-SCALY-COELAFISH
        pullFishBodyImageSubsetIncreaseCoelafish(FishStateManager.BodyTexture.SCALY, 339, 412);
        //INCREASE-SCALY-TACKLE
        pullFishBodyImageSubsetIncreaseTackle(FishStateManager.BodyTexture.SCALY, 339, 541);

        //INCREASE-SHELL-ORIGINAL
        pullFishBodyImageSubsetIncreaseOriginal(FishStateManager.BodyTexture.SHELL, 441, 298);
        //INCREASE-SHELL-COELAFISH
        pullFishBodyImageSubsetIncreaseCoelafish(FishStateManager.BodyTexture.SHELL, 441, 412);
        //INCREASE-SHELL-TACKLE
        pullFishBodyImageSubsetIncreaseTackle(FishStateManager.BodyTexture.SHELL, 441, 541);


        // BACKGROUND - IntroState
        chapter1Intro = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 6, 6, 133 + 1, 85 + 1);
        chapter2Intro = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 6, 232, 133 + 1, 85 + 1);
        chapter3Intro = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 11, 756, 133 + 1, 85 + 1);
        chapter4Intro = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 19, 1283, 133 + 1, 85 + 1);
        chapter5Intro = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 12, 1933, 133 + 1, 85 + 1);

        // BACKGROUND - ChapterState
        chapter1Chapter = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 146, 6, 255 + 1, 221 + 1);
        chapter2Chapter = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 146, 232, 255 + 1, 211 + 1);
        chapter3Chapter = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 152, 755, 255 + 1, 211 + 1);
        chapter4Chapter = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 159, 1283, 255 + 1, 221 + 1);
        chapter5Chapter = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 151, 1934, 255 + 1, 221 + 1);

        // BACKGROUND - WorldMapState
        chapter1WorldMap = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 407, 6, 255 + 1, 221 + 1);
        chapter2WorldMap = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 408, 230, 243 + 1, 223 + 1);
        chapter3WorldMap = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 411, 755, 255 + 1, 221 + 1);
        chapter4WorldMap = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 420, 1283, 255 + 1, 221 + 1);
        chapter5WorldMap = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 411, 1934, 255 + 1, 221 + 1);

        // TRANSPARENT LAYER - WAVE
        chapter1Wave = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 667, 4, 255 + 1, 223 + 1);
        chapter2Wave = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 657, 231, 255 + 1, 223 + 1);
        chapter3Wave = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 674, 754, 255 + 1, 223 + 1);
        chapter4Wave = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 682, 1282, 255 + 1, 223 + 1);
        chapter5Wave = Bitmap.createBitmap(spriteSheetChapterIntroAndWorldMap, 671, 1932, 255 + 1, 223 + 1);

        waveAnimationArray = new Bitmap[8];
        waveAnimationArray[0] = chapter1Wave;
        waveAnimationArray[1] = chapter2Wave;
        waveAnimationArray[2] = chapter3Wave;
        waveAnimationArray[3] = chapter4Wave;
        waveAnimationArray[4] = chapter5Wave;
        waveAnimationArray[5] = chapter4Wave;
        waveAnimationArray[6] = chapter3Wave;
        waveAnimationArray[7] = chapter2Wave;

        // OVERWORLD - TOKEN
        upOverworld0 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 149, 113, 9, 16);
        upOverworld1 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 158, 113, 9, 16);
        downOverworld0 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 148, 129, 9, 16);
        downOverworld1 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 158, 129, 9, 16);
        leftOverworld0 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 182, 113, 16, 9);
        leftOverworld1 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 199, 113, 16, 9);
        rightOverworld0 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 182, 123, 16, 9);
        rightOverworld1 = Bitmap.createBitmap(spriteSheetChapter1Creatures, 199, 123, 16, 9);


        // BACKGROUND - MainMenuState
        mainMenu = Bitmap.createBitmap(spriteSheetStartMenu, 6, 11, 127, 52);
        mainMenuEVOLUTION = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_mainmenustate_menulist_evolution);


        // BACKGROUND - GameStageState
        chapter1GameStage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.mario_7_2);
        brickGreen = Bitmap.createBitmap(chapter1GameStage, 0, 200, Tile.WIDTH, Tile.HEIGHT);
        coralPink1 = Bitmap.createBitmap(chapter1GameStage, 176, 184, Tile.WIDTH, Tile.HEIGHT);
        coralPink2 = Bitmap.createBitmap(chapter1GameStage, 1632, 120, Tile.WIDTH, Tile.HEIGHT);
        coralPink3 = Bitmap.createBitmap(chapter1GameStage, 2384, 184, Tile.WIDTH, Tile.HEIGHT);
        coinGameObject = Bitmap.createBitmap(chapter1GameStage, 224, 184, Tile.WIDTH, Tile.HEIGHT);


        // ENTITIES.NON_MOVEABLE - KELP
        kelpSolid = new Bitmap[4];
        kelpSolid[0] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 557, 718, 12, 32);
        kelpSolid[1] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 569, 718, 12, 32);
        kelpSolid[2] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 582, 718, 12, 32);
        kelpSolid[3] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 594, 718, 12, 32);

        // ENTITIES.MOVEABLE - ENEMIES
        seaJelly = new Bitmap[8];
        seaJelly[0] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 552, 29, 16, 32); //IDLE0 (forward-facing)
        seaJelly[1] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 568, 29, 16, 32); //IDLE1 (forward-facing)
        seaJelly[2] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 584, 29, 16, 32); //IDLE2 (forward-facing)
        seaJelly[3] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 599, 27, 16, 32); //ATTACK0 (forward-facing)
        seaJelly[4] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 619, 29, 16, 32); //ATTACK1 (left)
        seaJelly[5] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 639, 34, 32, 32); //ATTACK2 (left)
        seaJelly[6] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 674, 32, 32, 32); //ATTACK3 (left)
        seaJelly[7] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 714, 28, 16, 32); //HURT0 (forward-facing)

        eel = new Bitmap[7];
        eel[0] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 554, 70, 32, 16); //IDLE0 (left)
        eel[1] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 587, 70, 32, 16); //IDLE1 (left)
        eel[2] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 620, 70, 32, 16); //IDLE2 (left)
        eel[3] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 654, 70, 32, 16); //IDLE3 (left)
        eel[4] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 687, 70, 28, 16); //TURN (right-turning-left)
        eel[5] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 716, 62, 16, 32); //ATTACK0 (left)
        eel[6] = Bitmap.createBitmap(spriteSheetChapter1Creatures, 732, 69, 32, 16); //HURT0 (left)

        // ITEMS - MEAT
        meat = Bitmap.createBitmap(spriteSheetChapter1Creatures, 594, 697, 16, 16);


        // FONT (from pokemon yellow sprite sheet for battle)
        fontSpriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_pokemon_yellow_battle_interface);
        pokeballToken = Bitmap.createBitmap(fontSpriteSheet, 324, 269, 7, 7);
//        fontHashMap = FontGrabber.initFont();
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void pullFishHeadImageSubset(FishStateManager.BodySize bodySize, FishStateManager.BodyTexture bodyTexture,
                                               FishStateManager.Jaws jaws, int xStart, int yStart, int width, int height) {

        //minus 1 because ActionState.NONE doesn't count.
        for (int row = 0; row < (FishStateManager.ActionState.values().length - 1); row++) {

            int xStartLocal = xStart;
            int yStartLocal = yStart;
            int widthLocal = width;
            int heightLocal = height;

            FishStateManager.ActionState[] actionsArray = FishStateManager.ActionState.values();

            switch (actionsArray[row]) {

                case EAT:
                    yStartLocal = yStartLocal + (row * heightLocal);

                    for (int col = 0; col < eatFrames[0][0][0][0].length; col++) {
                        eatFrames[bodySize.ordinal()]
                                [bodyTexture.ordinal()]
                                [jaws.ordinal()]
                                [FishStateManager.ActionState.EAT.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, widthLocal, heightLocal);

                        xStartLocal = xStartLocal + widthLocal;
                    }

                    break;

                case BITE:
                    yStartLocal = yStartLocal + (row * heightLocal);

                    for (int col = 0; col < biteFrames[0][0][0][1].length; col++) {
                        biteFrames[bodySize.ordinal()]
                                [bodyTexture.ordinal()]
                                [jaws.ordinal()]
                                [FishStateManager.ActionState.BITE.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, widthLocal, heightLocal);

                        xStartLocal = xStartLocal + widthLocal;
                    }

                    break;

                case HURT:
                    yStartLocal = yStartLocal + (row * heightLocal);

                    for (int col = 0; col < hurtFrames[0][0][0][2].length; col++) {
                        hurtFrames[bodySize.ordinal()]
                                [bodyTexture.ordinal()]
                                [jaws.ordinal()]
                                [FishStateManager.ActionState.HURT.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, widthLocal, heightLocal);

                        xStartLocal = xStartLocal + widthLocal;
                    }

                    break;

                default:
                    break;

            }

        }
    }

    public static void pullFishBodyImageSubsetDecreaseOriginal(FishStateManager.BodyTexture bodyTexture,
                                                               int xStart, int yStart) {

        FishStateManager.Tail[] tailsArray = FishStateManager.Tail.values();

        for (int row = 0; row < FishStateManager.Tail.values().length; row++) {

            int xStartLocal = xStart;
            int yStartLocal = yStart;
            int width = 17;
            int height = 14;

            switch (tailsArray[row]) {

                case ORIGINAL:
                    for (int col = 0; col < 3; col++) {
                        tailOriginal[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.ORIGINAL.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 18;
                            width = width + 7;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 26;
                            width = width - 7;
                            height = height + 2;
                        }
                    }

                    break;

                case COELAFISH:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 2;
                            yStartLocal = yStartLocal + 19;
                            width = width - 2;
                            height = height + 1;
                        }

                        tailCoelafish[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.COELAFISH.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 20;
                            width = width + 5;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 24;
                            width = width - 5;
                            height = height + 2;
                        }
                    }

                    break;

                case TERATISU:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 1;
                            yStartLocal = yStartLocal + 39;
                            width = width - 1;
                            height = height + 1;
                        }

                        tailTeratisu[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.TERATISU.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 21;
                            width = width + 4;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 23;
                            width = width - 4;
                            height = height + 2;
                        }
                    }

                    break;

                case ZINICHTHY:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal - 1;
                            yStartLocal = yStartLocal + 59;
                            width = width + 1;
                            height = height + 1;
                        }

                        tailZinichthy[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.ZINICHTHY.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 19;
                            width = width + 6;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 25;
                            width = width - 6;
                            height = height + 2;
                        }
                    }

                    break;

                case KURASELACHE:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 79;
                            width = width + 0;
                            height = height + 2;
                        }

                        tailKuraselache[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.KURASELACHE.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 20;
                            width = width + 5;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 24;
                            width = width - 5;
                            height = height + 2;
                        }
                    }

                    break;

                default:
                    break;

            }

        }

    }

    public static void pullFishBodyImageSubsetDecreaseCoelafish(FishStateManager.BodyTexture bodyTexture,
                                                                int xStart, int yStart) {

        FishStateManager.Tail[] tailsArray = FishStateManager.Tail.values();

        for (int row = 0; row < FishStateManager.Tail.values().length; row++) {

            int xStartLocal = xStart;
            int yStartLocal = yStart;
            int width = 21;
            int height = 16;

            switch (tailsArray[row]) {

                case ORIGINAL:
                    for (int col = 0; col < 3; col++) {
                        tailOriginal[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.ORIGINAL.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 22;
                            width = width + 3;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 26;
                            width = width - 6;
                            height = height + 2;
                        }
                    }

                    break;

                case COELAFISH:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 19;
                            width = width + 0;
                            height = height + 1;
                        }

                        tailCoelafish[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.COELAFISH.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 26;
                            width = width - 1;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 24;
                            width = width - 4;
                            height = height + 2;
                        }
                    }

                    break;

                case TERATISU:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 43;
                            width = width + 0;
                            height = height + 1;
                        }

                        tailTeratisu[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.TERATISU.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 26;
                            width = width - 1;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 23;
                            width = width - 3;
                            height = height + 2;
                        }
                    }

                    break;

                case ZINICHTHY:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 65;
                            width = width + 0;
                            height = height + 1;
                        }

                        tailZinichthy[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.ZINICHTHY.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 22;
                            width = width + 3;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 25;
                            width = width - 5;
                            height = height + 2;
                        }
                    }

                    break;

                case KURASELACHE:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 87;
                            width = width + 0;
                            height = height + 1;
                        }

                        tailKuraselache[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.KURASELACHE.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 24;
                            width = width + 1;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 24;
                            width = width - 4;
                            height = height + 2;
                        }
                    }

                    break;

                default:
                    break;

            }

        }

    }

    public static void pullFishBodyImageSubsetDecreaseTackle(FishStateManager.BodyTexture bodyTexture,
                                                             int xStart, int yStart) {

        FishStateManager.Tail[] tailsArray = FishStateManager.Tail.values();

        for (int row = 0; row < FishStateManager.Tail.values().length; row++) {

            int xStartLocal = xStart;
            int yStartLocal = yStart;
            int width = 17;
            int height = 13;

            switch (tailsArray[row]) {

                case ORIGINAL:
                    for (int col = 0; col < 3; col++) {
                        tailOriginal[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.ORIGINAL.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 18;
                            width = width + 7;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 26;
                            width = width - 5;
                            height = height + 0;
                        }
                    }

                    break;

                case COELAFISH:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 1;
                            yStartLocal = yStartLocal + 15;
                            width = width - 1;
                            height = height + 2;
                        }

                        tailCoelafish[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.COELAFISH.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 21;
                            width = width + 4;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 24;
                            width = width - 3;
                            height = height - 1;
                        }
                    }

                    break;

                case TERATISU:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 1;
                            yStartLocal = yStartLocal + 32;
                            width = width - 1;
                            height = height + 2;
                        }

                        tailTeratisu[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.TERATISU.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 21;
                            width = width + 4;
                            height = height + 0;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 23;
                            width = width - 2;
                            height = height + 0;
                        }
                    }

                    break;

                case ZINICHTHY:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal - 1;
                            yStartLocal = yStartLocal + 48;
                            width = width + 1;
                            height = height + 1;
                        }

                        tailZinichthy[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.ZINICHTHY.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 19;
                            width = width + 6;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 25;
                            width = width - 4;
                            height = height + 0;
                        }
                    }

                    break;

                case KURASELACHE:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 64;
                            width = width + 0;
                            height = height + 3;
                        }

                        tailKuraselache[FishStateManager.BodySize.DECREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.KURASELACHE.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 20;
                            width = width + 5;
                            height = height + 0;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 24;
                            width = width - 3;
                            height = height + 0;
                        }
                    }

                    break;

                default:
                    break;

            }

        }

    }


    public static void pullFishBodyImageSubsetIncreaseOriginal(FishStateManager.BodyTexture bodyTexture,
                                                               int xStart, int yStart) {

        FishStateManager.Tail[] tailsArray = FishStateManager.Tail.values();

        for (int row = 0; row < FishStateManager.Tail.values().length; row++) {

            int xStartLocal = xStart;
            int yStartLocal = yStart;
            int width = 25;
            int height = 17;

            switch (tailsArray[row]) {

                case ORIGINAL:
                    for (int col = 0; col < 3; col++) {
                        tailOriginal[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.ORIGINAL.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 26;
                            width = width + 7;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 34;
                            width = width - 7;
                            height = height + 3;
                        }
                    }

                    break;

                case COELAFISH:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 2;
                            yStartLocal = yStartLocal + 22;
                            width = width - 2;
                            height = height + 1;
                        }

                        tailCoelafish[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.COELAFISH.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 28;
                            width = width + 5;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 32;
                            width = width - 5;
                            height = height + 3;
                        }
                    }

                    break;

                case TERATISU:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 1;
                            yStartLocal = yStartLocal + 45;
                            width = width - 1;
                            height = height + 1;
                        }

                        tailTeratisu[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.TERATISU.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 29;
                            width = width + 4;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 31;
                            width = width - 4;
                            height = height + 3;
                        }
                    }

                    break;

                case ZINICHTHY:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal - 1;
                            yStartLocal = yStartLocal + 68;
                            width = width + 1;
                            height = height + 1;
                        }

                        tailZinichthy[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.ZINICHTHY.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 27;
                            width = width + 6;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 33;
                            width = width - 6;
                            height = height + 3;
                        }
                    }

                    break;

                case KURASELACHE:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 91;
                            width = width + 0;
                            height = height + 1;
                        }

                        tailKuraselache[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.ORIGINAL.ordinal()]
                                //[FishStateManager.Tail.KURASELACHE.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 28;
                            width = width + 5;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 32;
                            width = width - 5;
                            height = height + 3;
                        }
                    }

                    break;

                default:
                    break;

            }

        }

    }


    public static void pullFishBodyImageSubsetIncreaseCoelafish(FishStateManager.BodyTexture bodyTexture,
                                                                int xStart, int yStart) {

        FishStateManager.Tail[] tailsArray = FishStateManager.Tail.values();

        for (int row = 0; row < FishStateManager.Tail.values().length; row++) {

            int xStartLocal = xStart;
            int yStartLocal = yStart;
            int width = 25;
            int height = 20;

            switch (tailsArray[row]) {

                case ORIGINAL:
                    for (int col = 0; col < 3; col++) {
                        tailOriginal[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.ORIGINAL.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 26;
                            width = width + 7;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 34;
                            width = width - 6;
                            height = height + 2;
                        }
                    }

                    break;

                case COELAFISH:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 2;
                            yStartLocal = yStartLocal + 25;
                            width = width - 2;
                            height = height + 1;
                        }

                        tailCoelafish[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.COELAFISH.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 28;
                            width = width + 5;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 32;
                            width = width - 4;
                            height = height + 2;
                        }
                    }

                    break;

                case TERATISU:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 1;
                            yStartLocal = yStartLocal + 51;
                            width = width - 1;
                            height = height + 1;
                        }

                        tailTeratisu[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.TERATISU.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 29;
                            width = width + 4;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 31;
                            width = width - 3;
                            height = height + 2;
                        }
                    }

                    break;

                case ZINICHTHY:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal - 1;
                            yStartLocal = yStartLocal + 77;
                            width = width + 1;
                            height = height + 1;
                        }

                        tailZinichthy[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.ZINICHTHY.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 27;
                            width = width + 6;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 33;
                            width = width - 5;
                            height = height + 2;
                        }
                    }

                    break;

                case KURASELACHE:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 103;
                            width = width + 0;
                            height = height + 1;
                        }

                        tailKuraselache[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.COELAFISH.ordinal()]
                                //[FishStateManager.Tail.KURASELACHE.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 28;
                            width = width + 5;
                            height = height + 2;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 32;
                            width = width - 4;
                            height = height + 2;
                        }
                    }

                    break;

                default:
                    break;

            }

        }

    }

    public static void pullFishBodyImageSubsetIncreaseTackle(FishStateManager.BodyTexture bodyTexture,
                                                             int xStart, int yStart) {

        FishStateManager.Tail[] tailsArray = FishStateManager.Tail.values();

        for (int row = 0; row < FishStateManager.Tail.values().length; row++) {

            int xStartLocal = xStart;
            int yStartLocal = yStart;
            int width = 25;
            int height = 16;

            switch (tailsArray[row]) {

                case ORIGINAL:
                    for (int col = 0; col < 3; col++) {
                        tailOriginal[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.ORIGINAL.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 26;
                            width = width + 7;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 34;
                            width = width - 5;
                            height = height + 2;
                        }
                    }

                    break;

                case COELAFISH:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 2;
                            yStartLocal = yStartLocal + 20;
                            width = width - 2;
                            height = height + 1;
                        }

                        tailCoelafish[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.COELAFISH.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 28;
                            width = width + 5;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 32;
                            width = width - 3;
                            height = height + 2;
                        }
                    }

                    break;

                case TERATISU:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 1;
                            yStartLocal = yStartLocal + 41;
                            width = width - 1;
                            height = height + 1;
                        }

                        tailTeratisu[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.TERATISU.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 29;
                            width = width + 4;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 31;
                            width = width - 2;
                            height = height + 2;
                        }
                    }

                    break;

                case ZINICHTHY:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal - 1;
                            yStartLocal = yStartLocal + 62;
                            width = width + 1;
                            height = height + 1;
                        }

                        tailZinichthy[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.ZINICHTHY.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 27;
                            width = width + 6;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 33;
                            width = width - 4;
                            height = height + 2;
                        }
                    }

                    break;

                case KURASELACHE:
                    for (int col = 0; col < 3; col++) {
                        if (col == 0) {
                            xStartLocal = xStartLocal + 0;
                            yStartLocal = yStartLocal + 83;
                            width = width + 0;
                            height = height + 1;
                        }

                        tailKuraselache[FishStateManager.BodySize.INCREASE.ordinal()]
                                [bodyTexture.ordinal()]
                                [FishStateManager.FinPectoral.TACKLE.ordinal()]
                                //[FishStateManager.Tail.KURASELACHE.ordinal()]
                                [col] = Bitmap.createBitmap(spriteSheetChapter1Creatures, xStartLocal, yStartLocal, width, height);

                        //setting up for next col
                        if (col == 0) {
                            xStartLocal = xStartLocal + 28;
                            width = width + 5;
                            height = height + 1;
                        } else if (col == 1) {
                            xStartLocal = xStartLocal + 32;
                            width = width - 3;
                            height = height + 2;
                        }
                    }

                    break;

                default:
                    break;

            }

        }

    }


}
