package com.example.doghub;

public class EventMember {


    //declaring variables
    String name, url, userid, key, event, privacy, time;

    //default constructor
    public EventMember() {
    }


    //getters and setters for the declared variables

    //getName returns name of user
    public String getName() {
        return name;
    }


    //setName changes name of user
    public void setName(String name) {
        this.name = name;
    }


    //gets the URL for the profile image of the user
    public String getUrl() {
        return url;
    }

    //allows you to set a different URL, hence changing pictures
    public void setUrl(String url) {
        this.url = url;
    }

    //allows you to retrieve current user's id
    public String getUserid() {
        return userid;
    }

    //allows you to set the user's id
    public void setUserid(String userid) {
        this.userid = userid;
    }

    //gets the key
    public String getKey() {
        return key;
    }


    //sets key
    public void setKey(String key) {
        this.key = key;
    }


    //gets the event
    public String getEvent() {
        return event;
    }

    //sets the event
    public void setEvent(String event) {
        this.event = event;
    }

    //gets privacy
    public String getPrivacy() {
        return privacy;
    }

    //sets privacy
    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    //get's time
    public String getTime() {
        return time;
    }

    //sets time
    public void setTime(String time) {
        this.time = time;
    }


}
