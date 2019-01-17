package com.iinfotechsystemsonline.app.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserMDL extends RealmObject {
    @PrimaryKey
    public String id;
    public  String token;
    public  String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
