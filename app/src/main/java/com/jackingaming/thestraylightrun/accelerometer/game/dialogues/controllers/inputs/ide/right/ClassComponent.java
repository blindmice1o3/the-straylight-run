package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassComponent {
    // [reserved words] categorized by functionality
    public enum DataType {
        BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT;
    }

    public enum ControlFlow {
        BREAK, CASE, CONTINUE, DEFAULT, DO, ELSE, FOR, IF, RETURN,
        SWITCH, WHILE;
    }

    public enum AccessModifier {
        DEFAULT, PRIVATE, PROTECTED, PUBLIC;
    }

    public enum ClassInterfaceAndObjectRelated {
        ABSTRACT, CLASS, EXTENDS, FINAL, IMPLEMENTS, INSTANCEOF,
        INTERFACE, NEW, STATIC, STRICTFP, SUPER, THIS;
    }

    public enum ExceptionHandling {
        CATCH, FINALLY, THROW, THROWS, TRY;
    }

    public enum OtherKeywords {
        ASSERT, ENUM, IMPORT, NATIVE, PACKAGE, SYNCHRONIZED, TRANSIENT,
        VOID, VOLATILE;
    }

    public enum ReservedButNotUsed {
        CONST, GOTO;
    }

    public enum Literal {
        TRUE, FALSE, NULL;
    }

    public static Map<String, List<String>> reservedWordsByFunctionality;

    static {
        reservedWordsByFunctionality = new HashMap<>();

        List<String> dataTypeAsString = new ArrayList<>();
        for (DataType dataType : DataType.values()) {
            dataTypeAsString.add(dataType.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(DataType.class.getSimpleName(), dataTypeAsString);

        List<String> controlFlowAsString = new ArrayList<>();
        for (ControlFlow controlFlow : ControlFlow.values()) {
            controlFlowAsString.add(controlFlow.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(ControlFlow.class.getSimpleName(), controlFlowAsString);

        List<String> accessModifierAsString = new ArrayList<>();
        for (AccessModifier accessModifier : AccessModifier.values()) {
            accessModifierAsString.add(accessModifier.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(AccessModifier.class.getSimpleName(), accessModifierAsString);

        List<String> classInterfaceAndObjectRelatedAsString = new ArrayList<>();
        for (ClassInterfaceAndObjectRelated classInterfaceAndObjectRelated : ClassInterfaceAndObjectRelated.values()) {
            classInterfaceAndObjectRelatedAsString.add(classInterfaceAndObjectRelated.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(ClassInterfaceAndObjectRelated.class.getSimpleName(), classInterfaceAndObjectRelatedAsString);

        List<String> exceptionHandlingAsString = new ArrayList<>();
        for (ExceptionHandling exceptionHandling : ExceptionHandling.values()) {
            exceptionHandlingAsString.add(exceptionHandling.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(ExceptionHandling.class.getSimpleName(), exceptionHandlingAsString);

        List<String> otherKeywordsAsString = new ArrayList<>();
        for (OtherKeywords otherKeywords : OtherKeywords.values()) {
            otherKeywordsAsString.add(otherKeywords.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(OtherKeywords.class.getSimpleName(), otherKeywordsAsString);

        List<String> reservedButNotUsedAsString = new ArrayList<>();
        for (ReservedButNotUsed reservedButNotUsed : ReservedButNotUsed.values()) {
            reservedButNotUsedAsString.add(reservedButNotUsed.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(ReservedButNotUsed.class.getSimpleName(), reservedButNotUsedAsString);

        List<String> literalAsString = new ArrayList<>();
        for (Literal literal : Literal.values()) {
            literalAsString.add(literal.toString().toLowerCase());
        }
        reservedWordsByFunctionality.put(Literal.class.getSimpleName(), literalAsString);
    }

    public static Map<String, List<String>> getReservedWordsByFunctionality() {
        return reservedWordsByFunctionality;
    }

    private AccessModifier accessModifier;
    private List<ClassInterfaceAndObjectRelated> classInterfaceAndObjectRelateds;
    private String type;
    private String name;

    public ClassComponent(AccessModifier accessModifier,
                          List<ClassInterfaceAndObjectRelated> classInterfaceAndObjectRelateds,
                          String type, String name) {
        this.accessModifier = accessModifier;
        this.classInterfaceAndObjectRelateds = classInterfaceAndObjectRelateds;
        this.type = type;
        this.name = name;
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public List<ClassInterfaceAndObjectRelated> getClassInterfaceAndObjectRelateds() {
        return classInterfaceAndObjectRelateds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
