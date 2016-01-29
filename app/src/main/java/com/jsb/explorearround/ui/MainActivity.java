package com.jsb.explorearround.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsb.explorearround.Controller;
import com.jsb.explorearround.R;
import com.jsb.explorearround.location.LocationTracker;
import com.jsb.explorearround.parser.Model;
import com.jsb.explorearround.utils.PreferencesHelper;

public class MainActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MainActivity";

    private static final String RADIUS = "5000";

    private ControllerResults mControllerCallback;
    /**
     * Id to identify a camera permission request.
     */
    private static final int REQUEST_LOCATION = 0;

    /**
     * Id to identify a contacts permission request.
     */
    private static final int REQUEST_PHONE = 1;

    private TextView currentLocation;
    private ImageView locationImage;
    private LinearLayout locationLayout;

    public static String[] prgmDisplayNameList = {
            "Airport",
            "ATM",
            "Bar",
            "Beauty Salon",
            "Bus Station",
            "Casino",
            "Car parking",
            "Cafe",
            "Car Wash",
            "Church",
            "Grocery",
            "Gym",
            "Gas station",
            "Hospital",
            "Hindu temple",
            "Lodge",
            "Lawyer",
            "Laundry",
            "Library",
            "Movie theatre",
            "Mosque",
            "Post office",
            "Pharmacy",
            "Parks",
            "Restaurant",
            "Railway Station",
            "RV Parks"
    };
    public static String[] prgmNameList = {
            "airport",
            "atm",
            "bar",
            "beauty_salon",
            "bus_station",
            "casino",
            "parking",
            "cafe",
            "car_wash",
            "church",
            "grocery_or_supermarket",
            "gym",
            "gas_station",
            "hospital",
            "hindu_temple",
            "lodging",
            "lawyer",
            "laundry",
            "library",
            "movie_theatre",
            "mosque",
            "post_office",
            "pharmacy",
            "parks",
            "restaurant",
            "train_station",
            "rv_park"
    };
    public static String[] prgmKeyword = {
            "airport",
            "ATM",
            "wine",
            "beauty|spa",
            "bus",
            "casino",
            "parking",
            "coffee",
            "car|clean",
            "church",
            "",
            "gym",
            "gas|motor",
            "hospital",
            "hindu_temple",
            "lodging",
            "lawyer",
            "laundry",
            "library",
            "cinemas",
            "mosque",
            "post office",
            "pharmacy",
            "parks",
            "restaurant",
            "train",
            "rv"
    };

    public static int[] prgmImages = {
            R.drawable.airport,
            R.drawable.atm,
            R.drawable.bar,
            R.drawable.beauty,
            R.drawable.bus_station,
            R.drawable.casino,
            R.drawable.parking,
            R.drawable.cafe,
            R.drawable.car_wash,
            R.drawable.church,
            R.drawable.groceries,
            R.drawable.gym,
            R.drawable.gas,
            R.drawable.hospital,
            R.drawable.temple,
            R.drawable.hotel,
            R.drawable.layer,
            R.drawable.laundry,
            R.drawable.library,
            R.drawable.movies,
            R.drawable.mosque,
            R.drawable.poffice,
            R.drawable.pharmacy,
            R.drawable.parks,
            R.drawable.resturant,
            R.drawable.train_station,
            R.drawable.rv
    };
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Initialise the Controller from here
        mControllerCallback = new ControllerResults();
        Controller.getsInstance(this).addResultCallback(mControllerCallback);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new CustomAdapter(this, prgmNameList, prgmDisplayNameList,
                prgmImages, prgmKeyword));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            }
        });
        currentLocation = (TextView) findViewById(R.id.location);
        locationImage = (ImageView) findViewById(R.id.locationimage);
        locationLayout = (LinearLayout)findViewById(R.id.location_layout);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation();
            }
        });
    }

    /**
     * Controller result Callback.
     */
    private class ControllerResults extends Controller.Result {

        @Override
        public void getInformationCallback(final Model status, final String reason) {
            Log.d(TAG, "getInformationCallback");
            runOnUiThread(new Runnable() {
                public void run() {
                    if (status == null) {
                        Toast.makeText(MainActivity.this, reason, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (status.getResults().length > 0) {
                        ResultsActivity.actionLaunchResultsActivity(MainActivity.this, status);
                    } else {
                        Toast.makeText(MainActivity.this, "No search results found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            // Location permissions is already available, show the camera preview.
            Log.d(TAG, "Location permission has already been granted.");
            //Enable the Location Settings
            if (LocationTracker.getsInstance(this).isLocationEnabled(MainActivity.this)) {
                LocationTracker.getsInstance(this).getLocation();
            }
        }
        updateLocation();
    }

    private void updateLocation() {
        PreferencesHelper preference = PreferencesHelper.getPreferences(this);
        String cityName = preference.getCityname();
        String countryName = preference.getCountryname();
        String text = "";

        if (cityName != null) {
            text = cityName + ", " ;
        }
        if (countryName != null) {
            text = text + countryName;
        }
        currentLocation.setText(text);
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

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestLocationPermission() {
        Log.d(TAG, "Location permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.d(TAG, "Displaying location permission rationale to provide additional context.");
//            Snackbar.make(mLayout, R.string.permission_camera_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    REQUEST_LOCATION);
//                        }
//                    })
//                    .show();
        } else {
            // Location permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for location permission.
            Log.d(TAG, "Received response for Location permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission has been granted, preview can be displayed
                Log.d(TAG, "Location permission has now been granted.");
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                if (LocationTracker.getsInstance(this).isLocationEnabled(MainActivity.this)) {
                    LocationTracker.getsInstance(this).getLocation();
                }
            } else {
                Log.d(TAG, "Location permission was NOT granted.");
                Toast.makeText(this, "Permission Rejected", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingsActivity.actionLaunchSettings(this);
            return true;
        }
        /*
        if (id == R.id.action_change_location) {
            LocationSearchActivity.actionLocationSearchActivity(this);
            return true;
        }
        */
        if (id == R.id.action_about) {
            AboutActivity.actionAboutActivity(this);
            return true;
        }
        /*
        if (id == R.id.action_change_location) {
            LocationSearchActivity.actionLocationSearchActivity(this);
            return true;
        }
        */
        if (id == R.id.action_rate_us) {
            final Uri marketUri = Uri.parse("market://details?id=" + "com.jsb.explorearround");
            startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
            return true;
        }

        if (id == R.id.action_mail_us) {
            try {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/html");
                String shareText = "Explore Arround";
                share.putExtra(Intent.EXTRA_TEXT, shareText);
                share.putExtra(Intent.EXTRA_EMAIL, new String[] { "jaysb.android@gmail.com" });
                share.putExtra(Intent.EXTRA_SUBJECT, "Explore Arround");
                startActivity(Intent.createChooser(share, "Send Email"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request.",
                    Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter {

        String[] names;
        String[] displayNames;
        Context context;
        int[] imageId;
        String[] keywords;
        private LayoutInflater inflater = null;

        public CustomAdapter(MainActivity mainActivity, String[] prgmNameList,
                             String[] prgmDisplayNameList, int[] prgmImages,
                             String[] prgmKeywordList) {
            names = prgmNameList;
            displayNames = prgmDisplayNameList;
            context = mainActivity;
            imageId = prgmImages;
            keywords = prgmKeywordList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.d("CustomAdapter", "pos: " + position + " v: " + convertView);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridview_holder, null);
            }
            TextView locationName = (TextView) convertView.findViewById(R.id.textView1);
            ImageView locationImage = (ImageView) convertView.findViewById(R.id.imageView1);

            locationName.setText(displayNames[position]);
            locationImage.setImageResource(imageId[position]);
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // check if GPS enabled
                    if (LocationTracker.getsInstance(MainActivity.this).isLocationEnabled(MainActivity.this)) {
                        //Invoke the Controller API to get the response back from Google server
                        Controller.getInstance().getInformation(MainActivity.this, names[position],
                                RADIUS, keywords[position]);
                    }
                }
            });
            return convertView;
        }

    }
}