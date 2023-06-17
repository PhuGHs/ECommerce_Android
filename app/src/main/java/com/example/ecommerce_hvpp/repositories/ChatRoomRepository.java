package com.example.ecommerce_hvpp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.ChatMessage;
import com.example.ecommerce_hvpp.model.ChatRoom;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRoomRepository {
    private FirebaseHelper fbHelper;
    private MutableLiveData<Resource<List<ChatRoom>>> _mldChatRoomList;
    private MutableLiveData<Resource<List<User>>> _mldUser;
    private DatabaseReference ref;
    private DatabaseReference chatRef;
    private UserRepository userRepository;
    public ChatRoomRepository() {
        _mldChatRoomList = new MutableLiveData<>();
        _mldUser = new MutableLiveData<>();
        fbHelper = FirebaseHelper.getInstance();
        userRepository = new UserRepository();
        ref = fbHelper.getDatabaseReference("chatRooms");
        chatRef = fbHelper.getDatabaseReference("messages");
    }

    public LiveData<Resource<User>> getUser(String UID) {
        return userRepository.getUser(UID);
    }

    public void updateChatRoom(String chatRoomId, String recipientId) {
        chatRef.orderByChild("roomId").equalTo(chatRoomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatMessage lastMessage = null;
                for(DataSnapshot messageSnapShot : snapshot.getChildren()) {
                    ChatMessage message = messageSnapShot.getValue(ChatMessage.class);
                    if(lastMessage == null || message.getSendingTime() > lastMessage.getSendingTime() ) {
                        lastMessage = message;
                    }
                }
                if (lastMessage != null) {
                    Map<String, Object> updatedMessage = new HashMap<>();
                    updatedMessage.put("lastMessage", lastMessage.getMessageText());
                    updatedMessage.put("lastMessageTimeStamp", lastMessage.getSendingTime());
                    updatedMessage.put("recipientId", recipientId);
                    ref.child(chatRoomId).updateChildren(updatedMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<Resource<List<ChatRoom>>> getChatRoomList() {
        String currentUserId = getCurrentUserUID();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ChatRoom> result = new ArrayList<>();
                AtomicInteger expectedCallbacks = new AtomicInteger(0);
                AtomicInteger completedCallbacks = new AtomicInteger(0);

                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    String user1Id = datasnapshot.child("user1Id").getValue(String.class);
                    String user2Id = datasnapshot.child("user2Id").getValue(String.class);
                    if (user1Id.equals(currentUserId) || user2Id.equals(currentUserId)) {
                        ChatRoom room = datasnapshot.getValue(ChatRoom.class);
                        result.add(room);
                        expectedCallbacks.incrementAndGet();

                        if (currentUserId.equals(room.getUser1Id())) {
                            userRepository.getUserWith(room.getUser2Id(), new UserRepository.UserCallback() {
                                @Override
                                public void onSuccess(User user) {
                                    room.setRoomName(user.getUsername());
                                    room.setImagePath(user.getImagePath());
                                    if (completedCallbacks.incrementAndGet() == expectedCallbacks.get()) {
                                        // All callbacks completed, populate data and update LiveData
                                        populateDataAndNotify(result);
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    if (completedCallbacks.incrementAndGet() == expectedCallbacks.get()) {
                                        // All callbacks completed, populate data and update LiveData
                                        populateDataAndNotify(result);
                                    }
                                }
                            });
                        } else {
                            userRepository.getUserWith(room.getUser1Id(), new UserRepository.UserCallback() {
                                @Override
                                public void onSuccess(User user) {
                                    room.setRoomName(user.getUsername());
                                    room.setImagePath(user.getImagePath());
                                    if (completedCallbacks.incrementAndGet() == expectedCallbacks.get()) {
                                        // All callbacks completed, populate data and update LiveData
                                        populateDataAndNotify(result);
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    if (completedCallbacks.incrementAndGet() == expectedCallbacks.get()) {
                                        // All callbacks completed, populate data and update LiveData
                                        populateDataAndNotify(result);
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving chatroom data" + error.getMessage());
            }
        };

        ref.addValueEventListener(listener);
        return _mldChatRoomList;
    }

    private void populateDataAndNotify(List<ChatRoom> result) {
        _mldChatRoomList.setValue(Resource.success(result));
    }

    public LiveData<Resource<List<ChatRoom>>> checkIfHasRoomBefore() {
        String currentUserId = getCurrentUserUID();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ChatRoom> result = new ArrayList<>();
                for(DataSnapshot datasnapshot : snapshot.getChildren()) {
                    String user1Id = datasnapshot.child("user1Id").getValue(String.class);
                    String user2Id = datasnapshot.child("user2Id").getValue(String.class);
                    if(user1Id.equals(currentUserId) || user2Id.equals(currentUserId)) {
                        result.add(datasnapshot.getValue(ChatRoom.class));
                    }
                }
                _mldChatRoomList.setValue(Resource.success(result));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving chatroom data" + error.getMessage());
            }
        };
        ref.addValueEventListener(listener);

        return _mldChatRoomList;
    }


    public void updateAllChatRoom() {

    }

    public ChatRoom createNewChatRoom() {
        String roomId = ref.push().getKey();
        ChatRoom room2 = new ChatRoom(roomId, fbHelper.getAuth().getCurrentUser().getUid(), "03oJJtgjDlMjZkIQl65anPzEvm62", "", System.currentTimeMillis());
        userRepository.getUserWith(fbHelper.getAuth().getCurrentUser().getUid(), new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                room2.setRoomName(user.getUsername());
                room2.setImagePath(user.getImagePath());
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        ref.child(roomId).setValue(room2);
        return room2;
    }

    public String getCurrentUserUID() {
        return fbHelper.getAuth().getCurrentUser().getUid();
    }
}
