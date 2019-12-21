package com.royarijit998.whatsclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.royarijit998.whatsclone.Users.User;
import com.royarijit998.whatsclone.Users.UserListAdapter;

import java.util.ArrayList;
import java.util.HashSet;

public class FindUsersActivity extends AppCompatActivity {

    private RecyclerView userList;
    private RecyclerView.Adapter userListAdapter;
    private RecyclerView.LayoutManager userListLayoutManager;
    private ArrayList<User> userArrayList;
    private String ISO;
    private HashSet<String> uniqueContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        userArrayList = new ArrayList<>();
        uniqueContacts = new HashSet<>();

        userList = findViewById(R.id.userList);
        // For more seamless scrolling
        userList.setNestedScrollingEnabled(false);
        userList.setHasFixedSize(false);
        userListLayoutManager = new LinearLayoutManager(FindUsersActivity.this, LinearLayoutManager.VERTICAL, false);
        userList.setLayoutManager(userListLayoutManager);
        userListAdapter = new UserListAdapter(userArrayList);
        userList.setAdapter(userListAdapter);

        getUsersFromContacts();

    }

    public void getUsersFromContacts(){
        ISO = "+91";
        Cursor contact = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(contact.moveToNext()){
            String contactName = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactPhoneNum = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contactPhoneNum = contactPhoneNum.replace(" ", "");
            contactPhoneNum = contactPhoneNum.replace("-", "");
            contactPhoneNum = contactPhoneNum.replace("(", "");
            contactPhoneNum = contactPhoneNum.replace(")", "");

            if(contactPhoneNum.charAt(0) != '+')
                    contactPhoneNum = ISO + contactPhoneNum;

            // To prevent multiple entries to be displayed
            User user = new User("", contactName, contactPhoneNum);
            authContact(user);
        }
    }

    public void authContact(final User user){
        String phoneNum = user.getPhoneNum();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("phone").equalTo(phoneNum);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    user.setUID(dataSnapshot.getKey());
                    if (uniqueContacts.contains(user.getPhoneNum()))
                        return;
                    uniqueContacts.add(user.getPhoneNum());
                    userArrayList.add(user);
                    userListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
