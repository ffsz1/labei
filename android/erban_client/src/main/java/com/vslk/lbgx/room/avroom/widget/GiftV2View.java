package com.vslk.lbgx.room.avroom.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.GiftReceiveInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.gift.IGiftCoreClient;
import com.tongdaxing.xchat_core.gift.MultiGiftReceiveInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.LogUtil;
import com.tongdaxing.xchat_framework.util.util.ResolutionUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 礼物特效布局
 *
 * @author xiaoyu
 * @date 2017/12/20
 */

public class GiftV2View extends RelativeLayout implements GiftEffectView.GiftEffectListener {
    public GiftEffectView giftEffectView;
    private int giftWidth;
    private int giftHeight;
    private List<GiftReceiveInfo> giftReceiveInfoList;
    private UiHandler handler;

    private Context context;
    private int mScreenWidth;
    private int mGiftSendY;
    private RelativeLayout flSend;


    public GiftV2View(Context context) {
        this(context, null);

    }

    public GiftV2View(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public GiftV2View(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        CoreManager.addClient(this);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_gift_v2_view, this, true);
        mScreenWidth = ResolutionUtils.getScreenWidth(getContext());
        mGiftSendY = UIUtil.dip2px(context, 90);
        giftWidth = UIUtil.dip2px(context, 80);
        giftHeight = UIUtil.dip2px(context, 80);

        giftReceiveInfoList = new ArrayList<>();
        giftEffectView = findViewById(R.id.gift_effect_view);
        flSend = findViewById(R.id.fl_gift_send);
        giftEffectView.setGiftEffectListener(this);
        handler = new UiHandler(this);
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onRecieveMultiGiftMsg(MultiGiftReceiveInfo multiGiftReceiveInfo) {
        if (multiGiftReceiveInfo != null) {
            List<Long> targetUids = multiGiftReceiveInfo.getTargetUids();
            List<GiftReceiveInfo> giftReceiveInfos = new ArrayList<>();
            for (int i = 0; i < targetUids.size(); i++) {
                Long targetUid = targetUids.get(i);
                GiftReceiveInfo giftReceiveInfo = new GiftReceiveInfo();
                giftReceiveInfo.setRoomUid(multiGiftReceiveInfo.getRoomUid());
                giftReceiveInfo.setUid(multiGiftReceiveInfo.getUid());
                giftReceiveInfo.setGiftNum(multiGiftReceiveInfo.getGiftNum());
                giftReceiveInfo.setTargetUid(targetUid);
                giftReceiveInfo.setNick(multiGiftReceiveInfo.getNick());
                giftReceiveInfo.setGiftId(multiGiftReceiveInfo.getGiftId());
                giftReceiveInfo.setAvatar(multiGiftReceiveInfo.getAvatar());
                giftReceiveInfos.add(giftReceiveInfo);
            }
            if (AvRoomDataManager.get().isMinimize() || !AvRoomDataManager.get().isStartPlayFull())
                return;
            drawAnimation(giftReceiveInfos);
        }
    }

    private void drawAnimation(List<GiftReceiveInfo> giftReceiveInfos) {
        if (giftReceiveInfos.size() > 0) {
            RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (roomInfo != null) {
                int totalCoin = 0;
                for (int i = 0; i < giftReceiveInfos.size(); i++) {
                    GiftReceiveInfo giftReceiveInfo = giftReceiveInfos.get(i);
                    SparseArray<Point> micViewPoint = AvRoomDataManager.get().mMicPointMap;
                    Log.d("GiftAnimation", "drawAnimation: get micPointMap=" + micViewPoint);
                    GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(giftReceiveInfo.getGiftId());
                    // 算出发送者和接受者的位置
                    int senderPosition = AvRoomDataManager.get().getMicPosition(giftReceiveInfo.getUid());
                    int receivePosition = AvRoomDataManager.get().getMicPosition(giftReceiveInfo.getTargetUid());


                    Log.d("GiftAnimation", "drawAnimation: receivePosition" + receivePosition);
                    Point senderPoint = micViewPoint.get(senderPosition);
                    Point receivePoint = micViewPoint.get(receivePosition);
//                    if (senderPoint == null && roomInfo.getUid() == giftReceiveInfo.getUid()) {
//                        senderPoint = new Point();
//                        senderPoint.x = mScreenWidth / 2 - giftWidth / 2;
//                        senderPoint.y = mGiftSendY;
//                    }
//
//                    if (receivePoint == null && roomInfo.getUid() == giftReceiveInfo.getTargetUid()) {
//                        receivePoint = new Point();
//                        receivePoint.x = mScreenWidth / 2 - giftWidth / 2;
//                        receivePoint.y = mGiftSendY;
//                    }
                    RoomInfo info = AvRoomDataManager.get().mCurrentRoomInfo;
                    if (info != null && totalCoin < 520 && info.getGiftEffectSwitch() == 1) {
                        //开启了屏蔽小礼物开关不显示小礼物动画
                    } else {
                        if (receivePoint != null) {
                            //送出礼物到麦上的动画
                            drawGiftView(senderPoint, receivePoint, giftInfo);
                        } else {//接收礼物不在麦位动画
                            receivePoint = new Point(UIUtil.dip2px(context, 50), UIUtil.dip2px(context, 20));
                            drawGiftView(senderPoint, receivePoint, giftInfo);
                        }
                    }
                    totalCoin += giftInfo.getGoldPrice() * giftReceiveInfo.getGiftNum();
                }

                if (totalCoin >= 520) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    GiftReceiveInfo giftReceiveInfo = giftReceiveInfos.get(0);
                    giftReceiveInfo.setPersonCount(giftReceiveInfos.size());
                    msg.obj = giftReceiveInfo;
                    handler.sendMessageDelayed(msg, 200);
                }
            }
        }
    }

