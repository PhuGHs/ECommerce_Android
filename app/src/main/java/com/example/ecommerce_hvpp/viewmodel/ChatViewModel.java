package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.ChatMessage;
import com.example.ecommerce_hvpp.repositories.ChatRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private ChatRepository repo;
    private MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData;

    public ChatViewModel() {
        repo = new ChatRepository();
        chatMessageMutableLiveData = new MutableLiveData<>();
        List<ChatMessage> defaultData = new ArrayList<>();
        defaultData.add(new ChatMessage("Hello!", "123", "234", ChatMessage.TYPE_INCOMING));
        defaultData.add(new ChatMessage("Hi there", "234", "123", ChatMessage.TYPE_OUTGOING));
        chatMessageMutableLiveData.setValue(defaultData);
    }

    public LiveData<Resource<List<ChatMessage>>> getChatMessagesLiveData() {
        return repo.getChatMessagesLiveData();
    }

    public void sendChatMessage(String message, String senderId, String receiverId, int type) {
        ChatMessage chatMessage = new ChatMessage(message, senderId, receiverId, type);
        repo.sendMessage(chatMessage);
    }
}
