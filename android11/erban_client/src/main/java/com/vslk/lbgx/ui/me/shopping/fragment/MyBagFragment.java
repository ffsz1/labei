package com.vslk.lbgx.ui.me.shopping.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.shopping.DressUpFragmentPresenter;
import com.vslk.lbgx.room.egg.call.CallCoreImpl;
import com.vslk.lbgx.room.egg.call.ICallCore;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.shopping.activity.GiveGoodsActivity;
import com.vslk.lbgx.ui.me.shopping.adapter.CarAdapter;
import com.vslk.lbgx.ui.me.shopping.view.IDressUpFragmentView;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletDetailsActivity;
import com.vslk.lbgx.ui.web.PayWebViewActivity;

import java.util.List;

import static com.vslk.lbgx.ui.me.shopping.activity.MyBagActivity.DRESS_CAR;
import static com.vslk.lbgx.ui.me.shopping.activity.MyBagActivity.DRESS_HEADWEAR;

/**
 * @author Administrator
 * @date 2018/5/7
 */

@CreatePresenter(DressUpFragmentPresenter.class)
public class MyBagFragment extends BaseMvpFragment<IDressUpFragmentView, DressUpFragmentPresenter> implements
        CarAdapter.OnItemSelectedListener, IDressUpFragmentView, View.OnClickListener {

    private CarAdapter shopAdapter;
    private RecyclerView recyclerView;

    private int dressType;
    private boolean isMySelf;
    private UserInfo targetUserInfo;

    private TextView mEffectiveTime;
    private TextView chargeTv;
    private DrawableTextView mGive;
    private DrawableTextView mBuy;

    private DressUpBean data;
    private int selectId;
    private ICallCore iCallCore;

    @Override
    public void onFindViews() {
        recyclerView = mView.findViewById(R.id.rv_shop);
        mEffectiveTime = mView.findViewById(R.id.text);
        chargeTv = mView.findViewById(R.id.charge_fragment_tv);
        mGive = mView.findViewById(R.id.give);
        mBuy = mView.findViewById(R.id.buy);
        mGive.setText("装备");
    }

    @Override
    protected void onInitArguments(Bundle bundle) {
        dressType = bundle.getInt("type", 0);
        isMySelf = bundle.getBoolean("isMySelf", false);
        long targetUid = getArguments().getLong("targetUid", 0);
        if (!isMySelf && targetUid != 0) {
            targetUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(targetUid);
        }
    }

    @Override
    public void onSetListener() {
        mBuy.setOnClickListener(this);
        chargeTv.setOnClickListener(this);
    }

    @Override
    public void initiate() {
        iCallCore = CallCoreImpl.getInstance().getICallCore();
        if (iCallCore != null) iCallCore.getGold((wallet -> {
            mEffectiveTime.setText(String.valueOf(wallet.getGoldNum()));
        }));

        if (!isMySelf) {
            mBuy.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = ConvertUtils.dp2px(8);
                outRect.bottom = ConvertUtils.dp2px(8);
            }
        });

        shopAdapter = new CarAdapter(dressType, isMySelf, getActivity(), true);
        shopAdapter.setVggAction(url -> {
            try {
                CoreManager.notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_SHOW_CAR_ANIM, url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        shopAdapter.setOnItemSelectedListener(this);
        recyclerView.setAdapter(shopAdapter);
        getDialogManager().showProgressDialog(getContext(), "加载中...");
        getData();
    }

    private void getData() {
        getMvpPresenter().getDressUpData(isMySelf, dressType, 1, 50);
    }

    @Override
    public void getDressUpListSuccess(ServiceResult<List<DressUpBean>> result) {
        try {
            if (getDialogManager() != null) {
                getDialogManager().dismissDialog();
            }
        } catch (Exception e) {
            return;
        }
        if (result == null || !result.isSuccess()) {
            toast("获取数据失败");
        } else {
            initSelected(result.getData());
        }
    }

    @Override
    public void getDressUpListFail(String msg) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast("网络异常");
    }

    private void initSelected(List<DressUpBean> carList) {
        if (carList == null) return;
        if (isMySelf) {
            for (DressUpBean dressUpBean : carList) {
                if (dressUpBean.getIsPurse() == 2) {
                    dressUpBean.setIsSelect(2);
                    onItemSelected(dressUpBean);
                }
            }
        }
        shopAdapter.setNewData(carList);
    }

    @Override
    public void onChangeDressUpStateSuccess(int dressUpId) {
        CoreManager.getCore(IUserCore.class).requestUserInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        getData();
    }

    @Override
    public void onChangeDressUpStateFail(String error) {
        SingleToastUtil.showToast(error);
    }

    @Override
    public void onItemSelected(DressUpBean data) {
        if (data == null) {
            return;
        }
        this.data = data;
//        mEffectiveTime.setText(String.valueOf(data.getGoldPrice())/*MessageFormat.format("{0}/{1}天", data.getGoldPrice(), data.getEffectiveTime())*/);
        int isPurse = data.getIsPurse();
        if (isPurse == 0) {
            mBuy.setText("购买");
            //mBuy.setBackgroundResource(R.drawable.icon_shop_buy);
        } else {
            mBuy.setText("续费");
            //mBuy.setBackgroundResource(R.drawable.icon_shop_renew);
        }

        mGive.setText(isPurse == 1 ? "装备" : "取消装备");
        mGive.setOnClickListener(v -> {
            String giveStr = "装备";
            if (data.getIsPurse() == 1) {
                selectId = (dressType == 0 ? data.getHeadwearId() : data.getCarId());
                giveStr = "取消装备";
                data.setIsPurse(2);
                data.setIsSelect(2);
                if (dressType == 0) {
                    CoreManager.notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_SHOW_HEAD_WEAR, data.getPicUrl());
                }
            } else if (data.getIsPurse() == 2) {
                giveStr = "装备";
                data.setIsPurse(1);
                data.setIsSelect(1);
                selectId = -1;
            }

            mGive.setText(giveStr);
            //装备
            getMvpPresenter().onChangeDressUpState(dressType, selectId);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buy:
                buyCar();
                break;
//            case R.id.give:
//                giveCar();

//                break;
            case R.id.charge_fragment_tv:
//                PayWebViewActivity.start(getActivity(), WebUrl.CHARGE_URL);
                WalletActivity.start(getActivity());
//                WalletDetailsActivity.start(getContext(), 0);
//                callKefu();
                break;
            default:
                break;
        }
    }

    private void callKefu() {
        getDialogManager().showOkCancelDialog("充值功能正在开发中，请先移步微信公众号充值：\n" + getString(R.string.txt_common_num), "复制", "取消", new DialogManager.AbsOkDialogListener() {
            @Override
            public void onOk() {
                ClipboardManager mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("复制成功!", getString(R.string.txt_common_num));
                mClipboardManager.setPrimaryClip(clipData);
                toast("复制成功!");
            }
        });
    }

    private void giveCar() {
        if (data == null) {
            return;
        }
        if (isMySelf) {
            Intent intent = new Intent(mContext, GiveGoodsActivity.class);
            intent.putExtra("carName", dressType == DRESS_HEADWEAR ? data.getHeadwearName() : data.getCarName());
            intent.putExtra("goodsId", (dressType == DRESS_HEADWEAR ? data.getHeadwearId() : data.getCarId()) + "");
            intent.putExtra("type", dressType == DRESS_HEADWEAR ? DRESS_HEADWEAR : DRESS_CAR);
            intent.putExtra("url", data.getPicUrl());

            mContext.startActivity(intent);
        } else {
            giveGift(dressType == DRESS_HEADWEAR ? data.getHeadwearName() : data.getCarName(),
                    (dressType == DRESS_HEADWEAR ? data.getHeadwearId() : data.getCarId()) + "");
        }
    }

    private void giveGift(String dressName, String goodsId) {

        getDialogManager().showOkCancelDialog("确认购买 “" + dressName + "” 并赠送给 " + targetUserInfo.getNick() + " ？", true, new DialogManager.AbsOkDialogListener() {

            @Override
            public void onOk() {
                requestGift(String.valueOf(targetUserInfo.getUid()), goodsId);
            }
        });
    }

    private void requestGift(String uid, String goodsId) {
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(goodsId)) {
            toast("参数异常");
            return;
        }
        getDialogManager().showProgressDialog(getActivity());
        getMvpPresenter().giveGift(dressType, uid, goodsId);
    }

    @Override
    public void giftGiveSuccess() {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
    }

    @Override
    public void giftGiveFail(String msg) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(msg);
    }

    private void buyCar() {
        if (data == null) {
            return;
        }
        int isPurse = data.getIsPurse();
        showBuyDialog(isPurse == DRESS_HEADWEAR ? 1 : 2, dressType == DRESS_HEADWEAR ? data.getHeadwearId() : data.getCarId(),
                dressType == DRESS_HEADWEAR ? data.getHeadwearName() : data.getCarName());
    }

    private void showBuyDialog(int purseType, int dressId, String carName) {
        String message = "确认" + (purseType == 1 ? "购买 “" : "续费 “") + carName + (dressType == DRESS_HEADWEAR ? "” 头饰吗" : "” 座驾吗");
        getDialogManager().showOkCancelDialog(message, true, new DialogManager.AbsOkDialogListener() {
            @Override
            public void onOk() {
                getDialogManager().showProgressDialog(getContext(), "请稍后");
                getMvpPresenter().onPurseDressUp(dressType, purseType, dressId);
            }
        });
    }

    @Override
    public void onPurseDressUpSuccess(int purseType) {
        getDialogManager().dismissDialog();
        toast(purseType == 1 ? "购买成功" : "续费成功");
        CoreManager.getCore(IUserCore.class).requestUserInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        getData();
    }

    @Override
    public void onPurseDressUpFail(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.shop_fragment;
    }

    public static MyBagFragment newInstance(int type, boolean isMySelf, long targetUid) {
        Bundle bundle = new Bundle();
        MyBagFragment fragment = new MyBagFragment();
        bundle.putInt("type", type);
        bundle.putLong("targetUid", targetUid);
        bundle.putBoolean("isMySelf", isMySelf);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
