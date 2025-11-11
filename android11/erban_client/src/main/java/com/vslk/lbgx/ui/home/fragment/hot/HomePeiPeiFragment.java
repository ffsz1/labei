package com.vslk.lbgx.ui.home.fragment.hot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.vslk.lbgx.presenter.home.HomeHotPresenter;
import com.vslk.lbgx.ui.home.adpater.HomeHotAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


/**
 * 首页热门分类列表页
 *
 * @author zwk
 */
@CreatePresenter(HomeHotPresenter.class)
public class HomePeiPeiFragment extends BaseHTItemFragment {

    private int gender = 0;

    @Override
    public void initData() {
        getMvpPresenter().getBestCompanies(mPage, gender);
    }

    @Override
    protected RecyclerView.LayoutManager initManager() {
        return new GridLayoutManager(mContext, 2);
    }

    @Override
    public void getPeiPeiList(List<HomeRoom> result) {
        EventBus.getDefault().post(new Hot.upTab());
        srlRefresh.finishRefresh();
        if (result != null && result.size() > 0) {
            if (mPage == Constants.PAGE_START) {
                srlRefresh.finishRefresh();
                mAdapter.setNewData(result);
                if (result.size() < Constants.PAGE_HOME_HOT_SIZE)
                    srlRefresh.finishLoadmoreWithNoMoreData();
            } else {
                if (ListUtils.isListEmpty(result)) {
                    srlRefresh.finishLoadmoreWithNoMoreData();
                } else {
                    mAdapter.addData(result);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGenderSelect(GenderFilter genderFilter) {
        if (genderFilter != null) {
            mPage = Constants.PAGE_START;
            gender = genderFilter.getGender();
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        HomeHotAdapter.closePlay();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public static class GenderFilter {
        private int gender;

        public GenderFilter(int gender) {
            this.gender = gender;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }
    }
}
