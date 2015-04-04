package com.uber.demo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nate on 3/26/15.
 */
public class GoogleImage {

    @SerializedName("GsearchResultClass")
    private String mGsearchResultClass;
    @SerializedName("width")
    private String mWidth;
    @SerializedName("height")
    private String mHeight;
    @SerializedName("imageId")
    private String mImageId;
    @SerializedName("tbWidth")
    private String mTbWidth;
    @SerializedName("tbHeight")
    private String mTbHeight;
    @SerializedName("unescapedUrl")
    private String mUnescapedUrl;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("visibleUrl")
    private String mVisibleUrl;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("titleNoFormatting")
    private String mTitleNoFormatting;
    @SerializedName("originalContextUrl")
    private String mOriginalContextUrl;
    @SerializedName("content")
    private String mContent;
    @SerializedName("contentNoFormatting")
    private String mContentNoFormatting;
    @SerializedName("tbUrl")
    private String mTbUrl;

    public String getUrl(){
        return mTbUrl;
    }
}
