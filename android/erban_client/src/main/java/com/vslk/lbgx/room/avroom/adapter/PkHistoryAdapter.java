package com.vslk.lbgx.room.avroom.adapter;

import android.widget.ImageView;
import android.widget.SeekBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;

public class PkHistoryAdapter extends BaseQuickAdapter<PkVoteInfo, BaseViewHolder> {

    public PkHistoryAdapter() {
        super(R.layout.list_item_pk_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, PkVoteInfo item) {
        if (item == null)
            return;
        helper.setText(R.id.tv_left_nick,item.getNick());
        helper.setText(R.id.tv_right_nick,item.getPkNick());
        helper.setText(R.id.tv_pk_create_time,item.getCreateTime());
        SeekBar sb = helper.getView(R.id.skb_pk_result_progress);
        sb.setEnabled(false);
        if(item.getVoteCount() == 0 && item.getPkVoteCount() == 0) {
            sb.setProgress(50);
        }else {
            sb.setProgress(item.getVoteCount() * 100 / ((item.getVoteCount() + item.getPkVoteCount()) <= 0 ? 1 : (item.getVoteCount() + item.getPkVoteCount())));
        }
        helper.setText(R.id.tv_pk_left_count,item.getVoteCount()+"");
        helper.setText(R.id.tv_pk_right_count,item.getPkVoteCount()+"");
        helper.setText(R.id.tv_pk_type,item.getPkType() == 1?"按人数投票":"按礼物价值投票");
        ImageView ivLeft = helper.getView(R.id.iv_pk_left_state);
        ImageView ivRight = helper.getView(R.id.iv_pk_right_state);
        if (item.getVoteCount() == item.getPkVoteCount()){
            ivLeft.setImageResource(R.drawable.ic_pk_left_ping);
            ivRight.setImageResource(R.drawable.ic_pk_right_ping);
        }else if (item.getVoteCount() > item.getPkVoteCount()){
            ivLeft.setImageResource(R.drawable.ic_pk_left_victory);
            ivRight.setImageResource(R.drawable.ic_pk_history_lost);
        }else if (item.getVoteCount() < item.getPkVoteCount()){
            ivLeft.setImageResource(R.drawable.ic_pk_history_lost);
            ivRight.setImageResource( R.drawable.ic_pk_right_victory);
        }
        ImageLoadUtils.loadCircleImage(mContext, item.getAvatar(), helper.getView(R.id.iv_left_pk), R.drawable.ic_pk_left_avatar);
        ImageLoadUtils.loadCircleImage(mContext, item.getPkAvatar(), helper.getView(R.id.iv_right_pk), R.drawable.ic_pk_right_avatar);
        helper.addOnClickListener(R.id.tv_pk_again);
    }
}
