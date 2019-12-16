package com.royarijit998.whatsclone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.nameTextView.setText(userArrayList.get(position).getName());
        holder.phoneTextView.setText(userArrayList.get(position).getPhoneNum());
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, phoneTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
        }
    }


}
