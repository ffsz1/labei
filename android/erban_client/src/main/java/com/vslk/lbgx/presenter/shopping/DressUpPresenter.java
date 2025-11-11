package com.vslk.lbgx.presenter.shopping;

import com.vslk.lbgx.model.shopping.DressUpModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

/**
 * 装扮商城主界面Presenter
 */
public class DressUpPresenter extends AbstractMvpPresenter<IMvpBaseView> {
    private DressUpModel dressUpModel;


    public DressUpPresenter() {
        if (this.dressUpModel == null)
            this.dressUpModel = new DressUpModel();
    }

    /**
     * 获取标签数据
     * @return List<TabInfo>
     */
    public List<TabInfo> getTabInfos() {
        if (dressUpModel != null)
            return dressUpModel.getTabInfos();
        return null;
    }

}
