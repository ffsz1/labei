package com.vslk.lbgx.ui.me.task.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.vslk.lbgx.ui.me.task.adapter.TaskDayAdapter;
import com.vslk.lbgx.ui.me.task.adapter.TaskTimeAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.room.bean.TaskInfo;

import java.util.List;

public class MyTaskHead extends android.widget.LinearLayout {
    private RecyclerView rvNewTask;
    private TaskDayAdapter mNewAdapter;
    private RecyclerView rvTimeTask;
    private TaskTimeAdapter mTimeAdatper;
    private LinearLayout llNewTask;
    private TextView tvTime;

    public MyTaskHead(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_task_head, this);
        tvTime = view.findViewById(R.id.tv_task_room_time);
        rvTimeTask = view.findViewById(R.id.rv_time_task);
        mTimeAdatper = new TaskTimeAdapter((ScreenUtil.getScreenWidth(context) - ConvertUtils.dp2px(60)) / 4);
        LinearLayoutManager ll = new LinearLayoutManager(context);
        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTimeTask.setLayoutManager(ll);
        int itemRight = ConvertUtils.dp2px(10);
        rvTimeTask.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = itemRight;
            }
        });
        rvTimeTask.setAdapter(mTimeAdatper);
        llNewTask = view.findViewById(R.id.ll_new_task);
        rvNewTask = view.findViewById(R.id.rv_task_new);
        rvNewTask.setLayoutManager(new LinearLayoutManager(context));
        mNewAdapter = new TaskDayAdapter();
        rvNewTask.setAdapter(mNewAdapter);
        rvTimeTask.setFocusableInTouchMode(false);
        rvTimeTask.requestFocus();
        rvNewTask.setFocusableInTouchMode(false);
        rvNewTask.requestFocus();
    }

    /**
     * 设置头部当前在房间的时间
     * @param time
     */
    public void setTime(int time) {
        if (tvTime != null)
            tvTime.setText("" + time);
    }

    /**
     * 设置头部时间任务列表的数据
     *
     * @param data
     */
    public void setTimeTask(List<TaskInfo> data) {
        if (ListUtils.isListEmpty(data))
            return;
        if (mTimeAdatper != null)
            mTimeAdatper.setNewData(data);
    }

    /**
     * 设置头部的新手任务的列表数据
     *
     * @param data
     */
    public void setNewTask(List<TaskInfo> data) {
        if (ListUtils.isListEmpty(data)) {
            if (llNewTask != null && llNewTask.getVisibility() == View.VISIBLE)
                llNewTask.setVisibility(View.GONE);
            return;
        }
        if (llNewTask != null && llNewTask.getVisibility() == View.GONE)
            llNewTask.setVisibility(View.VISIBLE);
        if (mNewAdapter != null) {
            mNewAdapter.setNewData(data);
        }
    }

}
