package com.vslk.lbgx.ui.home.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.BannerInfo;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/8/7
 */

public class BannerAdapter extends LoopPagerAdapter {
    private Context context;
    private List<BannerInfo> bannerInfoList;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;

    public BannerAdapter(RollPagerView viewPager, Context context, List<BannerInfo> bannerInfoList) {
        super(viewPager);
        this.context = context;
        this.bannerInfoList = bannerInfoList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        final BannerInfo bannerInfo = bannerInfoList.get(position);
        ImageView imgBanner = (ImageView) mInflater.inflate(R.layout.banner_page_item, container, false);
        ImageLoadUtils.loadImage(context, bannerInfo.getBannerPic(), imgBanner, R.drawable.ic_home_banner_placeholder);
        imgBanner.setOnClickListener(v -> {

            if (mListener != null) {
                mListener.itemClick(bannerInfo);
            }
            /*if (bannerInfo.getSkipType() == 3) {
                CommonWebViewActivity.start(context, bannerInfo.getSkipUri());
            } else if (bannerInfo.getSkipType() == 2) {
                AVRoomActivity.start(context, JavaUtil.str2long(bannerInfo.getSkipUri()));
            }*/
        });
        return imgBanner;
    }

    @Override
    public int getRealCount() {
        return bannerInfoList.size();
    }

    public interface OnItemClickListener {
        void itemClick(BannerInfo bannerInfo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
