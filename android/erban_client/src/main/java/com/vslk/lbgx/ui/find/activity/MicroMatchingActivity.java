package com.vslk.lbgx.ui.find.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hncxco.library_ui.widget.AppToolBar;
import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.tinderstack.TinderStackLayout;
import com.tongdaxing.erban.libcommon.tinderstack.bus.RxBus;
import com.tongdaxing.erban.libcommon.tinderstack.bus.events.TopCardMovedEvent;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.audio.AudioPlayAndRecordManager;
import com.tongdaxing.xchat_core.find.MicroMatch;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.find.MicroMatchingPresenter;
import com.vslk.lbgx.presenter.find.MicroMatchingView;
import com.vslk.lbgx.room.audio.activity.AudioRecordActivity;
import com.vslk.lbgx.ui.widget.MatchCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/24
 * 描述
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(MicroMatchingPresenter.class)
public class MicroMatchingActivity extends BaseMvpActivity<MicroMatchingView, MicroMatchingPresenter>
        implements MicroMatchingView, TinderStackLayout.OnCardViewRemovedListener, View.OnClickListener, MatchCardView
        .OnMatchCardViewClickListener {

    private static final String TAG = MicroMatchingActivity.class.getSimpleName();

    @BindView(R.id.tsl_matchs)
    TinderStackLayout<MatchCardView> tsl_matchs;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;
    @BindView(R.id.iv_like)
    ImageView iv_like;
    @BindView(R.id.toolbar)
    AppToolBar mToolBar;
    @BindView(R.id.error_content)
    TextView errorContent;

    private List<MicroMatch> microMatches = new ArrayList<>();

    private MicroMatch linkMicroMatch = null;
    private AudioPlayAndRecordManager audioManager;
    private AudioPlayer audioPlayer;

    public static void start(Context context) {
        Intent intent = new Intent(context, MicroMatchingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_matching);
        ButterKnife.bind(this);
        TinderStackLayout.canTouchToMove = true;
        initView();
        initData();
    }

    private void initView() {
        tsl_matchs.getPublishSubject().subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(integer -> onMatchCardViewNumChanged(integer));
        tsl_matchs.setOnCardViewRemovedListener(this);

        iv_delete.setOnClickListener(this);
        iv_like.setOnClickListener(this);

        mToolBar.setOnBackBtnListener(view -> finish());
        mToolBar.setOnRightBtnClickListener(view -> AudioRecordActivity.start(this));
    }

    private void initData() {
        getMvpPresenter().soundMatchRandomUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                final int childCount = tsl_matchs.getChildCount();
                if (childCount >= 2) {
                    RxBus.getInstance().send(new TopCardMovedEvent(tsl_matchs.screenWidth));
                    MatchCardView childView = (MatchCardView) tsl_matchs.getChildAt(childCount - 1);
                    childView.dismissCardSelf();
                } else {
                    toast("没有更多了哦！");
                }
                break;
            case R.id.iv_like:
//                if (null != linkMicroMatch) {
//                    AVRoomActivity.start(this, linkMicroMatch.getUid());
//                }
                final int cCount = tsl_matchs.getChildCount();
                if (cCount > 0) {
                    MatchCardView childView = (MatchCardView) tsl_matchs.getChildAt(cCount - 1);
                    onAttentionIVClicked(childView);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetNextMatchView(List<MicroMatch> microMatch) {
        if (!ListUtils.isListEmpty(microMatch)) {
            if (tsl_matchs.getVisibility() != View.VISIBLE) {
                tsl_matchs.setVisibility(View.VISIBLE);
                errorContent.setVisibility(View.GONE);
            }
            microMatches.clear();
            microMatches.addAll(microMatch);
            MicroMatch match = microMatches.get(0);
            addMicroMatchCardView(match);
            this.linkMicroMatch = match;
        } else {
            if (microMatches.size() <= 0) {
                tsl_matchs.setVisibility(View.GONE);
                errorContent.setText("暂无任何数据哦!");
                errorContent.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addMicroMatchCardView(MicroMatch microMatch) {
        if (null != microMatch) {
            MatchCardView mcv = new MatchCardView(this);
            mcv.setOnMatchCardViewClickListener(this);
            mcv.bind(microMatch);
            tsl_matchs.addCard(mcv);
            microMatches.remove(microMatch);
        }
    }

    @Override
    public void onGetNextMatchFailView(String msg) {
        if (microMatches.size() <= 0) {
            tsl_matchs.setVisibility(View.GONE);
            errorContent.setVisibility(View.VISIBLE);
            errorContent.setText(isNetworkAvailable() ? "暂无任何数据哦!" : "网络异常，请检查网络设置!");
        }
    }

    @Override
    public void onCardViewRemoved() {
        lastClickedMatchCardView = null;
        lastMicroMatch = null;
        if (null != audioManager && audioManager.isPlaying()) {
            audioManager.stopPlay();
        }

        final int childCount = tsl_matchs.getChildCount();
        if (childCount > 0) {
            MatchCardView childView = (MatchCardView) tsl_matchs.getChildAt(childCount - 1);
            Logger.i(TAG, childView.getMicroMatch().toString());
            linkMicroMatch = childView.getMicroMatch();
        }

//        if (microMatches.size() > 0) {
//            MicroMatch delMM = microMatches.remove(0);
//            LogUtils.d(TAG, "onCardViewRemoved-delMM.uid:" + delMM.getUid());
//        }
//        if (microMatches.size() > 0) {
//            linkMicroMatch = microMatches.get(0);
//        }
    }

    private void onMatchCardViewNumChanged(Integer integer) {
        if (integer != null && integer <= 2 && !ListUtils.isListEmpty(microMatches)) {
            MicroMatch microMatch = microMatches.get(0);
            addMicroMatchCardView(microMatch);
        }
    }

    //===========================cardview界面点击事件响应（关注、录音播放）========================

    private MatchCardView lastClickedMatchCardView = null;
    private MicroMatch lastMicroMatch;

    OnPlayListener onPlayListener = new OnPlayListener() {

        @Override
        public void onPrepared() {
            //准备开始试听,图标状态不管
        }

        @Override
        public void onCompletion() {
            //试听结束,更新试听按钮为试听
            if (null != lastClickedMatchCardView) {
                lastClickedMatchCardView.updateAudioPlayStatus(false);
            }
        }

        @Override
        public void onInterrupt() {
            //试听被中断
            if (null != lastClickedMatchCardView) {
                lastClickedMatchCardView.updateAudioPlayStatus(false);
            }
        }

        @Override
        public void onError(String s) {
            //试听出错
            if (null != lastClickedMatchCardView) {
                lastClickedMatchCardView.updateAudioPlayStatus(false);
            }
        }

        @Override
        public void onPlaying(long l) {

        }
    };

    @Override
    public void onAttentionIVClicked(MatchCardView matchCardView) {
        lastClickedMatchCardView = matchCardView;
        MicroMatch microMatch = (MicroMatch) matchCardView.getTag();
        if (null != microMatch) {
            if (null == lastMicroMatch || lastMicroMatch.getUid() != microMatch.getUid()) {
                lastMicroMatch = microMatch;
                if (null == audioManager) {
                    audioManager = AudioPlayAndRecordManager.getInstance();
                }
                if (null != audioPlayer) {
                    audioPlayer.stop();
                    audioPlayer.setOnPlayListener(null);
                    audioPlayer = null;
                }
            }
            getDialogManager().showProgressDialog(MicroMatchingActivity.this, "请稍后...", true);
            if (microMatch.isLike()) {
                CoreManager.getCore(IPraiseCore.class).cancelPraise(microMatch.getUid(), true);
            } else {
                CoreManager.getCore(IPraiseCore.class).praise(microMatch.getUid());
            }
        }
    }

    @Override
    public void onAudioPlayerViewClicked(MatchCardView matchCardView, boolean isPlaying) {
        lastClickedMatchCardView = matchCardView;
        MicroMatch microMatch = (MicroMatch) matchCardView.getTag();
        if (null != microMatch) {
            if (null == lastMicroMatch || lastMicroMatch.getUid() != microMatch.getUid()) {
                lastMicroMatch = microMatch;
                if (null == audioManager) {
                    audioManager = AudioPlayAndRecordManager.getInstance();
                }
                if (null != audioPlayer) {
                    audioPlayer.stop();
                    audioPlayer.setOnPlayListener(null);
                    audioPlayer = null;
                }
                audioPlayer = audioManager.getAudioPlayer(this, null, onPlayListener);
            }

            if (isPlaying) {
                if (null != audioManager && null != audioPlayer && !audioPlayer.isPlaying() && !TextUtils.isEmpty(microMatch.getUserVoice()) && microMatch.getVoiceDura() > 0) {
                    audioPlayer.setDataSource(microMatch.getUserVoice());
                    audioManager.play();
                    matchCardView.updateAudioPlayStatus(true);
                }
            } else {
                if (null != audioManager && null != audioPlayer && audioPlayer.isPlaying()) {
                    audioManager.stopPlay();
                    matchCardView.updateAudioPlayStatus(false);
                }
            }
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraise(long likedUid) {
        if (null != lastClickedMatchCardView) {
            lastClickedMatchCardView.updateAttentionStatus(false);
        }
        lastClickedMatchCardView = null;
        if (null != lastMicroMatch) {
            lastMicroMatch.setLike(true);
        }
        getDialogManager().dismissDialog();
        toast("关注成功，相互关注可成为好友哦！");
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraiseFaith(String error) {
        lastClickedMatchCardView = null;
        getDialogManager().dismissDialog();
        toast(error);
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long likedUid, boolean showNotice) {
        if (null != lastClickedMatchCardView) {
            lastClickedMatchCardView.updateAttentionStatus(false);
        }
        lastClickedMatchCardView = null;
        if (null != lastMicroMatch) {
            lastMicroMatch.setLike(false);
        }
        toast("取消关注成功");
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraiseFaith(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioManager != null) {
            audioManager.release();
            if (audioPlayer != null) {
                audioPlayer = null;
            }
            audioManager = null;
        }
    }
}
