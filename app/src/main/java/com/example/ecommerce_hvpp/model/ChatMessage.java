package com.example.ecommerce_hvpp.model;

public class ChatMessage {
    public static final int TYPE_INCOMING = 0;
    public static final int TYPE_OUTGOING = 1;
    private int type;
    private String messageText;
    private String senderId;
    private String receiverId;
    private String chatId;
    private String roomId;
    private long sendingTime;

    public ChatMessage() {
    }

    public ChatMessage(String roomId, String chatId, String senderId, String receiverId, String messageText, long sendingTime) {;
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.chatId = chatId;
        this.roomId = roomId;
        this.sendingTime = sendingTime;
    }


    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(long sendingTime) {
        this.sendingTime = sendingTime;
    }
}
