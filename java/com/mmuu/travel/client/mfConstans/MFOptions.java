package com.mmuu.travel.client.mfConstans;

import android.graphics.Bitmap;

import com.mmuu.travel.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MFOptions {
    /**
     * 默认itemoption
     */
    public static DisplayImageOptions OPTION_DEF = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    /**
     * 头像
     */
    public static DisplayImageOptions Option_UserPhoto = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.user_photo_selector)
            .showImageOnLoading(R.drawable.user_photo_selector)
            .showImageOnFail(R.drawable.user_photo_selector).build();
    /**
     * 引导页
     */
    public static DisplayImageOptions OPTION_IVGUIDEBG = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.guide0)
            .showImageOnLoading(R.drawable.guide0)
            .showImageOnFail(R.drawable.guide0).build();
}
