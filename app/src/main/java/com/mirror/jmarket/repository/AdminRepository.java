package com.mirror.jmarket.repository;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.view.activity.AdminActivity;

import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class AdminRepository {

    private static final String TAG = AdminRepository.class.getSimpleName();

    private static AdminRepository instance;
    private Application application;

    private DatabaseReference myRef;

    private MutableLiveData<List<User>> usersProfile;


    public static synchronized AdminRepository getInstance(Application application) {
        if (instance == null) {
            instance = new AdminRepository(application);
        }
        return instance;
    }

    private AdminRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("users");

        usersProfile = new MutableLiveData<>();
    }

    private ValueEventListener createValueEventListener(final PublishSubject<DataSnapshot> subject) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subject.onNext(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                subject.onError(databaseError.toException());
            }
        };
    }

    public Observable<DataSnapshot> observeValueEvent(final DatabaseReference ref) {
        final PublishSubject<DataSnapshot> subject = PublishSubject.create();

        final ValueEventListener listener = createValueEventListener(subject);
        ref.addValueEventListener(listener);

        return subject.doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                ref.removeEventListener(listener);
            }
        });
    }

    public void getUsers() {

        observeValueEvent(myRef)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) throws Exception {
                        // 데이터를 가져왔을 때 처리할 로직 작성
                        Log.d(TAG, dataSnapshot.getValue().toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 데이터 가져오기에 실패했을 때 처리할 로직 작성
                    }
                });
    }


    public MutableLiveData<List<User>> getUsersProfile() {
        return usersProfile;
    }

}
