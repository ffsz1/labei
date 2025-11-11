package com.vslk.lbgx.ui.home.adpater;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/25
 * 描述        遇见首页界面
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class MeetYouHeadAdapter extends BaseQuickAdapter<HomeRoom, BaseViewHolder> {

    private int width;

    public MeetYouHeadAdapter() {
        super(R.layout.meet_you_header_item);
        width = (ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(54)) / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeRoom item) {
        if (item == null) {
            return;
        }
        FrameLayout rlLayout = helper.getView(R.id.fl_layout);
        ViewGroup.LayoutParams params = rlLayout.getLayoutParams();
        params.width = width;
        params.height = (int) (params.width * 0.72);
        rlLayout.setLayoutParams(params);
        ImageView ivOnlineAnim = helper.getView(R.id.iv);
        ImageLoadUtils.loadImageRes(mContext, R.drawable.ic_new_online, ivOnlineAnim);
        helper.setText(R.id.title, item.getTitle()).setText(R.id.count, String.valueOf(item.getOnlineNum() + "人"));
        RoundedImageView avatar = helper.getView(R.id.avatar);
        ImageLoadUtils.loadAvatar(avatar.getContext(), item.getAvatar(), avatar);
    }
}
