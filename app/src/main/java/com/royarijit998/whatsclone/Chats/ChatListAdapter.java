package com.royarijit998.whatsclone.Chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royarijit998.whatsclone.ChatActivity;
import com.royarijit998.whatsclone.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    ArrayList<Chat> ChatArrayList;

    public ChatListAdapter(ArrayList<Chat> ChatArrayList) {
        this.ChatArrayList = ChatArrayList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null, false);
        ChatViewHolder ChatViewHolder =  new ChatViewHolder(layoutView);
        return ChatViewHolder;
    }

    @Override
    public int getItemCount() {
        return ChatArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {
        holder.titleTextView.setText(ChatArrayList.get(position).getChatID());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatID", ChatArrayList.get(position).getChatID());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private LinearLayout itemLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            itemLayout = itemView.findViewById(R.id.chatItemLayout);
        }
    }


}
