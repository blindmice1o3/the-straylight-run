package com.jackingaming.thestraylightrun.nextweektonight;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NextWeekTonightEpisodesGeneratorFragment extends Fragment
        implements Serializable {
    public static final String TAG = NextWeekTonightEpisodesGeneratorFragment.class.getSimpleName();
    public static final String ARG_SHOW_TOOLBAR_ON_DISMISS = "showToolbarOnDismiss";
    //    private static final String ID_VIDEO_HOST = "vid_20250819_032748939_run_one_attempt_0";
    //    private static final String ID_VIDEO_HOST = "vid_20250826_045602759_run_one_attempt_1";
    //    private static final String ID_VIDEO_HOST = "vid_20250904_195343803_run_one_attempt_2";
    //    private static final String ID_VIDEO_HOST = "pxl_20250429_193429506";
    private static final String ID_VIDEO_HOST = "vid_20250917_180111542_run_three_pre";
    private static final String ID_VIDEO_FULL_SCREEN_NOT_BAD_NOT_BAD_BURGERS = "vid_not_bad_not_bad_burger_2025_06_10";
    private static final String ID_VIDEO_FULL_SCREEN_PRIMITIVE_VS_OBJECT_TYPE = "vid_primitive_vs_object_type_2025_08_11";
//    private static final String ID_VIDEO_FULL_SCREEN = "vid_20230603_145112";

    private static final int ID_IMAGE_CORGI = R.drawable.corgi_crusade_editted;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean showToolbarOnDismiss;
    private List resourceIDs;
    private int indexResourceIDs;

    private FrameLayout frameLayoutParent;
    private ConstraintLayout constraintlayoutHost, constraintlayoutMarqueeAndPresentationbox;

    private RecyclerView recyclerView;
    private FragmentContainerView fcvPresentationBox;
    private VideoView videoViewHost;

    private VideoViewFragment videoViewFragmentNotBadNotBadBurgers, videoViewFragmentPrimitiveVsObjectType;

    public NextWeekTonightEpisodesGeneratorFragment() {
        // Required empty public constructor
    }

    public static NextWeekTonightEpisodesGeneratorFragment newInstance(boolean showToolbarOnDismiss) {
        NextWeekTonightEpisodesGeneratorFragment fragment = new NextWeekTonightEpisodesGeneratorFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOW_TOOLBAR_ON_DISMISS, showToolbarOnDismiss);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        if (getArguments() != null) {
            showToolbarOnDismiss = getArguments().getBoolean(ARG_SHOW_TOOLBAR_ON_DISMISS);

//            imageViewNotePrimitiveTypesPt1 = new ImageView(getContext());
//            imageViewNotePrimitiveTypesPt1.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageViewNotePrimitiveTypesPt1.setImageResource(R.drawable.notes_02);
//            ImageWithSlideAnimation imageWithSlideAnimation = new ImageWithSlideAnimation(imageViewNotePrimitiveTypesPt1);

//            List listForMultipleImagesSlide1 = new ArrayList();
//            listForMultipleImagesSlide1.add(R.drawable.nwt_run_one_slide1_2of4);
//            listForMultipleImagesSlide1.add(R.drawable.nwt_run_one_slide1_3of4);
//            listForMultipleImagesSlide1.add(R.drawable.nwt_run_one_slide1_4of4);
//
//            List listForMultipleImagesSlide2 = new ArrayList();
//            listForMultipleImagesSlide2.add(R.drawable.nwt_run_one_slide2_1of5);
//            listForMultipleImagesSlide2.add(R.drawable.nwt_run_one_slide2_2of5);
//            listForMultipleImagesSlide2.add(R.drawable.nwt_run_one_slide2_3of5);
//            listForMultipleImagesSlide2.add(R.drawable.nwt_run_one_slide2_4of5);
//            listForMultipleImagesSlide2.add(R.drawable.nwt_run_one_slide2_5of5);
//
//            List listForMultipleImagesSlide3 = new ArrayList();
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_fields);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_fields);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_fields);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_methods);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_methods);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_methods);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_fields);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_fields);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_fields);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_fields_types);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_fields_types);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_fields_types);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_fields_names);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_fields_names);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_fields_names);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_4of7);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_4of7_primitive);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_4of7_primitive_lowercase);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_4of7_object_uppercase);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_methods);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_methods);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_methods);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_5of7_methods);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_methods_names);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_methods_names);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_methods_names);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_5of7_methods_names);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_methods_return_types);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_methods_return_types);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_methods_return_types);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_5of7_methods_return_types);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_methods_argument_lists);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_methods_argument_lists);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_methods_argument_lists);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_5of7_methods_argument_lists);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_1of7_methods_body);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_2of7_methods_body);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_3of7_methods_body);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_5of7_methods_body);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_6of7);
//            listForMultipleImagesSlide3.add(R.drawable.nwt_run_one_slide3_7of7);

            resourceIDs = new ArrayList();
