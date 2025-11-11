package com.vslk.lbgx.ui.home.adpater;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;

/**
 * <p> 首页非热门adapter </p>
 *
 * @author Administrator
 * @date 2017/11/15
 */
public class HomeAttentionAdapter extends BaseQuickAdapter<AttentionInfo, BaseViewHolder> {

    private int itemWidth;
    private boolean isRound;

    public HomeAttentionAdapter(boolean isRound) {
        super(R.layout.item_home_attention_head);
        this.isRound = isRound;
        this.itemWidth = (ScreenUtil.getDisplayWidth() - ConvertUtils.dp2px(10)) / 3;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AttentionInfo attentionInfo) {

        baseViewHolder.setText(R.id.tv_room_title, attentionInfo.getNick())
                .setGone(R.id.tv_room_attention, !isRound)
                .setGone(R.id.room_attention, isRound);

        if (itemWidth > 0) {
            FrameLayout llContent = baseViewHolder.getView(R.id.ll_attention_item);
            ViewGroup.LayoutParams vl = llContent.getLayoutParams();
            vl.width = itemWidth;
            llContent.setLayoutParams(vl);
        }
        if (isRound) {
            if (attentionInfo.isFan()) {
                baseViewHolder.setText(R.id.room_attention, "已关注");
            } else {
                baseViewHolder.setText(R.id.room_attention, "关注")
                        .addOnClickListener(R.id.room_attention);
            }
            ImageLoadUtils.loadCircleImage(mContext, attentionInfo.getAvatar(), baseViewHolder.getView(R.id.iv_room_pic), R.drawable.ic_default_avatar);
        } else {
            TextView findHim = baseViewHolder.getView(R.id.tv_room_attention);
            findHim.setOnClickListener(v -> AVRoomActivity.start(mContext, attentionInfo.getUid()));
            ImageLoadUtils.loadSmallRoundBackground(mContext, attentionInfo.getAvatar(), baseViewHolder.getView(R.id.iv_room_pic));
        }
    }
}
