package com.royarijit998.whatsclone.Chats;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royarijit998.whatsclone.R;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    ArrayList<Message> MessageArrayList;

    public MessageListAdapter(ArrayList<Message> MessageArrayList) {
        this.MessageArrayList = MessageArrayList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null, false);
        MessageViewHolder MessageViewHolder =  new MessageViewHolder(layoutView);
        return MessageViewHolder;
    }

    @Override
    public int getItemCount() {
        return MessageArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {
        holder.senderTextView.setText(MessageArrayList.get(position).getSenderID());
        holder.messageTextView.setText(MessageArrayList.get(position).getMessage());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView, messageTextView;
        private LinearLayout itemLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            itemLayout = itemView.findViewById(R.id.messageItemLayout);
        }
    }


}