    private static class UiHandler extends Handler {
        private WeakReference<GiftV2View> giftV2ViewWeakReference;

        public UiHandler(GiftV2View giftV2View) {
            this.giftV2ViewWeakReference = new WeakReference<>(giftV2View);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GiftV2View giftV2View = giftV2ViewWeakReference.get();
            if (giftV2View != null) {
                Activity activity = (Activity) giftV2View.context;
                if (activity != null && !activity.isDestroyed()) {
                    GiftReceiveInfo giftRecieveInfo = (GiftReceiveInfo) msg.obj;
                    if (giftRecieveInfo != null) {
                        giftV2View.drawGiftEffect(giftRecieveInfo);
                    }
                }
            }
        }
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onRecieveGiftMsg(GiftReceiveInfo giftReceiveInfo) {
        //如果不是大礼物的传空适配ios
        if (giftReceiveInfo != null) {
            RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (mCurrentRoomInfo == null) return;
            giftReceiveInfo.setRoomUid(String.valueOf(mCurrentRoomInfo.getUid()));
        }
        List<GiftReceiveInfo> giftReceiveInfos = new ArrayList<>();
        giftReceiveInfos.add(giftReceiveInfo);
        drawAnimation(giftReceiveInfos);
    }

    /**
     * 大礼物的特效
     *
     * @param giftReceiveInfo
     */
    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onSuperGiftMsg(GiftReceiveInfo giftReceiveInfo) {

        LogUtil.d("IGiftCoreClient", "onSuperGiftMsg");
        Message msg = Message.obtain();
        msg.what = 0;
        giftReceiveInfo.setPersonCount(1);
        msg.obj = giftReceiveInfo;
        handler.sendMessageDelayed(msg, 100);
    }

    private void drawGiftEffect(GiftReceiveInfo giftReceiveInfo) {
        giftReceiveInfoList.add(giftReceiveInfo);
        if (!giftEffectView.isAnim()) {
            giftEffectView.startGiftEffect(giftReceiveInfo);
            giftReceiveInfoList.remove(0);
        }
    }

