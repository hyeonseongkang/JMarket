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
import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.MyViewHolder>{

    private List<Item> items = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Item item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.price.setText(item.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(items.get(position).getFirstPhotoUri()))
                .into(holder.photo);
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size();}

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        View rootView;
        private ImageView photo;
        private TextView title, content, price;

        public MyViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            price = itemView.findViewById(R.id.price);

            title.setSelected(true);
            content.setSelected(true);
            price.setSelected(true);

            itemView.setOnClickListener(new View.OnClickListener() {
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
