package com.tongdaxing.xchat_core.initial;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * @author xiaoyu
 * @date 2017/12/29
 */

public interface IInitView extends IMvpBaseView {
    /**
     * -
     *
     * @param data -
     */
    void onInitSuccess(InitInfo data);

}
