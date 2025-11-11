package com.vslk.lbgx.ui.me.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.find.FindSquarePresenter;
import com.vslk.lbgx.presenter.find.IFindSquareView;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.find.adapter.FindActivityAdapter;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityShowActivitiesBinding;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.find.AlertInfo;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/21
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(FindSquarePresenter.class)
public class ShowActivitiesActivity extends BaseMvpActivity<IFindSquareView, FindSquarePresenter> implements
        IFindSquareView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    private FindActivityAdapter mAdapter;
    private ActivityShowActivitiesBinding mBinding;

    public static void start(Context context) {
        Intent intent = new Intent(context, ShowActivitiesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_activities);
        onSetListener();
        onLoadData();
    }

    private void onSetListener() {
        back(mBinding.toolbar);
        mBinding.swipeRefresh.setOnRefreshListener(this);

        mAdapter = new FindActivityAdapter();
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void onLoadData() {
        getMvpPresenter().showActivities();
    }

    @Override
    public void getFindActivity(List<AlertInfo> findInfos) {
        hideStatus();
        mBinding.swipeRefresh.setRefreshing(false);
        if (ListUtils.isListEmpty(findInfos)) {
            showNoData("暂无活动哦");
        } else {
            findInfos.get(0).setItemType(1);
            if (findInfos.size() > 1) {
                AlertInfo alertInfo = new AlertInfo();
                alertInfo.setItemType(2);
                findInfos.add(1, alertInfo);
            }
            mAdapter.setNewData(findInfos);
        }
    }

    @Override
    public void getFindActivityFail(String msg) {
        hideStatus();
        mBinding.swipeRefresh.setRefreshing(false);
        showNoData("暂无活动哦");
    }

    @Override
    public void onRefresh() {
        showLoading();
        getMvpPresenter().showActivities();
    }

    @Override
    public void onReloadDate() {
        onRefresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            AlertInfo alertInfo = mAdapter.getData().get(position);
            if (alertInfo.getSkipType() == 3||alertInfo.getSkipType() ==1) {
                CommonWebViewActivity.start(this, alertInfo.getSkipUrl());
            } else if (alertInfo.getSkipType() == 2) {
                AVRoomActivity.start(this, JavaUtil.str2long(alertInfo.getSkipUrl()));
            }
        }
    }
}
