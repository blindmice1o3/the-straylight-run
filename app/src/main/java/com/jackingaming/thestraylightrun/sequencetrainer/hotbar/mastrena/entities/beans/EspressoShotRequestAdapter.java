package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.beans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.util.List;

public class EspressoShotRequestAdapter extends RecyclerView.Adapter<EspressoShotRequestAdapter.ViewHolder> {

    private List<EspressoShotRequest> espressoShotRequests;

    public EspressoShotRequestAdapter(List<EspressoShotRequest> espressoShotRequests) {
        this.espressoShotRequests = espressoShotRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View espressoShotRequestView = inflater.inflate(R.layout.listitem_espresso_shot_request, parent, false);
        return new ViewHolder(espressoShotRequestView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EspressoShotRequest espressoShotRequest = espressoShotRequests.get(position);

        TextView tvQuantity = holder.tvQuantity;
        tvQuantity.setText(Integer.toString(espressoShotRequest.getQuantity()));

        TextView tvType = holder.tvType;
        tvType.setText(espressoShotRequest.getType().name());

        TextView tvAmountOfWater = holder.tvAmountOfWater;
        tvAmountOfWater.setText(espressoShotRequest.getAmountOfWater().name());

        TextView tvAmountOfBean = holder.tvAmountOfBean;
        tvAmountOfBean.setText(espressoShotRequest.getAmountOfBean().name());
    }

    @Override
    public int getItemCount() {
        return espressoShotRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvQuantity;
        public TextView tvType;
        public TextView tvAmountOfWater;
        public TextView tvAmountOfBean;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvType = itemView.findViewById(R.id.tv_type);
            tvAmountOfWater = itemView.findViewById(R.id.tv_amount_of_water);
            tvAmountOfBean = itemView.findViewById(R.id.tv_amount_of_bean);
        }
    }
}
