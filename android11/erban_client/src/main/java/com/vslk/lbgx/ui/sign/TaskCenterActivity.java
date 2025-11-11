package com.vslk.lbgx.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.sign.TaskCenterPresenter;
import com.vslk.lbgx.ui.sign.adapter.MengCoinAdapter;
import com.vslk.lbgx.ui.sign.view.ITaskCenterView;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.mengcoin.MengCoinBean;
import com.tongdaxing.xchat_core.mengcoin.MengCoinTaskBean;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.WebViewInfo;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;

/**
 * Function:任务中心
 * Author: Edward on 2019/5/22
 */
@CreatePresenter(TaskCenterPresenter.class)
public class TaskCenterActivity extends BaseMvpActivity<ITaskCenterView, TaskCenterPresenter> implements ITaskCenterView, View.OnClickListener, ShareDialog.OnShareDialogItemClick {
    //头部布局：签到任务和新手任务以及萌币数量
    private MengCoinHeadView headView;
    //底部
    private RecyclerView recyclerView;
    private MengCoinAdapter dailyAdapter;
    private AppToolBar mTitleBar;

    public static void start(Context activity) {
        Intent intent = new Intent(activity, TaskCenterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_center);

        mTitleBar = (AppToolBar) findViewById(R.id.toolbar);
        mTitleBar.setOnBackBtnListener(v -> finish());
        mTitleBar.setOnRightBtnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_mb_daily_task_list);
        headView = new MengCoinHeadView(this);
        dailyAdapter = new MengCoinAdapter(null);
        dailyAdapter.addHeaderView(headView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(dailyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMvpPresenter().getCurrentUserMengCoinTaskList();
    }

    @Override
    public void showMengCoinTaskListView(MengCoinTaskBean mengCoinTaskBean) {
        hideStatus();
        headView.setData(mengCoinTaskBean);
        if (mengCoinTaskBean == null) {
            //空布局 没有规定的空布局样式
            showNoData("当前还没有新的任务哦");
        } else {
            if (!ListUtils.isListEmpty(mengCoinTaskBean.getDailyMissions())) {//日常任务
                dailyAdapter.setNewData(convertBean(mengCoinTaskBean));
            }
        }
    }

    private MengCoinBean getTitleInfo(String name) {
        MengCoinBean mengCoinBean = new MengCoinBean();
        mengCoinBean.setItemType(MengCoinBean.ITEM_TITLE);
        mengCoinBean.setMissionName(name);
        return mengCoinBean;
    }

    private List<MengCoinBean> convertBean(MengCoinTaskBean mengCoinTaskBean) {
        List<MengCoinBean> temp = new ArrayList<>();
        if (!ListUtils.isListEmpty(mengCoinTaskBean.getBeginnerMissions())) {
            MengCoinBean mengCoinBean = getTitleInfo("新手任务");
            temp.add(mengCoinBean);
            for (int i = 0; i < mengCoinTaskBean.getBeginnerMissions().size(); i++) {
                mengCoinBean = mengCoinTaskBean.getBeginnerMissions().get(i);
                mengCoinBean.setItemType(MengCoinBean.ITEM_DAILY);
                temp.add(mengCoinBean);
            }
        }

        if (!ListUtils.isListEmpty(mengCoinTaskBean.getDailyMissions())) {
            MengCoinBean mengCoinBean = getTitleInfo("日常任务");
            temp.add(mengCoinBean);
            for (int i = 0; i < mengCoinTaskBean.getDailyMissions().size(); i++) {
                mengCoinBean = mengCoinTaskBean.getDailyMissions().get(i);
                mengCoinBean.setItemType(MengCoinBean.ITEM_DAILY);
                temp.add(mengCoinBean);
            }
        }
        return temp;
    }

    @Override
    public void showMengCoinErrorView(String error) {
        hideStatus();
        showNoData(error);
        //错误布局
//        SingleToastUtil.showToast(error);
    }

    @Override
    public void onReloadDate() {
        super.onReloadDate();
        showLoading();
        getMvpPresenter().getCurrentUserMengCoinTaskList();
    }

    @Override
    public void receiveMengCoinSucToast(int missionId) {
        SingleToastUtil.showToast("任务领取成功");
        getMvpPresenter().getCurrentUserMengCoinTaskList();
    }

    @Override
    public void receiveMengCoinFailToast(String error) {
        SingleToastUtil.showToast(error);
    }

    public void share(Platform platform) {
        WebViewInfo webViewInfo = new WebViewInfo();
        webViewInfo.setTitle(getString(R.string.share_h5_title));
        webViewInfo.setImgUrl(WebUrl.SHARE_DEFAULT_LOGO);
        webViewInfo.setDesc(getString(R.string.share_h5_desc));
        webViewInfo.setShowUrl(WebUrl.SHARE_DOWNLOAD);
        CoreManager.getCore(IShareCore.class).sharePage(webViewInfo, platform);
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareWebViewError() {
        SingleToastUtil.showToast("分享失败，请重试");
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareWebViewCanle() {
        SingleToastUtil.showToast("取消分享");
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareReport() {
        SingleToastUtil.showToast("分享成功");
        getMvpPresenter().getCurrentUserMengCoinTaskList();
    }

    @Override
    public void onClick(View v) {
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.setOnShareDialogItemClick(this);
        shareDialog.show();
    }

    @Override
    public void onSharePlatformClick(Platform platform) {
        share(platform);
    }
}
