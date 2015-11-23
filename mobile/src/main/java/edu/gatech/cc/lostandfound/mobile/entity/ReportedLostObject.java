package edu.gatech.cc.lostandfound.mobile.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;

import edu.gatech.cc.lostandfound.api.lostAndFound.model.GeoPt;
import edu.gatech.cc.lostandfound.api.lostAndFound.model.LostReport;

/**
 * Created by guoweidong on 11/6/15.
 */
public class ReportedLostObject extends ReportedObject implements Parcelable {
    public static final Parcelable.Creator<ReportedLostObject> CREATOR = new
            Parcelable.Creator<ReportedLostObject>() {
                @Override
                public ReportedLostObject createFromParcel(Parcel source) {
                    return new ReportedLostObject(source);
                }

                @Override
                public ReportedLostObject[] newArray(int size) {
                    return new ReportedLostObject[size];
                }
            };
    public String owner;

    public ReportedLostObject() {

    }

    private ReportedLostObject(Parcel in) {
        objectName = in.readString();
        description = in.readString();
        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
        image = new BitmapDrawable(bitmap);
        in.readTypedList(alPostions, Position.CREATOR);
        detailedPosition = in.readString();
        fromDate = new Date(in.readLong());
        toDate = new Date(in.readLong());
        timestamp = in.readLong();
        owner = in.readParcelable(getClass().getClassLoader());
    }

    public ReportedLostObject(LostReport report) {
        objectName = report.getTitle();
        description = report.getDescription();
        detailedPosition = "Need to store that";
        fromDate = new Date(report.getTimeWindowBegin().getValue());
        toDate = new Date(report.getTimeWindowEnd().getValue());
        timestamp = report.getCreated().getValue();
        owner = report.getUserNickname();
        alPostions = new ArrayList<>(report.getLocations().size());
        for(GeoPt point : report.getLocations()) {
            alPostions.add(new Position(point.getLatitude(),point
                    .getLongitude()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectName);
        dest.writeString(description);
        Bitmap bitmap = (Bitmap) ((BitmapDrawable) image).getBitmap();
        dest.writeParcelable(bitmap, flags);
        dest.writeTypedList(alPostions);
        dest.writeString(detailedPosition);
        dest.writeLong(fromDate.getTime());
        dest.writeLong(toDate.getTime());
        dest.writeLong(timestamp);
        dest.writeString(owner);
    }

    public void writeToReport(LostReport report) {
        report.setTitle(objectName);
        report.setDescription(description);
        report.setTimeWindowBegin(new DateTime(fromDate));
        report.setTimeWindowEnd(new DateTime(toDate));
        ArrayList<GeoPt> points = new ArrayList<>(alPostions.size());
        for (Position location : alPostions) {
            GeoPt point = new GeoPt();
            point.setLatitude(location.lat);
            point.setLongitude(location.lng);
            points.add(point);
        }
        report.setLocations(points);
    }
}
