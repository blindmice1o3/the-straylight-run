package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.ClassComponent;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Constructor;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Class
        implements Serializable {
    private ClassComponent.AccessModifier accessModifier;
    private String name;
    private List<Constructor> constructors;
    private List<Field> fields;
    private List<Method> methods;

    public Class(ClassComponent.AccessModifier accessModifier, String name) {
        this.accessModifier = accessModifier;
        this.name = name;
        constructors = new ArrayList<>();
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public boolean addConstructor(Constructor constructor) {
        constructor.setName(name);

        return constructors.add(constructor);
    }

    public boolean addField(Field field) {
        return fields.add(field);
    }

    public boolean addMethod(Method method) {
        return methods.add(method);
    }

    public void updateName(String name) {
        this.name = name;

        for (Constructor constructor : constructors) {
            constructor.setName(name);
        }
    }

    public ClassComponent.AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public String getName() {
        return name;
    }

    public List<Constructor> getConstructors() {
        return constructors;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }
}
