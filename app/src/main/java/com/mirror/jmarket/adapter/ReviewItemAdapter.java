package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.BR;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityReviewBinding;
import com.mirror.jmarket.databinding.AdapterChatListItemBinding;
import com.mirror.jmarket.databinding.AdapterReivewItemBinding;
import com.mirror.jmarket.model.ChatRoom;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.Review;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.MyViewHolder> {

    private List<Review> reviews = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        AdapterReivewItemBinding binding = AdapterReivewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() { return reviews == null ? 0 : reviews.size(); }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterReivewItemBinding binding;

        public MyViewHolder(AdapterReivewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Review review) {
            binding.setVariable(BR.review, review);
        }
    }

}
