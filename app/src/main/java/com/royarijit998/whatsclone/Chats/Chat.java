package com.royarijit998.whatsclone.Chats;

public class Chat {
    private String chatID;
    private String contactName;
    private String contactNumber;

    public Chat(String chatID, String contactNumber, String contactName) {
        this.chatID = chatID;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getChatID() {
        return chatID;
    }
}
