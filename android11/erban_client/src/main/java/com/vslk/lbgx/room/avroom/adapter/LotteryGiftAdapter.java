package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/11.
 */

public class LotteryGiftAdapter extends BaseQuickAdapter<EggGiftInfo, LotteryGiftAdapter.ViewHolder> {

    private Context context;

    public LotteryGiftAdapter(Context context) {
        super(R.layout.item_lottery_gift);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder helper, EggGiftInfo item) {
        ImageLoadUtils.loadImage(context, item.getPicUrl(), helper.ivGiftIcon);
        helper.tvGiftInfo.setText(item.getGiftName());
        helper.tvGiftNum.setText("(" + item.getGoldPrice() + ")x" + item.getGiftNum());
    }


    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_lottery_dialog_gift_icon)
        ImageView ivGiftIcon;
        @BindView(R.id.tv_lottery_dialog_gift_info)
        TextView tvGiftInfo;
        @BindView(R.id.tv_lottery_dialog_gift_num)
        TextView tvGiftNum;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


}
