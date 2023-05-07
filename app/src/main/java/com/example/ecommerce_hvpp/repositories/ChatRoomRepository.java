package com.example.ecommerce_hvpp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.ChatRoom;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomRepository {
    private FirebaseHelper fbHelper;
    private MutableLiveData<Resource<List<ChatRoom>>> _mldChatRoomList;
    private DatabaseReference ref;
    public ChatRoomRepository() {
        _mldChatRoomList = new MutableLiveData<>();
        fbHelper = FirebaseHelper.getInstance();
        ref = fbHelper.getDatabaseReference("chatRooms");
    }

    public LiveData<Resource<List<ChatRoom>>> getChatRoomList() {
        _mldChatRoomList.setValue(Resource.loading(null));
        Query query = ref.orderByChild("senderId").equalTo(getCurrentUserUID());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ChatRoom> list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatRoom ib = dataSnapshot.getValue(ChatRoom.class);
                    list.add(ib);
                }
                _mldChatRoomList.setValue(Resource.success(list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _mldChatRoomList.setValue(Resource.error(error.getMessage(), null));
            }
        });
        return _mldChatRoomList;
    }

    public void createNewChatRoom() {
        String roomId = ref.push().getKey();
        ChatRoom room2 = new ChatRoom(roomId, getCurrentUserUID(), "Thủ tướng", "Bác đi đâu đấy", System.currentTimeMillis());
        ref.child(roomId).setValue(room2);
    }

    public String getCurrentUserUID() {
        return fbHelper.getAuth().getCurrentUser().getUid();
    }
}
