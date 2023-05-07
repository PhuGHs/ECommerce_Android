package com.example.ecommerce_hvpp.model;

public class ChatRoom {
    private String id;
    private String recipientId;
    private String senderId;
    private String lastMessage;
    private long lastMessageTimeStamp;

    public ChatRoom(String id, String senderId, String recipientName, String lastMessage, long lastMessageTimeStamp) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientName;
        this.lastMessage = lastMessage;
        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public ChatRoom() {

    }

    public String getChatRoomId() {
        return id;
    }

    public void setChatRoomId(String inboxId) {
        this.id = inboxId;
    }

    public String getRecipientName() {
        return recipientId;
    }

    public void setRecipientName(String chatName) {
        this.recipientId = chatName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTimeStamp() {
        return lastMessageTimeStamp;
    }

    public void setLastMessageTimeStamp(long lastMessageTimeStamp) {
        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
