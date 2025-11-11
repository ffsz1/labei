package com.vslk.lbgx.room.avroom.fragment.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomMemberComeInfo;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.im.message.IIMMessageCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.bean.RoomFunctionBean;
import com.tongdaxing.xchat_core.room.bean.RoomFunctionEnum;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.face.FaceInfo;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_core.room.model.RoomSettingModel;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.ChatUtil;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.room.audio.widget.MusicPlayerView;
import com.vslk.lbgx.room.avroom.activity.RoomManagerListActivity;
import com.vslk.lbgx.room.avroom.activity.RoomPlayTipActivity;
import com.vslk.lbgx.room.avroom.activity.RoomSettingActivity;
import com.vslk.lbgx.room.avroom.activity.RoomTopicActivity;
import com.vslk.lbgx.room.avroom.adapter.MicroViewAdapter;
import com.vslk.lbgx.room.avroom.adapter.RoomBannerAdapter;
import com.vslk.lbgx.room.avroom.other.BottomViewListenerWrapper;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.avroom.widget.BottomView;
import com.vslk.lbgx.room.avroom.widget.MessageView;
import com.vslk.lbgx.room.avroom.widget.MicroView;
import com.vslk.lbgx.room.avroom.widget.dialog.RoomFunctionDialog;
import com.vslk.lbgx.room.avroom.widget.dialog.RoomTopicDIalog;
import com.vslk.lbgx.room.avroom.widget.dialog.StartFingerGuessingGameDialog;
import com.vslk.lbgx.room.chat.RoomPrivateMsgDialog;
import com.vslk.lbgx.room.egg.PoundEggDialog;
import com.vslk.lbgx.room.face.DynamicFaceDialog;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.room.widget.dialog.BigListDataDialog;
import com.vslk.lbgx.room.widget.dialog.GiftRecordDialog;
import com.vslk.lbgx.room.widget.dialog.ListDataDialog;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.ui.web.TransWebViewActivity;
import com.vslk.lbgx.ui.widget.Banner;
import com.vslk.lbgx.ui.widget.LevelView;
import com.vslk.lbgx.ui.widget.MarqueeTextView;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.NumberFormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN;

public class AudioFragment extends BaseAudioFragment {
    @BindView(R.id.micro_view)
    MicroView microView;
    @BindView(R.id.tv_room_desc)
    ImageView tvRoomDesc;
    @BindView(R.id.fl_no1_money)
    ImageView flNo1Money;
    @BindView(R.id.iv_room_rank)
    ImageView ivRoomRank;
    @BindView(R.id.fl_gift_record)
    FrameLayout flGiftRecord;
    @BindView(R.id.fl_sea_record)
    FrameLayout flSeaRecord;
    @BindView(R.id.message_view)
    MessageView messageView;
    @BindView(R.id.bottom_view)
    BottomView bottomView;
    @BindView(R.id.play_together)
    ImageView playTogether;
    @BindView(R.id.input_edit)
    EditText inputEdit;
    @BindView(R.id.input_send)
    ImageView inputSend;
    @BindView(R.id.input_layout)
    LinearLayout inputLayout;
    @BindView(R.id.bu_mic_in_list_count)
    Button buMicInListCount;
    @BindView(R.id.level_view)
    LevelView levelView;
    @BindView(R.id.tv_msg_name)
    TextView tvMsgName;
    @BindView(R.id.tv_msg_car)
    TextView tvMsgCar;
    @BindView(R.id.layout_come_msg)
    LinearLayout layoutComeMsg;
    @BindView(R.id.iv_finger_guessing_game_start)
    ImageView ivFingerGuessingGameStart;
    //    @BindView(R.id.rmv_match)
//    RoomMatchView rmvMatch;
    @BindView(R.id.activity_img)
    Banner activityImg;
    @BindView(R.id.icon_room_lottery_box)
    ImageView iconRoomLotteryBox;
    @BindView(R.id.vs_music_player)
    ViewStub vsMusicPlayer;

    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.iv_room_lock)
    ImageView ivRoomLock;
    @BindView(R.id.room_title)
    MarqueeTextView tvUserRoomName;
    @BindView(R.id.room_id)
    TextView tvRoomId;
    @BindView(R.id.tv_online_num)
    TextView tvOnlineNum;
    @BindView(R.id.tv_micro_charm)
    TextView tvMicroCharm;
    @BindView(R.id.tv_room_master_attention)
    ImageView tvRoomMasterAttention;
    @BindView(R.id.iv_room_operate)
    ImageView ivRoomOperate;
    @BindView(R.id.iv_room_share)
    ImageView ivRoomShare;
    Unbinder unbinder;
    @BindView(R.id.room_message_select_iv)
    ImageView messageSelectIv;

    private DynamicFaceDialog dynamicFaceDialog = null;
    private GiftDialog giftDialog = null;
    private boolean attentionFlag = false;
    protected RoomFunctionDialog functionDialog = null;
    protected RoomSettingModel roomSettingModel;
