package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.left;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.EditTextDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Package;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.VariableDeclaration;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.ClassComponent;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Constructor;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectViewportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectViewportFragment extends Fragment {
    public static final String TAG = ProjectViewportFragment.class.getSimpleName();
    public static final String CLASS_NAME_MAIN = "Main";
    public static final String METHOD_NAME_MAIN = "main";
    public static final String ARG_CLASSES_DATA_OBJECT = "classesDataObject";

    public interface ProjectViewportListener {
        void onPackageClicked(Package packageClicked);

        void onClassClicked(Class classClicked);

        void onClassRenamed(Class classRenamed);

        void onClassDoubleClicked(Class classDoubleClicked);
    }

    private ProjectViewportListener listener;

    public void setProjectViewportListener(ProjectViewportListener listener) {
        this.listener = listener;
    }

    private Package packageMain;
    private List<Class> classes = new ArrayList<>();
    private TextView textView;
    private RecyclerView recyclerView;
    private ClassRVAdapterForProject classRVAdapter;

    private Class initClassMain() {
        Class classMain = new Class(ClassComponent.AccessModifier.PUBLIC,
                CLASS_NAME_MAIN, null);
        classMain.addField(new Field(ClassComponent.AccessModifier.PRIVATE,
                null,
                "int", "counter", null, null));
        List<ClassComponent.ClassInterfaceAndObjectRelated> nonAccessModifiersStatic = new ArrayList<>();
        nonAccessModifiersStatic.add(
                ClassComponent.ClassInterfaceAndObjectRelated.STATIC
        );
        List<VariableDeclaration> argumentListMain = new ArrayList<>();
        argumentListMain.add(
                new VariableDeclaration("String[]", "args")
        );
        String bodyMain = "        GrowTentSystem tent = new GrowTentSystem(3);\n" +
                "\n" +
                "        for (int i = 0; i < 24; i++) {\n" +
                "            tent.simulateHour();\n" +
                "        }\n" +
                "\n" +
                "        tent.debugStatus();";
        classMain.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                nonAccessModifiersStatic,
                "void", METHOD_NAME_MAIN,
                argumentListMain,
                bodyMain,
                null));
        return classMain;
    }

    private Class initClassFoo() {
        return new Class(ClassComponent.AccessModifier.PUBLIC,
                "Foo", null);
    }

    private Class initClassBar() {
        return new Class(ClassComponent.AccessModifier.PUBLIC,
                "Bar", null);
    }

    private Class initClassGrowTentSystem() {
        Class classGrowTentSystem = new Class(ClassComponent.AccessModifier.PUBLIC,
                "GrowTentSystem", null);

        // FIELDS
        List<ClassComponent.ClassInterfaceAndObjectRelated> nonAccessModifiersFinal = new ArrayList<>();
        nonAccessModifiersFinal.add(ClassComponent.ClassInterfaceAndObjectRelated.FINAL);
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                nonAccessModifiersFinal,
                "int", "LIGHT_START", "6", "  // 6 AM"));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                nonAccessModifiersFinal,
                "int", "LIGHT_END", "18", "   // 6 PM"));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "lightOn", null, null,
                true));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "int", "hour", null, null));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "List<Plant>", "plants", null, null,
                true));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isTentZipped", "false", null,
                true));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isLightCycleCorrect", "false", null));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "pestsDetected", "false", null));

        // CONSTRUCTORS
        List<VariableDeclaration> argumentListGrowTentSystem = new ArrayList<>();
        argumentListGrowTentSystem.add(
                new VariableDeclaration("int", "plantCount")
        );
        String bodyGrowTentSystem = "        plants = new Plant[plantCount];\n" +
                "        for (int i = 0; i < plantCount; i++) {\n" +
                "            plants[i] = new Plant(\"Plant_\" + (i + 1));\n" +
                "        }\n" +
                "\n" +
                "        //TODOlightOn = false;\n" +
                "        hour = 0;";
        classGrowTentSystem.addConstructor(new Constructor(
                ClassComponent.AccessModifier.PUBLIC,
                argumentListGrowTentSystem, bodyGrowTentSystem, null));

        String bodyGrowTentSystemWithList = "        this.plants = List.of(\n" +
                "            new Plant(\"Blue Dream\", true, true),\n" +
                "            new Plant(\"Northern Lights\", trueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee, true),\n" +
                "            new Plant(\"OG Kush\", true, true)\n" +
                "        );//todoclassics";
        classGrowTentSystem.addConstructor(new Constructor(
                ClassComponent.AccessModifier.PUBLIC,
                null,
                bodyGrowTentSystemWithList,
                null));

        // METHODS
        String bodyRunDailyCycle = "        // Only if tent is zipped should light be counted as \"correct\"\n" +
                "        // TODO: Set isLightCycleCorrect based on isTentZipped\n" +
                "\n" +
                "        // Loop through plants and update their growth\n" +
                "        // TODO: Loop through each plant and call updateGrowth()\n" +
                "\n" +
                "        // TODO: Count how many plants are ready for harvest and print result";
        classGrowTentSystem.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "runDailyCycle",
                null,
                bodyRunDailyCycle,
                null));
        String bodyUpdateLights = "    //\n" +
                "        if (hour >=// LIGHT_START toDO && hour < LIGHT_END) {\n" +
                "//TODO:            lightOn = true;\n" +
                "        } else {\n" +
                "            lightOn = false;\n" +
                "        }//";
