package com.equalinfotech.coffee.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.viewpager.widget.PagerAdapter;

import com.equalinfotech.coffee.R;
import com.equalinfotech.coffee.modal.HomeScreenReponse;
import com.equalinfotech.coffee.util.Utils;


import java.util.ArrayList;
import java.util.List;


public class ImageSlideAdapter extends PagerAdapter {
    Activity activity;
    List<HomeScreenReponse.Data.Slider> imageList;
    OnBannerItemClick onItemClick;

    public ImageSlideAdapter(Activity activity, List<HomeScreenReponse.Data.Slider> imageList
    , OnBannerItemClick onItemClick) {
        this.activity = activity;
        if(this.imageList==null)
        this.imageList=new ArrayList<>(imageList.size());
        this.imageList = imageList;
        this.onItemClick=onItemClick;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vp_image, container, false);

        ImageView mImageView = (ImageView) view
                .findViewById(R.id.image_display);

        ProgressBar progressBar=view.findViewById(R.id.pgrBanner);
        if(imageList.get(position).getImage()!=null) {
            Utils.loadingImageUsingGlideWithOutRound(activity,progressBar,imageList.get(position).getImage(),mImageView);

        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

   public interface  OnBannerItemClick{
        void onBannerImageClick(String data);
   }

}
