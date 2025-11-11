package com.vslk.lbgx.ui.me.task.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.MainActivity;
import com.vslk.lbgx.ui.me.task.activity.MyTaskActivity;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.utils.ToastUtil;
import com.vslk.lbgx.utils.UIHelper;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.bean.TaskInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

public class TaskDayAdapter extends BaseQuickAdapter<TaskInfo,BaseViewHolder>{


    public TaskDayAdapter() {
        super(R.layout.item_rv_my_task);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfo item) {
        helper.setText(R.id.tv_task_list_name,item.getDutyName())
                .setText(R.id.tv_task_list_gold_num,"+" + item.getGoldAmount());
        TextView tvState = helper.getView(R.id.tv_task_list_btn);
        if (item.getUdStatus() == 3){//已完成
            tvState.setText("已完成");
            tvState.setTextColor(ContextCompat.getColor(mContext,R.color.color_d0d0d0));
            tvState.setBackgroundResource(R.drawable.shape_line_d0d0d0_r_16);
        }else if (item.getUdStatus() == 2){//未领取奖励
            tvState.setText("领取");
            tvState.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            tvState.setBackgroundResource(R.drawable.shape_theme_r_16);
        }else {//去完成
            tvState.setText("去完成");
            tvState.setTextColor(ContextCompat.getColor(mContext,R.color.mm_theme));
            tvState.setBackgroundResource(R.drawable.shape_line_theme_r_16);
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
                if (mContext == null)
                    return;
                switch (item.getDutyId()){
                    case 1://修改个性签名
                        UIHelper.showUserInfoModifyAct(mContext, CoreManager.getCore(IAuthCore.class).getCurrentUid());
                        break;
                    case 2://上传图片
                        UserInfoActivity.start(mContext, CoreManager.getCore(IAuthCore.class).getCurrentUid());
                        break;
                    case 5:
                        Intent intent = new Intent(mContext, BinderPhoneActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 6://朋友圈
                        if (mContext != null && mContext instanceof MyTaskActivity)
                            ((MyTaskActivity) mContext).share(ShareSDK.getPlatform(WechatMoments.NAME));
                        break;
                    case 7://qq空间
                        if (mContext != null && mContext instanceof MyTaskActivity)
                                ((MyTaskActivity) mContext).share(ShareSDK.getPlatform(QZone.NAME));
                        break;
                    case 10://充值
                        WalletActivity.start(mContext);
                        break;
                    case 3://关注一个主播
                    case 8://捡海螺
                    case 9://送礼物
                        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                        if (roomInfo != null) {
                            AVRoomActivity.start(mContext, roomInfo.getUid());
                        }else {
                            MainActivity.startPage(mContext,0);
                        }
                        break;
                    case 4://大厅发言
                        MainActivity.startPage(mContext,1);
                        break;

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
