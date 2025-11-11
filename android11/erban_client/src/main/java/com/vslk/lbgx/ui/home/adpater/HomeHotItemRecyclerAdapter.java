package com.vslk.lbgx.ui.home.adpater;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/26
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class HomeHotItemRecyclerAdapter extends BaseQuickAdapter<HomeRoom, BaseViewHolder> {

    public HomeHotItemRecyclerAdapter() {
        super(R.layout.item_home_hot_child);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeRoom item) {

        if(!StringUtil.isEmpty(item.getTitle())){
            helper.setText(R.id.home_title, item.getTitle());
        }
        ImageLoadUtils.loadAvatar(mContext, item.getAvatar(), helper.getView(R.id.avatar));
        ImageLoadUtils.loadImageWithBlurTransformation(mContext, item.getAvatar(), helper.getView(R.id.blur_avatar));

    }
}
