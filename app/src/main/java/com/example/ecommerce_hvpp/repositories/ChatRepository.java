package com.example.ecommerce_hvpp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.ChatMessage;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatRepository {
    private final MutableLiveData<Resource<List<ChatMessage>>> chatMessagesLiveData;
    private MutableLiveData<Resource<ChatMessage>> lastChatMessageLiveData;
    private FirebaseHelper fbHelper;
    private DatabaseReference ref;
    private static final String TAG = "ChatRepository";

    public ChatRepository() {
        chatMessagesLiveData = new MutableLiveData<>();
        lastChatMessageLiveData = new MutableLiveData<>();
        fbHelper = FirebaseHelper.getInstance();
        ref = fbHelper.getDatabaseReference("messages");
    }

    public LiveData<Resource<List<ChatMessage>>> getChatMessagesLiveData(String roomId) {
        chatMessagesLiveData.setValue(Resource.loading(null));
        Query query = ref.orderByChild("roomId").equalTo(roomId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ChatMessage> chatMessages = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            ChatMessage chatMessage = child.getValue(ChatMessage.class);
                            if(Objects.equals(chatMessage.getSenderId(), fbHelper.getAuth().getCurrentUser().getUid())) {
                                chatMessage.setType(ChatMessage.TYPE_OUTGOING);
                            } else {
                                chatMessage.setType(ChatMessage.TYPE_INCOMING);
                            }
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

    public ChatMessage sendMessage(String roomId, String senderId, String receiverId, long sendingTime, String message) {
        String chatId = ref.push().getKey();
        ChatMessage ms = new ChatMessage(roomId, chatId, senderId, receiverId,message, sendingTime);
        if(chatId == null) {
            Log.i(TAG, "chatid null");
            return null;
        }
        ref.child(chatId).setValue(ms);
        return ms;
    }


}
