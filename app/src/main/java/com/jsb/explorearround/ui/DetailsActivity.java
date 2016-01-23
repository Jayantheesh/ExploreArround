package com.jsb.explorearround.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;
/**
 * Created by JSB on 10/24/15.
 */


public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "DetailsActivity";
    private static Result mResults = null;
    private TextView mWebAddress;
    private TextView mPhoneNumber;
    private TextView mVicinity;
    private TextView mRating;
    private TextView mName;
    private TextView mReviews;
    private TextView mDistance;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private RatingBar mRatingBar;

    private LinearLayout mPhone;
    private LinearLayout mDirection;
    private LinearLayout mShare;
    private LinearLayout mWebsite;

    private LinearLayout mWebDetails;
    private LinearLayout mPhoneDetails;
    private LinearLayout mRatingBarLayout;

    private LinearLayout mOpenHoursLayout;
    private LinearLayout mWeekDayItemLayout;
    private TextView mOpenHourText;
    private TextView mWeekdayStatus;
    private TextView mWeekdayHours;
    private Button mUpDownButton;

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
        mWebAddress = (TextView) findViewById(R.id.weblink);
        mPhoneNumber = (TextView) findViewById(R.id.phone);
        mVicinity = (TextView) findViewById(R.id.vicinity);
        mRating = (TextView) findViewById(R.id.rating);
        mName = (TextView) findViewById(R.id.name);
        mReviews = (TextView) findViewById(R.id.reviews);
        mDistance = (TextView) findViewById(R.id.distance);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mPhone = (LinearLayout) findViewById(R.id.phone_layout);
        mDirection = (LinearLayout) findViewById(R.id.direction_layout);
        mShare = (LinearLayout) findViewById(R.id.share_layout);
        mWebsite = (LinearLayout) findViewById(R.id.website_layout);

        mOpenHoursLayout = (LinearLayout) findViewById(R.id.open_hours_layout);
        mOpenHourText = (TextView) findViewById(R.id.open_status_text);
        mUpDownButton = (Button) findViewById(R.id.updown_btn);
        mUpDownButton.setOnClickListener(this);
        mWeekDayItemLayout = (LinearLayout) findViewById(R.id.weekday_item_layout);

        mWeekdayStatus =(TextView) findViewById(R.id.weekday_text);
        mWeekdayHours =(TextView) findViewById(R.id.weekday_hours);

        mWebDetails = (LinearLayout)findViewById(R.id.weblayout);
        mPhoneDetails = (LinearLayout)findViewById(R.id.phonelayout);
        mRatingBarLayout = (LinearLayout)findViewById(R.id.ratingBarLayout);

        mPhone.setOnClickListener(this);
        mDirection.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mWebsite.setOnClickListener(this);
        mImageGallery = (LinearLayoutCompat) findViewById(R.id.imageGallery);

        if (mResults == null) {
            finish();
            return;
        }
        if (mResults.getWebsite() != null) {
            mWebAddress.setText(mResults.getWebsite());
        } else {
            mWebDetails.setVisibility(View.GONE);
        }

        if (mResults.getInternational_phone_number() != null) {
            mPhoneNumber.setText(mResults.getInternational_phone_number());
        } else {
            mPhoneDetails.setVisibility(View.GONE);
        }

        mVicinity.setText(mResults.getVicinity());
        mRating.setText(mResults.getRating());
        mName.setText(mResults.getName());

        String rating = mResults.getRating();
        if (rating != null) {
            mRatingBar.setRating(Float.valueOf(mResults.getRating()));
            Reviews[] reviews = mResults.getReviews();
            if (reviews != null) {
                String count = "- " + String.valueOf(reviews.length) + " reviews";
                mReviews.setText(count);

                mDistance.setText(" - " + calculateDst(mResults.getGeometry().getLocation()));
            } else {
                mReviews.setEnabled(false);
            }

        } else {
            mRatingBar.setEnabled(false);
            mRatingBarLayout.setVisibility(View.GONE);
        }

        if (mResults.getOpening_hours() == null) {
            mOpenHoursLayout.setVisibility(View.GONE);
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

        if ((mResults.getPhotos().length != 0)) {
            //String[] urls = new String[mResults.getPhotos().length];
            for (int i = 0; i < mResults.getPhotos().length; i++ ) {
                String url = Controller.BASE_URL + "/maps/api/place/photo" + "?maxwidth=400&photoreference=" +
                        mResults.getPhotos()[i].getPhoto_reference() + "&key=" + AppConstants.API_KEY;
                mImageGallery.addView(addDynamicImageView(url, i));
            }
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
        srcLoc.setLongitude(Double.valueOf( preference.getLongtitude()));


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

    private void shrinkOpenHoursLayout() {
        if (mWeekDayItemLayout.getVisibility() == View.VISIBLE) {
            shrinkOpenHoursLayout(true);
        } else {
            shrinkOpenHoursLayout(false);
        }
    }

    private void shrinkOpenHoursLayout(boolean collapse) {
        if (collapse) {
            if (mUpDownButton == null) {
                mUpDownButton = (Button) mOpenHoursLayout.findViewById(R.id.updown_btn);
            }
            mUpDownButton.setBackground(ContextCompat.getDrawable(this, R.drawable.arrow_down));
            mWeekDayItemLayout.setVisibility(View.GONE);
        } else { /* Spread mode */
            if (mUpDownButton == null) {
                mUpDownButton = (Button) mOpenHoursLayout.findViewById(R.id.updown_btn);
            }
            mUpDownButton.setBackground(ContextCompat.getDrawable(this,R.drawable.arrow_up));
            mWeekDayItemLayout.setVisibility(View.VISIBLE);
            if (mResults != null && mResults.getOpening_hours() != null) {
                String[] weekStatus = mResults.getOpening_hours().getWeekday_text();
                StringBuffer days = new StringBuffer();
                StringBuffer time = new StringBuffer();
                int len = weekStatus.length;
                for (int i=0; i < len; i++) {
                    String[] split = weekStatus[i].split(": ");
                    String[] dualTime = split[1].split(",");
                    if (dualTime.length > 1) {
                        time.append(dualTime[0] + "\n" + dualTime[1] + "\n");
                        days.append(split[0] + "\n" + "\n");
                    } else {
                        time.append(split[1] + "\n");
                        days.append(split[0] + "\n");
                    }
                }
                mWeekdayHours.setText(time.toString());
                mWeekdayStatus.setText(days.toString());
            }
        }
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
            case R.id.updown_btn:
                shrinkOpenHoursLayout();
                break;
            case R.id.phone_layout://Call
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
            case R.id.direction_layout://Direction
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
            case R.id.share_layout://Share
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