//            resourceIDs.add(listForMultipleImagesSlide1);
//            resourceIDs.add(listForMultipleImagesSlide3);
//            resourceIDs.add(ID_VIDEO_FULL_SCREEN_NOT_BAD_NOT_BAD_BURGERS);
//            resourceIDs.add(ID_VIDEO_FULL_SCREEN_PRIMITIVE_VS_OBJECT_TYPE);
//            resourceIDs.add(ID_IMAGE_CORGI);

            indexResourceIDs = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_next_week_tonight_episodes_generator, container, false);
    }

    private int indexList;
    //    private ImageView imageViewNotePrimitiveTypesPt1, imageViewRoadMap,
//            imageViewTerraformFarmPlanet, imageViewSceneFarm, imageViewRobot,
//            imageViewJavaReservedWords;
//    private ImageView imageViewClassCat, imageViewClassHouse, imageViewClassChicken,
//            imageViewBlueprint, imageViewInstancesOfHouse;
    private ImageView ivGeneralForm, ivGeneralFormStart, ivGeneralFormStop, ivGeneralFormUpdate,
            ivWateringPlants, ivWateringPlantsStart, ivWateringPlantsStop, ivWateringPlantsUpdate,
            ivCollectionOfPlants, ivForEachLoop, ivKeywordContinue, ivInfiniteLoop;
