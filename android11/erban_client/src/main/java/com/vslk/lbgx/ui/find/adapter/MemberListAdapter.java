package com.vslk.lbgx.ui.find.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;

import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.ADMIN;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.MANAGER;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述        管理成员等界面适配器
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class MemberListAdapter extends BaseQuickAdapter<MemberInfo, BaseViewHolder> {

    private int roleStatus;
    private boolean isManager;

    public MemberListAdapter(boolean isManager) {
        super(R.layout.item_member_info_manager);
        this.isManager = isManager;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberInfo item) {
        helper.setText(R.id.nickName, item.getNike())
                .addOnClickListener(R.id.remove)
                .addOnClickListener(R.id.appointment);

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

        if (isManager) {
            helper.setVisible(R.id.appointment, false);
            if (item.getRoleStatus() == MANAGER) {
                helper.setVisible(R.id.remove, false);
            } else if (item.getRoleStatus() == ADMIN) {
                if (roleStatus == ADMIN) {
                    helper.setVisible(R.id.remove, false);
                }
            } else {
                helper.setVisible(R.id.remove, true);
            }
        } else {
            helper.setVisible(R.id.appointment, item.getRoleStatus() == 3)
                    .setVisible(R.id.remove, item.getRoleStatus() == 2);
            ImageView appointment = helper.getView(R.id.appointment);
            appointment.setSelected(item.isCheck());
        }

        ImageView avatar = helper.getView(R.id.avatar);
        ImageLoadUtils.loadCircleImage(avatar.getContext(), item.getAvatar(), avatar, R.drawable.ic_default_avatar);
    }

    public void setRoleStatus(int roleStatus) {
        this.roleStatus = roleStatus;
    }

}
