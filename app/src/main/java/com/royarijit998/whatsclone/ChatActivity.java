package com.royarijit998.whatsclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.royarijit998.whatsclone.Chats.Message;
import com.royarijit998.whatsclone.Chats.MessageListAdapter;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private RecyclerView.Adapter messageListAdapter;
    private RecyclerView.LayoutManager messageListLayoutManager;
    private ArrayList<Message> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageArrayList = new ArrayList<>();


        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        // For more seamless scrolling
        messageRecyclerView.setNestedScrollingEnabled(false);
        messageRecyclerView.setHasFixedSize(false);
        messageListLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        messageRecyclerView.setLayoutManager(messageListLayoutManager);
        messageListAdapter = new MessageListAdapter(messageArrayList);
        messageRecyclerView.setAdapter(messageListAdapter);
    }
}
