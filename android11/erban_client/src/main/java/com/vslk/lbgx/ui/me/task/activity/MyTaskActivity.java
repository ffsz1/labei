package com.vslk.lbgx.ui.me.task.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.ui.me.task.adapter.TaskDayAdapter;
import com.vslk.lbgx.ui.me.task.view.MyTaskHead;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.WebViewInfo;
import com.tongdaxing.xchat_core.room.bean.TaskBean;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;

public class MyTaskActivity extends BaseActivity implements OnRefreshListener, ShareDialog.OnShareDialogItemClick {
    @BindView(R.id.srl_task_refresh)
    public SmartRefreshLayout srlRefresh;
    //列表头 - 新手任务
    private MyTaskHead head;

    //每日任务列表
    @BindView(R.id.rv_task_list)
    public RecyclerView rvDayTask;
    private TaskDayAdapter dayAdapter;
    private boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        ButterKnife.bind(this);
        initTitleBar("我的任务");
        if (mTitleBar != null)
            mTitleBar.setDividerColor(R.color.line_color);
        rvDayTask.setLayoutManager(new LinearLayoutManager(this));
        dayAdapter = new TaskDayAdapter();
        srlRefresh.setEnableLoadmore(false);
        srlRefresh.setOnRefreshListener(this);
        dayAdapter.setEnableLoadMore(false);
        head = new MyTaskHead(this);
        dayAdapter.addHeaderView(head);
        rvDayTask.setAdapter(dayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (isFirst) {
            isFirst = false;
            showLoading();
        }
        CoreManager.getCore(IUserCore.class).getTaskList();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onTaskList(TaskBean task) {
        srlRefresh.finishRefresh();
        hideStatus();
        if (head != null) {
            head.setTime(task.getRoomTime());
            head.setTimeTask(task.getDailyTime());
            head.setNewTask(task.getFresh());
        }
        if (dayAdapter != null)
            dayAdapter.setNewData(task.getDaily());
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onTaskListFAIL(String error) {
        srlRefresh.finishRefresh();
        showNetworkErr();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        initData();
    }

    @Override
    public void onReloadDate() {
        super.onReloadDate();
        showLoading();
        initData();
    }

    public void share(Platform platform) {
        WebViewInfo webViewInfo = new WebViewInfo();
        webViewInfo.setTitle(getString(R.string.share_h5_title));
        webViewInfo.setImgUrl(WebUrl.SHARE_DEFAULT_LOGO);
        webViewInfo.setDesc(getString(R.string.share_h5_desc));
        webViewInfo.setShowUrl(WebUrl.SHARE_DOWNLOAD);
        CoreManager.getCore(IShareCore.class).sharePage(webViewInfo, platform);
    }

    @Override
    public void onSharePlatformClick(Platform platform) {

    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareWebViewError() {
        toast("分享失败，请重试");
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareWebViewCanle() {
        toast("取消分享");
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onShareReport() {
        toast("分享成功");
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
