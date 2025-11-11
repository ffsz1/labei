package com.vslk.lbgx.ui.me.wallet.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.pay.bean.ChargeBean;

import java.util.Objects;

/**
 * <p> 充值adapter </p>
 * Created by Administrator on 2017/11/20.
 */
public class ChargeAdapter extends BaseQuickAdapter<ChargeBean, BaseViewHolder> {

    public ChargeAdapter() {
        super(R.layout.list_item_charge);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChargeBean chargeBean) {
        if (chargeBean == null) {
            return;
        }
        baseViewHolder.setText(R.id.item_charge_gold, chargeBean.prodName)
                .setText(R.id.item_charge_money, mContext.getString(R.string.charge_number, chargeBean.money))
                .setText(R.id.item_charge_warning, chargeBean.prodDesc)
                .setVisible(R.id.item_charge_warning, !Objects.equals(chargeBean.prodDesc, ""));

        ImageView bg = baseViewHolder.itemView.findViewById(R.id.item_charge_bg);
        bg.setSelected(chargeBean.isSelected);
        TextView gold = baseViewHolder.getView(R.id.item_charge_gold);
        gold.setSelected(chargeBean.isSelected);
        TextView amount = baseViewHolder.getView(R.id.item_charge_money);
        amount.setSelected(chargeBean.isSelected);
    }

}
