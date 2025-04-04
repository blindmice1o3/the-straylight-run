package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import java.util.List;

public class ClassComponent {
    public enum AccessModifier {
        PUBLIC, PRIVATE, PROTECTED, DEFAULT;
    }

    public enum NonAccessModifier {
        STATIC, FINAL, ABSTRACT, SYNCHRONIZED, VOLATILE, TRANSIENT, NATIVE, STRICTFP;
    }

    private AccessModifier accessModifier;
    private List<NonAccessModifier> nonAccessModifiers;
    private String type;
    private String name;

    public ClassComponent(AccessModifier accessModifier,
                          List<NonAccessModifier> nonAccessModifiers,
                          String type, String name) {
        this.accessModifier = accessModifier;
        this.nonAccessModifiers = nonAccessModifiers;
        this.type = type;
        this.name = name;
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public List<NonAccessModifier> getNonAccessModifiers() {
        return nonAccessModifiers;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
