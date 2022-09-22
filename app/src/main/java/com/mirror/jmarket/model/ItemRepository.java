package com.mirror.jmarket.model;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mirror.jmarket.classes.CompleteUser;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.classes.Review;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    public static final String TAG = "ItemRepository";

    private Application application;

    private DatabaseReference myRef;
    private DatabaseReference completeRef;
    private DatabaseReference reviewsRef;

    private MutableLiveData<Boolean> itemSave;

    private MutableLiveData<List<Item>> items;

    private MutableLiveData<Item> item;

    private MutableLiveData<Boolean> like;

    private MutableLiveData<Boolean> complete;

    private List<Item> tempItems;

    private MutableLiveData<Boolean> reviewComplete;

    private MutableLiveData<List<Review>> reviews;
    private List<Review> reviewList;

    public ItemRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("items");
        completeRef = FirebaseDatabase.getInstance().getReference("completeItems");
        reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
        itemSave = new MutableLiveData<>();
        items = new MutableLiveData<>();
        tempItems = new ArrayList<>();
        item = new MutableLiveData<>();
        like = new MutableLiveData<>();
        complete = new MutableLiveData<>();
        reviewComplete = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
        reviewList = new ArrayList<>();
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

    public MutableLiveData<Boolean> getComplete() { return complete; }

    public MutableLiveData<Boolean> getReviewComplete() {
        return reviewComplete;
    }

    public MutableLiveData<List<Review>> getReviews() {
        return reviews;
    }

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

    // 거래 완료 요청
    public void setComplete(String myUid, String userUid, String itemKey, CompleteUser completeUser) {
        completeRef.child(myUid).child(userUid).child(itemKey).setValue(completeUser);
        completeRef.child(userUid).child(myUid).child(itemKey).setValue(completeUser);
    }

    // 상대가 거래 완료를 요청했는지 확인
    public void getComplete(String userUid, String myUid, String itemKey) {
        completeRef.child(userUid).child(myUid).child(itemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Log.d(TAG, snapshot.getValue().toString());
                    CompleteUser completeUser = snapshot.getValue(CompleteUser.class);

                    if (completeUser.getSender().length() > 0 && completeUser.getReceiver().length() > 0) {
                        complete.setValue(true);
                    } else if (!(completeUser.getSender().equals(myUid))) {
                        complete.setValue(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // review 작성
    public void setReview(String userUid, Review review) {

        reviewsRef.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean alreadyReview = false;
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Review tempReview = snapshot1.getValue(Review.class);
                    if (tempReview.getItem().getKey().equals(review.getItem().getKey())) {
                        reviewComplete.setValue(false);
                        alreadyReview = true;
                    }
                }

                if (!alreadyReview) {
                    reviewsRef.child(userUid).push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                reviewComplete.setValue(true);
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getReviews(String myUid) {
        reviewsRef.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                reviewList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Review review = snapshot1.getValue(Review.class);
                    reviewList.add(review);
                }
                reviews.setValue(reviewList);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