//            ivClassCat, ivIfElseGeneralForm, ivIfElseTemperature, ivIfElseCheckIfDry, ivComparisonAndLogicalOperators, ivElseIfAgeInYears, ivElseIfSoilMoisture, ivForLoop,
//            ivClassHouse, ivClassChicken,
//            ivTypePrimitiveVsObject, ivJavaReservedWordsPrimitive,
//            ivMethodSignature, ivBookVsJavaProgram, ivMainGetterSetterConstructorComments;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");
        fcvPresentationBox = view.findViewById(R.id.fcv_presentation_box);

        frameLayoutParent = view.findViewById(R.id.framelayout_parent);
        frameLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List listOfResources = (List) resourceIDs.get(0);

                if (indexList == 0) {
                    // general form (base)
                    Log.e(TAG, "indexList == 0");

                    ivGeneralForm = new ImageView(getContext());
                    ivGeneralForm.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivGeneralForm.setImageResource(
                            R.drawable.nwt_run_three_slide_1_1of6_for_loop_general_form
                    );
                    FrameLayout.LayoutParams layoutParamsGeneralForm = new FrameLayout.LayoutParams(
                            1142, // Width
                            232  // Height
                    );
                    layoutParamsGeneralForm.setMargins(32, 32, 0, 0);
                    frameLayoutParent.addView(ivGeneralForm, layoutParamsGeneralForm);
                    indexList++;
                } else if (indexList == 1) {
                    // watering plants (base)
                    Log.e(TAG, "indexList == 1");

                    ivWateringPlants = new ImageView(getContext());
                    ivWateringPlants.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivWateringPlants.setImageResource(
                            R.drawable.nwt_run_three_slide_1_2of6_for_loop_watering_plants
                    );
                    FrameLayout.LayoutParams layoutParamsWateringPlants = new FrameLayout.LayoutParams(
                            1332, // Width
                            386  // Height
                    );
                    layoutParamsWateringPlants.setMargins(32, 296, 0, 0);
                    frameLayoutParent.addView(ivWateringPlants, layoutParamsWateringPlants);
                    indexList++;
                } else if (indexList == 2) {
                    // general form (start)
                    // watering plants (start)
                    Log.e(TAG, "indexList == 2");

                    ivGeneralForm.setImageResource(
                            R.drawable.nwt_run_three_slide_1_1of6_for_loop_general_form_start
                    );
                    ivWateringPlants.setImageResource(
                            R.drawable.nwt_run_three_slide_1_2of6_for_loop_watering_plants_start
                    );
                    indexList++;
                } else if (indexList == 3) {
                    // general form (stop)
                    // watering plants (stop)
                    Log.e(TAG, "indexList == 3");

                    ivGeneralForm.setImageResource(
                            R.drawable.nwt_run_three_slide_1_1of6_for_loop_general_form_stop
                    );
                    ivWateringPlants.setImageResource(
                            R.drawable.nwt_run_three_slide_1_2of6_for_loop_watering_plants_stop
                    );
                    indexList++;
                } else if (indexList == 4) {
                    // general form (update)
                    // watering plants (update)
                    Log.e(TAG, "indexList == 4");

                    ivGeneralForm.setImageResource(
                            R.drawable.nwt_run_three_slide_1_1of6_for_loop_general_form_update
                    );
                    ivWateringPlants.setImageResource(
                            R.drawable.nwt_run_three_slide_1_2of6_for_loop_watering_plants_update
                    );
                    indexList++;
                } else if (indexList == 5) {
                    // general form (base)
                    // watering plants (stop)
                    Log.e(TAG, "indexList == 5");

                    ivGeneralForm.setImageResource(
                            R.drawable.nwt_run_three_slide_1_1of6_for_loop_general_form
                    );
                    ivWateringPlants.setImageResource(
                            R.drawable.nwt_run_three_slide_1_2of6_for_loop_watering_plants_stop
                    );
                    indexList++;
                } else if (indexList == 6) {
                    // collection of plants (list)
                    Log.e(TAG, "indexList == 6");

                    FrameLayout.LayoutParams layoutParamsGeneralForm = new FrameLayout.LayoutParams(
                            571, // Width
                            116  // Height
                    );
                    layoutParamsGeneralForm.setMargins(32, 32, 0, 0);
                    ivGeneralForm.setLayoutParams(layoutParamsGeneralForm);

                    FrameLayout.LayoutParams layoutParamsWateringPlants = new FrameLayout.LayoutParams(
                            666, // Width
                            193  // Height
                    );
                    layoutParamsWateringPlants.setMargins(32, 180, 0, 0);
                    ivWateringPlants.setLayoutParams(layoutParamsWateringPlants
                    );

                    ivCollectionOfPlants = new ImageView(getContext());
                    ivCollectionOfPlants.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivCollectionOfPlants.setImageResource(
                            R.drawable.nwt_run_three_slide_1_3of6_for_loop_collection_of_plants
                    );
                    FrameLayout.LayoutParams layoutParamsCollectionOfPlants = new FrameLayout.LayoutParams(
                            1514, // Width
                            384  // Height
                    );
                    layoutParamsCollectionOfPlants.setMargins(32, 405, 0, 0);
                    frameLayoutParent.addView(ivCollectionOfPlants, layoutParamsCollectionOfPlants);
                    indexList++;
                } else if (indexList == 7) {
                    // for each loop
                    Log.e(TAG, "indexList == 7");

                    FrameLayout.LayoutParams layoutParamsCollectionOfPlants = new FrameLayout.LayoutParams(
                            757, // Width
                            192  // Height
                    );
                    layoutParamsCollectionOfPlants.setMargins(32, 405, 0, 0);
                    ivCollectionOfPlants.setLayoutParams(layoutParamsCollectionOfPlants);

                    ivForEachLoop = new ImageView(getContext());
                    ivForEachLoop.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivForEachLoop.setImageResource(
                            R.drawable.nwt_run_three_slide_1_4of6_for_each_loop
                    );
                    FrameLayout.LayoutParams layoutParamsForEachLoop = new FrameLayout.LayoutParams(
                            378, // Width
                            117  // Height
                    );
                    layoutParamsForEachLoop.setMargins(32, 629, 0, 0);
                    frameLayoutParent.addView(ivForEachLoop, layoutParamsForEachLoop);
                    indexList++;
                } else if (indexList == 8) {
                    // keyword continue
                    Log.e(TAG, "indexList == 8");

                    ivKeywordContinue = new ImageView(getContext());
                    ivKeywordContinue.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivKeywordContinue.setImageResource(
                            R.drawable.nwt_run_three_slide_1_5of6_keyword_continue
                    );
                    FrameLayout.LayoutParams layoutParamsKeywordContinue = new FrameLayout.LayoutParams(
                            320, // Width
                            232  // Height
                    );
                    layoutParamsKeywordContinue.setMargins(442, 629, 0, 0);
                    frameLayoutParent.addView(ivKeywordContinue, layoutParamsKeywordContinue);
                    indexList++;
                } else if (indexList == 9) {
                    // infinite loop
                    Log.e(TAG, "indexList == 9");

                    ivInfiniteLoop = new ImageView(getContext());
                    ivInfiniteLoop.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivInfiniteLoop.setImageResource(
                            R.drawable.nwt_run_three_slide_1_6of6_for_loop_infinite_loop
                    );
                    FrameLayout.LayoutParams layoutParamsInfiniteLoop = new FrameLayout.LayoutParams(
                            366, // Width
                            167  // Height
                    );
                    layoutParamsInfiniteLoop.setMargins(794, 629, 0, 0);
                    frameLayoutParent.addView(ivInfiniteLoop, layoutParamsInfiniteLoop);
                    indexList++;
                } else if (indexList == 10) {
                    Log.e(TAG, "indexList == 10");
                } else if (indexList == 11) {
                    Log.e(TAG, "indexList == 11");
                } else if (indexList == 12) {
                    Log.e(TAG, "indexList == 12");
                }

