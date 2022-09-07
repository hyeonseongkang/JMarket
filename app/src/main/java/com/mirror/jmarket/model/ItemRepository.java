package com.mirror.jmarket.model;

import android.app.Application;

public class ItemRepository {

    public static final String TAG = "ItemRepository";

    private Application application;



    public ItemRepository(Application application) {
        this.application = application;
    }
}
