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
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.EditTextDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.SpinnerDialogFragment;
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
    private static final String ARG_CLASS_VP2_ADAPTER = "classVP2Adapter";
    private static final String ARG_CLASS = "class";

    // TODO: Rename and change types of parameters
    private ClassVP2Adapter adapter;
    private Class classToEdit;
    private LinearLayout linearLayoutParent;
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
    public static ClassEditorFragment newInstance(ClassVP2Adapter adapter, Class classToEdit) {
        ClassEditorFragment fragment = new ClassEditorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS_VP2_ADAPTER, adapter);
        args.putSerializable(ARG_CLASS, classToEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adapter = (ClassVP2Adapter) getArguments().getSerializable(ARG_CLASS_VP2_ADAPTER);
            classToEdit = (Class) getArguments().getSerializable(ARG_CLASS);

            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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

    private void addTextViewAsBlankLineToLinearLayout() {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams);

        linearLayoutParent.addView(textView);
    }

    private void initLinesOfClassOpening(String nameClassToEdit) {
        LinearLayout llChild = new LinearLayout(getContext());
        llChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llChild.setOrientation(LinearLayout.HORIZONTAL);

        Spannable publicClass = convertToColoredSpannableString(
                "public class ", Color.BLUE);
        TextView tvPublicClass = new TextView(getContext());
        tvPublicClass.setLayoutParams(layoutParams);
        tvPublicClass.setText(publicClass);
        llChild.addView(tvPublicClass);

        Spannable nameClass = convertToColoredSpannableString(
                nameClassToEdit, Color.GREEN);
        TextView tvNameClass = new TextView(getContext());
        tvNameClass.setLayoutParams(layoutParams);
        tvNameClass.setText(nameClass);
        tvNameClass.append(" ");
        llChild.addView(tvNameClass);

        Spannable bracketCurlyOpen = convertToColoredSpannableString(
                "{", Color.BLACK);
        TextView tvBracketCurlyOpen = new TextView(getContext());
        tvBracketCurlyOpen.setLayoutParams(layoutParams);
        tvBracketCurlyOpen.setText(bracketCurlyOpen);
        llChild.addView(tvBracketCurlyOpen);

        linearLayoutParent.addView(llChild);

        addTextViewAsBlankLineToLinearLayout();
    }

    private void initLinesOfFields(List<Field> fields) {
        for (Field field : fields) {
            LinearLayout llChild = new LinearLayout(getContext());
            llChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llChild.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvIndent = new TextView(getContext());
            tvIndent.setLayoutParams(layoutParams);
            tvIndent.setText("    ");
            llChild.addView(tvIndent);

            // do NOT show AcessModifier.DEFAULT.
            String accessModifier = field.getAccessModifier().name().toLowerCase();
            if (!accessModifier.equals(
                    ClassComponent.AccessModifier.DEFAULT.name().toLowerCase())) {
                Spannable spannableAccessModifier = convertToColoredSpannableString(
                        accessModifier, Color.BLUE);
                TextView tvAccessModifier = new TextView(getContext());
                tvAccessModifier.setLayoutParams(layoutParams);
                tvAccessModifier.setText(spannableAccessModifier);
                tvAccessModifier.append(" ");
                llChild.addView(tvAccessModifier);
            }

            if (field.getClassInterfaceAndObjectRelateds() != null) {
                for (ClassComponent.ClassInterfaceAndObjectRelated nonAccessModifier : field.getClassInterfaceAndObjectRelateds()) {
                    String nonAccessModifiers = nonAccessModifier.name().toLowerCase();
                    Spannable spannableNonAccessModifiers = convertToColoredSpannableString(
                            nonAccessModifiers, Color.BLUE);

                    TextView tvNonAccessModifiers = new TextView(getContext());
                    tvNonAccessModifiers.setLayoutParams(layoutParams);
                    tvNonAccessModifiers.setText(spannableNonAccessModifiers);
                    tvNonAccessModifiers.append(" ");
                    llChild.addView(tvNonAccessModifiers);
                }
            }

            String type = field.getType();
            Spannable spannableType = convertToColoredSpannableString(
                    type, Color.RED);
            TextView tvType = new TextView(getContext());
            tvType.setLayoutParams(layoutParams);
            tvType.setText(spannableType);
            tvType.append(" ");
            tvType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] dataTypeAsString = new String[ClassComponent.DataType.values().length];
                    int indexDefault = -1;
                    for (int i = 0; i < ClassComponent.DataType.values().length; i++) {
                        dataTypeAsString[i] = ClassComponent.DataType.values()[i].toString();

                        if (type.equals(ClassComponent.DataType.values()[i].toString().toLowerCase())) {
                            Log.e(TAG, "type: " + type);
                            indexDefault = i;
                            Log.e(TAG, "indexDefault: " + indexDefault);
                        }
                    }

                    SpinnerDialogFragment spinnerDialogFragment = SpinnerDialogFragment.newInstance(
                            dataTypeAsString,
                            indexDefault,
                            new SpinnerDialogFragment.ItemSelectionListener() {
                                @Override
                                public void onDismiss() {
                                    // TODO:
                                }

                                @Override
                                public void onItemSelected(int indexSelected) {
                                    String itemSelectedAsString = dataTypeAsString[indexSelected].toLowerCase() + " ";

                                    // update self.
                                    Spannable spannableName = convertToColoredSpannableString(
                                            itemSelectedAsString, Color.RED);
                                    tvType.setText(spannableName);

                                    // update StructureViewportFragment.
                                    for (Field fieldFromClass : classToEdit.getFields()) {
                                        if (fieldFromClass.getName().equals(field.getName())) {
                                            adapter.changeFieldType(classToEdit, fieldFromClass, itemSelectedAsString);
                                            break;
                                        }
                                    }
                                }
                            });

                    spinnerDialogFragment.show(getChildFragmentManager(), SpinnerDialogFragment.TAG);
                }
            });
            llChild.addView(tvType);

            String nameField = field.getName();
            Spannable spannableNameField = convertToColoredSpannableString(
                    nameField, Color.GREEN);
            TextView tvNameField = new TextView(getContext());
            tvNameField.setLayoutParams(layoutParams);
            tvNameField.setText(spannableNameField);
            tvNameField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(new EditTextDialogFragment.EnterListener() {
                        @Override
                        public void onDismiss() {
                            // TODO:
                        }

                        @Override
                        public void onEnterKeyPressed(String name) {
                            // update self.
                            Spannable spannableName = convertToColoredSpannableString(
                                    name, Color.GREEN);
                            tvNameField.setText(spannableName);

                            // update StructureViewportFragment.
                            for (Field field : classToEdit.getFields()) {
                                if (field.getName().equals(nameField)) {
                                    adapter.renameField(classToEdit, field, name);
                                    break;
                                }
                            }
                        }
                    });

                    editTextDialogFragment.show(getChildFragmentManager(), EditTextDialogFragment.TAG);
                }
            });
            llChild.addView(tvNameField);

            // TODO: implement differentiation between value == null and value != null.
            TextView tvPostFixNameField = new TextView(getContext());
            tvPostFixNameField.setLayoutParams(layoutParams);
            if (field.getValue() == null) {
                String postFixNameField = ";";
                Spannable spannablePostFixNameField = convertToColoredSpannableString(
                        postFixNameField, Color.BLACK
                );
                tvPostFixNameField.setText(spannablePostFixNameField);
            } else {
                String postFixNameFieldWithValueOpening = " = ";
                String postFixNameFieldWithValueMiddle = field.getValue();
                String postFixNameFieldWithValueClosing = ";";
                Spannable spannableOpening = convertToColoredSpannableString(
                        postFixNameFieldWithValueOpening, Color.BLACK
                );
                Spannable spannableMiddle = convertToColoredSpannableString(
                        postFixNameFieldWithValueMiddle, Color.CYAN
                );
                Spannable spannableClosing = convertToColoredSpannableString(
                        postFixNameFieldWithValueClosing, Color.BLACK
                );
                tvPostFixNameField.setText(spannableOpening);
                tvPostFixNameField.append(spannableMiddle);
                tvPostFixNameField.append(spannableClosing);
            }
            llChild.addView(tvPostFixNameField);

            linearLayoutParent.addView(llChild);

            addTextViewAsBlankLineToLinearLayout();
        }
    }

    private void initLinesOfMethods(List<Method> methods) {
        for (Method method : methods) {
            LinearLayout llChild = new LinearLayout(getContext());
            llChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llChild.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvIndent = new TextView(getContext());
            tvIndent.setLayoutParams(layoutParams);
            tvIndent.setText("    ");
            llChild.addView(tvIndent);

            String accessModifier = method.getAccessModifier().name().toLowerCase();
            Spannable spannableAccessModifier = convertToColoredSpannableString(
                    accessModifier, Color.BLUE);

            TextView tvAccessModifier = new TextView(getContext());
            tvAccessModifier.setLayoutParams(layoutParams);
            tvAccessModifier.setText(spannableAccessModifier);
            tvAccessModifier.append(" ");
            llChild.addView(tvAccessModifier);

            if (method.getClassInterfaceAndObjectRelateds() != null) {
                for (ClassComponent.ClassInterfaceAndObjectRelated nonAccessModifier : method.getClassInterfaceAndObjectRelateds()) {
                    String nonAccessModifiers = nonAccessModifier.name().toLowerCase();
                    Spannable spannableNonAccessModifiers = convertToColoredSpannableString(
                            nonAccessModifiers, Color.BLUE);

                    TextView tvNonAccessModifiers = new TextView(getContext());
                    tvNonAccessModifiers.setLayoutParams(layoutParams);
                    tvNonAccessModifiers.setText(spannableNonAccessModifiers);
                    tvNonAccessModifiers.append(" ");
                    llChild.addView(tvNonAccessModifiers);
                }
            }

            String returnType = method.getType();
            Spannable spannableReturnType = convertToColoredSpannableString(
                    returnType, Color.RED);

            TextView tvReturnType = new TextView(getContext());
            tvReturnType.setLayoutParams(layoutParams);
            tvReturnType.setText(spannableReturnType);
            tvReturnType.append(" ");
            llChild.addView(tvReturnType);

            tvReturnType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (method.getName().equals(ProjectViewportFragment.METHOD_NAME_MAIN)) {
                        Log.e(TAG, "changing return type for main() is NOT allowed!!!");
                        return;
                    }

                    String[] dataTypeAsString = new String[ClassComponent.DataType.values().length];
                    int indexDefault = -1;
                    for (int i = 0; i < ClassComponent.DataType.values().length; i++) {
                        dataTypeAsString[i] = ClassComponent.DataType.values()[i].toString();

                        if (returnType.equals(ClassComponent.DataType.values()[i].toString().toLowerCase())) {
                            Log.e(TAG, "returnType: " + returnType);
                            indexDefault = i;
                            Log.e(TAG, "indexDefault: " + indexDefault);
                        }
                    }

                    SpinnerDialogFragment spinnerDialogFragment = SpinnerDialogFragment.newInstance(
                            dataTypeAsString,
                            indexDefault,
                            new SpinnerDialogFragment.ItemSelectionListener() {
                                @Override
                                public void onDismiss() {
                                    // TODO:
                                }

                                @Override
                                public void onItemSelected(int indexSelected) {
                                    String itemSelectedAsString = dataTypeAsString[indexSelected].toLowerCase() + " ";

                                    // update self.
                                    Spannable spannableName = convertToColoredSpannableString(
                                            itemSelectedAsString, Color.RED);
                                    tvReturnType.setText(spannableName);

                                    // update StructureViewportFragment.
                                    for (Method methodFromClass : classToEdit.getMethods()) {
                                        if (methodFromClass.getName().equals(method.getName())) {
                                            adapter.changeMethodReturnType(classToEdit, methodFromClass, itemSelectedAsString);
                                            break;
                                        }
                                    }
                                }
                            });

                    spinnerDialogFragment.show(getChildFragmentManager(), SpinnerDialogFragment.TAG);
                }
            });

            String nameMethod = method.getName();
            Spannable spannableNameMethod = convertToColoredSpannableString(
                    nameMethod, Color.GREEN);

            TextView tvNameMethod = new TextView(getContext());
            tvNameMethod.setLayoutParams(layoutParams);
            tvNameMethod.setText(spannableNameMethod);
            tvNameMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (method.getName().equals(ProjectViewportFragment.METHOD_NAME_MAIN)) {
                        Log.e(TAG, "renaming main() is NOT allowed!!!");
                        return;
                    }

                    EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(new EditTextDialogFragment.EnterListener() {
                        @Override
                        public void onDismiss() {
                            // TODO:
                        }

                        @Override
                        public void onEnterKeyPressed(String name) {
                            // update self.
                            Spannable spannableName = convertToColoredSpannableString(
                                    name, Color.GREEN);
                            tvNameMethod.setText(spannableName);

                            // update StructureViewportFragment.
                            for (Method methodOfClass : classToEdit.getMethods()) {
                                if (methodOfClass.getName().equals(nameMethod)) {
                                    adapter.renameMethod(classToEdit, methodOfClass, name);
                                }
                            }
                        }
                    });

                    editTextDialogFragment.show(getChildFragmentManager(), EditTextDialogFragment.TAG);
                }
            });
            llChild.addView(tvNameMethod);

            String parenthesisOpen = "(";
            TextView tvParenthesisOpen = new TextView(getContext());
            tvParenthesisOpen.setLayoutParams(layoutParams);
            tvParenthesisOpen.setText(parenthesisOpen);
            llChild.addView(tvParenthesisOpen);

            if (method.getArgumentList() != null) {
                List<VariableDeclaration> argumentList = method.getArgumentList();
                for (int i = 0; i < argumentList.size(); i++) {
                    VariableDeclaration variableDeclaration = argumentList.get(i);

                    String type = variableDeclaration.getType();
                    Spannable spannableType = convertToColoredSpannableString(
                            type, Color.RED);

                    TextView tvType = new TextView(getContext());
                    tvType.setLayoutParams(layoutParams);
                    tvType.setText(spannableType);
                    tvType.append(" ");
                    llChild.addView(tvType);

                    String name = variableDeclaration.getName();
                    Spannable spannableName = convertToColoredSpannableString(
                            name, Color.CYAN);

                    TextView tvName = new TextView(getContext());
                    tvName.setLayoutParams(layoutParams);
                    tvName.setText(spannableName);
                    llChild.addView(tvName);

                    if (i < argumentList.size() - 1) {
                        String commaAndSpace = ", ";
                        TextView tvCommaAndSpace = new TextView(getContext());
                        tvCommaAndSpace.setLayoutParams(layoutParams);
                        tvCommaAndSpace.setText(commaAndSpace);
                        llChild.addView(tvCommaAndSpace);
                    }
                }
            }

            String parenthesisClose = ") {";
            TextView tvParenthesisClose = new TextView(getContext());
            tvParenthesisClose.setLayoutParams(layoutParams);
            tvParenthesisClose.setText(parenthesisClose);
            llChild.addView(tvParenthesisClose);

            linearLayoutParent.addView(llChild);

            if (method.getName().equals(ProjectViewportFragment.METHOD_NAME_MAIN)) {
                List<String> lines = new ArrayList<>();
                int indexLastNewLine = 0;
                for (int i = 0; i < method.getBody().length(); i++) {
                    if (method.getBody().substring(i, i + 1).equals("\n")) {
                        lines.add(
                                method.getBody().substring(indexLastNewLine, i + 1)
                        );

                        indexLastNewLine = i + 1;
                    } else if (i == method.getBody().length() - 1) {
                        lines.add(
                                method.getBody().substring(indexLastNewLine, method.getBody().length())
                        );
                    }
                }

                StringBuilder sb = new StringBuilder();
                for (String line : lines) {
                    Log.e(TAG, "line: " + line);
                    sb.append("        " + line);
                }

                TextView tvMethodBody = new TextView(getContext());
                tvMethodBody.setLayoutParams(layoutParams);
                tvMethodBody.setText(sb.toString());
                linearLayoutParent.addView(tvMethodBody);

                tvMethodBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(new EditTextDialogFragment.EnterListener() {
                            @Override
                            public void onDismiss() {
                                // TODO:
                            }

                            @Override
                            public void onEnterKeyPressed(String name) {
                                // param name should actually be bodyMethodNew.
                                method.setBody(name);
                                tvMethodBody.setText("        " + name);
                            }
                        });

                        editTextDialogFragment.show(getChildFragmentManager(), EditTextDialogFragment.TAG);
                    }
                });
            } else {
                String ellipsisWithTwoIndents = "        ...";
                TextView tvEllipsisWithTwoIndents = new TextView(getContext());
                tvEllipsisWithTwoIndents.setLayoutParams(layoutParams);
                tvEllipsisWithTwoIndents.setText(ellipsisWithTwoIndents);
                linearLayoutParent.addView(tvEllipsisWithTwoIndents);

                tvEllipsisWithTwoIndents.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(new EditTextDialogFragment.EnterListener() {
                            @Override
                            public void onDismiss() {
                                // TODO:
                            }

                            @Override
                            public void onEnterKeyPressed(String name) {
                                // param name should actually be bodyMethodNew.
                                method.setBody(name);
                                tvEllipsisWithTwoIndents.setText("        " + name);
                            }
                        });

                        editTextDialogFragment.show(getChildFragmentManager(), EditTextDialogFragment.TAG);
                    }
                });
            }

            TextView tvCurlyBracketCloseWithIndent = new TextView(getContext());
            tvCurlyBracketCloseWithIndent.setLayoutParams(layoutParams);
            tvCurlyBracketCloseWithIndent.setText("    }");
            linearLayoutParent.addView(tvCurlyBracketCloseWithIndent);

            addTextViewAsBlankLineToLinearLayout();
        }

    }

    private void initLinesOfClassClosing() {
        TextView tvCurlyBracketClose = new TextView(getContext());
        tvCurlyBracketClose.setLayoutParams(layoutParams);
        tvCurlyBracketClose.setText("}");
        linearLayoutParent.addView(tvCurlyBracketClose);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutParent = view.findViewById(R.id.ll_class);

        initClassEditorFragment();
    }

    private void initClassEditorFragment() {
        // CLASS-OPENING
        String nameClassToEdit = classToEdit.getName();
        initLinesOfClassOpening(nameClassToEdit);

        // FIELDS
        List<Field> fields = classToEdit.getFields();
        initLinesOfFields(fields);

        // METHODS
        List<Method> methods = classToEdit.getMethods();
        initLinesOfMethods(methods);

        // CLIPPIT-MESSAGE
        Spannable spannableClippitMessage = convertToColoredSpannableString(
                "    clippit says hello!", Color.CYAN);
        TextView tvClippitMessage = new TextView(getContext());
        tvClippitMessage.setLayoutParams(layoutParams);
        tvClippitMessage.setText(spannableClippitMessage);
        linearLayoutParent.addView(tvClippitMessage);

        // CLASS-CLOSING
        initLinesOfClassClosing();
    }

    public void renameClass(Class classNew) {
        classToEdit.setName(
                classNew.getName()
        );

        linearLayoutParent.removeAllViews();

        initClassEditorFragment();
    }
}