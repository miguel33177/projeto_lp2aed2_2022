package edu.ufp.inf.Projeto.Classes;

import java.util.Date;

public class Object {

    private String username;
    private Integer userID;
    private Integer version;
    long timestamp = new Date().getTime();

    public Object(String username, Integer userID, Integer version, long timestamp) {
        this.username = username;
        this.userID = userID;
        this.version = version;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
