package com.vslk.lbgx.room.avroom.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.room.presenter.AuctionPresenter;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_core.room.view.IAuctionView;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_core.share.IShareCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.utils.StringUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
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
import com.vslk.lbgx.room.avroom.widget.AuctionView;
import com.vslk.lbgx.room.avroom.widget.BottomView;
import com.vslk.lbgx.room.avroom.widget.GiftView;
import com.vslk.lbgx.room.avroom.widget.MessageView;
import com.vslk.lbgx.room.avroom.widget.UserListView;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.room.widget.dialog.AuctionDialog;
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
import com.vslk.lbgx.utils.UIHelper;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.sharesdk.framework.Platform;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author xiaoyu
 * @date 2017/12/30
 */
@CreatePresenter(AuctionPresenter.class)
public class AuctionRoomFragment extends BaseMvpFragment<IAuctionView, AuctionPresenter> implements View.OnClickListener, IAuctionView,
        UserListViewAdapter.UserListViewItemClickListener, GiftDialog.OnGiftDialogBtnClickListener, ShareDialog.OnShareDialogItemClick {
    private RoomInfo currentRoomInfo;
    private long myUid;
    private CircleImageView roomOwnerAvatar;
    private TextView roomOwnerNick;
    private TextView roomNumber;
    private UserInfo roomOwner;
    private TextView attentionView;
    private ImageView roomMore;
    private MessageView messageView;
    private BottomView bottomView;
    private UserListView userListView;
    private AuctionView auctionView;
    private GiftView giftView;
    private RelativeLayout inputLayout;
    private EditText inputEdit;
    private ImageView inputSend;
    private ImageView auctionListBtn;
    private MusicPlayerView musicPlayerView;
    private View praiseClickView;
    private HiPraiseAnimationView praiseAnimationView;
    private ImageView notice;
    private ImageView activityImg;

    private SparseArray<SoftReference<Bitmap>> mBitmapCacheArray = new SparseArray<>();
    private static final int[] HEARTS = new int[]{
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
    private Disposable subscribe;
    private List<ActionDialogInfo> actionDialogInfos;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_av_room_auction;
    }

    @Override
    public void onFindViews() {
        roomOwnerAvatar = mView.findViewById(R.id.room_owner_avatar);
        roomOwnerNick = mView.findViewById(R.id.room_owner_nick);
        roomNumber = mView.findViewById(R.id.room_owner_number);
        attentionView = mView.findViewById(R.id.attention_view);
        messageView = mView.findViewById(R.id.message_view);
        bottomView = mView.findViewById(R.id.bottom_view);
        auctionView = mView.findViewById(R.id.auction_view);
        userListView = mView.findViewById(R.id.user_list_view);
        inputLayout = mView.findViewById(R.id.input_layout);
        inputEdit = mView.findViewById(R.id.input_edit);
        inputSend = mView.findViewById(R.id.input_send);
        musicPlayerView = mView.findViewById(R.id.music_player_view);
        auctionListBtn = mView.findViewById(R.id.auction_list_btn);
        giftView = mView.findViewById(R.id.gift_view);
        praiseClickView = mView.findViewById(R.id.praise_click_view);
        praiseAnimationView = mView.findViewById(R.id.praise_animation_view);
        roomMore = mView.findViewById(R.id.room_more);
        notice = mView.findViewById(R.id.auction_notice);
        activityImg = mView.findViewById(R.id.activity_img);
    }

    private Handler handler = new Handler();

    /**
     * 动画特效
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            addPraiseWithCallback();

            Random rand = new Random();
            int i = rand.nextInt(3);
            //设置延迟时间，此处是5秒
            handler.postDelayed(this, i * 1000);
        }
    };

    @Override
    public void initiate() {
        currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();

        subscribe = IMNetEaseManager.get().getChatRoomEventObservable().subscribe(new Consumer<RoomEvent>() {
            @Override
            public void accept(RoomEvent roomEvent) throws Exception {
                if (roomEvent == null || roomEvent.getEvent() == RoomEvent.NONE) return;
                onReceiveRoomEvent(roomEvent);
            }
        });

        setOnlineNumber();
        long roomUid = AvRoomDataManager.get().mCurrentRoomInfo.getUid();
        getMvpPresenter().requestAuctionInfo(roomUid).subscribe();

        updateView();
        updateMicBtn();

        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if (currentRoomInfo != null) {
            if (uid == currentRoomInfo.getUid()) {
                attentionView.setVisibility(View.GONE);
                // 房主默认队列-1的位置
                getMvpPresenter().upMic(-1, String.valueOf(uid), false);
            } else {
                attentionView.setVisibility(View.VISIBLE);
                getMvpPresenter().isFollowed(myUid, currentRoomInfo.getUid()).subscribe(new BiConsumer<ServiceResult<Boolean>, Throwable>() {
                    @Override
                    public void accept(ServiceResult<Boolean> result, Throwable throwable) throws Exception {
                        if (result != null && result.getData() != null && result.getData()) {
                            attentionView.setVisibility(View.GONE);
                        } else {
                            attentionView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }

        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        roomOwnerAvatar.startAnimation(operatingAnim);

        handler.postDelayed(task, 3000);
    }

    public void setOnlineNumber() {
        roomNumber.setText(getString(R.string.listening_number,
                AvRoomDataManager.get().mCurrentRoomInfo.getOnlineNum()));
    }

    private void onReceiveRoomEvent(@NonNull final RoomEvent roomEvent) {
        int event = roomEvent.getEvent();
        switch (event) {
            case RoomEvent.ROOM_MEMBER_IN:
                int onlineNum = AvRoomDataManager.get().mCurrentRoomInfo.getOnlineNum() + 1;
                roomNumber.setText(String.valueOf("收听：" + onlineNum));
                break;
            case RoomEvent.ROOM_MEMBER_EXIT:
                onlineNum = AvRoomDataManager.get().mCurrentRoomInfo.getOnlineNum() - 1;
                roomNumber.setText(String.valueOf("收听：" + onlineNum));
                break;
            case RoomEvent.INVITE_UP_MIC:
//                getDialogManager().showOkCancelDialog("房主邀请您上麦，是否上麦？", true,
//                        new DialogManager.AbsOkDialogListener() {
//                            @Override
//                            public void onOk() {
//                                long roomId = AvRoomDataManager.get().mCurrentRoomInfo.getRoomId();
//                                getMvpPresenter().inviteUpMic(roomId, roomEvent.getMicPosition());
//                            }
//                        });
                break;
            case RoomEvent.UP_MIC:
            case RoomEvent.DOWN_MIC:
                updateMicBtn();
                updateView();
                break;
            case RoomEvent.KICK_DOWN_MIC:
                updateMicBtn();
                toast(R.string.kick_mic);
                break;
            case RoomEvent.FOLLOW:
                onFollow(roomEvent.isSuccess());
                break;
            case RoomEvent.UNFOLLOW:
                onUnFollow(roomEvent.isSuccess());
                break;
            case RoomEvent.KICK_OUT_ROOM:
//                ChatRoomKickOutEvent reason = roomEvent.getReason();
                if (roomEvent.getReason_no() == 3) {
                    //清除资源
                    releaseView();
                }
                break;
            case RoomEvent.RECHARGE:
                getDialogManager().showOkCancelDialog("余额不足，是否充值", true,
                        new DialogManager.AbsOkDialogListener() {
                            @Override
                            public void onOk() {
                                WalletActivity.start(mContext);
                            }
                        });
                break;
            default:
        }
    }

    @Override
    public void onSetListener() {
        activityImg.setOnClickListener(this);
        bottomView.setBottomViewListener(new AuctionRoomBottomViewWrapper());
        inputSend.setOnClickListener(this);
        roomMore.setOnClickListener(this);
        userListView.setListViewItemClickListener(this);
        praiseClickView.setOnClickListener(this);
        praiseAnimationView.setOnClickListener(this);
        attentionView.setOnClickListener(this);
        roomOwnerAvatar.setOnClickListener(this);
        auctionListBtn.setOnClickListener(this);
        notice.setOnClickListener(this);
        softKeyboardListener();
        inputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLayout.setVisibility(View.GONE);
                inputEdit.clearFocus();
                hideKeyboard();
            }
        });
    }

    public void showActivity(List<ActionDialogInfo> actionDialogInfos) {
        this.actionDialogInfos = actionDialogInfos;
        if (actionDialogInfos != null && actionDialogInfos.size() > 0) {
            activityImg.setVisibility(View.VISIBLE);
            ActionDialogInfo actionDialogInfo = actionDialogInfos.get(0);
            ImageLoadUtils.loadImage(getContext(), actionDialogInfo.getAlertWinPic(), activityImg);
        } else {
            activityImg.setVisibility(View.GONE);
        }
    }

    public void releaseView() {
        messageView.release();
        giftView.release();
        auctionView.release();
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
        if (currentRoomInfo != null) {
            roomOwner = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(currentRoomInfo.getUid());
            if (roomOwner != null) {
                ImageLoadUtils.loadAvatar(getContext(), roomOwner.getAvatar(), roomOwnerAvatar);
                roomOwnerNick.setText(roomOwner.getNick());
                if (!StringUtils.isBlank(currentRoomInfo.getBackPic())) {
                    musicPlayerView.setImageBg(currentRoomInfo.getBackPic());
                } else {
                    musicPlayerView.setImageBg(roomOwner.getAvatar());
                }
            }

            if (AvRoomDataManager.get().isRoomOwner()) {
                musicPlayerView.setVisibility(View.VISIBLE);
                bottomView.showLightChatUpMicBottom();
            } else {
                if (!AvRoomDataManager.get().isOnMic(myUid)) {
                    musicPlayerView.setVisibility(View.GONE);
                    bottomView.showLightChatDownMicBottom();
                } else {
                    musicPlayerView.setVisibility(View.VISIBLE);
                    bottomView.showLightChatUpMicBottom();
                }
            }
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
            bottomView.setRemoteMuteOpen(!RtcEngineManager.get().isRemoteMute());
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
        }
        releaseView();
    }

    @Override
    public void onFollow(boolean success) {
        if (success) attentionView.setVisibility(View.GONE);
        toast(success ? "关注成功，相互关注可成为好友哦！" : "关注失败");
    }

    @Override
    public void onUnFollow(boolean success) {
        if (success) attentionView.setVisibility(View.VISIBLE);
        toast(success ? "取消关注成功!" : "取消关注失败");
    }

    private class AuctionRoomBottomViewWrapper extends BottomViewListenerWrapper {
        @Override
        public void onOpenMicBtnClick() {
            if (!RtcEngineManager.get().isAudienceRole()) {
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

        @Override
        public void onSendGiftBtnClick() {
            if (roomOwner != null) {
                GiftDialog dialog = new GiftDialog(getContext(), roomOwner.getUid(), roomOwner.getNick(), roomOwner.getAvatar());
                dialog.setGiftDialogBtnClickListener(AuctionRoomFragment.this);
                dialog.show();
            }
        }

        @Override
        public void onShareBtnClick() {
            ShareDialog shareDialog = new ShareDialog(getActivity());
            shareDialog.setOnShareDialogItemClick(AuctionRoomFragment.this);
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
                if (TextUtils.isEmpty(inputEdit.getText().toString().trim())) {
                    SingleToastUtil.showToast("输入框不能为空");
                    return;
                }
                IMNetEaseManager.get().sendTextMsg(currentRoomInfo.getRoomId(), inputEdit.getText().toString().trim(), new IMProCallBack() {
                    @Override
                    public void onSuccessPro(IMReportBean imReportBean) {

                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }
                });
                inputEdit.setText("");
                break;
            case R.id.praise_click_view:
            case R.id.praise_animation_view:
                addPraiseWithCallback();
                break;
            case R.id.room_more:
                showMoreItems();
                break;
            case R.id.attention_view:
                if (currentRoomInfo != null) {
                    getMvpPresenter().follow(currentRoomInfo.getUid(), true);
                }
                break;
            case R.id.room_owner_avatar:
                boolean isRoomOwner = AvRoomDataManager.get().isRoomOwner();
                boolean isRoomAdmin = AvRoomDataManager.get().isRoomAdmin();
                boolean isRoomGuess = AvRoomDataManager.get().isGuess();
                if (isRoomOwner || isRoomAdmin) {
                    if (AuctionModel.get().isInAuctionNow()) {
                        showUserDialog(roomOwner.getUid());
                    } else {
                        final AuctionInfo auctionInfo = getMvpPresenter().getAuctionInfo();
                        if (auctionInfo == null || auctionInfo.getAuctId() == null) {
                            ButtonItem buttonItem = new ButtonItem("发起竞拍", new ButtonItem.OnClickListener() {
                                @Override
                                public void onClick() {
                                    if (auctionInfo == null || auctionInfo.getAuctId() == null) {
                                        AuctionDialog dialog = new AuctionDialog(getActivity(), roomOwner.getUid());
                                        dialog.setOnClickItemListener(new AuctionDialog.OnClickItemListener() {
                                            @Override
                                            public void onClickHead() {
                                                UIHelper.showUserInfoAct(getContext(), roomOwner.getUid());
                                            }

                                            @Override
                                            public void onClickBegin(int price) {
                                                getMvpPresenter().startAuction(roomOwner.getUid(), roomOwner.getUid(),
                                                        price, 30, 10, "暂无竞拍描述");
                                            }
                                        });
                                        dialog.show();
                                    } else {
                                        toast("正在发起竞拍");
                                    }
                                }
                            });
                            List<ButtonItem> buttonItems = new ArrayList<>();
                            ButtonItem buttonItem5 = ButtonItemFactory.createCheckUserInfoDialogItem(getContext(), String.valueOf(myUid));
                            buttonItems.add(buttonItem5);
                            buttonItems.add(buttonItem);
                            getDialogManager().showCommonPopupDialog(buttonItems, "取消");

                        }
                    }
                } else if (isRoomGuess) {
                    if (roomOwner != null)
                        showUserDialog(roomOwner.getUid());
                }
                break;
            case R.id.auction_list_btn:
                Intent intent = new Intent(mContext, LightChatOnlineActivity.class);
                mContext.startActivity(intent);
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
                List<ActionDialogInfo> dialogInfos = actionDialogInfos;
                if (dialogInfos != null && dialogInfos.size() > 0) {
                    ActionDialogInfo dialogInfo = dialogInfos.get(0);
                    CommonWebViewActivity.start(getContext(), dialogInfo.getSkipUrl());
                }
                break;
            default:
        }
    }

    private void showUserDialog(long uid) {
        UserInfoDialog.showUserDialog(this.getContext(), uid);
    }

    private void showMoreItems() {
        List<ButtonItem> buttonItems = new ArrayList<>();
        ButtonItem buttonItem1 = new ButtonItem("最小化", () -> {
            AvRoomDataManager.get().setMinimize(true);
            getActivity().finish();
        });
        ButtonItem buttonItem2 = new ButtonItem("退出房间", () -> ((AVRoomActivity) getActivity()).toBack());
        ButtonItem buttonItem3 = new ButtonItem("管理员", () -> RoomManagerListActivity.start(getContext()));
        ButtonItem buttonItem4 = new ButtonItem("黑名单", () -> RoomBlackListActivity.start(getContext()));

        if (AvRoomDataManager.get().isRoomOwner()) {
            buttonItems.add(buttonItem3);
            buttonItems.add(buttonItem4);
        }

        buttonItems.add(buttonItem1);
        buttonItems.add(buttonItem2);

        if (AvRoomDataManager.get().isRoomAdmin()) {
            buttonItems.add(buttonItem4);
        }

        getDialogManager().showCommonPopupDialog(buttonItems, "取消");
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
                if (imm != null)
                    imm.showSoftInput(inputEdit, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘并显示头布局
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(inputEdit.getWindowToken(), 0);
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListener() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                inputLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClicked(IMChatRoomMember chatRoomMember) {
        onClickHeadToDialog(chatRoomMember);
    }

    private void onClickHeadToDialog(final IMChatRoomMember chatRoomMember) {
        boolean mySelf = (CoreManager.getCore(IAuthCore.class).getCurrentUid() == JavaUtil.str2long(chatRoomMember.getAccount()));
        boolean isTargetAdmin = AvRoomDataManager.get().isRoomAdmin(chatRoomMember.getAccount());
        boolean isTargetGuess = AvRoomDataManager.get().isGuess(chatRoomMember.getAccount());
        final int micPosition = AvRoomDataManager.get().getMicPosition(chatRoomMember.getAccount());
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(micPosition);
        if (roomQueueInfo == null || roomInfo == null || micPosition == Integer.MIN_VALUE) return;
        List<ButtonItem> buttonItems = new ArrayList<>();
        ButtonItem buttonItem1 = new ButtonItem("发起竞拍", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                if (AuctionModel.get().isInAuctionNow()) {
                    SingleToastUtil.showToast(null, "正在竞拍,请先结束竞拍!");
                    return;
                }
                AuctionDialog dialog = new AuctionDialog(getActivity(), JavaUtil.str2long(chatRoomMember.getAccount()));
                dialog.setOnClickItemListener(new AuctionDialog.OnClickItemListener() {
                    @Override
                    public void onClickHead() {
                        UIHelper.showUserInfoAct(getContext(), JavaUtil.str2long(chatRoomMember.getAccount()));
                    }

                    @Override
                    public void onClickBegin(int price) {
                        getMvpPresenter().startAuction(roomInfo.getUid(), JavaUtil.str2long(chatRoomMember.getAccount()),
                                price, 30, 10, "暂无竞拍描述");
                    }
                });
                dialog.show();
            }
        });
        ButtonItem buttonItem2 = ButtonItemFactory.createKickDownMicItem(chatRoomMember.getNick(), chatRoomMember.getAccount());
        ButtonItem buttonItem4 = ButtonItemFactory.createKickOutRoomItem(String.valueOf(roomInfo.getRoomId()), mContext, chatRoomMember.getAccount(), chatRoomMember.getNick());
        ButtonItem buttonItem5 = ButtonItemFactory.createCheckUserInfoDialogItem(getContext(), chatRoomMember.getAccount());
        ButtonItem buttonItem6 = ButtonItemFactory.createDownMicItem();
        ButtonItem buttonItem7 = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(),String.valueOf(roomInfo.getRoomId()), chatRoomMember.getAccount(), true);
        ButtonItem buttonItem8 = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(),String.valueOf(roomInfo.getRoomId()), chatRoomMember.getAccount(), false);
        ButtonItem buttonItem9 = ButtonItemFactory.createMarkBlackListItem(String.valueOf(roomInfo.getRoomId()), getContext(), chatRoomMember.getAccount(), chatRoomMember.getNick());
        if (AvRoomDataManager.get().isRoomOwner()) {
            // 查看资料
            buttonItems.add(buttonItem5);
            // 发起竞拍
            if (!getMvpPresenter().isInAuctionNow()) {
                buttonItems.add(buttonItem1);
            }
            // 抱Ta下麦
            buttonItems.add(buttonItem2);
            // 踢Ta出房间
            buttonItems.add(buttonItem4);
            if (isTargetAdmin) {
                // 移除管理员
                buttonItems.add(buttonItem8);
            } else if (isTargetGuess) {
                // 设置管理员
                buttonItems.add(buttonItem7);
            }
            // 设置黑名单
            buttonItems.add(buttonItem9);
            getBaseActivity().getDialogManager().showCommonPopupDialog(buttonItems, "取消");
        } else if (AvRoomDataManager.get().isRoomAdmin()) {
            if (mySelf) {
                // 查看资料
                buttonItems.add(buttonItem5);
                // 发起竞拍
                if (!getMvpPresenter().isInAuctionNow())
                    buttonItems.add(buttonItem1);
                // 下麦旁听
                buttonItems.add(buttonItem6);
                getBaseActivity().getDialogManager().showCommonPopupDialog(buttonItems, "取消");
                return;
            } else if (isTargetAdmin) {
//                if (!getMvpPresenter().isInAuctionNow()) {
//                    // 发起竞拍
//                    buttonItems.add(buttonItem1);
//                    // 查看资料
//                    buttonItems.add(buttonItem5);
//                } else {
                    // 显示资料框
                    showUserDialog(Long.parseLong(chatRoomMember.getAccount()));
                    return;
//                }
            } else if (isTargetGuess) {
                // 查看资料
                buttonItems.add(buttonItem5);
                if (!getMvpPresenter().isInAuctionNow())
                    // 发起竞拍
                    buttonItems.add(buttonItem1);
                // 抱Ta下麦
                buttonItems.add(buttonItem2);
                // 踢Ta出房间
                buttonItems.add(buttonItem4);
                // 加入黑名单
                buttonItems.add(buttonItem9);
            }
            getBaseActivity().getDialogManager().showCommonPopupDialog(buttonItems, "取消");
        } else {
            if (mySelf) {
                // 查看资料
                buttonItems.add(buttonItem5);
                // 下麦旁听
                buttonItems.add(buttonItem6);
                getBaseActivity().getDialogManager().showCommonPopupDialog(buttonItems, "取消");
            } else {
                showUserDialog(Long.parseLong(chatRoomMember.getAccount()));
            }
        }

    }

    @Override
    public void onRechargeBtnClick() {
        WalletActivity.start(getContext());
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP) {
        CoreManager.getCore(IGiftCore.class).sendRoomGift(giftInfo.getGiftId(), uid, currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP) {

    }

    @Override
    public void onSharePlatformClick(Platform platform) {
        CoreManager.getCore(IShareCore.class).shareRoom(platform, roomOwner.getUid(), currentRoomInfo.getTitle());
    }

    //------------------------------IShareCoreClient----------------------------------
    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoom() {
        toast("分享成功");
    }

    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoomFail() {
        toast("分享失败，请重试");
    }

    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoomCancel() {
        getDialogManager().dismissDialog();
    }
}
