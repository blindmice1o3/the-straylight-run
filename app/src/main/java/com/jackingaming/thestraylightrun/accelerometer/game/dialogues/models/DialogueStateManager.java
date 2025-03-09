package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models;

import java.util.List;

public class DialogueStateManager {
    private List<DialogueState> dialogueStates;
    private int indexCurrent;

    public DialogueStateManager(List<DialogueState> dialogueStates) {
        this.dialogueStates = dialogueStates;
        indexCurrent = 0;
    }

    public DialogueState getCurrentDialogueState() {
        return (indexCurrent < dialogueStates.size()) ?
                dialogueStates.get(indexCurrent) :
                null;
    }

    public DialogueState getPreviousDialogueState() {
        DialogueState lastDialogueState = dialogueStates.get(dialogueStates.size() - 1);
        return (indexCurrent > 0) ?
                lastDialogueState :
                null;
    }

    public void changeToNextState() {
        indexCurrent++;
    }
}
