package com.jackingaming.thestraylightrun.nextweektonight;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.util.List;

public class AnimatedTextViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = AnimatedTextViewAdapter.class.getSimpleName();
    public static final int VIEW_TYPE_LTR = 0;
    public static final int VIEW_TYPE_RTL = 1;

    private List<String> rowsOfLogoRepeated100Times;

    public AnimatedTextViewAdapter(List<String> rowsOfLogoRepeated100Times) {
        this.rowsOfLogoRepeated100Times = rowsOfLogoRepeated100Times;
    }

    @Override
    public int getItemViewType(int position) {
//        Log.i(TAG, "getItemViewType() position: " + position);
        return (position % 2 == 0) ? VIEW_TYPE_LTR : VIEW_TYPE_RTL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.i(TAG, "onCreateViewHolder()");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_LTR:
                View viewLTR = inflater.inflate(R.layout.listitem_textview_marquee_ltr, parent, false);
                return new ViewHolderLTR(viewLTR);
            case VIEW_TYPE_RTL:
                View viewRTL = inflater.inflate(R.layout.listitem_textview_marquee_rtl, parent, false);
                return new ViewHolderRTL(viewRTL);
            default:
                Log.i(TAG, "switch's default");
                View viewLTRDefault = inflater.inflate(R.layout.listitem_textview_marquee_ltr, parent, false);
                return new ViewHolderLTR(viewLTRDefault);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Log.i(TAG, "onBindViewHolder()");
        String logoRepeated100Times = rowsOfLogoRepeated100Times.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_LTR:
                ViewHolderLTR viewHolderLTR = (ViewHolderLTR) holder;
                viewHolderLTR.bind(logoRepeated100Times);
                break;
            case VIEW_TYPE_RTL:
                ViewHolderRTL viewHolderRTL = (ViewHolderRTL) holder;
                viewHolderRTL.bind(logoRepeated100Times);
                break;
            default:
                Log.i(TAG, "switch's default");
                ViewHolderLTR viewHolderLTRDefault = (ViewHolderLTR) holder;
                viewHolderLTRDefault.bind(logoRepeated100Times);
                break;
        }
    }

    @Override
    public int getItemCount() {
//        Log.i(TAG, "getItemCount()");
        return rowsOfLogoRepeated100Times.size();
    }

    class ViewHolderLTR extends RecyclerView.ViewHolder {
        private TextView tvLTR;

        public ViewHolderLTR(View itemView) {
            super(itemView);

            tvLTR = itemView.findViewById(R.id.tv_ltr);
        }

        public void bind(String logoRepeated100Times) {
            tvLTR.setText(logoRepeated100Times);
            tvLTR.setSelected(true);
        }
    }

    class ViewHolderRTL extends RecyclerView.ViewHolder {
        private TextView tvRTL;

        public ViewHolderRTL(View itemView) {
            super(itemView);

            tvRTL = itemView.findViewById(R.id.tv_rtl);
        }

        public void bind(String logoRepeated100Times) {
            tvRTL.setText(logoRepeated100Times);
            tvRTL.setSelected(true);
        }
    }
}
