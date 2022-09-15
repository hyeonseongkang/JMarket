package com.mirror.jmarket.model;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mirror.jmarket.classes.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    public static final String TAG = "ItemRepository";

    private Application application;

    private DatabaseReference myRef;

    private MutableLiveData<Boolean> itemSave;

    private MutableLiveData<List<Item>> items;

    private MutableLiveData<Item> item;

    private MutableLiveData<Boolean> like;

    private List<Item> tempItems;

    public ItemRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("items");
        itemSave = new MutableLiveData<>();
        items = new MutableLiveData<>();
        tempItems = new ArrayList<>();
        item = new MutableLiveData<>();
        like = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getItemSave() {
        return itemSave;
    }

    public MutableLiveData<List<Item>> getItems() {
        return items;
    }

    public MutableLiveData<Item> getItem() {
        return item;
    }

    public MutableLiveData<Boolean> getLike() { return like; }

    public void getItem(String key) {
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Item tempItem = snapshot.getValue(Item.class);
                item.setValue(tempItem);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getHomeItems() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                tempItems.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Item item = snapshot1.getValue(Item.class);
                    tempItems.add(item);
                }
                items.setValue(tempItems);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void setLike(String key, String uid) {
        // key = item
        // uid = 좋아요 누른 사람 uid

        //ArrayList<String> likes = myRef.child(key).child("likes");
        myRef.child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> likes = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    if (snapshot1.getValue() != null) {
                        Log.d(TAG, snapshot1.getValue().toString());
                        String userUid = snapshot1.getValue(String.class);
                        likes.add(userUid);
                    }
                }

                // 현재 아이템 좋아요 리스트에 uid가 없다면 추가하고 uid가 있다면 삭제 -> toggle
                if (!(likes.contains(uid))) {
                    likes.add(uid);
                    like.setValue(true);
                } else if (likes.contains(uid)) {
                    likes.remove(uid);
                    like.setValue(false);
                }

                myRef.child(key).child("likes").setValue(likes);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getLike(String key, String uid) {
        myRef.child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> likes = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    if (snapshot1.getValue() != null) {
                        String userUid = snapshot1.getValue(String.class);
                        likes.add(userUid);
                    }
                }

                // like list에 uid user가 있다면 true -> 빨간색 하트 표시
                if (likes.contains(uid))
                    like.setValue(true);
                else
                    like.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // String id, String title, String price, String content, ArrayList<String> photoKeys, String key, String firstPhotoUri
    public void createItem(Item item) {
        String id = item.getId();
        String title = item.getTitle();
        String price = item.getPrice();
        boolean priceOffer = item.isPriceOffer();
        String content = item.getContent();
        ArrayList<String> photoKeys = item.getPhotoKeys();
        String key = myRef.push().getKey();
        String firstPhotoUri = item.getFirstPhotoUri();
        String sellerProfileUri = item.getSellerProfileUri();
        String sellerName = item.getSellerName();
        ArrayList<String> likes = item.getLikes();

        if (photoKeys.size() == 0) {
            Toast.makeText(application, "이미지를 하나 이상 추가해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> tempPhotokeys = new ArrayList<>();
        for (int i = 0; i < photoKeys.size(); i++) {
            String photoKey = myRef.push().getKey();
            StorageReference storage = FirebaseStorage.getInstance().getReference().child("items/" + photoKey + ".jpg");
            UploadTask uploadTask = storage.putFile(Uri.parse(photoKeys.get(i)));
            int finalI = i;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            tempPhotokeys.add(uri.toString());

                            if (finalI == photoKeys.size() - 1) {
                                // String id, String title, String price, boolean priceOffer, String content, ArrayList<String> photoKeys, String key, String firstPhotoUri, String sellerProfileUri, String sellerName, String likes
                                Item tempItem = new Item(id, title, price, priceOffer, content, tempPhotokeys, key, tempPhotokeys.get(0), sellerProfileUri, sellerName, likes);
                               // Item item = new Item(id, title, price, priceOffer, content, tempPhotokeys, key, tempPhotokeys.get(0), sellerProfileUri, sellerName, likes);
                                myRef.child(key).setValue(tempItem);
                                itemSave.setValue(true);
                            }
                        }
                    });

                }
            });

        }





    }
}
