package com.vslk.lbgx.room.egg;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.IPayCoreClient;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.room.lotterybox.ILotteryBoxCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.egg.dialog.CashDialog;
import com.vslk.lbgx.room.egg.dialog.ConchResultDialog;
import com.vslk.lbgx.room.egg.dialog.PoundEggRankListDialog;
import com.vslk.lbgx.room.egg.dialog.PoundEggWinPrizeRecordDialog;
import com.vslk.lbgx.room.egg.dialog.PoundPrizePoolDialog;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;

import java.util.List;
import java.util.Map;

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
    @BindView(R.id.iv_lottery_box_box)
    ImageView ivLotteryBoxBox;
    @BindView(R.id.bu_lottery_box_one)
    ImageView buLotteryBoxOne;
    @BindView(R.id.bu_lottery_box_ten)
    ImageView buLotteryBoxTen;
    @BindView(R.id.bu_lottery_box_fifty)
    ImageView buLotteryBoxFifty;
    @BindView(R.id.ll_lottery_button)
    LinearLayout llLotteryButton;
    @BindView(R.id.tv_gold_count)
    TextView tvGoldCount;
    @BindView(R.id.tv_lottery_box_recharge)
    TextView tvLotteryBoxRecharge;
    Unbinder unbinder;
    @BindView(R.id.fra_main)
    RelativeLayout fraMain;
    @BindView(R.id.bu_lottery_box_open)
    ImageView buLotteryBoxOpen;
    private ILotteryBoxCore iLotteryBoxCore;


    private double goldNum;

    public static final String TAG = "PoundEggDialog";
    /**
     * 134
     * 大于520价值才发送礼物
     */
    private final int MAX = 1000;
    /**
     * 吹泡泡次数类别 1为1次、2为10次、3为自动开（循环1次1次的开）,4为50次
     */
    private int lotteryType = 0;

    private final int HUNDRED = 100;
    private final int TEN = 10;
    private ConchResultDialog conchResultDialog;

    private int openBoxType = 1;


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
        iLotteryBoxCore = CoreManager.getCore(ILotteryBoxCore.class);
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_pound_egg, window.findViewById(android.R.id.content), false);
        unbinder = ButterKnife.bind(this, view);
        setCancelable(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.0f;
        window.setAttributes(layoutParams);
        initView();
        return view;
    }


    private void initView() {
        iLotteryBoxCore = CoreManager.getCore(ILotteryBoxCore.class);
        isOpenAnimation = (boolean) SpUtils.get(getContext(), Constants.IS_OPEN_ANIMATION_EFFECT, true);
        initEvent();
        CoreManager.getCore(IPayCore.class).getWalletInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        CoreManager.getCore(IPayCore.class).loadDianDianCoinInfos();
        WalletInfo walletInfo = CoreManager.getCore(IPayCore.class).getCurrentWalletInfo();
        if (walletInfo != null) {
            goldNum = walletInfo.getGoldNum();
            tvGoldCount.setText(this.getString(R.string.gold_num_text, goldNum));
        }
        tvLotteryBoxRecharge.setText("+开心");
        upGold();

    }

    private void upGold() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("Cache-Control", "no-cache");
        OkHttpManager.getInstance().getRequest(UriProvider.getWalletInfos(), params, new OkHttpManager.MyCallBack<ServiceResult<WalletInfo>>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(ServiceResult<WalletInfo> response) {
                if (null != response && response.isSuccess()) {
                    updateWalletInfo(response.getData());
                }
            }
        });
    }


    private void showDrawResultDialog(List<EggGiftInfo> gifts) {
        if (conchResultDialog != null) {
            conchResultDialog.setGifts(gifts);
        } else {
            conchResultDialog = new ConchResultDialog(gifts);
            Bundle bundle = new Bundle();
            String str = tvGoldCount.getText().toString();
            bundle.putString("gold", str);
            conchResultDialog.setArguments(bundle);
            conchResultDialog.show(getActivity().getSupportFragmentManager(), "many");
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
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_lottery_win_prize_record:
                PoundEggWinPrizeRecordDialog poundEggWinPrizeRecordDialog = PoundEggWinPrizeRecordDialog.newInstance("中奖记录");
                poundEggWinPrizeRecordDialog.show(getFragmentManager());
//                CommonWebViewActivity.start(getContext(), WebUrl.GIFT_RECORD_URL);
                break;
            case R.id.iv_lottery_prize_pool:
                PoundPrizePoolDialog poundPrizePoolDialog = PoundPrizePoolDialog.newInstance("本期奖池");
                poundPrizePoolDialog.show(getFragmentManager());
                break;
            case R.id.bu_lottery_box_one:
                selectBoxType(1);
                break;
            case R.id.bu_lottery_box_ten:
                selectBoxType(2);
                break;
            case R.id.bu_lottery_box_fifty:
                selectBoxType(5);
                break;
            case R.id.bu_lottery_box_open:
                if (openBoxType < 0) {
                    Toast.makeText(getContext(), "请选择开盒子资料", Toast.LENGTH_LONG).show();
                    return;
                }
                startLottery(openBoxType);
                break;
            case R.id.tv_lottery_box_recharge:
//                Toast.makeText(getContext(), "敬请期待", Toast.LENGTH_LONG).show();
                WalletActivity.start(getContext());
//                finishLottery();
                break;

            case R.id.iv_lottery_rule:
//                CashDialog.newInstance().show(getFragmentManager());
                CommonWebViewActivity.start(getContext(), WebUrl.DRAW_RULE_URL);
                break;

            case R.id.iv_lottery_rank:
                PoundEggRankListDialog bigListDataDialog = PoundEggRankListDialog.newContributionListInstance(getContext());
                bigListDataDialog.show(getFragmentManager());
                break;
            case R.id.iv_pound_egg_closs:
            case R.id.fra_main:
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            //case R.id.tv_my_egg_record:
            //    ListDataDialog record = ListDataDialog.newPoundEggRecordInstance(getContext());
            //    record.show(getChildFragmentManager());
            //    break;
            default:

        }
    }

    private void selectBoxType(int type) {
        openBoxType = type;
        buLotteryBoxOne.setBackgroundResource(R.mipmap.lottery_x1_btn);
        buLotteryBoxTen.setBackgroundResource(R.mipmap.lottery_x10_btn);
        buLotteryBoxFifty.setBackgroundResource(R.mipmap.lottery_x50_btn);
        if (type == 1) {
            buLotteryBoxOne.setBackgroundResource(R.mipmap.lottery_x1_btn_on);
        } else if (type == 2) {
            buLotteryBoxTen.setBackgroundResource(R.mipmap.lottery_x10_btn_on);
        } else if (type == 5) {
            buLotteryBoxFifty.setBackgroundResource(R.mipmap.lottery_x50_btn_on);
        }
    }


    OkHttpManager.MyCallBack<ServiceResult<List<EggGiftInfo>>> lotteryCallBack = new OkHttpManager.MyCallBack<ServiceResult<List<EggGiftInfo>>>() {
        @Override
        public void onError(Exception e) {
            onLotteryError("网络异常");
        }

        @Override
        public void onResponse(ServiceResult<List<EggGiftInfo>> response) {
            if (response != null) {
                if (response.isSuccess()) {
                    onLotterySuccess(response.getData());
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
        fraMain.setOnClickListener(this);
        buLotteryBoxOpen.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (null != animationDrawable && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }

    }


    /**
     * 开始吹泡泡
     *
     * @param type 次数类别 1为1次、2为20次、3为自动开（循环1次1次的开）,4为50次, 5为100次
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
     * 结束吹泡泡
     */
    private void finishLottery() {
        lotteryType = 0;
    }

    public void onLotterySuccess(List<EggGiftInfo> gifts) {
        //dialog销毁之后不执行方法
        if (ListUtils.isListEmpty(gifts)) {
            finishLottery();
            return;
        }
        showFreeGiftAnim();
        animHandler.postDelayed(() -> {
            //礼物数等于1
            if (gifts.size() == 1 && gifts.get(0).getGiftNum() == 1) {
                //礼物数大于1 或者单个礼物数量大于1
//            sub(1);
            } else if (calculateAllGiftCount(gifts) == TEN) {
//            sub(TEN);
//                showFreeGiftDialog(gifts);
            } else if (calculateAllGiftCount(gifts) == HUNDRED) {//50连抽
//            sub(HUNDRED);
//                showFreeGiftDialog(gifts);
            }
            if (tvGoldCount != null) {
                upGold();
                setLotteryGiftInfo(gifts);
                showDrawResultDialog(gifts);
            }
        }, 990);
    }

    private Handler animHandler = new LotteryHandler();
    private AnimationDrawable animationDrawable;

    static class LotteryHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

        }
    }

    private void showFreeGiftAnim() {
        animationDrawable = (AnimationDrawable) ivLotteryBoxBox.getBackground();
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
            animationDrawable.start();
        } else {
            animationDrawable.start();
        }
    }

    private void sub(int ride) {
        double sub;
        sub = (goldNum - 20.0 * ride);
        CoreManager.getCore(IPayCore.class).minusGold(20 * ride);
        if (tvGoldCount != null) {
            tvGoldCount.setText(getString(R.string.gold_num_text, sub));
            if (conchResultDialog != null) conchResultDialog.upGold(goldNum);
        }
        goldNum = sub;
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
     * @param gifts
     */
    private void setLotteryGiftInfo(List<EggGiftInfo> gifts) {
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
        }
        if (markGift.getGoldPrice() >= MAX && markCount > 0) {
//            CoreManager.getCore(IGiftCore.class).sendLotteryMeg(markGift, markCount);
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

    /**
     * 更新金额信息
     */
    private void updateWalletInfo(WalletInfo walletInfo) {
        if (walletInfo != null) {
            goldNum = walletInfo.getGoldNum();
            tvGoldCount.setText(getContext().getString(R.string.gold_num_text, goldNum));
            if (conchResultDialog != null) conchResultDialog.upGold(goldNum);
        }
    }

    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onGetWalletInfo(WalletInfo walletInfo) {
        updateWalletInfo(walletInfo);
    }

    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onWalletInfoUpdate(WalletInfo walletInfo) {
        updateWalletInfo(walletInfo);
    }
}

