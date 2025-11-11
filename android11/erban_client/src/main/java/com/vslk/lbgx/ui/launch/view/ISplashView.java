package com.vslk.lbgx.ui.launch.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public interface ISplashView extends IMvpBaseView {
    void resultHomeTabsSuccess(List<TabInfo> tabInfoList);

    void resultHomeTabsFail(String error);
}
