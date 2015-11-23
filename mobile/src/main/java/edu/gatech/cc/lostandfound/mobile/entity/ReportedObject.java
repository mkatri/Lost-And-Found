package edu.gatech.cc.lostandfound.mobile.entity;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by guoweidong on 10/24/15.
 */
public class ReportedObject {
    public String objectName;
    public String description;
    public Drawable image;
    public ArrayList<Position> alPostions;
    public String detailedPosition;
    public Date fromDate;
    public Date toDate;
    public long timestamp;
}
