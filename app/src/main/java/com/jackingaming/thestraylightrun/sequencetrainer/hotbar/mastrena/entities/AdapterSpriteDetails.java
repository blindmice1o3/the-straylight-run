package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSpriteDetails extends RecyclerView.Adapter<AdapterSpriteDetails.ViewHolderSpriteDetails> {
    public static final String TAG = AdapterSpriteDetails.class.getSimpleName();

    private List<String> spriteDetails = new ArrayList<>();

    public AdapterSpriteDetails(List<String> spriteDetails) {
        this.spriteDetails = spriteDetails;
    }

    @NonNull
    @Override
    public ViewHolderSpriteDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_sprite_detail, parent, false);
        return new ViewHolderSpriteDetails(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSpriteDetails holder, int position) {
        String spriteDetailAsString = spriteDetails.get(position);
        holder.tvSpriteDetail.setText(spriteDetailAsString);
    }

    @Override
    public int getItemCount() {
        return spriteDetails.size();
    }

    public class ViewHolderSpriteDetails extends RecyclerView.ViewHolder {

        public TextView tvSpriteDetail;

        public ViewHolderSpriteDetails(@NonNull View itemView) {
            super(itemView);

            tvSpriteDetail = itemView.findViewById(R.id.tv_sprite_detail);
        }
    }
}
