package com.vslk.lbgx.room.avroom.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.room.audio.widget.MusicPlayerView;
import com.vslk.lbgx.room.avroom.activity.LightChatOnlineActivity;
import com.vslk.lbgx.room.avroom.activity.RoomBlackListActivity;
import com.vslk.lbgx.room.avroom.activity.RoomManagerListActivity;
import com.vslk.lbgx.room.avroom.adapter.UserListViewAdapter;
import com.vslk.lbgx.room.avroom.other.BottomViewListenerWrapper;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.avroom.other.SoftKeyBoardListener;
import com.vslk.lbgx.room.avroom.widget.BottomView;
import com.vslk.lbgx.room.avroom.widget.GiftView;
import com.vslk.lbgx.room.avroom.widget.MessageView;
import com.vslk.lbgx.room.avroom.widget.UserListView;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.room.widget.dialog.UserInfoDialog;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.praise.HiPraiseAnimationView;
import com.vslk.lbgx.ui.praise.HiPraiseWithCallback;
import com.vslk.lbgx.ui.praise.OnDrawCallback;
import com.vslk.lbgx.ui.praise.base.IPraise;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.LightChatRoomPresenter;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_core.room.view.ILightChatRoomView;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_core.share.IShareCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.utils.StringUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.sharesdk.framework.Platform;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 轻聊房
 *
 * @author chenran
 * @date 2017/7/26
 */
