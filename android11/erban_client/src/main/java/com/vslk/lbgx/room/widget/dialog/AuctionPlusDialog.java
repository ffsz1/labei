package com.vslk.lbgx.room.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionUser;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.utils.StringUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * @author zhouxiangfeng
 * @date 2017/5/13
 */

public class AuctionPlusDialog extends BottomSheetDialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "AuctionPlusDialog";
    private Context context;
    private ImageView ivMinus;
    private ImageView ivPlus;
    private TextView tvPriceNum;
    private TextView tv_name;
    private View rlAuction;
    private LinearLayout llHead;
    private RadioButton rbOne;
    private RadioButton rbTwo;
    private RadioButton rbThree;
    private RadioButton rbFour;
    private RadioGroup rgPrices;
    private TextView tvMyPrice;
    private TextView tvCurrentMaxPrice;
    private Button btnCharge;
    private Button btnDoPlus;
    private TextView tvPriceNeed;

    private AuctionInfo info;
    private LinearLayout llCurMaxMoney;
    private AuctionPlusDialogItemClickListener listener;

    private int price = 0;
    private int middlePrice = 10;

    public AuctionPlusDialog(Context context, AuctionInfo info) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
        this.info = info;
    }

    public void setAuctionPlusDialogItemClickListener(AuctionPlusDialogItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_auction_plus);

        llHead = (LinearLayout) findViewById(R.id.ll_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tvCurrentMaxPrice = (TextView) findViewById(R.id.tv_curbest_price);
        tvMyPrice = (TextView) findViewById(R.id.tv_myprice);
        rgPrices = (RadioGroup) findViewById(R.id.rg_prices);
        rbOne = (RadioButton) findViewById(R.id.rb_one);
        rbTwo = (RadioButton) findViewById(R.id.rb_two);
        rbThree = (RadioButton) findViewById(R.id.rb_three);
        rbFour = (RadioButton) findViewById(R.id.rb_four);
        ivMinus = (ImageView) findViewById(R.id.iv_minus);
        ivPlus = (ImageView) findViewById(R.id.iv_plus);
        tvPriceNum = (TextView) findViewById(R.id.tv_price_num);
        btnCharge = (Button) findViewById(R.id.btn_charge);
        btnDoPlus = (Button) findViewById(R.id.btn_do_plus);

        tvPriceNeed = (TextView) findViewById(R.id.tv_price_need);
        llCurMaxMoney = (LinearLayout) findViewById(R.id.ll_cur_max_money);

        llHead.getBackground().setAlpha(25);
        btnCharge.setOnClickListener(this);
        btnDoPlus.setOnClickListener(this);
        ivMinus.setOnClickListener(this);
        ivPlus.setOnClickListener(this);
        rgPrices.setOnCheckedChangeListener(this);

        update();

        FrameLayout bottomSheet = (FrameLayout) findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(
                    (int) context.getResources().getDimension(R.dimen.dialog_auction_height));
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = context.getResources().getDisplayMetrics().heightPixels - (Utils.hasSoftKeys(context) ? Utils.getNavigationBarHeight(context) : 0);
        getWindow().setAttributes(params);
    }

    private void plus() {
        price += middlePrice;
        tvPriceNum.setText(String.valueOf(price));
    }

    private void minus() {
        int minusPrice = price - middlePrice;
        if (minusPrice < middlePrice) {
            return;
        }

        if (info.getRivals() != null && info.getRivals().size() > 0) {
            AuctionUser auctionUser = findMyAlreadyRaisePrice();
            AuctionUser maxAuctionUserInfo = info.getRivals().get(0);

            int myPrice = 0;
            if (auctionUser != null) {
                myPrice = auctionUser.getAuctMoney() + minusPrice;
            } else {
                myPrice = minusPrice;
            }
            if (myPrice <= maxAuctionUserInfo.getAuctMoney()) {
                Toast.makeText(getContext(), "您出的价格不能少于当前最高价", LENGTH_SHORT).show();
                return;
            }
        } else {
            int auctPrice = info.getAuctMoney();
            if (minusPrice < auctPrice) {
                Toast.makeText(getContext(), "您出的价格不能少于最低起拍价", LENGTH_SHORT).show();
                return;
            }
        }

        price -= middlePrice;
        tvPriceNum.setText(String.valueOf(price));
    }

    private AuctionUser findMyAlreadyRaisePrice() {
        if (info != null) {
            if (info.getRivals() != null && info.getRivals().size() > 0) {
                long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
                for (int i = 0; i < info.getRivals().size(); i++) {
                    if (uid == info.getRivals().get(i).getUid()) {
                        return info.getRivals().get(i);
                    }
                }
            }
        }
        return null;
    }

    private void update() {
        if (info != null) {
            String unit = context.getString(R.string.gift_expend_gold);
            AuctionUser myAuctionUser = findMyAlreadyRaisePrice();
            if (info.getRivals() != null && info.getRivals().size() > 0) {
                AuctionUser maxAuctionUser = info.getRivals().get(0);
                UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(maxAuctionUser.getUid());
                if (userInfo != null) {
                    tv_name.setText(StringUtils.splic("'", userInfo.getNick(), "'", "出价"));
                    tvCurrentMaxPrice.setText(String.valueOf(info.getRivals().get(0).getAuctMoney() + unit));
                }
                if (myAuctionUser != null) {
                    tvMyPrice.setText(String.valueOf(myAuctionUser.getAuctMoney() +unit));
                    price = maxAuctionUser.getAuctMoney() - myAuctionUser.getAuctMoney() + middlePrice;
                } else {
                    tvMyPrice.setText("0"+unit);
                    price = maxAuctionUser.getAuctMoney() + middlePrice;
                }
                tvPriceNum.setText(String.valueOf(price));
                tvPriceNeed.setText(StringUtils.splic(String.valueOf(price), unit+"以上"));
            } else {
                price = info.getAuctMoney();
                tv_name.setText(String.valueOf("当前起拍价" + info.getAuctMoney() + unit));
                tvCurrentMaxPrice.setText("");
                tvPriceNum.setText(String.valueOf(price));
                tvPriceNeed.setText(String.valueOf(info.getAuctMoney() +unit+"以上"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_do_plus:
                if (listener != null) {
                    AuctionUser auctionUser = findMyAlreadyRaisePrice();
                    if (auctionUser != null) {
                        listener.onClickDoPlus(price + auctionUser.getAuctMoney());
                    } else {
                        listener.onClickDoPlus(price);
                    }
                    dismiss();
                }
                break;

            case R.id.iv_minus:
                minus();
                break;

            case R.id.iv_plus:
                plus();
                break;

            case R.id.btn_charge:
                WalletActivity.start(getContext());
                dismiss();
                break;

            default:
                break;
        }
    }

    private int selectMulti = 1;

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_one:
                selectMulti = 100;
                break;

            case R.id.rb_two:
                selectMulti = 300;
                break;

            case R.id.rb_three:
                selectMulti = 600;
                break;

            case R.id.rb_four:
                selectMulti = 1000;
                break;
            default:
        }

    }


    public interface AuctionPlusDialogItemClickListener {
        void onClickDoPlus(int price);
    }

}
