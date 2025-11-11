package com.vslk.lbgx.ui.me.task.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ToastUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.room.bean.TaskInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

public class TaskTimeAdapter extends BaseQuickAdapter<TaskInfo,BaseViewHolder>{
    private int itemWidth;


    public TaskTimeAdapter(int itemWidth) {
        super(R.layout.layout_task_time_item);
        this.itemWidth = itemWidth;
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfo item) {
        LinearLayout llItem = (LinearLayout) helper.getView(R.id.ll_time_task);
        RecyclerView.LayoutParams rvL = (RecyclerView.LayoutParams) llItem.getLayoutParams();
        rvL.width = itemWidth;
        llItem.setLayoutParams(rvL);
        helper.setText(R.id.tv_time_task_name,item.getDutyName());
        TextView tvState = helper.getView(R.id.tv_time_task_state);
        ImageView ivGold = helper.getView(R.id.iv_time_gold);
        if (item.getUdStatus() == 3){//已完成
            tvState.setText("已领取");
            tvState.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            tvState.setBackgroundResource(R.drawable.shape_ffdcdcdc_r_10);
            ivGold.setImageResource(R.drawable.ic_task_gold_select);
        }else if (item.getUdStatus() == 2){//未领取奖励
            tvState.setText("领取");
            tvState.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            tvState.setBackgroundResource(R.drawable.shape_ff4859_r_10);
            ivGold.setImageResource(R.drawable.ic_task_gold);
        }else {//去完成
            tvState.setText("待领取");
            tvState.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            tvState.setBackgroundResource(R.drawable.shape_ff8124_r_10);
            ivGold.setImageResource(R.drawable.ic_task_gold_enable);
        }
        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getUdStatus() == 3){
                    return;
                }else if (item.getUdStatus() == 2){
                    getReward(item.getDutyId(),item.getGoldAmount(),item);
                    return;
                }
            }
        });
    }

    private void getReward(int taskId,int goldNum, TaskInfo item){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",CoreManager.getCore(IAuthCore.class).getCurrentUid()+"");
        params.put("dutyId",taskId+"");
        params.put("ticket",CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.getTaskReward(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {
                if (response != null&&response.num("code") == 200){
                    ToastUtil.getInstance().showTaskMsg(mContext,"已领取+"+goldNum+"开心");
                    item.setUdStatus(3);
                    notifyDataSetChanged();
                }
            }
        });
    }
}
