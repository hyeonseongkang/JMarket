package com.mirror.jmarket.model;

import android.app.Application;
import android.net.Uri;

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
import com.mirror.jmarket.classes.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserManagerRepository {

    public static final String TAG = "UserManagerRepository";

    public Application application;

    private MutableLiveData<User> userProfile;
    private MutableLiveData<User> otherUserProfile;

    private MutableLiveData<List<User>> usersProfile;
    private List<User> users;

    private MutableLiveData<Boolean> updateValid;


    // Firebase Database
    private DatabaseReference myRef;

    public UserManagerRepository(Application application) {
        this.application = application;
        userProfile = new MutableLiveData<>();
        usersProfile = new MutableLiveData<>();
        users = new ArrayList<>();
        updateValid = new MutableLiveData<>();
        otherUserProfile = new MutableLiveData<>();
        myRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public MutableLiveData<User> getUserProfile() { return userProfile; }

    public MutableLiveData<User> getOtherUserProfile() { return otherUserProfile;}

    public MutableLiveData<List<User>> getUsersProfile() { return usersProfile; }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }


    // user profile 업데이트
    public void updateUserProfile(User user) {
        String uid = user.getUid();
        /*
        1. 인자값으로 넘어온 user의 getPhotoUri를 통해 Firebase Store에 사진을 먼저 저장한다.
        2. 사진 저장이 완료 됐다면 저장된 사진의 uri를 다운받은 뒤 해당 uri 값을 인자값으로 넘어온 user객체의 photoUri에 할당한다.
        3. users Ref 아래 자신의 uid 아래에 2번까지 완료한 user 객체를 저장한다.
        4. 값이 성공적으로 업데이트 됐다면 updateValid에 true를 할당하여 update가 완료됐음을 view에서 확인한다.
         */
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("profiles/" + uid + ".jpg");
        UploadTask uploadTask = storage.putFile(Uri.parse(user.getPhotoUri()));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.setPhotoUri(uri.toString());
                        myRef.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateValid.setValue(true);
                                } else {
                                    updateValid.setValue(false);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    // users Ref 아래에 인자값으로 넘어온 uid의 User 정보를 가져옴 (내 userProfile)
    public void getUserProfile(String uid) {
        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userProfile.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // users Ref 아래에 인자값으로 넘어온 uid의 User 정보를 가져옴 (상대 userProfile)
    public void getOtherUserProfile(String uid) {
        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUserProfile.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // users Ref 아래 있는 모든 User 정보를 가져옴
    public void getUsersProfile(List<String> uids) {
        users.clear();
        for (String uid: uids) {
            myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        usersProfile.setValue(users);
    }

}