//        String bodyUpdateLights = "        if (hour >= LIGHT_START && hour < LIGHT_END) {\n" +
//                "            lightOn = true;\n" +
//                "        } else {\n" +
//                "            lightOn = false;\n" +
//                "        }";
        classGrowTentSystem.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "updateLights",
                null,
                bodyUpdateLights,
                null));
        String bodySimulateHour = "// TODO:        updateLights();\n" +
                "//        for (Plant p : plants) {\n" +
                "            p.dryOut();\n" +
                "            if (p.needsWater()) {\n" +
                "//TODO :                p.water();\n" +
                "            }\n" +
                "        }\n" +
                "        hour = (hour + 1) % 24;";
//        String bodySimulateHour = "        updateLights();\n" +
//                "        for (Plant p : plants) {\n" +
//                "            p.dryOut();\n" +
//                "            if (p.needsWater()) {\n" +
//                "                p.water();\n" +
//                "            }\n" +
//                "        }\n" +
//                "        hour = (hour + 1) % 24;";
        classGrowTentSystem.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "simulateeeeeeeeeeeeeeeeeeeeeeeeeeeeeeHour",
                null,
                bodySimulateHour,
                null));
        String bodyDebugStatus = "        System.out.println(\"HOUR: \" + hour + \" | LIGHT ON: \" + lightOn);\n" +
                "        for (Plant p : plants) {\n" +
                "            p.printStatus();\n" +
                "        }";
        classGrowTentSystem.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "debugStatus",
                null,
                bodyDebugStatus,
                null));
        return classGrowTentSystem;
    }

    private Class initClassPlant() {
        Class classPlant = new Class(ClassComponent.AccessModifier.PUBLIC,
                "Plant_testing_long_class_nammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmme", " // Testing class's comment.. long linnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnne");
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "String", "name", null, null));
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "double", "moisture", null, " // 0.0 to 1.0"));
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isFlowering", null, " // Track if the plant has begun flowering phase"));
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isHealthy", null, " // Monitor plant health"));
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isReady", null, " // Determine if the plant is ready to harvest"));
        List<VariableDeclaration> argumentListPlant = new ArrayList<>();
        argumentListPlant.add(
                new VariableDeclaration("String", "name")
        );
        String bodyPlant = "        this.name = name; // Testing long linnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnne\n" +
                "        this.isFlowering = false; // Initialize to false\n" +
                "        this.isHealthy = true;       // Assume healthy on creation\n" +
                "        this.isReady = false;        // Harvest status begins as false";
        classPlant.addConstructor(new Constructor(ClassComponent.AccessModifier.PUBLIC,
                argumentListPlant, bodyPlant, null));
        String bodyUpdateGrowth = "    // TODO: If light is correct and pests are not present, start flowering";
        List<VariableDeclaration> argumentListUpdateGrowth = new ArrayList<>();
        argumentListUpdateGrowth.add(
                new VariableDeclaration("boolean", "lightCycleCorrect")
        );
        argumentListUpdateGrowth.add(
                new VariableDeclaration("boolean", "pestsPresent")
        );
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "updateGrowth",
                argumentListUpdateGrowth,
                bodyUpdateGrowth,
                null));
        String bodyDryOut = "        moisture -= 0.05;\n" +
                "        if (moisture < 0.0) {\n" +
                "            moisture = 0.0;\n" +
                "        }";
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "dryOut",
                null,
                bodyDryOut,
                null));
        String bodyNeedsWater = "        return moisture < 0.3;";
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "boolean", "needsWater",
                null,
                bodyNeedsWater,
                null));
        String bodyWater = "        moisture += 0.4;\n" +
                "        if (moisture > 1.0) {\n" +
                "            moisture = 1.0;\n" +
                "        }";
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "water",
                null,
                bodyWater,
                null));
        String bodyPrintStatus = "        System.out.println(name + \" - Moisture: \" + moisture);";
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "printStatus",
                null,
                bodyPrintStatus,
                null));
        return classPlant;
    }

    private Class initClassRobot() {
        Class classRobot = new Class(ClassComponent.AccessModifier.PUBLIC,
                "Robot", null);
        classRobot.addField(new Field(ClassComponent.AccessModifier.PRIVATE,
                null,
                "int", "counterTilledTile", null, null));
        String bodyWalkToUntilledTile = "        ...";
        classRobot.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "walkToUntilledTile",
                null,
                bodyWalkToUntilledTile,
                null));
        String bodyTillTile = "        ...";
        classRobot.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "tillTile",
                null,
                bodyTillTile,
                null));
        return classRobot;
    }

    public ProjectViewportFragment() {
        // Required empty public constructor

        packageMain = new Package("com.megacoolcorp");
        classes.add(
                initClassMain()
        );
        classes.add(
                initClassFoo()
        );
        classes.add(
                initClassBar()
        );
        classes.add(
                initClassGrowTentSystem()
        );
        classes.add(
                initClassPlant()
        );
        classes.add(
                initClassRobot()
        );
    }

    public Class getClassMain() {
        for (Class myClass : classes) {
            if (myClass.getName().equals(CLASS_NAME_MAIN)) {
                return myClass;
            }
        }
        return null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param classesDataObject ClassesDataObject.
     *
     * @return A new instance of fragment ProjectViewportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectViewportFragment newInstance() {
        ProjectViewportFragment fragment = new ProjectViewportFragment();
        Bundle args = new Bundle();
//        args.putSerializable(ARG_CLASSES_DATA_OBJECT, classesDataObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            ClassesDataObject classesDataObject = (ClassesDataObject) getArguments().getSerializable(ARG_CLASSES_DATA_OBJECT);
//            classes = classesDataObject.getClasses();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_viewport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.tv_package_name);
        recyclerView = view.findViewById(R.id.rv_project_view);

        textView.setText(
                packageMain.getName()
        );
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }

                listener.onPackageClicked(packageMain);
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addClass(new Class(ClassComponent.AccessModifier.PUBLIC,
                        "DEFAULT", null));
                return true;
            }
        });

        classRVAdapter = new ClassRVAdapterForProject(getContext(), classes, new ClassRVAdapterForProject.GestureListener() {
            @Override
            public void onSingleTapUp(int position) {
                Log.e(TAG, "projectviewportfragment classrvadapter onSingleTapUp(int)... position: " + position);
                listener.onClassClicked(
                        classes.get(position)
                );
            }

            @Override
            public void onDoubleTap(int position) {
                Log.e(TAG, "projectviewportfragment classrvadapter onDoubleTap(int)... position: " + position);
                listener.onClassDoubleClicked(
                        classes.get(position)
                );
            }

            @Override
            public void onLongPress(int position) {
                Log.e(TAG, "projectviewportfragment classrvadapter onLongPress(int)... position: " + position);

                EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(new EditTextDialogFragment.EnterListener() {
                    @Override
                    public void onDismiss() {
                        // TODO:
                    }

                    @Override
                    public void onEnterKeyPressed(String name) {
                        classes.get(position).updateName(
                                name
                        );

                        // update self.
                        classRVAdapter.notifyItemChanged(position);
                        // update MainViewportFragment and StructureViewportFragment.
                        listener.onClassRenamed(
                                classes.get(position)
                        );
                    }
                });

                editTextDialogFragment.show(getChildFragmentManager(), EditTextDialogFragment.TAG);
            }
        });
        recyclerView.setAdapter(classRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void addClass(Class classToAdd) {
        classes.add(classToAdd);
        classRVAdapter.notifyItemInserted(
                classes.size()
        );
    }

    public Class getMainClass() {
        return classes.get(0);
    }


}