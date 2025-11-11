package com.vslk.lbgx.room.avroom.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.PkCustomAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.pk.IPKCoreClient;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

/**
 * PK 互动view
 * 存在全屏和最小化两种状态
 *
 * @author zwk 2018/7/3
 */
public class HomePartyPKView extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {
    private Context mContext;
    private RelativeLayout rlMinimize;//最小化布局
    private RelativeLayout rlFull;//大屏布局
    private RelativeLayout llWin;//赢
    private RelativeLayout llPing;//平
    private ImageView ivWin;//胜利的用户logo
    private ImageView ivMinimize;//最小化按钮
    private SeekBar skbFull;//最大化和最小化进度, skbMin
    //大屏窗口用户logo和平局logo遮罩层和昵称
    private ImageView ivLeft, ivRight;//, ivLfetP, ivRightP
    private TextView tvLeftNick, tvRightNick, tvLeftCount, tvRightCount;
    private TextView tvCountDown, tvMinCount;//倒计时
    //小窗口用户昵称
    private TextView tvMinLeftNick, tvMinRightNick, tvMinLeftCount, tvMinRightCount;
    //pk类型
    private TextView tvPkType;//1人数 2礼物
    private PkVoteInfo pkVoteInfo;
    public boolean isFull = false;
    private boolean isShowing = false;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomePartyPKView(Context context) {
        this(context, null);
    }

