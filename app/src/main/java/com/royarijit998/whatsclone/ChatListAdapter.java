package com.royarijit998.whatsclone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    ArrayList<ChatItem> ChatArrayList;

    public ChatListAdapter(ArrayList<ChatItem> ChatArrayList) {
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
