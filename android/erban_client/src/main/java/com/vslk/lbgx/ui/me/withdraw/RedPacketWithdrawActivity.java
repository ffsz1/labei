package com.vslk.lbgx.ui.me.withdraw;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.invite.IInviteRedPacketDrawView;
import com.vslk.lbgx.presenter.invite.InviteRedPacketDrawPresenter;
import com.vslk.lbgx.ui.me.wallet.adapter.WithdrawRedListAdapter;
import com.vslk.lbgx.ui.widget.dialog.SMCAwardVerificationDialog;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCore;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedListInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedSucceedInfo;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCore;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCoreClient;
import com.tongdaxing.xchat_core.withdraw.bean.RefreshInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 红包提现,提现流程已改，可对本地登录的微信提现，但是必须绑定和验证手机号2019.07.12
 *
 * @author dell
 */
@CreatePresenter(InviteRedPacketDrawPresenter.class)
public class RedPacketWithdrawActivity extends BaseMvpActivity<IInviteRedPacketDrawView, InviteRedPacketDrawPresenter> implements IInviteRedPacketDrawView, View.OnClickListener,
        WithdrawWayDialog.OnWithdrawWayChangeListener {

    private AppToolBar mToolBar;
    private TextView redNum;
    //    private FrameLayout binder;
    private RelativeLayout binderSucceed;
    private RecyclerView recyclerView;
    private Button btnWithdraw;
    //    private WithdrawInfo mWithdrawInfo;//提现规则已改
    private WithdrawRedListAdapter mRedListAdapter;
    private WithdrawRedListInfo mSelectRedInfo;
    private TextView tvWxNickName;
    private boolean hasAlipay = false;
    private SMCAwardVerificationDialog smcAwardVerificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet_withdraw);
        EventBus.getDefault().register(this);
//        initTitleBar(getString(R.string.red_packet_withdraw));
        initView();
        initData();
        setListener();
    }

    private void initView() {
        redNum = (TextView) findViewById(R.id.tv_red_num);
//        binder = (FrameLayout) findViewById(R.id.rly_binder);
        binderSucceed = (RelativeLayout) findViewById(R.id.rly_binder_succeed);
        tvWxNickName = (TextView) findViewById(R.id.tv_withdraw_method);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnWithdraw = (Button) findViewById(R.id.btn_withdraw);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
    }

    private String wxNickName, openid;

    private void initData() {
        if (getIntent() != null) {
            wxNickName = getIntent().getStringExtra("WxNickName");
            openid = getIntent().getStringExtra("WxOpenid");
            tvWxNickName.setText(wxNickName);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false));
        mRedListAdapter = new WithdrawRedListAdapter();
        recyclerView.setAdapter(mRedListAdapter);
        mRedListAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<WithdrawRedListInfo> list = mRedListAdapter.getData();
            if (ListUtils.isListEmpty(list)) {
                return;
            }
            mSelectRedInfo = list.get(position);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                list.get(i).isSelected = position == i;
            }
            mRedListAdapter.notifyDataSetChanged();
            isWithdraw();
        });
//        loadWithdrawUserInfo();
        loadingData();
        loadingListData();
    }

    private void loadWithdrawUserInfo() {
        CoreManager.getCore(IWithdrawCore.class).getWithdrawUserInfo();
    }

