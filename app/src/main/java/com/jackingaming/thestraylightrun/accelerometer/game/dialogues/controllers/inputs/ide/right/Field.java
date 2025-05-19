package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import java.util.List;

public class Field extends ClassComponent {
    private String value;
    private String inLineComment;

    public Field(AccessModifier accessModifier,
                 List<ClassInterfaceAndObjectRelated> nonAccessModifiers,
                 String type, String name, String value,
                 String comment, String todo, String inLineComment,
                 boolean hasBlankLineAbove) {
        super(accessModifier, nonAccessModifiers, type, name, comment, todo, hasBlankLineAbove);
        this.value = value;
        this.inLineComment = inLineComment;
    }

    public String getValue() {
        return value;
    }

    public String getInLineComment() {
        return inLineComment;
    }
}
