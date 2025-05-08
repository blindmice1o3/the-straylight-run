package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public class Method extends ClassComponent {

    private List<VariableDeclaration> argumentList;
    private String body;

    public Method(AccessModifier accessModifier, List<ClassInterfaceAndObjectRelated> nonAccessModifiers, String returnType, String name, List<VariableDeclaration> argumentList, String body) {
        super(accessModifier, nonAccessModifiers, returnType, name);
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
