package com.jsb.explorearround.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jsb.explorearround.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JSB on 1/27/16.
 */
public class ReveiwAdapter extends RecyclerView.Adapter<ReveiwAdapter.DataObjectHolder> {

    private static String TAG = "ReveiwAdapter";
    private ArrayList<ReviewDataObject> mDataset;
    private Context mContext;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        AppCompatTextView ratingTime;
        AppCompatTextView rating_text;
        AppCompatRatingBar ratingBar;
        AppCompatTextView aspectRatingId;
        AppCompatTextView seperator;
        AppCompatTextView aspectType;
        ImageView photo;
        AppCompatTextView name;
        AppCompatTextView text;

        public DataObjectHolder(View itemView) {
            super(itemView);

            ratingTime = (AppCompatTextView) itemView.findViewById(R.id.review_rating_time);
            ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.review_rating_bar);
            rating_text = (AppCompatTextView) itemView.findViewById(R.id.review_rating_text);

            aspectType = (AppCompatTextView) itemView.findViewById(R.id.aspect_type_id);
            seperator = (AppCompatTextView) itemView.findViewById(R.id.seperator);
            aspectRatingId = (AppCompatTextView) itemView.findViewById(R.id.aspect_rating_id);

            photo = (ImageView) itemView.findViewById(R.id.review_avatar);
            name = (AppCompatTextView) itemView.findViewById(R.id.review_author_name);
            text = (AppCompatTextView) itemView.findViewById(R.id.review_comment);
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

    public ReveiwAdapter(ArrayList<ReviewDataObject> myDataset, Activity activity) {
        mDataset = myDataset;
        mContext = activity;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.ratingTime.setText(mDataset.get(position).getmRatingTime());
        holder.rating_text.setText(mDataset.get(position).getmRatingText());
        if (mDataset.get(position).getmRatingText() != null) {
            holder.ratingBar.setRating(Float.parseFloat(mDataset.get(position).getmRatingText()));
        }

        holder.aspectRatingId.setText(mDataset.get(position).getmAspectRatingId());
        holder.seperator.setText(mDataset.get(position).getmSeparator());
        holder.aspectType.setText(mDataset.get(position).getmAspectType());

        holder.name.setText(mDataset.get(position).getmAuthorName());
        holder.text.setText(mDataset.get(position).getmReviewText());
        holder.photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.face));
        String url = mDataset.get(position).getmImageUrl();
        if (url != null) {
            url = url.startsWith("http") ? url :"https:" + url;
            Picasso.with(mContext).load(url)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.face)
                    .into(holder.photo);
        }

    }

    public void addItem(ReviewDataObject dataObj, int index) {
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
