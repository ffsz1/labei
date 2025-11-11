package com.vslk.lbgx.ui.home.adpater;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.qiniu.android.utils.StringUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.audio.AudioPlayManager;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.vslk.lbgx.room.egg.AnimationsContainer;
import com.vslk.lbgx.utils.ImageLoadUtils;

/**
 * Function:
 * Author: Edward on 2019/6/17
 */
public class HomeRecommendAttentionAdapter extends BaseQuickAdapter<AttentionInfo, BaseViewHolder> {
    private boolean isPay = true;

    public HomeRecommendAttentionAdapter() {
        super(R.layout.item_home_recommend_attention);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AttentionInfo attentionInfo) {
        int position = baseViewHolder.getLayoutPosition();
//        RelativeLayout llAttentionItem = baseViewHolder.getView(R.id.ll_attention_item);
//        if (position == 1) {
//            llAttentionItem.setBackgroundResource(R.drawable.shape_fffafafa_corner_15dp);
//        } else {
//            llAttentionItem.setBackgroundResource(R.drawable.shape_ffffff_15dp);
//        }
        baseViewHolder.setText(R.id.tv_room_title, attentionInfo.getNick());
//        baseViewHolder.setText(R.id.tv_id, "ID: " + attentionInfo.getErbanNo());
//        Drawable drawableGender = attentionInfo.getGender() == 1 ? TextViewDrawableUtils.getCompoundDrawables(mContext, R.drawable.icon_man) : TextViewDrawableUtils.getCompoundDrawables(mContext, R.drawable.icon_female);
//        ((TextView) baseViewHolder.getView(R.id.tv_room_title)).setCompoundDrawablesRelative(null, null, drawableGender, null);
        ((ImageView) baseViewHolder.getView(R.id.gender)).setImageResource(attentionInfo.getGender() == 1 ? R.drawable.icon_man : R.drawable.icon_woman);
        if (attentionInfo.isFan()) {
            baseViewHolder.setText(R.id.room_attention, "已关注")
                    .setTextColor(R.id.room_attention, mContext.getResources()
                            .getColor(R.color.color_CCCCCC));

            baseViewHolder.getView(R.id.room_attention).setBackgroundResource(R.drawable.bg_home_attention_nor_off);
        } else {
            baseViewHolder.setText(R.id.room_attention, "关注")
                    .setTextColor(R.id.room_attention, mContext.getResources()
                            .getColor(R.color.color_7a9dff)).addOnClickListener(R.id.room_attention);
            baseViewHolder.getView(R.id.room_attention).setBackgroundResource(R.drawable.bg_home_attention_nor);
        }
        ImageLoadUtils.loadCircleImage(mContext, attentionInfo.getAvatar(), baseViewHolder.getView(R.id.iv_room_pic), R.drawable.ic_default_avatar);
        TextView tvSign = baseViewHolder.getView(R.id.tv_sign);
        if (!StringUtils.isBlank(attentionInfo.getUserDesc())) {
            String description = attentionInfo.getUserDesc();
            tvSign.setText(description);
        }
        baseViewHolder.setText(R.id.tv_time, attentionInfo.getVoiceDura() == null || attentionInfo.getVoiceDura().isEmpty() ? "0" : attentionInfo.getVoiceDura() + "``");
        ImageView gift = baseViewHolder.getView(R.id.iv_gif);
        View syView = baseViewHolder.getView(R.id.ll_sy);
        syView.setVisibility(View.VISIBLE);
        syView.setOnClickListener(v -> {
            AudioPlayManager playManager = null;
            AnimationsContainer.FramesSequenceAnimation dialogAnim = AnimationsContainer.getInstance(mContext, R.array.hot_voice, 5).createProgressDialogAnim(gift);
            if (isPay) {
                playManager = new AudioPlayManager(mContext, null, new OnPlayListener() {

                    // 音频转码解码完成，会马上开始播放了
                    @Override
                    public void onPrepared() {
                        dialogAnim.start();
                    }

                    // 播放结束
                    @Override
                    public void onCompletion() {
                        dialogAnim.stop();
                        isPay = true;
                        System.out.println("onCompletion");
                    }

                    // 播放被中断了
                    @Override
                    public void onInterrupt() {
                        dialogAnim.stop();
                        isPay = true;
                        System.out.println("onInterrupt");
                    }

                    // 播放过程中出错。参数为出错原因描述
                    @Override
                    public void onError(String error) {
                        dialogAnim.stop();
                        isPay = true;
                        System.out.println("onError");
                    }

                    // 播放进度报告，每隔 500ms 会回调一次，告诉当前进度。 参数为当前进度，单位为毫秒，可用于更新 UI
                    @Override
                    public void onPlaying(long curPosition) {
                    }
                });
                if (attentionInfo.getUserVoice() != null && !attentionInfo.getUserVoice().isEmpty()) {
                    playManager.setDataSource(attentionInfo.getUserVoice());
                    playManager.play();
                } else {
                    ToastUtils.showShort("TA还没有录制声音，快去提醒TA吧~");
                }
            } else {
                if (playManager != null) {
                    playManager.stopPlay();
                }
                isPay = true;
                dialogAnim.stop();
            }
        });
    }
}
