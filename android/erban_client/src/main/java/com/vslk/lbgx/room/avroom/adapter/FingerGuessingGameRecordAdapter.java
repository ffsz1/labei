package com.vslk.lbgx.room.avroom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.bean.FingerGuessingGameRecordInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

public class FingerGuessingGameRecordAdapter extends BaseQuickAdapter<FingerGuessingGameRecordInfo, BaseViewHolder> {
    public FingerGuessingGameRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FingerGuessingGameRecordInfo item) {
        ImageLoadUtils.loadCircleImage(mContext, item.getAvatar(), helper.getView(R.id.iv_user_head), R.drawable.ic_default_avatar);
        ImageLoadUtils.loadCircleImage(mContext, item.getGiftUrl(), helper.getView(R.id.iv_gift), R.drawable.ic_default_avatar);
        helper.setText(R.id.tv_name, item.getNick());
        helper.setText(R.id.tv_time, TimeUtils.getDateTimeString(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        helper.setText(R.id.tv_desc, item.getSubject());
        helper.setText(R.id.tv_gift_name_and_number, item.getGiftName() + "X" + item.getNum());
    }
}