//                ///////////////////
//                indexResourceIDs++;
//                ///////////////////
//
//                if (indexResourceIDs > resourceIDs.size() - 1) {
//                    indexResourceIDs = 0;
//                }
//
//                if (resourceIDs.get(indexResourceIDs) instanceof ImageWithSlideAnimation) {
//                    Log.e(TAG, "ImageWithSlideAnimation FOUND!!!!!");
//                    fcvPresentationBox.setVisibility(View.VISIBLE);
//
//                    ImageWithSlideAnimation imageWithSlideAnimation = (ImageWithSlideAnimation) resourceIDs.get(indexResourceIDs);
//
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                            542, // Width
//                            720  // Height
//                    );
//                    layoutParams.setMargins(64, 128, 0, 64);
//                    frameLayoutParent.addView(imageViewNotePrimitiveTypesPt1, layoutParams);
//
//                    imageWithSlideAnimation.startAnimator();
//                } else if (resourceIDs.get(indexResourceIDs) instanceof List) {
//                    Log.e(TAG, "LIST FOUND!!!!!");
//                    fcvPresentationBox.setVisibility(View.INVISIBLE);
//
//                    List listOfResources = (List) resourceIDs.get(indexResourceIDs);
//                    Log.e(TAG, "listOfResources.size() == " + listOfResources.size());
//                    if (listOfResources.size() == 3) {
//                        Log.e(TAG, "listOfResources.size() == 3");
//
//                        if (indexList == 0) {
//                            Log.e(TAG, "indexList == 0");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewRobot = new ImageView(getContext());
//                            imageViewRobot.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewRobot.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    480, // Width
//                                    480  // Height
//                            );
//                            layoutParams.setMargins(544, 32, 0, 0);
//                            frameLayoutParent.addView(imageViewRobot, layoutParams);
//
//                            indexList++;
//                        } else if (indexList == 1) {
//                            Log.e(TAG, "indexList == 1");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewJavaReservedWords = new ImageView(getContext());
//                            imageViewJavaReservedWords.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewJavaReservedWords.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    640, // Width
//                                    480  // Height
//                            );
//                            layoutParams.setMargins(80, 80, 0, 0);
//                            frameLayoutParent.addView(imageViewJavaReservedWords, layoutParams);
//
//                            indexList++;
//                        } else if (indexList == 2) {
//                            Log.e(TAG, "indexList == 2");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewRoadMap = new ImageView(getContext());
//                            imageViewRoadMap.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewRoadMap.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    963, // Width
//                                    466  // Height
//                            );
//                            layoutParams.setMargins(32, 528, 0, 0);
//                            frameLayoutParent.addView(imageViewRoadMap, layoutParams);
//
//                            indexList++;
//                        } else {
//                            Log.e(TAG, "indexList == ELSE");
//                            Log.e(TAG, "ELSE clause");
//
//                            indexList = 0;
//
//                            frameLayoutParent.removeView(imageViewTerraformFarmPlanet);
//                            frameLayoutParent.removeView(imageViewRobot);
//                            frameLayoutParent.removeView(imageViewJavaReservedWords);
//                            frameLayoutParent.removeView(imageViewRoadMap);
//
////                            fcvPresentationBox.setVisibility(View.VISIBLE);
//                        }
//                    } else if (listOfResources.size() == 5) {
//                        Log.e(TAG, "listOfResources.size() == 5");
//
//                        if (indexList == 0) {
//                            Log.e(TAG, "indexList == 0");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewClassCat = new ImageView(getContext());
//                            imageViewClassCat.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewClassCat.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    360, // Width
//                                    540  // Height
//                            );
//                            layoutParams.setMargins(32, 32, 0, 0);
//                            frameLayoutParent.addView(imageViewClassCat, layoutParams);
//
//                            indexList++;
//                        } else if (indexList == 1) {
//                            Log.e(TAG, "indexList == 1");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewClassHouse = new ImageView(getContext());
//                            imageViewClassHouse.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewClassHouse.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    417, // Width
//                                    398  // Height
//                            );
//                            layoutParams.setMargins(424, 32, 0, 0);
//                            frameLayoutParent.addView(imageViewClassHouse, layoutParams);
//
//                            indexList++;
//                        } else if (indexList == 2) {
//                            Log.e(TAG, "indexList == 2");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewBlueprint = new ImageView(getContext());
//                            imageViewBlueprint.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewBlueprint.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    450, // Width
//                                    450  // Height
//                            );
//                            layoutParams.setMargins(16, 572, 0, 0);
//                            frameLayoutParent.addView(imageViewBlueprint, layoutParams);
//
//                            indexList++;
//                        } else if (indexList == 3) {
//                            Log.e(TAG, "indexList == 3");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewClassChicken = new ImageView(getContext());
//                            imageViewClassChicken.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewClassChicken.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    360, // Width
//                                    540  // Height
//                            );
//                            layoutParams.setMargins(873, 32, 0, 0);
//                            frameLayoutParent.addView(imageViewClassChicken, layoutParams);
//
//                            indexList++;
//                        } else if (indexList == 4) {
//                            Log.e(TAG, "indexList == 4");
//                            ///////////////////
//                            indexResourceIDs--;
//                            ///////////////////
//
//                            imageViewInstancesOfHouse = new ImageView(getContext());
//                            imageViewInstancesOfHouse.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imageViewInstancesOfHouse.setImageResource(
//                                    (int) listOfResources.get(indexList)
//                            );
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                                    900, // Width
//                                    600  // Height
//                            );
//                            layoutParams.setMargins(360, 430, 0, 0);
//                            frameLayoutParent.addView(imageViewInstancesOfHouse, layoutParams);
//
//                            indexList++;
//                        } else {
//                            Log.e(TAG, "indexList == ELSE");
//                            Log.e(TAG, "ELSE clause");
//
//                            indexList = 0;
//
//                            frameLayoutParent.removeView(imageViewClassCat);
//                            frameLayoutParent.removeView(imageViewClassHouse);
//                            frameLayoutParent.removeView(imageViewClassChicken);
//                            frameLayoutParent.removeView(imageViewBlueprint);
//                            frameLayoutParent.removeView(imageViewInstancesOfHouse);
//
////                            fcvPresentationBox.setVisibility(View.VISIBLE);
//
//                            videoViewFragmentNotBadNotBadBurgers = VideoViewFragment.newInstance(ID_VIDEO_FULL_SCREEN_NOT_BAD_NOT_BAD_BURGERS, new OnCompletionListenerDTO(
//                                    new MediaPlayer.OnCompletionListener() {
//                                        @Override
//                                        public void onCompletion(MediaPlayer mediaPlayer) {
//                                            videoViewFragmentPrimitiveVsObjectType = VideoViewFragment.newInstance(ID_VIDEO_FULL_SCREEN_PRIMITIVE_VS_OBJECT_TYPE, new OnCompletionListenerDTO(
//                                                    new MediaPlayer.OnCompletionListener() {
//                                                        @Override
//                                                        public void onCompletion(MediaPlayer mediaPlayer) {
//                                                            getChildFragmentManager().beginTransaction()
//                                                                    .setReorderingAllowed(true)
//                                                                    .remove(videoViewFragmentPrimitiveVsObjectType)
//                                                                    .addToBackStack(null)
//                                                                    .commit();
//
//                                                            constraintlayoutMarqueeAndPresentationbox.setVisibility(View.VISIBLE);
//                                                            constraintlayoutHost.setVisibility(View.VISIBLE);
//                                                            fcvPresentationBox.setVisibility(View.VISIBLE);
//
//                                                            videoViewHost.start();
//                                                        }
//                                                    }
//                                            ));
//
//                                            getChildFragmentManager().beginTransaction()
//                                                    .setReorderingAllowed(true)
//                                                    .remove(videoViewFragmentNotBadNotBadBurgers)
//                                                    .add(R.id.framelayout_parent, videoViewFragmentPrimitiveVsObjectType)
//                                                    .addToBackStack(null)
//                                                    .commit();
//                                        }
//                                    })
//                            );
////                    replaceFragmentInPresentationBox(videoViewFragment);
//
//                            videoViewHost.pause();
//
//                            fcvPresentationBox.setVisibility(View.INVISIBLE);
//                            constraintlayoutHost.setVisibility(View.INVISIBLE);
//                            constraintlayoutMarqueeAndPresentationbox.setVisibility(View.INVISIBLE);
//
//                            getChildFragmentManager().beginTransaction()
//                                    .setReorderingAllowed(true)
//                                    .add(R.id.framelayout_parent, videoViewFragmentNotBadNotBadBurgers)
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//                    }
//                } else {
//                    fcvPresentationBox.setVisibility(View.VISIBLE);
//
//                    int resourceIdImage = (int) (resourceIDs.get(indexResourceIDs));
//                    ImageViewFragment imageViewFragment = ImageViewFragment.newInstance(resourceIdImage);
//                    replaceFragmentInPresentationBox(imageViewFragment);
//                }
//
//                Log.e(TAG, "indexResourcesIDs: " + indexResourceIDs);
            }
        });

        constraintlayoutHost = view.findViewById(R.id.constraintlayout_host);
        constraintlayoutMarqueeAndPresentationbox = view.findViewById(R.id.constraintlayout_marquee_and_presentationbox);

        videoViewHost = view.findViewById(R.id.video_view_host);

        StringBuilder sb = new StringBuilder();
        String logo = "Next Week Tonight";
        for (int i = 0; i < 100; i++) {
            sb.append(logo + "    ");
        }
        String logoRepeated100Times = sb.toString();
        List<String> rowsOfLogoRepeated100Times = new ArrayList();
        int counter = 0;
        for (int i = 0; i < 100; i++) {
            counter++;
            if (counter > 4) {
                counter = 1;
            }

            if (counter == 1 || counter == 2) {
                rowsOfLogoRepeated100Times.add(logoRepeated100Times);
            } else {
                rowsOfLogoRepeated100Times.add("    " + logoRepeated100Times);
            }
        }
        AnimatedTextViewAdapter adapter = new AnimatedTextViewAdapter(rowsOfLogoRepeated100Times);

        recyclerView = view.findViewById(R.id.rv_animated_textview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getChildFragmentManager().findFragmentById(R.id.fcv_presentation_box) == null) {
            Log.i(TAG, "NO fragment in presentation box");
            ImageViewFragment imageViewFragment = ImageViewFragment.newInstance(-1);
            replaceFragmentInPresentationBox(imageViewFragment);
        } else {
            Log.i(TAG, "YES fragment in presentation box");
        }

        fcvPresentationBox.setVisibility(View.INVISIBLE);