    private void drawGiftView(Point senderPoint, Point receivePoint, GiftInfo giftInfo) {
        final Point center = new Point();
        center.x = ResolutionUtils.getScreenWidth(context) / 2;
        center.y = ResolutionUtils.getScreenHeight(context) / 2;
        final ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams;
        if (senderPoint == null) {
            senderPoint = new Point(ResolutionUtils.getScreenWidth(context) / 2 - giftWidth / 2, UIUtil.dip2px(context, 10));
            layoutParams = new RelativeLayout.LayoutParams(giftWidth, giftWidth);
            layoutParams.leftMargin = mScreenWidth / 2 - giftWidth / 2;
            layoutParams.topMargin = UIUtil.dip2px(context, 10);
        } else {
            layoutParams = new RelativeLayout.LayoutParams(giftWidth, giftWidth);
            layoutParams.leftMargin = senderPoint.x;
            layoutParams.topMargin = senderPoint.y;
        }

        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        flSend.addView(imageView);
        ImageLoadUtils.loadImage(context, giftInfo.getGiftUrl(), imageView);

        Keyframe kx0 = Keyframe.ofFloat(0f, 0);
        Keyframe kx1 = Keyframe.ofFloat(0.2f, center.x - senderPoint.x - giftWidth / 2);
        Keyframe kx2 = Keyframe.ofFloat(0.4f, center.x - senderPoint.x - giftWidth / 2);
        kx2.setInterpolator(new AccelerateDecelerateInterpolator());
        Keyframe kx3 = Keyframe.ofFloat(0.8f, center.x - senderPoint.x - giftWidth / 2);
        kx3.setInterpolator(new AccelerateDecelerateInterpolator());
        Keyframe kx4 = Keyframe.ofFloat(1f, receivePoint.x - senderPoint.x);
        kx4.setInterpolator(new AccelerateDecelerateInterpolator());

        Keyframe ky0 = Keyframe.ofFloat(0f, 0);
        Keyframe ky1 = Keyframe.ofFloat(0.2f, center.y - senderPoint.y - giftHeight / 2);
        Keyframe ky2 = Keyframe.ofFloat(0.4f, center.y - senderPoint.y - giftHeight / 2);
        ky2.setInterpolator(new AccelerateDecelerateInterpolator());
        Keyframe ky3 = Keyframe.ofFloat(0.8f, center.y - senderPoint.y - giftHeight / 2);
        ky3.setInterpolator(new AccelerateDecelerateInterpolator());
        Keyframe ky4 = Keyframe.ofFloat(1f, receivePoint.y - senderPoint.y);
        ky4.setInterpolator(new AccelerateDecelerateInterpolator());

        Keyframe ks0 = Keyframe.ofFloat(0f, 0.2f);
        Keyframe ks1 = Keyframe.ofFloat(0.2f, 1f);
        Keyframe ks2 = Keyframe.ofFloat(0.4f, 1.5f);
        Keyframe ks3 = Keyframe.ofFloat(0.6f, 2f);
        Keyframe ks4 = Keyframe.ofFloat(0.8f, 2f);
        Keyframe ks5 = Keyframe.ofFloat(1f, 0.2f);

        PropertyValuesHolder p0 = PropertyValuesHolder.ofKeyframe("translationX", kx0, kx1, kx2, kx3, kx4);
        PropertyValuesHolder p1 = PropertyValuesHolder.ofKeyframe("translationY", ky0, ky1, ky2, ky3, ky4);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofKeyframe("scaleX", ks0, ks1, ks2, ks3, ks4, ks5);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofKeyframe("scaleY", ks0, ks1, ks2, ks3, ks4, ks5);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(imageView, p2, p3, p1, p0);
        objectAnimator.setDuration(4000);
        objectAnimator.start();

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) imageView.getParent();
                viewGroup.removeView(imageView);
            }
        });
    }

    public void release() {
        CoreManager.removeClient(this);
        giftReceiveInfoList.clear();
        giftEffectView.release();
        handler.removeMessages(0);
    }

    @Override
    public void onGiftEffectEnd() {
        if (giftReceiveInfoList != null && giftReceiveInfoList.size() > 0) {
            giftEffectView.startGiftEffect(giftReceiveInfoList.get(0));
            giftReceiveInfoList.remove(0);
        }
    }
}
