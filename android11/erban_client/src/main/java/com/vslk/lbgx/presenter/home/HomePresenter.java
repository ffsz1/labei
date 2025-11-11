package com.vslk.lbgx.presenter.home;

import com.vslk.lbgx.model.home.HomeModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.home.BannerInfo;
import com.tongdaxing.xchat_core.home.HomeIcon;
import com.tongdaxing.xchat_core.home.RecommendBannerInfo;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter extends AbstractMvpPresenter<IHomeView> {
    HomeModel homeModel;

    public HomePresenter() {
        if (homeModel == null) {
            this.homeModel = new HomeModel();
        }
    }


    public void getTopBanner() {
        homeModel.getHomeRoomBanner(new OkHttpManager.MyCallBack<ServiceResult<List<BannerInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getHomeBannerFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<BannerInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getHomeBannerSuccess(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getHomeBannerFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getHomeBannerFail("数据异常");
                    }
                }
            }
        });
    }

    //获取首页分类列表数据
    public void getHomeTabList() {
        homeModel.getHomeTabMenuList(new OkHttpManager.MyCallBack<ServiceResult<List<TabInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getHomeTabListFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<TabInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getHomeTabListSuccess(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getHomeTabListFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getHomeTabListFail("数据异常");
                    }
                }
            }
        });
    }

    public void getHomeRecommendBanner() {
        homeModel.getRecommendBannerData(new OkHttpManager.MyCallBack<ServiceResult<List<RecommendBannerInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (e != null) {
                    getMvpView().getRecommendBannerFailure();
                }
            }

            @Override
            public void onResponse(ServiceResult<List<RecommendBannerInfo>> response) {
                if (response == null || ListUtils.isListEmpty(response.getData())) {
                    onError(new Exception());
                    return;
                }

                if (response.isSuccess()) {
                    getMvpView().getRecommendBannerSuccess(convertInfo(response.getData()));
                } else {
                    onError(new Exception());
                }
            }
        });
    }

    private List<BannerInfo> convertInfo(List<RecommendBannerInfo> recommendBannerInfoList) {
        List<BannerInfo> list = new ArrayList<>();
        for (int i = 0; i < recommendBannerInfoList.size(); i++) {
            BannerInfo bannerInfo = new BannerInfo();
            bannerInfo.setBannerId(recommendBannerInfoList.get(i).getActId());
            bannerInfo.setSkipUri(recommendBannerInfoList.get(i).getSkipUrl());
            bannerInfo.setSkipType(recommendBannerInfoList.get(i).getSkipType());
            bannerInfo.setBannerPic(recommendBannerInfoList.get(i).getAlertWinPic());
            bannerInfo.setBannerName(recommendBannerInfoList.get(i).getActName());
            list.add(bannerInfo);
        }
        return list;
    }

    /**
     * 获取首页广告数据
     */
    public void getHomeBanner() {
        homeModel.getHomeRoomBanner(new OkHttpManager.MyCallBack<ServiceResult<List<BannerInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getHomeBannerFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<BannerInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getHomeBannerSuccess(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getHomeBannerFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getHomeBannerFail("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 获取首页icons标签
     */
    public void getHomeIconsList() {

        homeModel.getHomeIconsList(new OkHttpManager.MyCallBack<ServiceResult<List<HomeIcon>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().setupHomeIconsFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<HomeIcon>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().setupHomeIconsSuccessView(response.getData() == null ? new ArrayList<HomeIcon>() : response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().setupHomeIconsFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().setupHomeIconsFailView("返回数据异常");
                    }
                }
            }
        });
    }

}
