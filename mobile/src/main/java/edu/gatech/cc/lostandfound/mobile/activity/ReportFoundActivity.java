package edu.gatech.cc.lostandfound.mobile.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.gatech.cc.lostandfound.api.lostAndFound.model.FoundReport;
import edu.gatech.cc.lostandfound.mobile.R;
import edu.gatech.cc.lostandfound.mobile.camera.CameraHelper;
import edu.gatech.cc.lostandfound.mobile.entity.Position;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedFoundObject;
import edu.gatech.cc.lostandfound.mobile.network.Api;

/**
 * Created by guoweidong on 10/25/15.
 */
public class ReportFoundActivity extends AppCompatActivity {
    final static int REQUEST_OPEN_CAMERA_FOR_IMAGE = 100;
    final static int REQUEST_SELECT_PICTURE = 200;
    final static int REQUEST_POSITION = 300;
    Toolbar toolbar;
    EditText objectName;
    EditText description;
    ImageView objectImage;
    Button cameraBtn;
    Button selectBtn;
    EditText fromDate;
    EditText fromTime;
    EditText toDate;
    EditText toTime;
    EditText position;
    EditText detailedPosition;
    EditText howToGet;
    Button doneBtn;
    String imageName = null;
    ArrayList<Position> alPositions = new ArrayList<Position>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_found);
        //initiate toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Report Found");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportFoundActivity.this.onBackPressed();
            }
        });
        setupObject();
        setupImage();
        setupDateTime();
        setupPosition();
        setupHowToGet();
        final DateFormat format = new SimpleDateFormat("MM/d/y k:m", Locale
                .ENGLISH);
        doneBtn = (Button) findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportedFoundObject rfo = new ReportedFoundObject();
                rfo.objectName = objectName.getText().toString();
                rfo.description = description.getText().toString();
                rfo.image = objectImage.getBackground();
                rfo.alPostions = alPositions;
                rfo.detailedPosition = detailedPosition.getText().toString();
                try {
                    rfo.fromDate = format.parse(fromDate.getText().toString()
                            + " " + fromTime.getText().toString());
                    rfo.toDate = format.parse(toDate.getText().toString() + " "
                            + fromTime.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                rfo.timestamp = Calendar.getInstance().getTimeInMillis();
                rfo.reporter = getSharedPreferences("LostAndFound", 0)
                        .getString(Constants.PREF_ACCOUNT_NAME, null);
                rfo.howToGet = howToGet.getText().toString();

                final FoundReport report = new FoundReport();
                rfo.writeToReport(report);
                /**
                 * Use network to post data on server.
                 */
                new AsyncTask<FoundReport, Void, Void>() {
                    @Override
                    protected Void doInBackground(FoundReport...
                                                          params) {
                        try {
                            Api.getClient().foundReport().insert(report)
                                    .execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        ReportFoundActivity.this.finish();
                    }
                }.execute(report);

            }
        });


    }

    public void setupObject() {
        objectName = (EditText) findViewById(R.id.objectName);
        description = (EditText) findViewById(R.id.decription);
    }

    public void setupImage() {
        objectImage = (ImageView) findViewById(R.id.objectImage);
        cameraBtn = (Button) findViewById(R.id.cameraBtn);
        selectBtn = (Button) findViewById(R.id.selectBtn);
        CameraHelper.init(this);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = CameraHelper.openCameraForImage
                        (REQUEST_OPEN_CAMERA_FOR_IMAGE);
            }
        });
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select " +
                        "Picture"), REQUEST_SELECT_PICTURE);
            }
        });
    }

    public void setupDateTime() {
        fromDate = (EditText) findViewById(R.id.from_date);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(ReportFoundActivity.this, new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int
                                    monthOfYear, int dayOfMonth) {
                                fromDate.setText(monthOfYear + "/" +
                                        dayOfMonth + "/"
                                        + year);
                                fromDate.clearFocus();
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar
                        .MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fromTime = (EditText) findViewById(R.id.from_time);
        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(ReportFoundActivity.this, new
                        TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int
                                    hourOfDay, int
                                                          minute) {
                                fromTime.setText(hourOfDay + ":" + minute);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), Calendar
                        .MINUTE, true)
                        .show();
            }
        });
        toDate = (EditText) findViewById(R.id.to_date);
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(ReportFoundActivity.this, new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int
                                    monthOfYear, int dayOfMonth) {
                                toDate.setText(monthOfYear + "/" + dayOfMonth
                                        + "/" +
                                        year);
                                toDate.clearFocus();
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar
                        .MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        toTime = (EditText) findViewById(R.id.to_time);
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(ReportFoundActivity.this, new
                        TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int
                                    hourOfDay, int
                                                          minute) {
                                toTime.setText(hourOfDay + ":" + minute);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), Calendar
                        .MINUTE, true)
                        .show();
            }
        });
    }

    public void setupPosition() {
        position = (EditText) findViewById(R.id.position);
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportFoundActivity.this,
                        PinDropActivity.class);
                startActivityForResult(intent, REQUEST_POSITION);
            }
        });
        detailedPosition = (EditText) findViewById(R.id.detailedPosition);

    }

    public void setupHowToGet() {
        howToGet = (EditText) findViewById(R.id.howToGet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_CAMERA_FOR_IMAGE) {
            if (resultCode == RESULT_OK) {
                File imageFile = CameraHelper.getMediaFile(imageName);
                if (imageFile != null) {
                    Drawable drawable = BitmapDrawable.createFromPath
                            (imageFile.getPath());
                    objectImage.setBackground(drawable);
                }
            }
        } else if (requestCode == REQUEST_SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.i("myinfo", "data is empty");
                }
                try {
                    InputStream in = getContentResolver().openInputStream
                            (data.getData());
                    Drawable drawable = BitmapDrawable.createFromStream(in,
                            "image");
                    objectImage.setBackground(drawable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_POSITION) {
            if (resultCode == RESULT_OK) {
                position.setText("");
                alPositions = data.getParcelableArrayListExtra("alPositions");
                for (Position pos : alPositions) {
                    position.append(pos.address + ";");
                }
            }
        }
    }
}