@CreatePresenter(LightChatRoomPresenter.class)
public class LightChatRoomFragment extends BaseMvpFragment<ILightChatRoomView, LightChatRoomPresenter>
        implements View.OnClickListener, UserListViewAdapter.UserListViewItemClickListener,
        GiftDialog.OnGiftDialogBtnClickListener, ShareDialog.OnShareDialogItemClick, ILightChatRoomView {
    private RoomInfo currentRoomInfo;
    private long myUid;
    private CircleImageView roomOwnerAvatar;
    private ImageView roomOwnerSpeek;
    private TextView roomOwnerNick;
    private TextView roomNumber;
    private UserInfo roomOwnner;
    private ImageView attentionView;
    private ImageView roomMore;
    private MessageView messageView;
    private BottomView bottomView;
    private UserListView userListView;
    private GiftView giftView;
    private RelativeLayout inputLayout;
    private MusicPlayerView musicPlayerView;
    private EditText inputEdit;
    private ImageView inputSend;
    private View praiseClickView;
    private HiPraiseAnimationView praiseAnimationView;
    private ImageView notice;
    private ImageView contributeList;
    private String shareName;
    private ImageView activityImg;

    private Disposable mDisposable;

    private SparseArray<SoftReference<Bitmap>> mBitmapCacheArray = new SparseArray<>();
    private static final int HEARTS[] = new int[]{
            R.drawable.heart_1,
            R.drawable.heart_2,
            R.drawable.heart_3,
            R.drawable.heart_4,
            R.drawable.heart_5,
            R.drawable.heart_6,
            R.drawable.heart_7,
            R.drawable.heart_8,
            R.drawable.heart_9,
            R.drawable.heart_10,
            R.drawable.heart_11
    };
    private List<ActionDialogInfo> mDialogInfos;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_av_room_home_party;
    }

    @Override
    public void onFindViews() {
        roomOwnerAvatar = mView.findViewById(R.id.room_owner_avatar);
        roomOwnerSpeek = mView.findViewById(R.id.room_owner_speek);
        roomOwnerNick = mView.findViewById(R.id.room_owner_nick);
        roomNumber = mView.findViewById(R.id.room_owner_number);
        attentionView = mView.findViewById(R.id.attention_view);
        messageView = mView.findViewById(R.id.message_view);
        bottomView = mView.findViewById(R.id.bottom_view);
        musicPlayerView = mView.findViewById(R.id.music_player_view);
        userListView = mView.findViewById(R.id.user_list_view);
        inputLayout = mView.findViewById(R.id.input_layout);
        inputEdit = mView.findViewById(R.id.input_edit);
        inputSend = mView.findViewById(R.id.input_send);
        roomMore = mView.findViewById(R.id.room_more);
        giftView = mView.findViewById(R.id.gift_view);
        praiseClickView = mView.findViewById(R.id.praise_click_view);
        praiseAnimationView = mView.findViewById(R.id.praise_animation_view);
        notice = mView.findViewById(R.id.auction_notice);
        contributeList = mView.findViewById(R.id.contribute_list);

        activityImg = mView.findViewById(R.id.activity_img);
    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            addPraiseWithCallback();

            Random rand = new Random();
            int i = rand.nextInt(3);
            handler.postDelayed(this, i * 1000);
        }
    };

    @Override
    public void initiate() {
        currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();

        updateView();
        updateMicBtn();
        setListeningNumber();

        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if (currentRoomInfo != null) {
            if (uid == currentRoomInfo.getUid()) {
                attentionView.setVisibility(View.GONE);
                //将房主设置进队列,默认房主在队列上
                getMvpPresenter().upMicroPhone(-1, String.valueOf(currentRoomInfo.getUid()), false);
            } else {
                attentionView.setVisibility(View.VISIBLE);
                CoreManager.getCore(IPraiseCore.class).isPraised(myUid, currentRoomInfo.getUid());
            }
        }

        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        roomOwnerAvatar.startAnimation(operatingAnim);

        handler.postDelayed(task, 3000);

        mDisposable = IMNetEaseManager.get().getChatRoomEventObservable()
                .subscribe(new Consumer<RoomEvent>() {
                    @Override
                    public void accept(RoomEvent roomEvent) throws Exception {
                        if (roomEvent == null) return;
                        onReceiveRoomMessage(roomEvent);
                    }
                });
    }

    private void onReceiveRoomMessage(RoomEvent roomEvent) {
        int event = roomEvent.getEvent();
        switch (event) {
            case RoomEvent.UP_MIC:
            case RoomEvent.DOWN_MIC:
                if (roomEvent.getMicPosition() == -1 || roomEvent.getMicPosition() == Integer.MIN_VALUE) {
                    return;
                }
                if (AvRoomDataManager.get().isOwner(roomEvent.getAccount())) {
                    upDownMicroView();
                    updateMicBtn();
                }
                break;
            case RoomEvent.KICK_DOWN_MIC:
                toast(R.string.kick_mic);
                break;
            case RoomEvent.INVITE_UP_MIC:
                onMicInvite(roomEvent.getMicPosition());
                break;
            case RoomEvent.KICK_OUT_ROOM:
//                ChatRoomKickOutEvent reason = roomEvent.getReason();
//                if (reason != null && reason.getReason() == ChatRoomKickOutEvent.ChatRoomKickOutReason.CHAT_ROOM_INVALID) {
//                    //清除资源
//                    releaseView();
//                }
                if (roomEvent.getReason_no() == 3) {
                    //清除资源
                    releaseView();
                }
                break;
            default:
        }
    }

    public void setListeningNumber() {
        roomNumber.setText(getString(R.string.listening_number, currentRoomInfo.getOnlineNum()));
    }

    @Override
    public void onSetListener() {
        mView.findViewById(R.id.iv_user_online).setOnClickListener(this);

        activityImg.setOnClickListener(this);
        inputSend.setOnClickListener(this);
        roomMore.setOnClickListener(this);
        userListView.setListViewItemClickListener(this);
        praiseClickView.setOnClickListener(this);
        bottomView.setBottomViewListener(new HomePartyRoomBottomViewWrapper());
        praiseAnimationView.setOnClickListener(this);
        attentionView.setOnClickListener(this);
        roomOwnerAvatar.setOnClickListener(this);
        notice.setOnClickListener(this);
        contributeList.setOnClickListener(this);
        inputLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputEdit.clearFocus();
                inputLayout.setVisibility(View.GONE);
                hideKeyboard();
                return false;
            }
        });
        softKeyboardListener();
    }

    public void showActivity(List<ActionDialogInfo> dialogInfos) {
        mDialogInfos = dialogInfos;
        if (!ListUtils.isListEmpty(dialogInfos)) {
            activityImg.setVisibility(View.VISIBLE);
            ActionDialogInfo actionDialogInfo = dialogInfos.get(0);
            ImageLoadUtils.loadImage(getContext(), actionDialogInfo.getAlertWinPic(), activityImg);
        } else {
            activityImg.setVisibility(View.GONE);
        }
    }


    public void releaseView() {
        messageView.release();
        giftView.release();
        musicPlayerView.release();
        roomOwnerAvatar.clearAnimation();
        handler.removeCallbacks(task);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (praiseAnimationView != null) {
            praiseAnimationView.start();
        }
        if (musicPlayerView != null) {
            musicPlayerView.updateVoiceValue();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (praiseAnimationView != null) {
            praiseAnimationView.stop();
        }
    }

    /**
     * 添加具有回调的点赞动画
     */
    private void addPraiseWithCallback() {
        final IPraise hiPraiseWithCallback = new HiPraiseWithCallback(getHeartBitmap(),
                new OnDrawCallback() {
                    @Override
                    public void onFinish() {
                    }
                });
        praiseAnimationView.addPraise(hiPraiseWithCallback);
    }

    private Bitmap getHeartBitmap() {
        final int id = HEARTS[new Random().nextInt(HEARTS.length)];
        SoftReference<Bitmap> bitmapRef = mBitmapCacheArray.get(id);
        Bitmap retBitmap = null;
        if (null != bitmapRef) {
            retBitmap = bitmapRef.get();
        }
        if (null == retBitmap) {
            retBitmap = BitmapFactory.decodeResource(getResources(),
                    id);
            mBitmapCacheArray.put(id, new SoftReference<>(retBitmap));
        }
        return retBitmap;
    }

    private void updateView() {
        currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            roomOwnner = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(currentRoomInfo.getUid());
            if (roomOwnner != null) {
                ImageLoadUtils.loadAvatar(getContext(), roomOwnner.getAvatar(), roomOwnerAvatar);
                roomOwnerNick.setText(roomOwnner.getNick());
                if (!StringUtils.isBlank(currentRoomInfo.getBackPic())) {
                    musicPlayerView.setImageBg(currentRoomInfo.getBackPic());
                } else {
                    musicPlayerView.setImageBg(roomOwnner.getAvatar());
                }
            }

            if (AvRoomDataManager.get().isRoomOwner()) {
                musicPlayerView.setVisibility(View.VISIBLE);
                bottomView.showLightChatUpMicBottom();
            } else {
                upDownMicroView();
            }
        }
    }

    /**
     * 上下麦更新
     */
    private void upDownMicroView() {
        if (!AvRoomDataManager.get().isOnMic(myUid)) {
            musicPlayerView.setVisibility(View.INVISIBLE);
            bottomView.showLightChatDownMicBottom();
        } else {
            musicPlayerView.setVisibility(View.VISIBLE);
            bottomView.showLightChatUpMicBottom();
        }
    }

    private void updateMicBtn() {
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

    private void updateRemoteMuteBtn() {
        if (currentRoomInfo != null) {
            if (RtcEngineManager.get().isRemoteMute())
                bottomView.setRemoteMuteOpen(false);
            else {
                bottomView.setRemoteMuteOpen(true);
            }
        }
    }

    private void showMoreItems() {
        List<ButtonItem> buttonItems = new ArrayList<>();
        ButtonItem buttonItem1 = new ButtonItem("最小化", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                AvRoomDataManager.get().setMinimize(true);
                getActivity().finish();
            }
        });
        ButtonItem buttonItem2 = new ButtonItem("退出房间", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                ((AVRoomActivity) getActivity()).toBack();
            }
        });
        ButtonItem buttonItem3 = new ButtonItem("管理员", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                RoomManagerListActivity.start(getContext());
            }
        });
        ButtonItem buttonItem4 = new ButtonItem("黑名单", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                RoomBlackListActivity.start(getContext());
            }
        });

        if (AvRoomDataManager.get().isRoomOwner()) {
            buttonItems.add(buttonItem3);
            buttonItems.add(buttonItem4);
        }

        if (AvRoomDataManager.get().isRoomAdmin()) {
            buttonItems.add(buttonItem4);
        }

        buttonItems.add(buttonItem1);
        buttonItems.add(buttonItem2);

        getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel));
    }

    //------------------------------IPraiseClient--------------------------------
    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onIsLiked(Boolean islike, long uid) {
        if (uid == roomOwnner.getUid() && uid != myUid) {
            if (islike) {
                attentionView.setVisibility(View.GONE);
            } else {
                attentionView.setVisibility(View.VISIBLE);
            }
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long uid, boolean showNotice) {
        if (uid == roomOwnner.getUid() && uid != myUid) {
            attentionView.setVisibility(View.VISIBLE);
        }
    }

    public void onMicInvite(final int position) {
        getDialogManager().showOkCancelDialog("房主邀请您上麦，是否上麦？",
                true, new DialogManager.AbsOkDialogListener() {
                    @Override
                    public void onOk() {
                        getMvpPresenter().upMicroPhone(position,
                                String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()), true);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        super.onDestroyView();
        releaseView();
    }

    @Override
    public void praiseSuccess(int type, long likedUid) {
        if (roomOwnner == null) return;
        if (likedUid == roomOwnner.getUid() && likedUid != myUid) {
            attentionView.setVisibility(View.GONE);
            // 更新被关注人的个人信息页面
            CoreManager.getCore(IUserCore.class).requestUserInfo(likedUid);
            // 发送公屏信息
            IMNetEaseManager.get().sendTipMsg(likedUid,
                    IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_ATTENTION_ROOM_OWNER);
//                    .subscribe(new Consumer<ChatRoomMessage>() {
//                        @Override
//                        public void accept(ChatRoomMessage chatRoomMessage) throws Exception {
//                            if (chatRoomMessage != null) {
//                                IMNetEaseManager.get().addMessagesImmediately(chatRoomMessage);
//                            }
//                        }
//                    });
            toast("关注成功，相互关注可成为好友哦！");
        }
    }

    @Override
    public void praiseFail(int type, long likedUid) {

    }

    @Override
    public void kickDownMicroPhoneSuccess() {
        //踢人下麦成功
    }

    private class HomePartyRoomBottomViewWrapper extends BottomViewListenerWrapper {
        @Override
        public void onOpenMicBtnClick() {
            RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByAccount(
                    String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
            if (roomQueueInfo == null || roomQueueInfo.mRoomMicInfo == null) return;
            //先判断麦上是否是开麦的
            if (!roomQueueInfo.mRoomMicInfo.isMicMute() && !RtcEngineManager.get().isAudienceRole()) {
                AvRoomDataManager.get().mIsNeedOpenMic = !AvRoomDataManager.get().mIsNeedOpenMic;
                RtcEngineManager.get().setMute(!RtcEngineManager.get().isMute());
                updateMicBtn();
            }
        }

        @Override
        public void onSendMsgBtnClick() {
            inputLayout.setVisibility(View.VISIBLE);
            inputEdit.setText("");
            inputEdit.setFocusableInTouchMode(true);
            inputEdit.requestFocus();
            showKeyboard();
        }

        GiftDialog giftDialog;

        @Override
        public void onSendGiftBtnClick() {
            if (roomOwnner == null) return;
            if (giftDialog != null && giftDialog.isShowing()) return;
            giftDialog = new GiftDialog(getContext(), roomOwnner.getUid(), roomOwnner.getNick(), roomOwnner.getAvatar());
            giftDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    giftDialog = null;
                }
            });
            giftDialog.setGiftDialogBtnClickListener(LightChatRoomFragment.this);
            giftDialog.show();
        }

        @Override
        public void onShareBtnClick() {
            ShareDialog shareDialog = new ShareDialog(getActivity());
            shareDialog.setOnShareDialogItemClick(LightChatRoomFragment.this);
            shareDialog.show();
        }

        @Override
        public void onRemoteMuteBtnClick() {
            RtcEngineManager.get().setRemoteMute(!RtcEngineManager.get().isRemoteMute());
            updateRemoteMuteBtn();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_send:
                getMvpPresenter().sendRoomTextMsg(inputEdit.getText().toString());
                inputEdit.setText("");
                break;
            case R.id.praise_click_view:
                addPraiseWithCallback();
                break;
            case R.id.praise_animation_view:
                addPraiseWithCallback();
                break;
            case R.id.room_more:
                showMoreItems();
                break;
            case R.id.attention_view:
                if (currentRoomInfo != null) {
                    getMvpPresenter().praise(1, currentRoomInfo.getUid());
                }
                break;
            case R.id.room_owner_avatar:
                UserInfoDialog.showUserDialog(getContext(), roomOwnner.getUid());
                break;
            case R.id.auction_notice:
                if (currentRoomInfo != null) {
                    if (!StringUtil.isEmpty(currentRoomInfo.getRoomDesc())) {
                        getDialogManager().showOkBigTips("房间公告", currentRoomInfo.getRoomDesc(), true, null);
                    } else {
                        getDialogManager().showOkBigTips("房间公告", "暂无公告", true, null);
                    }
                }
                break;
            case R.id.activity_img:
                if (!ListUtils.isListEmpty(mDialogInfos)) {
                    ActionDialogInfo dialogInfo = mDialogInfos.get(0);
                    CommonWebViewActivity.start(getContext(), dialogInfo.getSkipUrl());
                }
                break;
            case R.id.contribute_list:
                ((LightChatFragment) getParentFragment()).showListFragment();
                break;
            case R.id.iv_user_online:
                LightChatOnlineActivity.openActivity(getActivity(), 0);
                break;
            default:
        }
    }


    /**
     * 显示软键盘并因此头布局
     */
    private void showKeyboard() {
        inputLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm == null) return;
                imm.showSoftInput(inputEdit, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘并显示头布局
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(inputEdit.getWindowToken(), 0);
    }


    private SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            mOnSoftKeyBoardChangeListener = new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
        @Override
        public void keyBoardShow(int height) {
            /*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
        }

        @Override
        public void keyBoardHide(int height) {
            /*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
            inputLayout.setVisibility(View.GONE);
        }
    };

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListener() {
        SoftKeyBoardListener.setListener(getActivity(), mOnSoftKeyBoardChangeListener);
    }

    @Override
    public void onItemClicked(IMChatRoomMember chatRoomMember) {
        onClickHeadToDialog(chatRoomMember);
    }

    private void onClickHeadToDialog(final IMChatRoomMember chatRoomMember) {
        if (chatRoomMember == null) return;
        String account = chatRoomMember.getAccount();
        boolean mySelf = AvRoomDataManager.get().isOwner(account);

        List<ButtonItem> buttonItems = new ArrayList<>();
        ButtonItem downMicItem = ButtonItemFactory.createDownMicItem();
        ButtonItem showUserInfoButtonItem = ButtonItemFactory.createCheckUserInfoDialogItem(mContext, account);
        if (mySelf) {
            buttonItems.add(showUserInfoButtonItem);
            buttonItems.add(downMicItem);
            getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel));
            return;
        }

        boolean roomOwner = AvRoomDataManager.get().isRoomOwner();
        boolean isRoomAdmin = AvRoomDataManager.get().isRoomAdmin();
        final RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        String roomId = String.valueOf(currentRoomInfo.getRoomId());

        ButtonItem kickDownMicButtonItem = ButtonItemFactory.createKickDownMicItem(chatRoomMember.getNick(), account);
        ButtonItem kickOutRoomItem = ButtonItemFactory.createKickOutRoomItem(roomId, mContext, account, chatRoomMember.getNick());
        //设置管理员
        ButtonItem markManagerListItem = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(),roomId, account, true);
        //取消管理员
        ButtonItem noMarkManagerListItem = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(),roomId, account, false);
        //加入黑名单
        ButtonItem markBlackListItem = ButtonItemFactory.createMarkBlackListItem(roomId, mContext, chatRoomMember.getAccount(), chatRoomMember.getNick());
        if (roomOwner) {
            //房主点击
            buttonItems.add(showUserInfoButtonItem);
            buttonItems.add(kickDownMicButtonItem);
            buttonItems.add(kickOutRoomItem);
            buttonItems.add(AvRoomDataManager.get().isRoomAdmin(account) ? noMarkManagerListItem : markManagerListItem);
            buttonItems.add(markBlackListItem);
            getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel));
        } else if (isRoomAdmin) {
            //管理员点击
            if (!AvRoomDataManager.get().isRoomAdmin(account) && !AvRoomDataManager.get().isRoomOwner(account)) {
                buttonItems.add(showUserInfoButtonItem);
                buttonItems.add(kickDownMicButtonItem);
                buttonItems.add(kickOutRoomItem);
                buttonItems.add(markBlackListItem);
                getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel));
            } else {
                UserInfoDialog.showUserDialog(getContext(), JavaUtil.str2long(account));
            }
        } else {
            UserInfoDialog.showUserDialog(getContext(), JavaUtil.str2long(account));
        }
    }

    @Override
    public void onRechargeBtnClick() {
        WalletActivity.start(getContext());
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP) {
        CoreManager.getCore(IGiftCore.class).sendRoomGift(giftInfo.getGiftId(), uid,
                currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP) {

    }

    @Override
    public void onSharePlatformClick(Platform platform) {
        shareName = platform.getName();
        CoreManager.getCore(IShareCore.class).shareRoom(platform, roomOwnner.getUid(), currentRoomInfo.getTitle());
    }


    //------------------------------IUserClient--------------------------------
    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        if (currentRoomInfo != null) {
            if (currentRoomInfo.getUid() == info.getUid()) {
                updateView();
            }
        }
    }

    //------------------------------IShareCoreClient----------------------------------
    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoom() {
        toast("分享成功");
    }

    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoomFail() {
        toast("分享失败");
    }

    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoomCancel() {
        toast("取消分享");
    }
}
