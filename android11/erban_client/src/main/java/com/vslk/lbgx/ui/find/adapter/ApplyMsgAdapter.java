package com.vslk.lbgx.ui.find.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.ui.widget.LevelView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.find.family.bean.ApplyMsgInfo;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述        家族申请信息适配器
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class ApplyMsgAdapter extends BaseQuickAdapter<ApplyMsgInfo, BaseViewHolder> {

    public ApplyMsgAdapter() {
        super(R.layout.item_member_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplyMsgInfo item) {
        helper.setText(R.id.nickName, item.getNick())
                .setVisible(R.id.level_view, item.getLevel() > 0)
                .setGone(R.id.btn_container, item.getStatus() == 0)
                .setVisible(R.id.status, item.getStatus() != 0)
                .setText(R.id.status, item.getStatus() == 1 ? "已同意" : item.getStatus() == 2 ? "已忽略" : "已自动退出")
                .addOnClickListener(R.id.confirm)
                .addOnClickListener(R.id.ignore);

        //设置用户等级
        if (item.getLevel() > 0) {
            LevelView levelView = helper.getView(R.id.level_view);
            levelView.setExperLevel(item.getLevel());
        }

        //设置头像
        ImageView avatar = helper.getView(R.id.avatar);
        ImageLoadUtils.loadCircleImage(avatar.getContext(), item.getAvatar(), avatar, R.drawable.ic_default_avatar);
    }
}
