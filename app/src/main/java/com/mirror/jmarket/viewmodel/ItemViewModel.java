package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.model.CompleteUser;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.Review;
import com.mirror.jmarket.repository.ItemRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repository;

    private MutableLiveData<Boolean> itemSave;

    private MutableLiveData<List<Item>> items;

    private MutableLiveData<List<Item>> myInterestItems;
    private MutableLiveData<List<Item>> myOnSalesItems;
    private MutableLiveData<List<Item>> myCompleteSalesItems;

    private MutableLiveData<List<Item>> myBuyItems;

    private MutableLiveData<Item> item;

    private MutableLiveData<Boolean> like;

    private MutableLiveData<Boolean> complete;

    private MutableLiveData<Boolean> reviewComplete;

    private MutableLiveData<List<Review>> reviews;

    private MutableLiveData<Boolean> deleteItemState;

    public ItemViewModel(@NonNull @NotNull Application application) {
        super(application);
        //repository = new ItemRepository(application);
        repository = ItemRepository.getInstance(application);
        itemSave = repository.getItemSave();
        items = repository.getItems();
        myInterestItems = repository.getMyInterestItems();
        myOnSalesItems = repository.getMyOnSalesItems();
        myCompleteSalesItems = repository.getMyCompleteSalesItems();
        myBuyItems = repository.getMyBuyItems();
        item = repository.getItem();
        like = repository.getLike();
        complete = repository.getComplete();
        reviewComplete= repository.getReviewComplete();
        reviews = repository.getReviews();
        deleteItemState = repository.getDeleteItemState();
    }

    public MutableLiveData<Boolean> getItemSave() { return itemSave; }

    public MutableLiveData<List<Item>> getItems() { return items; }

    public MutableLiveData<List<Item>> getMyInterestItems() { return myInterestItems; }

    public MutableLiveData<List<Item>> getMyOnSalesItems() { return myOnSalesItems; }

    public MutableLiveData<List<Item>> getMyCompleteSalesItems() { return myCompleteSalesItems; }

    public MutableLiveData<List<Item>> getMyBuyItems() { return myBuyItems; }

    public MutableLiveData<Item> getItem() { return item; }

    public MutableLiveData<Boolean> getLike() { return like; }

    public MutableLiveData<Boolean> getComplete() { return complete; }

    public MutableLiveData<Boolean> getReviewComplete() { return reviewComplete; }

    public MutableLiveData<List<Review>> getReviews() { return reviews; }

    public MutableLiveData<Boolean> getDeleteItemState() { return deleteItemState; }

    public void getHomeItems(String findText) { repository.getHomeItems(findText); }

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

    public void setReview(String myUid, String userUid, Review review) { repository.setReview(myUid, userUid, review);}

    public void getReviews(String myUid, String state) { repository.getReviews(myUid, state);}

    public void getMyInterestItems(String myUid) { repository.getMyInterestItems(myUid); }

    public void getMyOnSalesItems(String myUid) { repository.getMyOnSalesItems(myUid); }

    public void getMyCompleteSalesItems(String myUid) { repository.getMyCompleteSalesItems(myUid); }

    public void getMyBuyItems(String myUid) { repository.getMyBuyItems(myUid); }

    public void deleteItem(String itemKey) { repository.deleteItem(itemKey); }
}
