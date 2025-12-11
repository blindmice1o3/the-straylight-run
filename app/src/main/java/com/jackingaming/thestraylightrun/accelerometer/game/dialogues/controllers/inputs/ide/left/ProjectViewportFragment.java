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
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
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
    public static final String NAME_PACKAGE = "com.greenhorizonstudio";
    public static final String NAME_CLASS_MAIN = "Main";
    public static final String NAME_CLASS_FOO = "Foo";
    public static final String NAME_CLASS_BAR = "Bar";
    public static final String NAME_CLASS_GROW_TENT_SYSTEM_RUN5 = "GrowTentSystemRun5";
    public static final String NAME_CLASS_GROW_TENT_SYSTEM_RUN4 = "GrowTentSystemRun4";
    public static final String NAME_CLASS_EQUIPMENT_RUN4 = "EquipmentRun4";
    public static final String NAME_CLASS_PLANT_RUN3 = "PlantRun3";
    public static final String NAME_CLASS_ROBOT_RUN3 = "RobotRun3";
    public static final String NAME_CLASS_PLANT_RUN2 = "PlantRun2";
    public static final String NAME_CLASS_ROBOT_RUN2 = "RobotRun2";
    public static final String NAME_CLASS_GROWABLE_TILE_RUN1 = "GrowableTileRun1";
    public static final String NAME_CLASS_SEED_RUN1 = "SeedRun1";
    public static final String NAME_CLASS_ROBOT_RUN1 = "RobotRun1";
    public static final String NAME_CLASS_CHICKEN_RUN1 = "ChickenRun1";
    public static final String NAME_CLASS_COW_RUN1 = "CowRun1";
    public static final String NAME_METHOD_MAIN = "main";
    public static final String ARG_RUN = "run";

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
    private Class classMain;
    private List<Class> classes = new ArrayList<>();
    private TextView textView;
    private RecyclerView recyclerView;
    private ClassRVAdapterForProject classRVAdapter;
    private Game.Run run;

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
        // TODO: new bodyMain per run.
        String bodyMain = "        GrowTentSystem tent = new GrowTentSystem();\n" +
                "        tent.runDailyCycle();";
        classMain.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                nonAccessModifiersStatic,
                "void", NAME_METHOD_MAIN,
                argumentListMain,
                bodyMain,
                null, null, false));

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

    private Class initClassGrowTentSystemRun5() {
        Class classGrowTentSystem = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_GROW_TENT_SYSTEM_RUN5, null);

        // FIELDS
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

        // CONSTRUCTORS
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
                null, null, true));
        String bodyZipTent = "        isTentZipped = true;";
        classGrowTentSystem.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "zipTent",
                null,
                bodyZipTent,
                null, null, true));

        return classGrowTentSystem;
    }

    private Class initClassGrowTentSystemRun4() {
        Class classGrowTentSystem = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_GROW_TENT_SYSTEM_RUN4, null);

        // FIELDS
        classGrowTentSystem.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "List<Equipment>", "equipmentList", null,
                null, null, null, false));

        // CONSTRUCTORS
        List<VariableDeclaration> argumentListGrowTentSystemWithEquipmentList = new ArrayList<>();
        argumentListGrowTentSystemWithEquipmentList.add(
                new VariableDeclaration("List<Equipment>", "equipmentList")
        );
        String bodyGrowTentSystemWithEquipments = "        this.equipmentList = equipmentList;";
        classGrowTentSystem.addConstructor(new Constructor(
                ClassComponent.AccessModifier.PUBLIC,
                argumentListGrowTentSystemWithEquipmentList, bodyGrowTentSystemWithEquipments,
                null, null, false));

        // METHODS
        String bodyPerformDiagnostics = "        int functionalCount = 0;\n" +
                "\n" +
                "        // TODO: Loop through each Equipment item, use isFunctional() to count how many are working\n" +
                "\n" +
                "        // TODO: Print a warning if fewer than 6 are functional";
        classGrowTentSystem.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "performDiagnostics",
                null,
                bodyPerformDiagnostics,
                null, null, false));

        return classGrowTentSystem;
    }

    private Class initClassEquipmentRun4() {
        Class classEquipment = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_EQUIPMENT_RUN4, null);

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
                null, null, false));

        // METHODS
        classEquipment.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "boolean", "isFunctional",
                null,
                null,
                null,
                "// TODO: Write a method isFunctional() that returns true only if powered AND calibrated",
                false));

        return classEquipment;
    }

    private Class initClassPlantRun3() {
        Class classPlant = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_PLANT_RUN3, null);

        // FIELDS
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "boolean", "diseased", null,
//                null,
//                null,
//                null, false));

        // CONSTRUCTORS

        // METHODS
