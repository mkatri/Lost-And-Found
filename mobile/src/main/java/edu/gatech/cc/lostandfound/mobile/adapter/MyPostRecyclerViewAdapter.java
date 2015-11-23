package edu.gatech.cc.lostandfound.mobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.gatech.cc.lostandfound.mobile.R;
import edu.gatech.cc.lostandfound.mobile.activity.MainActivity;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedFoundObject;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedLostObject;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedObject;
import edu.gatech.cc.lostandfound.mobile.time.TimeManager;

/**
 * Created by guoweidong on 10/24/15.
 */
public class MyPostRecyclerViewAdapter extends RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder> {

    private List<ReportedObject> alRO;
    private MainActivity mAct;

    public MyPostRecyclerViewAdapter(List<ReportedObject> objectList, MainActivity act) {
        this.alRO = objectList;
        this.mAct = act;
    }


    public void clearObjects() {
        int size = this.alRO.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                alRO.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addObjects(List<ReportedObject> applications) {
        this.alRO.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {


        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_my_posts, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ReportedObject obj = alRO.get(i);

        viewHolder.objectImage.setBackground(obj.image);
//        viewHolder.username.setText(obj.owner.username);
        viewHolder.timestamp.setText(TimeManager.getTimeDifferential(obj.timestamp));

        if (obj instanceof ReportedLostObject) {
            viewHolder.objectName.setText("Lost: " + obj.objectName);
            viewHolder.username.setText(((ReportedLostObject) obj).owner);
            viewHolder.searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (obj instanceof ReportedFoundObject) {
            viewHolder.objectName.setText("Found: " + obj.objectName);
            viewHolder.username.setText(((ReportedFoundObject) obj).reporter);
            viewHolder.searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAct.gotoDetailActivity(obj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alRO == null ? 0 : alRO.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView objectImage;
        public TextView objectName;
        public TextView username;
        public TextView timestamp;
        public Button searchBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            objectImage = (ImageView) itemView.findViewById(R.id.objectImage);
            objectName = (TextView) itemView.findViewById(R.id.objectName);
            username = (TextView) itemView.findViewById(R.id.username);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);

            searchBtn = (Button) itemView.findViewById(R.id.searchBtn);

        }

    }
}
