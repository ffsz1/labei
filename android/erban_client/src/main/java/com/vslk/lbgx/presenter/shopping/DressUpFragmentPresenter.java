package com.vslk.lbgx.presenter.shopping;

import com.vslk.lbgx.model.shopping.DressUpModel;
import com.vslk.lbgx.ui.me.shopping.view.IDressUpFragmentView;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;

import java.util.List;

import io.realm.RealmList;

/**
 * 装扮商城列表的Presenter
 */
public class DressUpFragmentPresenter extends AbstractMvpPresenter<IDressUpFragmentView> {

    private DressUpModel dressUpModel;


    public DressUpFragmentPresenter() {
        if (this.dressUpModel == null) {
            this.dressUpModel = new DressUpModel();
        }
    }

    public String getPhotosId(UserInfo userInfo) {
        if (userInfo == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        RealmList<UserPhoto> photo = userInfo.getPrivatePhoto();
        if (!ListUtils.isListEmpty(photo)) {
            for (int i = 0; i < photo.size(); i++) {
                stringBuilder.append(photo.get(i).getPid());
                if (i != photo.size() - 1) {
                    stringBuilder.append(",");
                }
            }
            String ids = stringBuilder.toString();
            LogUtils.e("举报相册:" + ids);
            return ids;
        }
        return "";
    }

    /**
     * 装扮商城旧数据接口获取数据
     *
     * @param type 商品类型
     */
    public void getDressUpData(int type) {

        if (dressUpModel != null) {

            dressUpModel.getDressUpData(type, new OkHttpManager.MyCallBack<ServiceResult<List<DressUpBean>>>() {

                @Override
                public void onError(Exception e) {
                    if (getMvpView() != null) {
                        getMvpView().getDressUpListFail(e.getMessage());
                    }
                }

                @Override
                public void onResponse(ServiceResult<List<DressUpBean>> response) {
                    if (response != null && response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getDressUpList(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getDressUpListFail(response.getMessage());
                        }
                    }
                }
            });
        }
    }

    /**
     * 获取装扮商城列表
     *
     * @param type
     * @param pageNum
     * @param pageSize
     */
    public void getDressUpData(boolean isMyself, int type, int pageNum, int pageSize) {
        if (dressUpModel != null) {
            dressUpModel.getDressUpData(isMyself, type, pageNum, pageSize, new OkHttpManager.MyCallBack<ServiceResult<List<DressUpBean>>>() {
                @Override
                public void onError(Exception e) {
                    if (getMvpView() != null) {
                        getMvpView().getDressUpListFail(e);
                    }
                }

                @Override
                public void onResponse(ServiceResult<List<DressUpBean>> response) {
                    if (getMvpView() != null) {
                        getMvpView().getDressUpListSuccess(response);
                    }
                }
            });
        }
    }

    /**
     * 获取装扮商城列表
     *
     * @param type 0 头饰，1座驾
     */
    public void getMyDressUpData(int type, long currentUid) {
        if (dressUpModel != null) {
            dressUpModel.getMyDressUpData(type, currentUid, new OkHttpManager.MyCallBack<ServiceResult<List<DressUpBean>>>() {
                @Override
                public void onError(Exception e) {
                    if (getMvpView() != null) {
                        if (type == 0) {
                            getMvpView().getHeadWearListSuccessFail(e.getMessage());
                        } else {
                            getMvpView().getCarListSuccessFail(e.getMessage());
                        }
                    }
                }

                @Override
                public void onResponse(ServiceResult<List<DressUpBean>> response) {
                    if (response != null && response.isSuccess()) {
                        if (getMvpView() != null) {
                            if (type == 0) {
                                getMvpView().getHeadWearListSuccess(response.getData());
                            } else {
                                getMvpView().getCarListSuccess(response.getData());
                            }
                        }
                    } else {
                        if (getMvpView() != null) {
                            if (type == 0) {
                                getMvpView().getHeadWearListSuccessFail(response.getMessage());
                            } else {
                                getMvpView().getCarListSuccessFail(response.getMessage());
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 购买座驾
     *
     * @param type  type 0 头饰 1 座驾
     * @param carId
     */
    public void onPurseDressUp(int type, int purseType, int carId) {
        if (dressUpModel != null) {
            dressUpModel.onPurseDressUp(type, purseType, carId, new OkHttpManager.MyCallBack<Json>() {
                @Override
                public void onError(Exception e) {
                    if (getMvpView() != null) {
                        getMvpView().onPurseDressUpFail(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Json response) {
                    if (response != null) {
                        if (response.num("code") == 200) {
                            if (getMvpView() != null) {
                                getMvpView().onPurseDressUpSuccess(purseType);
                            }
                        } else {
                            if (getMvpView() != null) {
                                getMvpView().onPurseDressUpFail(response.str("message"));
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 修改装扮的使用状态
     *
     * @param type    type 0 头饰 1 座驾
     * @param dressId 装扮id
     */
    public void onChangeDressUpState(int type, int dressId) {
        if (dressUpModel != null) {
            dressUpModel.onChangeDressUpState(type, dressId, new OkHttpManager.MyCallBack<Json>() {
                @Override
                public void onError(Exception e) {
                    if (getMvpView() != null) {
                        getMvpView().onChangeDressUpStateFail(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Json response) {
                    if (response != null) {
                        if (response.num("code") == 200) {
                            //更新用户信息
                            CoreManager.getCore(IUserCore.class).requestUserInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
                            if (getMvpView() != null) {
                                getMvpView().onChangeDressUpStateSuccess(dressId);
                            }
                        } else {
                            if (getMvpView() != null) {
                                getMvpView().onChangeDressUpStateFail(response.str("message"));
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 赠送礼物
     *
     * @param type      类型
     * @param targetUid 被赠送人
     * @param goodsId   礼物id
     */
    public void giveGift(int type, String targetUid, String goodsId) {

        if (dressUpModel != null) {

            dressUpModel.giveGift(type, targetUid, goodsId, new OkHttpManager.MyCallBack<ServiceResult>() {

                @Override
                public void onError(Exception e) {
                    if (getMvpView() != null) {
                        getMvpView().giftGiveFail(e.getMessage());
                    }
                }

                @Override
                public void onResponse(ServiceResult response) {
                    if (response != null && response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().giftGiveSuccess();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().giftGiveFail(response.getMessage());
                        }
                    }
                }
            });
        }
    }

}
