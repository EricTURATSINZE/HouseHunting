package com.example.househunting.utils;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
/**
 * Author: FABRICE IRANKUNDA
 */
public class LoadImage {
    public static void loadImage(Context cxt, String imageUrl, ImageView holder, int resourcePlaceHolder){
        RequestOptions option = new RequestOptions().placeholder(resourcePlaceHolder).error(resourcePlaceHolder);
        Glide.with(cxt).load(imageUrl).apply(option).into(holder);

    }
}
