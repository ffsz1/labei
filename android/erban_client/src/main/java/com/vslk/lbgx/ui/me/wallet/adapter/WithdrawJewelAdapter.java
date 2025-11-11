package com.vslk.lbgx.ui.me.wallet.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrwaListInfo;

/**
 * <p>  钻石提现界面</p>
 *
 * @author Administrator
 * @date 2017/11/21
 */
public class WithdrawJewelAdapter extends BaseQuickAdapter<WithdrwaListInfo, BaseViewHolder> {

    public WithdrawJewelAdapter() {
        super(R.layout.withdraw_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WithdrwaListInfo withdrwaListInfo) {
        if (withdrwaListInfo == null) {
            return;
        }
        try {
            String[] split = withdrwaListInfo.cashProdName.split("钻=");
            baseViewHolder.setText(R.id.list_name, split[1]);
            baseViewHolder.setText(R.id.tv_wd_money, split[0] + "钻石");
        } catch (Exception e) {
            baseViewHolder.setText(R.id.list_name, withdrwaListInfo.cashProdName);
        }

        ImageView bg = baseViewHolder.itemView.findViewById(R.id.select_withdraw);
        bg.setSelected(withdrwaListInfo.isSelected);
        TextView gold = baseViewHolder.getView(R.id.list_name);
        gold.setSelected(withdrwaListInfo.isSelected);
        TextView amount = baseViewHolder.getView(R.id.tv_wd_money);
        amount.setSelected(withdrwaListInfo.isSelected);
        TextView tvWd = baseViewHolder.getView(R.id.tv_wd_money);
        tvWd.setEnabled(withdrwaListInfo.isWd());

        //背景
        LinearLayout container = baseViewHolder.getView(R.id.container);
        container.setSelected(withdrwaListInfo.isSelected);
        TextView tvDiamond = baseViewHolder.getView(R.id.tv_diamond);
        tvDiamond.setSelected(withdrwaListInfo.isSelected);
    }
}
