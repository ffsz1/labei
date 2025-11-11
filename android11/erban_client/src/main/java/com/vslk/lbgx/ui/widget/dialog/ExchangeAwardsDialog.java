package com.vslk.lbgx.ui.widget.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.pay.bean.ExchangeAwardInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ExchangeAwardsDialog extends BaseDialogFragment {

    @BindView(R.id.iv_awards)
    ImageView ivAwards;
    @BindView(R.id.tv_awards_content)
    TextView tvAwardsContent;
    @BindView(R.id.bu_awards_ok)
    Button buAwardsOk;
    Unbinder unbinder;
    @BindView(R.id.tv_awards_title)
    TextView tvAwardsTitle;
    @BindView(R.id.iv_awards_close)
    ImageView ivAwardsClose;
    private ExchangeAwardInfo data;

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    public static ExchangeAwardsDialog newInstance() {
        ExchangeAwardsDialog exchangeAwardsDialog = new ExchangeAwardsDialog();
        return exchangeAwardsDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        CoreManager.removeClient(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = inflater.inflate(R.layout.dialog_exchange_awards, window.findViewById(android.R.id.content), false);
        unbinder = ButterKnife.bind(this, view);
        buAwardsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ivAwardsClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        initData();
        return view;
    }

    private void initData() {
        if (data == null)
            return;

        String drawMsg = data.getDrawMsg();
        String drawUrl = data.getDrawUrl();
        if ("0".equals(drawMsg) || "0".equals(drawUrl)) {
            ivAwards.setBackground(null);
            ivAwards.setImageResource(R.drawable.dh_nothing);
            tvAwardsTitle.setVisibility(View.GONE);
            tvAwardsContent.setText("很遗憾，没有爆中");
        } else {
            ivAwards.setBackgroundResource(R.drawable.dh_awards_bg);
            ImageLoadUtils.loadImage(getContext(), drawUrl, ivAwards);
            tvAwardsTitle.setVisibility(View.VISIBLE);
            tvAwardsContent.setText(drawMsg);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setData(ExchangeAwardInfo data) {
        this.data = data;
    }
}