//        String bodyIsDiseased = "        return diseased;";
//        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "boolean", "isDiseased",
//                null,
//                bodyIsDiseased,
//                null,
//                null,
//                false));
        String bodyHarvest = "        /* cut & collect */";
        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "harvest",
                null,
                bodyHarvest,
                null,
                null,
                false));

        return classPlant;
    }

    public Class initClassRobotRun3() {
        Class classRobotRun3 = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_ROBOT_RUN3, null);

        // FIELDS
        // Intentionally blank.

        // CONSTRUCTORS
        // Intentionally blank.

        // METHODS
//        List<VariableDeclaration> argumentListInspectAndCull = new ArrayList<>();
//        argumentListInspectAndCull.add(
//                new VariableDeclaration("List<Plant>", "plants")
//        );
//        String bodyInspectAndCull = "        // TODO: Use for loop to check each plant. If the plant is diseased, remove it.";
//        classRobotRun3.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "void", "inspectAndCull",
//                argumentListInspectAndCull,
//                bodyInspectAndCull,
//                null, null, false));
        List<VariableDeclaration> argumentListHarvestAll = new ArrayList<>();
        argumentListHarvestAll.add(
                new VariableDeclaration("List<Plant>", "plants")
        );
        String bodyHarvestAll = "        // TODO: Use enhanced for loop to check each plant. If the plant is ready to harvest, harvest it.";
        classRobotRun3.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "harvestAll",
                argumentListHarvestAll,
                bodyHarvestAll,
                null,
                null,
                false));

        return classRobotRun3;
    }

    private Class initClassPlantRun2() {
        Class classPlant = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_PLANT_RUN2, null);

        // FIELDS
        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isDry", null,
                null, null, null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "String", "name", null,
//                null, null, null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "String", "type", null,
//                null, null, null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "String", "description", null,
//                null, null, null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "String", "origin", null,
//                null, null, null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "boolean", "flowering", null,
//                "    // This plant will be grown inside a controlled tent.",
//                "// TODO: Declare a boolean to track if the plant is flowering",
//                null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "boolean", "diseased", null,
//                null,
//                "// TODO: Declare a boolean to track if the plant is diseased",
//                null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "int", "vegDays", null,
//                null,
//                "// TODO: Declare an int for number of days in veg stage",
//                null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "boolean", "needsWater", null,
//                null,
//                null,
//                null, false));
//        classPlant.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
//                null,
//                "int", "hydrationLevel", null,
//                null,
//                null,
//                null, false));

        // CONSTRUCTORS
//        List<VariableDeclaration> argumentListPlant = new ArrayList<>();
//        argumentListPlant.add(
//                new VariableDeclaration("Seed", "seed")
//        );
//        String bodyPlant = "        this.name = seed.getName();\n" +
//                "        this.type = seed.getType();\n" +
//                "        this.description = seed.getDescription();\n" +
//                "        this.origin = seed.getOrigin();\n" +
//                "        this.flowering = false; // Initialize to false\n" +
//                "        this.diseased = false;       // Assume healthy on creation\n" +
//                "        this.vegDays = 0;\n" +
//                "        this.needsWater = true;\n" +
//                "        this.hydrationLevel = 0;";
//        classPlant.addConstructor(new Constructor(ClassComponent.AccessModifier.PUBLIC,
//                argumentListPlant, bodyPlant,
//                null, null, false));

        // METHODS