//        imageViewTerraformFarmPlanet = new ImageView(getContext());
//        imageViewTerraformFarmPlanet.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageViewTerraformFarmPlanet.setImageResource(R.drawable.nwt_run_one_slide_1_1of4);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                480, // Width
//                480  // Height
//        );
//        layoutParams.setMargins(32, 32, 0, 0);
//        frameLayoutParent.addView(imageViewTerraformFarmPlanet, layoutParams);
    }

    private void replaceFragmentInPresentationBox(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fcv_presentation_box, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");

        // "If you only stop playing your video in [onStop()], as in the previous
        // step, then on older devices there may be a few seconds where even though
        // the app is no longer visible on screen, the video's audio track continues
        // to play while [onStop()] catches up. This test for older versions of
        // Android pauses the actual playback in [onPause()] to prevent the sound
        // from playing after the app has disappeared from the screen."
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoViewHost.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
        if (showToolbarOnDismiss) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach()");
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private Uri getMedia(String nameMedia) {
        Log.i(TAG, "getMedia() nameMedia: " + nameMedia);

        return Uri.parse("android.resource://" + getActivity().getPackageName() +
                "/raw/" + nameMedia);
    }

    private void initializePlayer() {
        Log.i(TAG, "initializePlayer()");

        Uri videoUri = getMedia(ID_VIDEO_HOST);
        videoViewHost.setVideoURI(videoUri);
        // Skipping to 1 shows the first frame of the video.
        videoViewHost.seekTo(1);

        videoViewHost.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(getContext(), "Playback complete", Toast.LENGTH_SHORT).show();
            }
        });

        videoViewHost.start();
    }

    private void releasePlayer() {
        Log.i(TAG, "releasePlayer()");

        videoViewHost.stopPlayback();
    }
}