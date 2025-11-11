package com.vslk.lbgx.ui.me.shopping.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpListFragment;
import com.vslk.lbgx.presenter.shopping.DressUpFragmentPresenter;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.shopping.adapter.DressUpListAdapter;
import com.vslk.lbgx.ui.me.shopping.listener.OnHeadWearCallback;
import com.vslk.lbgx.ui.me.shopping.view.IDressUpFragmentView;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.text.MessageFormat;
import java.util.List;

import static com.vslk.lbgx.ui.me.shopping.activity.DressUpMallActivity.DRESS_HEADWEAR;

/**
 * 装扮商城分类列表模块
 *
 * @author zwk 2018/10/16
 */
@CreatePresenter(DressUpFragmentPresenter.class)
public class DressUpFragment extends BaseMvpListFragment<DressUpListAdapter, IDressUpFragmentView, DressUpFragmentPresenter>
        implements IDressUpFragmentView, DressUpListAdapter.OnDressUpClickListener, View.OnClickListener {
    private int type;//0 头饰 1 座驾
    private boolean isMySelf = false;
    private OnHeadWearCallback onHeadWearCallback;
    private FrameLayout llAction;//装扮操作布局
    private DrawableTextView tvLeftAct;//左侧功能按钮
    private TextView giftInfo;
    private UserInfo userInfo;

    public void setOnHeadWearCallback(OnHeadWearCallback onHeadWearCallback) {
        this.onHeadWearCallback = onHeadWearCallback;
    }

    public static DressUpFragment newInstance(int type, boolean isMySelf, long targetUid) {
        DressUpFragment dressFragment = new DressUpFragment();
        Bundle dressBundle = new Bundle();
        dressBundle.putInt("type", type);
        dressBundle.putLong("targetUid", targetUid);
        dressBundle.putBoolean("isMySelf", isMySelf);
        dressFragment.setArguments(dressBundle);
        return dressFragment;
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_dress_up_list;
    }

    @Override
    protected void getMyArguments() {
        if (getArguments() != null) {
            type = getArguments().getInt("type", DRESS_HEADWEAR);
            userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(getArguments().getLong("targetUid"));
            isMySelf = getArguments().getBoolean("isMySelf", false);
        }
    }

    @Override
    protected void initMyView() {
        giftInfo = mView.findViewById(R.id.text);
        llAction = mView.findViewById(R.id.ll_dress_up_action);
        tvLeftAct = mView.findViewById(R.id.tv_dress_up_left_action);
    }

    @Override
    public void addItemDecoration() {
        if (rvList != null) {
            rvList.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.top = 2;
                    if (parent.getChildAdapterPosition(view) % 2 == 0) {
                        outRect.right = 2;
                    }
                }
            });
        }
    }

    @Override
    protected RecyclerView.LayoutManager initManager() {
        return new GridLayoutManager(mContext, 2);
    }

    @Override
    protected DressUpListAdapter initAdpater() {
        return new DressUpListAdapter(type, isMySelf);
    }

    @Override
    protected void initClickListener() {
        tvLeftAct.setOnClickListener(this);
        if (mAdapter != null) {
            mAdapter.setOnDressUpClickListener(this);
        }
    }

    @Override
    public void initData() {
        getMvpPresenter().getDressUpData(isMySelf, type, mPage, pageSize);
    }

    @Override
    public void getDressUpListSuccess(ServiceResult<List<DressUpBean>> result) {
        String emtpy;
        if (isMySelf) {
            emtpy = getString(type == DRESS_HEADWEAR ? R.string.txt_my_headwear_emtpy : R.string.txt_my_car_emtpy);
        } else {
            emtpy = getString(type == DRESS_HEADWEAR ? R.string.txt_headwear_emtpy : R.string.txt_car_emtpy);
        }
        if (mPage == Constants.PAGE_START) {//刷新
            if (result == null || !result.isSuccess() || ListUtils.isListEmpty(result.getData())) {
                if (llAction.getVisibility() == View.VISIBLE) {
                    llAction.setVisibility(View.GONE);
                }
            }
        }
        dealSuccess(result, emtpy);
    }

    @Override
    public void getDressUpListFail(Exception e) {
        if (llAction.getVisibility() == View.VISIBLE) {
            llAction.setVisibility(View.GONE);
        }
        dealFail(e);
    }

    @Override
    public void onClick(View v) {
        if (mAdapter == null) {
            return;
        }
        DressUpBean data = mAdapter.getCurrentSelectData();
        if (data == null || userInfo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_dress_up_left_action:
                giveGift(type == DRESS_HEADWEAR ? data.getHeadwearName() : data.getCarName(),
                        (type == DRESS_HEADWEAR ? data.getHeadwearId() : data.getCarId()) + "");
                break;
            default:
                break;

        }
    }

    private void giveGift(String dressName, String goodsId) {
        getDialogManager().showOkCancelDialog("确认购买“" + dressName + "”并赠送给" + userInfo.getNick() + "？", true, new DialogManager.AbsOkDialogListener() {

            @Override
            public void onOk() {
                requestGift(String.valueOf(userInfo.getUid()), goodsId);
            }
        });
    }

    private void requestGift(String uid, String goodsId) {
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(goodsId)) {
            toast("参数异常");
            return;
        }
        getDialogManager().showProgressDialog(getActivity());
        getMvpPresenter().giveGift(type, uid, goodsId);
    }

    @Override
    public void giftGiveSuccess() {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast("赠送礼物成功");
    }

    @Override
    public void giftGiveFail(String msg) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(msg);
    }

    @Override
    public void onPurseDressUpSuccess(int purseType) {
        if (purseType == 1) {
            SingleToastUtil.showToast(getString(R.string.txt_purchase_success));
        } else if (purseType == 2) {
            SingleToastUtil.showToast(getString(R.string.txt_successful_renewal));
        }
        if (isMySelf) {
            onReloadData();
            CoreManager.getCore(IUserCore.class).requestUserInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        } else {
            if (purseType == 1) {
                CoreManager.getCore(IUserCore.class).requestUserInfo(
                        CoreManager.getCore(IAuthCore.class).getCurrentUid());
            }
        }
    }

    @Override
    public void onPurseDressUpFail(String error) {
        SingleToastUtil.showToast(error);
    }


    @Override
    public void onChangeDressUpStateSuccess(int dressUpId) {
        getDialogManager().dismissDialog();
        if (dressUpId == -1) {
            SingleToastUtil.showToast(getString(R.string.txt_cancel_use_success));
            if (tvLeftAct != null) {
                tvLeftAct.setText(R.string.txt_use);
            }
            if (mAdapter != null && mAdapter.getCurrentSelectData() != null) {
                mAdapter.getCurrentSelectData().setIsPurse(1);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            SingleToastUtil.showToast(getString(R.string.txt_use_success));
            if (tvLeftAct != null) {
                tvLeftAct.setText(R.string.txt_cancel_use);
            }
            if (mAdapter != null && mAdapter.getCurrentSelectData() != null) {
                mAdapter.resetUseState(dressUpId);
            }
        }
        CoreManager.getCore(IUserCore.class).requestUserInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
    }

    @Override
    public void onChangeDressUpStateFail(String error) {
        getDialogManager().dismissDialog();
        SingleToastUtil.showToast(error);
    }

    @Override
    public void onDressUpItemClickListener(DressUpBean item) {
        if (item == null) {
            return;
        }
        dealWithSelectStae(item);
        if (onHeadWearCallback != null) {
            if (type == DRESS_HEADWEAR) {
                onHeadWearCallback.onHeadWearChangeListener(item.getPicUrl());
            }
        }
    }

    /**
     * 根据选中的装扮的信息修改底部功能按钮状态
     */
    private void dealWithSelectStae(DressUpBean item) {
        //默认不显示
        if (llAction.getVisibility() == View.GONE) {
            llAction.setVisibility(View.VISIBLE);
        }
        giftInfo.setText(MessageFormat.format("{0}/{1}天", item.getGoldPrice(), item.getEffectiveTime()));
    }

    @Override
    public void onCarTryClickListener(String vggUrl) {
        if (onHeadWearCallback != null) {
            onHeadWearCallback.onCarTryListener(vggUrl);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHeadWearCallback) {
            setOnHeadWearCallback((OnHeadWearCallback) context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onHeadWearCallback = null;
    }

}
