package com.example.ecommerce_hvpp.model;

public class Inbox {
    private String inboxId;
    private String recipientName;
    private String lastMessage;
    private long lastMessageTimeStamp;

    public Inbox(String inboxId, String recipientName, String lastMessage, long lastMessageTimeStamp) {
        this.inboxId = inboxId;
        this.recipientName = recipientName;
        this.lastMessage = lastMessage;
        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public String getInboxId() {
        return inboxId;
    }

    public void setInboxId(String inboxId) {
        this.inboxId = inboxId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String chatName) {
        this.recipientName = chatName;
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
}