//        String bodyUpdateGrowth = "        // Only grow if there's enough light and no pests.\n" +
//                "        // TODO: If light is correct and no pests, increase days in veg\n" +
//                "\n" +
//                "        // TODO: If days in veg is >= 14 and plant is NOT diseased, set flowering to true";
//        List<VariableDeclaration> argumentListUpdateGrowth = new ArrayList<>();
//        argumentListUpdateGrowth.add(
//                new VariableDeclaration("boolean", "lightIsCorrect")
//        );
//        argumentListUpdateGrowth.add(
//                new VariableDeclaration("boolean", "hasPests")
//        );
//        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "void", "updateGrowth",
//                argumentListUpdateGrowth,
//                bodyUpdateGrowth,
//                null, null, false));
//        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "boolean", "readyToHarvest",
//                null,
//                null,
//                null,
//                "// TODO: Write a method that returns true if flowering, NOT diseased, vegDays >= 21)",
//                true));
//        String bodyWater = "        hydrationLevel++;\n" +
//                "        needsWater = false;";
//        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "void", "water",
//                null,
//                bodyWater,
//                null,
//                null,
//                true));
//        String bodyCheckHydration = "        return needsWater;";
//        classPlant.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "boolean", "checkHydration",
//                null,
//                bodyCheckHydration,
//                null,
//                null,
//                true));

        return classPlant;
    }

    public Class initClassRobotRun2() {
        Class classRobotRun2 = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_ROBOT_RUN2, null);

        // FIELDS
        // Intentionally blank.

        // CONSTRUCTORS
        // Intentionally blank.

        // METHODS
        List<VariableDeclaration> argumentListWaterIfDry = new ArrayList<>();
        argumentListWaterIfDry.add(
                new VariableDeclaration("Plant", "p")
        );
        String bodyWaterIfDry = "        if (p.isDry) {\n" +
                "            System.out.println(\"Watering plant!\");\n" +
                "        } else {\n" +
                "            System.out.println(\"Skipping plant.\");\n" +
                "        }";
        classRobotRun2.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "waterIfDry",
                argumentListWaterIfDry,
                bodyWaterIfDry,
                null, null, false));

        return classRobotRun2;
    }

    public Class initClassGrowableTileRun1() {
        Class classGrowableTileRun1 = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_GROWABLE_TILE_RUN1, null);

        // FIELDS
        classGrowableTileRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isTilled", "false",
                null, null, null, false));
        classGrowableTileRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "hasSeed", "false",
                null, null, null, false));
        classGrowableTileRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isWatered ", "false",
                null, null, null, false));
        classGrowableTileRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "Plant", "plant", "null",
                null, null, null, false));

        // CONSTRUCTORS
        // Intentionally blank.

        // METHODS
        String bodyTill = "        isTilled = true;";
        classGrowableTileRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "till",
                null,
                bodyTill,
                null, null, false
        ));
        List<VariableDeclaration> argumentListPlantSeed = new ArrayList<>();
        argumentListPlantSeed.add(new VariableDeclaration(
                "Seed", "seed"
        ));
        String bodyPlantSeed = "        if (isTilled && plant == null) {\n" +
                "            hasSeed = true;\n" +
                "            plant = new Plant(seed);\n" +
                "        }";
        classGrowableTileRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "seed",
                argumentListPlantSeed,
                bodyPlantSeed,
                null, null, true
        ));
        String bodyWater = "        isWatered = true;";
        classGrowableTileRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "water",
                null,
                bodyWater,
                null, null, true
        ));

        return classGrowableTileRun1;
    }

