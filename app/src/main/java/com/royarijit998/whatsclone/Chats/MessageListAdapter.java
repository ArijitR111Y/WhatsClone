package com.royarijit998.whatsclone.Chats;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.royarijit998.whatsclone.R;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    ArrayList<Message> MessageArrayList;
    String currUserId;

    public MessageListAdapter(ArrayList<Message> MessageArrayList) {
        this.MessageArrayList = MessageArrayList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, null, false);
        MessageViewHolder MessageViewHolder =  new MessageViewHolder(layoutView);
        currUserId = FirebaseAuth.getInstance().getUid();
        return MessageViewHolder;
    }

    @Override
    public int getItemCount() {
        return MessageArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {
        if(currUserId.equals(MessageArrayList.get(position).getSenderID()))
            holder.senderTextView.setText("You");
        else
            holder.senderTextView.setText(MessageArrayList.get(position).getSenderName());
        holder.messageTextView.setText(MessageArrayList.get(position).getMessage());

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

