package com.jsb.explorearround.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jsb.explorearround.Controller;
import com.jsb.explorearround.R;
import com.jsb.explorearround.utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JSB on 10/9/15.
 */
public class RecyclerViewAdapter extends RecyclerView
        .Adapter<RecyclerViewAdapter
        .DataObjectHolder> {
    private static String TAG = "RecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private Context mContext;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView photo;
        TextView name;
        TextView address;
        TextView distance;
        TextView open_status;
        RatingBar rating;

        public DataObjectHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            distance = (TextView) itemView.findViewById(R.id.distance);
            open_status = (TextView) itemView.findViewById(R.id.status);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            Log.d(TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public RecyclerViewAdapter(ArrayList<DataObject> myDataset, Activity activity) {
        mDataset = myDataset;
        mContext = activity;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        if (mDataset.get(position).getPhotos() != null) {
            Log.d("RecyclerViewAdapter", "photoUrl: " + mDataset.get(position).getPhotos()[0].getPhotoUrl()[0]);
        }
        holder.name.setText(mDataset.get(position).getmName());
        holder.address.setText(mDataset.get(position).getmAddress());
        holder.distance.setText(mDataset.get(position).getmDistance());
        holder.open_status.setText(mDataset.get(position).getmStatus());
        if(mDataset.get(position).getmStatus().equalsIgnoreCase("closed")) {
            holder.open_status.setTextColor(Color.RED);
        } else {
            holder.open_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
        }
        if (mDataset.get(position).getPhotos() != null) {
            String url = Controller.BASE_URL + "/maps/api/place/photo" + "?maxwidth=400&photoreference=" +
                    mDataset.get(position).getPhotos()[0].getPhoto_reference() + "&key=" + AppConstants.API_KEY;
            Picasso.with(mContext).load(url)
                    .into(holder.photo);
            Log.e(TAG, "Icon=" + mDataset.get(position).getIcon());
            holder.photo.setTag(R.string.photo_url, url);
        } else {
            Picasso.with(mContext).load(mDataset.get(position).getIcon())
                    .into(holder.photo);
            holder.photo.setTag(R.string.photo_url, null);

        }
        if (mDataset.get(position).getRating() != null) {
            holder.rating.setRating(Float.parseFloat(mDataset.get(position).getRating()));
        }
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
