package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHothouse;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm.RunFive;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm.RunFour;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm.RunThree;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm.RunTwo;

public class IntroState
        implements State {
    public static final String TAG = IntroState.class.getSimpleName();

    private Game game;

    private boolean isTextShown = false;

    private RunTwo runTwo;
    private RunThree runThree;
    private RunFour runFour;
    private RunFive runFive;

    public IntroState() {
    }

    @Override
    public void init(Game game) {
        this.game = game;

        runTwo = new RunTwo(game);
        runThree = new RunThree(game);
        runFour = new RunFour(game);
        runFive = new RunFive(game);
    }

    @Override
    public void enter(Object[] args) {

    }

    @Override
    public void exit() {

    }

    private boolean isIntroTextAnimationFinished = false;

    @Override
    public void update(long elapsed) {
        if (!isTextShown) {
            isTextShown = true;

            game.getSceneManager().update(elapsed);

            String textRunNumber = null;
            String requirementForRun = null;
            switch (game.getRun()) {
                case ONE:
                    textRunNumber = game.getContext().getString(R.string.text_run_one);
                    requirementForRun = game.getContext().getResources().getString(R.string.click_button_holder_a_or_b);
                    break;
                case TWO:
                    textRunNumber = game.getContext().getString(R.string.text_run_two);
                    requirementForRun = runTwo.getDialogueForCurrentState();
                    break;
                case THREE:
                    textRunNumber = game.getContext().getString(R.string.text_run_three);
                    requirementForRun = runThree.getDialogueForCurrentState();
                    break;
                case FOUR:
                    textRunNumber = game.getContext().getString(R.string.text_run_four);
                    requirementForRun = runFour.getDialogueForCurrentState();
                    break;
                case FIVE:
                    textRunNumber = game.getContext().getString(R.string.text_run_five);
                    requirementForRun = runFive.getDialogueForCurrentState();
                    break;
            }

            Bitmap imageForTextbox = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.dialogue_image_nwt_host);
            String textRunNumberNoSpace = textRunNumber.replaceAll("\\s", "");
            String textForTextBox = String.format("%s: %s",
                    textRunNumberNoSpace,
                    requirementForRun);
            typeWriterDialogFragment = TypeWriterDialogFragment.newInstance(
                    50L, imageForTextbox,
                    textForTextBox,
                    new TypeWriterDialogFragment.DismissListener() {
                        @Override
                        public void onDismiss() {
                            Log.e(TAG, "TextboxListener.showTextbox( TypeWriterDialogFragment.onDismiss() )");
                        }
                    }, new TypeWriterTextView.TextCompletionListener() {
                        @Override
                        public void onAnimationFinish() {
                            Log.e(TAG, "TextboxListener.showTextbox( TypeWriterDialogFragment.onAnimationFinish() )");
                            isIntroTextAnimationFinished = true;

                            switch (game.getRun()) {
                                case ONE:
                                    // Do nothing.
//                                    game.giveRunOneQuest();
                                    break;
                                case TWO:
                                    giveRunTwoQuest();
                                    break;
                                case THREE:
                                    giveRunThreeQuest();
                                    break;
                                case FOUR:
                                    giveRunFourQuest();
                                    break;
                                case FIVE:
                                    giveRunFiveQuest();
                                    break;
                            }
                        }
                    }
            );

            game.getTextboxListener().showTextbox(
                    typeWriterDialogFragment
            );
        }

        interpretInput();
    }

    TypeWriterDialogFragment typeWriterDialogFragment;

    private void giveRunFiveQuest() {
        SceneHothouse.getInstance().init(game);

        boolean wasQuestAcceptedRunFive =
                Player.getInstance().getQuestManager().addQuest(
                        runFive
                );

        if (wasQuestAcceptedRunFive) {
            Log.e(TAG, "wasQuestAcceptedRunFive");
            runFive.dispenseStartingItems();
        } else {
            Log.e(TAG, "!wasQuestAcceptedRunFive");
        }
    }

    private void giveRunFourQuest() {
        boolean wasQuestAcceptedRunFour =
                Player.getInstance().getQuestManager().addQuest(
                        runFour
                );

        if (wasQuestAcceptedRunFour) {
            Log.e(TAG, "wasQuestAcceptedRunFour");
            runFour.dispenseStartingItems();
        } else {
            Log.e(TAG, "!wasQuestAcceptedRunFour");
        }
    }

    private void giveRunThreeQuest() {
        boolean wasQuestAcceptedRunThree =
                Player.getInstance().getQuestManager().addQuest(
                        runThree
                );

        if (wasQuestAcceptedRunThree) {
            Log.e(TAG, "wasQuestAcceptedRunThree");
            runThree.dispenseStartingItems();
        } else {
            Log.e(TAG, "!wasQuestAcceptedRunThree");
        }
    }

    private void giveRunTwoQuest() {
        boolean wasQuestAcceptedRunTwo =
                Player.getInstance().getQuestManager().addQuest(
                        runTwo
                );

        if (wasQuestAcceptedRunTwo) {
            Log.e(TAG, "wasQuestAcceptedRunTwo");
            runTwo.dispenseStartingItems();
        } else {
            Log.e(TAG, "!wasQuestAcceptedRunTwo");
        }
    }


    private void interpretInput() {
        if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            if (isIntroTextAnimationFinished) {
                game.getTextboxListener().showStatsDisplayer();
                game.getStateManager().pop();
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        game.getSceneManager().drawCurrentFrame(canvas);
    }
}
