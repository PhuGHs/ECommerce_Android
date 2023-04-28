package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.ChatMessage;
import com.example.ecommerce_hvpp.repositories.ChatRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class ChatViewModel extends ViewModel {
    private ChatRepository repo;
    private MutableLiveData<ChatMessage> chatMessageMutableLiveData;

    public ChatViewModel() {
        repo = new ChatRepository();
        chatMessageMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Resource<List<ChatMessage>>> getChatMessagesLiveData() {
        return repo.getChatMessagesLiveData();
    }

    public void sendChatMessage(String message, String senderId, String receiverId) {
        ChatMessage chatMessage = new ChatMessage(message, senderId, receiverId);
        repo.sendMessage(chatMessage);
    }
}
