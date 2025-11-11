package com.vslk.lbgx.room.egg.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.ConchGiftInfo;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.vslk.lbgx.room.avroom.adapter.LotteryGiftAdapter;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 捡海螺结果显示dialog
 */

@SuppressLint("ValidFragment")
public class ConchResultDialog extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.iv_pound_egg_closs)
    ImageView ivPoundEggCloss;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_ticket)
    TextView tvTicket;
    @BindView(R.id.rv_lottery_dialog)
    RecyclerView rvLotteryDialog;
    @BindView(R.id.bu_lottery_box_one)
    ImageView buLotteryBoxOne;
    @BindView(R.id.bu_lottery_box_ten)
    ImageView buLotteryBoxTen;
    @BindView(R.id.bu_lottery_box_fifty)
    ImageView buLotteryBoxFifty;
    @BindView(R.id.ll_lottery_button)
    LinearLayout llLotteryButton;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    Unbinder unbinder;
    @BindView(R.id.tv_gold_count)
    TextView tvGoldCount;
    @BindView(R.id.tv_lottery_box_recharge)
    TextView tvLotteryBoxRecharge;
    @BindView(R.id.fra_main)
    RelativeLayout fraMain;

    private LotteryGiftAdapter lotteryGiftAdapter;
    private List<EggGiftInfo> gifts;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
    }

    public ConchResultDialog(List<EggGiftInfo> gifts) {
        this.gifts = gifts;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_many_egg, null);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window;
        if (dialog != null && (window = dialog.getWindow()) != null) {
            int w = ViewGroup.LayoutParams.MATCH_PARENT;
            int h = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setLayout(w, h);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setBackgroundDrawableResource(R.color.transparent);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                listener.close();
            }
            return false;
        });
    }

    private void init() {
        rvLotteryDialog.setLayoutManager(new GridLayoutManager(getContext(), 3));
        lotteryGiftAdapter = new LotteryGiftAdapter(getContext());
        rvLotteryDialog.setAdapter(lotteryGiftAdapter);
        setGifts(gifts);
        ivPoundEggCloss.setOnClickListener(v -> {
            listener.close();
            dismiss();
        });
        buLotteryBoxOne.setOnClickListener(this);
        buLotteryBoxTen.setOnClickListener(this);
        buLotteryBoxFifty.setOnClickListener(this);
        tvLotteryBoxRecharge.setOnClickListener(v -> {
            Toast.makeText(getContext(), "敬请期待", Toast.LENGTH_LONG).show();
        });
        String gold = getArguments().getString("gold");
        tvGoldCount.setText(gold);

        fraMain.setOnClickListener(this);
    }

    public void setGifts(List<EggGiftInfo> gifts) {
        if (lotteryGiftAdapter != null) {
            lotteryGiftAdapter.setNewData(gifts);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bu_lottery_box_one:
                listener.again(1);
                break;
            case R.id.bu_lottery_box_ten:
                listener.again(2);
                break;
            case R.id.bu_lottery_box_fifty:
                listener.again(5);
                break;
            case R.id.fra_main:
                try {
                    listener.close();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public void upGold(double gold) {
        tvGoldCount.setText(this.getString(R.string.gold_num_text, gold));
    }

    private EggClickListener listener;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface EggClickListener {
        void close();

        void again(int type);
    }

    public void setListener(EggClickListener listener) {
        this.listener = listener;
    }
}
