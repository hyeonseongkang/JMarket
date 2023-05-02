package com.mirror.jmarket.repository;

import android.app.Application;

public class AdminRepository {

    private static AdminRepository instance;

    private Application application;

    public static synchronized AdminRepository getInstance(Application application) {
        if (instance == null) {
            instance = new AdminRepository(application);
        }
        return instance;
    }
    private AdminRepository(Application application) {
        this.application = application;
    }
}
