package com.mirror.jmarket.data;

import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class LoadImage {
   private String photoUri;

   public LoadImage() {}

   public LoadImage(String photoUri) {
      this.photoUri = photoUri;
   }

   public String getPhotoUri() {
      return photoUri;
   }

   @BindingAdapter("imageUri")
   public static void loadItemImage(ImageView imageView, String photoUri) {
      Glide.with(imageView.getContext())
              .load(Uri.parse(photoUri))
              .into(imageView);
   }
}
