package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeItemPhotoAdapter extends RecyclerView.Adapter<HomeItemPhotoAdapter.MyViewHolder>{

    private List<String> photoUris = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_photo_item, parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(photoUris.get(position)))
                .into(holder.photo);
    }

    @Override
    public int getItemCount() { return photoUris == null ? 0 : photoUris.size();}

    public void setPhotoUris(List<String> photoUris) {
        this.photoUris = photoUris;
        notifyDataSetChanged();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private ImageView deletePhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            deletePhoto = itemView.findViewById(R.id.deletePhoto);

            deletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener ) {this.listener= listener;}
}
