package com.vslk.lbgx.ui.me.user.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;

import java.util.ArrayList;

/**
 * Created by ${Seven} on 2017/8/14.
 */

public class PhotoAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<UserPhoto> bigImagesList;

    private PhotoAdapter.imageOnclickListener mImageOnclickListener;

    public void setmImageOnclickListener(PhotoAdapter.imageOnclickListener imageOnclickListener){
        mImageOnclickListener=imageOnclickListener;
    }
    public interface imageOnclickListener{
        void onClick();
    }

    //传进来的photolist用于设置进imageview里面去
    public PhotoAdapter(Context Context, ArrayList<UserPhoto> photoList) {
        context = Context;
        this.bigImagesList = photoList;
    }
    @Override
    public int getCount() {
        return bigImagesList == null ? 0 : bigImagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(context);
        //拿到userphoto对象
        UserPhoto userPhoto = bigImagesList.get(position);
        //把对象设置到imageview里面去
        ImageLoadUtils.loadImage(context, userPhoto.getPhotoUrl(), imageView);
        //然后把imageview添加到容器里面去
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageOnclickListener.onClick();
            }
        });
        return imageView;
    }
}
