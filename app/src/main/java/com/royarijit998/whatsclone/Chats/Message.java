package com.royarijit998.whatsclone.Chats;

import java.util.ArrayList;

public class Message {
    private String MessageID, SenderID, SenderName, message;
    private ArrayList<String> UrlArrayList;

    public Message(String MessageID, String SenderID, String SenderName){
        this.MessageID = MessageID;
        this.SenderID = SenderID;
        this.SenderName = SenderName;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public ArrayList<String> getUrlArrayList() {
        return UrlArrayList;
    }

    public void setUrlArrayList(ArrayList<String> urlArrayList) {
        UrlArrayList = urlArrayList;
    }
}
