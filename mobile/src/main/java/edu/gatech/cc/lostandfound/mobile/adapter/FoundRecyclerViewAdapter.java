package edu.gatech.cc.lostandfound.mobile.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.gatech.cc.lostandfound.api.lostAndFound.model.FoundReport;
import edu.gatech.cc.lostandfound.mobile.R;
import edu.gatech.cc.lostandfound.mobile.activity.DetailFoundActivity;
import edu.gatech.cc.lostandfound.mobile.fragment.FoundFragment;
import edu.gatech.cc.lostandfound.mobile.time.TimeManager;
import edu.gatech.cc.lostandfound.mobile.utility.ImageConvertor;

/**
 * Created by guoweidong on 10/24/15.
 */
public class FoundRecyclerViewAdapter extends RecyclerView.Adapter<FoundRecyclerViewAdapter.ViewHolder> {

    private List<FoundReport> reports;
    private int rowLayout;
    private FoundFragment fragment;

    public FoundRecyclerViewAdapter(List<FoundReport> reports, int rowLayout, FoundFragment fragment) {
        this.reports = reports;
        this.rowLayout = rowLayout;
        this.fragment = fragment;
    }


    public void clearObjects() {
        int size = this.reports.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                reports.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addObjects(List<FoundReport> reports) {
        this.reports.addAll(reports);
        this.notifyItemRangeInserted(0, reports.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final FoundReport report = reports.get(i);
        viewHolder.objectImage.setBackground(report.getImage() != null ? ImageConvertor.stringToDrawable(report.getImage(), true) : fragment.getActivity().getDrawable(R.drawable.img_no_image_found));
        viewHolder.title.setText(report.getTitle());
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(fragment.getActivity(), DetailFoundActivity.class);
                intent.putExtra("reportId", report.getId());
                fragment.getActivity().startActivity(intent);
            }
        });
        viewHolder.nickname.setText(report.getUserNickname());
        viewHolder.timestamp.setText(TimeManager.getTimeDifferential(report.getCreated().getValue()));
        viewHolder.emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", report.getUserNickname() + "@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                fragment.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        if(report.getReturned()) {
            viewHolder.returned.setText("Returned");
            viewHolder.returned.setTextColor(Color.GREEN);
        } else {
            viewHolder.returned.setText("Not Returned");
            viewHolder.returned.setTextColor(Color.RED);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return reports == null ? 0 : reports.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView objectImage;
        public TextView title;
        public TextView nickname;
        public TextView timestamp;
        public ImageView emailBtn;
        public TextView returned;

        public ViewHolder(View itemView) {
            super(itemView);
            objectImage = (ImageView) itemView.findViewById(R.id.objectImage);
            title = (TextView) itemView.findViewById(R.id.title);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            emailBtn = (ImageView) itemView.findViewById(R.id.emailBtn);
            returned = (TextView) itemView.findViewById(R.id.returned);
        }

    }


}
