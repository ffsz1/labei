package com.vslk.lbgx.room.avroom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vslk.lbgx.room.avroom.other.BottomViewListenerWrapper;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtil;

/**
 * @author chenran
 * @date 2017/7/26
 */

public class BottomView extends RelativeLayout implements View.OnClickListener {
    private BottomViewListenerWrapper wrapper;
    private ImageView openMic;
    //private LinearLayout sendMsg;
    private ImageView ivSendMsg;
    private ImageView sendGift;
    private ImageView remoteMute;
    private ImageView faceLayout;
    private FrameLayout rlMsg;
    private ImageView ivMsgMark;
    private boolean micInListOption;
    private ImageView iconRoomMicInList;

    public BottomView(Context context) {
        super(context);
        init();
    }

    public BottomView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public BottomView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_bottom_view, this);
        Json configData = CoreManager.getCore(VersionsCore.class).getConfigData();
        int micInListOption = configData.num("micInListOption");
        this.micInListOption = micInListOption == 1;
        openMic = findViewById(R.id.icon_room_open_mic);
        ivSendMsg = findViewById(R.id.iv_room_send_msg);
        sendGift = findViewById(R.id.icon_room_send_gift);
        remoteMute = findViewById(R.id.icon_room_open_remote_mic);
        faceLayout = findViewById(R.id.icon_room_face);
        rlMsg = findViewById(R.id.rl_room_msg);
        iconRoomMicInList = findViewById(R.id.icon_room_mic_in_list);
        ivMsgMark = findViewById(R.id.iv_room_msg_mark);

        iconRoomMicInList.setOnClickListener(this);
        openMic.setOnClickListener(this);
        ivSendMsg.setOnClickListener(this);
        faceLayout.setOnClickListener(this);
        sendGift.setOnClickListener(this);
        remoteMute.setOnClickListener(this);
        rlMsg.setOnClickListener(this);

        setMicBtnEnable(false);
        setMicBtnOpen(false);
        RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (mCurrentRoomInfo != null) {
            setInputMsgBtnEnable(mCurrentRoomInfo.getPublicChatSwitch() == 0);
            //if (mCurrentRoomInfo.getPublicChatSwitch() == 0) {
            //    setInputMsgBtnEnable(true);
            //} else {
            //    setInputMsgBtnEnable(false);
            //}
        }

    }

    public void setBottomViewListener(BottomViewListenerWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void setMicBtnEnable(boolean enable) {
        if (enable) {
            openMic.setClickable(true);
            openMic.setOnClickListener(this);
        } else {
            openMic.setClickable(false);
            openMic.setOnClickListener(null);
        }
    }

    public void setMicBtnOpen(boolean isOpen) {
        LogUtil.d("mic_btn", "isOpen = " + isOpen);
        if (isOpen) {
            openMic.setImageResource(R.mipmap.icon_room_open_mic);
        } else {
            openMic.setImageResource(R.mipmap.icon_room_close_mic);
        }
    }

    public void setRemoteMuteOpen(boolean isOpen) {
        LogUtil.d("remote_mic_btn", "isOpen = " + isOpen);
        if (isOpen) {
            remoteMute.setImageResource(R.mipmap.icon_remote_mute_open);
        } else {
            remoteMute.setImageResource(R.mipmap.icon_remote_mute_close);
        }
    }

    public void showHomePartyUpMicBottom() {
        faceLayout.setVisibility(VISIBLE);
        openMic.setVisibility(VISIBLE);
    }

    public void showHomePartyDownMicBottom() {
        faceLayout.setVisibility(GONE);
        openMic.setVisibility(GONE);
    }

    public void showLightChatUpMicBottom() {
        faceLayout.setVisibility(GONE);
        openMic.setVisibility(VISIBLE);
    }

    public void showLightChatDownMicBottom() {
        faceLayout.setVisibility(GONE);
        openMic.setVisibility(GONE);
    }

    /**
     * 新私聊消息提示
     *
     * @param isShow true 显示 false不显示
     */
    public void showMsgMark(boolean isShow) {
        if (ivMsgMark != null) {
            ivMsgMark.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 公屏发言按钮状态
     */
    public void setInputMsgBtnEnable(boolean enable) {
        ivSendMsg.setEnabled(enable);
        //关闭状态需要修改ui
        ivSendMsg.setImageResource(enable ? R.mipmap.ic_room_send_msg : R.mipmap.ic_room_send_msg_ban);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_room_open_mic:
                if (wrapper != null) {
                    wrapper.onOpenMicBtnClick();
                }
                break;
            case R.id.iv_room_send_msg:
                if (wrapper != null) {
                    wrapper.onSendMsgBtnClick();
                }
                break;
            case R.id.icon_room_send_gift:
                if (wrapper != null) {
                    wrapper.onSendGiftBtnClick();
                }
                break;
            case R.id.icon_room_face:
                if (wrapper != null) {
                    wrapper.onSendFaceBtnClick();
                }
                break;
            case R.id.icon_room_open_remote_mic:
                if (wrapper != null) {
                    wrapper.onRemoteMuteBtnClick();
                }
                break;
            case R.id.icon_room_lottery_box:
                if (wrapper != null) {
                    wrapper.onLotteryBoxeBtnClick();
                }
                break;

            case R.id.icon_room_mic_in_list:
                if (wrapper != null) {
                    wrapper.onBuShowMicInList();
                }
                break;
            case R.id.rl_room_msg:
                if (wrapper != null) {
                    wrapper.onMsgBtnClick();
                }
                break;
            default:
        }
    }
}
