package com.vslk.lbgx.room.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

public class AuctionDialog extends BottomSheetDialog implements View.OnClickListener {
    private Context context;
    private CircleImageView civ_head;
    private ImageView iv_minus;
    private ImageView iv_plus;
    private TextView tv_price_num;
    private Button btn_begin_auction;
    private TextView tv_name;
    private View rl_auction;
    private UserInfo userInfo;
    private long uid;
    private int price = 10;
    private int middlePrice = 10;
    private boolean isFullScreen = true;


    public AuctionDialog(Context context, long uid) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
        this.uid = uid;
        this.userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid);
    }

    public AuctionDialog(Context context, long uid, boolean isFullScreen) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
        this.uid = uid;
        this.userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid);
        this.isFullScreen = isFullScreen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
        setContentView(R.layout.dialog_bottom_auction);

        rl_auction = this.findViewById(R.id.rl_auction);
        civ_head = (CircleImageView) this.findViewById(R.id.civ_head);
        tv_name = (TextView) this.findViewById(R.id.tv_name);
        iv_minus = (ImageView) this.findViewById(R.id.iv_minus);
        iv_plus = (ImageView) this.findViewById(R.id.iv_plus);
        tv_price_num = (TextView) this.findViewById(R.id.tv_price_num);
        btn_begin_auction = (Button) this.findViewById(R.id.btn_begin_auction);

        rl_auction.getBackground().setAlpha(246);
        btn_begin_auction.setOnClickListener(this);
        civ_head.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        iv_plus.setOnClickListener(this);

        initiate(userInfo);

        FrameLayout bottomSheet = (FrameLayout) findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(
                    (int) context.getResources().getDimension(R.dimen.dialog_auction_height));
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        params.height = (isFullScreen ? context.getResources().getDisplayMetrics().heightPixels : realDisplayMetrics.heightPixels) -
                (Utils.hasSoftKeys(context) ? Utils.getNavigationBarHeight(context) : 0);
        getWindow().setAttributes(params);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_begin_auction:
                if (null != onClickItemListener) {
                    onClickItemListener.onClickBegin(price);
                }
                dismiss();
                break;
            case R.id.iv_minus:
                if (price - middlePrice > 0) {
                    price -= middlePrice;
                    tv_price_num.setText(String.valueOf(price));
                }
                break;
            case R.id.iv_plus:
                price += middlePrice;
                tv_price_num.setText(String.valueOf(price));
                break;
            case R.id.civ_head:
                if (null != onClickItemListener) {
                    onClickItemListener.onClickHead();
                }
                break;

            default:
                break;
        }
    }

    private OnClickItemListener onClickItemListener;

    public OnClickItemListener getOnClickItemListener() {
        return onClickItemListener;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void onClickHead();

        void onClickBegin(int price);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        if (info.getUid() == uid) {
            initiate(info);
        }
    }

    public void initiate(UserInfo userInfo) {
        if (userInfo != null) {
            ImageLoadUtils.loadAvatar(getContext(), userInfo.getAvatar(), civ_head);
            tv_name.setText(userInfo.getNick());
            tv_price_num.setText(String.valueOf(price));
        }
    }

}