    public HomePartyPKView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomePartyPKView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context);
        initListener();
    }

    private void initView(Context context) {
        inflate(context, R.layout.layout_home_party_pk, this);
        mWidthPixels = ScreenUtil.getScreenWidth(context);
        mHeightPixels = ScreenUtil.getScreenHeight(context);
        rlFull = findViewById(R.id.rl_pk_full_screen);
        llWin = findViewById(R.id.rl_pk_win_remark);
        llPing = findViewById(R.id.rl_pk_ping_remark);
        ivWin = findViewById(R.id.iv_win_avatar);
        rlMinimize = findViewById(R.id.rl_pk_minimize);
        ivMinimize = findViewById(R.id.iv_minimize);
        skbFull = findViewById(R.id.skb_pk_full_progress);
        ivLeft = findViewById(R.id.iv_left_pk);
        ivRight = findViewById(R.id.iv_right_pk);
        tvLeftNick = findViewById(R.id.tv_left_nick);
        tvRightNick = findViewById(R.id.tv_right_nick);
        tvLeftCount = findViewById(R.id.tv_pk_left_vote);
        tvRightCount = findViewById(R.id.tv_pk_right_vote);
        tvPkType = findViewById(R.id.tv_pk_type);
        tvCountDown = findViewById(R.id.tv_pk_countdown);
        tvMinCount = findViewById(R.id.tv_pk_min_countdown);
        tvMinLeftNick = findViewById(R.id.tv_minimize_left_nick);
        tvMinRightNick = findViewById(R.id.tv_minimize_right_nick);
        tvMinLeftCount = findViewById(R.id.tv_minimize_left_count);
        tvMinRightCount = findViewById(R.id.tv_minimize_right_count);
//        skbMin.setEnabled(false);
        skbFull.setEnabled(false);
        if (isFull) {
            if (rlMinimize.getVisibility() == View.VISIBLE)
                rlMinimize.setVisibility(View.GONE);
            if (rlFull.getVisibility() == View.GONE)
                rlFull.setVisibility(View.VISIBLE);
        }
        initData(false);
    }

    /**
     * 通过接口获取最新信息
     *
     * @param isVote true 投票操作  false初始化获取pk数据
     */
    private void initData(boolean isVote) {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            if (duration == 0)
                resetState();
            return;
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("roomId", (AvRoomDataManager.get().mCurrentRoomInfo == null ? 0 : AvRoomDataManager.get().mCurrentRoomInfo.getRoomId()) + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getPkResult(), params, new OkHttpManager.MyCallBack<ServiceResult<PkVoteInfo>>() {
            @Override
            public void onError(Exception e) {
                if (duration == 0)
                    resetState();
            }

            @Override
            public void onResponse(ServiceResult<PkVoteInfo> response) {
                if (isVote) {
                    if (response.getData() == null) return;
                    if (response.getData().getDuration() > 0) {
                        countDown(response.getData().getDuration());
                        setPkInfo(response.getData());
                    } else {
                        //屏蔽因为延迟导致的重复执行显示隐藏问题
                        if (!isShowing && pkVoteInfo != null && (rlFull.getVisibility() == View.VISIBLE || rlMinimize.getVisibility() == View.VISIBLE))
                            dealWithPKEnd(response.getData());
                    }
                } else {
                    if (response != null && response.isSuccess()) {
                        if (response.getData() != null && response.getData().getDuration() > 0) {
                            setPkInfo(response.getData());
                            countDown(response.getData().getDuration());
                        } else {
                            resetState();
                        }
                    } else {
                        resetState();
                    }
                }
            }
        });
    }

    private void initListener() {
        rlMinimize.setOnClickListener(this);
        ivMinimize.setOnClickListener(this);
        rlMinimize.setOnTouchListener(this);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_minimize://由全屏到最小化
                if (pkVoteInfo == null) {
                    resetState();
                    return;
                }
                isFull = false;
                if (rlFull.getVisibility() == View.VISIBLE)
                    rlFull.setVisibility(View.GONE);
                if (rlMinimize.getVisibility() == View.GONE)
                    rlMinimize.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_left_pk:
                if (pkVoteInfo == null)
                    return;
                if (pkVoteInfo.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid())
                    return;
                if (pkVoteInfo.getPkType() == 1) {
                    dealPkDialogShow(true, pkVoteInfo.getUid() + "", pkVoteInfo.getNick(), pkVoteInfo.getAvatar());
                } else {
                    showGiftDialog(true);
                }
                break;
            case R.id.iv_right_pk:
                if (pkVoteInfo == null)
                    return;
                if (pkVoteInfo.getPkUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid())
                    return;
                if (pkVoteInfo.getPkType() == 1) {
                    dealPkDialogShow(false, pkVoteInfo.getPkUid() + "", pkVoteInfo.getNick(), pkVoteInfo.getAvatar());
                } else {
                    showGiftDialog(false);
                }
                break;
        }
    }

    //倒计时
    Handler handler = new Handler();
    private int duration = 0;//真实的时间 -- 用于显示
    private int countDuration = 0;//用于倒计时 -- 如果延迟10秒依然出现问题则隐藏显示
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                //倒计时剩余3和7秒分别请求
                if (countDuration == 3 || countDuration == 7)
                    initData(true);
                if (countDuration == 0) {
                    handler.removeCallbacks(runnable);
                    resetState();
                    return;
                }
                if (duration > 0) {
                    duration--;
                }
                countDuration--;
                tvCountDown.setText(duration + "S");
                tvMinCount.setText(duration + "S");
                handler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 倒计时 + 延时10秒
     *
     * @param count
     */
    private void countDown(int count) {
        if (count <= 0) {
            handlerRelease();
            return;
        }
        countDuration = count + 10;
        duration = count;
        tvCountDown.setText(duration + "S");
        tvMinCount.setText(duration + "S");
        if (handler != null) {
            try {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放handler
     */
    private void handlerRelease() {
        if (handler != null && runnable != null)
            try {
                handler.removeCallbacks(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * 人数投票按钮
     *
     * @param left
     * @param uid
     */
    private void dealPkDialogShow(boolean left, String uid, String nick, String avatar) {
        //公屏点击弹框
        final List<ButtonItem> buttonItems = new ArrayList<>();
        ButtonItem msgBlackListItem = ButtonItemFactory.createMsgBlackListItem("投票给Ta", new ButtonItemFactory.OnItemClick() {
            @Override
            public void itemClick() {
                Map<String, String> params = CommonParamUtil.getDefaultParam();
                params.put("roomId", (AvRoomDataManager.get().mCurrentRoomInfo == null ? 0 : AvRoomDataManager.get().mCurrentRoomInfo.getRoomId()) + "");
                params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
                params.put("voteUid", uid + "");
                OkHttpManager.getInstance().doPostRequest(UriProvider.sendPkVote(), params, new OkHttpManager.MyCallBack<ServiceResult<PkVoteInfo>>() {
                    @Override
                    public void onError(Exception e) {
                        SingleToastUtil.showToast("投票失败！");
                    }

                    @Override
                    public void onResponse(ServiceResult<PkVoteInfo> response) {
                        if (response != null && response.getCode() == 200) {
                            IMNetEaseManager.get().sendPkNotificationBySdk(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_FIRST, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_ADD, new PkVoteInfo());
                        } else {
                            SingleToastUtil.showToast(response.getMessage());
                        }
                    }
                });
            }
        });
        buttonItems.add(msgBlackListItem);
        buttonItems.add(ButtonItemFactory.createSendGiftItem(mContext, uid, nick, avatar));
        buttonItems.add(ButtonItemFactory.createCheckUserInfoDialogItem(mContext, uid));
        ((BaseMvpActivity) mContext).getDialogManager().showCommonPopupDialog(buttonItems, "取消");
    }

    /**
     * 礼物赠送投票
     *
     * @param left
     */
    private void showGiftDialog(boolean left) {
        GiftDialog giftDialog = new GiftDialog(getContext(), left ? pkVoteInfo.getUid() : pkVoteInfo.getPkUid(), left ? pkVoteInfo.getNick() : pkVoteInfo.getPkNick(), left ? pkVoteInfo.getAvatar() : pkVoteInfo.getPkAvatar());
        giftDialog.setGiftDialogBtnClickListener(new GiftDialog.OnGiftDialogBtnClickListener() {
            @Override
            public void onRechargeBtnClick() {
                WalletActivity.start(mContext);
            }

            @Override
            public void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP) {
                RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (currentRoomInfo == null) return;
                CoreManager.getCore(IGiftCore.class).sendRoomGift(giftInfo.getGiftId(), uid, currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
            }

            @Override
            public void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP) {

            }
        });
        giftDialog.show();
    }

    /**
     * 礼物赠送成功回调 -- 送给单人
     *
     * @param target
     */
    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onPkGift(long target) {
        if (pkVoteInfo != null && !isShowing) {//如果正在执行结束动画将不执行礼物消息发送
            if (pkVoteInfo.getUid() == target || pkVoteInfo.getPkUid() == target)
                IMNetEaseManager.get().sendPkNotificationBySdk(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_FIRST, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_ADD, new PkVoteInfo());
        }
    }

    /**
     * 礼物赠送成功回调 -- 送给全麦
     *
     * @param targetUids
     */
    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onPkMultiGift(List<Long> targetUids) {
        if (pkVoteInfo != null && !isShowing) {
            IMNetEaseManager.get().sendPkNotificationBySdk(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_FIRST, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_ADD, new PkVoteInfo());
        }
    }

    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onPkGiftFail(String error) {
        SingleToastUtil.showToast(error);
    }

    /**
     * 控制不同消息显示
     *
     * @param chatRoomMessage
     */
    private void dealWithEvent(ChatRoomMessage chatRoomMessage) {
        if (chatRoomMessage.getAttachment() instanceof PkCustomAttachment) {
            PkCustomAttachment pk = (PkCustomAttachment) chatRoomMessage.getAttachment();
            if (pk == null)
                return;
            if (pk.getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_START) {
                //操作者是自己
                if (pk.getPkVoteInfo() != null && AvRoomDataManager.get().isOwner(pk.getPkVoteInfo().getOpUid()))
                    isFull = true;
                setPkInfo(pk.getPkVoteInfo());
                initData(true);
            } else if (pk.getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_ADD) {
                initData(true);
            } else if (pk.getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_END) {
                if (!isShowing && pkVoteInfo != null)//屏蔽因为延迟导致的重复执行显示隐藏问题
                    dealWithPKEnd(pk.getPkVoteInfo());
            } else if (pk.getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_CANCEL) {
                resetState();
            }
        }
    }

    /**
     * 处理PK结果
     *
     * @param info
     */
    private void dealWithPKEnd(PkVoteInfo info) {
        if (info != null) {
            handlerRelease();
            setPkInfo(info);
            //结束后都显示全屏
            rlFull.setVisibility(View.VISIBLE);
            rlMinimize.setVisibility(View.GONE);
            if (info.getVoteCount() == info.getPkVoteCount()) {//平
                llWin.setVisibility(View.GONE);
                llPing.setVisibility(View.VISIBLE);
//                ivLfetP.setVisibility(View.VISIBLE);
//                ivRightP.setVisibility(View.VISIBLE);
                showPKWin();
            } else if (info.getVoteCount() > info.getPkVoteCount()) {//左赢
                llWin.setVisibility(View.VISIBLE);
                llPing.setVisibility(View.GONE);
//                ivLfetP.setVisibility(View.GONE);
//                ivRightP.setVisibility(View.GONE);
                ImageLoadUtils.loadCircleImage(getContext().getApplicationContext(), info.getAvatar(), ivWin, R.drawable.ic_default_avatar);
                showPKWin();
            } else {
                llWin.setVisibility(View.VISIBLE);
                llPing.setVisibility(View.GONE);
//                ivLfetP.setVisibility(View.GONE);
//                ivRightP.setVisibility(View.GONE);
                ImageLoadUtils.loadCircleImage(getContext().getApplicationContext(), info.getPkAvatar(), ivWin, R.drawable.ic_default_avatar);
                showPKWin();
            }
        } else {
            if (duration == 0) {//数据异常倒计时结束隐藏
                resetState();
            }
        }
    }

    /**
     * 显示大窗口并且执行消失动画
     */
    private void showPKWin() {
        isShowing = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScaleAnimation disappear = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, rlFull.getWidth() / 2, rlFull.getHeight() / 2);
                disappear.setDuration(500);
                disappear.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        resetState();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                rlFull.startAnimation(disappear);
//                resetState();
                handler.removeCallbacks(this);
            }
        }, 3000);
    }

    /***
     * 重置view的初始状态
     */
    private void resetState() {
        //释放定时器
        handlerRelease();
        //隐藏布局
        rlFull.setVisibility(View.GONE);
        rlMinimize.setVisibility(View.GONE);
        llWin.setVisibility(View.GONE);
//        ivLfetP.setVisibility(View.GONE);
//        ivRightP.setVisibility(View.GONE);
        llPing.setVisibility(View.GONE);
        //重置默认状态
        pkVoteInfo = null;
        isFull = false;
        duration = 0;
        countDuration = 0;
        isShowing = false;
    }

    /**
     * seekbar进度显示
     *
     * @param voteCount
     * @param pkVote
     * @return
     */
    private int getProgress(int voteCount, int pkVote) {
        if (voteCount == pkVote) {
            return 50;
        }
        return voteCount * 100 / (voteCount + pkVote == 0 ? 1 : voteCount + pkVote);
    }

    public void setPkInfo(PkVoteInfo info) {
        if (info == null)
            return;
        pkVoteInfo = info;
        tvLeftNick.setText(info.getNick() + "");
        tvLeftCount.setText(info.getVoteCount() + "");
        tvRightNick.setText(info.getPkNick() + "");
        tvRightCount.setText(info.getPkVoteCount() + "");
        ImageLoadUtils.loadCircleImage(getContext().getApplicationContext(), info.getAvatar(), ivLeft, R.drawable.ic_default_avatar);
        ImageLoadUtils.loadCircleImage(getContext().getApplicationContext(), info.getPkAvatar(), ivRight, R.drawable.ic_default_avatar);
        tvMinLeftNick.setText(info.getNick() + "");
        tvMinLeftCount.setText(info.getVoteCount() + "");
        tvMinRightNick.setText(info.getPkNick() + "");
        tvMinRightCount.setText(info.getPkVoteCount() + "");
//        skbMin.setProgress(getProgress(info.getVoteCount(), info.getPkVoteCount()));
        skbFull.setProgress(getProgress(info.getVoteCount(), info.getPkVoteCount()));
        if (info.getPkType() == 1) {
            tvPkType.setText("本轮按照人数投票");
        } else {
            tvPkType.setText("本轮按照礼物价值投票");
        }
        if (isFull) {
            rlFull.setVisibility(View.VISIBLE);
            rlMinimize.setVisibility(View.GONE);
        } else {
            rlFull.setVisibility(View.GONE);
            rlMinimize.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        CoreManager.addClient(this);
        if (compositeDisposable == null)
            compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(IMNetEaseManager.get().getChatRoomMsgFlowable()
                .subscribe(messages -> {
                    if (messages.size() == 0) return;
                    for (ChatRoomMessage msg : messages) {
                        if (msg.getAttachment() instanceof IMCustomAttachment) {
                            dealWithEvent(msg);
                        }
                    }
                }));
        compositeDisposable.add(IMNetEaseManager.get().getChatRoomEventObservable()
                .subscribe(roomEvent -> {
                    if (roomEvent == null ||
                            roomEvent.getEvent() != RoomEvent.RECEIVE_MSG) return;
                    ChatRoomMessage chatRoomMessage = roomEvent.getChatRoomMessage();
                    if (chatRoomMessage.getAttachment() instanceof IMCustomAttachment) {
                        dealWithEvent(chatRoomMessage);
                    }
                }));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
        handlerRelease();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public PkVoteInfo getPkVoteInfo() {
        return pkVoteInfo;
    }

    /**
     * 控制view的位置
     */
    private int mWidthPixels;
    private int mHeightPixels;
    long mDownTimeMillis = 0;
    int xDelta = 0;
    int yDelta;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
//        Log.d(TAG, "onTouch: x= " + x + "y=" + y);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownTimeMillis = System.currentTimeMillis();
                LayoutParams params = (LayoutParams) view
                        .getLayoutParams();
                xDelta = x - params.leftMargin;
                yDelta = y - params.topMargin;
//                Log.d(TAG, "ACTION_DOWN: xDelta= " + xDelta + "yDelta=" + yDelta);
                break;
            case MotionEvent.ACTION_MOVE:
                LayoutParams layoutParams = (LayoutParams) view
                        .getLayoutParams();
                int width = layoutParams.width;
                int height = layoutParams.height;
                int xDistance = x - xDelta;
                int yDistance = y - yDelta;

                int outX = (mWidthPixels - width) - 10;
                if (xDistance > outX) {
                    xDistance = outX;
                }

                int outY = mHeightPixels - height;
                if (yDistance > outY) {
                    yDistance = outY;
                }

                if (yDistance < 100) {
                    yDistance = 100;
                }
                if (xDistance < 10) {
                    xDistance = 10;
                }


                layoutParams.leftMargin = xDistance;
                layoutParams.topMargin = yDistance;
                view.setLayoutParams(layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - mDownTimeMillis < 150) {
                    if (pkVoteInfo != null) {
                        isFull = true;
                        if (rlMinimize.getVisibility() == View.VISIBLE)
                            rlMinimize.setVisibility(View.GONE);
                        if (rlFull.getVisibility() == View.GONE)
                            rlFull.setVisibility(View.VISIBLE);
                    } else {
                        resetState();
                    }
                }
                break;
        }
//        mViewGroup.invalidate();
        return true;
    }
}
