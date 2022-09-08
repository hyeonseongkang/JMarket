package com.mirror.jmarket.model;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
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

    private List<Item> tempItems;


    public ItemRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("items");
        itemSave = new MutableLiveData<>();
        items = new MutableLiveData<>();
        tempItems = new ArrayList<>();
        item = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getItemSave() { return itemSave; }

    public MutableLiveData<List<Item>> getItems() { return items; }

    public MutableLiveData<Item> getItem() { return item; }

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
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Item item = snapshot1.getValue(Item.class);
                    Log.d(TAG, item.getId());
                    tempItems.add(item);
                }
                items.setValue(tempItems);
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

        if (photoKeys.size() == 0) {
            Toast.makeText(application, "이미지를 하나 이상 추가해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> tempPhotokeys = new ArrayList<>();
        for (int i = 0; i < photoKeys.size(); i++) {
            String photoKey = myRef.push().getKey();
            tempPhotokeys.add(photoKey);
        }

        for (int i = 0; i < tempPhotokeys.size(); i++) {
            StorageReference storage = FirebaseStorage.getInstance().getReference().child("items/" + tempPhotokeys.get(i) + ".jpg");
            UploadTask uploadTask = storage.putFile(Uri.parse(photoKeys.get(i)));

            int finalI = i;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (finalI == 0) {
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Item tempItem = new Item(id, title, price, priceOffer, content, tempPhotokeys, key, uri.toString());
                                myRef.child(key).setValue(tempItem);
                                itemSave.setValue(true);
                            }
                        });
                    }
                }
            });
        }


    }
}