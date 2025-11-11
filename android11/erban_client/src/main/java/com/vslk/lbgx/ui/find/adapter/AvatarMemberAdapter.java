package com.vslk.lbgx.ui.find.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/29
 * 描述        显示家族成员头像
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class AvatarMemberAdapter extends BaseQuickAdapter<MemberInfo, BaseViewHolder> {

    public AvatarMemberAdapter() {
        super(R.layout.member_avatar_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberInfo item) {
        RoundedImageView avatar = helper.getView(R.id.avatar);
        ImageLoadUtils.loadCircleImage(avatar.getContext(), item.getAvatar(), avatar, R.drawable.ic_default_avatar);
    }
}
