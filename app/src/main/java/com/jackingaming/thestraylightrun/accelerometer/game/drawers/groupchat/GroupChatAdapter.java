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
                        messages.add(new Message("Mulan", "MEOW?", false));
                        notifyItemInserted(indexNewMessage);
                    } else if (counter == 2) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Muhang", "meow", false));
                        notifyItemInserted(indexNewMessage);
                    } else if (counter == 3) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Mom", "hello", true));
                        notifyItemInserted(indexNewMessage);
                    } else if (counter == 4) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Apsara", "meow", false));
                        notifyItemInserted(indexNewMessage);
                    } else if (counter == 5) {
                        int indexNewMessage = messages.size();
                        messages.add(new Message("Colin", "silence", false));
                        notifyItemInserted(indexNewMessage);
                    }
                }
            });
        }

        public void bind(Message messageCurrent) {
            String message = messageCurrent.getMessage();

            if (messageCurrent.isFromPlayer()) {
                tvNameOfSender.setVisibility(View.GONE);
            } else {
                String nameOfSender = messageCurrent.getNameOfSender();
                tvNameOfSender.setText(nameOfSender);
            }

            tvMessage.setText(message);
        }

        public TextView getTvNameOfSender() {
            return tvNameOfSender;
        }

        public TextView getTvMessage() {
            return tvMessage;
        }
    }

    private static final int VIEW_TYPE_PLAYER = 1;
    private static final int VIEW_TYPE_NPC = 2;
    private List<Message> messages;
    private int counter = 0;

    public GroupChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isFromPlayer() ?
                VIEW_TYPE_PLAYER :
                VIEW_TYPE_NPC;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View groupChatMessageView;
        if (viewType == VIEW_TYPE_PLAYER) {
            groupChatMessageView = inflater.inflate(R.layout.listitem_group_chat_message_player, parent, false);
        } else {
            groupChatMessageView = inflater.inflate(R.layout.listitem_group_chat_message_npc, parent, false);
        }

        return new ViewHolder(groupChatMessageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message messageCurrent = messages.get(position);

        holder.bind(messageCurrent);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
