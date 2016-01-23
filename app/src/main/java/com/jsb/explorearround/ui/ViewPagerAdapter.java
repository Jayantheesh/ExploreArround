package com.jsb.explorearround.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jsb.explorearround.R;
import com.jsb.explorearround.parser.Photos;
import com.squareup.picasso.Picasso;

/**
 * Created by JSB on 11/16/15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context mContext;
    LayoutInflater mInflater;
    String[] mUrl;
    Photos[] mPhotos;

    public ViewPagerAdapter(Context context, String[] url, Photos[] photos) {
        this.mContext = context;
        this.mUrl = url;
        mPhotos = photos;
    }

    @Override
    public int getCount() {
        return mUrl.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        ImageView imgflag;

        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mInflater.inflate(R.layout.viewpager_item, container, false);

        // Locate the ImageView in viewpager_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.image);
        imgflag.setScaleType(ImageView.ScaleType.FIT_XY);
        imgflag.setMaxWidth(Integer.valueOf(mPhotos[position].getWidth()));
        imgflag.setMaxHeight(Integer.valueOf(mPhotos[position].getHeight()));
        Picasso.with(mContext)
                .load(mUrl[position])
                .placeholder(R.drawable.beauty)
                .into(imgflag);


        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
