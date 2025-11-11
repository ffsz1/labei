package com.vslk.lbgx.ui.find.fragment;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.find.FindSquarePresenter;
import com.vslk.lbgx.presenter.find.IFindSquareView;
import com.vslk.lbgx.ui.find.activity.InviteAwardActivity;
import com.vslk.lbgx.ui.find.view.PublicMessageView;
import com.vslk.lbgx.ui.verified.VerifiedDialog;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachParser;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.ChatUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.List;

import butterknife.BindView;

/**
 * 新版的公聊大厅 -- 广场
 *
 * @zwk 2018/11/23
 */
@CreatePresenter(FindSquarePresenter.class)
public class SquareFragment extends BaseMvpFragment<IFindSquareView, FindSquarePresenter> implements IFindSquareView, View.OnClickListener {

    @BindView(R.id.message_view)
    PublicMessageView mMessageView;
    @BindView(R.id.input_edit)
    EditText mInputEdit;
    @BindView(R.id.input_send)
    TextView mInputSend;
    @BindView(R.id.tv_count_down)
    TextView mTvCountDown;
    @BindView(R.id.iv_invite_friends)
    ImageView ivInvite;
    public static long devRoomId = 2;//测试公聊
    public static long formalRoomId = 4;//线上
    public static long checkDevRoomId = 1;//线下审核
    public static long checkRoomId = 3;//线上审核
    private long roomId = formalRoomId;
    public long cacheTime = 0;
    private String cacheNameKey = "cacheNameKey";
    private CountDownTimer mCountDownTimer;

    //避免重复提交
    private boolean isReport = true;

    @Override
    public void showVerifiedDialog(int errorno, String error) {
        VerifiedDialog verifiedDialog = VerifiedDialog.newInstance(error, errorno);
        verifiedDialog.show(getChildFragmentManager(), "verifiedDialog");
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_square;
    }

    @Override
    public void onFindViews() {
    }

    @Override
    public void onSetListener() {
        ivInvite.setOnClickListener(this);
        mInputSend.setOnClickListener(this);
        mTvCountDown.setOnClickListener(this);
    }

    @Override
    public void initiate() {
        cacheTime = (long) SpUtils.get(getContext(), cacheNameKey, 0L);
        IMNetEaseManager.get().subscribeChatRoomEventObservable(roomEvent -> {
            if (roomEvent == null) {
                return;
            }
            int event = roomEvent.getEvent();
            switch (event) {
                //IM重连 -- 重新进聊天室
                case RoomEvent.SOCKET_IM_RECONNECT_LOGIN_SUCCESS:
                    LogUtil.e(AvRoomDataManager.TAG, "大厅重连");
                    getMvpPresenter().enterRoom(roomId + "");
                    break;
                default:
            }
        }, this);
    }

    @Override
    protected void onLazyLoadData() {
        super.onLazyLoadData();
        showLoading();
        getMvpPresenter().getSquareRoomId();
    }


    @Override
    public void getSquareRoomIdSuccess(boolean audit) {
        mMessageView.clear();
        if (!audit) {
            if (BasicConfig.INSTANCE.isDebuggable()) {
                roomId = devRoomId;
            } else {
                roomId = formalRoomId;
            }
        } else {
            if (BasicConfig.INSTANCE.isDebuggable()) {
                roomId = checkDevRoomId;
            } else {
                roomId = checkRoomId;
            }
        }
        checkCountDown();
        getMvpPresenter().enterRoom(roomId + "");
    }

    @Override
    public void resetSquareLayout() {
        hideStatus();
    }

    @Override
    public void enterPublicRoomSuccess(IMReportBean imReportBean) {
        //历史记录的重进房间后补发消息id的标记
        int msgId = -1;
        if (mMessageView != null && !ListUtils.isListEmpty(mMessageView.getChatRoomMessages())) {
            ChatRoomMessage msg = mMessageView.getChatRoomMessages().get(mMessageView.getChatRoomMessages().size() - 1);
            if (msg.getAttachment() instanceof PublicChatRoomAttachment) {
                msgId = ((PublicChatRoomAttachment) msg.getAttachment()).getServer_msg_id();
            }
        }
        Json str = imReportBean.getReportData().data;
        List<Json> his_list = str.jlist("his_list");
        for (int i = 0; i < his_list.size(); i++) {
            ChatRoomMessage message = new ChatRoomMessage();
            message.setRoute(IMReportRoute.sendPublicMsgNotice);
            String custom = his_list.get(i).toString();
            if (StringUtils.isEmpty(custom))
                continue;
            IMCustomAttachment IMCustomAttachment = IMCustomAttachParser.parse(custom);
            if (IMCustomAttachment == null) continue;
            message.setAttachment(IMCustomAttachment);
            if (IMCustomAttachment instanceof PublicChatRoomAttachment) {//如果是历史记录补发
                if (((PublicChatRoomAttachment) IMCustomAttachment).getServer_msg_id() > msgId) {
                    IMNetEaseManager.get().addMessagesImmediately(message);
                }
            }
        }
    }

