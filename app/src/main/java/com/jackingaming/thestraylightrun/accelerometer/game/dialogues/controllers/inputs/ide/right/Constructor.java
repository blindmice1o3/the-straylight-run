package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public class Constructor extends ClassComponent {

    private List<VariableDeclaration> argumentList;
    private String body;

    public Constructor(AccessModifier accessModifier, List<VariableDeclaration> argumentList,
                       String body, String comment, String todo,
                       boolean hasBlankLineAbove) {
        super(accessModifier, null, null, null, comment, todo, hasBlankLineAbove);
        this.argumentList = new ArrayList<>();
        if (argumentList != null) {
            this.argumentList.addAll(argumentList);
        }
        this.body = body;
    }

    public List<VariableDeclaration> getArgumentList() {
        return argumentList;
    }

    public String getBody() {
        return body;
    }
}
