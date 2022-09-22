package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.classes.CompleteUser;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.classes.Review;
import com.mirror.jmarket.model.ItemRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repository;

    private MutableLiveData<Boolean> itemSave;

    private MutableLiveData<List<Item>> items;

    private MutableLiveData<Item> item;

    private MutableLiveData<Boolean> like;

    private MutableLiveData<Boolean> complete;

    private MutableLiveData<Boolean> reviewComplete;

    public ItemViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        itemSave = repository.getItemSave();
        items = repository.getItems();
        item = repository.getItem();
        like = repository.getLike();
        complete = repository.getComplete();
        reviewComplete= repository.getReviewComplete();
    }

    public MutableLiveData<Boolean> getItemSave() { return itemSave; }

    public MutableLiveData<List<Item>> getItems() { return items; }

    public MutableLiveData<Item> getItem() { return item; }

    public MutableLiveData<Boolean> getLike() { return like; }

    public MutableLiveData<Boolean> getComplete() { return complete; }

    public MutableLiveData<Boolean> getReviewComplete() { return reviewComplete; }

    public void getHomeItems() { repository.getHomeItems(); }

    public void createItem(Item item) {
        repository.createItem(item);
    }

    public void getItem(String key) { repository.getItem(key); }

    public void setLike(String key, String uid) {
        repository.setLike(key, uid);
    }

    public void getLike(String key, String uid) {
        repository.getLike(key, uid);
    }

    public void setComplete(String myUid, String userUid, String itemKey, CompleteUser completeUser) { repository.setComplete(myUid, userUid, itemKey, completeUser); }

    public void getComplete(String userUid, String myUid, String itemKey) { repository.getComplete(userUid, myUid, itemKey); }

    public void setReview(String userUid, Review review) { repository.setReview(userUid, review);}
}
