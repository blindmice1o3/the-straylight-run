package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.left;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;

import java.util.List;

public class ClassRVAdapterForProject extends RecyclerView.Adapter<ClassRVAdapterForProject.ViewHolder> {
    public static final String TAG = ClassRVAdapterForProject.class.getSimpleName();

    public interface GestureListener {
        void onSingleTapUp(int position);
    }

    private GestureListener gestureListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private GestureDetectorCompat detector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);

            detector = new GestureDetectorCompat(context, new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(@NonNull MotionEvent motionEvent) {
//                Log.e(TAG, "onDown: " + motionEvent.toString());
                    return true;
                }

                @Override
                public void onShowPress(@NonNull MotionEvent motionEvent) {
//                Log.e(TAG, "onShowPress: " + motionEvent.toString());
                }

                @Override
                public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                    Log.e(TAG, "onSingleTapUp: " + motionEvent.toString());
                    gestureListener.onSingleTapUp(
                            getAbsoluteAdapterPosition()
                    );
                    return true;
                }

                @Override
                public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
//                Log.e(TAG, "onScroll: " + motionEvent.toString() + motionEvent1.toString());
                    return true;
                }

                @Override
                public void onLongPress(@NonNull MotionEvent motionEvent) {
                    Log.e(TAG, getAdapterPosition() + ". onLongPress: " + motionEvent.toString());
                }

                @Override
                public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
//                Log.e(TAG, "onFling: " + motionEvent.toString() + motionEvent1.toString());
                    return true;
                }
            });
            detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
                    Log.e(TAG, "onSingleTapConfirmed: " + motionEvent.toString());
                    return true;
                }

                @Override
                public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
                    Log.e(TAG, "onDoubleTap: " + motionEvent.toString());
                    return true;
                }

                @Override
                public boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent) {
                    Log.e(TAG, "onDoubleTapEvent: " + motionEvent.toString());
                    return true;
                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (detector.onTouchEvent(motionEvent)) {
                        return true;
                    }
                    return false;
                }
            });
        }

        public TextView getTvName() {
            return tvName;
        }
    }

    private Context context;
    private List<Class> classes;

    public ClassRVAdapterForProject(Context context, List<Class> classes, GestureListener gestureListener) {
        this.context = context;
        this.classes = classes;
        this.gestureListener = gestureListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View classView = inflater.inflate(R.layout.listitem_class_for_project, parent, false);
        ViewHolder viewHolder = new ViewHolder(classView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class myClass = classes.get(position);

        holder.getTvName().setText(
                myClass.getName()
        );
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }
}
