package com.vslk.lbgx.room.egg.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

/**
 * <p>房间消费adapter  </p>
 *
 * @author Administrator
 * @date 2017/11/20
 */
public class PoundEggRewordListAdapter extends BaseQuickAdapter<EggGiftInfo, BaseViewHolder> {

    public PoundEggRewordListAdapter() {
        super(R.layout.lv_item_egg_record);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EggGiftInfo info) {
        if (info == null) {
            return;
        }
        baseViewHolder.setText(R.id.tv_gift_name,  info.getGiftName() + "X" + info.getGiftNum())
                .setText(R.id.tv_gift_date, TimeUtils.getDateTimeString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        ImageView avatar = baseViewHolder.getView(R.id.img_avatar);
        ImageLoadUtils.loadImage(mContext,info.getPicUrl(), avatar);
    }
}
