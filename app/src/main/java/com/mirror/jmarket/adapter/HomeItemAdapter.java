package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;
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

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home_item, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, interest);

//        Glide.with(holder.itemView.getContext())
//                .load(Uri.parse(items.get(position).getFirstPhotoUri()))
//                .into(holder.photo);


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

    @BindingAdapter("bind:item")
    public static void bindItem(RecyclerView recyclerView, ObservableArrayList<Item> items) {
        HomeItemAdapter adapter = (HomeItemAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            if (items != null) {
                adapter.setItems(items, false);
            }
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterHomeItemBinding binding;
        private ImageView photo;
        private ImageView interest;
        private TextView title, content, price;

        public MyViewHolder(AdapterHomeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            photo = itemView.findViewById(R.id.photo);
            interest = itemView.findViewById(R.id.interest);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            price = itemView.findViewById(R.id.price);

            title.setSelected(true);
            content.setSelected(true);
            price.setSelected(true);

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
