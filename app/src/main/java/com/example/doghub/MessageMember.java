package com.example.doghub;

public class MessageMember {

    //declare string variables
    String message, time, date, type, senderUid;


    public void MessageMember() {
    }

    //getter for messages
    public String getMessage() {
        return message;
    }

    //setter for messages
    public void setMessage(String message) {
        this.message = message;
    }

    //getter for time
    public String getTime() {
        return time;
    }

    //setter for time
    public void setTime(String time) {
        this.time = time;
    }

    //getter for date
    public String getDate() {
        return date;
    }

    //setter for date
    public void setDate(String date) {
        this.date = date;
    }

    //getter for type
    public String getType() {
        return type;
    }

    //setter for type
    public void setType(String type) {
        this.type = type;
    }

    //getter for sender's user id
    public String getSenderUid() {
        return senderUid;
    }

    //setter for sender's user id
    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    //getter for receiver's user id
    public String getReceiverUid() {
        return receiverUid;
    }

    //setter for receiver's user id
    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    String receiverUid;


}
