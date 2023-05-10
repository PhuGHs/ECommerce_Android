package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.ChatRoom;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.ChatRoomRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class ChatRoomViewModel extends ViewModel {
    private ChatRoomRepository repo;

    public ChatRoomViewModel() {
        repo = new ChatRoomRepository();
    }
    public void updateChatRoom(String roomId) {
        repo.updateChatRoom(roomId);
    }
    public LiveData<Resource<User>> getUser(String UID) {
        return repo.getUser(UID);
    }

    public LiveData<Resource<List<ChatRoom>>> getChatRoomList() {
        return repo.getChatRoomList();
    }
    public String getCurrentUserUID() {
        return repo.getCurrentUserUID();
    }
}
