package com.equalinfotech.coffee.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.equalinfotech.coffee.R;


import java.io.File;


public class Utils {


    public static Bitmap getBitmapFromImagePath(String path) {
        Bitmap bitmap = null;
        File imgFile = new File(path);
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        }
        return bitmap;
    }
    /**
     * will toast message
     *
     * @param context
     * @param msg
     */
    public static void showLongToastMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * show message In Toast.
     *
     * @param context
     * @param string
     */
    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }



    public static void loadingImageUsingGlideWithOutRound(Context context, ProgressBar pbLoader, String url, ImageView imv) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_image_placeholder_app_user);
        requestOptions.error(R.drawable.ic_image_placeholder_app_user);
        requestOptions.timeout(30000);
        requestOptions.priority(Priority.HIGH);


        pbLoader.setVisibility(View.VISIBLE);
        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pbLoader.setVisibility(View.GONE);
                        imv.setImageResource(R.drawable.drawable_gray);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pbLoader.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imv);


    }




}
