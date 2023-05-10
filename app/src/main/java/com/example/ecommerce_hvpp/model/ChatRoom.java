package com.example.ecommerce_hvpp.model;

public class ChatRoom {
    private String id;
    private String user1Id;
    private String user2Id;
    private String lastMessage;
    private String roomName;
    private long lastMessageTimeStamp;

    public ChatRoom(String id, String user1Id,String user2Id, String lastMessage, long lastMessageTimeStamp) {
        this.id = id;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
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
    public String getId() {
        return id;
    }

    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
