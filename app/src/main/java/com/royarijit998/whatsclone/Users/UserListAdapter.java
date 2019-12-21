package com.royarijit998.whatsclone.Users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.royarijit998.whatsclone.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    ArrayList<User> userArrayList;

    public UserListAdapter(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null, false);
        UserViewHolder userViewHolder =  new UserViewHolder(layoutView);
        return userViewHolder;
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {
        holder.nameTextView.setText(userArrayList.get(position).getName());
        holder.phoneTextView.setText(userArrayList.get(position).getPhoneNum());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("chats").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Users").child(userArrayList.get(position).getUID()).child("chats").child(key).setValue(true);
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
