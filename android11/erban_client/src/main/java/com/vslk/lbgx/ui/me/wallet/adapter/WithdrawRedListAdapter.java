package com.vslk.lbgx.ui.me.wallet.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedListInfo;

/**
 * <p> 红包提现展示列表 </p>
 * Created by Administrator on 2017/11/20.
 */
public class WithdrawRedListAdapter extends BaseQuickAdapter<WithdrawRedListInfo, BaseViewHolder> {

    public WithdrawRedListAdapter() {
        super(R.layout.withdraw_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WithdrawRedListInfo withdrwaListInfo) {
        if (withdrwaListInfo == null) {
            return;
        }

        baseViewHolder.setText(R.id.list_name, "¥" + withdrwaListInfo.getPacketNum());

        ImageView bg = baseViewHolder.itemView.findViewById(R.id.select_withdraw);
        bg.setSelected(withdrwaListInfo.isSelected);
        TextView gold = baseViewHolder.getView(R.id.list_name);
        gold.setSelected(withdrwaListInfo.isSelected);
        TextView amount = baseViewHolder.getView(R.id.tv_wd_money);
        amount.setSelected(withdrwaListInfo.isSelected);

    }
}
