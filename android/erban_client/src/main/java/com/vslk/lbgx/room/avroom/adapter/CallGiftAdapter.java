package com.vslk.lbgx.room.avroom.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.vslk.lbgx.utils.ImageLoadUtils;

import java.util.List;

public class CallGiftAdapter extends BaseQuickAdapter<GiftInfo, BaseViewHolder> {


    private int oldPosition = 0;

    public CallGiftAdapter(@Nullable List<GiftInfo> data) {
        super(R.layout.item_call_gift, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftInfo item) {
        helper.setText(R.id.tv_gift, item.getGiftName());
        helper.setText(R.id.tv_gold, item.getGoldPrice() + "");
        ImageLoadUtils.loadImage(mContext, item.getGiftUrl(), helper.getView(R.id.iv_gift));
        helper.getView(R.id.ll_gift).setBackgroundResource(item.isSelected() ? R.mipmap.ic_call_gift_item_on : R.mipmap.ic_call_gift_item_off);
        helper.itemView.setOnClickListener(v -> {
            if (oldPosition != helper.getAdapterPosition()) {
                getData().get(helper.getAdapterPosition()).setSelected(true);
                notifyItemChanged(helper.getAdapterPosition());
                getData().get(oldPosition).setSelected(false);
                notifyItemChanged(oldPosition);
                oldPosition = helper.getAdapterPosition();
                if (listener != null)
                    listener.selectGift(getData().get(helper.getAdapterPosition()));
            }
        });
    }

    private ICallGiftListener listener;

    public void setCallGiftListener(ICallGiftListener listener) {
        this.listener = listener;
    }

    public interface ICallGiftListener {
        void selectGift(GiftInfo info);
    }
}
