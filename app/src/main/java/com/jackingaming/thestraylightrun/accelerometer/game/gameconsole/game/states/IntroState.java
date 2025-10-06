package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.EditTextDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHothouse;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.daughter.RunFive;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.daughter.RunFour;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.daughter.RunOne;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.daughter.RunThree;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.daughter.RunTwo;

public class IntroState
        implements State {
    public static final String TAG = IntroState.class.getSimpleName();

    public interface SeedListener {
        void onAssignedNameAndDescription();
    }

    private Game game;

    private boolean isTextShown = false;

    private RunOne runOne;
    private String seedName;
    private String seedDescription;
    private SeedListener seedListener;

    public void setSeedListener(SeedListener seedListener) {
        this.seedListener = seedListener;
    }

    private RunTwo runTwo;
    private RunThree runThree;
    private RunFour runFour;
    private RunFive runFive;

    public IntroState() {
    }

    @Override
    public void init(Game game) {
        this.game = game;

        String[] dialogueArrayRunOne = game.getContext().getResources().getStringArray(R.array.run_one_dialogue_array);
        runOne = new RunOne(game, dialogueArrayRunOne);

        String[] dialogueArrayRunTwo = game.getContext().getResources().getStringArray(R.array.run_two_dialogue_array);
        runTwo = new RunTwo(game, dialogueArrayRunTwo);

        String[] dialogueArrayRunThree = game.getContext().getResources().getStringArray(R.array.run_three_dialogue_array);
        runThree = new RunThree(game, dialogueArrayRunThree);

        String[] dialogueArrayRunFour = game.getContext().getResources().getStringArray(R.array.run_four_dialogue_array);
        runFour = new RunFour(game, dialogueArrayRunFour);

        String[] dialogueArrayRunFive = game.getContext().getResources().getStringArray(R.array.run_five_dialogue_array);
        runFive = new RunFive(game, dialogueArrayRunFive);
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

            String requirementForRun = null;
            switch (game.getRun()) {
                case ONE:
                    requirementForRun = runOne.getDialogueForCurrentState();
                    break;
                case TWO:
                    requirementForRun = runTwo.getDialogueForCurrentState();
                    break;
                case THREE:
                    requirementForRun = runThree.getDialogueForCurrentState();
                    break;
                case FOUR:
                    requirementForRun = runFour.getDialogueForCurrentState();
                    break;
                case FIVE:
                    requirementForRun = runFive.getDialogueForCurrentState();
                    break;
            }

            Bitmap imageForTextbox = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.group_chat_image_nwt_host);
            String textForTextBox = String.format("Run: %s + NOW IN IntroState!!! \n" + "%s",
                    game.getRun().toString(),
                    requirementForRun);
            game.getTextboxListener().showTextbox(
                    TypeWriterDialogFragment.newInstance(
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
                                            giveRunOneQuest();
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
                    )
            );
        }

        interpretInput();
    }

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

    private void giveRunOneQuest() {
        boolean wasQuestAcceptedRunOne =
                Player.getInstance().getQuestManager().addQuest(
                        runOne
                );

        if (wasQuestAcceptedRunOne) {
            Log.e(TAG, "wasQuestAcceptedRunOne");
            runOne.dispenseStartingItems();

            EditTextDialogFragment dialogFragmentRunOneName = EditTextDialogFragment.newInstance(
                    new EditTextDialogFragment.EnterListener() {
                        @Override
                        public void onDismiss() {
                            Log.e(TAG, "onDismiss()");
                        }

                        @Override
                        public void onEnterKeyPressed(String name) {
                            Log.e(TAG, "onEnterKeyPressed()");

                            seedName = name;

                            EditTextDialogFragment dialogFragmentRunOneDescription = EditTextDialogFragment.newInstance(
                                    new EditTextDialogFragment.EnterListener() {
                                        @Override
                                        public void onDismiss() {
                                            Log.e(TAG, "onDismiss()");
                                        }

                                        @Override
                                        public void onEnterKeyPressed(String name) {
                                            Log.e(TAG, "onEnterKeyPressed()");

                                            seedDescription = name;

                                            seedListener.onAssignedNameAndDescription();
                                        }
                                    },
                                    "seed description",
                                    false
                            );
                            dialogFragmentRunOneDescription.show(game.getFragmentManager(), TAG);
                        }
                    },
                    "seed name",
                    false
            );
            dialogFragmentRunOneName.show(game.getFragmentManager(), TAG);
        } else {
            Log.e(TAG, "!wasQuestAcceptedRunOne");
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

    public String getSeedName() {
        return seedName;
    }

    public String getSeedDescription() {
        return seedDescription;
    }
}
