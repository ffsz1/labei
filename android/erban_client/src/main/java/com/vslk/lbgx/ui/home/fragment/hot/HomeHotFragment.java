package com.vslk.lbgx.ui.home.fragment.hot;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.home.HomeInfo;
import com.vslk.lbgx.presenter.home.HomeHotPresenter;
import com.vslk.lbgx.room.AVRoomActivity;

import org.greenrobot.eventbus.EventBus;


/**
 * 首页热门分类列表页
 *
 * @author zwk
 */
@CreatePresenter(HomeHotPresenter.class)
public class HomeHotFragment extends BaseHTItemFragment {
    @Override
    public void initData() {
        getMvpPresenter().getSquareRoomId();
        getMvpPresenter().getHomeHotRoomList(mPage);
    }

    @Override
    protected RecyclerView.LayoutManager initManager() {
        return new GridLayoutManager(mContext, 2);
    }

    @Override
    public void getHomeHotRoomSuccess(HomeInfo result) {
        EventBus.getDefault().post(new Hot.upTab());
        srlRefresh.finishRefresh();
        if (result != null) {
            if (mPage == Constants.PAGE_START) {
                if (!ListUtils.isListEmpty(result.agreeRecommendRooms)) {
                    //设置首页推荐房
//                    head.setNewData(result.agreeRecommendRooms);
                    EventBus.getDefault().post(new Hot.TopList(result.agreeRecommendRooms));
                }
                //设置列表数据
//                if (!ListUtils.isListEmpty(result.listRoom)) {
//                    //添加第一个条目
//                    HomeRoom home = new HomeRoom(0);
//                    home.setItemType(2);
//                    result.listRoom.add(0, home);
//                }
                if (!ListUtils.isListEmpty(result.listRoom)) {
                    mAdapter.setNewData(result.listRoom);
                }
//                if (ListUtils.isListEmpty(result.agreeRecommendRooms) && ListUtils.isListEmpty(result.listRoom)) {
//                    showNoData("暂时还没有热门房间哦");
//                    srlRefresh.finishLoadmoreWithNoMoreData();
//                }
            } else {
                if (ListUtils.isListEmpty(result.listRoom)) {
                    srlRefresh.finishLoadmoreWithNoMoreData();
                } else {
                    mAdapter.addData(result.listRoom);
                    srlRefresh.finishLoadmore();
                }
            }
        } else {
            if (mPage == Constants.PAGE_START) {
                showNoData("暂时还没有数据哦");
            } else {
                mPage--;
                srlRefresh.finishLoadmoreWithNoMoreData();
            }
        }
        hideStatus();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData()) && mAdapter.getData().size() > position) {
            long uid = mAdapter.getData().get(position).getUid();
            if (uid == 0) {
                return;
            }
            AVRoomActivity.start(getActivity(), uid);
        }
    }
}
