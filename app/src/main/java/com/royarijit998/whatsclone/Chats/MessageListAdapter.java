package com.royarijit998.whatsclone.Chats;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.royarijit998.whatsclone.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

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
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        if(currUserId.equals(MessageArrayList.get(position).getSenderID()))
            holder.senderTextView.setText("You");
        else
            holder.senderTextView.setText(MessageArrayList.get(position).getSenderName());

        if(!MessageArrayList.get(position).getMessage().isEmpty())
            holder.messageTextView.setText(MessageArrayList.get(position).getMessage());
        else
            holder.messageTextView.setVisibility(View.GONE);

        if(MessageArrayList.get(holder.getAdapterPosition()).getUrlArrayList().isEmpty()){
            holder.viewMediaBtn.setVisibility(View.GONE);
        }

        holder.viewMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(v.getContext(), MessageArrayList.get(holder.getAdapterPosition()).getUrlArrayList())
                        .setStartPosition(0)
                        .show();
            }
        });

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView, messageTextView;
        private Button viewMediaBtn;
        private LinearLayout itemLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            viewMediaBtn = itemView.findViewById(R.id.viewMediaBtn);
            itemLayout = itemView.findViewById(R.id.messageItemLayout);
        }
    }


}

