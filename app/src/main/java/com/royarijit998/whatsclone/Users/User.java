package com.royarijit998.whatsclone.Users;

public class User {

    String name, phoneNum, UID;

    public User(String UID, String name, String phoneNum) {
        this.UID = UID;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}