    @Override
    public void enterPublicRoomFail(String error) {
        showNoData(error);
    }

    @Override
    public void onReloadData() {
        super.onReloadData();
        cacheTime = System.currentTimeMillis();
        SpUtils.put(getContext(), cacheNameKey, cacheTime);
        onLazyLoadData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_send:
                sendMsg();
                break;
            case R.id.tv_count_down:
                toast("请稍等");
                break;
            case R.id.iv_invite_friends:
                InviteAwardActivity.start(getActivity());
                break;
            default:

        }
    }


    @Override
    public void reportSuccess() {
        isReport = false;
    }

    private long getPublicChatHallTime() {
        Json json = CoreManager.getCore(VersionsCore.class).getConfigData();
        long publicChatHallTime = json.num("publicChatHallTime");
        if (publicChatHallTime <= 0) {//默认为30秒
            publicChatHallTime = 10;
        }
        return publicChatHallTime * 1000;
    }

    public void sendMsg() {
        if (System.currentTimeMillis() - cacheTime < getPublicChatHallTime()) {
            toast("广播发送间隔为30秒，请稍等");
            return;
        }
        if (ChatUtil.checkBanned())
            return;
        String trim = mInputEdit.getText().toString().trim();
        if (StringUtils.isEmpty(trim))
            return;
        String sensitiveWordData = CoreManager.getCore(VersionsCore.class).getSensitiveWordData();
        if (!TextUtils.isEmpty(sensitiveWordData) && !TextUtils.isEmpty(trim)) {
            if (trim.matches(sensitiveWordData)) {
                SingleToastUtil.showToast(this.getString(R.string.sensitive_word_data));
                return;
            }
        }
        if (trim.length() > 40) {
            trim = trim.substring(0, 40);
        }
        getMvpPresenter().sendMessage(roomId + "", trim);
        mInputEdit.setText("");
        cacheTime = System.currentTimeMillis();
        SpUtils.put(getContext(), cacheNameKey, cacheTime);
        checkCountDown();
        if (isReport) {
            getMvpPresenter().getReportState();
        }
    }


    @Override
    public void sendMessageSuccess() {
    }

    @Override
    public void sendMessageFail(String error) {
        toast(error);
    }


    public void checkCountDown() {
        long time = System.currentTimeMillis();
        long l = time - cacheTime;
        l = getPublicChatHallTime() - l;
        if (l <= getPublicChatHallTime() && l > 0) {
            mInputSend.setVisibility(View.GONE);
            mTvCountDown.setVisibility(View.VISIBLE);
            mCountDownTimer = new CountDownTimer(l, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int countDownTime = (int) (millisUntilFinished / 1000);
                    mTvCountDown.setText(countDownTime + "S");
                }

                @Override
                public void onFinish() {
                    mInputSend.setVisibility(View.VISIBLE);
                    mTvCountDown.setVisibility(View.GONE);
                }
            };
            mCountDownTimer.start();
        } else {
            mInputSend.setVisibility(View.VISIBLE);
            mTvCountDown.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
//        if (mPublicChatRoomController != null)
//            mPublicChatRoomController.leaveRoom();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogout() {//用户进入该页面才会有回调
        //退出后离开公聊
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
//        if (mPublicChatRoomController != null) {
//            mPublicChatRoomController.leaveRoom();
//            Constants.isNeedJoin = true;
//            isReport = true;
//        }
    }


//
//    @CoreEvent(coreClientClass = IRoomCoreClient.class)
//    public void onGetUserRoom(RoomInfo roomInfo) {
//        long formalRoomId = BasicConfig.isDebug ? PublicChatRoomController.devRoomId : PublicChatRoomController.formalRoomId;
//        if (roomInfo.getRoomId() == formalRoomId) {
//            toast("对方不在房间内");
//            return;
//        }
//        getDialogManager().dismissDialog();
//        RoomInfo current = AvRoomDataManager.get().mCurrentRoomInfo;
//        if (roomInfo != null && roomInfo.getUid() > 0) {
//            if (current != null) {
//                if (current.getUid() == roomInfo.getUid()) {
//                    toast("已经和对方在同一个房间");
//                    return;
//                }
//            }
//            AVRoomActivity.start(mContext, roomInfo.getUid());
//        } else {
//            toast("对方不在房间内");
//        }
//    }

    public static Fragment newInstance() {
        SquareFragment fragment = new SquareFragment();
        return fragment;
    }


}
