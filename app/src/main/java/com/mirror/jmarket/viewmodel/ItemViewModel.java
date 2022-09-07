package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.model.ItemRepository;

import org.jetbrains.annotations.NotNull;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repository;

    private MutableLiveData<Boolean> itemSave;

    public ItemViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        itemSave = repository.getItemSave();
    }

    public MutableLiveData<Boolean> getItemSave() { return itemSave; }

    public void createItem(Item item) {
        repository.createItem(item);
    }
}
