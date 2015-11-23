package edu.gatech.cc.lostandfound.api.model;


import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;

import java.util.Date;
import java.util.List;

/**
 * Created by mkatri on 11/22/15.
 */

@Entity
public class LostReport extends Report {
    Date timeWindowBegin;
    Date timeWindowEnd;
    String reporter;
    List<GeoPt> locations;

    public LostReport() {
        super();
    }

    public Date getTimeWindowBegin() {
        return timeWindowBegin;
    }

    public void setTimeWindowBegin(Date timeWindowBegin) {
        this.timeWindowBegin = timeWindowBegin;
    }

    public Date getTimeWindowEnd() {
        return timeWindowEnd;
    }

    public void setTimeWindowEnd(Date timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public List<GeoPt> getLocations() {
        return locations;
    }

    public void setLocations(List<GeoPt> locations) {
        this.locations = locations;
    }
}
