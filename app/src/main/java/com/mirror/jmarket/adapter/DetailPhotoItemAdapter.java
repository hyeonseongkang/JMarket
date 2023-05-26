package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.BR;
import com.mirror.jmarket.R;
import com.mirror.jmarket.data.LoadImage;
import com.mirror.jmarket.databinding.AdapterDetailPhotoItemBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailPhotoItemAdapter extends RecyclerView.Adapter<DetailPhotoItemAdapter.MyViewHolder> {

    private List<String> photoUris = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDetailPhotoItemBinding binding = AdapterDetailPhotoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LoadImage loadImage = new LoadImage(photoUris.get(position));
        holder.bind(loadImage);
    }

    @Override
    public int getItemCount() { return photoUris == null ? 0 : photoUris.size(); }

    public void setPhotoUris(List<String> photoUris) {
        this.photoUris = photoUris;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterDetailPhotoItemBinding binding;

        public MyViewHolder(AdapterDetailPhotoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(LoadImage loadImage) {

            binding.setVariable(BR.loadImage, loadImage);
        }
    }
}