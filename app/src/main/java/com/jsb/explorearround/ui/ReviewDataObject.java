package com.jsb.explorearround.ui;

import android.content.Context;


/**
 * Created by JSB on 1/27/16.
 */
public class ReviewDataObject {

    private String mRatingTime;
    private String mRatingText;
    private String mAspectRatingId;
    private String mSeparator;
    private String mAspectType;
    private String mImageUrl;
    private String mAuthorName;
    private String mReviewText;

    public ReviewDataObject(String time, String ratingText,String aRatingId,
                            String seperator, String aspectType, String imageUrl, String author, String reveiwText) {
        mRatingTime = time;
        mRatingText = ratingText;
        mAspectRatingId = aRatingId;
        mSeparator = seperator;
        mAspectType = aspectType;
        mImageUrl = imageUrl;
        mAuthorName = author;
        mReviewText = reveiwText;

    }

    public String getmAspectRatingId() {
        return mAspectRatingId;
    }

    public String getmAspectType() {
        return mAspectType;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmRatingText() {
        return mRatingText;
    }

    public String getmRatingTime() {
        return mRatingTime;
    }

    public String getmReviewText() {
        return mReviewText;
    }

    public String getmSeparator() {
        return mSeparator;
    }

    public static class DataBuilder {
        Context context;
        String mRatingTime;
        String mRatingText;
        String mAspectRatingId;
        String mSeparator;
        String mAspectType;
        String mImageUrl;
        String mAuthorName;
        String mReviewText;

        public DataBuilder setmAspectRatingId(String mAspectRatingId) {
            this.mAspectRatingId = mAspectRatingId;
            return this;
        }

        public DataBuilder setmAspectType(String mAspectType) {
            this.mAspectType = mAspectType;
            return this;
        }

        public DataBuilder setmAuthorName(String mAuthorName) {
            this.mAuthorName = mAuthorName;
            return this;
        }

        public DataBuilder setmImageUrl(String mImageUrl) {
            this.mImageUrl = mImageUrl;
            return this;
        }

        public DataBuilder setmRatingText(String mRatingText) {
            this.mRatingText = mRatingText;
            return this;
        }

        public DataBuilder setmRatingTime(String mRatingTime) {
            this.mRatingTime = mRatingTime;
            return this;
        }

        public DataBuilder setmReviewText(String mReviewText) {
            this.mReviewText = mReviewText;
            return this;
        }

        public DataBuilder setmSeparator(String mSeparator) {
            this.mSeparator = mSeparator;
            return this;
        }

        public DataBuilder(Context activity) {
            this.context = activity;
        }

        public ReviewDataObject build() {
            return new ReviewDataObject(mRatingTime, mRatingText, mAspectRatingId, mSeparator, mAspectType, mImageUrl, mAuthorName, mReviewText);
        }
    }
}
