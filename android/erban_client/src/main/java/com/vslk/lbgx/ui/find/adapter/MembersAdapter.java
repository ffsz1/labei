package com.vslk.lbgx.ui.find.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述        家族成员列表适配器
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class MembersAdapter extends BaseQuickAdapter<MemberInfo, BaseViewHolder> {

    public MembersAdapter() {
        super(R.layout.item_member_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberInfo item) {
        helper.setText(R.id.nickName, item.getNike())
                .setVisible(R.id.position, item.getRoleStatus() == 1 || item.getRoleStatus() == 2)
                .setGone(R.id.container, false);

        DrawableTextView jurisdiction = helper.getView(R.id.position);
        if (item.getRoleStatus() == 1) {
            jurisdiction.setText("族长");
            jurisdiction.changeSoildColor(jurisdiction.getContext().getResources().getColor(R.color.color_ffe6a3f4));
        } else if (item.getRoleStatus() == 2) {
            jurisdiction.setText("副族长");
            jurisdiction.changeSoildColor(jurisdiction.getContext().getResources().getColor(R.color.color_ff91d9ee));
        } else {
            jurisdiction.setVisibility(View.INVISIBLE);
        }

        ImageView avatar = helper.getView(R.id.avatar);
        ImageLoadUtils.loadCircleImage(avatar.getContext(), item.getAvatar(), avatar, R.drawable.ic_default_avatar);
    }
}
