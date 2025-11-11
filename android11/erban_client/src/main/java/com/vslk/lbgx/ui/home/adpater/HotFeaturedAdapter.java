package com.vslk.lbgx.ui.home.adpater;

import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeIcon;

/**
 * <p> 首页非热门adapter </p>
 *
 * @author Administrator
 * @date 2017/11/15
 */
public class HotFeaturedAdapter extends BaseQuickAdapter<HomeIcon, BaseViewHolder> {

    int width;

    public HotFeaturedAdapter() {
        super(R.layout.item_home_hot_featrued);
        width = (ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(24)) / 3;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeIcon homeRoom) {
        baseViewHolder.setText(R.id.tv_title, homeRoom.getTitle());
        RelativeLayout rlLayout = baseViewHolder.getView(R.id.fl_layout);
        ViewGroup.LayoutParams params = rlLayout.getLayoutParams();
        params.width = width;
//        params.height = (int) (width * 0.6);
        rlLayout.setLayoutParams(params);

        ImageLoadUtils.loadBannerRoundBackground(mContext, homeRoom.getPic(), baseViewHolder.getView(R.id.iv_room_pic), R.dimen.dp_5);
    }
}
