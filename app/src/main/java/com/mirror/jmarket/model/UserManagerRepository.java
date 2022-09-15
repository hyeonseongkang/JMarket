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

    private MutableLiveData<User> chatUserProfile;
    private MutableLiveData<User> chatMyProfile;

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
        myRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public MutableLiveData<User> getUserProfile() { return userProfile; }

    public MutableLiveData<List<User>> getUsersProfile() { return usersProfile; }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }


    public void updateUserProfile(User user) {
        String uid = user.getUid();
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
