package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide;

public class VariableDeclaration {
    private String type;
    private String name;

    public VariableDeclaration(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
