package com.royarijit998.whatsclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
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

    private RecyclerView mediaRecyclerView;
    private RecyclerView.Adapter mediaListAdapter;
    private RecyclerView.LayoutManager mediaListLayoutManager;

    private ArrayList<Message> messageArrayList;
    private ArrayList<String> mediaURIArrayList;

    private Button sendBtn;
    private Button addMediaBtn;
    private String chatID, contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");
        contactName = getIntent().getExtras().getString("contactName");

        messageArrayList = new ArrayList<>();
        mediaURIArrayList = new ArrayList<>();
        getChatMessages();

        initialiseMessageRecyclerView();
        intialiseMediaRecycleView();

        // -------Working with buttons-------
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        addMediaBtn = findViewById(R.id.addMediaBtn);
        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        // ----------------------------------
    }

    private void intialiseMediaRecycleView() {
        mediaRecyclerView = findViewById(R.id.mediaRecyclerView);
        // For more seamless scrolling
        mediaRecyclerView.setNestedScrollingEnabled(false);
        mediaRecyclerView.setHasFixedSize(false);
        mediaListLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mediaRecyclerView.setLayoutManager(mediaListLayoutManager);
        mediaListAdapter = new MediaListAdapter(getApplicationContext(), mediaURIArrayList);
        mediaRecyclerView.setAdapter(mediaListAdapter);
    }

    private void initialiseMessageRecyclerView() {
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        // For more seamless scrolling
        messageRecyclerView.setNestedScrollingEnabled(false);
        messageRecyclerView.setHasFixedSize(false);
        messageListLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        messageRecyclerView.setLayoutManager(messageListLayoutManager);
        messageListAdapter = new MessageListAdapter(messageArrayList);
        messageRecyclerView.setAdapter(messageListAdapter);
    }

    public void sendMessage(){
        EditText sendMsgEditText = findViewById(R.id.sendMsgEditText);
        if(!sendMsgEditText.getText().toString().isEmpty()){
            Message message = new Message(FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).push().getKey(), FirebaseAuth.getInstance().getUid(), "You", sendMsgEditText.getText().toString());
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

                    Message msg = new Message(dataSnapshot.getKey(), SenderID, contactName, message);
                    messageArrayList.add(msg);
                    messageListLayoutManager.scrollToPosition(messageArrayList.size()-1);
                    messageListAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    int PICK_IMAGE_INTENT = 1;

    private void openGallery() {
        Intent mediaIntent = new Intent();
        mediaIntent.setType("image/*");
        mediaIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        mediaIntent.setAction(mediaIntent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(mediaIntent, "Select a picture to send.."), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_IMAGE_INTENT){
                if(data.getClipData() == null){
                    mediaURIArrayList.add(data.getData().toString());
                }
                else{
                    for(int i=0; i<data.getClipData().getItemCount(); i++){
                        mediaURIArrayList.add(data.getClipData().getItemAt(i).toString());
                    }
                }
                mediaListAdapter.notifyDataSetChanged();
            }
        }
    }
}
