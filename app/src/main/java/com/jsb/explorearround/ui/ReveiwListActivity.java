package com.jsb.explorearround.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.jsb.explorearround.R;
import com.jsb.explorearround.parser.Result;
import com.jsb.explorearround.parser.Reviews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReveiwListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Result mResults = null;
    private static String TAG="ReviewListActivity";
    private Toolbar toolbar;

    public static void actionLaunchReviewActivity(Activity fromActivity, Result res) {
        //FIXME - Need to use Bundle here
        mResults = res;
        Intent i = new Intent(fromActivity, ReveiwListActivity.class);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reveiw_details);

        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.review_list_id);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReveiwAdapter(getDataSet(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<ReviewDataObject> getDataSet() {
        ArrayList<ReviewDataObject> results = new ArrayList<>();
        Reviews[] res = mResults.getReviews();

        for (int index = 0; index < res.length; index++) {
            results.add(index, new ReviewDataObject.DataBuilder(this)
                .setmRatingTime(getTime(res[index].getTime()))
                .setmRatingText(res[index].getRating())
                .setmAspectRatingId(getRatingId(res[index].getAspects()[0].getRating()))
                .setmSeparator(":")
                .setmAspectType(res[index].getAspects()[0].getType())
                .setmImageUrl(res[index].getProfile_photo_url())
                .setmAuthorName(res[index].getAuthor_name())
                .setmReviewText(res[index].getText())
                .build());
        }
        return results;
    }

    private String getRatingId(String rating) {
        int rat = Integer.valueOf(rating);
        String ret = null;
        switch (rat) {
            case 0:
                ret = "Poor";
                break;
            case 1:
                ret = "Average";
                break;
            case 2:
                ret = "Good";
                break;
            case 3:
                ret = "Excellent";
                break;
            default:
                ret = "Poor";
        }
        return ret;
    }

    private String getTime(String time) {
        Date updatedate = new Date(Long.valueOf(time) * 1000);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return outputFormat.format(updatedate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        ((ReveiwAdapter) mAdapter).setOnItemClickListener(new ReveiwAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
};