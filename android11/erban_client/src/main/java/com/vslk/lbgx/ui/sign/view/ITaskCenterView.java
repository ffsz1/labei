package com.vslk.lbgx.ui.sign.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.mengcoin.MengCoinTaskBean;

/**
 * Function:
 * Author: Edward on 2019/5/22
 */
public interface ITaskCenterView extends IMvpBaseView {
    void showMengCoinTaskListView(MengCoinTaskBean mengCoinTaskBean);

    void showMengCoinErrorView(String error);

    void receiveMengCoinSucToast(int missionId);

    void receiveMengCoinFailToast(String error);
}
