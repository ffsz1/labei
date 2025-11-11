package com.vslk.lbgx.ui.sign.adapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.MainActivity;
import com.vslk.lbgx.ui.find.activity.SquareActivity;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.sign.TaskCenterActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.UIHelper;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.mengcoin.MengCoinBean;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

public class MengCoinAdapter extends BaseMultiItemQuickAdapter<MengCoinBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MengCoinAdapter(List<MengCoinBean> data) {
        super(data);
        addItemType(MengCoinBean.ITEM_DAILY, R.layout.item_rv_my_task);
        addItemType(MengCoinBean.ITEM_TITLE, R.layout.item_sign_in_task_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, MengCoinBean item) {
        if (item == null) {
            return;
        }
        switch (item.getItemType()) {
            case MengCoinBean.ITEM_DAILY:
                fun(helper, item);
                break;
            case MengCoinBean.ITEM_TITLE:
                setTaskTitle(helper, item);
                break;
            default:
        }
    }

    private void setTaskTitle(BaseViewHolder helper, MengCoinBean item) {
        helper.setText(R.id.tv_task_title, item.getMissionName());
    }

    private void fun(BaseViewHolder helper, MengCoinBean item) {
        ImageLoadUtils.loadImage(mContext, item.getPicUrl(), helper.getView(R.id.iv_task_list_gold));
        helper.setText(R.id.tv_task_list_name, item.getMissionName())
                .setText(R.id.tv_task_list_gold_num, mContext.getResources().getString(R.string.dian_dian_coin) + " +" + (int) item.getMcoinAmount());
        TextView tvState = helper.getView(R.id.tv_task_list_btn);
        if (item.getMissionStatus() == 3) {//已完成
            tvState.setBackgroundResource(R.drawable.ic_already_get_task);
        } else if (item.getMissionStatus() == 2) {//未领取奖励
            tvState.setBackgroundResource(R.drawable.ic_get_task);
        } else {//去完成
            tvState.setBackgroundResource(R.drawable.ic_get_task_finish);
        }
        tvState.setOnClickListener(v -> {
            if (item.getMissionStatus() == 3) {
                return;
            } else if (item.getMissionStatus() == 2) {
                if (mContext instanceof TaskCenterActivity) {
                    ((TaskCenterActivity) mContext).getMvpPresenter().receiveMengCoinByMissionId(item.getMissionId());
                }
                return;
            }
            if (mContext == null)
                return;
            switch (item.getMissionId()) {
                case 1://上传图片
                    UIHelper.showModifyPhotosAct((Activity) mContext, CoreManager.getCore(IAuthCore.class).getCurrentUid());
                    break;
                case 3://大厅发言
                    SquareActivity.start(mContext);
                    break;
                case 4://绑定手机号
                    Intent intent = new Intent(mContext, BinderPhoneActivity.class);
                    mContext.startActivity(intent);
                    break;
                case 5://设置个性签名
                    UIHelper.showUserInfoModifyAct(mContext, CoreManager.getCore(IAuthCore.class).getCurrentUid());
                    break;
                case 6://朋友圈
                    if (mContext instanceof TaskCenterActivity)
                        ((TaskCenterActivity) mContext).share(ShareSDK.getPlatform(WechatMoments.NAME));
                    break;
                case 7://qq空间
                    if (mContext instanceof TaskCenterActivity)
                        ((TaskCenterActivity) mContext).share(ShareSDK.getPlatform(QZone.NAME));
                    break;
                case 8://充值
                    WalletActivity.start(mContext);
                    break;
                case 2://关注一个主播
                case 9://送礼物
                case 10://捡海螺
                case 11://房间停留30分钟
                    RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                    if (roomInfo != null) {
                        AVRoomActivity.start(mContext, roomInfo.getUid());
                    } else {
                        MainActivity.startPage(mContext, 0);
                    }
                    break;
                case 24://实名认证
                    CommonWebViewActivity.start(mContext, WebUrl.VERIFIED_REAL_NAME);
                    break;
            }
        });
    }

}
