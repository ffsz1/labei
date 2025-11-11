package com.vslk.lbgx.ui.home.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseMvpListFragment;
import com.vslk.lbgx.presenter.home.HomeListPresenter;
import com.vslk.lbgx.presenter.home.IHomeListView;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.home.adpater.HomeNormalAdapter;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.vslk.lbgx.ui.home.fragment.hot.Hot;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/25
 * 描述        首页遇见页面
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(HomeListPresenter.class)
public class HomeMeetYouFragment extends BaseMvpListFragment<HomeNormalAdapter, IHomeListView, HomeListPresenter>
        implements BaseQuickAdapter.OnItemClickListener, IHomeListView {
    //    private HomeMeetHeadView head;
    private int tabId = -1;

    @Override
    protected void getMyArguments() {
        tabId = getArguments().getInt("tabId", -1);
    }

    @Override
    protected void initMyView() {
        hasDefualLoadding = false;
        srlRefresh.setEnableRefresh(true);
        srlRefresh.setEnableLoadmore(true);
    }

    @Override
    protected HomeNormalAdapter initAdpater() {
        return new HomeNormalAdapter(mContext);
    }

    @Override
    protected void addHead() {
//        head = new HomeMeetHeadView(mContext);
//        mAdapter.setHeaderAndEmpty(true);
//        mAdapter.addHeaderView(head);
    }

    @Override
    protected void initClickListener() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onLazyLoadData() {
        super.onLazyLoadData();
        mPage = Constants.PAGE_START;
        initData();
    }

    @Override
    public void onReloadData() {
        showLoading();
        initData();
    }

    @Override
    public void initData() {
        getMvpPresenter().getHomeRoomListById(tabId, mPage, pageSize);
    }

    @Override
    public void getHomeRoomListByIdSuccess(ServiceResult<List<HomeRoom>> result) {
        srlRefresh.finishRefresh();
        if (result != null && !ListUtils.isListEmpty(result.getData())) {
            hideStatus();
            if (mPage == Constants.PAGE_START) {
                srlRefresh.finishRefresh();
//                if (result.getData().size() > 3) {
//                    List<HomeRoom> headRooms = new ArrayList<>(result.getData().subList(0, 3));
//                    List<HomeRoom> homeRooms = new ArrayList<>(result.getData().subList(3, result.getData().size()));
                //设置数据
//                    head.setNewData(headRooms);
//                    mAdapter.setNewData(homeRooms);
//                } else {
//                    mAdapter.setNewData(result.getData());
//                }
                List<HomeRoom> tList = result.getData();
                if (!ListUtils.isListEmpty(tList) && tList.size() > 3) {
                    setTopData(tList.subList(0, 3));
                    mAdapter.setNewData(tList.subList(3, tList.size()));
                } else {
                    setTopData(tList);
                    mAdapter.setNewData(null);
                }
//                mAdapter.setNewData(result.getData());
            } else {
                if (ListUtils.isListEmpty(result.getData())) {
                    srlRefresh.finishLoadmoreWithNoMoreData();
                } else {
                    mAdapter.addData(result.getData());
                    srlRefresh.finishLoadmore();
                }
            }
        } else {
            if (mPage == Constants.PAGE_START) {
                srlRefresh.finishRefresh();
                showNoData("该分类下面还没有房间哦！");
                setTopData(null);
                mAdapter.setNewData(null);
            } else {
                mPage--;
                srlRefresh.finishLoadmoreWithNoMoreData();
            }
        }
    }

    @Override
    public void getHomeRoomListByIdFail(Exception error) {
        srlRefresh.finishRefresh();
        dealFail(error, "该分类下面还没有房间哦！");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            HomeRoom homeRoom = mAdapter.getData().get(position);
            if (homeRoom == null) {
                return;
            }
            AVRoomActivity.start(mContext, homeRoom.getUid());
        }
    }
}
