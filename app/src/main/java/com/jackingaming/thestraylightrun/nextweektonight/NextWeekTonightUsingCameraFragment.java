package com.jackingaming.thestraylightrun.nextweektonight;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NextWeekTonightUsingCameraFragment extends Fragment
        implements Serializable {
    public static final String TAG = NextWeekTonightUsingCameraFragment.class.getSimpleName();
    public static final String ARG_SHOW_TOOLBAR_ON_DISMISS = "showToolbarOnDismiss";
    private static final int CAMERA_ID_BACK_CAMERA = 0;
    private static final int CAMERA_ID_FRONT_CAMERA = 1;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean showToolbarOnDismiss;

    private FrameLayout frameLayoutParent;
    private RecyclerView recyclerView;
    private FragmentContainerView fcvPresentationBox;
    private SurfaceView surfaceView;
    private Camera camera;

    public NextWeekTonightUsingCameraFragment() {
        // Required empty public constructor
    }

    public static NextWeekTonightUsingCameraFragment newInstance(boolean showToolbarOnDismiss) {
        NextWeekTonightUsingCameraFragment fragment = new NextWeekTonightUsingCameraFragment();

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_next_week_tonight_using_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");
        frameLayoutParent = view.findViewById(R.id.framelayout_parent);
        frameLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoViewFragment videoViewFragment = VideoViewFragment.newInstance(null, null);
                replaceFragmentInContainer(videoViewFragment);
            }
        });

        surfaceView = view.findViewById(R.id.surface_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                // Tell the camera to use this surface as its preview area
                try {
                    if (camera != null) {
                        camera.setPreviewDisplay(surfaceHolder);
                    }
                } catch (IOException exception) {
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                if (camera == null) {
                    return;
                }

                setCameraDisplayOrientation(CAMERA_ID_BACK_CAMERA, camera);

                // The surface has changed size; update the camera preview size
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), i1, i2);
                parameters.setPreviewSize(s.width, s.height);
                camera.setParameters(parameters);
                try {
                    camera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "Could not start preview", e);
                    camera.release();
                    camera = null;
                }
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                // We can no longer display on this surface, so stop the preview.
                if (camera != null) {
                    camera.stopPreview();
                }
            }
        });

        StringBuilder sb = new StringBuilder();
        String logo = "Next Week Tonight";
        for (int i = 0; i < 100; i++) {
            sb.append(logo + "    ");
        }
        String logoRepeated100Times = sb.toString();
        List<String> rowsOfLogoRepeated100Times = new ArrayList();
        for (int i = 0; i < 100; i++) {
            rowsOfLogoRepeated100Times.add(logoRepeated100Times);
        }
        AnimatedTextViewAdapter adapter = new AnimatedTextViewAdapter(rowsOfLogoRepeated100Times);

        recyclerView = view.findViewById(R.id.rv_animated_textview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getChildFragmentManager().findFragmentById(R.id.fcv_presentation_box) == null) {
            Log.i(TAG, "NO fragment in presentation box");
            ImageViewFragment imageViewFragment = ImageViewFragment.newInstance(null, null);
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

    private void setCameraDisplayOrientation(int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        camera = Camera.open(CAMERA_ID_BACK_CAMERA);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        if (camera != null) {
            camera.release();
            camera = null;
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
}
