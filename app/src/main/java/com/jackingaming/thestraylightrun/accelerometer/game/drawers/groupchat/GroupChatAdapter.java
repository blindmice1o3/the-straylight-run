package com.jackingaming.thestraylightrun.accelerometer.game.drawers.groupchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    public static final String TAG = GroupChatAdapter.class.getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameOfSender;
        private TextView tvMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNameOfSender = itemView.findViewById(R.id.tv_name_of_sender);
            tvMessage = itemView.findViewById(R.id.tv_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counter++;

                    if (counter == 1) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Mulan", "MEOW?"));
                        notifyItemInserted(indexNewMessage);
                    } else if (counter == 2) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Muhang", "meow"));
                        notifyItemInserted(indexNewMessage);
                    } else if (counter == 3) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Apsara", "meow"));
                        notifyItemInserted(indexNewMessage);
                    } else if (counter == 4) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Colin", "silence"));
                        notifyItemInserted(indexNewMessage);
                    }
                }
            });
        }

        public TextView getTvNameOfSender() {
            return tvNameOfSender;
        }

        public TextView getTvMessage() {
            return tvMessage;
        }
    }

    private List<Message> messages;
    private int counter = 0;

    public GroupChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View groupChatMessageView = inflater.inflate(R.layout.listitem_group_chat_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(groupChatMessageView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message messageCurrent = messages.get(position);

        String nameOfSender = messageCurrent.getNameOfSender();
        String message = messageCurrent.getMessage();

        holder.getTvNameOfSender().setText(nameOfSender + ": ");
        holder.getTvMessage().setText(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
