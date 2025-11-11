package com.vslk.lbgx.ui.me.shopping.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

/**
 * 装扮商城View 接口
 *
 * @author zwk 2018/10/16
 */
public interface IDressUpFragmentView extends IMvpBaseView {

    //获取装扮列表数据成功

    default void getHeadWearListSuccessFail(String msg) {

    }

    //获取装扮列表数据成功
    default void getHeadWearListSuccess(List<DressUpBean> result) {

    }

    //获取装扮列表数据成功

    default void getCarListSuccessFail(String msg) {

    }

    //获取装扮列表数据成功
    default void getCarListSuccess(List<DressUpBean> result) {

    }

    default void getDressUpListSuccess(ServiceResult<List<DressUpBean>> result) {

    }

    //获取装扮列表数据失败
    default void getDressUpListFail(Exception e) {

    }

    default void getDressUpList(List<DressUpBean> result) {

    }

    default void getDressUpListFail(String msg) {

    }

    //购买装扮/续费装扮成功
    default void onPurseDressUpSuccess(int purseType) {

    }

    //购买装扮/续费装扮失败
    default void onPurseDressUpFail(String error) {

    }

    //修改装扮的使用/取消使用 状态成功
    default void onChangeDressUpStateSuccess(int dressUpId) {

    }

    //修改装扮的使用/取消使用 失败
    default void onChangeDressUpStateFail(String error) {

    }

    /**
     * 赠送礼物回调
     */
    default void giftGiveSuccess() {

    }

    default void giftGiveFail(String msg) {

    }

}
