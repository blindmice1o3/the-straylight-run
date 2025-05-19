package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import java.util.List;

public class Field extends ClassComponent {
    private String value;
    private boolean hasBlankLineAbove;

    public Field(AccessModifier accessModifier, List<ClassInterfaceAndObjectRelated> nonAccessModifiers, String type, String name, String value, String comment) {
        super(accessModifier, nonAccessModifiers, type, name, comment);
        this.value = value;
    }

    public Field(AccessModifier accessModifier, List<ClassInterfaceAndObjectRelated> nonAccessModifiers, String type, String name, String value, String comment, boolean hasBlankLineAbove) {
        this(accessModifier, nonAccessModifiers, type, name, value, comment);
        this.hasBlankLineAbove = hasBlankLineAbove;
    }

    public String getValue() {
        return value;
    }

    public boolean isHasBlankLineAbove() {
        return hasBlankLineAbove;
    }
}
