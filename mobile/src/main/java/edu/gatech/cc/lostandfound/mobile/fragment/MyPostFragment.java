package edu.gatech.cc.lostandfound.mobile.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import edu.gatech.cc.lostandfound.mobile.R;
import edu.gatech.cc.lostandfound.mobile.activity.MainActivity;
import edu.gatech.cc.lostandfound.mobile.adapter.MyPostRecyclerViewAdapter;
import edu.gatech.cc.lostandfound.mobile.entity.ReportedObject;


public class MyPostFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    private MyPostRecyclerViewAdapter rvAdapter;
    private ArrayList<ReportedObject> alRO = new ArrayList<ReportedObject>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rvAdapter = new MyPostRecyclerViewAdapter(new ArrayList<ReportedObject>(), (MainActivity) getActivity());
        mRecyclerView.setAdapter(rvAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InitializeObjectsTask().execute();
            }
        });

        new InitializeObjectsTask().execute();

        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        return view;
    }

    private class InitializeObjectsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            rvAdapter.clearObjects();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            alRO.clear();

            //TODO download all my reports
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            //handle visibility
            super.onPostExecute(param);

            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

            //set data for list
            rvAdapter.addObjects(alRO);
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }
}
