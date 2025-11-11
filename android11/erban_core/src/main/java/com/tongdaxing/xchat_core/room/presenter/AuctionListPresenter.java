package com.tongdaxing.xchat_core.room.presenter;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionListUserInfo;
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.room.view.IAuctionListView;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

import io.reactivex.functions.BiConsumer;

/**
 * @author xiaoyu
 * @date 2017/12/25
 */

public class AuctionListPresenter extends AbstractMvpPresenter<IAuctionListView> {
    private static final String TAG = "AuctionPresenter";
    private AuctionModel auctionModel;

    public AuctionListPresenter() {
        auctionModel = AuctionModel.get();
    }


    public void requestWeekAuctionList(long roomUid) {
        auctionModel.requestWeekAuctionList(roomUid).subscribe(new BiConsumer<ServiceResult<List<AuctionListUserInfo>>, Throwable>() {
            @Override
            public void accept(ServiceResult<List<AuctionListUserInfo>> auctionListUserInfoResult,
                               Throwable throwable) throws Exception {
                if (throwable == null && auctionListUserInfoResult != null && getMvpView() != null) {
                    List<AuctionListUserInfo> data = auctionListUserInfoResult.getData();
                    getMvpView().onRequestWeekAuctionList(data);
                }
            }
        });
    }

    public void requestTotalAuctionList(long roomUid) {
        auctionModel.requestTotalAuctionList(roomUid).subscribe(new BiConsumer<ServiceResult<List<AuctionListUserInfo>>, Throwable>() {
            @Override
            public void accept(ServiceResult<List<AuctionListUserInfo>> auctionListUserInfoResult,
                               Throwable throwable) throws Exception {
                if (throwable == null && auctionListUserInfoResult != null && getMvpView() != null) {
                    List<AuctionListUserInfo> data = auctionListUserInfoResult.getData();
                    getMvpView().onRequestTotalAuctionList(data);
                }
            }
        });
    }
}
























