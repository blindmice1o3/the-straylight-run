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
    private static final String ID_VIDEO_HOST = "vid_20250907_164743392_run_one_attempt_3_pre";
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
    private ImageView ivClassCat, ivClassHouse, ivClassChicken,
            ivTypePrimitiveVsObject, ivJavaReservedWordsPrimitive,
            ivMethodSignature, ivBookVsJavaProgram, ivMainGetterSetterConstructorComments;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");
        fcvPresentationBox = view.findViewById(R.id.fcv_presentation_box);

        frameLayoutParent = view.findViewById(R.id.framelayout_parent);
        frameLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List listOfResources = (List) resourceIDs.get(0);

                if (indexList == 0) {
                    // 3 classes (base)
                    Log.e(TAG, "indexList == 0");

                    ivClassCat = new ImageView(getContext());
                    ivClassCat.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParamsCat = new FrameLayout.LayoutParams(
                            360, // Width
                            540  // Height
                    );
                    layoutParamsCat.setMargins(32, 32, 0, 0);
                    frameLayoutParent.addView(ivClassCat, layoutParamsCat);
                    indexList++;

                    ivClassHouse = new ImageView(getContext());
                    ivClassHouse.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParamsHouse = new FrameLayout.LayoutParams(
                            417, // Width
                            398  // Height
                    );
                    layoutParamsHouse.setMargins(424, 32, 0, 0);
                    frameLayoutParent.addView(ivClassHouse, layoutParamsHouse);
                    indexList++;

                    ivClassChicken = new ImageView(getContext());
                    ivClassChicken.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParamsChicken = new FrameLayout.LayoutParams(
                            360, // Width
                            246  // Height
                    );
                    layoutParamsChicken.setMargins(873, 32, 0, 0);
                    frameLayoutParent.addView(ivClassChicken, layoutParamsChicken);
                    indexList++;
                } else if (indexList == 3) {
                    // 3 classes (fields)
                    Log.e(TAG, "indexList == 3");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 6) {
                    // 3 classes (methods)
                    Log.e(TAG, "indexList == 6");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 9) {
                    // 3 classes (fields)
                    Log.e(TAG, "indexList == 9");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 12) {
                    // 3 classes (fields - type)
                    Log.e(TAG, "indexList == 12");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 15) {
                    // 3 classes (fields - name)
                    Log.e(TAG, "indexList == 15");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 18) {
                    // primitive vs object type (base)
                    Log.e(TAG, "indexList == 18");

                    ivTypePrimitiveVsObject = new ImageView(getContext());
                    ivTypePrimitiveVsObject.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivTypePrimitiveVsObject.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            540, // Width
                            214  // Height
                    );
                    layoutParams.setMargins(32, 604, 0, 0);
                    frameLayoutParent.addView(ivTypePrimitiveVsObject, layoutParams);
                    indexList++;
                } else if (indexList == 19) {
                    // java reserved words (primitive types)
                    Log.e(TAG, "indexList == 19");

                    ivJavaReservedWordsPrimitive = new ImageView(getContext());
                    ivJavaReservedWordsPrimitive.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivJavaReservedWordsPrimitive.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            540, // Width
                            360  // Height
                    );
                    layoutParams.setMargins(604, 604, 0, 0);
                    frameLayoutParent.addView(ivJavaReservedWordsPrimitive, layoutParams);
                    indexList++;
                } else if (indexList == 20) {
                    // primitive vs object type (primitive)
                    Log.e(TAG, "indexList == 20");

                    ivTypePrimitiveVsObject.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 21) {
                    // primitive vs object type (object)
                    Log.e(TAG, "indexList == 21");

                    ivTypePrimitiveVsObject.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 22) {
                    // 3 classes (methods)
                    // method signature (base)
                    Log.e(TAG, "indexList == 22");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    frameLayoutParent.removeView(ivJavaReservedWordsPrimitive);
                    frameLayoutParent.removeView(ivTypePrimitiveVsObject);

                    ivMethodSignature = new ImageView(getContext());
                    ivMethodSignature.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivMethodSignature.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParamsMethodSignature = new FrameLayout.LayoutParams(
                            1080, // Width
                            404 // Height
                    );
                    layoutParamsMethodSignature.setMargins(32, 588, 0, 0);
                    frameLayoutParent.addView(ivMethodSignature, layoutParamsMethodSignature);
                    indexList++;
                } else if (indexList == 26) {
                    // method signature (name)
                    Log.e(TAG, "indexList == 26");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivMethodSignature.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 30) {
                    // method signature (return type)
                    Log.e(TAG, "indexList == 30");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivMethodSignature.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 34) {
                    // method signature (argument list)
                    Log.e(TAG, "indexList == 34");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivMethodSignature.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 38) {
                    // method signature (body)
                    Log.e(TAG, "indexList == 38");

                    ivClassCat.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassHouse.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                    ivClassChicken.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;

                    ivMethodSignature.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    indexList++;
                } else if (indexList == 42) {
                    // structure of book and java program
                    Log.e(TAG, "indexList == 42");

                    frameLayoutParent.removeView(ivClassCat);
                    frameLayoutParent.removeView(ivClassHouse);
                    frameLayoutParent.removeView(ivClassChicken);
                    frameLayoutParent.removeView(ivMethodSignature);

                    ivBookVsJavaProgram = new ImageView(getContext());
                    ivBookVsJavaProgram.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivBookVsJavaProgram.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParamsBookVsJavaProgram = new FrameLayout.LayoutParams(
                            864, // Width
                            576 // Height
                    );
                    layoutParamsBookVsJavaProgram.setMargins(32, 32, 0, 0);
                    frameLayoutParent.addView(ivBookVsJavaProgram, layoutParamsBookVsJavaProgram);
                    indexList++;
                } else if (indexList == 43) {
                    // main, getter+setter, constructor, comments
                    Log.e(TAG, "indexList == 43");

                    frameLayoutParent.removeView(ivBookVsJavaProgram);

                    ivMainGetterSetterConstructorComments = new ImageView(getContext());
                    ivMainGetterSetterConstructorComments.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivMainGetterSetterConstructorComments.setImageResource(
                            (int) listOfResources.get(indexList)
                    );
                    FrameLayout.LayoutParams layoutParamsMainGetterSetterConstructorComments = new FrameLayout.LayoutParams(
                            864, // Width
                            576 // Height
                    );
                    layoutParamsMainGetterSetterConstructorComments.setMargins(32, 32, 0, 0);
                    frameLayoutParent.addView(ivMainGetterSetterConstructorComments, layoutParamsMainGetterSetterConstructorComments);
                    indexList++;
                } else if (indexList == 44) {
                    Log.e(TAG, "indexList == 44");

                    // TODO:
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