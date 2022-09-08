package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;

import java.util.List;

class DetailItemPhotoAdapter extends RecyclerView.Adapter<DetailItemPhotoAdapter.MyViewHolder>{

    private List<String> dataList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        private ImageView photoImage;

        public MyViewHolder(View v) {
            super(v);

            photoImage = (ImageView) v.findViewById(R.id.photoImage);
            rootView = v;
        }
    }

    public DetailItemPhotoAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.home_photo_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(holder.rootView.getContext())
                .load(Uri.parse(dataList.get(position)))
                .into(holder.photoImage);
    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}
