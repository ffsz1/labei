package com.vslk.lbgx.ui.message.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.ui.message.adapter.FansViewAdapter;
import com.netease.nim.uikit.NimUIKit;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_core.user.AttentionCore;
import com.tongdaxing.xchat_core.user.AttentionCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.bean.FansInfo;
import com.tongdaxing.xchat_core.user.bean.FansListInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝列表
 *
 * @author chenran
 * @date 2017/10/2
 */
public class FansListFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FansViewAdapter mAdapter;
    private int mCurrentCounter = Constants.PAGE_START;
    private List<FansInfo> mFansInfoList = new ArrayList<>();
    private Context mContext;
    private int mPageType;

    public static FansListFragment newInstance(int pageType) {
        FansListFragment fragment = new FansListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_PAGE_TYPE, pageType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onInitArguments(Bundle bundle) {
        super.onInitArguments(bundle);
        if (bundle != null) {
            mPageType = bundle.getInt(Constants.KEY_PAGE_TYPE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onFindViews() {
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipe_refresh);
    }

    @Override
    public void onSetListener() {
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FansViewAdapter(mFansInfoList, mContext);
        mAdapter.setOnLoadMoreListener(() -> {
            mCurrentCounter++;
            onRefreshing();
        }, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mCurrentCounter = Constants.PAGE_START;
            onRefreshing();
        });
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            FansInfo fansInfo = mAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.rly:
                    if (Constants.OFFICIAL == fansInfo.getUid()) {
                        return;
                    }
//                    UserInfoActivity.start(mContext, fansInfo.getUid());
                    NimUIKit.startP2PSession(getActivity(), String.valueOf(fansInfo.getUid()));
                    break;
                case R.id.attention_img:
                    getDialogManager().showProgressDialog(mContext, getString(R.string.waiting_text));
                    if (fansInfo.isMyFriend()) {
                        CoreManager.getCore(IPraiseCore.class).cancelPraise(fansInfo.getUid(), true);
                    } else {
                        CoreManager.getCore(IPraiseCore.class).praise(fansInfo.getUid());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void onRefreshing() {
        CoreManager.getCore(AttentionCore.class).getFansList(CoreManager.getCore(IAuthCore.class).getCurrentUid(),
                mCurrentCounter, Constants.PAGE_SIZE, mPageType);
    }

    @Override
    public void initiate() {
        showLoading();
        onRefreshing();
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_fans_list;
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraise(long uid) {
        getDialogManager().dismissDialog();
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            for (int position = 0; position < mAdapter.getData().size(); position++) {
                FansInfo fansInfo = mAdapter.getData().get(position);
                if (fansInfo.getUid() == uid) {
                    mAdapter.notifyItemChanged(position);
                }
            }
        }
        toast(getString(R.string.fan_success));
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long likedUid, boolean showNotice) {
        getDialogManager().dismissDialog();
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            for (int position = 0; position < mAdapter.getData().size(); position++) {
                FansInfo fansInfo = mAdapter.getData().get(position);
                if (fansInfo.getUid() == likedUid) {
                    mAdapter.notifyItemChanged(position);
                }
            }
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraiseFaith(String error) {
        toast(error);
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = AttentionCoreClient.class)
    public void onGetMyFansList(FansListInfo fansListInfo, int pageType, int page) {
        mCurrentCounter = page;
        if (pageType == mPageType) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (fansListInfo == null || ListUtils.isListEmpty(fansListInfo.getFansList())) {
                //第一页
                if (mCurrentCounter == Constants.PAGE_START) {
                    showNoData(getString(R.string.no_fan_text));
                } else {
                    mAdapter.loadMoreEnd(true);
                }
            } else {
                hideStatus();
                if (mCurrentCounter == Constants.PAGE_START) {
                    mFansInfoList.clear();
                    List<FansInfo> fansList = fansListInfo.getFansList();
                    mFansInfoList.addAll(fansList);
                    mAdapter.setNewData(mFansInfoList);
                    if (fansList.size() < Constants.PAGE_SIZE) {
                        mAdapter.setEnableLoadMore(false);
                    }
                    return;
                }
                mAdapter.loadMoreComplete();
                mAdapter.addData(fansListInfo.getFansList());
            }
        }
    }

    @CoreEvent(coreClientClass = AttentionCoreClient.class)
    public void onGetMyFansListFail(String error, int pageType, int page) {
        mCurrentCounter = page;
        if (pageType == mPageType) {
            if (mCurrentCounter == Constants.PAGE_START) {
                mSwipeRefreshLayout.setRefreshing(false);
                showNetworkErr();
            } else {
                mAdapter.loadMoreFail();
                toast(error);
            }
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        mCurrentCounter = Constants.PAGE_START;
        onRefreshing();
    }


    @Override
    public void onReloadData() {
        super.onReloadData();
        mCurrentCounter = Constants.PAGE_START;
        showLoading();
        onRefreshing();
    }
}
