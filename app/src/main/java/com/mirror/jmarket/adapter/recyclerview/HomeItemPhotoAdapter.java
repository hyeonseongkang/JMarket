package com.mirror.jmarket.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.jmarket.BR;
import com.mirror.jmarket.data.LoadImage;
import com.mirror.jmarket.databinding.AdapterHomePhotoItemBinding;

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
        AdapterHomePhotoItemBinding binding = AdapterHomePhotoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        LoadImage loadImage = new LoadImage(photoUris.get(position));
        holder.bind(loadImage);
    }

    @Override
    public int getItemCount() { return photoUris == null ? 0 : photoUris.size();}

    public void setPhotoUris(List<String> photoUris) {
        this.photoUris = photoUris;
        notifyDataSetChanged();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterHomePhotoItemBinding binding;

        public MyViewHolder(AdapterHomePhotoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.deletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        void bind(LoadImage loadImage) {
            binding.setVariable(BR.loadImage, loadImage);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener ) {this.listener= listener;}
}
