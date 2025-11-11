package com.vslk.lbgx.ui.home.adpater;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiniu.android.utils.StringUtils;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.vslk.lbgx.utils.ImageLoadUtils;

/**
 * <p> 首页热门adapter </p>
 *
 * @author Administrator
 * @date 2017/11/16
 */
public class HomeNormalAdapter extends BaseQuickAdapter<HomeRoom, BaseViewHolder> {

    private int radius;

    public HomeNormalAdapter(Context context) {
        super(R.layout.item_meet_you);
        radius = ConvertUtils.dp2px(10);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeRoom homeItem) {
        if (homeItem == null) {
            return;
        }
        holder.setText(R.id.tv_hot_room_title, homeItem.getTitle())
                .setText(R.id.count, String.valueOf(homeItem.getOnlineNum() + "人"))
                .setText(R.id.erban_no, String.valueOf("ID: " + homeItem.getErbanNo()));
        ImageView ivOnlineAnim = holder.getView(R.id.iv_online_anim);
        ImageLoadUtils.loadImageRes(mContext, R.drawable.ic_new_online, ivOnlineAnim);
        ImageLoadUtils.loadSmallRoundBackground(mContext, homeItem.getAvatar(), holder.getView(R.id.iv_hot_room_logo), radius);
        ImageLoadUtils.loadImageTag(mContext, homeItem.tagPict, holder.getView(R.id.iv_home_tag));
        TextView tvSign = holder.getView(R.id.tv_sign);
        String description = homeItem.getUserDescription();
        if (!StringUtils.isBlank(description)) {
            tvSign.setText(description);
        }
    }
}
