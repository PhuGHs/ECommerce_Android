package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.ChatRoom;
import com.example.ecommerce_hvpp.repositories.ChatRoomRepository;
import com.example.ecommerce_hvpp.repositories.UserRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class ChatRoomViewModel extends ViewModel {
    private ChatRoomRepository repo;
    private UserRepository repoUser;

    public ChatRoomViewModel() {
        repo = new ChatRoomRepository();
    }

    public LiveData<Resource<List<ChatRoom>>> getChatRoomList() {
        return repo.getChatRoomList();
    }
}
