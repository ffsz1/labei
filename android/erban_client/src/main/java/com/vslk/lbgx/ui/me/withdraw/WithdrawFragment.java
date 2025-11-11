package com.vslk.lbgx.ui.me.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCore;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCoreClient;
import com.tongdaxing.xchat_core.withdraw.bean.ExchangerInfo;
import com.tongdaxing.xchat_core.withdraw.bean.RefreshInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrwaListInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.vslk.lbgx.ui.me.wallet.adapter.WithdrawJewelAdapter;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.utils.NumberFormatUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import static com.vslk.lbgx.utils.ThreadUtil.runOnUiThread;

/**
 * 钻石提现
 *
 * @author dell
 */
public class WithdrawFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, WithdrawWayDialog.OnWithdrawWayChangeListener {

    private DrawableTextView mWithDraw;
    private TextView withdrawMethod;
    private TextView diamondNumWithdraw;
    private RecyclerView mRecyclerView;
    private WithdrawJewelAdapter mJewelAdapter;
    public WithdrwaListInfo checkedPosition;
    private WithdrawInfo withdrawInfos = new WithdrawInfo();
    private LinearLayout llBinding;
    private ImageView ivIcon;
    private TextView tvTips;
    private IDiamondsCore iDiamondsCore;
    private TextView phoneTv;
    private TextView getCodeBtn;
    private CodeDownTimer mTimer;
    private UserInfo userInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        iDiamondsCore = DiamondsCoreImpl.getInstance().get();
    }


    @Override
    public void initiate() {
        initView();
        setListener();
        initData();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mJewelAdapter = new WithdrawJewelAdapter();
        mJewelAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mJewelAdapter);
        loadRecyclerViewData();
    }

    private void loadAlipayInfo() {
        CoreManager.getCore(IWithdrawCore.class).getWithdrawUserInfo();
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetWithdrawUserInfo(WithdrawInfo withdrawInfo) {
        if (withdrawInfo != null) {
            withdrawInfos = withdrawInfo;
            diamondNumWithdraw.setText(String.format(Locale.getDefault(), "%.2f", withdrawInfo.diamondNum));
        }

    }


    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onWxAuthFailure(String errorStr) {
        runOnUiThread(() -> getDialogManager().dismissDialog());
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onWxAuthSucceed(String openid, String unionid, String accessToken, String nickName) {
        runOnUiThread(() -> {
            getDialogManager().dismissDialog();
            withdrawMethod.setText("昵称: " + nickName);
        });
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetWithdrawUserInfoFail(String error) {
        toast(error);
    }

    private void loadRecyclerViewData() {
        CoreManager.getCore(IWithdrawCore.class).getWithdrawList();
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetWithdrawList(List<WithdrwaListInfo> withdrwaListInfo) {
        if (withdrwaListInfo != null && withdrwaListInfo.size() > 0) {
            notifyListState(withdrwaListInfo);
            mJewelAdapter.setNewData(withdrwaListInfo);
        }
    }

    private void notifyListState(List<WithdrwaListInfo> withdrwaListInfo) {
        if (withdrwaListInfo != null && !withdrwaListInfo.isEmpty()) {
            for (WithdrwaListInfo info : withdrwaListInfo) {
                info.setWd(true);
            }
        }
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetWithdrawListFail(String error) {
        toast("获取提现列表失败");
    }


    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetSmsCode() {
        if (userInfo != null) {
            phoneTv.setText("验证码已发送至您绑定的手机号"+ NumberFormatUtils.hideMiddleExtend(userInfo.getPhone()));
        }
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetSmsCodeFail(String error) {
        toast(error);
    }

    private void setListener() {
        llBinding.setOnClickListener(v -> {
            //判断是否实名
            if (iDiamondsCore != null) iDiamondsCore.isAuthentication((it) -> {
                if (it) {
                    Intent intent = new Intent(getContext(), BindingActivity.class);
                    startActivityForResult(intent, 10022);
                } else {
                    IsAuthDialog isAuthDialog = new IsAuthDialog();
                    isAuthDialog.show(getChildFragmentManager(), "isAuth");
                }
            });

        });
        mWithDraw.setOnClickListener(view -> {
            if (type == 0) {
                toast("请先选择提取方式！");
                return;
            }
            if (checkedPosition == null) {
                toast("未选择提现金额");
                return;
            }

            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.get_code_dialog,null,false);
            getDialogManager().showCustomViewDialog(view1);
            getCodeBtn = view1.findViewById(R.id.tv_get_code);
            phoneTv = view1.findViewById(R.id.text_show_tv);
            EditText authEt = view1.findViewById(R.id.et_auth_code);

            getCodeBtn.setOnClickListener(it ->{
                mTimer = new CodeDownTimer(getCodeBtn, 60000, 1000);
                mTimer.start();
                CoreManager.getCore(IWithdrawCore.class).getSmsCode(CoreManager.getCore(IAuthCore.class).getCurrentUid());
            });
            view1.findViewById(R.id.btn_cancel).setOnClickListener(it ->{
                getDialogManager().dismissDialog();
                return;
            });
            view1.findViewById(R.id.btn_ok).setOnClickListener(it ->{
                if(authEt.getText().toString().isEmpty()){
                    toast("验证码为空，请重新输入");
                    return;
                }
                getDialogManager().dismissDialog();
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.onFinish();
                }
                //发起兑换
                getDialogManager().showOkCancelDialog(/*"您将要提现" + checkedPosition.getCashProdName()*/"是否确认兑换收益？", true, new DialogManager.OkCancelDialogListener() {

                    @Override
                    public void onCancel() {
                        getDialogManager().dismissDialog();
                    }

                    @Override
                    public void onOk() {
                        getDialogManager().dismissDialog();
                        if (checkedPosition != null) {
                            if (iDiamondsCore != null)
                                iDiamondsCore.bindWithdrawAccount((it -> {
                                    WithdrawalDialog withdrawalDialog = new WithdrawalDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("gold", checkedPosition.getCashNum() + "");
                                    withdrawalDialog.show(getChildFragmentManager(), "withdrawal");
                                    loadAlipayInfo();
                                }), selectNum, selectName, String.valueOf(type), String.valueOf(checkedPosition.cashProdId),userInfo.getPhone(),authEt.getText().toString());
                        } else {
                            toast("兑换失败");
                        }
                    }
                });
            });
        });
    }

    private int type = 0;
    private String selectName = "";
    private String selectNum = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10022 && resultCode == 10020) {
            type = data.getIntExtra("type", 0);
            selectName = data.getStringExtra("name");
            selectNum = data.getStringExtra("num");
            if (type == 0) {
                ivIcon.setVisibility(View.GONE);
                tvTips.setVisibility(View.VISIBLE);
            } else if (type == 1) {
                ivIcon.setVisibility(View.VISIBLE);
                tvTips.setVisibility(View.GONE);
                ivIcon.setImageResource(R.mipmap.ic_binding_zfb);
            } else if (type == 2) {
                ivIcon.setVisibility(View.VISIBLE);
                tvTips.setVisibility(View.GONE);
                ivIcon.setImageResource(R.mipmap.ic_binding_yhk);
            }
        }
    }


    private boolean isWithdraw(WithdrwaListInfo withdrwaListInfo) {
        if (withdrawInfos != null) {
            //用户的钻石余额 > 选中金额的钻石数时
            if (withdrawInfos.diamondNum >= withdrwaListInfo.diamondNum) {
                return true;
            } else {
                toast("您的钻石不足以进行此次提现！");
                return false;
            }
        } else {
            return false;
        }
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onRequestExchange(ExchangerInfo exchangerInfo) {
        if (exchangerInfo != null) {
            diamondNumWithdraw.setText(String.format(Locale.getDefault(), "%.2f", exchangerInfo.diamondNum));
            toast("提现成功");
        }
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onRequestExchangeFail(String error) {
        toast(error);
    }

    private void initView() {
        diamondNumWithdraw = (TextView) mView.findViewById(R.id.tv_diamondNums);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        mWithDraw = (DrawableTextView) mView.findViewById(R.id.btn_withdraw);
        withdrawMethod = (TextView) mView.findViewById(R.id.tv_withdraw_method);
        llBinding = (LinearLayout) mView.findViewById(R.id.ll_binding);
        ivIcon = (ImageView) mView.findViewById(R.id.iv_icon);
        tvTips = (TextView) mView.findViewById(R.id.tv_tips);
//        mView. findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        mView.findViewById(R.id.tv_right).setOnClickListener(v -> CommonWebViewActivity.start(v.getContext(), WebUrl.WITTH_DRAW));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshEvent(RefreshInfo refreshInfo) {
        loadAlipayInfo();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mJewelAdapter != null && !ListUtils.isListEmpty(mJewelAdapter.getData())) {
            WithdrwaListInfo withdrwaListInfo = mJewelAdapter.getData().get(position);
            if (!isWithdraw(withdrwaListInfo)) {
                return;
            }
            checkedPosition = withdrwaListInfo;
            int size = mJewelAdapter.getData().size();
            for (int i = 0; i < size; i++) {
                mJewelAdapter.getData().get(i).isSelected = position == i;
            }
            mJewelAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadAlipayInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (iDiamondsCore != null) iDiamondsCore = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void onChangeListener(WithdrawInfo withdrawInfo) {
        onGetWithdrawUserInfo(withdrawInfo);
    }
}
