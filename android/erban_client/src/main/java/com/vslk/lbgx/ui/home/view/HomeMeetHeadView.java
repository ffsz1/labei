package com.vslk.lbgx.ui.home.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.home.adpater.MeetYouHeadAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.home.HomeRoom;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/25
 * 描述        遇见头部
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class HomeMeetHeadView extends LinearLayout implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView recyclerView;

    private MeetYouHeadAdapter mAdapter;

    public HomeMeetHeadView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 初始化View
     */
    private void initView(Context context) {
        inflate(context, R.layout.ly_home_meet_head, this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        //初始化RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new MeetYouHeadAdapter();
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    //设置数据
    public void setNewData(List<HomeRoom> roomList) {
        if (!ListUtils.isListEmpty(roomList)) {
            recyclerView.setVisibility(VISIBLE);
            mAdapter.setNewData(roomList);
        } else {
            recyclerView.setVisibility(GONE);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            HomeRoom homeRoom = mAdapter.getData().get(position);
            if (homeRoom == null) {
                return;
            }
            AVRoomActivity.start(getContext(), homeRoom.getUid());
        }
    }
}
