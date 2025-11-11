package com.vslk.lbgx.room.avroom.adapter;


import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.room.avroom.other.BgTypeHelper;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.bean.ChatSelectBgBean;

/**
 * Created by Administrator on 2018/3/23.
 */

public class RoomSelectBgAdapter extends BaseQuickAdapter<ChatSelectBgBean, BaseViewHolder> {


    public RoomSelectBgAdapter() {
        super(R.layout.item_select_chat_room_bg);
    }

    public String selectIndex = "0";

    public void setItemAction(RoomSelectBgAdapter.itemAction itemAction) {
        this.itemAction = itemAction;
    }

    itemAction itemAction;

    @Override
    protected void convert(BaseViewHolder helper, ChatSelectBgBean item) {
        helper.getView(R.id.iv_select_icon).setVisibility(selectIndex.equals(item.id) ? View.VISIBLE : View.GONE);
        helper.setText(R.id.tv_select_bg_name, item.getBackName() + "");
        ImageView ivBack = helper.getView(R.id.iv_select_bg);
        ivBack.setOnClickListener(v -> {
            selectIndex = item.id;
            notifyDataSetChanged();
            if (itemAction != null) {
                itemAction.itemSelect(item);
            }
        });
        if (StringUtil.isEmpty(item.picUrl)) {
            ImageLoadUtils.loadImageRes(mContext, BgTypeHelper.getBgId(item.id), ivBack, R.drawable.ic_default_avatar);
        } else {
            ImageLoadUtils.loadImages(mContext, item.picUrl, ivBack, R.drawable.ic_default_avatar, R.drawable.ic_default_avatar);
        }
    }

    public interface itemAction {
        void itemSelect(ChatSelectBgBean item);
    }
}
