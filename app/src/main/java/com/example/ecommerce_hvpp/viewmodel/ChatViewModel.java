package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.ChatMessage;
import com.example.ecommerce_hvpp.repositories.ChatRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class ChatViewModel extends ViewModel {
    private ChatRepository repo;

    public ChatViewModel() {
        repo = new ChatRepository();
    }

    public LiveData<Resource<List<ChatMessage>>> getChatMessagesLiveData(String roomId) {
        return repo.getChatMessagesLiveData(roomId);
    }

    public ChatMessage sendChatMessage(String roomId, String senderId, String receiverId, long sendingTime, String message) {
        return repo.sendMessage(roomId, senderId, receiverId, sendingTime, message);
    }
}
