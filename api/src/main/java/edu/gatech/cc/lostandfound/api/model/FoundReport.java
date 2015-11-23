package edu.gatech.cc.lostandfound.api.model;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;

import java.util.Date;

/**
 * Created by mkatri on 11/22/15.
 */
@Entity
public class FoundReport extends Report {
    Date time;
    Blob image;
    GeoPt location;

    public FoundReport() {
        super();
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public GeoPt getLocation() {
        return location;
    }

    public void setLocation(GeoPt location) {
        this.location = location;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
