package com.royarijit998.whatsclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.royarijit998.whatsclone.Chats.Chat;
import com.royarijit998.whatsclone.Chats.ChatListAdapter;

import java.util.ArrayList;
import java.util.HashSet;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView chatsRecyclerView;
    private RecyclerView.Adapter chatsListAdapter;
    private RecyclerView.LayoutManager chatListLayoutManager;
    private ArrayList<Chat> chatArrayList;
    private HashSet<String> uniqueIds;

    private Button findUsersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findUsersBtn = findViewById(R.id.findUsersBtn);

        findUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FindUsersActivity.class));
            }
        });


        getPermissions();


        chatArrayList = new ArrayList<>();
        uniqueIds = new HashSet<>();
        getUserChat();

        chatsRecyclerView = findViewById(R.id.chatsRecyclerView);
        // For more seamless scrolling
        chatsRecyclerView.setNestedScrollingEnabled(false);
        chatsRecyclerView.setHasFixedSize(false);
        chatListLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
        chatsRecyclerView.setLayoutManager(chatListLayoutManager);
        chatsListAdapter = new ChatListAdapter(chatArrayList);
        chatsRecyclerView.setAdapter(chatsListAdapter);



    }

    public void getUserChat(){
        DatabaseReference userChatDB = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("chats");
        userChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        if(uniqueIds.contains(childSnapshot.getKey())){
                            continue;
                        }
                        String phone = childSnapshot.getValue().toString();
                        Chat chat = new Chat(childSnapshot.getKey(), phone, getContactName(phone, getApplicationContext()));
                        chatArrayList.add(chat);
                        uniqueIds.add(chat.getChatID());
                        chatsListAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getPermissions(){
        // As the concept of permissions was introduced from and after Android Mashmello
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
    }

    public void logoutFromApp(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        // To sign-out from all the activities (for any activity that was started and still running)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }

    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }
        return contactName;
    }
}
