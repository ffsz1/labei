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

import com.blankj.utilcode.util.ConvertUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.ConchGiftInfo;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.vslk.lbgx.room.avroom.adapter.LotteryGiftAdapter;

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
    Button buLotteryBoxOne;
    @BindView(R.id.bu_lottery_box_ten)
    Button buLotteryBoxTen;
    @BindView(R.id.bu_lottery_box_fifty)
    Button buLotteryBoxFifty;
    @BindView(R.id.ll_lottery_button)
    LinearLayout llLotteryButton;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    Unbinder unbinder;
    @BindView(R.id.tv_gold_count)
    TextView tvGoldCount;
    @BindView(R.id.tv_lottery_box_recharge)
    TextView tvLotteryBoxRecharge;

    private LotteryGiftAdapter lotteryGiftAdapter;
    private ConchGiftInfo conchGiftInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
    }

    public ConchResultDialog(ConchGiftInfo conchGiftInfo) {
        this.conchGiftInfo = conchGiftInfo;
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
        rvLotteryDialog.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.right = ConvertUtils.dp2px(10);
                outRect.bottom = ConvertUtils.dp2px(10);
            }
        });
        rvLotteryDialog.setAdapter(lotteryGiftAdapter);
        if (conchGiftInfo != null) {
            setNewData(conchGiftInfo);
        }
        ivPoundEggCloss.setOnClickListener(v -> {
            listener.close();
            dismiss();
        });
        buLotteryBoxOne.setOnClickListener(this);
        buLotteryBoxTen.setOnClickListener(this);
        buLotteryBoxFifty.setOnClickListener(this);
        String gold = getArguments().getString("gold");
        tvGoldCount.setText(gold);
        tvLotteryBoxRecharge.setOnClickListener(v -> {
            dismiss();
            listener.recharge();
        });
    }

    public void setNewData(ConchGiftInfo conchGiftInfo) {
        List<EggGiftInfo> giftList = conchGiftInfo.getGiftList();
        EggGiftInfo ticketGift = null;
        Iterator<EggGiftInfo> sListIterator = giftList.iterator();
        while (sListIterator.hasNext()) {
            EggGiftInfo giftInfo = sListIterator.next();
            if (giftInfo.getGiftId() == conchGiftInfo.getTicketId()) {
                ticketGift = giftInfo;
                sListIterator.remove();
            }
        }

        tvTicket.setText(ticketGift == null ? "" : "额外获得" + ticketGift.getGiftNum() + "人气票");
        lotteryGiftAdapter.setNewData(giftList);
    }

    public void upHlCount(int count) {
        tvGoldCount.setText(count + "");
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
                listener.again(4);
                break;

        }
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

        void recharge();
    }

    public void setListener(EggClickListener listener) {
        this.listener = listener;
    }
}
