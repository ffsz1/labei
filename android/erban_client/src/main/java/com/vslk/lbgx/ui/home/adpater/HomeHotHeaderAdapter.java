package com.vslk.lbgx.ui.home.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.util.util.DisplayUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.vslk.lbgx.utils.ImageLoadUtils;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/25
 * 描述        热门头部
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class HomeHotHeaderAdapter extends BaseQuickAdapter<HomeRoom, BaseViewHolder> {

    private int radius;
    private int width;

    public HomeHotHeaderAdapter(Context context) {
        super(R.layout.home_hot_header_item);
        radius = ConvertUtils.dp2px(10);
        width = (DisplayUtils.getScreenWidth(context) - ConvertUtils.dp2px(40)) / 3;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, HomeRoom item) {
        if (item == null) {
            return;
        }
        LinearLayout llTag = helper.getView(R.id.ll_tag);
        LinearLayout llOnline = helper.getView(R.id.ll_online);
        TextView tvTitle = helper.getView(R.id.title);
        TextView tvCount = helper.getView(R.id.count);
        ImageView ivTabCategory = helper.getView(R.id.iv_tab_category);
        if (TextUtils.isEmpty(item.getTitle()) || TextUtils.isEmpty(item.getAvatar())) {
            tvTitle.setVisibility(View.INVISIBLE);
            tvCount.setVisibility(View.GONE);
            llOnline.setVisibility(View.GONE);
            llTag.setVisibility(View.GONE);
        } else {
            llTag.setVisibility(View.VISIBLE);
            llOnline.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvCount.setVisibility(View.VISIBLE);
            tvTitle.setText(item.getTitle());
            tvCount.setText(item.getOnlineNum() + "");
            if (StringUtils.isNotEmpty(item.badge)) {
                ivTabCategory.setVisibility(View.VISIBLE);
                ImageLoadUtils.loadImageTag(mContext, item.badge, helper.getView(R.id.iv_tab_category));
            } else {
                ivTabCategory.setVisibility(View.GONE);
            }
            ImageLoadUtils.loadImageRes(mContext, R.drawable.ic_new_online, helper.getView(R.id.iv));
            ImageLoadUtils.loadImageTag(mContext, item.tagPict, helper.getView(R.id.iv_tab_label));
        }
        ImageLoadUtils.loadSmallRoundBackground(mContext, item.getAvatar(), helper.getView(R.id.avatar), radius);
    }
}
