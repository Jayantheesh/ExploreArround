package com.jsb.explorearround.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jsb.explorearround.Controller;
import com.jsb.explorearround.R;
import com.jsb.explorearround.parser.Photos;
import com.jsb.explorearround.utils.AppConstants;

public class PhotoViewerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;
    private static Photos[] mPhotos;
    private static int mCurrentPhoto;

    public static void actionLaunchPhotoViewerActivity(Activity fromActivity, int currentPhoto,
                                                       Photos[] photos) {
        Intent i = new Intent(fromActivity, PhotoViewerActivity.class);
        fromActivity.startActivity(i);
        mPhotos = photos;
        mCurrentPhoto = currentPhoto;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {

        }

        String[] urls = new String[mPhotos.length];
        if (mPhotos.length != 0) {
            //String[] urls = new String[mResults.getPhotos().length];
            for (int i = 0; i < mPhotos.length; i++ ) {
                urls[i] = Controller.BASE_URL + "/maps/api/place/photo" + "?maxwidth=400&photoreference=" +
                        mPhotos[i].getPhoto_reference() + "&key=" + AppConstants.API_KEY;
            }
        }

        //Invoke View Pager
        mPagerAdapter = new ViewPagerAdapter(PhotoViewerActivity.this, urls, mPhotos);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentPhoto);
    }
}