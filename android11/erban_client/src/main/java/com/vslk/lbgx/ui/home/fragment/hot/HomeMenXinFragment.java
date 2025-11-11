package com.vslk.lbgx.ui.home.fragment.hot;

import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.vslk.lbgx.presenter.home.HomeHotPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * 首页热门分类列表页
 *
 * @author zwk
 */
@CreatePresenter(HomeHotPresenter.class)
public class HomeMenXinFragment extends BaseHTItemFragment {

    public static final String TAG = "HomeMenXinFragment";

    @Override
    public void initData() {
        getMvpPresenter().getNewUsers(mPage);
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
}
