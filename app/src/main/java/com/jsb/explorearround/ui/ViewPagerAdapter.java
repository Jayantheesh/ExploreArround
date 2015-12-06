package com.jsb.explorearround.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsb.explorearround.R;
import com.squareup.picasso.Picasso;

/**
 * Created by JSB on 11/16/15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context mContext;
    LayoutInflater mInflater;
    String[] mUrl;

    public ViewPagerAdapter(Context context, String[] url) {
        this.mContext = context;
        this.mUrl = url;
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
        TextView txtrank;
        TextView txtcountry;
        TextView txtpopulation;
        ImageView imgflag;

        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mInflater.inflate(R.layout.viewpager_item, container,
                false);

        // Locate the ImageView in viewpager_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.image);
        // Capture position and set to the ImageView
        //imgflag.setImageResource(flag[position]);

        imgflag.setScaleType(ImageView.ScaleType.FIT_XY);

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
