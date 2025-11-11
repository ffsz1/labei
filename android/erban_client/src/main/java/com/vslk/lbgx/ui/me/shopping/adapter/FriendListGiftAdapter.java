package com.vslk.lbgx.ui.me.shopping.adapter;

import com.vslk.lbgx.base.bindadapter.BaseAdapter;
import com.vslk.lbgx.base.bindadapter.BindingViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.databinding.ListItemShareFansBinding;


/**
 * Created by chenran on 2017/10/3.
 */

public class FriendListGiftAdapter extends BaseAdapter<NimUserInfo> {

    public FriendListGiftAdapter(int layoutResId, int brid) {
        super(layoutResId, brid);
    }

    @Override
    protected void convert(BindingViewHolder helper, NimUserInfo item) {
        super.convert(helper, item);
        ListItemShareFansBinding binding = (ListItemShareFansBinding) helper.getBinding();
        ImageLoadUtils.loadImage(mContext, item.getAvatar(), binding.imageView);
        String name = item.getName();
        String account = item.getAccount();
        binding.tvItemName.setText(name);
        binding.buInvite.setText("赠送");

        binding.buInvite.setOnClickListener(view -> {
            if (iGiveAction != null)
                iGiveAction.onGiveEvent(account, name);
        });
    }

    public IGiveAction iGiveAction;

    public interface IGiveAction {
        void onGiveEvent(String uid, String userName);
    }
}
