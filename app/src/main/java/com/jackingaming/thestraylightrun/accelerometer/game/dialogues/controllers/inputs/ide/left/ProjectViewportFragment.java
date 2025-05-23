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
    public static final String NAME_CLASS_MAIN = "Main";
    public static final String NAME_CLASS_FOO = "Foo";
    public static final String NAME_CLASS_BAR = "Bar";
    public static final String NAME_CLASS_GROW_TENT_SYSTEM = "GrowTentSystem";
    public static final String NAME_CLASS_EQUIPMENT = "Equipment";
    public static final String NAME_CLASS_PLANT = "Plant";
    public static final String NAME_CLASS_ROBOT = "Robot";
    public static final String NAME_METHOD_MAIN = "main";
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
                NAME_CLASS_MAIN, null);

        // METHODS
        List<ClassComponent.ClassInterfaceAndObjectRelated> nonAccessModifiersStatic = new ArrayList<>();
        nonAccessModifiersStatic.add(
                ClassComponent.ClassInterfaceAndObjectRelated.STATIC
        );
        List<VariableDeclaration> argumentListMain = new ArrayList<>();
        argumentListMain.add(
                new VariableDeclaration("String[]", "args")
        );
        String bodyMain = "        GrowTentSystem tent = new GrowTentSystem();\n" +
                "        tent.runDailyCycle();";
        classMain.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                nonAccessModifiersStatic,
                "void", NAME_METHOD_MAIN,
                argumentListMain,
                bodyMain,
                null, null, true));
        return classMain;
    }

    private Class initClassFoo() {
        return new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_FOO, null);
    }

    private Class initClassBar() {
        return new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_BAR, null);
    }

    private Class initClassGrowTentSystem() {
        Class classGrowTentSystem = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_GROW_TENT_SYSTEM, null);

        // FIELDS
        List<ClassComponent.ClassInterfaceAndObjectRelated> nonAccessModifiersFinal = new ArrayList<>();
        nonAccessModifiersFinal.add(ClassComponent.ClassInterfaceAndObjectRelated.FINAL);
        // run 5
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isTentZipped", "false",
                null, null, null, false));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isLightCycleCorrect", "false",
                null, null, null, false));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "pestsDetected", "false",
                null, null, null, false));
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "List<Plant>", "plants", null,
                null, null, null, true));
        // run 4
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "List<Equipment>", "equipmentList", null,
                null, null, null, false));

        // CONSTRUCTORS
        // run 5
        String bodyGrowTentSystemDefaultConstructor = "        plants = new ArrayList<Plant>();\n" +
                "        plants.add(\n" +
                "            new Plant(\"Blue Dream\")\n" +
                "        );\n" +
                "        plants.add(\n" +
                "            new Plant(\"Northern Lights\")\n" +
                "        );\n" +
                "        plants.add(\n" +
                "            new Plant(\"OG Kush\")\n" +
                "        );\n";
        classGrowTentSystem.addConstructor(new Constructor(
                ClassComponent.AccessModifier.PUBLIC,
                null,
                bodyGrowTentSystemDefaultConstructor,
                null, null, true));
        // run 4
        List<VariableDeclaration> argumentListGrowTentSystemWithEquipmentList = new ArrayList<>();
        argumentListGrowTentSystemWithEquipmentList.add(
                new VariableDeclaration("List<Equipment>", "equipmentList")
        );
        String bodyGrowTentSystemWithEquipments = "        this();\n" +
                "        this.equipmentList = equipmentList;";
        classGrowTentSystem.addConstructor(new Constructor(
                ClassComponent.AccessModifier.PUBLIC,
                argumentListGrowTentSystemWithEquipmentList, bodyGrowTentSystemWithEquipments,
                null, null, true));

        // METHODS
        // run 5
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
                null, null, true));
        // run 4
        String bodyPerformDiagnostics = "        int functionalCount = 0;\n" +
                "        boolean allLightsOff = true;\n" +
                "\n" +
                "        // TODO: Loop through each Equipment item, use isFunctional() to count how many are working\n" +
                "\n" +
                "        // TODO: Print a warning if fewer than 3 are functional OR if all lights are off";
        classGrowTentSystem.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "performDiagnostics",
                null,
                bodyPerformDiagnostics,
                null, null, true));
        return classGrowTentSystem;
    }

    private Class initClassPlant() {
        Class classPlant = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_PLANT, null);

        // FIELDS
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "String", "name", null,
                null, null, null, false));
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isFlowering", null,
                "    // This plant will be grown inside a controlled tent.",
                "// TODO: Declare a boolean to track if the plant is flowering",
                null, false));
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isHealthy", null,
                null,
                "// TODO: Declare a boolean to track if the plant is healthy",
                " // Monitor plant health", false));
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "int", "vegDays", null,
                null,
                "// TODO: Declare an int for number of days in veg stage",
                null, false));

        // CONSTRUCTORS
        List<VariableDeclaration> argumentListPlant = new ArrayList<>();
        argumentListPlant.add(
                new VariableDeclaration("String", "name")
        );
        String bodyPlant = "        this.name = name;\n" +
                "        this.isFlowering = false; // Initialize to false\n" +
                "        this.isHealthy = true;       // Assume healthy on creation\n" +
                "        this.isReady = false;        // Harvest status begins as false";
        classPlant.addConstructor(new Constructor(ClassComponent.AccessModifier.PUBLIC,
                argumentListPlant, bodyPlant,
                null, null, true));

        // METHODS
        String bodyUpdateGrowth = "        // Only grow if there's enough light and no pests.\n" +
                "        // TODO: If light is correct and no pests, increase days in veg\n" +
                "\n" +
                "        // TODO: If days in veg is >= 14 and plant is healthy, set flowering to true";
        List<VariableDeclaration> argumentListUpdateGrowth = new ArrayList<>();
        argumentListUpdateGrowth.add(
                new VariableDeclaration("boolean", "lightIsCorrect")
        );
        argumentListUpdateGrowth.add(
                new VariableDeclaration("boolean", "hasPests")
        );
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "updateGrowth",
                argumentListUpdateGrowth,
                bodyUpdateGrowth,
                null, null, true));
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "boolean", "isReadyForHarvest",
                null,
                null,
                null,
                "// TODO: Write a method that returns true if flowering, healthy, vegDays >= 21)",
                true));
        return classPlant;
    }

    private Class initClassEquipment() {
        Class classEquipment = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_EQUIPMENT, null);

        // FIELDS
        classEquipment.addField(new Field(ClassComponent.AccessModifier.PRIVATE,
                null,
                "String", "name", null,
                null, null, null, false));
        classEquipment.addField(new Field(ClassComponent.AccessModifier.PRIVATE,
                null,
                "boolean", "isPowered", null,
                null, null, null, false));
        classEquipment.addField(new Field(ClassComponent.AccessModifier.PRIVATE,
                null,
                "boolean", "isCalibrated", null,
                null, null, null, false));

        // CONSTRUCTORS
        List<VariableDeclaration> argumentListEquipment = new ArrayList<>();
        argumentListEquipment.add(
                new VariableDeclaration("String", "name")
        );
        argumentListEquipment.add(
                new VariableDeclaration("boolean", "isPowered")
        );
        argumentListEquipment.add(
                new VariableDeclaration("boolean", "isCalibrated")
        );
        String bodyEquipment = "        this.name = name;\n" +
                "        this.isPowered = isPowered;\n" +
                "        this.isCalibrated = isCalibrated;";
        classEquipment.addConstructor(new Constructor(ClassComponent.AccessModifier.PUBLIC,
                argumentListEquipment, bodyEquipment,
                null, null, true));

        // METHODS
        classEquipment.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "boolean", "isFunctional",
                null,
                null,
                null,
                "// TODO: Write a method isFunctional() that returns true only if powered AND calibrated",
                true));
        return classEquipment;
    }

    private Class initClassRobot() {
        Class classRobot = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_ROBOT, null);

        // FIELDS
        classRobot.addField(new Field(ClassComponent.AccessModifier.PRIVATE,
                null,
                "int", "counterTilledTile", null,
                null, null, null, false));

        // METHODS
        String bodyWalkToUntilledTile = "        ...";
        classRobot.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "walkToUntilledTile",
                null,
                bodyWalkToUntilledTile,
                null, null, true));
        String bodyTillTile = "        ...";
        classRobot.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "tillTile",
                null,
                bodyTillTile,
                null, null, true));
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
                initClassEquipment()
        );
        classes.add(
                initClassRobot()
        );
    }

    public Class getClassMain() {
        for (Class myClass : classes) {
            if (myClass.getName().equals(NAME_CLASS_MAIN)) {
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