package com.example.ecommerce_hvpp.model;

public class ChatMessage {
    public static final int TYPE_INCOMING = 0;
    public static final int TYPE_OUTGOING = 1;
    private int type;
    private String messageText;
    private String senderId;
    private String receiverId;

    public ChatMessage() {
    }

    public ChatMessage(String messageText, String senderId, String receiverId, int type) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
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
}
