package com.example.ecommerce_hvpp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.model.ChatMessage;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private final MutableLiveData<Resource<List<ChatMessage>>> chatMessagesLiveData;

    public ChatRepository() {
        chatMessagesLiveData = new MutableLiveData<>();
    }

    public LiveData<Resource<List<ChatMessage>>> getChatMessagesLiveData() {
        chatMessagesLiveData.setValue(Resource.loading(null));
        FirebaseDatabase.getInstance().getReference("chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ChatMessage> chatMessages = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            ChatMessage chatMessage = child.getValue(ChatMessage.class);
                            chatMessages.add(chatMessage);
                        }
                        chatMessagesLiveData.setValue(Resource.success(chatMessages));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        chatMessagesLiveData.setValue(Resource.error(error.getMessage(), null));
                    }
                });
        return chatMessagesLiveData;
    }

    public void sendMessage(ChatMessage chatMessage) {
        FirebaseDatabase.getInstance().getReference("chat")
                .push()
                .setValue(chatMessage);
    }
}
