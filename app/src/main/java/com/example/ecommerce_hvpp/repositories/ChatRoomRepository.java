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

    public void createNewChatRoom() {
        String roomId = ref.push().getKey();
        ChatRoom room2 = new ChatRoom(roomId, getCurrentUserUID(), "lqPv1mxDkVe3O6dZCkzN28OgNKF2", "Vừa nãy t đến xong về", System.currentTimeMillis());
        ref.child(roomId).setValue(room2);
    }

    public String getCurrentUserUID() {
        return fbHelper.getAuth().getCurrentUser().getUid();
    }
}
