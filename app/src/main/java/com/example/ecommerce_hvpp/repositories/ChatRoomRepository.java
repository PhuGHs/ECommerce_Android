package com.example.ecommerce_hvpp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.ChatMessage;
import com.example.ecommerce_hvpp.model.ChatRoom;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
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

//    public List<String> getUsersInSpecificRoom(String roomId) {
//        List<String> idList = new ArrayList<>();
//        ref.child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot chatRoomSnapshot : snapshot.getChildren()) {
//                    String user1Id = chatRoomSnapshot.child("user1Id").getValue(String.class);
//                    String user2Id = chatRoomSnapshot.child("user2Id").getValue(String.class);
//                    idList.add(user1Id);
//                    idList.add(user2Id);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("ChatRoomRepository_getUsersInSpecificRoom: ", error.getMessage());
//            }
//        });
//        return idList;
//    }


    public void updateChatRoom(String chatRoomId) {
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
        _mldChatRoomList.setValue(Resource.loading(null));

        Query query1 = ref.orderByChild("user1Id").equalTo(currentUserId);
        Query query2 = ref.orderByChild("user2Id").equalTo(currentUserId);

        List<ChatRoom> chatRooms = new ArrayList<>();

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                    chatRooms.add(chatRoom);
                }
                // After query1 is completed, also execute query2
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                            chatRooms.add(chatRoom);
                        }
                        // After both queries are completed, handle the resulting chatRooms list
                        _mldChatRoomList.setValue(Resource.success(chatRooms));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        _mldChatRoomList.setValue(Resource.error(error.getMessage(), null));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _mldChatRoomList.setValue(Resource.error(error.getMessage(), null));
            }
        });

        return _mldChatRoomList;
    }


//    public void createNewChatRoom() {
//        String roomId = ref.push().getKey();
//        ChatRoom room2 = new ChatRoom(roomId, getCurrentUserUID(), "Lê Hoài Hải", "TAU MỚI ĐẾN", System.currentTimeMillis());
//        ref.child(roomId).setValue(room2);
//    }

    public String getCurrentUserUID() {
        return fbHelper.getAuth().getCurrentUser().getUid();
    }
}
