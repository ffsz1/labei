package com.vslk.lbgx.ui.home.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseMvpListFragment;
import com.vslk.lbgx.presenter.home.HomeAttentionPresenter;
import com.vslk.lbgx.presenter.home.IHomeAttentionView;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.home.adpater.HomeRecommendAttentionAdapter;
import com.vslk.lbgx.ui.home.view.HomeAttentionHeadView;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

/**
 * 首页关注分类列表页
 *
 * @author zwk
 */
@CreatePresenter(HomeAttentionPresenter.class)
public class HomeAttentionFragment extends BaseMvpListFragment<HomeRecommendAttentionAdapter, IHomeAttentionView, HomeAttentionPresenter> implements
        IHomeAttentionView, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private HomeAttentionHeadView head;

    @Override
    protected void initMyView() {
        setClickReload(false);
        hasDefualLoadding = false;
        srlRefresh.setEnableRefresh(true);
        srlRefresh.setEnableLoadmore(false);
        srlRefresh.setEnableAutoLoadmore(false);
        srlRefresh.setEnableOverScrollDrag(false);
        srlRefresh.setEnableOverScrollBounce(false);

        rvList.setPadding(0, ConvertUtils.dp2px(10), 0, 0);
    }

    @Override
    protected RecyclerView.LayoutManager initManager() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    protected HomeRecommendAttentionAdapter initAdpater() {
        return new HomeRecommendAttentionAdapter();
    }

    @Override
    protected void addHead() {
        rvList.setBackgroundResource(R.color.white);
        head = new HomeAttentionHeadView(mContext);
        mAdapter.setEnableLoadMore(false);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(head);

        mAdapter.setOnItemChildClickListener(this);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogin(AccountInfo accountInfo) {
        initData();
    }

    @Override
    public void initData() {
        getMvpPresenter().getRoomAttentionByUid(mPage, pageSize);
        getMvpPresenter().getRoomRecommendList(mPage, pageSize);
    }

    @Override
    protected void initClickListener() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onLazyLoadData() {
        super.onLazyLoadData();
        mPage = Constants.PAGE_START;
//        showLoading();//FIXME 注释此行代码，临时解决方案。已登录用户或者第一次进入APP有一定的概率关注页会一直处于加载状态（原因是getMvpView没有完成初始化，未能回到V层的代码）
        initData();
    }

    @Override
    public void getRoomAttentionListSuccess(ServiceResult<List<AttentionInfo>> response) {
        srlRefresh.finishRefresh();
        head.setListData(response.getData());
    }

    @Override
    public void getRoomAttentionListFail(String error) {
        srlRefresh.finishRefresh();
        head.setListData(null);
    }

    @Override
    public void getRoomRecommendListSuccess(ServiceResult<List<AttentionInfo>> response) {
        srlRefresh.finishRefresh();
        hideStatus();
        if (response != null && !ListUtils.isListEmpty(response.getData())) {
            if (mPage == Constants.PAGE_START) {
                head.showNoData(false, "");
                srlRefresh.finishRefresh();
                mAdapter.setNewData(response.getData());
            } else {
                if (ListUtils.isListEmpty(response.getData())) {
                    srlRefresh.finishLoadmoreWithNoMoreData();
                } else {
                    mAdapter.addData(response.getData());
                    srlRefresh.finishLoadmore();
                }
            }
        } else {
            if (mPage == Constants.PAGE_START) {
                srlRefresh.finishRefresh();
                head.showNoData(true, "");
            } else {
                mPage--;
                srlRefresh.finishLoadmore();
            }
        }
    }

    @Override
    public void getRoomRecommendListFail(Exception error) {
        srlRefresh.finishRefresh();
        hideStatus();
        if (mPage == Constants.PAGE_START) {
            srlRefresh.finishRefresh(false);
            if (NetworkUtil.isNetAvailable(getActivity())) {
                head.showNoData(true, "");
            } else {
                head.showNoData(true, "网络异常，请检查网络!");
            }
        } else {
            mPage--;
            srlRefresh.finishLoadmore(false);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData()) && mAdapter.getData().size() > position) {
            UserInfoActivity.start(mContext, mAdapter.getData().get(position).getUid());
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            AttentionInfo homeRoom = mAdapter.getData().get(position);
            if (homeRoom == null) {
                return;
            }
            getDialogManager().showOkCancelDialog("确定要关注" + homeRoom.getNick() + "吗?", true, new DialogManager.OkCancelDialogListener() {

                @Override
                public void onCancel() {

                }

                @Override
                public void onOk() {
                    roomAttention(homeRoom, position);
                }
            });
        }
    }

    /**
     * 关注房间
     */
    private void roomAttention(AttentionInfo homeRoom, int position) {
        getDialogManager().showProgressDialog(getActivity());
        getMvpPresenter().userAttention(String.valueOf(homeRoom.getUid()), position);
    }

    @Override
    public void userAttentionSuccess(int position) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            AttentionInfo homeRoom = mAdapter.getData().get(position);
            //加入到头部
            head.addData(homeRoom);
            //从当前列表删除
            mAdapter.remove(position);
        }
    }

    @Override
    public void userAttentionFail(String error) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(error);
    }
}
