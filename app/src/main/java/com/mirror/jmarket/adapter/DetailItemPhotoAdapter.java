package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;

import java.util.ArrayList;
import java.util.List;

public class DetailItemPhotoAdapter extends RecyclerView.Adapter<DetailItemPhotoAdapter.MyViewHolder> {

    private List<String> photoUris = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_photo_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(photoUris.get(position)))
                .into(holder.photo);
    }

    @Override
    public int getItemCount() { return photoUris == null ? 0 : photoUris.size(); }

    public void setPhotoUris(List<String> photoUris) {
        this.photoUris = photoUris;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
        }
    }
}