//    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
//    public void onGetWithdrawUserInfo(WithdrawInfo withdrawInfo) {
//        Log.e("withdrawInfo",
//                "onGetWithdrawUserInfo: " + withdrawInfo.getUid() + " " + withdrawInfo.getWithDrawType() + " " + withdrawInfo.hasWx);
//        if (withdrawInfo != null) {
//            mWithdrawInfo = withdrawInfo;
//            if (withdrawInfo.withDrawType == 1) {
//                //有微信账号改变状态
//                if (!withdrawInfo.isHasWx()) {
//                    notifyListState(mRedListAdapter.getData());
//                    mRedListAdapter.notifyDataSetChanged();
//                    binder.setVisibility(View.VISIBLE);
//                    binderSucceed.setVisibility(View.GONE);
//                } else {
//                    notifyListState(mRedListAdapter.getData());
//                    mRedListAdapter.notifyDataSetChanged();
//                    binder.setVisibility(View.GONE);
//                    binderSucceed.setVisibility(View.VISIBLE);
//                    if (!TextUtils.isEmpty(wxNickName)) {
//                        tvWxNickName.setText("昵称: " + wxNickName);
//                    }
//                }
//            }
//        }
//    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetWithdrawUserInfoFail(String error) {
        toast(error);
    }

    private void loadingListData() {
        CoreManager.getCore(IRedPacketCore.class).getRedList();
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onGetRedList(List<WithdrawRedListInfo> withdrawRedListInfos) {
        if (!withdrawRedListInfos.isEmpty()) {
            notifyListState(withdrawRedListInfos);
            mRedListAdapter.setNewData(withdrawRedListInfos);
        }
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onGetRedListError(String error) {
        toast(error);
    }

    private void notifyListState(List<WithdrawRedListInfo> list) {
/*        if (!hasAlipay) {
            return;
        }*/
        if (list != null && !list.isEmpty()) {
            for (WithdrawRedListInfo info : list) {
                info.setWd(true);
            }
        }
    }

    private void loadingData() {
        CoreManager.getCore(IRedPacketCore.class).getRedPacketInfo();
    }

    private RedPacketInfo redPacketInfos;

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onGetRedInfo(RedPacketInfo redPacketInfo) {
        if (null != redPacketInfo) {
            redPacketInfos = redPacketInfo;
            Double packetNum = redPacketInfo.getPacketNum();
            packetNum = (double) Math.round(packetNum * 100) / 100;
            redNum.setText(String.valueOf(packetNum));
        }
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onGetRedInfoError(String error) {
        toast(error);
    }

    private void setListener() {
//        binder.setOnClickListener(this);
        btnWithdraw.setOnClickListener(this);
        binderSucceed.setOnClickListener(this);
        mToolBar.setOnBackBtnListener(view -> finish());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //未绑定支付宝时可点---绑定支付宝账号
            case R.id.rly_binder:
//                WithdrawWayDialog withdrawWayDialog = WithdrawWayDialog.newInstance(mWithdrawInfo, this);
//                withdrawWayDialog.setOnWithdrawWayChangeListener(this);
//                withdrawWayDialog.show();

                break;
            //绑定成功支付宝后可点---更改支付宝账号
            case R.id.rly_binder_succeed:
//                WithdrawWayDialog withdrawWayDialog2 = WithdrawWayDialog.newInstance(mWithdrawInfo, this);
//                withdrawWayDialog2.setOnWithdrawWayChangeListener(this);
//                withdrawWayDialog2.show();
                break;
            case R.id.btn_withdraw:
                redPackWithdraw();
                break;
            default:
                break;
        }
    }

    //eventbus监听绑定支付宝页面是否绑定成功，然后刷新红包提现页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshEvent(RefreshInfo refreshInfo) {
        loadWithdrawUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void redPackWithdraw() {

        if (mSelectRedInfo == null) {
            toast("兑换失败");
            return;
        }
        Log.e(TAG, "redPackWithdraw: " + isWithdraw() + " " + openid);
        if (isWithdraw()) {
            //有账号才能提现
//            if (!mWithdrawInfo.isHasWx()) {
//                toast(R.string.bind_your_alipay);
//                return;
//            }
            if (TextUtils.isEmpty(openid)) {
                toast("请登录您的微信号！");
                return;
            }

            //验证短信，成功之后发起兑换
            showSmcDialog();

            //发起兑换

//            CoreManager.getCore(IRedPacketCore.class).getRedWithdraw(
//                    CoreManager.getCore(IAuthCore.class)
//                            .getCurrentUid(), mSelectRedInfo.getPacketId(), openid);


//            getDialogManager().showOkCancelDialog(
//                    getString(R.string.withdraw_dialog_notice, mSelectRedInfo.getPacketNum(),
//                            mSelectRedInfo.getPacketNum()), true, new DialogManager.OkCancelDialogListener() {
//
//                        @Override
//                        public void onCancel() {
//                            getDialogManager().dismissDialog();
//                        }
//
//                        @Override
//                        public void onOk() {
//                            getDialogManager().dismissDialog();
////                            CoreManager.getCore(IRedPacketCore.class).getRedWithdraw(
////                                    CoreManager.getCore(IAuthCore.class)
////                                            .getCurrentUid(), mSelectRedInfo.getPacketId());
//                            showSmcDialog();
//
//                        }
//                    });
        }
    }

    //获取和验证短信验证码弹框
    private void showSmcDialog() {
        smcAwardVerificationDialog = SMCAwardVerificationDialog.newInstance(getMvpPresenter());
        smcAwardVerificationDialog.show(getSupportFragmentManager(), null);
        smcAwardVerificationDialog.iOnSubmit = sms -> {
            getMvpPresenter().getCheckCode(sms);
        };
    }

    @Override
    public void checkSucceed() {
        smcAwardVerificationDialog.dismiss();
        //短信验证成功则提现
        CoreManager.getCore(IRedPacketCore.class).getRedWithdraw(
                CoreManager.getCore(IAuthCore.class)
                        .getCurrentUid(), mSelectRedInfo.getPacketId(), openid);
    }

    @Override
    public void checkFailure(String errorStr) {

        toast(errorStr);
    }

    @Override
    public void onRemindToastSuc() {
        smcAwardVerificationDialog.countDown();
    }

    @Override
    public void onRemindToastError(String error) {
        toast(error);
    }

    public boolean isWithdraw() {
        //如果选中position不为空的时候
//        if (!(mWithdrawInfo != null && mWithdrawInfo.isNotBoundPhone)) {
        if (!TextUtils.isEmpty(openid)) {
            if (mSelectRedInfo != null) {
                //用户的钻石余额 > 选中金额的钻石数时
                if (redPacketInfos.getPacketNum() >= mSelectRedInfo.getPacketNum()) {
                    btnWithdraw.setEnabled(true);
                    return true;
                } else {
                    btnWithdraw.setEnabled(false);
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

//    private boolean isAlipayValid() {
//        return mWithdrawInfo != null && mWithdrawInfo.isHasWx();
//    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onGetWithdraw(WithdrawRedSucceedInfo succeedInfo) {
        toast("兑换成功");
        if (null != succeedInfo) {
            Double packetNum = succeedInfo.getPacketNum();
            packetNum = (double) Math.round(packetNum * 100) / 100;
            redNum.setText(String.valueOf(packetNum));
        }

    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onGetWithdrawError(String error) {
        toast(error);
    }

    @Override
    public void onChangeListener(WithdrawInfo withdrawInfo) {
//        onGetWithdrawUserInfo(withdrawInfo);
    }

}
