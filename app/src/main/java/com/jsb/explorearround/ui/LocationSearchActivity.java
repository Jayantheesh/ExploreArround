package com.jsb.explorearround.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.jsb.explorearround.Controller;
import com.jsb.explorearround.R;
import com.jsb.explorearround.location.LocationTracker;
import com.jsb.explorearround.parser.LocationResults;
import com.jsb.explorearround.parser.LocationResultsModel;

import java.util.ArrayList;

/**
 * Created by JSB on 12/5/15.
 */
public class LocationSearchActivity extends AppCompatActivity {

    private static final String TAG = "LocationSearchActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private ControllerResults mControllerCallback;

    private static LocationResultsModel mResults = null;

    public static void actionLocationSearchActivity(Activity fromActivity) {
        Intent i = new Intent(fromActivity, LocationSearchActivity.class);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search_card_view);

        // Attaching the layout to the toolbar object
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Initialise the Controller from here
        mControllerCallback = new ControllerResults();
        Controller.getsInstance(this).addResultCallback(mControllerCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Controller.getInstance().removeResultCallback(mControllerCallback);
    }

    private ArrayList<LocationSearchDataObject> getDataSet() {
        ArrayList<LocationSearchDataObject> results = new ArrayList<>();
        LocationResults[] res = mResults.getResults();
        for (int index = 0; index < res.length; index++) {
            results.add(index, new LocationSearchDataObject.DataBuilder(this)
                    .setName(res[index].getFormatted_address())
                    .build());
        }
        return results;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String locationText) {
                if (!TextUtils.isEmpty(locationText)) {
                    Controller.getInstance().getLocationDetails(LocationSearchActivity.this, locationText);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }


    /**
     * Controller result Callback.
     */
    private class ControllerResults extends Controller.Result {

        @Override
        public void getLocationDetailsCallback(final LocationResultsModel status, final String reason) {
            Log.d(TAG, "getLocationDetailsCallback");
            runOnUiThread(new Runnable() {
                public void run() {
                    if (status == null) {
                        Toast.makeText(LocationSearchActivity.this, reason, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int resultLen = status.getResults().length;
                    if (resultLen > 0) {
                        mResults = status;
                        mAdapter = new LocationSearchRVAdapter(getDataSet(), LocationSearchActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        ((LocationSearchRVAdapter) mAdapter).setOnItemClickListener(new LocationSearchRVAdapter
                                .MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                Toast.makeText(LocationSearchActivity.this, "Address=" + mResults.getResults()[position].getFormatted_address()
                                                + " Lat=" + mResults.getResults()[position].getGeometry().getLocation().getLatitude()
                                                + " Long=" + mResults.getResults()[position].getGeometry().getLocation().getLongtitude(),
                                        Toast.LENGTH_SHORT).show();

                                LocationTracker.getsInstance(LocationSearchActivity.this).updateLocationPref(
                                        mResults.getResults()[position].getFormatted_address(),
                                        mResults.getResults()[position].getGeometry().getLocation().getLatitude(),
                                        mResults.getResults()[position].getGeometry().getLocation().getLongtitude());
                                finish();

                            }
                        });
                    } else {
                        Toast.makeText(LocationSearchActivity.this, "No search results found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
