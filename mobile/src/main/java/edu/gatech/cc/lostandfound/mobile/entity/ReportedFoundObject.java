package edu.gatech.cc.lostandfound.mobile.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;

import edu.gatech.cc.lostandfound.api.lostAndFound.model.FoundReport;
import edu.gatech.cc.lostandfound.api.lostAndFound.model.GeoPt;

/**
 * Created by guoweidong on 11/6/15.
 */
public class ReportedFoundObject extends ReportedObject implements Parcelable {
    public static final Creator<ReportedFoundObject> CREATOR = new
            Creator<ReportedFoundObject>() {
                @Override
                public ReportedFoundObject createFromParcel(Parcel source) {
                    return new ReportedFoundObject(source);
                }

                @Override
                public ReportedFoundObject[] newArray(int size) {
                    return new ReportedFoundObject[size];
                }
            };
    public String reporter;
    public String howToGet;

    private ReportedFoundObject(Parcel in) {
        objectName = in.readString();
        description = in.readString();
        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
        image = new BitmapDrawable(bitmap);
        in.readTypedList(alPostions, Position.CREATOR);
        detailedPosition = in.readString();
        fromDate = new Date(in.readLong());
        toDate = new Date(in.readLong());
        timestamp = in.readLong();
        reporter = in.readParcelable(getClass().getClassLoader());
        howToGet = in.readString();
    }

    public ReportedFoundObject() {

    }

    public ReportedFoundObject(FoundReport report) {
        objectName = report.getTitle();
        description = report.getDescription();
        fromDate = new Date(report.getTime().getValue());
        toDate = new Date(report.getTime().getValue());
        timestamp = report.getCreated().getValue();
        reporter = report.getUserNickname();
        alPostions = new ArrayList<>(1);
        alPostions.add(new Position(report.getLocation().getLatitude(),
                report.getLocation()
                        .getLongitude()));

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
        dest.writeString(reporter);
        dest.writeString(howToGet);
    }

    public void writeToReport(FoundReport report) {
        report.setTitle(objectName);
        report.setDescription(description);
        report.setTime(new DateTime(fromDate));
        GeoPt point = new GeoPt();
        point.setLatitude(alPostions.get(0).lat);
        point.setLongitude(alPostions.get(0).lng);
        report.setLocation(point);
    }
}
