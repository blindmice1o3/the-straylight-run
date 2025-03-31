package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;

import java.util.ArrayList;
import java.util.List;

public class ClassRVAdapterForStructure extends RecyclerView.Adapter<ClassRVAdapterForStructure.ViewHolder> {
    public static final String TAG = ClassRVAdapterForStructure.class.getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
        }

        public TextView getTvName() {
            return tvName;
        }
    }

    private Class myClass;
    private List<ClassComponent> classComponents;

    public ClassRVAdapterForStructure(Class myClass) {
        this.myClass = myClass;

        classComponents = new ArrayList<>();
        classComponents.addAll(
                myClass.getMethods()
        );
        classComponents.addAll(
                myClass.getFields()
        );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View classView = inflater.inflate(R.layout.listitem_class_for_structure, parent, false);
        ViewHolder viewHolder = new ViewHolder(classView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassComponent classComponent = classComponents.get(position);

        String typeOfComponent = (classComponent instanceof Field) ? "f" : "m";

        holder.getTvName().setText(
                typeOfComponent + " - " + classComponent.getName()
        );
    }

    @Override
    public int getItemCount() {
        return classComponents.size();
    }
}
