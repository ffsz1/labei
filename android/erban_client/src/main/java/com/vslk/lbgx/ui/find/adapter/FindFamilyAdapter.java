package com.vslk.lbgx.ui.find.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/20
 * 描述        发现页家族界面适配器
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FindFamilyAdapter extends BaseQuickAdapter<FamilyInfo, BaseViewHolder> {

    public FindFamilyAdapter() {
        super(R.layout.family_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyInfo item) {
        helper.setText(R.id.family_name, item.getFamilyName())
                .setText(R.id.gold_count, item.getPrestige() + "")
                .setText(R.id.member, item.getMember() + "个成员")
                .setText(R.id.family_notice, item.getFamilyNotice())
                .setText(R.id.ranking, helper.getAdapterPosition() + 1 + "");

        RelativeLayout container = helper.getView(R.id.rootView);
        if (helper.getAdapterPosition() == 0) {
            container.setBackgroundResource(R.drawable.icon_find_family_item_bg_first);
        } else {
            container.setBackgroundResource(R.drawable.icon_find_family_item_bg_other);
        }
        avatars(helper, item);
    }

    private void avatars(BaseViewHolder helper, FamilyInfo item) {

        ImageView avatar = helper.getView(R.id.avatar);
        ImageLoadUtils.loadCircleImage(avatar.getContext(), item.getFamilyLogo(), avatar, R.drawable.ic_default_avatar);

        ImageView avatarFirst = helper.getView(R.id.avatar_first);
        ImageView avatarSecond = helper.getView(R.id.avatar_second);
        ImageView avatarThird = helper.getView(R.id.avatar_third);
        if (!ListUtils.isListEmpty(item.getFamilyUsersDTOS())) {
            int size = item.getFamilyUsersDTOS().size();
            if (size == 1) {
                avatarFirst.setVisibility(View.VISIBLE);
                avatarSecond.setVisibility(View.GONE);
                avatarThird.setVisibility(View.GONE);
                ImageLoadUtils.loadCircleImage(avatarFirst.getContext(), item.getFamilyUsersDTOS().get(0).getAvatar(),
                        avatarFirst, R.drawable.ic_default_avatar);
            } else if (size == 2) {
                avatarFirst.setVisibility(View.VISIBLE);
                avatarSecond.setVisibility(View.VISIBLE);
                avatarThird.setVisibility(View.GONE);
                ImageLoadUtils.loadCircleImage(avatarFirst.getContext(), item.getFamilyUsersDTOS().get(0).getAvatar(),
                        avatarFirst, R.drawable.ic_default_avatar);
                avatarSecond.setVisibility(View.VISIBLE);
                ImageLoadUtils.loadCircleImage(avatarSecond.getContext(), item.getFamilyUsersDTOS().get(1).getAvatar(),
                        avatarSecond, R.drawable.ic_default_avatar);
            } else if (size >= 3) {
                avatarFirst.setVisibility(View.VISIBLE);
                avatarSecond.setVisibility(View.VISIBLE);
                avatarFirst.setVisibility(View.VISIBLE);
                ImageLoadUtils.loadCircleImage(avatarFirst.getContext(), item.getFamilyUsersDTOS().get(0).getAvatar(),
                        avatarFirst, R.drawable.ic_default_avatar);
                avatarSecond.setVisibility(View.VISIBLE);
                ImageLoadUtils.loadCircleImage(avatarSecond.getContext(), item.getFamilyUsersDTOS().get(1).getAvatar(),
                        avatarSecond, R.drawable.ic_default_avatar);
                avatarThird.setVisibility(View.VISIBLE);
                ImageLoadUtils.loadCircleImage(avatarThird.getContext(), item.getFamilyUsersDTOS().get(2).getAvatar(),
                        avatarThird, R.drawable.ic_default_avatar);
            }
        } else {
            avatarFirst.setVisibility(View.GONE);
            avatarSecond.setVisibility(View.GONE);
            avatarThird.setVisibility(View.GONE);
        }
    }

}
