package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.VariableDeclaration;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.ClassComponent;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassEditorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLASS = "class";

    // TODO: Rename and change types of parameters
    private Class classToEdit;
    private EditText editText;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_editor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String xOffset = "";
        StringBuilder sb = new StringBuilder();
        sb.append("public class " + classToEdit.getName() + " {\n");

        sb.append("\n");
        xOffset = "    ";
        List<Field> fields = classToEdit.getFields();
        for (Field field : fields) {
            sb.append(xOffset +
                    field.getAccessModifier().name().toLowerCase() + " " +
                    field.getType() + " " +
                    field.getName() +
                    ";\n");
        }

        sb.append("\n");
        List<Method> methods = classToEdit.getMethods();
        for (Method method : methods) {
            String nonAccessModifiers = null;
            for (ClassComponent.NonAccessModifier nonAccessModifier : method.getNonAccessModifiers()) {
                if (nonAccessModifiers == null) {
                    nonAccessModifiers = (nonAccessModifier.name().toLowerCase() + " ");
                } else {
                    nonAccessModifiers += (nonAccessModifier.name().toLowerCase() + " ");
                }
            }
            String argumentList = null;
            for (VariableDeclaration variableDeclaration : method.getArgumentList()) {
                if (argumentList != null) {
                    argumentList += ", " + (variableDeclaration.getType() + " " + variableDeclaration.getName());
                } else {
                    argumentList = (variableDeclaration.getType() + " " + variableDeclaration.getName());
                }
            }

            sb.append(xOffset +
                    method.getAccessModifier().name().toLowerCase() + " ");
            if (nonAccessModifiers != null) {
                sb.append(nonAccessModifiers + " ");
            }
            sb.append(method.getType() + " " +
                    method.getName() +
                    " (");
            if (argumentList != null) {
                sb.append(argumentList);
            }
            sb.append(") {\n");
            sb.append("\n");
            sb.append(xOffset + "}\n");
        }

        sb.append("\n");
        sb.append("}");

        editText = view.findViewById(R.id.et_class);
        if (sb.length() > 0) {
            editText.setText(sb.toString());
        } else {
            editText.setHint(
                    classToEdit.getName()
            );
        }
    }

    public void renameClass(Class classNew) {
        classToEdit.setName(
                classNew.getName()
        );
        editText.setHint(
                classToEdit.getName()
        );
    }
}