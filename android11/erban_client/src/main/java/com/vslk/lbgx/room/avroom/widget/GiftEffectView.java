package com.vslk.lbgx.room.avroom.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.GiftReceiveInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.ResolutionUtils;
import com.tongdaxing.xchat_framework.util.util.RichTextUtil;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenran on 2017/10/8.
 */

public class GiftEffectView extends RelativeLayout implements SVGACallback {
    private RelativeLayout container;
    private SVGAImageView svgaImageView;
    public View svgaBg;
    private ImageView giftLightBg;
    private ImageView giftImg;
    private ImageView imgBg;
    private CircleImageView benefactorAvatar;
    private CircleImageView receiverAvatar;
    private TextView benefactorNick;
    private TextView receiverNick;
    private TextView giftNumber;
    private TextView giftName;
    private GiftEffectListener giftEffectListener;
    private EffectHandler effectHandler;
    private boolean isAnim;
    private LinearLayout benfactor;
    private TextView giveText;
    private ImageView ivSend;
    private LinearLayout receiverContainer;
    private TextView tvSupergiftInfo;

    public interface GiftEffectListener {
        void onGiftEffectEnd();
    }

    public boolean isAnim() {
        return isAnim;
    }

    public GiftEffectView(Context context) {
        super(context);
        init();
    }

