package com.vslk.lbgx.presenter.home;

import com.vslk.lbgx.model.attention.AttentionModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

public class HomeAttentionPresenter extends AbstractMvpPresenter<IHomeAttentionView> {

    private AttentionModel attentionModel;

    public HomeAttentionPresenter() {
        if (this.attentionModel == null) {
            this.attentionModel = new AttentionModel();
        }
    }

    /**
     * 点击关注按钮关注
     */
    public void userAttention(String roomId, int position) {
        attentionModel.userAttention(roomId, new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null){
                    getMvpView().userAttentionFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null){
                    if (response.isSuccess()){
                        if (getMvpView() != null){
                            getMvpView().userAttentionSuccess(position);
                        }
                    }else {
                        if (getMvpView() != null){
                            getMvpView().userAttentionFail(response.getMessage());
                        }
                    }
                }else {
                    if (getMvpView() != null){
                        getMvpView().userAttentionFail("数据异常！");
                    }
                }
            }
        });
    }

    /**
     * 获取关注页面的关注房间列表
     */
    public void getRoomAttentionByUid(int pageNum,int pageSize) {

        attentionModel.getRoomAttentionByUid(CoreManager.getCore(IAuthCore.class).getCurrentUid() + "", pageNum, pageSize, new OkHttpManager.MyCallBack<ServiceResult<List<AttentionInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null){
                    getMvpView().getRoomAttentionListFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<AttentionInfo>> response) {
                if (response != null){
                    if (response.isSuccess()){
                        if (getMvpView() != null){
                            getMvpView().getRoomAttentionListSuccess(response);
                        }
                    }else {
                        if (getMvpView() != null){
                            getMvpView().getRoomAttentionListFail(response.getMessage());
                        }
                    }
                }else {
                    if (getMvpView() != null){
                        getMvpView().getRoomAttentionListFail("数据异常！");
                    }
                }
            }
        });
    }


    /**
     * 获取关注页面的关注房间列表
     */
    public void getRoomRecommendList(int pageNum,int pageSize) {
        attentionModel.getRecommendUsers(CoreManager.getCore(IAuthCore.class).getCurrentUid() + "", pageNum, pageSize, new OkHttpManager.MyCallBack<ServiceResult<List<AttentionInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null){
                    getMvpView().getRoomRecommendListFail(e);
                }
            }
            @Override
            public void onResponse(ServiceResult<List<AttentionInfo>> response) {
                if (response != null){
                    if (response.isSuccess()){
                        if (getMvpView() != null){
                            getMvpView().getRoomRecommendListSuccess(response);
                        }
                    }else {
                        if (getMvpView() != null){
                            getMvpView().getRoomRecommendListFail(new Exception(response.getMessage()));
                        }
                    }
                }else {
                    if (getMvpView() != null){
                        getMvpView().getRoomRecommendListFail(new Exception("数据异常！"));
                    }
                }
            }
        });
    }
}
