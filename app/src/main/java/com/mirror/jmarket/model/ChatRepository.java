package com.mirror.jmarket.model;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.User;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatRepository {
    public static final String TAG = "ChatRepository";

    private Application application;

    private DatabaseReference chatsRef;
    private DatabaseReference usersRef;

    private List<ChatRoom> chatRooms;
    private List<Chat> chats;

    private MutableLiveData<List<String>> chatUsers;
    private ArrayList<String> users;

    private MutableLiveData<List<User>> getUsersProfile;
    private ArrayList<User> myChatUsers;

    public ChatRepository(Application application) {
        this.application = application;
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        chatRooms = new ArrayList<>();
        chats = new ArrayList<>();
        chatUsers = new MutableLiveData<>();
        users = new ArrayList<>();

        getUsersProfile = new MutableLiveData<>();
        myChatUsers = new ArrayList<>();
    }

    public MutableLiveData<List<String>> getChatUsers() {return chatUsers; }

    public MutableLiveData<List<User>> getUsersProfile() { return getUsersProfile; }


    public void getChatRoom(String uid) {
        chatsRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    users.add(snapshot1.getKey());
                }
                chatUsers.setValue(users);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getMyChatUsers(String uid) {
        ArrayList<String> userUids = new ArrayList<>();

        chatsRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userUids.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    userUids.add(snapshot1.getKey());
                    Log.d("uid", snapshot1.getKey());
                }


                /*
                myChatUsers.clear();
                for (int i = 0; i < userUids.size(); i++) {
                    Log.d("TASK", "1");
                    GetMyChatUsersTask task = new GetMyChatUsersTask();
                    task.execute(userUids.get(i));
                }
                getUsersProfile.setValue(myChatUsers);
                 */

                myChatUsers.clear();
                for (int i = 0; i < userUids.size(); i++) {
                    usersRef.child(userUids.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            myChatUsers.add(user);
                            if (myChatUsers.size() == userUids.size()) {
                                getUsersProfile.setValue(myChatUsers);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void setChatRoom(String uid, String sellerUid) {

        if (uid.equals(sellerUid))
            return;

        chatsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatRooms.clear();
                chats.clear();
                boolean alreadyUser = false;
                Log.d(TAG, String.valueOf(alreadyUser));
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    chats.add(chat);
                    if (snapshot1.getKey().equals(sellerUid)) {
                        alreadyUser = true;
                    }
                }
                Log.d(TAG, String.valueOf(alreadyUser));
                if (!alreadyUser) {
                    Log.d(TAG, "Hello!");
                    String date = getDate();
                    chatsRef.child(uid).child(sellerUid).child("createDate").setValue(date);
                    chatsRef.child(sellerUid).child(uid).child("createDate").setValue(date);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void sendMessage(String sender, String receiver, Chat chat) {
        chatsRef.child(sender).child(receiver).child("chats").push().setValue(chat);
        chatsRef.child(receiver).child(sender).child("chats").push().setValue(chat);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = now.format(formatter);
        return date;
    }

    private class GetMyChatUsersTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... uid) {
            Log.d("TASK", "2");
            usersRef.child(uid[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User tempUser = snapshot.getValue(User.class);
                    Log.d("TASK", tempUser.getEmail());
                    myChatUsers.add(tempUser);
                }


                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void val) {
            super.onPostExecute(val);
            Log.d("TASK", "END");
        }
    }
}