    public GiftEffectView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public GiftEffectView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init();
    }

    public void setGiftEffectListener(GiftEffectListener giftEffectListener) {
        this.giftEffectListener = giftEffectListener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_gift_effect, this, true);
        effectHandler = new EffectHandler(this);
        container = (RelativeLayout) findViewById(R.id.container);
        imgBg = (ImageView) findViewById(R.id.img_bg);
        giftLightBg = (ImageView) findViewById(R.id.gift_light_bg);
        giftImg = (ImageView) findViewById(R.id.gift_img);
        benefactorAvatar = (CircleImageView) findViewById(R.id.benefactor_avatar);
        receiverAvatar = (CircleImageView) findViewById(R.id.receiver_avatar);
        benefactorNick = (TextView) findViewById(R.id.benefactor_nick);
        receiverNick = (TextView) findViewById(R.id.receiver_nick);
        giftNumber = (TextView) findViewById(R.id.gift_number);
        giftName = (TextView) findViewById(R.id.gift_name);
        svgaImageView = (SVGAImageView) findViewById(R.id.svga_imageview);


        benfactor = (LinearLayout) findViewById(R.id.benefactor_container);
        giveText = (TextView) findViewById(R.id.give_text);
        ivSend = (ImageView) findViewById(R.id.iv_send);
        receiverContainer = (LinearLayout) findViewById(R.id.receiver_container);
        tvSupergiftInfo = findViewById(R.id.tv_super_gift_info);


        svgaImageView.setCallback(this);
        svgaImageView.setClearsAfterStop(true);
        svgaImageView.setLoops(1);
        svgaBg = findViewById(R.id.svga_imageview_bg);
    }

    //: 2018/3/10  横幅的ui
    public void startGiftEffect(GiftReceiveInfo giftRecieveInfo) {
        this.isAnim = true;
        GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(giftRecieveInfo.getGiftId());
        if (giftInfo != null) {
            //如果有roomId是大礼物，全服特效
            //这个roomId其实是送礼物的人的uid
            //userNo是送礼物的房间  显示的ID号
            final String roomId = giftRecieveInfo.getRoomUid();
            RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (mCurrentRoomInfo == null) return;
            if (roomId.equals(String.valueOf(mCurrentRoomInfo.getUid()))) {
                benfactor.setVisibility(VISIBLE);
                ivSend.setVisibility(VISIBLE);
                receiverContainer.setVisibility(VISIBLE);
                tvSupergiftInfo.setVisibility(GONE);
                giftNumber.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                tvSupergiftInfo.setOnClickListener(v -> {
                    ((BaseMvpActivity) getContext()).getDialogManager().showOkCancelDialog("是否确认跳到该房间", true, new DialogManager.AbsOkDialogListener() {
                        @Override
                        public void onOk() {
                            Intent intent = new Intent(getContext(), AVRoomActivity.class);
                            intent.putExtra(Constants.ROOM_UID, JavaUtil.str2long(roomId));
                            intent.putExtra(Constants.ROOM_TYPE, 3);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);

                        }
                    });
                });
                benfactor.setVisibility(INVISIBLE);
                giveText.setVisibility(INVISIBLE);
                ivSend.setVisibility(INVISIBLE);
                receiverContainer.setVisibility(INVISIBLE);
                tvSupergiftInfo.setVisibility(VISIBLE);

                String nick = giftRecieveInfo.getNick() + "";
                if (nick.length() > 6) {
                    nick = nick.substring(0, 6) + "…";
                }
                String targetNick = giftRecieveInfo.getTargetNick() + "";
                if (targetNick.length() > 6) {
                    targetNick = targetNick.substring(0, 6) + "…";
                }
                List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> map;
                map = new HashMap<String, Object>();
                map.put(RichTextUtil.RICHTEXT_STRING, nick);
                map.put(RichTextUtil.RICHTEXT_COLOR, Color.YELLOW);
                list.add(map);

                map = new HashMap<String, Object>();
                map.put(RichTextUtil.RICHTEXT_STRING, "在");
                map.put(RichTextUtil.RICHTEXT_COLOR, Color.WHITE);
                list.add(map);

                map = new HashMap<String, Object>();
                map.put(RichTextUtil.RICHTEXT_STRING, "ID" + giftRecieveInfo.getUserNo());
                map.put(RichTextUtil.RICHTEXT_COLOR, Color.YELLOW);
                list.add(map);

                map = new HashMap<String, Object>();
                map.put(RichTextUtil.RICHTEXT_STRING, "房间送给");
                map.put(RichTextUtil.RICHTEXT_COLOR, Color.WHITE);
                list.add(map);

                map = new HashMap<String, Object>();
                map.put(RichTextUtil.RICHTEXT_STRING, targetNick);
                map.put(RichTextUtil.RICHTEXT_COLOR, Color.YELLOW);
                list.add(map);

                tvSupergiftInfo.setText(RichTextUtil.getSpannableStringFromList(list));
                giftNumber.setTextColor(Color.parseColor("#491E1A"));
            }
            ImageLoadUtils.loadImage(benefactorAvatar.getContext(), giftRecieveInfo.getAvatar(), benefactorAvatar);
            ImageLoadUtils.loadImage(giftImg.getContext(), giftInfo.getGiftUrl(), giftImg);
            benefactorNick.setText(giftRecieveInfo.getNick());
            giftNumber.setText("x" + giftRecieveInfo.getGiftNum());
            giftName.setText(giftInfo.getGiftName());
            container.setVisibility(GONE);

            if (!StringUtil.isEmpty(giftRecieveInfo.getTargetAvatar()) && !StringUtil.isEmpty(giftRecieveInfo.getNick())) {
                ImageLoadUtils.loadAvatar(receiverAvatar.getContext(), giftRecieveInfo.getTargetAvatar(), receiverAvatar);
                receiverNick.setText(giftRecieveInfo.getTargetNick());
            } else {
                receiverAvatar.setImageResource(R.drawable.ic_default_avatar);
                receiverNick.setText("全麦");
            }

            Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.light_bg_rotate_anim);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            giftLightBg.setAnimation(operatingAnim);

            final Point center = new Point();
            center.x = ResolutionUtils.getScreenWidth(getContext()) / 2;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(container, "translationX", -UIUtil.dip2px
                    (getContext(), 400), center.x - container.getWidth() / 2).setDuration(500);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();

            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(container, "alpha", 0.0F, 1.0F).setDuration(500);
            objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator1.start();


            int totalCoin = giftInfo.getGoldPrice() * giftRecieveInfo.getGiftNum();
            if (giftRecieveInfo.getPersonCount() > 0) {
                totalCoin = giftInfo.getGoldPrice() * giftRecieveInfo.getGiftNum() * giftRecieveInfo.getPersonCount();
            }
            if (!roomId.equals(String.valueOf(mCurrentRoomInfo.getUid()))) {
                imgBg.setImageResource(R.drawable.icon_gift_effect_bg_4);
            } else if (totalCoin >= 520 && totalCoin < 4999) {
                imgBg.setImageResource(R.drawable.icon_gift_effect_bg_1);
            } else if (totalCoin >= 4999 && totalCoin < 9999) {
                imgBg.setImageResource(R.drawable.icon_gift_effect_bg_2);
            } else if (totalCoin >= 9999) {
                imgBg.setImageResource(R.drawable.icon_gift_effect_bg_3);
            }
            effectHandler.sendEmptyMessageDelayed(0, 6000);
            if (mCurrentRoomInfo.getGiftEffectSwitch() == 0 && giftInfo.isHasEffect() && !StringUtil.isEmpty(giftInfo.getVggUrl()) && roomId.equals(String.valueOf(mCurrentRoomInfo.getUid()))) {
                try {
                    drawSvgaEffect(giftInfo.getVggUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 礼物全屏特效
     *
     * @param url
     * @throws MalformedURLException
     */
    public void drawSvgaEffect(String url) throws Exception {
        SVGAParser parser = new SVGAParser(getContext());
        parser.parse(new URL(url), new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                SVGADrawable drawable = new SVGADrawable(videoItem);
                svgaImageView.setImageDrawable(drawable);
                svgaImageView.startAnimation();
                svgaBg.setVisibility(VISIBLE);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(svgaBg, "alpha", 0.0F, 2.0F).setDuration(800);
                objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator1.start();
            }

            @Override
            public void onError() {

            }
        });

    }

    private void deleteAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(container, "translationX", container.getX(),
                ResolutionUtils.getScreenWidth(getContext())).setDuration(500);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(container, "alpha", 1.0F, 0.0F).setDuration(500);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (giftEffectListener != null) {
                    isAnim = false;
                    giftEffectListener.onGiftEffectEnd();
                }
            }
        });
        objectAnimator1.start();
    }

    private static class EffectHandler extends Handler {
        private WeakReference<GiftEffectView> effectViewWeakReference;

        public EffectHandler(GiftEffectView giftEffectView) {
            effectViewWeakReference = new WeakReference<>(giftEffectView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (effectViewWeakReference != null) {
                final GiftEffectView giftEffectView = effectViewWeakReference.get();
                if (giftEffectView != null) {
                    if (msg.what == 0) {
                        giftEffectView.deleteAnim();
                    }
                }
            }
        }
    }

    public void release() {
        effectHandler.removeMessages(0);
//        svgaImageView.stopAnimation(true);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onFinished() {
        svgaBg.setVisibility(GONE);
    }

    @Override
    public void onRepeat() {

    }

    @Override
    public void onStep(int i, double v) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (svgaImageView != null) {
            svgaImageView.clearAnimation();
        }
        if (svgaBg != null) {
            svgaBg.clearAnimation();
        }
    }
}
