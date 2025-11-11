package com.vslk.lbgx.room.avroom.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vslk.lbgx.room.avroom.adapter.MicroViewAdapter;
import com.vslk.lbgx.room.face.anim.AnimFaceFactory;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.custom.bean.RoomMatchAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.room.face.FaceReceiveInfo;
import com.tongdaxing.xchat_core.room.face.IFaceCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 上麦布局界面
 *
 * @author xiaoyu
 * @date 2017/12/20
 */
public class MicroView extends RelativeLayout {
    private static final String TAG = "MicroView";
    private RecyclerView recyclerView;
    private MicroViewAdapter adapter;
    private SparseArray<ImageView> faceImageViews;

    private Context mContext;
    private int giftWidth;
    private int giftHeight;
    private int faceWidth;
    private int faceHeight;
    private Disposable subscribe;

    public MicroView(Context context) {
        this(context, null);
    }

    public MicroView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public MicroView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        CoreManager.addClient(this);
        subscribe = IMNetEaseManager.get()
                .getChatRoomEventObservable()
                .subscribe(this::onReceiveRoomEvent);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
        }
    }

    private void onReceiveRoomEvent(RoomEvent roomEvent) {
        if (roomEvent == null || roomEvent.getEvent() == RoomEvent.NONE) {
            return;
        }
        switch (roomEvent.getEvent()) {
            case RoomEvent.SPEAK_STATE_CHANGE:
                onSpeakQueueUpdate(roomEvent.getMicPositionList());
                break;
            case RoomEvent.SPEAK_ZEGO_STATE_CHANGE:
                onZegoSpeakQueueUpdate(roomEvent.getSpeakQueueMembersPosition());
                break;
            case RoomEvent.CURRENT_SPEAK_STATE_CHANGE:
                onCurrentSpeakUpdate(roomEvent.getCurrentMicPosition(),roomEvent.getCurrentMicStreamLevel());
                break;
            default:
        }
    }

    public void setMicCenterPoint() {
        SparseArray<Point> centerPoints = new SparseArray<>();
        // 算出每一个麦位的位置
        int childCount = recyclerView.getChildCount();
        View child;

        for (int i = 0; i < childCount; i++) {
            child = recyclerView.getChildAt(i);
            int[] location = new int[2];
            int[] nameLocation = new int[2];
            // 找到头像
            View view = child.findViewById(R.id.micro_layout);
            View nameView = child.findViewById(R.id.nick);
            if (view != null) {
                child = view;
            }
            child.getLocationInWindow(location);
            nameView.getLocationInWindow(nameLocation);
            int x = (location[0] + child.getWidth() / 2) - giftWidth / 2;
            int y = location[1] >= nameLocation[1] ?
                    ((location[1] + child.getHeight() * 7 / 8) - giftHeight / 2) :
                    ((nameLocation[1] + nameView.getHeight() / 2) - giftHeight / 2);
            // 放置表情占位image view
            if (faceImageViews.get(i - 1) == null) {
                LayoutParams params = new LayoutParams(faceWidth, faceHeight);
                child.getLocationInWindow(location);
                int[] containerLocation = new int[2];
                this.getLocationInWindow(containerLocation);
                params.leftMargin = ((location[0] - containerLocation[0] + child.getWidth() / 2) - faceWidth / 2);
                params.topMargin = ((location[1] - containerLocation[1] + child.getHeight() / 2) - faceHeight / 2);
                ImageView face = new ImageView(mContext);
                face.setLayoutParams(params);
                faceImageViews.put(i - 1, face);
                addView(face);
            }
            Point point = new Point(x, y);
            centerPoints.put(i - 1, point);
        }
        AvRoomDataManager.get().mMicPointMap = centerPoints;
    }

    public MicroViewAdapter getAdapter() {
        return adapter;
    }

    private void init(final Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.layout_micro_view, this);
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.postDelayed(() -> setMicCenterPoint(), 1000);

                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        adapter = new MicroViewAdapter(mContext);
        recyclerView.setAdapter(adapter);

        giftWidth = UIUtil.dip2px(mContext, 80);
        giftHeight = UIUtil.dip2px(mContext, 80);
        faceWidth = UIUtil.dip2px(mContext, 80);
        faceHeight = UIUtil.dip2px(mContext, 80);

        faceImageViews = new SparseArray<>(9);
    }

    /**
     * 声网的声浪监听
     * @param positions
     */
    public void onSpeakQueueUpdate(List<Integer> positions) {
        int count = recyclerView.getChildCount();
        for (int i = 0; i < positions.size(); i++) {
            int pos = positions.get(i) + 1;
            if (pos >= count) continue;
            WaveView speakState = recyclerView.getChildAt(pos).findViewById(R.id.waveview);
            if (speakState != null) {
                speakState.start();
            }
        }
    }

    /**
     * 即构除自己外其他人的声浪监听
     * @param speakers
     */
    public void onZegoSpeakQueueUpdate(List<ZegoSoundLevelInfo> speakers) {
        int count = recyclerView.getChildCount();
        for (int i = 0; i < speakers.size(); i++) {
            int pos =  AvRoomDataManager.get().getMicPositionByStreamID(speakers.get(i).streamID) + 1;
            if (pos >= count) continue;
            WaveView speakState = recyclerView.getChildAt(pos).findViewById(R.id.waveview);
            if (speakState != null) {
                if (RtcEngineManager.get().isRemoteMute()){
                    speakState.stop();
                }else {
                    if (speakers.get(i).soundLevel > 0) {
                        speakState.start();
                    } else {
                        speakState.stop();
                    }
                }
            }
        }
    }

    /**
     * 即构除自己的声浪监听
     * @param currentMicPosition 当前麦位逻辑
     * @param currentMicStreamLevel
     */
    public void onCurrentSpeakUpdate(int currentMicPosition, float currentMicStreamLevel) {
        if (currentMicPosition == Integer.MIN_VALUE) {
            return;
        }
        LogUtil.d("onCurrentSpeakUpdate","currentMicPosition = " + currentMicPosition  + " currentMicStreamLevel = " + currentMicStreamLevel);
        WaveView speakState = recyclerView.getChildAt(currentMicPosition + 1).findViewById(R.id.waveview);
        if (speakState != null) {
//                speakState.setBackground(null);
//                speakState.setBackgroundResource(R.drawable.home_party_mic_list_anim);
//                AnimationDrawable animationDrawable = (AnimationDrawable) speakState.getBackground();
//                animationDrawable.start();
            // speakState.stop();
            if (RtcEngineManager.get().isMute()) {
                speakState.stop();
            }else {
                if (currentMicStreamLevel > 0) {
                    speakState.start();
                } else {
                    speakState.stop();
                }
            }
        }
    }

    @CoreEvent(coreClientClass = IFaceCoreClient.class)
    public void onReceiveFace(List<FaceReceiveInfo> faceReceiveInfos) {
        if (faceReceiveInfos == null || faceReceiveInfos.size() <= 0) return;
        for (FaceReceiveInfo faceReceiveInfo : faceReceiveInfos) {
            int position = AvRoomDataManager.get().getMicPosition(faceReceiveInfo.getUid());
            if (position < -1) continue;
            ImageView imageView = faceImageViews.get(position);
            if (imageView == null) continue;
            AnimationDrawable drawable = AnimFaceFactory.get(faceReceiveInfo, mContext, imageView.getWidth(), imageView.getHeight());
            if (drawable == null) continue;
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            drawable.setOneShot(true);
            drawable.start();
        }
    }

    @CoreEvent(coreClientClass = IFaceCoreClient.class)
    public void onReceiveMatchFace(RoomMatchAttachment faceAttachment) {
        if (faceAttachment == null) return;
        int position = AvRoomDataManager.get().getMicPosition(faceAttachment.getUid());
        if (position < -1) return;
        ImageView imageView = faceImageViews.get(position);
        if (imageView == null) return;
        AnimationDrawable drawable = AnimFaceFactory.get(faceAttachment.getNumArr(), mContext, imageView.getWidth(), imageView.getHeight(),faceAttachment.isShowd(),faceAttachment.isShowd()?2000:1000);
        if (drawable == null) return;
        imageView.setImageDrawable(drawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        drawable.setOneShot(true);
        drawable.start();
    }


    public void release() {
        // 移除麦上的所有成员还有说话的光晕
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(child);
            if (holder instanceof MicroViewAdapter.NormalMicroViewHolder) {
                ((MicroViewAdapter.NormalMicroViewHolder) holder).clear();
            }
        }
        // 移除所有的表情动画
        for (int i = -1; i < faceImageViews.size() - 1; i++) {
            ImageView imageView = faceImageViews.get(i);
            if (imageView == null) continue;
            imageView.setImageDrawable(null);
            imageView.clearAnimation();
        }
    }
}
