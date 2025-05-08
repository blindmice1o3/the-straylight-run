package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.VariableDeclaration;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.left.ProjectViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.ClassComponent;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassEditorFragment extends Fragment {
    public static final String TAG = ClassEditorFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLASS = "class";

    // TODO: Rename and change types of parameters
    private Class classToEdit;
    private LinearLayout linearLayoutClass;
    private LinearLayout.LayoutParams layoutParams;

    public ClassEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classToEdit Class.
     * @return A new instance of fragment ClassEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassEditorFragment newInstance(Class classToEdit) {
        ClassEditorFragment fragment = new ClassEditorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS, classToEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classToEdit = (Class) getArguments().getSerializable(ARG_CLASS);

            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            foregroundColorSpanBlack = new ForegroundColorSpan(Color.BLACK);
            foregroundColorSpanBlue = new ForegroundColorSpan(Color.BLUE);
            foregroundColorSpanGreen = new ForegroundColorSpan(Color.GREEN);
            foregroundColorSpanCyan = new ForegroundColorSpan(Color.CYAN);
            foregroundColorSpanGray = new ForegroundColorSpan(Color.GRAY);
            foregroundColorSpanRed = new ForegroundColorSpan(Color.RED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_editor, container, false);
    }

    private ForegroundColorSpan foregroundColorSpanBlack;
    private ForegroundColorSpan foregroundColorSpanBlue;
    private ForegroundColorSpan foregroundColorSpanGreen;
    private ForegroundColorSpan foregroundColorSpanCyan;
    private ForegroundColorSpan foregroundColorSpanGray;
    private ForegroundColorSpan foregroundColorSpanRed;

    private SpannableString convertToColoredSpannableString(String textToColor, int color) {
        SpannableString spannableString = new SpannableString(textToColor);
        if (color == Color.BLACK) {
            spannableString.setSpan(foregroundColorSpanBlack, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (color == Color.BLUE) {
            spannableString.setSpan(foregroundColorSpanBlue, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (color == Color.GREEN) {
            spannableString.setSpan(foregroundColorSpanGreen, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (color == Color.CYAN) {
            spannableString.setSpan(foregroundColorSpanCyan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (color == Color.GRAY) {
            spannableString.setSpan(foregroundColorSpanGray, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (color == Color.RED) {
            spannableString.setSpan(foregroundColorSpanRed, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    private void addTextViewAsClassOrMethodClosingToLinearLayout(int indentationLevel) {
        Spannable bracketCurlyClose = convertToColoredSpannableString(
                "}", Color.BLACK);

        TextView textViewClassOrMethodClosing = new TextView(getContext());
        textViewClassOrMethodClosing.setLayoutParams(layoutParams);
        for (int indent = 0; indent < indentationLevel; indent++) {
            textViewClassOrMethodClosing.append("    ");
        }

        textViewClassOrMethodClosing.append(bracketCurlyClose);

        linearLayoutClass.addView(textViewClassOrMethodClosing);
    }

    private void addTextViewAsClassOrMethodOpeningToLinearLayout(List<Spannable> spannables, int indentationLevel) {
        TextView textViewClassOrMethodOpening = new TextView(getContext());
        textViewClassOrMethodOpening.setLayoutParams(layoutParams);
        for (int indent = 0; indent < indentationLevel; indent++) {
            textViewClassOrMethodOpening.append("    ");
        }

        for (int i = 0; i < spannables.size(); i++) {
            textViewClassOrMethodOpening.append(spannables.get(i));
            textViewClassOrMethodOpening.append(" ");
        }

        linearLayoutClass.addView(textViewClassOrMethodOpening);
    }

    private void addTextViewAsFieldToLinearLayout(List<Spannable> spannables, int indentationLevel) {
        TextView textViewField = new TextView(getContext());
        textViewField.setLayoutParams(layoutParams);
        for (int indent = 0; indent < indentationLevel; indent++) {
            textViewField.append("    ");
        }

        for (int i = 0; i < spannables.size(); i++) {
            textViewField.append(spannables.get(i));

            if (i != spannables.size() - 1) {
                textViewField.append(" ");
            } else {
                textViewField.append(";");
            }
        }

        linearLayoutClass.addView(textViewField);
    }

    private void addTextViewAsBlankLineToLinearLayout() {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams);

        linearLayoutClass.addView(textView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutClass = view.findViewById(R.id.ll_class);

        // NOTE: this approach to adding color to a line of code has
        // a caveat... CONSECUTIVE-SAME-COLOR will NOT work when
        // adding elements to the list.
        ///////////CLASS-OPENING///////////////////
        Spannable keyWord = convertToColoredSpannableString(
                "public class", Color.BLUE);
        Spannable nameClass = convertToColoredSpannableString(
                classToEdit.getName(), Color.GREEN);
        Spannable bracketCurlyOpen = convertToColoredSpannableString(
                "{", Color.BLACK);

        List<Spannable> spannablesClassOpening = new ArrayList<>();
        spannablesClassOpening.add(keyWord);
        spannablesClassOpening.add(nameClass);
        spannablesClassOpening.add(bracketCurlyOpen);

        addTextViewAsClassOrMethodOpeningToLinearLayout(
                spannablesClassOpening, 0);
        addTextViewAsBlankLineToLinearLayout();
        /////////////////////////////////////////

        //////////FIELDS/////////////////////////
        List<Field> fields = classToEdit.getFields();
        for (Field field : fields) {
            String accessModifier = field.getAccessModifier().name().toLowerCase();
            String type = field.getType();
            Spannable spannableAccessModifierAndType = convertToColoredSpannableString(
                    accessModifier + " " + type, Color.BLUE);

            String name = field.getName();
            Spannable spannableName = convertToColoredSpannableString(
                    name, Color.GREEN);

            List<Spannable> spannablesField = new ArrayList<>();
            spannablesField.add(spannableAccessModifierAndType);
            spannablesField.add(spannableName);

            addTextViewAsFieldToLinearLayout(
                    spannablesField, 1);
            addTextViewAsBlankLineToLinearLayout();
        }
        /////////////////////////////////////////

        /////////METHODS/////////////////////////
        List<Method> methods = classToEdit.getMethods();
        for (Method method : methods) {
            String nonAccessModifiers = null;
            if (method.getClassInterfaceAndObjectRelateds() != null) {
                for (ClassComponent.ClassInterfaceAndObjectRelated nonAccessModifier : method.getClassInterfaceAndObjectRelateds()) {
                    if (nonAccessModifiers == null) {
                        Log.e(TAG, "nonAccessModifiers NULL");
                        nonAccessModifiers = nonAccessModifier.name().toLowerCase();
                    } else {
                        Log.e(TAG, "nonAccessModifiers not NULL");
                        nonAccessModifiers += nonAccessModifier.name().toLowerCase();
                    }
                }
            }

            String argumentList = null;
            if (method.getArgumentList() != null) {
                for (VariableDeclaration variableDeclaration : method.getArgumentList()) {
                    if (argumentList != null) {
                        Log.e(TAG, "argumentList not NULL");
                        argumentList += ", " + (variableDeclaration.getType() + " " + variableDeclaration.getName());
                    } else {
                        Log.e(TAG, "argumentList NULL");
                        argumentList = (variableDeclaration.getType() + " " + variableDeclaration.getName());
                    }
                }
            }

            List<Spannable> spannablesMethod = new ArrayList<>();
            if (nonAccessModifiers != null) {
                spannablesMethod.add(convertToColoredSpannableString(
                        method.getAccessModifier().name().toLowerCase() + " " + nonAccessModifiers + " " + method.getType(),
                        Color.BLUE));
            } else {
                spannablesMethod.add(convertToColoredSpannableString(
                        method.getAccessModifier().name().toLowerCase() + " " + method.getType(),
                        Color.BLUE));
            }

            spannablesMethod.add(convertToColoredSpannableString(
                    method.getName(), Color.GREEN
            ));
            spannablesMethod.add(convertToColoredSpannableString(
                    "(", Color.BLACK
            ));

            if (argumentList != null) {
                spannablesMethod.add(convertToColoredSpannableString(
                        argumentList, Color.GRAY
                ));
            }

            spannablesMethod.add(convertToColoredSpannableString(
                    ") {", Color.BLACK
            ));
            addTextViewAsClassOrMethodOpeningToLinearLayout(
                    spannablesMethod, 1);
            addTextViewAsBlankLineToLinearLayout();

            if (method.getName().equals(ProjectViewportFragment.METHOD_NAME_MAIN)) {
                addTextViewAsBlankLineToLinearLayout();
            } else {
                List<Spannable> spannablesMain = new ArrayList<>();
                spannablesMain.add(new SpannableString("..."));
                addTextViewAsClassOrMethodOpeningToLinearLayout(
                        spannablesMain, 2);
            }

            addTextViewAsClassOrMethodClosingToLinearLayout(1);
            addTextViewAsBlankLineToLinearLayout();
        }
        /////////////////////////////////////////

        ////////CLIPPIT-MESSAGE//////////////////
        Spannable spannableClippitMessage = convertToColoredSpannableString(
                "clippit says hello!", Color.CYAN);
        List<Spannable> spannablesClippitMessage = new ArrayList<>();
        spannablesClippitMessage.add(spannableClippitMessage);
        addTextViewAsClassOrMethodOpeningToLinearLayout(spannablesClippitMessage, 1);
        /////////////////////////////////////////

        ////////CLASS-CLOSING////////////////////
        addTextViewAsBlankLineToLinearLayout();
        addTextViewAsClassOrMethodClosingToLinearLayout(0);
        /////////////////////////////////////////

//        String xOffset = "";
//        StringBuilder sb = new StringBuilder();
//        sb.append("public class " + classToEdit.getName() + " {\n");
//
//        sb.append("\n");
//        xOffset = "    ";
//        List<Field> fields = classToEdit.getFields();
//        for (Field field : fields) {
//            sb.append(xOffset +
//                    field.getAccessModifier().name().toLowerCase() + " " +
//                    field.getType() + " " +
//                    field.getName() +
//                    ";\n");
//        }
//
//        sb.append("\n");
//        List<Method> methods = classToEdit.getMethods();
//        for (Method method : methods) {
//            String nonAccessModifiers = null;
//            if (method.getNonAccessModifiers() != null) {
//                for (ClassComponent.NonAccessModifier nonAccessModifier : method.getNonAccessModifiers()) {
//                    if (nonAccessModifiers == null) {
//                        nonAccessModifiers = (nonAccessModifier.name().toLowerCase() + " ");
//                    } else {
//                        nonAccessModifiers += (nonAccessModifier.name().toLowerCase() + " ");
//                    }
//                }
//            }
//            String argumentList = null;
//            if (method.getArgumentList() != null) {
//                for (VariableDeclaration variableDeclaration : method.getArgumentList()) {
//                    if (argumentList != null) {
//                        argumentList += ", " + (variableDeclaration.getType() + " " + variableDeclaration.getName());
//                    } else {
//                        argumentList = (variableDeclaration.getType() + " " + variableDeclaration.getName());
//                    }
//                }
//            }
//
//            sb.append(xOffset +
//                    method.getAccessModifier().name().toLowerCase() + " ");
//            if (nonAccessModifiers != null) {
//                sb.append(nonAccessModifiers + " ");
//            }
//            sb.append(method.getType() + " " +
//                    method.getName() +
//                    " (");
//            if (argumentList != null) {
//                sb.append(argumentList);
//            }
//            sb.append(") {\n");
//            if (method.getName().equals(ProjectViewportFragment.METHOD_NAME_MAIN)) {
//                sb.append("\n");
//            } else {
//                sb.append(xOffset + xOffset + "...\n");
//            }
//            sb.append(xOffset + "}\n");
//        }
//
//        sb.append("\n");
//        sb.append("}");
    }

    public void renameClass(Class classNew) {
        classToEdit.setName(
                classNew.getName()
        );
    }
}