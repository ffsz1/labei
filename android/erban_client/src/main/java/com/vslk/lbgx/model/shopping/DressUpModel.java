package com.vslk.lbgx.model.shopping;

import com.vslk.lbgx.ui.me.shopping.activity.DressUpMallActivity;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 装扮商城的 Model类
 *
 * @author zwk 2018/10/16
 */
public class DressUpModel extends BaseMvpModel {

    public List<TabInfo> getTabInfos() {
        List<TabInfo> tabInfoList = new ArrayList<>();
        tabInfoList.add(new TabInfo(1, "座驾"));
        tabInfoList.add(new TabInfo(2, "头饰"));
        return tabInfoList;
    }

    /**
     * 使用旧接口，获取装扮商城数据
     */
    public void getDressUpData(int type, OkHttpManager.MyCallBack<ServiceResult<List<DressUpBean>>> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        param.put("pageNum", "1");
        param.put("pageSize", "50");

        postRequest(type == 0 ? UriProvider.headWearList() : UriProvider.carList(), param, callBack);
    }

    /**
     * 获取装扮商城列表数据
     *
     * @param isMyself true 我的装扮商城  false 装扮商城
     * @param type     类型 0 头饰 1 座驾
     * @param pageNum  页码
     * @param pageSize 一页item数
     * @param callBack 回调结果
     */
    public void getDressUpData(boolean isMyself, int type, int pageNum, int pageSize, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("pageNum", pageNum + "");
        params.put("pageSize", pageSize + "");
        String url = "";
        if (isMyself) {
            url = (type == 0 ? UriProvider.getMyHeadWearList() : UriProvider.getMyCarList());
        } else {
            url = (type == 0 ? UriProvider.getHeadWearList() : UriProvider.getCarList());
        }
        postRequest(url, params, callBack);
    }

    /**
     * 获取当前用户的头饰与座驾
     *
     * @param type 类型
     * @param callBack 回调
     */
    public void getMyDressUpData(int type, long currentUid, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        params.put("queryUid", String.valueOf(currentUid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        params.put("pageNum", "1");
        params.put("pageSize", "50");

        postRequest(type == 0 ? UriProvider.getMyHeadWearList() : UriProvider.getMyCarList(), params, callBack);
    }

    /**
     * 购买装扮接口
     *
     * @param type      0 头饰 1座驾
     * @param purseType 购买类型: 1 购买 2 续费
     * @param dressUpId 装扮id
     */
    public void onPurseDressUp(int type, int purseType, int dressUpId, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket() + "");
        params.put("type", purseType + "");
        params.put(type == 0 ? "headwearId" : "carId", dressUpId + "");
        postRequest(type == 0 ? UriProvider.purseHeadWear() : UriProvider.purseCar(), params, callBack);
    }

    /***
     * 修改装扮的状态
     * @param type 0 头饰 1 座驾
     * @param dressId 对应装扮id
     * @param callBack
     */
    public void onChangeDressUpState(int type, int dressId, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        //如果为空的话传-1表明不选座驾
        params.put(type == 0 ? "headwearId" : "carId", String.valueOf(dressId));
        postRequest(type == 0 ? UriProvider.changeHeadWearState() : UriProvider.changeCarState(), params, callBack);
    }

    /**
     * 赠送礼物
     * @param type      礼物类型
     * @param targetUid 被赠送人
     * @param goodsId   礼物id
     * @param callBack  回调
     */
    public void giveGift(int type, String targetUid, String goodsId, OkHttpManager.MyCallBack<ServiceResult> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        param.put("targetUid", targetUid);
        if (type == DressUpMallActivity.DRESS_CAR) {
            param.put("carId", goodsId);
        } else {
            param.put("headwearId", goodsId);
        }
        postRequest(type == DressUpMallActivity.DRESS_CAR ? UriProvider.giftCarGive() : UriProvider.giftHeadWearGive(), param, callBack);
    }
}
