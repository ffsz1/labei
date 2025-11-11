package com.vslk.lbgx.room.avroom.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.vslk.lbgx.room.gift.anim.CustormAnim;
import com.vslk.lbgx.room.gift.anim.GiftControl;
import com.vslk.lbgx.room.gift.anim.GiftFrameLayout;
import com.vslk.lbgx.room.gift.model.GiftModel;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.GiftReceiveInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.gift.IGiftCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenran
 * @date 2017/7/29
 */

public class GiftView extends RelativeLayout implements SVGACallback {
    public static final int TYPE_ROOM = 0;
    public static final int TYPE_PERSONAL = 1;
    private GiftFrameLayout giftFrameLayout1;
    private GiftFrameLayout giftFrameLayout2;
    private GiftControl giftControl;
    private GiftModel giftModel;
    private int type;
    private SVGAImageView svgaImageView;
    private List<GiftInfo> giftInfos;
    private boolean isSvgaAnimate;
    private View svgaBg;

    public GiftView(Context context) {
        super(context);
        init();
    }

    public GiftView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public GiftView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init();
    }

    private void init() {
        CoreManager.addClient(this);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_gift_view, this, true);
        findView();
    }

    private void findView() {
        giftFrameLayout1 = (GiftFrameLayout) findViewById(R.id.gift_layout_1);
        giftFrameLayout2 = (GiftFrameLayout) findViewById(R.id.gift_layout_2);
        giftControl = new GiftControl();
        giftInfos = new ArrayList<>();
        SparseArray<GiftFrameLayout> giftLayoutList = new SparseArray<>();
        giftLayoutList.append(0, giftFrameLayout1);
        giftLayoutList.append(1, giftFrameLayout2);
        giftControl.setGiftLayout(false, giftLayoutList)
                .setCustormAnim(new CustormAnim());//这里可以自定义礼物动画
        svgaImageView = (SVGAImageView) findViewById(R.id.svga_imageview);
        svgaImageView.setCallback(this);
        svgaImageView.setClearsAfterStop(true);
        svgaImageView.setLoops(1);
        svgaBg = findViewById(R.id.svga_imageview_bg);
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onRecievePersonalGift(GiftReceiveInfo giftRecieveInfo) {


        if (type != TYPE_PERSONAL) {
            return;
        }
        showGift(giftRecieveInfo);
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onRecieveGiftMsg(GiftReceiveInfo giftRecieveInfo) {

        if (type != TYPE_ROOM) {
            return;
        }
        showGift(giftRecieveInfo);
    }

    private void showGift(GiftReceiveInfo giftRecieveInfo) {


        GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(giftRecieveInfo.getGiftId());
        if (giftInfo != null) {
            giftModel = new GiftModel();
            giftModel.setGiftId(giftInfo.getGiftId() + "").setGiftName("送出" + giftInfo.getGiftName()).setGiftCount(1).setGiftPic(giftInfo.getGiftUrl())
                    .setSendUserId(giftRecieveInfo.getUid() + "").setSendUserName(giftRecieveInfo.getNick()).setSendUserPic(giftRecieveInfo.getAvatar()).setSendGiftTime(System.currentTimeMillis())
                    .setCurrentStart(false).setGiftGroup(giftRecieveInfo.getGiftNum());
            giftControl.loadGift(giftModel);

            if (giftInfo.isHasEffect() && !StringUtil.isEmpty(giftInfo.getVggUrl())) {
                addGiftEffectToList(giftInfo);
            }
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    private void addGiftEffectToList(GiftInfo giftInfo) {
        giftInfos.add(giftInfo);
        if (!isSvgaAnimate) {
            try {
                drawSvgaEffect(giftInfo.getVggUrl());

            } catch (Exception e) {
                e.printStackTrace();
            }
            giftInfos.remove(0);
        }
    }

    private void drawNext() {
        if (giftInfos != null && giftInfos.size() > 0) {
            try {
                drawSvgaEffect(giftInfos.get(0).getVggUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
            giftInfos.remove(0);
        }
    }

    private void drawSvgaEffect(String url) throws Exception {
        isSvgaAnimate = true;
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
                isSvgaAnimate = false;
            }
        });

    }


    public void release() {
        CoreManager.removeClient(this);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onFinished() {
        svgaBg.setVisibility(GONE);
        isSvgaAnimate = false;
        drawNext();
    }

    @Override
    public void onRepeat() {

    }

    @Override
    public void onStep(int i, double v) {

    }
}
