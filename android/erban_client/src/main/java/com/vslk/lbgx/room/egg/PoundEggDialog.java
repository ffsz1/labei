package com.vslk.lbgx.room.egg;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.gift.ConchGiftInfo;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.room.lotterybox.ILotteryBoxCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.egg.dialog.CashDialog;
import com.vslk.lbgx.room.egg.dialog.ConchResultDialog;
import com.vslk.lbgx.room.egg.dialog.PoundEggRankListDialog;
import com.vslk.lbgx.room.egg.dialog.PoundEggWinPrizeRecordDialog;
import com.vslk.lbgx.room.egg.dialog.PoundPrizePoolDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 吹泡泡dialog
 * Created by zwk on 2018/6/6.
 */
public class PoundEggDialog extends BaseDialogFragment implements View.OnClickListener {

    boolean isOpenAnimation = true;
    @BindView(R.id.iv_pound_egg_closs)
    ImageView ivPoundEggCloss;
    @BindView(R.id.iv_lottery_rank)
    TextView ivLotteryRank;
    @BindView(R.id.iv_lottery_prize_pool)
    TextView ivLotteryPrizePool;
    @BindView(R.id.iv_lottery_rule)
    TextView ivLotteryRule;
    @BindView(R.id.iv_lottery_win_prize_record)
    TextView ivLotteryWinPrizeRecord;
//    @BindView(R.id.iv_lottery_box_box)
//    ImageView ivLotteryBoxBox;
    @BindView(R.id.bu_lottery_box_one)
    Button buLotteryBoxOne;
    @BindView(R.id.bu_lottery_box_ten)
    Button buLotteryBoxTen;
    @BindView(R.id.bu_lottery_box_fifty)
    Button buLotteryBoxFifty;
    @BindView(R.id.ll_lottery_button)
    LinearLayout llLotteryButton;
    @BindView(R.id.tv_gold_count)
    TextView tvGoldCount;
    @BindView(R.id.tv_lottery_box_recharge)
    TextView tvLotteryBoxRecharge;
    Unbinder unbinder;
    private ILotteryBoxCore iLotteryBoxCore;


    private int goldNum;


    public static final String TAG = "PoundEggDialog";
    /**
     * 134
     * 大于520价值才发送礼物
     */
    private final int MAX = 520;

    /**
     * 吹泡泡次数类别 1为1次、2为10次、3为自动开（循环1次1次的开）,4为50次
     */
    private int lotteryType = 0;

    private final int FIFTY = 50;
    private final int TEN = 10;
    private ConchResultDialog conchResultDialog;


    public static PoundEggDialog newOnlineUserListInstance() {
        return newInstance();
    }

    /**
     * 根据：
     * http://blog.sina.com.cn/s/blog_5da93c8f0102x5zl.html
     * https://blog.csdn.net/freelander_j/article/details/52925745
     * 修复IllegalStateException: Can not perform this action after onSaveInstanceState
     *
     * @param transaction
     * @param tag
     * @return
     */
    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    private static PoundEggDialog newInstance() {
        PoundEggDialog lotteryBoxDialog = new PoundEggDialog();
        return lotteryBoxDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        //设置dialog的动画
        lp.windowAnimations = R.style.dialgBottomDialogAnimation;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        CoreManager.removeClient(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iLotteryBoxCore = CoreManager.getCore(ILotteryBoxCore.class);
        View view = inflater.inflate(R.layout.dialog_pound_egg, null);
        unbinder = ButterKnife.bind(this, view);
        setCancelable(true);
        initView();
        return view;
    }


    private void initView() {
        iLotteryBoxCore = CoreManager.getCore(ILotteryBoxCore.class);
        isOpenAnimation = (boolean) SpUtils.get(getContext(), Constants.IS_OPEN_ANIMATION_EFFECT, true);
        initEvent();
        CoreManager.getCore(IPayCore.class).loadDianDianCoinInfos();
        String count = getArguments().getString("count");
        tvGoldCount.setText(count);
        goldNum = Integer.parseInt(count);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_lottery_win_prize_record:
                PoundEggWinPrizeRecordDialog poundEggWinPrizeRecordDialog = PoundEggWinPrizeRecordDialog.newInstance("中奖记录");
                poundEggWinPrizeRecordDialog.show(getFragmentManager());
                break;
            case R.id.iv_lottery_prize_pool:
                PoundPrizePoolDialog poundPrizePoolDialog = PoundPrizePoolDialog.newInstance("本期礼物");
                poundPrizePoolDialog.show(getFragmentManager());
                break;
            case R.id.bu_lottery_box_one:
                startLottery(1);
                break;
            case R.id.bu_lottery_box_ten:
                startLottery(2);
                break;
            case R.id.bu_lottery_box_fifty:
                //4-500，5-100
                startLottery(4);
                break;
            case R.id.tv_lottery_box_recharge:
//                WalletActivity.start(getContext());
//                finishLottery();
                dismiss();
                break;
            case R.id.iv_pound_egg_closs:
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_lottery_rule:
                CashDialog.newInstance().show(getFragmentManager());
                break;

            case R.id.iv_lottery_rank:
                PoundEggRankListDialog bigListDataDialog = PoundEggRankListDialog.newContributionListInstance(getContext());
                bigListDataDialog.show(getFragmentManager());
                break;
            default:

        }
    }


