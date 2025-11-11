package com.vslk.lbgx.presenter.rankinglist;

import com.vslk.lbgx.model.rank.RankingListModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.bean.RankingInfo;
import com.tongdaxing.xchat_core.bean.UserLevelInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

public class RankingListPresenter extends AbstractMvpPresenter<IRankingListView> {
    private RankingListModel dataSource;

    public RankingListPresenter() {
        dataSource = new RankingListModel();
    }

    /**
     * 刷新数据
     *
     * @param type     排行榜类型 0巨星榜，1贵族榜，2房间榜
     * @param dateType 榜单周期类型 0日榜，1周榜，2总榜
     */
    public void refreshData(int type, int dateType) {
        getData(type, dateType);
    }

    private void getData(int type, int dateType) {
        dataSource.getRankingList(type, dateType, new OkHttpManager.MyCallBack<List<RankingInfo>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().setupFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(List<RankingInfo> response) {
                if (getMvpView() != null) {
                    getMvpView().setupSuccessView(response);
                }
            }
        });
    }

    /**
     * 获取用户等级与魅力
     */
    public void getUserLevel(String url) {

        dataSource.getUserLevel(url, new OkHttpManager.MyCallBack<ServiceResult<UserLevelInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getUserLevelFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<UserLevelInfo> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().getUserLevelSuccess(response.getData());
                    }
                } else {
                    getMvpView().getUserLevelFail(response.getMessage());
                }
            }
        });
    }

}