//    private String furColor, name;
//    private int ageInYears;
//
//    private void biteHandGently() {
//
//    }
//
//    private void giftDeadAnimals() {
//
//
//        List<String> plants = List.of("Blue Dream", "AK-47", "GSC");
//
//        for (int i = 0; i < plants.size(); i++) {
//            System.out.println("Checking " + plants.get(i));
//        }
//
//
//
//
//    }
//
//    private void purr() {
//
//    }
//
//    private void lookConfused() {
//
//    }
//
//    public static class Treat {
//        private FreshnessLevel freshnessLevel;
//
//        public enum FreshnessLevel {
//            STALE,
//            OVER_NIGHT,
//            CRUNCHY;
//        }
//
//        public FreshnessLevel getFreshnessLevel() {
//            return freshnessLevel;
//        }
//    }
//
//    public void showAffectionBasedOnTreats(Treat treatGiven) {
//        if (treatGiven.getFreshnessLevel() == Treat.FreshnessLevel.STALE) {
//            biteHandGently();
//        } else if (treatGiven.getFreshnessLevel() == Treat.FreshnessLevel.OVER_NIGHT) {
//            giftDeadAnimals();
//        } else if (treatGiven.getFreshnessLevel() == Treat.FreshnessLevel.CRUNCHY) {
//            purr();
//        } else {
//            lookConfused();
//        }
//    }

    public Class initClassSeedRun1() {
        Class classSeedRun1 = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_SEED_RUN1, null);

        // FIELDS
        String inLineCommentName = " // e.g. \"UtilityKit Kush\"";
        classSeedRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "String", "name", null,
                null, null, inLineCommentName, false));
        String inLineCommentDescription = " // e.g. \"Helps me focus on long days\"";
        classSeedRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "String", "description", null,
                null, null, inLineCommentDescription, false));
        String inLineCommentType = " // indica, sativa, or hybrid";
        classSeedRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "String", "type", null,
                null, null, inLineCommentType, false));
        String inLineCommentOrigin = " // e.g. \"The eighth (bought from someone in the alley behind the cannabis lounge) had seeds in it.\"";
        classSeedRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "String", "origin", null,
                null, null, inLineCommentOrigin, false));

        // CONSTRUCTORS
        List<VariableDeclaration> argumentListSeedRun1 = new ArrayList<>();
        argumentListSeedRun1.add(
                new VariableDeclaration("String", "name")
        );
        argumentListSeedRun1.add(
                new VariableDeclaration("String", "description")
        );
        argumentListSeedRun1.add(
                new VariableDeclaration("String", "type")
        );
        argumentListSeedRun1.add(
                new VariableDeclaration("String", "origin")
        );
        String bodySeedRun1 = "        this.name = name;\n" +
                "        this.description = description;\n" +
                "        // TODO: Set this.type to the provided type\n" +
                "        this.origin = origin;";
        classSeedRun1.addConstructor(new Constructor(ClassComponent.AccessModifier.PUBLIC,
                argumentListSeedRun1,
                bodySeedRun1,
                null, null, false));

        // METHODS
//        String bodyGetName = "        return name;";
//        classSeedRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "String", "getName",
//                null, bodyGetName, null, null, false));
//        String bodyGetDescription = "        return description;";
//        classSeedRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "String", "getDescription",
//                null, bodyGetDescription, null, null, true));
//        String bodyGetType = "        return type;";
//        classSeedRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "String", "getType",
//                null, bodyGetType, null, null, true));
//        String bodyGetOrigin = "        return origin;";
//        classSeedRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "String", "getOrigin",
//                null, bodyGetOrigin, null, null, true));

        return classSeedRun1;
    }

    public Class initClassRobotRun1() {
        Class classRobotRun1 = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_ROBOT_RUN1, null);

        // FIELDS
        // Intentionally blank.

        // CONSTRUCTORS
        // Intentionally blank.

        // METHODS
