package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jackingaming.thestraylightrun.R;

public class CupCaddyFragment extends Fragment {
    public static final String TAG_DEBUG = CupCaddyFragment.class.getSimpleName();

    public static final String TAG_TRENTA = "trenta";
    public static final String TAG_VENTI = "venti";
    public static final String TAG_GRANDE = "grande";
    public static final String TAG_TALL = "tall";
    public static final String TAG_SHORT = "short";

    private CupCaddyViewModel mViewModel;
    private LinearLayout llCupCaddy;
    private ImageView ivTrenta, ivVenti, ivGrande, ivTall, ivShort;

    public static CupCaddyFragment newInstance() {
        Log.e(TAG_DEBUG, "newInstance()");
        return new CupCaddyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(TAG_DEBUG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_cup_caddy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG_DEBUG, "onViewCreated()");

        llCupCaddy = view.findViewById(R.id.ll_cup_caddy);

        ivTrenta = view.findViewById(R.id.iv_trenta);
        ivVenti = view.findViewById(R.id.iv_venti);
        ivGrande = view.findViewById(R.id.iv_grande);
        ivTall = view.findViewById(R.id.iv_tall);
        ivShort = view.findViewById(R.id.iv_short);

        ivTrenta.setTag(TAG_TRENTA);
        ivVenti.setTag(TAG_VENTI);
        ivGrande.setTag(TAG_GRANDE);
        ivTall.setTag(TAG_TALL);
        ivShort.setTag(TAG_SHORT);

        View.OnTouchListener myOnTouchListener = new MyOnTouchListener();
        ivTrenta.setOnTouchListener(myOnTouchListener);
        ivVenti.setOnTouchListener(myOnTouchListener);
        ivGrande.setOnTouchListener(myOnTouchListener);
        ivTall.setOnTouchListener(myOnTouchListener);
        ivShort.setOnTouchListener(myOnTouchListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG_DEBUG, "onActivityCreated()");

        mViewModel = new ViewModelProvider(this).get(CupCaddyViewModel.class);
        // TODO: Use the ViewModel
    }

    private class MyOnTouchListener
            implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Create a new ClipData. This is done in two steps to provide clarity. The
                // convenience method ClipData.newPlainText() can create a plain text
                // ClipData in one step.

                String label = view.getTag().toString();
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                // Create a new ClipData.Item from the ImageView object's tag.
                ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

                // Create a new ClipData using the tag as a label, the plain text MIME type,
                // and the already-created item. This creates a new ClipDescription object
                // within the ClipData and sets its MIME type to "text/plain".
                ClipData dragData = new ClipData(
                        label,
                        mimeTypes,
                        item
                );
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                // Start the drag.
                view.startDragAndDrop(
                        dragData,           // The data to be dragged.
                        myShadow,           // The drag shadow builder.
                        null,    // No need to use local data.
                        0              // Flags. Not currently used, set to 0.
                );

                Log.e("CupCaddyFragment", "label: " + label);

                // Indicate that the on-touch event is handled.
                return true;
            }

            return false;
        }
    }
}