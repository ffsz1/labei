package com.vslk.lbgx.ui.home.adpater;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;
import com.vslk.lbgx.utils.ImageLoadUtils;

import java.util.List;

public class UserAlbumAdapter extends BaseQuickAdapter<UserPhoto, BaseViewHolder> {
    public UserAlbumAdapter(@Nullable List<UserPhoto> data) {
        super(R.layout.item_user_info_album, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserPhoto item) {
        ImageLoadUtils.loadImage(mContext, item.getPhotoUrl(), helper.getView(R.id.iv_album));
    }
}
