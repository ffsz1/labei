package com.vslk.lbgx.ui.me.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/25
 * 描述        用户头像的banner
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class PhotosBannerAdapter extends StaticPagerAdapter {

    private Context context;
    private List<UserPhoto> bannerInfoList;
    private LayoutInflater mInflater;

    public PhotosBannerAdapter(List<UserPhoto> bannerInfoList, Context context) {
        this.context = context;
        this.bannerInfoList = bannerInfoList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        final UserPhoto bannerInfo = bannerInfoList.get(position);
        ImageView imgBanner = (ImageView) mInflater.inflate(R.layout.banner_page_item, container, false);
        ImageLoadUtils.loadImage(context, bannerInfo.getPhotoUrl(), imgBanner, R.drawable.ic_default_avatar);
        return imgBanner;
    }

    @Override
    public int getCount() {
        if (bannerInfoList == null) {
            return 0;
        } else {
            return bannerInfoList.size();
        }
    }

}
