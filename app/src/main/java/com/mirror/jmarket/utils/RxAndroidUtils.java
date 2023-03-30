package com.mirror.jmarket.utils;

import android.widget.EditText;

import com.jakewharton.rxbinding4.widget.RxTextView;

import io.reactivex.rxjava3.core.Observable;

public class RxAndroidUtils {
    private static final String TAG = RxAndroidUtils.class.getSimpleName();
    private static RxAndroidUtils instance;

    private RxAndroidUtils() {}

    public static RxAndroidUtils getInstance() {
        if (instance == null) {
            instance = new RxAndroidUtils();
        }
        return instance;
    }

    public Observable<String> getEditTextObservable(EditText editText) {
        return RxTextView.textChanges(editText).map(charSequence -> charSequence.toString());
    }

    public String getTag() {
        return TAG;
    }


}
