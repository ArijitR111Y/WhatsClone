package com.royarijit998.whatsclone.Users;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.royarijit998.whatsclone.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    ArrayList<User> userArrayList;
    String userMobNumber;
    private static final String TAG = "UserListAdapter";

    public UserListAdapter(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null, false);
        UserViewHolder userViewHolder =  new UserViewHolder(layoutView);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        Query query = dbRef.child(firebaseAuth.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userMobNumber = dataSnapshot.child("phone").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return userViewHolder;
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        holder.nameTextView.setText(userArrayList.get(position).getName());
        holder.phoneTextView.setText(userArrayList.get(position).getPhoneNum());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

                User user = userArrayList.get(position);
                Log.i(TAG, user.getUID() + " " + user.getPhoneNum());
                Log.i(TAG, firebaseAuth.getUid() + " " + userMobNumber);

                dbRef.child(firebaseAuth.getUid())
                        .child("chats").child(key)
                        .setValue(user.getPhoneNum());
                dbRef.child(user.getUID())
                        .child("chats").child(key)
                        .setValue(userMobNumber);

            }
        });

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, phoneTextView;
        private LinearLayout itemLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }


}
