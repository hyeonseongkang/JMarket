package com.mirror.jmarket.model;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mirror.jmarket.classes.Item;

import java.util.ArrayList;

public class ItemRepository {

    public static final String TAG = "ItemRepository";

    private Application application;

    private DatabaseReference myRef;

    private MutableLiveData<Boolean> itemSave;


    public ItemRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("items");
        itemSave = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getItemSave() { return itemSave; }


    // String id, String title, String price, String content, ArrayList<String> photoKeys, String key, String firstPhotoUri
    public void createItem(Item item) {
        String id = item.getId();
        String title = item.getTitle();
        String price = item.getPrice();
        String content = item.getContent();
        ArrayList<String> photoKeys = item.getPhotoKeys();
        String key = item.getKey();
        String firstPhotoUri = item.getFirstPhotoUri();

        if (photoKeys.size() == 0) {
            Toast.makeText(application, "이미지를 하나 이상 추가해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < photoKeys.size(); i++) {
            String photoKey = myRef.push().getKey();
            StorageReference storage = FirebaseStorage.getInstance().getReference().child("items/" + photoKey + ".jpg");
            UploadTask uploadTask = storage.putFile(Uri.parse(photoKeys.get(i)));

            int finalI = i;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (finalI == 0) {
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Item tempItem = new Item(id, title, price, content, photoKeys, key, uri.toString());
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
