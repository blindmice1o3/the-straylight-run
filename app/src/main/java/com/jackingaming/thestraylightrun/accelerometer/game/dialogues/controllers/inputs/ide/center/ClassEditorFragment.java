package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import android.annotation.SuppressLint;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.HorizontalScrollView;
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
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Constructor;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassEditorFragment extends Fragment {
    public static final String TAG = ClassEditorFragment.class.getSimpleName();
    public static final int DEFAULT_INDENT_SIZE = 48;
    public static final String DEFAULT_INDENT = "    ";
    public static final String IDENTIFIER_COMMENT_SINGLE_LINE = "//";
    public static final String IDENTIFIER_COMMENT_TODO = "todo";

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

    private void initLinesOfClassOpening() {
        // COMMENT
        if (classToEdit.getComment() != null) {
            LinearLayout llComment = new LinearLayout(getContext());
            llComment.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llComment.setOrientation(LinearLayout.HORIZONTAL);
            String comment = classToEdit.getComment();
            Spannable spannableComment = convertToColoredSpannableString(
                    comment, Color.GRAY);
            TextView tvComment = new TextView(getContext());
            tvComment.setLayoutParams(layoutParams);
            tvComment.setText(spannableComment);
            llComment.addView(tvComment);
            linearLayoutParent.addView(llComment);
        }

        LinearLayout llChild = new LinearLayout(getContext());
        llChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llChild.setOrientation(LinearLayout.HORIZONTAL);

        String accessModifierAndKeywordClass = classToEdit.getAccessModifier().name().toLowerCase() + " class ";
        Spannable spannableAccessModifierAndKeywordClass =
                convertToColoredSpannableString(
                        accessModifierAndKeywordClass, Color.BLUE);
        TextView tvAccessModifierAndKeywordClass = new TextView(getContext());
        tvAccessModifierAndKeywordClass.setLayoutParams(layoutParams);
        tvAccessModifierAndKeywordClass.setText(spannableAccessModifierAndKeywordClass);
        llChild.addView(tvAccessModifierAndKeywordClass);

        String nameClass = classToEdit.getName();
        Spannable spannablenameClass = convertToColoredSpannableString(
                nameClass, Color.GREEN);
        TextView tvNameClass = new TextView(getContext());
        tvNameClass.setLayoutParams(layoutParams);
        tvNameClass.setText(spannablenameClass);
        tvNameClass.append(" ");
        llChild.addView(tvNameClass);

        Spannable bracketCurlyOpen = convertToColoredSpannableString(
                "{", Color.BLACK);
        TextView tvBracketCurlyOpen = new TextView(getContext());
        tvBracketCurlyOpen.setLayoutParams(layoutParams);
        tvBracketCurlyOpen.setText(bracketCurlyOpen);
        llChild.addView(tvBracketCurlyOpen);

        linearLayoutParent.addView(llChild);
    }

    @SuppressLint("ResourceAsColor")
    private void initLinesOfFields() {
        for (Field field : classToEdit.getFields()) {
            LinearLayout llChild = new LinearLayout(getContext());
            llChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llChild.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvIndent = new TextView(getContext());
            tvIndent.setLayoutParams(layoutParams);
            tvIndent.setText(DEFAULT_INDENT);
            llChild.addView(tvIndent);

            // do NOT show AccessModifier.DEFAULT.
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

            if (field.getComment() != null) {
                String comment = field.getComment();
                Spannable spannableComment = convertToColoredSpannableString(
                        comment, Color.GRAY
                );
                TextView tvComment = new TextView(getContext());
                tvComment.setLayoutParams(layoutParams);
                tvComment.setText(spannableComment);
                llChild.addView(tvComment);
            }

            HorizontalScrollView scroll = new HorizontalScrollView(getContext());
            scroll.setBackgroundColor(android.R.color.transparent);
            scroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            scroll.setHorizontalScrollBarEnabled(false);
            scroll.addView(llChild);
            linearLayoutParent.addView(
                    scroll
            );
        }
    }

    private void initLinesOfConstructors() {
        for (Constructor constructor : classToEdit.getConstructors()) {
            addTextViewAsBlankLineToLinearLayout();

            // COMMENT
            if (constructor.getComment() != null) {
                LinearLayout llConstructor = new LinearLayout(getContext());
                llConstructor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llConstructor.setOrientation(LinearLayout.HORIZONTAL);
                String comment = DEFAULT_INDENT + constructor.getComment();
                Spannable spannableComment = convertToColoredSpannableString(
                        comment, Color.GRAY);
                TextView tvComment = new TextView(getContext());
                tvComment.setLayoutParams(layoutParams);
                tvComment.setText(spannableComment);
                llConstructor.addView(tvComment);
                linearLayoutParent.addView(llConstructor);
            }

            LinearLayout llChild = new LinearLayout(getContext());
            llChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llChild.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvIndent = new TextView(getContext());
            tvIndent.setLayoutParams(layoutParams);
            tvIndent.setText(DEFAULT_INDENT);
            llChild.addView(tvIndent);

            String accessModifier = constructor.getAccessModifier().name().toLowerCase();
            Spannable spannableAccessModifier = convertToColoredSpannableString(
                    accessModifier, Color.BLUE);
            TextView tvAccessModifier = new TextView(getContext());
            tvAccessModifier.setLayoutParams(layoutParams);
            tvAccessModifier.setText(spannableAccessModifier);
            tvAccessModifier.append(" ");
            llChild.addView(tvAccessModifier);

            String nameConstructor = constructor.getName();
            Spannable spannableNameConstructor = convertToColoredSpannableString(
                    nameConstructor, Color.GREEN);
            TextView tvNameConstructor = new TextView(getContext());
            tvNameConstructor.setLayoutParams(layoutParams);
            tvNameConstructor.setText(spannableNameConstructor);
            llChild.addView(tvNameConstructor);

            String parenthesisOpen = "(";
            TextView tvParenthesisOpen = new TextView(getContext());
            tvParenthesisOpen.setLayoutParams(layoutParams);
            tvParenthesisOpen.setText(parenthesisOpen);
            llChild.addView(tvParenthesisOpen);

            if (constructor.getArgumentList() != null) {
                List<VariableDeclaration> argumentList = constructor.getArgumentList();
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

            TextView tvConstructorBody = new TextView(getContext());
            tvConstructorBody.setLayoutParams(layoutParams);
            tvConstructorBody.setText(constructor.getBody());
            linearLayoutParent.addView(tvConstructorBody);

            TextView tvCurlyBracketCloseWithIndent = new TextView(getContext());
            tvCurlyBracketCloseWithIndent.setLayoutParams(layoutParams);
            tvCurlyBracketCloseWithIndent.setText(DEFAULT_INDENT + "}");
            linearLayoutParent.addView(tvCurlyBracketCloseWithIndent);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void initLinesOfMethods() {
        for (Method method : classToEdit.getMethods()) {
            addTextViewAsBlankLineToLinearLayout();

            // COMMENT
            if (method.getComment() != null) {
                LinearLayout llComment = new LinearLayout(getContext());
                llComment.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llComment.setOrientation(LinearLayout.HORIZONTAL);
                String comment = DEFAULT_INDENT + method.getComment();
                Spannable spannableComment = convertToColoredSpannableString(
                        comment, Color.GRAY);
                TextView tvComment = new TextView(getContext());
                tvComment.setLayoutParams(layoutParams);
                tvComment.setText(spannableComment);
                llComment.addView(tvComment);

                HorizontalScrollView scroll = new HorizontalScrollView(getContext());
                scroll.setBackgroundColor(android.R.color.transparent);
                scroll.setHorizontalScrollBarEnabled(false);
                scroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                scroll.addView(llComment);
                linearLayoutParent.addView(
                        scroll
                );
            }

            LinearLayout llChild = new LinearLayout(getContext());
            llChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llChild.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvIndent = new TextView(getContext());
            tvIndent.setLayoutParams(layoutParams);
            tvIndent.setText(DEFAULT_INDENT);
            llChild.addView(tvIndent);

            // ACCESS-MODIFIERS
            String accessModifier = method.getAccessModifier().name().toLowerCase();
            Spannable spannableAccessModifier = convertToColoredSpannableString(
                    accessModifier, Color.BLUE);
            TextView tvAccessModifier = new TextView(getContext());
            tvAccessModifier.setLayoutParams(layoutParams);
            tvAccessModifier.setText(spannableAccessModifier);
            tvAccessModifier.append(" ");
            llChild.addView(tvAccessModifier);

            // NON-ACCESS-MODIFIERS
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

            // RETURN-TYPE
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

            // NAME
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

            // SEPARATOR OF NAME AND ARGUMENT-LIST
            String parenthesisOpen = "(";
            TextView tvParenthesisOpen = new TextView(getContext());
            tvParenthesisOpen.setLayoutParams(layoutParams);
            tvParenthesisOpen.setText(parenthesisOpen);
            llChild.addView(tvParenthesisOpen);

            // ARGUMENT-LIST
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

            // SEPARATOR OF ARGUMENT-LIST AND BODY
            String parenthesisClose = ") {";
            TextView tvParenthesisClose = new TextView(getContext());
            tvParenthesisClose.setLayoutParams(layoutParams);
            tvParenthesisClose.setText(parenthesisClose);
            llChild.addView(tvParenthesisClose);

            HorizontalScrollView scroll = new HorizontalScrollView(getContext());
            scroll.setBackgroundColor(android.R.color.transparent);
            scroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            scroll.setHorizontalScrollBarEnabled(false);
            scroll.addView(llChild);
            linearLayoutParent.addView(
                    scroll
            );

            // BODY
            String bodyMethod = method.getBody();

            /////////////////////////////////
            int counterOpenBracketCurly = 0;
            int counterCloseBracketCurly = 0;
            int counterOpenBracketRound = 0;
            int counterCloseBracketRound = 0;
            for (int i = 0; i < bodyMethod.length() - 1; i++) {
                String currentChar = bodyMethod.substring(i, i + 1);
                if (currentChar.equals("{")) {
                    counterOpenBracketCurly++;
                } else if (currentChar.equals("}")) {
                    counterCloseBracketCurly++;
                } else if (currentChar.equals("(")) {
                    counterOpenBracketRound++;
                } else if (currentChar.equals(")")) {
                    counterCloseBracketRound++;
                }
            }
            Log.e(TAG, "counterOpenBracketCurly: " + counterOpenBracketCurly);
            Log.e(TAG, "counterCloseBracketCurly: " + counterCloseBracketCurly);
            Log.e(TAG, "counterOpenBracketRound: " + counterOpenBracketRound);
            Log.e(TAG, "counterCloseBracketRound: " + counterCloseBracketRound);
            /////////////////////////////////

            String[] bodyMethodSplitedByNewLine = bodyMethod.split("\\n");
            for (int i = 0; i < bodyMethodSplitedByNewLine.length; i++) {
                String line = bodyMethodSplitedByNewLine[i];

                LinearLayout llLine = new LinearLayout(getContext());
                llLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llLine.setOrientation(LinearLayout.HORIZONTAL);

                boolean hasComment = false;
                int indexComment = -1;
                for (int j = 0; j < line.length(); j++) {
                    // scan line for single line comment (prevents index out of bounds).
                    if (j + IDENTIFIER_COMMENT_SINGLE_LINE.length() - 1 < line.length()) {
                        // FOUND COMMENT
                        if (line.substring(j, j + IDENTIFIER_COMMENT_SINGLE_LINE.length()).equals(IDENTIFIER_COMMENT_SINGLE_LINE)) {
                            hasComment = true;
                            indexComment = j;
                            break;
                        }
                    }
                }

                boolean hasTODO = false;
                int indexTODO = -1;
                if (hasComment) {
                    for (int k = indexComment; k < line.length(); k++) {
                        // scan line for TO-DO (prevents index out of bounds).
                        if (k + IDENTIFIER_COMMENT_TODO.length() - 1 < line.length()) {
                            // FOUND TO-DO
                            if (line.substring(k, k + IDENTIFIER_COMMENT_TODO.length()).toLowerCase().equals(IDENTIFIER_COMMENT_TODO)) {
                                hasTODO = true;
                                indexTODO = k;
                                break;
                            }
                        }
                    }
                }

                if (hasTODO) {
                    TextView tvLineAfterTODO = new TextView(getContext());
                    tvLineAfterTODO.setLayoutParams(layoutParams);
                    Spannable spannableLineAfterTODO = convertToColoredSpannableString(
                            line.substring(indexTODO), Color.CYAN);
                    tvLineAfterTODO.setText(spannableLineAfterTODO);

                    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                    if (method.getName().equals("updateGrowth")) {
                        tvLineAfterTODO.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                int indexToInsert = linearLayoutParent.indexOfChild(
                                        (LinearLayout) view.getParent()
                                ) + 1;

                                String answer = DEFAULT_INDENT + DEFAULT_INDENT + "if (lightCycleCorrect && !pestsPresent) {\n" +
                                        DEFAULT_INDENT + DEFAULT_INDENT + DEFAULT_INDENT + "isFlowering = true;\n" +
                                        DEFAULT_INDENT + DEFAULT_INDENT + "}";
                                TextView tvAnswer = new TextView(getContext());
                                tvAnswer.setLayoutParams(layoutParams);
                                Spannable spannableAnswer = convertToColoredSpannableString(
                                        answer, Color.BLACK
                                );
                                tvAnswer.setText(spannableAnswer);

                                linearLayoutParent.addView(tvAnswer, indexToInsert);

                                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.6f);
                                anim.setDuration(2000L);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                tvAnswer.startAnimation(anim);

                                return true;
                            }
                        });
                    }
                    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

                    TextView tvLineAfterCommentBeforeTODO = new TextView(getContext());
                    tvLineAfterCommentBeforeTODO.setLayoutParams(layoutParams);
                    Spannable spannableLineAfterCommentBeforeTODO = convertToColoredSpannableString(
                            line.substring(indexComment, indexTODO), Color.GRAY
                    );
                    tvLineAfterCommentBeforeTODO.setText(spannableLineAfterCommentBeforeTODO);

                    TextView tvLineBeforeComment = new TextView(getContext());
                    tvLineBeforeComment.setLayoutParams(layoutParams);
                    Spannable spannableLineBeforeComment = convertToColoredSpannableString(
                            line.substring(0, indexComment), Color.BLACK
                    );
                    tvLineBeforeComment.setText(spannableLineBeforeComment);

                    llLine.addView(tvLineBeforeComment);
                    llLine.addView(tvLineAfterCommentBeforeTODO);
                    llLine.addView(tvLineAfterTODO);
                } else if (hasComment) {
                    TextView tvLineAfterComment = new TextView(getContext());
                    tvLineAfterComment.setLayoutParams(layoutParams);
                    Spannable spannableLineAfterComment = convertToColoredSpannableString(
                            line.substring(indexComment), Color.GRAY
                    );
                    tvLineAfterComment.setText(spannableLineAfterComment);

                    TextView tvLineBeforeComment = new TextView(getContext());
                    tvLineBeforeComment.setLayoutParams(layoutParams);
                    Spannable spannableLineBeforeComment = convertToColoredSpannableString(
                            line.substring(0, indexComment), Color.BLACK
                    );
                    tvLineBeforeComment.setText(spannableLineBeforeComment);

                    llLine.addView(tvLineBeforeComment);
                    llLine.addView(tvLineAfterComment);
                } else {
                    TextView tvLine = new TextView(getContext());
                    tvLine.setLayoutParams(layoutParams);
                    Spannable spannableLine = convertToColoredSpannableString(
                            line, Color.BLACK
                    );
                    tvLine.setText(spannableLine);

                    llLine.addView(tvLine);
                }

                linearLayoutParent.addView(
                        llLine
                );
            }

            // CLOSING
            TextView tvCurlyBracketCloseWithIndent = new TextView(getContext());
            tvCurlyBracketCloseWithIndent.setLayoutParams(layoutParams);
            tvCurlyBracketCloseWithIndent.setText("    }");
            linearLayoutParent.addView(
                    tvCurlyBracketCloseWithIndent
            );
        }
    }

    // !!!!!RECURSIVE!!!!!
    private int checkLineForNumberOfIndents(String line) {
        if (line.length() >= 4) {
            if (line.substring(0, 4).equals(DEFAULT_INDENT)) {
                String lineWithoutFrontIndent = line.substring(4);
                return 1 + checkLineForNumberOfIndents(lineWithoutFrontIndent);
            } else {
                return 0;
            }
        } else {
            return 0;
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
        initLinesOfClassOpening();

        // FIELDS
        initLinesOfFields();

        // CONSTRUCTORS
        initLinesOfConstructors();

        // METHODS
        initLinesOfMethods();

        // CLIPPIT-MESSAGE
        addTextViewAsBlankLineToLinearLayout();
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
        classToEdit.updateName(
                classNew.getName()
        );

        linearLayoutParent.removeAllViews();

        initClassEditorFragment();
    }
}