//        List<VariableDeclaration> argumentPlantSeed = new ArrayList<>();
//        argumentPlantSeed.add(
//                new VariableDeclaration("Seed", "seedToPlant")
//        );
//        String bodyPlantSeed = "        // TODO: complete tilling, seeding, and watering steps";
//        classRobotRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
//                null,
//                "void", "plantSeed",
//                argumentPlantSeed,
//                null,
//                bodyPlantSeed,
//                null, null, false));

        String bodyTurnInPlace = "        ...";
        classRobotRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "turnInPlace",
                null,
                bodyTurnInPlace,
                null, null, false));

        return classRobotRun1;
    }

    public Class initClassChickenRun1() {
        Class classChickenRun1 = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_CHICKEN_RUN1, null);

        // FIELDS
        classChickenRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "int", "ageInDays", null,
                null, null, null, false));
        classChickenRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isHungry", null,
                null, null, null, false));

        // CONSTRUCTORS
        // intentionally blank.

        // METHODS
        String bodyCluck = "        System.out.println(\"cluck cluck\");";
        classChickenRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "cluck",
                null, bodyCluck, null, null, false));

        return classChickenRun1;
    }

    public Class initClassCowRun1() {
        Class classCowRun1 = new Class(ClassComponent.AccessModifier.PUBLIC,
                NAME_CLASS_COW_RUN1, null);

        // FIELDS
        classCowRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "String", "name", null,
                null, null, null, false));
        classCowRun1.addField(new Field(ClassComponent.AccessModifier.DEFAULT,
                null,
                "boolean", "isHungry", null,
                null, null, null, false));

        // CONSTRUCTORS
        // intentionally blank.

        // METHODS
        String bodyWalk = "        System.out.println(\"mooooove\");";
        classCowRun1.addMethod(new Method(ClassComponent.AccessModifier.PUBLIC,
                null,
                "void", "walk",
                null, bodyWalk, null, null, false));

        return classCowRun1;
    }

    public ProjectViewportFragment() {
        // Required empty public constructor

        packageMain = new Package(NAME_PACKAGE);
        classMain = initClassMain();

        classes.add(
                classMain
        );
//        classes.add(
//                initClassFoo()
//        );
//        classes.add(
//                initClassBar()
//        );
    }

    public Class getClassMain() {
        return classMain;
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
    public static ProjectViewportFragment newInstance(Game.Run run) {
        ProjectViewportFragment fragment = new ProjectViewportFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RUN, run);
//        args.putSerializable(ARG_CLASSES_DATA_OBJECT, classesDataObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
//            ClassesDataObject classesDataObject = (ClassesDataObject) getArguments().getSerializable(ARG_CLASSES_DATA_OBJECT);
//            classes = classesDataObject.getClasses();
            run = (Game.Run) arguments.getSerializable(ARG_RUN);

            // TODO: load panes' content via Run.
            switch (run) {
                case ONE:
//                    classes.add(
//                            initClassGrowableTileRun1()
//                    );
                    classes.add(
                            initClassSeedRun1()
                    );
                    classes.add(
                            initClassRobotRun1()
                    );
//                    classes.add(
//                            initClassChickenRun1()
//                    );
//                    classes.add(
//                            initClassCowRun1()
//                    );
                    break;
                case TWO:
                    classes.add(
                            initClassPlantRun2()
                    );
                    classes.add(
                            initClassRobotRun2()
                    );
                    break;
                case THREE:
                    classes.add(
                            initClassPlantRun3()
                    );
                    classes.add(
                            initClassRobotRun3()
                    );
                    break;
                case FOUR:
                    classes.add(
                            initClassGrowTentSystemRun4()
                    );
                    classes.add(
                            initClassEquipmentRun4()
                    );
                    break;
                case FIVE:
                    classes.add(
                            initClassGrowTentSystemRun5()
                    );
                    break;
            }
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
}