package com.royarijit998.whatsclone.Chats;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.royarijit998.whatsclone.R;

import java.util.ArrayList;

public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.MediaViewHolder> {

    private ArrayList<String> mediaURIArrayList;
    private Context context;

    public MediaListAdapter(Context context, ArrayList<String> mediaURIArrayList){
        this.context = context;
        this.mediaURIArrayList = mediaURIArrayList;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, null, false);
        MediaViewHolder mediaViewHolder =  new MediaViewHolder(layoutView);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        // We now use Glide to populate the ImageView
        Glide.with(context).load(Uri.parse(mediaURIArrayList.get(position))).into(holder.mediaImageView);

    }

    @Override
    public int getItemCount() {
        return mediaURIArrayList.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        private ImageView mediaImageView;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaImageView = itemView.findViewById(R.id.mediaImageView);
        }
    }
}
