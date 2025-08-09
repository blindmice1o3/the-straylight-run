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
    private static final String VIDEO_SAMPLE = "pxl_20250429_193429506";
    private static final String RESOURCE_ID_VIDEO = "vid_20230603_145112";
    private static final int RESOURCE_ID_DRAWABLE = R.drawable.corgi_crusade_editted;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean showToolbarOnDismiss;
    private List resourceIDs;
    private int indexResourceIDs;

    private FrameLayout frameLayoutParent;
    private RecyclerView recyclerView;
    private FragmentContainerView fcvPresentationBox;
    private VideoView videoViewHost;

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

            List listForNestedImageViewsFragment = new ArrayList();
            listForNestedImageViewsFragment.add(R.drawable.nwt_run_one_slide1_1of3);
            listForNestedImageViewsFragment.add(R.drawable.nwt_run_one_slide1_2of3);
            listForNestedImageViewsFragment.add(R.drawable.nwt_run_one_slide1_3of3);

            resourceIDs = new ArrayList();
            resourceIDs.add(VIDEO_SAMPLE);
//            resourceIDs.add(imageWithSlideAnimation);
            resourceIDs.add(RESOURCE_ID_DRAWABLE);
            resourceIDs.add(listForNestedImageViewsFragment);
            resourceIDs.add(VIDEO_SAMPLE);
            resourceIDs.add(RESOURCE_ID_DRAWABLE);
            resourceIDs.add(VIDEO_SAMPLE);
            resourceIDs.add(RESOURCE_ID_DRAWABLE);
            resourceIDs.add(VIDEO_SAMPLE);
            resourceIDs.add(RESOURCE_ID_DRAWABLE);

            indexResourceIDs = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_next_week_tonight_episodes_generator, container, false);
    }

    private int indexList;
    private ImageView imageViewNotePrimitiveTypesPt1, imageViewSceneFarm, imageViewRobot, imageViewJavaReservedWords;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");
        fcvPresentationBox = view.findViewById(R.id.fcv_presentation_box);

        frameLayoutParent = view.findViewById(R.id.framelayout_parent);
        frameLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///////////////////
                indexResourceIDs++;
                ///////////////////

                if (indexResourceIDs > resourceIDs.size() - 1) {
                    indexResourceIDs = 0;
                }

                if (resourceIDs.get(indexResourceIDs) instanceof ImageWithSlideAnimation) {
                    Log.e(TAG, "ImageWithSlideAnimation FOUND!!!!!");
                    ImageWithSlideAnimation imageWithSlideAnimation = (ImageWithSlideAnimation) resourceIDs.get(indexResourceIDs);

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            542, // Width
                            720  // Height
                    );
                    layoutParams.setMargins(64, 128, 0, 64);
                    frameLayoutParent.addView(imageViewNotePrimitiveTypesPt1, layoutParams);

                    imageWithSlideAnimation.startAnimator();
                } else if (resourceIDs.get(indexResourceIDs) instanceof List) {
                    Log.e(TAG, "LIST FOUND!!!!!");

                    List listOfResources = (List) resourceIDs.get(indexResourceIDs);
                    if (indexList == 0) {
                        fcvPresentationBox.setVisibility(View.INVISIBLE);
                        ///////////////////
                        indexResourceIDs--;
                        ///////////////////

                        imageViewSceneFarm = new ImageView(getContext());
                        imageViewSceneFarm.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageViewSceneFarm.setImageResource(
                                (int) listOfResources.get(indexList)
                        );
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                320, // Width
                                480  // Height
                        );
                        layoutParams.setMargins(64, 128, 0, 0);
                        frameLayoutParent.addView(imageViewSceneFarm, layoutParams);

                        indexList++;
                    } else if (indexList == 1) {
                        ///////////////////
                        indexResourceIDs--;
                        ///////////////////

                        imageViewRobot = new ImageView(getContext());
                        imageViewRobot.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageViewRobot.setImageResource(
                                (int) listOfResources.get(indexList)
                        );
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                320, // Width
                                320  // Height
                        );
                        layoutParams.setMargins(128, 192, 0, 0);
                        frameLayoutParent.addView(imageViewRobot, layoutParams);

                        indexList++;
                    } else if (indexList == listOfResources.size() - 1) {
                        ///////////////////
                        indexResourceIDs--;
                        ///////////////////

                        imageViewJavaReservedWords = new ImageView(getContext());
                        imageViewJavaReservedWords.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageViewJavaReservedWords.setImageResource(
                                (int) listOfResources.get(indexList)
                        );
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                480, // Width
                                320  // Height
                        );
                        layoutParams.setMargins(96, 256, 0, 0);
                        frameLayoutParent.addView(imageViewJavaReservedWords, layoutParams);

                        indexList++;
                    } else {
                        Log.e(TAG, "ELSE clause");

                        indexList = 0;

                        frameLayoutParent.removeView(imageViewSceneFarm);
                        frameLayoutParent.removeView(imageViewRobot);
                        frameLayoutParent.removeView(imageViewJavaReservedWords);

                        fcvPresentationBox.setVisibility(View.VISIBLE);
                    }
                } else if (resourceIDs.get(indexResourceIDs) instanceof String) {
                    String resourceIdVideo = (String) resourceIDs.get(indexResourceIDs);
                    VideoViewFragment videoViewFragment = VideoViewFragment.newInstance(resourceIdVideo);
                    replaceFragmentInContainer(videoViewFragment);
                } else {
                    int resourceIdImage = (int) (resourceIDs.get(indexResourceIDs));
                    ImageViewFragment imageViewFragment = ImageViewFragment.newInstance(resourceIdImage);
                    replaceFragmentInContainer(imageViewFragment);
                }

                Log.e(TAG, "indexResourcesIDs: " + indexResourceIDs);
            }
        });

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
            replaceFragmentInContainer(imageViewFragment);
        } else {
            Log.i(TAG, "YES fragment in presentation box");
        }
    }

    private void replaceFragmentInContainer(Fragment fragment) {
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

        Uri videoUri = getMedia(VIDEO_SAMPLE);
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