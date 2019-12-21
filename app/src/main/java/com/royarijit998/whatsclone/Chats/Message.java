package com.royarijit998.whatsclone.Chats;

public class Message {
    private String MessageID, SenderID, message;

    Message(String MessageID, String SenderID, String message){
        this.MessageID = MessageID;
        this.SenderID = SenderID;
        this.message = message;
    }

    public String getMessageID() {
        return MessageID;
    }

    public String getSenderID() {
        return SenderID;
    }

    public String getMessage() {
        return message;
    }
}
