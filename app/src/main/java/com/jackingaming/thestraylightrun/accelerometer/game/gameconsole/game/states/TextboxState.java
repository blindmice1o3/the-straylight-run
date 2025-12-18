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

public class TextboxState
        implements State {
    public static final String TAG = TextboxState.class.getSimpleName();

    private Game game;

    private boolean isTextShown = false;
    private Bitmap imageForDialogue;
    private String textToShow;
    private TypeWriterDialogFragment.DismissListener dismissListener;
    private TypeWriterTextView.TextCompletionListener textCompletionListener;

    private RunTwo runTwo;
    private RunThree runThree;
    private RunFour runFour;
    private RunFive runFive;

    public TextboxState() {
    }

    @Override
    public void reload(Game game) {
        this.game = game;

        runTwo.reload(game);
        runThree.reload(game);
        runFour.reload(game);
        runFive.reload(game);
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
        Log.d(TAG, "enter()");

        isTextShown = false;
        isTextboxAnimationFinished = false;

        if (args != null) {
            Log.d(TAG, "args != null");

            imageForDialogue = (Bitmap) args[0];
            textToShow = (String) args[1];
            dismissListener = (TypeWriterDialogFragment.DismissListener) args[2];
            textCompletionListener = (TypeWriterTextView.TextCompletionListener) args[3];
        } else {
            Log.d(TAG, "args == null");

            // Do nothing.
        }
    }

    @Override
    public void exit() {
        Log.d(TAG, "exit()");

        // Do nothing.
    }

    private boolean isTextboxAnimationFinished = false;

    @Override
    public void update(long elapsed) {
        if (!isTextShown) {
            isTextShown = true;

            game.getSceneManager().update(elapsed);

            if (imageForDialogue != null) {
                Log.d(TAG, "imageForDialogue != null");

                // Do nothing.
            } else {
                Log.d(TAG, "imageForDialogue == null");

                imageForDialogue = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.dialogue_image_nwt_host);
            }

            if (textToShow != null) {
                Log.d(TAG, "textToShow != null");

                // Do nothing.
            } else {
                Log.d(TAG, "textToShow == null");

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

                String textRunNumberNoSpace = textRunNumber.replaceAll("\\s", "");
                textToShow = String.format("%s: %s",
                        textRunNumberNoSpace,
                        requirementForRun);
            }

            if (dismissListener != null) {
                Log.d(TAG, "dismissListener != null");

                // Do nothing.
            } else {
                Log.d(TAG, "dismissListener == null");

                dismissListener = new TypeWriterDialogFragment.DismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.d(TAG, "update() TypeWriterDialogFragment.DismissListener.onDismiss()");

                        // Do nothing.
                    }
                };
            }

            if (textCompletionListener != null) {
                Log.d(TAG, "textCompletionListener != null");

                // Do nothing.
            } else {
                Log.d(TAG, "textCompletionListener == null");

                textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
                    @Override
                    public void onAnimationFinish() {
                        Log.d(TAG, "update() TypeWriterTextView.TextCompletionListener.onAnimationFinish()");

                        isTextboxAnimationFinished = true;

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
                };
            }

            typeWriterDialogFragment = TypeWriterDialogFragment.newInstance(
                    50L, imageForDialogue,
                    textToShow,
                    dismissListener,
                    textCompletionListener
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
            if (isTextboxAnimationFinished) {
                game.getTextboxListener().showStatsDisplayer();
                game.getStateManager().pop();

                ////////////////////////////
                dismissListener.onDismiss();
                ////////////////////////////
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        game.getSceneManager().drawCurrentFrame(canvas);
    }

    public void setTextboxAnimationFinished(boolean textboxAnimationFinished) {
        this.isTextboxAnimationFinished = textboxAnimationFinished;
    }
}