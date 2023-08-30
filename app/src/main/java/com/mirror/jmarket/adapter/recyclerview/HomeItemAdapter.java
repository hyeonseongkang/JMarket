package com.mirror.jmarket.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.jmarket.databinding.AdapterHomeItemBinding;
import com.mirror.jmarket.model.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.MyViewHolder> {

    private List<Item> items = new ArrayList<>();
    private onItemClickListener listener;

    private boolean interest;

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        AdapterHomeItemBinding binding = AdapterHomeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, interest);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<Item> items, boolean interest) {
        if (items != null) {
            this.items = items;
            this.interest = interest;
            notifyDataSetChanged();

        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterHomeItemBinding binding;

        public MyViewHolder(AdapterHomeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items.get(position));
                    }
                }
            });
        }

        void bind(Item item, boolean interest) {
            binding.interest.setVisibility(View.GONE);

            if (interest == true) {
                binding.interest.setVisibility(View.VISIBLE);
            }

            binding.setVariable(BR.item, item);

        }
    }

    public interface onItemClickListener {
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
