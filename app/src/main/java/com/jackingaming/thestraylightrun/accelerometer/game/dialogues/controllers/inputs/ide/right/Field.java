package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import java.util.List;

public class Field extends ClassComponent {
    private String value;

    public Field(AccessModifier accessModifier, List<ClassInterfaceAndObjectRelated> nonAccessModifiers, String type, String name, String value, String comment) {
        super(accessModifier, nonAccessModifiers, type, name, comment);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
