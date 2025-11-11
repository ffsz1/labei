package com.vslk.lbgx.base.bindadapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.utils.StarUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.Date;

/**
 * Created by huangmeng1 on 2018/1/4.
 */

public class ViewAdapter {

    @BindingAdapter(value = {"imgUrl","defaultImage","isRound"},requireAll = false)
    public static void setImgUrl(ImageView imageView, String url, Drawable drawable,boolean isRound){
        if (isRound){
            ImageLoadUtils.loadSmallRoundBackground(imageView.getContext(), url, imageView);
        }else {
            GlideApp.with(imageView.getContext())
                    .load(TextUtils.isEmpty(url) ? R.mipmap.ic_tag_default : url)
                    .placeholder(drawable)
                    .error(drawable)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                            float ratio = (drawable.getIntrinsicHeight() + 0.F) / drawable.getIntrinsicWidth();
                            int width = Math.round(imageView.getContext().getResources().getDimensionPixelOffset(R.dimen.tag_height) / ratio);
                            int height = imageView.getContext().getResources().getDimensionPixelOffset(R.dimen.tag_height);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                            params.width = width;
                            params.height = height;
                            imageView.setLayoutParams(params);
                            imageView.setImageDrawable(drawable);
                            return true;
                        }
                    })
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"roundImage"})
    public static void setRoundImage(ImageView imageView, String url) {
        ImageLoadUtils.loadCircleImage(imageView.getContext(), url, imageView, R.drawable.ic_default_avatar);
    }

    @BindingAdapter(value = {"family_bg"})
    public static void setBgImage(ImageView imageView, String url) {
        if (StringUtils.isEmpty(url)) {
            ImageLoadUtils.loadImageRes(imageView.getContext(), R.drawable.ic_family_avatar_bg, imageView);
        } else {
            ImageLoadUtils.loadBigAvatar(imageView.getContext(), url, imageView);
        }
    }

    @BindingAdapter(value = {"date"},requireAll = false)
    public static void setConstellation(TextView textView,long date){
        String star = StarUtils.getConstellation(new Date(date));
        if (null == star) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(star);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter(value = {"phone","passWord","code"},requireAll = false)
    public static void setButtonState(TextView textView,String phone,String passWord,String code){
        if (phone.length()!=0 && (passWord==null || passWord.length()>=6) && (code==null || code.length()!=0)){
            textView.setEnabled(true);
            textView.setBackgroundResource(R.drawable.shape_green_gradient_5dp);
        }else {
            textView.setEnabled(false);
            textView.setBackgroundResource(R.drawable.shape_gray_5dp);
        }
    }
}
