package com.tongdaxing.xchat_core.home;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;

/**
 * 主页接口逻辑处理
 * Created by zhouxiangfeng on 2017/5/17.
 */
public interface IHomeCore extends IBaseCore {
    //全局数据--------------------------

    List<TabInfo> getMainTabInfos();

    void setMainTabInfos(List<TabInfo> tabInfoList);

    //接口数据------------------------------

    void getHomeData(int page, int pageSize);

    void getLightChatData(int tabType);

    void getHotData(int tabType);

    void getHomePartyData(int tabType);

    void getHomeBanner();

    /** 获取首页排行数据 */
    void getRankingData();

    /** 获取首页tab数据 */
    void getMainTabData();

    /**
     * 获取tab下的数据
     *
     * @param tagId    tab的id
     * @param pageNum  当前页数
     */
    void getMainDataByTab(int tagId, int pageNum);
    void getSortDataByTab(int tagId, int pageNum);
    void getMainDataByMenu();
}
