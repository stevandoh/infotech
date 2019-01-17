package com.iinfotechsystemsonline.app.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AgeGroupMDL extends RealmObject {
    @PrimaryKey
    public int id;
    public int minage ;
    public  int maxage;
    public  String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinage() {
        return minage;
    }

    public void setMinage(int minage) {
        this.minage = minage;
    }

    public int getMaxage() {
        return maxage;
    }

    public void setMaxage(int maxage) {
        this.maxage = maxage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
