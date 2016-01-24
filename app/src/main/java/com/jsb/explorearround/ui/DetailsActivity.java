package com.jsb.explorearround.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jsb.explorearround.Controller;
import com.jsb.explorearround.R;
import com.jsb.explorearround.parser.Location;
import com.jsb.explorearround.parser.Result;
import com.jsb.explorearround.parser.Reviews;
import com.jsb.explorearround.utils.AppConstants;
import com.jsb.explorearround.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;
/**
 * Created by JSB on 10/24/15.
 */


public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "DetailsActivity";
    private static Result mResults = null;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    // 1st Card view
    private TextView mPlaceName;
    private TextView mVicinity;
    private LinearLayoutCompat mRatingBarLayout;
    private AppCompatRatingBar mRatingBar;
    private TextView mRating;
    private TextView mRatingCount;
    private TextView mDistance;
    private TextView mOpenStatus;

    //2nd card view
    private LinearLayoutCompat mDirections;
    private LinearLayoutCompat mShare;
    private LinearLayoutCompat mWebDetails;
    private LinearLayoutCompat mPhoneDetails;
    private TextView mWebAddress;
    private TextView mPhoneNumber;
    private LinearLayoutCompat mTimingLayout;

    //3rd Card view
    private LinearLayoutCompat mImageGallery;

    public static void actionLaunchResultsActivity(Activity fromActivity, Result res) {
        Intent i = new Intent(fromActivity, DetailsActivity.class);
        fromActivity.startActivity(i);
        mResults = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_details);
        if (mResults == null) {
            finish();
            return;
        }

        //Place details - 1st Card view
        mPlaceName =(TextView) findViewById(R.id.place_name);
        mPlaceName.setText(mResults.getName());

        mVicinity = (TextView) findViewById(R.id.vicinity);
        mVicinity.setText(mResults.getVicinity());

        mRatingBarLayout = (LinearLayoutCompat)findViewById(R.id.rating_bar);
        mRatingBar = (AppCompatRatingBar) mRatingBarLayout.findViewById(R.id.rating_star);

        mRating = (TextView) findViewById(R.id.rating);
        mRating.setText(mResults.getRating());

        mRatingCount =(TextView) findViewById(R.id.rating_count);
        mDistance = (TextView) findViewById(R.id.distance);
        String rating = mResults.getRating();
        if (rating != null) {
            mRatingBar.setRating(Float.valueOf(mResults.getRating()));
            Reviews[] reviews = mResults.getReviews();
            if (reviews != null) {
                String count = "- " + String.valueOf(reviews.length) + " Reviews";
                mRatingCount.setText(count);
                mDistance.setText(calculateDst(mResults.getGeometry().getLocation()));
            } else {
                mRatingBarLayout.setEnabled(false);
            }
        } else {
            mRatingBar.setEnabled(false);
            mRatingBarLayout.setVisibility(View.GONE);
        }

        mOpenStatus = (TextView) findViewById(R.id.open_now);
        mOpenStatus.setText("Open");
        //mOpenStatus.setTextColor(getColor(R.color.color_red));

        //Navigation - 2nd Card view
        mDirections = (LinearLayoutCompat) findViewById(R.id.direction);
        mShare = (LinearLayoutCompat) findViewById(R.id.share);
        mWebDetails = (LinearLayoutCompat)findViewById(R.id.website_layout);
        mWebAddress =(TextView) findViewById(R.id.website);
        if (mResults.getWebsite() != null) {
            mWebAddress.setText(mResults.getWebsite());
        } else {
            mWebDetails.setVisibility(View.GONE);
        }

        mPhoneDetails = (LinearLayoutCompat)findViewById(R.id.call_layout);
        mPhoneNumber =(TextView) findViewById(R.id.call);
        if (mResults.getInternational_phone_number() != null) {
            mPhoneNumber.setText(mResults.getInternational_phone_number());
        } else {
            mPhoneDetails.setVisibility(View.GONE);
        }

        mPhoneDetails.setOnClickListener(this);
        mDirections.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mWebDetails.setOnClickListener(this);
        updatePlaceTiming();

        //Photo - 3rd Card view
        mImageGallery = (LinearLayoutCompat) findViewById(R.id.imageGallery);
        if ((mResults.getPhotos() != null && mResults.getPhotos().length != 0)) {
            //String[] urls = new String[mResults.getPhotos().length];
            for (int i = 0; i < mResults.getPhotos().length; i++ ) {
                String url = Controller.BASE_URL + "/maps/api/place/photo" + "?maxwidth=400&photoreference=" +
                        mResults.getPhotos()[i].getPhoto_reference() + "&key=" + AppConstants.API_KEY;
                mImageGallery.addView(addDynamicImageView(url, i));
            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mResults.getName());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(this.getResources().getColor(R.color.color_primary));
        collapsingToolbarLayout.setStatusBarScrimColor(this.getResources().getColor(R.color.color_primary));

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        toolbar.setTitle(mResults.getName());
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Update place timing details
     */
    private void updatePlaceTiming() {
        mTimingLayout = (LinearLayoutCompat)findViewById(R.id.id_timings);
        if(mResults == null || mResults.getOpening_hours() == null) {
            mTimingLayout.setVisibility(View.GONE);
            return;
        }
        final LinearLayoutCompat weekly_timings = (LinearLayoutCompat) mTimingLayout.findViewById(R.id.id_week_timing);
        final String[] weekStatus = mResults.getOpening_hours().getWeekday_text();

        mTimingLayout.setVisibility(View.VISIBLE);
        TextView todayTime = (TextView) mTimingLayout.findViewById(R.id.id_today_hours);
        todayTime.append(getTodayTiming(Arrays.asList(weekStatus)));
        final TextView more = (TextView) mTimingLayout.findViewById(R.id.id_hours_more);
        more.setPaintFlags(more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getString(R.string.more).
                        equalsIgnoreCase(more.getText().toString())) {
                    if (weekStatus.length  == 0) {
                        return;
                    }

                    more.setText(getResources().getString(R.string.closed));
                    weekly_timings.setVisibility(View.VISIBLE);

                    ((TextView) mTimingLayout.findViewById(R.id.id_monday_hours)).setText(weekStatus[0]);
                    ((TextView) mTimingLayout.findViewById(R.id.id_tuesday_hours)).setText(weekStatus[1]);
                    ((TextView) mTimingLayout.findViewById(R.id.id_wednesday_hours)).setText(weekStatus[2]);
                    ((TextView) mTimingLayout.findViewById(R.id.id_thursday_hours)).setText(weekStatus[3]);
                    ((TextView) mTimingLayout.findViewById(R.id.id_friday_hours)).setText(weekStatus[4]);
                    ((TextView) mTimingLayout.findViewById(R.id.id_saturday_hours)).setText(weekStatus[5]);
                    ((TextView) mTimingLayout.findViewById(R.id.id_sunday_hours)).setText(weekStatus[6]);

                } else {
                    more.setText(getResources().getString(R.string.more));
                    weekly_timings.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Returns today's string from timing map
     *
     * @param timeMap
     * @return
     */
    public static String getTodayTiming(final List<String> timeMap) {
        if (timeMap == null || timeMap.size() == 0) {
            return null;
        }

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String timing;
        switch (day) {
            case 1: //Sunday
                timing = timeMap.get(6);
                return timing.substring(timing.indexOf(":") + 1);
            case 2:
                timing = timeMap.get(0);
                return timing.substring(timing.indexOf(":") + 1);
            case 3:
                timing = timeMap.get(1);
                return timing.substring(timing.indexOf(":") + 1);
            case 4:
                timing = timeMap.get(2);
                return timing.substring(timing.indexOf(":") + 1);
            case 5:
                timing = timeMap.get(3);
                return timing.substring(timing.indexOf(":") + 1);
            case 6:
                timing = timeMap.get(4);
                return timing.substring(timing.indexOf(":") + 1);
            case 7:
                timing = timeMap.get(5);
                return timing.substring(timing.indexOf(":") + 1);
            default:
                return null;
        }
    }

    private float getDpToPixel(final int dp) {
        Resources r = getResources();
        return applyDimension(COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private ImageView addDynamicImageView(final String url, final int currentPhoto) {
        final ImageView imageView = new ImageView(this);
        LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                (int) getDpToPixel(100),
                (int) getDpToPixel(100));
        lp.setMargins(0, 0, 10, 0);
        imageView.setLayoutParams(lp);
        imageView.setClickable(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewerActivity.actionLaunchPhotoViewerActivity(DetailsActivity.this,
                        currentPhoto, mResults.getPhotos());
            }
        });
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.beauty)
                .into(imageView);
        return imageView;
    }

    private String calculateDst(Location location) {
        String ret = null;

        PreferencesHelper preference = PreferencesHelper.getPreferences(this);
        android.location.Location srcLoc = new android.location.Location("source");
        srcLoc.setLatitude(Double.valueOf( preference.getLatitude()));
        srcLoc.setLongitude(Double.valueOf(preference.getLongtitude()));


        android.location.Location dstLoc = new android.location.Location("destination");
        dstLoc.setLatitude(Double.valueOf(location.getLatitude()));
        dstLoc.setLongitude(Double.valueOf(location.getLongtitude()));

        int unit = preference.getUnits();
        Float dst = (srcLoc.distanceTo(dstLoc) / 1000);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        if (unit == AppConstants.MILES) {
            Double miles = dst * 0.621;
            //ret = NumberFormat.getInstance().format(miles);
            ret = df.format(miles);
            ret = ret + " Miles";
        } else {
            ret = df.format(dst);
            ret = ret + " KM";
        }
        return ret;
    }

//    private void shrinkOpenHoursLayout() {
//        if (mWeekDayItemLayout.getVisibility() == View.VISIBLE) {
//            shrinkOpenHoursLayout(true);
//        } else {
//            shrinkOpenHoursLayout(false);
//        }
//    }
//
//    private void shrinkOpenHoursLayout(boolean collapse) {
//        if (collapse) {
//            if (mUpDownButton == null) {
//                mUpDownButton = (Button) mOpenHoursLayout.findViewById(R.id.updown_btn);
//            }
//            mUpDownButton.setBackground(ContextCompat.getDrawable(this, R.drawable.arrow_down));
//            mWeekDayItemLayout.setVisibility(View.GONE);
//        } else { /* Spread mode */
//            if (mUpDownButton == null) {
//                mUpDownButton = (Button) mOpenHoursLayout.findViewById(R.id.updown_btn);
//            }
//            mUpDownButton.setBackground(ContextCompat.getDrawable(this,R.drawable.arrow_up));
//            mWeekDayItemLayout.setVisibility(View.VISIBLE);
//            if (mResults != null && mResults.getOpening_hours() != null) {
//                String[] weekStatus = mResults.getOpening_hours().getWeekday_text();
//                StringBuffer days = new StringBuffer();
//                StringBuffer time = new StringBuffer();
//                int len = weekStatus.length;
//                for (int i=0; i < len; i++) {
//                    String[] split = weekStatus[i].split(": ");
//                    String[] dualTime = split[1].split(",");
//                    if (dualTime.length > 1) {
//                        time.append(dualTime[0] + "\n" + dualTime[1] + "\n");
//                        days.append(split[0] + "\n" + "\n");
//                    } else {
//                        time.append(split[1] + "\n");
//                        days.append(split[0] + "\n");
//                    }
//                }
//                mWeekdayHours.setText(time.toString());
//                mWeekdayStatus.setText(days.toString());
//            }
//        }
//    }

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
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng location = new LatLng(Double.valueOf(mResults.getGeometry().getLocation().getLatitude()),
                Double.valueOf(mResults.getGeometry().getLocation().getLongtitude()));
        map.addMarker(new MarkerOptions().position(location).title(mResults.getName()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Log.d(TAG, "action bar clicked");
            Toast.makeText(this, R.string.text_no_phone_error, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
//            case R.id.updown_btn:
//                shrinkOpenHoursLayout();
//                break;
            case R.id.call_layout://Call
                try {
                    String phone = mResults.getInternational_phone_number();
                    if (phone != null) {
                        String uri = "tel:" + mResults.getInternational_phone_number();
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                        startActivity(dialIntent);
                    } else {
                        Toast.makeText(this, R.string.text_no_phone_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, R.string.text_no_app_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.direction://Direction
                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(AppConstants.GOOGLE_MAPS_URI
                                    + String.valueOf(mResults.getGeometry().getLocation().getLatitude())
                                    + ","
                                    + String.valueOf(mResults.getGeometry().getLocation().getLongtitude())));
                    intent.setClassName(AppConstants.GOOGLE_MAPS_PACKAGE, AppConstants.GOOGLE_MAPS_ACTIVITY);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, R.string.text_no_app_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.share://Share
                try {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    String web = "";
                    String phone = "";
                    if (mResults.getWebsite() != null) {
                        web = mResults.getWebsite();
                    }
                    if (mResults.getInternational_phone_number() != null) {
                        phone = mResults.getInternational_phone_number();
                    }
                    String shareText = mResults.getName() + "\n" + mResults.getVicinity() + "\n"
                            + phone + "\n"
                            + web + "\n"
                            + mResults.getGeometry().getLocation().getLatitude()
                            + ","
                            + mResults.getGeometry().getLocation().getLongtitude();
                    share.putExtra(Intent.EXTRA_TEXT, shareText);
                    startActivity(Intent.createChooser(share,
                            getResources().getString(R.string.text_share_info)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, R.string.text_no_app_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.website_layout://Website
                try {
                    String web = mResults.getWebsite();
                    if (web != null) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(this, R.string.text_no_website_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, R.string.text_no_app_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }
}
