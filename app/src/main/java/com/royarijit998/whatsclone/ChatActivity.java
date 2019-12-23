package com.royarijit998.whatsclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.royarijit998.whatsclone.Chats.MediaListAdapter;
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
    private ArrayList<String> mediaIDList;

    private Button sendBtn;
    private Button addMediaBtn;
    private EditText sendMsgEditText;
    private String chatID, contactName;

    private int totalMediaUpload;

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
        initialiseMediaRecycleView();

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

    private void initialiseMediaRecycleView() {
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

    private void sendMessage(){
        sendMsgEditText = findViewById(R.id.sendMsgEditText);
        final HashMap<String, Object> messageMap = new HashMap<>();
        final Message message = new Message(FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).push().getKey(), FirebaseAuth.getInstance().getUid(), "You");
        messageMap.put("SenderID", message.getSenderID());

        if(!sendMsgEditText.getText().toString().isEmpty()) {
            message.setMessage(sendMsgEditText.getText().toString());
            messageMap.put("message", message.getMessage());
        }


        if(!mediaURIArrayList.isEmpty()){
            mediaIDList = new ArrayList<>();
            totalMediaUpload = 0;
            for (String mediaURI : mediaURIArrayList){
                String mediaID = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).child(message.getMessageID()).child("media").push().getKey();
                mediaIDList.add(mediaID);
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Chats").child(chatID).child(message.getMessageID()).child(mediaID);
                final UploadTask uploadTask = storageReference.putFile(Uri.parse(mediaURI));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                messageMap.put("/media/" + mediaIDList.get(totalMediaUpload) + "/", uri.toString());

                                totalMediaUpload++;
                                if(totalMediaUpload == mediaIDList.size()){
                                    updateDbWithMediaMessage(FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).child(message.getMessageID()), messageMap);
                                }
                            }
                        });
                    }
                });
            }
        }else if(!sendMsgEditText.getText().toString().isEmpty()){
            updateDbWithMessage(FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).child(message.getMessageID()), messageMap);
        }
    }

    private void updateDbWithMediaMessage(DatabaseReference dbRef, HashMap<String, Object> messageMap){
        dbRef.updateChildren(messageMap);
        sendMsgEditText.setText(null);
        mediaURIArrayList.clear();
        mediaIDList.clear();
        mediaListAdapter.notifyDataSetChanged();

    }

    private void updateDbWithMessage(DatabaseReference dbRef, HashMap<String, Object> messageMap){
        dbRef.setValue(messageMap);
        sendMsgEditText.setText(null);
        mediaURIArrayList.clear();
        mediaIDList.clear();
        mediaListAdapter.notifyDataSetChanged();

    }

    public void getChatMessages(){
        FirebaseDatabase.getInstance().getReference().child("Chats").child(chatID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = "";
                    String SenderID = "";

                    ArrayList<String> UrlArrayList = new ArrayList<>();

                    if(dataSnapshot.child("message").getValue() != null)
                        message = dataSnapshot.child("message").getValue().toString();
                    if(dataSnapshot.child("SenderID").getValue() != null)
                        SenderID = dataSnapshot.child("SenderID").getValue().toString();

                    if(dataSnapshot.child("media").getChildrenCount() > 0){
                        for(DataSnapshot childSnapshot : dataSnapshot.child("media").getChildren()){
                            UrlArrayList.add(childSnapshot.getValue().toString());
                        }
                    }

                    Message msg = new Message(dataSnapshot.getKey(), SenderID, contactName);
                    msg.setMessage(message);
                    msg.setUrlArrayList(UrlArrayList);
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
