package com.vslk.lbgx.room.avroom.widget.dialog;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.bean.RoomFunctionBean;

/**
 * <p> 首页非热门adapter </p>
 *
 * @author Administrator
 * @date 2017/11/15
 */
public class RoomFunctionAdapter extends BaseQuickAdapter<RoomFunctionBean, BaseViewHolder> {

    public RoomFunctionAdapter(Context context) {
        super(R.layout.item_room_bottom_function);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RoomFunctionBean functionBean) {
        baseViewHolder.setText(R.id.tv_bottom_function,functionBean.getTitle());
        baseViewHolder.setImageResource(R.id.iv_bottom_function,functionBean.getImgRes());

    }
}
