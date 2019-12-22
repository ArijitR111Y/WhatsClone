package com.royarijit998.whatsclone.Chats;

public class Message {
    private String MessageID, SenderID, SenderName, message;

    public Message(String MessageID, String SenderID, String SenderName, String message){
        this.MessageID = MessageID;
        this.SenderID = SenderID;
        this.message = message;
        this.SenderName = SenderName;
    }

    public String getMessageID() {
        return MessageID;
    }

    public String getSenderID() {
        return SenderID;
    }

    public String getSenderName() {
        return SenderName;
    }

    public String getMessage() {
        return message;
    }
}
