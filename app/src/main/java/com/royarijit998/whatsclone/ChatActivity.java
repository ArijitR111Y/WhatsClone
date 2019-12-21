package com.royarijit998.whatsclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.royarijit998.whatsclone.Chats.Message;
import com.royarijit998.whatsclone.Chats.MessageListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private RecyclerView.Adapter messageListAdapter;
    private RecyclerView.LayoutManager messageListLayoutManager;
    private ArrayList<Message> messageArrayList;
    private Button sendBtn;
    private String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");

        messageArrayList = new ArrayList<>();
        getChatMessages();


        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        // For more seamless scrolling
        messageRecyclerView.setNestedScrollingEnabled(false);
        messageRecyclerView.setHasFixedSize(false);
        messageListLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        messageRecyclerView.setLayoutManager(messageListLayoutManager);
        messageListAdapter = new MessageListAdapter(messageArrayList);
        messageRecyclerView.setAdapter(messageListAdapter);

        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    public void sendMessage(){
        EditText sendMsgEditText = findViewById(R.id.sendMsgEditText);
        if(!sendMsgEditText.getText().toString().isEmpty()){
            Message message = new Message(FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).push().getKey(), FirebaseAuth.getInstance().getUid(), sendMsgEditText.getText().toString());
            HashMap<String, String> messageMap = new HashMap<>();
            messageMap.put("SenderID", message.getSenderID());
            messageMap.put("message", message.getMessage());
            FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).child(message.getMessageID()).setValue(messageMap);
        }
        sendMsgEditText.setText(null);
    }

    public void getChatMessages(){
        FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = "";
                    String SenderID = "";

                    if(dataSnapshot.child("message").getValue() != null)
                        message = dataSnapshot.child("message").getValue().toString();
                    if(dataSnapshot.child("SenderID").getValue() != null)
                        SenderID = dataSnapshot.child("SenderID").getValue().toString();

                    Message msg = new Message(dataSnapshot.getKey(), SenderID, message);
                    messageArrayList.add(msg);
                    messageListLayoutManager.scrollToPosition(messageArrayList.size()-1);
                    messageListAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
