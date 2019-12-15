package com.royarijit998.whatsclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class FindUsersActivity extends AppCompatActivity {

    private RecyclerView userList;
    private RecyclerView.Adapter userListAdapter;
    private RecyclerView.LayoutManager userListLayoutManager;
    private ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        userArrayList = new ArrayList<>();

        userList = findViewById(R.id.userList);
        // For more seamless scrolling
        userList.setNestedScrollingEnabled(false);
        userList.setHasFixedSize(false);
        userListLayoutManager = new LinearLayoutManager(FindUsersActivity.this, LinearLayoutManager.VERTICAL, false);
        userList.setLayoutManager(userListLayoutManager);
        userListAdapter = new UserListAdapter(userArrayList);
        userList.setAdapter(userListAdapter);

    }
}
