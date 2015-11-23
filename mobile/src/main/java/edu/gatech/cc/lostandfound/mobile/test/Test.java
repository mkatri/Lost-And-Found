package edu.gatech.cc.lostandfound.mobile.test;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.gatech.cc.lostandfound.mobile.R;
import edu.gatech.cc.lostandfound.mobile.entity.Position;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedFoundObject;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedLostObject;

/**
 * Created by guoweidong on 11/7/15.
 */
public class Test {
    public static void loadReportedLostObjects(Activity activity) {
        ReportedLostObject rlo = new ReportedLostObject();
        rlo.objectName = "Gerogia Tech Buzzcard";
        rlo.description = "A Gatech Buzzcard with photo on it. I appreciate whoever found it and sent it back.";
        rlo.image = activity.getDrawable(R.drawable.test_buzzcard);
        ArrayList<Position> alPositions = new ArrayList<Position>();
        Position position = new Position();
        position.address = "266 4th St NW Atlanta, GA 30313";
        alPositions.add(position);
        position = new Position();
        position.address = "349 Ferst Dr NW Atlant, GA 30318";
        alPositions.add(position);
        position = new Position();
        position.address = "Cherry St NW Atlant, GA 30313";
        alPositions.add(position);
        rlo.alPostions = alPositions;
        rlo.detailedPosition = "On the Toilet of Second Floor";
//        rlo.fromDate = new Date(); "10/7/2015";
//        rlo.fromTime = "14:12";
//        rlo.toDate = "10/7/2015";
//        rlo.toTime = "15:30";
        rlo.timestamp = Calendar.getInstance().getTimeInMillis();
//        rlo.owner = UserManager.curUser;
        //Uploader.uploadRLO(rlo);


        rlo = new ReportedLostObject();
        rlo.objectName = "Discover Credit Card";
        rlo.description = "A Discover Credit, it's very important. I appreciate whoever found it and sent it back.";
        rlo.image = activity.getDrawable(R.drawable.test_discover_card);
        alPositions = new ArrayList<Position>();
        position = new Position();
        position.address = "266 4th St NW Atlanta, GA 30313";
        alPositions.add(position);
        rlo.alPostions = alPositions;
        rlo.detailedPosition = "On the dining room of Second Floor";
//        rlo.fromDate = "10/7/2015";
//        rlo.fromTime = "14:12";
//        rlo.toDate = "10/7/2015";
//        rlo.toTime = "15:40";
        rlo.timestamp = Calendar.getInstance().getTimeInMillis();
//        rlo.owner = UserManager.curUser;
        //Uploader.uploadRLO(rlo);

        rlo = new ReportedLostObject();
        rlo.objectName = "Fitbit Surge Bandwrist";
        rlo.description = "A Fitbit Surge Bandwrist, It's blue. I appreciate whoever found it and sent it back.";
        rlo.image = activity.getDrawable(R.drawable.test_fitbit_surge_bandwrist);
        alPositions = new ArrayList<Position>();
        position = new Position();
        position.address = "266 4th St NW Atlanta, GA 30313";
        alPositions.add(position);
        position = new Position();
        position.address = "Cherry St NW Atlant, GA 30313";
        alPositions.add(position);
        rlo.alPostions = alPositions;
//        rlo.fromDate = "10/7/2015";
//        rlo.fromTime = "14:12";
//        rlo.toDate = "10/7/2015";
//        rlo.toTime = "15:30";
        rlo.timestamp = Calendar.getInstance().getTimeInMillis();
//        rlo.owner = UserManager.curUser;
        //Uploader.uploadRLO(rlo);

        rlo = new ReportedLostObject();
        rlo.objectName = "iPhone 6s White";
        rlo.description = "I lost the phone I just bought. I appreciate whoever found it and sent it back.";
        rlo.image = activity.getDrawable(R.drawable.test_iphone6s);
        alPositions = new ArrayList<Position>();
        position = new Position();
        position.address = "349 Ferst Dr NW Atlant, GA 30318";
        alPositions.add(position);
        position = new Position();
        position.address = "Cherry St NW Atlant, GA 30313";
        alPositions.add(position);
        rlo.alPostions = alPositions;
        rlo.detailedPosition = "On the Toilet of Second Floor";
//        rlo.fromDate = "10/7/2015";
//        rlo.fromTime = "14:12";
//        rlo.toDate = "10/7/2015";
//        rlo.toTime = "15:30";
        rlo.timestamp = Calendar.getInstance().getTimeInMillis();
//        rlo.owner = UserManager.curUser;
        //Uploader.uploadRLO(rlo);
    }

    public static void loadReportedFoundObjects(Activity activity) {
        ReportedFoundObject rfo = new ReportedFoundObject();
        rfo.objectName = "Gerogia Tech Buzzcard";
        rfo.description = "A Gatech Buzzcard with photo on it. Owner must be upset now, I have put it on the security.";
        rfo.image = activity.getDrawable(R.drawable.test_buzzcard);
        ArrayList<Position> alPositions = new ArrayList<Position>();
        Position position = new Position();
        position.address = "266 4th St NW Atlanta, GA 30313";
        alPositions.add(position);
        rfo.alPostions = alPositions;
        rfo.detailedPosition = "On the Toilet of Second Floor";
//        rfo.fromDate = "10/7/2015";
//        rfo.fromTime = "14:12";
//        rfo.toDate = "10/7/2015";
//        rfo.toTime = "15:30";
        rfo.timestamp = Calendar.getInstance().getTimeInMillis();
//        rfo.reporter = UserManager.curUser;
        rfo.howToGet = "On the security of Library";
        //Uploader.uploadRFO(rfo);


        rfo = new ReportedFoundObject();
        rfo.objectName = "LV handbag";
        rfo.description = "A LV handbag for women. Very expensive I think.";
        rfo.image = activity.getDrawable(R.drawable.test_discover_card);
        alPositions = new ArrayList<Position>();
        position = new Position();
        position.address = "266 4th St NW Atlanta, GA 30313";
        alPositions.add(position);
        rfo.alPostions = alPositions;
        rfo.detailedPosition = "On the dining room of Second Floor";
//        rfo.fromDate = "10/7/2015";
//        rfo.fromTime = "14:12";
//        rfo.toDate = "10/7/2015";
//        rfo.toTime = "15:40";
        rfo.timestamp = Calendar.getInstance().getTimeInMillis();
//        rfo.reporter = UserManager.curUser;
        rfo.howToGet = "On the security of Library";
        //Uploader.uploadRFO(rfo);

    }
}
