package com.jackingaming.thestraylightrun.accelerometer.game.quests.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

import java.util.List;

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private OnItemClickListener onItemClickListener;

    public class QuestViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTAG;

        public QuestViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTAG = itemView.findViewById(R.id.tv_tag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }

        public TextView getTextViewTAG() {
            return textViewTAG;
        }
    }

    private List<Quest> quests;

    public QuestAdapter(List<Quest> quests, OnItemClickListener onItemClickListener) {
        this.quests = quests;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View questView = inflater.inflate(R.layout.item_quest, parent, false);
        QuestViewHolder viewHolder = new QuestViewHolder(questView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {
        Quest quest = quests.get(position);

        TextView textViewTAG = holder.getTextViewTAG();
        textViewTAG.setText(quest.getTAG());
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }
}
