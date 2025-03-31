package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Class
        implements Serializable {
    private String name;
    private List<Field> fields;
    private List<Method> methods;

    public Class(String name) {
        this.name = name;
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public boolean addField(Field field) {
        return fields.add(field);
    }

    public boolean addMethod(Method method) {
        return methods.add(method);
    }

    public String getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }
}