    private void setButtonLayoutParams(Button buLotteryDialogOk, int oldVerb, int newVerb) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) buLotteryDialogOk.getLayoutParams();
        if (oldVerb >= 0) {
            layoutParams.removeRule(oldVerb);
        }
        layoutParams.addRule(newVerb);
        buLotteryDialogOk.setLayoutParams(layoutParams);
    }


    OkHttpManager.MyCallBack<ServiceResult<ConchGiftInfo>> lotteryCallBack = new OkHttpManager.MyCallBack<ServiceResult<ConchGiftInfo>>() {
        @Override
        public void onError(Exception e) {
            onLotteryError("网络异常");
        }

        @Override
        public void onResponse(ServiceResult<ConchGiftInfo> response) {
            if (response != null) {
                if (response.isSuccess()) {
                    onLotterySuccess(response.getData());
                    sub(response.getData().getConchNum());
                } else if (response.getCode() == 2103) {
                    finishLottery();
                    if (getContext() != null) {
                        SingleToastUtil.showShortToast(response.getMessage());
                    }
                } else {
                    finishLottery();
                    if (getContext() != null) {
                        SingleToastUtil.showShortToast(response.getMessage());
                    }
                }
            } else {
                onLotteryError("数据异常");
            }
        }
    };

    private void initEvent() {
        buLotteryBoxOne.setOnClickListener(this);
        buLotteryBoxTen.setOnClickListener(this);
        buLotteryBoxFifty.setOnClickListener(this);
        tvLotteryBoxRecharge.setOnClickListener(this);
        ivLotteryRule.setOnClickListener(this);
        ivLotteryWinPrizeRecord.setOnClickListener(this);
        ivLotteryPrizePool.setOnClickListener(this);
        ivLotteryRank.setOnClickListener(this);
        ivPoundEggCloss.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 开始吹泡泡
     *
     * @param type 次数类别 1为1次、2为10次、3为自动开（循环1次1次的开）,4为50次
     */
    private void startLottery(int type) {
        lotteryType = type;
        doLottery();
    }

    /**
     * 吹泡泡
     */
    private void doLottery() {
        iLotteryBoxCore.lotteryRequest(lotteryType, lotteryCallBack);
    }


    /**
     * 显示draw礼物结果
     *
     * @param conchGiftInfo
     */
    private void showDrawResultDialog(ConchGiftInfo conchGiftInfo) {

        if (conchResultDialog != null) {
            conchResultDialog.setNewData(conchGiftInfo);
        } else {
            conchResultDialog = new ConchResultDialog(conchGiftInfo);
            Bundle bundle = new Bundle();
            String str = tvGoldCount.getText().toString();
            bundle.putString("gold", str);
            conchResultDialog.setArguments(bundle);
            conchResultDialog.show(getFragmentManager(), "many");
            conchResultDialog.setListener(new ConchResultDialog.EggClickListener() {
                @Override
                public void close() {
                    //如果当前是自动吹泡泡模式，继续开
                    conchResultDialog = null;
                    finishLottery();
                }

                @Override
                public void again(int type) {
                    lotteryType = type;
                    startLottery(lotteryType);
                }

                @Override
                public void recharge() {
                    dismiss();
                }
            });
        }
    }

    /**
     * 结束吹泡泡
     */
    private void finishLottery() {
        //自动吹泡泡模式下，结束吹泡泡的时候要恢复按钮形状
        lotteryType = 0;
    }

    public void onLotterySuccess(ConchGiftInfo conchGiftInfo) {
        List<EggGiftInfo> gifts = conchGiftInfo.getGiftList();
        //dialog销毁之后不执行方法
        if (ListUtils.isListEmpty(gifts)) {
            finishLottery();
            return;
        }
        showDrawResultDialog(conchGiftInfo);
        setLotteryGiftInfo(conchGiftInfo);
    }


    public void sub(long gold) {
        if (tvGoldCount != null) {
            String str = tvGoldCount.getText().toString();
            if (str.isEmpty()) {
                eggListener.upCallCount((int) gold);
                tvGoldCount.setText(String.valueOf(gold));
                if (conchResultDialog != null) conchResultDialog.upHlCount((int) gold);
            } else {
                if (gold < Integer.parseInt(str)) {
                    tvGoldCount.setText(String.valueOf(gold));
                    if (conchResultDialog != null) conchResultDialog.upHlCount((int) gold);
                    eggListener.upCallCount((int) gold);
                }
            }
        }
    }

    private int calculateAllGiftCount(List<EggGiftInfo> giftInfos) {
        int count = 0;
        for (int i = 0; i < giftInfos.size(); i++) {
            count += giftInfos.get(i).getGiftNum();
        }
        return count;
    }

    /***
     * 更新礼物信息 普通礼物和神秘礼物
     * @param conchGiftInfo
     */
    private void setLotteryGiftInfo(ConchGiftInfo conchGiftInfo) {
        List<EggGiftInfo> gifts = conchGiftInfo.getGiftList();
        if (ListUtils.isListEmpty(gifts)) {
            return;
        }
        List<GiftInfo> giftInfos = CoreManager.getCore(IGiftCore.class).getGiftInfosByType2And3();
        if (ListUtils.isListEmpty(giftInfos)) {
            return;
        }
        //把gift的索引标记起来
        SparseIntArray map = new SparseIntArray();
        for (int i = 0; i < giftInfos.size(); i++) {
            GiftInfo giftInfo = giftInfos.get(i);
            if (giftInfo == null) {
                continue;
            }
            map.put(giftInfo.getGiftId(), i);
        }

        GiftInfo markGift = new GiftInfo();
        int markCount = 0;
        //把获得的礼物加进集合
        boolean isRequest = false;
        for (int i = 0; i < gifts.size(); i++) {
            EggGiftInfo gift = gifts.get(i);
            Integer integer = map.get(gift.getGiftId());
            if (gift.getGiftType() == 3) {
                isRequest = true;
            }
            if (integer == null) {
                continue;
            }
            int index = integer;
            GiftInfo giftInfo = giftInfos.get(index);
            if (giftInfo.getGoldPrice() >= MAX && giftInfo.getGoldPrice() > markGift.getGoldPrice()) {
                LogUtil.d(TAG, "寻找最大礼物" + giftInfo.toString());
                markGift = giftInfo;
                markCount = gift.getGiftNum();
            }
            giftInfo.setUserGiftPurseNum(giftInfo.getUserGiftPurseNum() + gift.getGiftNum());
            if (markGift.getGoldPrice() >= MAX && markCount > 0) {
                CoreManager.getCore(IGiftCore.class).sendLotteryMeg(markGift, markCount);
            }
        }
        if (isRequest) {
            CoreManager.getCore(IGiftCore.class).requestGiftInfos();
        }
    }


    public void onLotteryError(String msg) {
        finishLottery();
        if (getContext() != null) {
            SingleToastUtil.showShortToast(msg);
        }
    }

    private IEggListener eggListener;

    public void setEggListener(IEggListener eggListener) {
        this.eggListener = eggListener;
    }

    interface IEggListener {
        void upCallCount(int count);
    }
}