//    private RoomCallDialog roomCallDialog;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_av_room_game;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (roomCallDialog != null) {
//            roomCallDialog = null;
//        }
    }

    private int selectFlag = 0; //all,1.

    @Override
    protected void initClick() {
        //消息筛选
        messageSelectIv.setOnClickListener(v -> {
            selectFlag++;
            int bg;
            if (selectFlag == 1) {
                bg = R.mipmap.room_talk_message_select;
            } else if (selectFlag == 2) {
                bg = R.mipmap.room_gift_message_select;
            } else if (selectFlag == 3) {
                bg = R.mipmap.room_call_message_select;
            } else {
                selectFlag = 0;
                bg = R.mipmap.room_all_message_select;
            }
            messageView.setSelectFlag(selectFlag);
            messageSelectIv.setBackgroundResource(bg);
        });

        tvRoomDesc.setOnClickListener(v -> {
            if (AvRoomDataManager.get().isRoomOwner() || AvRoomDataManager.get().isRoomAdmin()) {
                RoomTopicActivity.start(getContext());
            } else {
                RoomTopicDIalog roomTopicDIalog = new RoomTopicDIalog();
                roomTopicDIalog.show(getChildFragmentManager());
            }
        });
        ivRoomRank.setOnClickListener(v -> {
//            TransWebViewActivity.start(getContext(), WebUrl.RANK_ROOM_URL);
            BigListDataDialog bigListDataDialog = BigListDataDialog.newContributionListInstance(getActivity());
            bigListDataDialog.setSelectOptionAction(new BigListDataDialog.SelectOptionAction() {
                @Override
                public void optionClick() {
                    getDialogManager().showProgressDialog(mContext, "请稍后");
                }

                @Override
                public void onDataResponse() {
                    //请求结束前退出可能会导致奔溃，直接捕获没关系
                    try {
                        getDialogManager().dismissDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            bigListDataDialog.show(getChildFragmentManager());
        });

        flGiftRecord.setOnClickListener(v -> {
//            if (giftMsgList == null || giftMsgList.isEmpty()) {
//                toast("暂无礼物记录!");
//            } else {
            recordDialog = new GiftRecordDialog(mContext);
            recordDialog.show();
            recordDialog.loadData(giftMsgList);
//            }
        });

        flSeaRecord.setOnClickListener(v -> {
//            seaRecordDialog = new SeaRecordDialog(mContext);
//            seaRecordDialog.show();
//            seaRecordDialog.loadData(null);

//            if (giftMsgList == null || giftMsgList.isEmpty()) {
            toast("暂无开盒子记录!");
            /*} else {
                recordDialog = new GiftRecordDialog(mContext);
                recordDialog.show();
                recordDialog.loadData(giftMsgList);
            }*/
        });
        ivFingerGuessingGameStart.setOnClickListener(v -> {
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                StartFingerGuessingGameDialog dialog1 = new StartFingerGuessingGameDialog();
                dialog1.show(getActivity().getSupportFragmentManager(), "");
            }
        });
        inputSend.setOnClickListener(v -> {
            RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (mCurrentRoomInfo != null && mCurrentRoomInfo.getPublicChatSwitch() == 1) {
                toast("公屏消息已经被管理员禁止，请稍候再试");
                return;
            }
            String sensitiveWordData = CoreManager.getCore(VersionsCore.class).getSensitiveWordData();
            String content = inputEdit.getText().toString().trim();
            if (!TextUtils.isEmpty(sensitiveWordData) && !TextUtils.isEmpty(content)) {
                if (content.matches(sensitiveWordData)) {
                    SingleToastUtil.showToast(v.getContext().getString(R.string.sensitive_word_data));
                    return;
                }
            }
            getMvpPresenter().sendTextMsg(content);
            inputEdit.setText("");
            messageView.scrollToBottom();
        });
        playTogether.setOnClickListener(v -> {
            if (!CoreManager.getCore(IFaceCore.class).isShowingFace()) {
                FaceInfo faceInfo = CoreManager.getCore(IFaceCore.class).getPlayTogetherFace();
                if (faceInfo != null) {
                    CoreManager.getCore(IFaceCore.class).sendAllFace(faceInfo);
                } else {
                    toast("加载失败，请重试!");
                }
            }
        });

        iconRoomLotteryBox.setOnClickListener(v -> {
//            if (roomCallDialog == null) {
//                roomCallDialog = new RoomCallDialog();
//            }
//            if (roomCallDialog.isAdded())
//                roomCallDialog.dismiss();
//            else
//                roomCallDialog.show(getChildFragmentManager(), "RoomColl");
            PoundEggDialog lotteryBoxDialog = PoundEggDialog.newOnlineUserListInstance();
            lotteryBoxDialog.show(getChildFragmentManager(), null);
        });

        flNo1Money.setOnClickListener(v -> showSuperRichPopup());
        buMicInListCount.setOnClickListener(v -> showMicInListDialog());
        //房间状态监听
        IMNetEaseManager.get().subscribeChatRoomEventObservable(roomEvent -> {
            if (roomEvent == null) {
                return;
            }
            int event = roomEvent.getEvent();
            switch (event) {
                case RoomEvent.ENTER_ROOM:
                case RoomEvent.ROOM_INFO_UPDATE:
                    updateRoomInfo();
                    break;
                default:
            }
        }, this);
        if (roomSettingModel == null) {
            roomSettingModel = new RoomSettingModel();
        }
        updateRoomInfo();
        tvRoomMasterAttention.setOnClickListener(v -> {
            RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (currentRoomInfo != null) {
                long likeUid = currentRoomInfo.getUid();
                getDialogManager().showProgressDialog(getActivity(), "请稍后...");
                if (!attentionFlag) {
                    CoreManager.getCore(IPraiseCore.class).praise(likeUid);
                } else {
                    CoreManager.getCore(IPraiseCore.class).cancelPraise(likeUid, true);
                }
            }
        });
        tvOnlineNum.setOnClickListener(v -> {
            FragmentActivity activity1 = getActivity();
            if (activity1 != null) {
                ListDataDialog.newOnlineUserListInstance(activity1).show(getChildFragmentManager());
            }
        });
        ivGoBack.setOnClickListener(v -> {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        });

        ivRoomShare.setOnClickListener(v -> {
            ShareDialog shareDialog = new ShareDialog(getActivity());
            shareDialog.setHasInvite(true);
            shareDialog.setOnShareDialogItemClick(this);
            shareDialog.show();
        });

        ivRoomOperate.setOnClickListener(v -> {
            showMoreItems();
        });
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null)
            CoreManager.getCore(IPraiseCore.class).isPraised(CoreManager.getCore(IAuthCore.class).getCurrentUid(), currentRoomInfo.getUid());
    }

    private void setDialogParams(int roleType) {
        final RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (mCurrentRoomInfo != null) {
            functionDialog.setRoleType(roleType);
            functionDialog.setPublicChatSwitch(mCurrentRoomInfo.getPublicChatSwitch());
            functionDialog.setVehicleSwitch(mCurrentRoomInfo.getGiftCardSwitch());
            functionDialog.setGiftSwitch(mCurrentRoomInfo.getGiftEffectSwitch());
        } else {
            functionDialog.setRoleType(-1);
        }
    }

    /***
     * 取消关注回调
     * @param
     * @param
     */
    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long likedUid, boolean showNotice) {
        getDialogManager().dismissDialog();
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            if (currentRoomInfo.getUid() == likedUid && showNotice) {
                attentionFlag = false;
                SingleToastUtil.showShortToast("取消关注成功");
                tvRoomMasterAttention.setImageResource(R.mipmap.bg_room_owner_attention_select);
            }
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onIsLiked(boolean islike, long uid) {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            if (currentRoomInfo.getUid() == uid) {
                attentionFlag = islike;
                if (islike) {
                    tvRoomMasterAttention.setImageResource(R.mipmap.bg_room_owner_attention_selected);
                } else {
                    tvRoomMasterAttention.setImageResource(R.mipmap.bg_room_owner_attention_select);
                }
                long curUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
                if (curUid == uid) {//如果当前是房主自己则不展示
                    tvRoomMasterAttention.setVisibility(View.GONE);
                }
            }
        }
    }


    private void showMoreItems() {
        if (functionDialog != null && functionDialog.isShowing()) {
            return;
        }
        String currentUid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        if (functionDialog == null) {
            functionDialog = new RoomFunctionDialog(mContext);
        }
        if (AvRoomDataManager.get().isRoomOwner(currentUid)) {
            setDialogParams(0);
        } else if (AvRoomDataManager.get().isRoomAdmin(currentUid)) {
            setDialogParams(1);
        } else {
            functionDialog.setRoleType(-1);
            if (AvRoomDataManager.get().isOnMic(myUid)) {
                functionDialog.setMusic(true);
            } else {
                functionDialog.setMusic(false);
            }
        }
        functionDialog.setOnFunctionClickListener(new FunctionClickListenerImpl());
        functionDialog.show();
        functionDialog.initData();
    }

    public class FunctionClickListenerImpl implements RoomFunctionDialog.OnFunctionClickListener {
        @Override
        public void onItemClick(RoomFunctionBean bean) {
            if (bean.getFunctionType() == RoomFunctionEnum.ROOM_QUIT) {
                if (getActivity() != null) {
                    ((AVRoomActivity) getActivity()).toBack();
                }
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_MINIMIZE) {
                //魅力值清零
//                AvRoomDataManager.get().setMinimize(true);
//                getActivity().finish();
                eliminateMeiLi();
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_SETTING) {
                int isPermitRoom = 2;
                if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                    isPermitRoom = AvRoomDataManager.get().mCurrentRoomInfo.getIsPermitRoom();
                }
                RoomSettingActivity.start(getContext(), AvRoomDataManager.get().mCurrentRoomInfo, isPermitRoom);
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_REPORT) {
                initButtonItems();
                getDialogManager().showCommonPopupDialog(buttons, "取消");
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_MUSIC) {
                musicPlayerView.setMusicClick();
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_PUBLIC_CLOSE) {
                IAuthCore core = CoreManager.getCore(IAuthCore.class);
                if (core == null) {
                    return;
                }
                String ticket = core.getTicket();
                long currentUid1 = core.getCurrentUid();
                CoreManager.getCore(IAVRoomCore.class).changeRoomMsgFilter(AvRoomDataManager.get().isRoomOwner(), 1, ticket, currentUid1);
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_PUBLIC_OPEN) {
                IAuthCore core = CoreManager.getCore(IAuthCore.class);
                if (core == null) {
                    return;
                }
                String ticket = core.getTicket();
                long currentUid1 = core.getCurrentUid();
                CoreManager.getCore(IAVRoomCore.class).changeRoomMsgFilter(AvRoomDataManager.get().isRoomOwner(), 0, ticket, currentUid1);
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_VEHICLE_CLOSE) {
                if (AvRoomDataManager.get().isRoomOwner()) {
                    updateRoomVehicleInfoByOwner(true, bean.getGiftSwitch(), 1);
                } else if (AvRoomDataManager.get().isRoomAdmin()) {
                    updateRoomVehicleInfoByAdmin(true, bean.getGiftSwitch(), 1);
                }
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_VEHICLE_OPEN) {
                if (AvRoomDataManager.get().isRoomOwner()) {
                    updateRoomVehicleInfoByOwner(true, bean.getGiftSwitch(), 0);
                } else if (AvRoomDataManager.get().isRoomAdmin()) {
                    updateRoomVehicleInfoByAdmin(true, bean.getGiftSwitch(), 0);
                }
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_GIFT_CLOSE) {
                if (AvRoomDataManager.get().isRoomOwner()) {
                    updateRoomVehicleInfoByOwner(false, 1, bean.getVehicleSwitch());
                } else if (AvRoomDataManager.get().isRoomAdmin()) {
                    updateRoomVehicleInfoByAdmin(false, 1, bean.getVehicleSwitch());
                }
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_GIFT_OPEN) {
                if (AvRoomDataManager.get().isRoomOwner()) {
                    updateRoomVehicleInfoByOwner(false, 0, bean.getVehicleSwitch());
                } else if (AvRoomDataManager.get().isRoomAdmin()) {
                    updateRoomVehicleInfoByAdmin(false, 0, bean.getVehicleSwitch());
                }
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_ANMIN_MANAGER) {
                RoomManagerListActivity.start(getActivity());
            } else if (bean.getFunctionType() == RoomFunctionEnum.ROOM_ENTER_HINT) {
                RoomPlayTipActivity.start(getActivity());
            }
        }
    }

    public void updateRoomVehicleInfoByAdmin(boolean showHintType, int giftEffect, int rideEffect) {
        final long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        roomSettingModel.updateByAdmin(roomInfo.getUid(), null, null, null, null, roomInfo.tagId, uid,
                CoreManager.getCore(IAuthCore.class).getTicket(), null, giftEffect, rideEffect, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {
                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onResponse(ServiceResult<RoomInfo> data) {
                        if (null != data && data.isSuccess()) {
                            AvRoomDataManager.get().mCurrentRoomInfo = data.getData();
                            if (showHintType) {
                                if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                                    IMNetEaseManager.get().systemNotificationBySdk(uid, rideEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE, -1);
                                }
                            } else {
                                if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                                    IMNetEaseManager.get().systemNotificationBySdk(uid, giftEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE, -1);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 更新房间设置信息
     */
    public void updateRoomVehicleInfoByOwner(boolean showHintType, int giftEffect, int rideEffect) {
        final long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        roomSettingModel.updateRoomInfo(null, null, null, null, roomInfo.tagId, uid, CoreManager.getCore(IAuthCore.class).getTicket(),
                null, giftEffect, rideEffect, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {
                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onResponse(ServiceResult<RoomInfo> data) {
                        if (null != data && data.isSuccess()) {
                            AvRoomDataManager.get().mCurrentRoomInfo = data.getData();
                            if (showHintType) {
                                if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                                    IMNetEaseManager.get().systemNotificationBySdk(uid, rideEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE, -1);
                                }
                            } else {
                                if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                                    IMNetEaseManager.get().systemNotificationBySdk(uid, giftEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE, -1);
                                }
                            }
                        }
                    }
                });
    }

    private List<ButtonItem> buttons = new ArrayList<>(4);

    private void initButtonItems() {
        if (!ListUtils.isListEmpty(buttons)) {
            return;
        }
        ButtonItem button0 = new ButtonItem("举报房名", () -> ButtonItemFactory.reportCommit(mContext, 2, 8));
        ButtonItem button1 = new ButtonItem("政治敏感", () -> ButtonItemFactory.reportCommit(mContext, 2, 1));
        ButtonItem button2 = new ButtonItem("色情低俗", () -> ButtonItemFactory.reportCommit(mContext, 2, 2));
        ButtonItem button3 = new ButtonItem("广告骚扰", () -> ButtonItemFactory.reportCommit(mContext, 2, 3));
        ButtonItem button4 = new ButtonItem("人身攻击", () -> ButtonItemFactory.reportCommit(mContext, 2, 4));
        buttons.add(button0);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
    }


    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraise(long uid) {
        getDialogManager().dismissDialog();
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            if (currentRoomInfo.getUid() == uid) {
                attentionFlag = true;
                SingleToastUtil.showShortToast("关注成功");
                tvRoomMasterAttention.setImageResource(R.drawable.bg_room_owner_attention_selected);
            }
        }
    }

    public void updateRoomInfo() {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            UserInfo mUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(currentRoomInfo.getUid());
            tvUserRoomName.setText(currentRoomInfo.getTitle());
            if (!StringUtil.isEmpty(currentRoomInfo.getRoomPwd())) {
                ivRoomLock.setVisibility(View.VISIBLE);
            } else {
                ivRoomLock.setVisibility(View.GONE);
            }
            setIdOnlineData(mUserInfo, currentRoomInfo.getOnlineNum());
        }
    }


    private void setIdOnlineData(UserInfo mUserInfo, int onlineNumber) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        if (mUserInfo != null) {
            tvRoomId.setText("ID: " + mUserInfo.getErbanNo());
        } else {
            if (roomInfo.getErbanNo() > 0) {
                tvRoomId.setText("ID: " + roomInfo.getErbanNo());
            } else {
                tvRoomId.setText("ID :0");
            }
        }
        tvOnlineNum.setText(NumberFormatUtils.getThousandStr(onlineNumber) + "人在线");
    }


    @Override
    protected void getState() {
//        if (rmvMatch != null) {
//            rmvMatch.getRoomMatchState(true);
//        }
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            getMvpPresenter().getFingerGuessingGameState(roomInfo.getRoomId());
        }
    }

    @Override
    protected void updateMatchView(boolean isDowm) {
        //不是房主直接消失
        if (!AvRoomDataManager.get().isRoomOwner()) {
            //下麦
            if (isDowm) {
                if (AvRoomDataManager.get().isOwnerOnMic()) {
                    return;
                }
//                if (rmvMatch != null) {
//                    rmvMatch.setShowState(true);
//                }
            } else {//上麦
                if (!AvRoomDataManager.get().isOwnerOnMic()) {
                    return;
                }
                getState();
            }
        }
    }

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void onMicInListChange() {
        int size = AvRoomDataManager.get().mMicInListMap.size();
        boolean b = size == 0 || !micInListOption || (!AvRoomDataManager.get().isRoomAdmin() && !AvRoomDataManager.get()
                .isRoomOwner());
        buMicInListCount.setVisibility(b ? View.GONE : View.VISIBLE);
        buMicInListCount.setText(size + "");
    }

    @Override
    protected void initMicroAdapter() {
        MicroViewAdapter microViewAdapter = microView.getAdapter();
        if (microViewAdapter != null) {
            microViewAdapter.setOnMicroItemClickListener(this);
        }
    }


    @Override
    protected void onIMReconnection() {
        autoUpMicStateChange();
        updateMicBtn();
        messageView.refreshAdapter();
    }

    @Override
    protected void onExitRoom() {
        if (messageView != null) {
            messageView.clear();
        }
    }

    @Override
    protected void upCharm(RoomEvent roomEvent) {
        if (microView != null && microView.getAdapter() != null) {
            microView.getAdapter().updateCharmData(roomEvent.getRoomCharmAttachment());
        }
    }

    protected void releaseView() {
        messageView.release();
        microView.release();
        onMusicPlayViewRelease();
        giftMsgList = null;
    }

    /***
     * 更新在线人数
     * @param onlineNumber
     */
    @Override
    protected void onRoomOnlineNumberSuccess(int onlineNumber) {
        tvOnlineNum.setText(NumberFormatUtils.getThousandStr(onlineNumber) + "人在线");
    }


    @Override
    protected void initLotteryBoxChange() {
        if (AvRoomDataManager.get().mCurrentRoomInfo != null && AvRoomDataManager.get().mCurrentRoomInfo.getGiftDrawEnable() == 1) {
            iconRoomLotteryBox.setVisibility(View.VISIBLE);
        } else {
            iconRoomLotteryBox.setVisibility(View.GONE);
        }
    }

    @Override
    protected void updateRoomTip(String playInfo) {
        if (messageView != null) messageView.updateRoomTip(playInfo);
    }

    @Override
    protected void bottomView(boolean inputMsgBtn) {
        bottomView.setInputMsgBtnEnable(inputMsgBtn);
    }

    @Override
    public void showFingerGuessingGame(boolean isShow) {
        ivFingerGuessingGameStart.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void upComeMsg(int vs) {
        layoutComeMsg.setVisibility(vs);
    }

    @Override
    protected void startComeMsgAnim(TranslateAnimation animation) {
        layoutComeMsg.startAnimation(animation);
    }

    @Override
    protected void hideInputLayout() {
        if (inputLayout != null)
            inputLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDownMicro(int micPosition) {
        showBottomViewForDifRole();
        updateMicBtn();
        microView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onQueueMicStateChange(int micPosition, int micPosState) {
        microView.getAdapter().notifyItemChanged(micPosition);
        onMicStateChanged();
    }

    //神豪榜
    @Override
    public void reDaySuperRich(RoomConsumeInfo roomConsumeInfo) {
        ImageLoadUtils.loadCircleImage(mContext, roomConsumeInfo.getAvatar(), flNo1Money, 0);
        if (popupContentView != null) {
            TextView tvNickName = popupContentView.findViewById(R.id.tv_nickname);
            ImageView ivAvatar = popupContentView.findViewById(R.id.iv_super_rich_avatar);
            tvNickName.setText(roomConsumeInfo.getNick());
            ImageLoadUtils.loadCircleImage(mContext, roomConsumeInfo.getAvatar(), ivAvatar, R.mipmap.iv_rich_avatar_default);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onSetListener() {
        bottomView.setBottomViewListener(new GameRoomBottomViewWrapper());

        inputLayout.setOnTouchListener((v, event) -> {
            inputEdit.clearFocus();
            inputLayout.setVisibility(View.GONE);
            hideKeyBoard();
            return false;
        });
        softKeyboardListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 底部按钮点击处理
     */
    private class GameRoomBottomViewWrapper extends BottomViewListenerWrapper {
        @Override
        public void onOpenMicBtnClick() {

            //没有im的时候不给操作
            if (!ReUsedSocketManager.get().isConnect()) {
                SingleToastUtil.showShortToast("网络已断开无法操作");
                return;
            }

            RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByAccount(
                    String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
            if (roomQueueInfo == null || roomQueueInfo.mRoomMicInfo == null) return;
            //先判断麦上是否是开麦的
            if (!roomQueueInfo.mRoomMicInfo.isMicMute() && !RtcEngineManager.get().isAudienceRole()) {
                //AvRoomDataManager.get().mIsNeedOpenMic = !AvRoomDataManager.get().mIsNeedOpenMic;
                RtcEngineManager.get().setMute(!RtcEngineManager.get().isMute());
                updateMicBtn();
            }
        }

        @Override
        public void onSendFaceBtnClick() {
            if (AvRoomDataManager.get().isOnMic(myUid) || AvRoomDataManager.get().isRoomOwner()) {
                if (dynamicFaceDialog == null) {
                    dynamicFaceDialog = new DynamicFaceDialog(getContext());
                    dynamicFaceDialog.setOnDismissListener(dialog -> dynamicFaceDialog = null);
                }
                if (!dynamicFaceDialog.isShowing()) {
                    dynamicFaceDialog.show();
                }
            } else {
                toast("上麦才能发表情哦!");
            }

        }

        @Override
        public void onSendMsgBtnClick() {
            if (ChatUtil.checkBanned()) {
                return;
            }
            inputLayout.setVisibility(View.VISIBLE);
            inputEdit.setText("");
            inputEdit.setFocusableInTouchMode(true);
            inputEdit.requestFocus();
            showKeyBoard();
        }

        @Override
        public void onSendGiftBtnClick() {
            if (giftDialog == null) {
                giftDialog = new GiftDialog(getContext(), 0, "", "", false);
                giftDialog.setGiftDialogBtnClickListener(AudioFragment.this);
                giftDialog.setOnDismissListener(dialog -> giftDialog = null);
            }
            if (!giftDialog.isShowing()) {
                giftDialog.show();
            }
        }

        @Override
        public void onShareBtnClick() {
            ShareDialog shareDialog = new ShareDialog(getActivity());
            shareDialog.setHasInvite(true);
            shareDialog.setOnShareDialogItemClick(AudioFragment.this);
            shareDialog.show();
        }

        @Override
        public void onRemoteMuteBtnClick() {
            RtcEngineManager.get().setRemoteMute(!RtcEngineManager.get().isRemoteMute());
            updateRemoteMuteBtn();
        }

        @Override
        public void onLotteryBoxeBtnClick() {

            PoundEggDialog lotteryBoxDialog = PoundEggDialog.newOnlineUserListInstance();
            lotteryBoxDialog.show(getChildFragmentManager(), null);
        }


        @Override
        public void onBuShowMicInList() {
            if (AvRoomDataManager.get().isOwnerOnMic()) {
                toast("您已经在麦上");
                return;
            }
            showMicInListDialog();
        }

        @Override
        public void onMsgBtnClick() {
            RoomPrivateMsgDialog msgDialog = new RoomPrivateMsgDialog();
            msgDialog.show(getChildFragmentManager(), null);
        }
    }


    /**
     * 显示活动图标
     */
    @Override
    public void showActivity(List<ActionDialogInfo> dialogInfo) {
        if (!ListUtils.isListEmpty(dialogInfo)) {
            activityImg.setVisibility(View.VISIBLE);
            activityImg.setHintView(null);
            RoomBannerAdapter bannerAdapter = new RoomBannerAdapter(dialogInfo, getActivity());
            activityImg.setAnimationDurtion(500);
            activityImg.setPlayDelay(3000);
            activityImg.setAdapter(bannerAdapter);
            bannerAdapter.notifyDataSetChanged();
        } else {
            activityImg.setVisibility(View.GONE);
        }
    }

    private MusicPlayerView musicPlayerView;

    public void onShowMusicPlayView(UserInfo roomOwnner, boolean isOnMic) {
        if (roomOwnner != null && isOnMic) {
            if (musicPlayerView == null) {

                musicPlayerView = (MusicPlayerView) vsMusicPlayer.inflate();
            }
//            musicPlayerView.setVisibility(View.VISIBLE);
            musicPlayerView.setImageBg(roomOwnner.getAvatar());
        }
        if (musicPlayerView != null) {
//            musicPlayerView.setVisibility(isOnMic ? View.VISIBLE : View.GONE);
        }
    }

    public void onMusicPlayViewUpdate() {
        if (musicPlayerView != null) {
            musicPlayerView.updateVoiceValue();
        }
    }

    @Override
    public void onUpMicro(int micPosition) {
        showBottomViewForDifRole();
        updateMicBtn();
        microView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void setLevel(int level) {
        levelView.setExperLevel(level);
    }

    @Override
    protected void setLevelDraw(int draw) {
        layoutComeMsg.setBackgroundResource(draw);
    }

    @Override
    protected void setComeInfo(RoomMemberComeInfo comeInfo) {
        tvMsgName.setText(comeInfo.getNickName());
        String carName = comeInfo.getCarName();
        tvMsgCar.setText(TextUtils.isEmpty(carName) ? getString(R.string.come_msg_tip_not_car)
                : getString(R.string.come_msg_tip_car, carName));
    }

    @Override
    protected void updateView() {
        microView.getAdapter().notifyDataSetChanged();
        // 更新底栏
        showBottomViewForDifRole();
    }

    @Override
    protected void updateMicBtn() {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            if (RtcEngineManager.get().isAudienceRole()) {
                bottomView.setMicBtnEnable(false);
                bottomView.setMicBtnOpen(false);
            } else {
                if (RtcEngineManager.get().isMute()) {
                    bottomView.setMicBtnEnable(true);
                    bottomView.setMicBtnOpen(false);
                } else {
                    bottomView.setMicBtnEnable(true);
                    bottomView.setMicBtnOpen(true);
                }
            }
        } else {
            bottomView.setMicBtnEnable(false);
            bottomView.setMicBtnOpen(false);
        }
    }

    @Override
    protected void updateRemoteMuteBtn() {
        if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
            bottomView.setRemoteMuteOpen(!RtcEngineManager.get().isRemoteMute());
        }
    }

    @Override
    protected void changeState() {
        int unreadCount = CoreManager.getCore(IIMMessageCore.class).queryUnreadMsg();
        if (unreadCount > 0) {
            bottomView.showMsgMark(true);
        } else {
            bottomView.showMsgMark(false);
        }
    }


    /**
     * 根据角色显示不同的状态
     */
    protected void showBottomViewForDifRole() {
        boolean isOnMic = AvRoomDataManager.get().isOnMic(myUid);
        // 更新播放器界面
        RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (mCurrentRoomInfo == null) return;
        roomOwnner = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(mCurrentRoomInfo.getUid());
        onShowMusicPlayView(roomOwnner, isOnMic);
        if (isOnMic) {
            bottomView.showHomePartyUpMicBottom();
        } else {
            bottomView.showHomePartyDownMicBottom();
        }
    }

    public void onMusicPlayViewRelease() {
        if (musicPlayerView != null) {
            musicPlayerView.release();
        }
    }


    @Override
    public void onAvatarSendMsgClick(int micPosition) {
        if (ChatUtil.checkBanned()) {
            return;
        }
        RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoom == null) return;
        RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(micPosition);
        if (roomQueueInfo == null) return;
        IMChatRoomMember chatRoomMember = roomQueueInfo.mChatRoomMember;
        RoomMicInfo roomMicInfo = roomQueueInfo.mRoomMicInfo;
        if (chatRoomMember == null && micPosition == -1) {
            chatRoomMember = new IMChatRoomMember();
            RoomInfo info = AvRoomDataManager.get().mCurrentRoomInfo;
            chatRoomMember.setNick("房主");
            chatRoomMember.setAvatar("");
            chatRoomMember.setAccount(info.getUid() + "");
        }
        if (chatRoomMember == null || roomMicInfo == null) return;
        String str = "@" + chatRoomMember.getNick() + " ";
        if (inputLayout.getVisibility() == View.VISIBLE) {
            if (inputEdit.getText() != null) {
                str = inputEdit.getText().toString() + str;
            }
            inputEdit.setText(str);
        } else {
            inputLayout.setVisibility(View.VISIBLE);
            inputEdit.setText(str);
            inputEdit.setSelection(str.length());
            inputEdit.setFocusable(true);
            inputEdit.setFocusableInTouchMode(true);
            inputEdit.requestFocus();
            showKeyBoard();
        }
    }

    @Override
    public void upMicroCharm(long charm, boolean isShow) {
        if (isShow != (tvMicroCharm.getVisibility() == View.VISIBLE)) {
            tvMicroCharm.setVisibility(View.VISIBLE);
        }
        tvMicroCharm.setText(NumberFormatUtils.getBigDecimal(charm));
//        tvMicroCharm.setOnClickListener((v) -> {
//            View popView = LayoutInflater.from(mContext).inflate(R.layout.dialog_charm_number, null);
//            PopupWindow myPop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape_white_round_10);
//            myPop.setBackgroundDrawable(drawable);
//            myPop.setOutsideTouchable(true);
//            myPop.getContentView().measure(0, 0);
//            myPop.showAsDropDown(tvMicroCharm, Gravity.CENTER_HORIZONTAL, ConvertUtils.dp2px(-25), 30);
//            TextView viewById = popView.findViewById(R.id.tv_charm_number);
//            viewById.setText("" + charm);
//            myPop.setOnDismissListener(() -> {
//                tvMicroCharm.setVisibility(View.VISIBLE);
//            });
//
//            tvMicroCharm.setVisibility(View.INVISIBLE);
//        });
    }


    /**
     * 打开软键盘并显示头布局
     */
    public void showKeyBoard() {
        inputLayout.postDelayed(() -> {
            InputMethodManager imm = null;
            if (getActivity() != null) {
                imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            if (imm == null) return;
            imm.showSoftInput(inputEdit, InputMethodManager.SHOW_FORCED);
        }, 80);
    }

    /**
     * 隐藏软键盘并隐藏头布局
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(inputEdit.getWindowToken(), 0);
    }

    @Override
    public void chatRoomReConnectView() {
        if (microView != null && microView.getAdapter() != null) {
            microView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void notifyRefresh() {
        MicroViewAdapter microViewAdapter = microView.getAdapter();
        if (microViewAdapter != null) {
            microViewAdapter.notifyDataSetChanged();
        }
    }
}
