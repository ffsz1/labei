package com.vslk.lbgx.model.attention;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * 关注相关功能model
 */
public class AttentionModel extends BaseMvpModel{

    /**
     * 关注用户
     */
    public void userAttention(String likedUid, OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();

        params.put("type", String.valueOf(1));
        params.put("likedUid", String.valueOf(likedUid));
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.praise(), params, myCallBack);
    }

    /**
     * 检查是否关注房间
     * @param uid
     * @param roomId
     * @param myCallBack
     */
    public void checkAttention(String uid, String roomId, OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",uid);
        params.put("roomId",roomId);
        params.put("ticket",CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.checkAttention(),params,myCallBack);
    }

    /**
     * 取消房间关注
     * @param uid
     * @param roomId
     * @param myCallBack
     */
    public void deleteAttention(String uid, String roomId, OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",uid);
        params.put("roomId",roomId);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.deleteAttention(),params,myCallBack);
    }

    /**
     * 获取关注房间列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @param myCallBack
     */
    public void getRoomAttentionByUid(String uid,int pageNum,int pageSize,OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",uid);
        params.put("pageNo",String.valueOf(pageNum));
        params.put("pageSize",String.valueOf(pageSize));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getAllFans(), params, myCallBack);
    }

    /**
     * 获取关注房间列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @param myCallBack
     */
    public void getRecommendUsers(String uid,int pageNum,int pageSize,OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",uid);
        params.put("pageNum",String.valueOf(pageNum));
        params.put("pageSize",String.valueOf(pageSize));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getRecommendUsers(),params,myCallBack);
    }
}
