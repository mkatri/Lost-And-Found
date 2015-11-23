package edu.gatech.cc.lostandfound.api.model;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.repackaged.com.google.type.LatLng;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * Created by mkatri on 11/22/15.
 */

@Entity
public abstract class Report {

    @Id Long id;
    String title;
    String description;
    Date created;
    String userId;
    String userNickname;

    public Report() {
        created = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
