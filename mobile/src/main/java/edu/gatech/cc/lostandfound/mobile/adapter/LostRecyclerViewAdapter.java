package edu.gatech.cc.lostandfound.mobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import edu.gatech.cc.lostandfound.mobile.R;
import edu.gatech.cc.lostandfound.mobile.activity.MainActivity;
import edu.gatech.cc.lostandfound.mobile.entity.Position;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedLostObject;
import edu.gatech.cc.lostandfound.mobile.time.TimeManager;

/**
 * Created by guoweidong on 10/24/15.
 */
public class LostRecyclerViewAdapter extends RecyclerView.Adapter<LostRecyclerViewAdapter.ViewHolder> {

    private List<ReportedLostObject> alRLO;
    private int rowLayout;
    private MainActivity mAct;

    public LostRecyclerViewAdapter(List<ReportedLostObject> objectList, int rowLayout, MainActivity act) {
        this.alRLO = objectList;
        this.rowLayout = rowLayout;
        this.mAct = act;
    }


    public void clearObjects() {
        int size = this.alRLO.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                alRLO.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addObjects(List<ReportedLostObject> applications) {
        this.alRLO.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final ReportedLostObject obj = alRLO.get(i);
        viewHolder.objectImage.setBackground(obj.image);
        viewHolder.objectName.setText(obj.objectName);
        viewHolder.username.setText(obj.owner);
        viewHolder.timestamp.setText(TimeManager.getTimeDifferential(obj.timestamp));
        viewHolder.expandBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewHolder.expandableLayout.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.expandableLayout.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.objectDescription.setText(Html.fromHtml("<b>Description: </b>"));
        viewHolder.objectDescription.append("\n" + obj.description);

        viewHolder.position.setText(Html.fromHtml("<b>Possible Positions: </b>"));
        for (Position pos : obj.alPostions) {
            viewHolder.position.append("\n" + pos.address + ";");
        }

        viewHolder.detailedPosition.setText(Html.fromHtml("<b>Detailed Position: </b>"));
        viewHolder.detailedPosition.append("\n" + obj.detailedPosition);

        viewHolder.timeRange.setText(Html.fromHtml("<b>From: </b>" + obj
                .fromDate + "\n" + "<b>To: </b>" + obj.toDate));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAct.gotoDetailActivity(obj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alRLO == null ? 0 : alRLO.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView objectImage;
        public TextView objectName;
        public TextView username;
        public TextView timestamp;
        public ToggleButton expandBtn;

        public LinearLayout expandableLayout;
        public TextView objectDescription;
        public TextView position;
        public TextView detailedPosition;
        public TextView timeRange;


        public ViewHolder(View itemView) {
            super(itemView);
            objectImage = (ImageView) itemView.findViewById(R.id.objectImage);
            objectName = (TextView) itemView.findViewById(R.id.objectName);
            username = (TextView) itemView.findViewById(R.id.username);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            expandBtn = (ToggleButton) itemView.findViewById(R.id.expandBtn);

            expandableLayout = (LinearLayout) itemView.findViewById(R.id.expandableLayout);
            objectDescription = (TextView) itemView.findViewById(R.id.objectDescription);
            position = (TextView) itemView.findViewById(R.id.position);
            detailedPosition = (TextView) itemView.findViewById(R.id.detailedPosition);
            timeRange = (TextView) itemView.findViewById(R.id.timeRange);
        }

    }
}
