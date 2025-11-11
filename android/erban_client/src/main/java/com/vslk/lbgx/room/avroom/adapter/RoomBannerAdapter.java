package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/8/7
 */

public class RoomBannerAdapter extends StaticPagerAdapter {
    private Context context;
    private List<ActionDialogInfo> bannerInfoList;
    private LayoutInflater mInflater;

    public RoomBannerAdapter(List<ActionDialogInfo> bannerInfoList, Context context) {
        this.context = context;
        this.bannerInfoList = bannerInfoList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        final ActionDialogInfo bannerInfo = bannerInfoList.get(position);
        ImageView imgBanner = (ImageView) mInflater.inflate(R.layout.banner_page_item, container, false);
        ImageLoadUtils.loadBannerRoundBackground(context, bannerInfo.getAlertWinPic(), imgBanner,
                R.dimen.common_cover_round_size, R.drawable.ic_default_avatar);
        imgBanner.setOnClickListener(v -> CommonWebViewActivity.start(context, bannerInfo.getSkipUrl()));
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
