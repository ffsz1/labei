package com.vslk.lbgx.ui.home.adpater;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;

/**
 * Function:
 * Author: Edward on 2019/4/15
 */
public class HomeRecommendItemRecyclerAdapter extends BaseQuickAdapter<HomeRoom, BaseViewHolder> {

    public HomeRecommendItemRecyclerAdapter() {
        super(R.layout.item_recommend);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeRoom item) {

//        helper.setText(R.id.home_title, item.getTitle());
//        ImageLoadUtils.loadAvatar(mContext, item.getAvatar(), helper.getView(R.id.avatar));
//        ImageLoadUtils.loadImageWithBlurTransformation(mContext, item.getAvatar(), helper.getView(R.id.blur_avatar));

    }
}
