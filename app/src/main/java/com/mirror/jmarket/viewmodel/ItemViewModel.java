package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.model.CompleteUser;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.Review;
import com.mirror.jmarket.repository.ItemRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repository;

    private LiveData<Boolean> itemSave;

    private LiveData<List<Item>> items;

    private LiveData<List<Item>> myInterestItems;
    private LiveData<List<Item>> myOnSalesItems;
    private LiveData<List<Item>> myCompleteSalesItems;

    private LiveData<List<Item>> myBuyItems;

    private LiveData<Item> item;

    private LiveData<Boolean> like;

    private LiveData<Boolean> complete;

    private LiveData<Boolean> reviewComplete;

    private LiveData<List<Review>> reviews;

    private LiveData<Boolean> deleteItemState;

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

//        itemSave.setValue(false);
//        complete.setValue(false);
//        reviewComplete.setValue(false);
//        deleteItemState.setValue(false);
    }

    public LiveData<Boolean> getItemSave() { return itemSave; }

    public LiveData<List<Item>> getItems() { return items; }

    public LiveData<List<Item>> getMyInterestItems() { return myInterestItems; }

    public LiveData<List<Item>> getMyOnSalesItems() { return myOnSalesItems; }

    public LiveData<List<Item>> getMyCompleteSalesItems() { return myCompleteSalesItems; }

    public LiveData<List<Item>> getMyBuyItems() { return myBuyItems; }

    public LiveData<Item> getItem() { return item; }

    public LiveData<Boolean> getLike() { return like; }

    public LiveData<Boolean> getComplete() { return complete; }

    public LiveData<Boolean> getReviewComplete() { return reviewComplete; }

    public LiveData<List<Review>> getReviews() { return reviews; }

    public LiveData<Boolean> getDeleteItemState() { return deleteItemState; }

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
