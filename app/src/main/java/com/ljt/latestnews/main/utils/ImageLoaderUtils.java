package com.ljt.latestnews.main.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ljt.latestnews.R;

/**
 * Created by ${JT.L} on 2017/11/21.
 */

public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView,String url){
        if(imageView ==null){
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loadfail).crossFade().into(imageView);
    }
}
