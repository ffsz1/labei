package com.vslk.lbgx.room.audio.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.player.IPlayerCore;
import com.tongdaxing.xchat_core.player.IPlayerCoreClient;
import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.audio.activity.AddMusicListActivity;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * 音乐播放入口
 * Created by chenran on 2017/10/28.
 */

public class MusicPlayerView extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ImageView musicFlagLayout;
    private LinearLayout musicBoxLayout;
    //private ImageView musicFlag;
    //private ImageView packUp;
    private ImageView musicListMore;
    private ImageView musicPlayPause;
    private ImageView nextBtn;
    private SeekBar volumeSeekBar;
    private TextView musicName;
    private String imageBg;
    private View rootView;

    private ImageView forwardBtn;
    private SeekBar musicSoiceSeek;

    public MusicPlayerView(Context context) {
        super(context);
        init();
    }

    public MusicPlayerView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public MusicPlayerView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init();
    }

    private void init() {
        CoreManager.addClient(this);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_music_player_view, this, true);
        rootView = findViewById(R.id.fl_root);
        rootView.setOnClickListener(this);
        rootView.setClickable(false);

        musicFlagLayout = findViewById(R.id.music_flag_layout);
        musicFlagLayout.setOnClickListener(this);
        //musicFlag = (ImageView) findViewById(R.id.music_flag);
        //packUp = (ImageView) findViewById(R.id.pack_up);
        //packUp.setOnClickListener(this);
        musicBoxLayout = findViewById(R.id.music_box_layout);
        musicBoxLayout.setOnClickListener(this);
        musicListMore = findViewById(R.id.music_list_more);
        musicListMore.setOnClickListener(this);

        musicPlayPause = findViewById(R.id.music_play_pause);
        musicPlayPause.setOnClickListener(this);

        volumeSeekBar = findViewById(R.id.voice_seek);
        volumeSeekBar.setMax(100);
        volumeSeekBar.setProgress(CoreManager.getCore(IPlayerCore.class).getCurrentVolume());
        volumeSeekBar.setOnSeekBarChangeListener(this);

        musicSoiceSeek = findViewById(R.id.music_voice_seek);
        musicSoiceSeek.setMax(100);
        musicSoiceSeek.setProgress(CoreManager.getCore(IPlayerCore.class).getCurrentRecordingVolume());
        musicSoiceSeek.setOnSeekBarChangeListener(this);


        musicName = findViewById(R.id.music_name);
        nextBtn = findViewById(R.id.music_play_next);
        nextBtn.setOnClickListener(this);
        forwardBtn = findViewById(R.id.music_play_forward);
        forwardBtn.setOnClickListener(this);
        updateView();
    }

    public void setImageBg(String imageBg) {
        this.imageBg = imageBg;
    }

    public void setMusicClick() {
        rootView.setClickable(true);
        showBoxInAnim();
    }

    private void playFlagRotateAnim() {
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_quick_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        //musicFlag.startAnimation(operatingAnim);
    }

    private void stopFlagRotateAnim() {
        //musicFlag.clearAnimation();
    }

    public void showFlagInAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(musicBoxLayout, "translationX",
                (UIUtil.getScreenWidth(getContext()) - UIUtil.dip2px(getContext(), 380)) / 2, UIUtil.dip2px
                        (getContext(), 380)).setDuration(150);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                musicBoxLayout.setVisibility(GONE);
            }
        });

//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(musicFlagLayout, "translationX", UIUtil.dip2px
// (getContext(), 80), 0).setDuration(150);
//        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
//        objectAnimator1.setStartDelay(150);
//        objectAnimator1.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                musicBoxLayout.setVisibility(GONE);
//            }
//        });
//        objectAnimator1.start();
    }

    private void showBoxInAnim() {
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(musicFlagLayout, "translationX", 0, UIUtil.dip2px
// (getContext(), 80)).setDuration(150);
//        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        objectAnimator.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(musicBoxLayout, "translationX",
                UIUtil.getScreenWidth(getContext()), (UIUtil.getScreenWidth(getContext()) - UIUtil.dip2px(getContext(), 380)) / 2).setDuration(150);
        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator1.setStartDelay(150);
        objectAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                musicBoxLayout.setVisibility(VISIBLE);
            }
        });
        objectAnimator1.start();
    }

    public void updateVoiceValue() {
        volumeSeekBar.setProgress(CoreManager.getCore(IPlayerCore.class).getCurrentVolume());
    }

    public void release() {
        CoreManager.removeClient(this);
        //stopFlagRotateAnim();
//        stopMusic();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_flag_layout:
                setMusicClick();
                break;
            case R.id.fl_root:
                //case R.id.pack_up:
                rootView.setClickable(false);
                showFlagInAnim();
                break;
            case R.id.music_list_more:
                AddMusicListActivity.start(getContext(), imageBg);
                break;
            case R.id.music_play_pause:
                //: 2018/3/29
                List<LocalMusicInfo> localMusicInfoList = CoreManager.getCore(IPlayerCore.class)
                        .getPlayerListMusicInfos();
                if (localMusicInfoList != null && localMusicInfoList.size() > 0) {
                    int state = CoreManager.getCore(IPlayerCore.class).getState();
                    if (state == IPlayerCore.STATE_PLAY) {
                        CoreManager.getCore(IPlayerCore.class).pause();
                    } else if (state == IPlayerCore.STATE_PAUSE) {
                        CoreManager.getCore(IPlayerCore.class).play(null);
                    } else {
                        int result = CoreManager.getCore(IPlayerCore.class).playNext();
                        if (result < 0) {
                            if (result == -3) {
                                ((BaseMvpActivity) getContext()).toast("播放列表中还没有歌曲哦！");
                            } else {
                                ((BaseMvpActivity) getContext()).toast("播放失败，文件异常");
                            }
                        }
                    }
                } else {
                    AddMusicListActivity.start(getContext(), imageBg);
                }
                break;
            case R.id.music_play_next:
                List<LocalMusicInfo> localMusicInfoList1 = CoreManager.getCore(IPlayerCore.class)
                        .getPlayerListMusicInfos();
                if (localMusicInfoList1 != null && localMusicInfoList1.size() > 0) {
                    int result = CoreManager.getCore(IPlayerCore.class).playNext();
                    if (result < 0) {
                        if (result == -3) {
                            ((BaseMvpActivity) getContext()).toast("播放列表中还没有歌曲哦！");
                        } else {
                            ((BaseMvpActivity) getContext()).toast("播放失败，文件异常");
                        }
                    }
                } else {
                    AddMusicListActivity.start(getContext(), imageBg);
                }
                break;
            //播放上一首
            case R.id.music_play_forward:
                List<LocalMusicInfo> localMusicInfoList0 = CoreManager.getCore(IPlayerCore.class).getPlayerListMusicInfos();
                if (localMusicInfoList0 != null && localMusicInfoList0.size() > 0) {
                    int result = CoreManager.getCore(IPlayerCore.class).playForward();
                    if (result < 0) {
                        if (result == -3) {
                            ((BaseMvpActivity) getContext()).toast("播放列表中还没有歌曲哦！");
                        } else {
                            ((BaseMvpActivity) getContext()).toast("播放失败，文件异常");
                        }
                    }
                } else {
                    AddMusicListActivity.start(getContext(), imageBg);
                }
                break;
            default:
                break;
        }
    }

    private void updateView() {
        LocalMusicInfo current = CoreManager.getCore(IPlayerCore.class).getCurrent();
        if (current != null) {
            musicName.setText(current.getSongName());
            int state = CoreManager.getCore(IPlayerCore.class).getState();
            if (state == IPlayerCore.STATE_PLAY) {
                musicPlayPause.setImageResource(R.mipmap.icon_music_play);
            } else {
                musicPlayPause.setImageResource(R.mipmap.icon_music_pause_small);
            }
            playFlagRotateAnim();
        } else {
            musicName.setText("暂无歌曲播放");
            musicPlayPause.setImageResource(R.mipmap.icon_music_pause_small);
            //stopFlagRotateAnim();
        }
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onMusicPlaying(LocalMusicInfo localMusicInfo) {
        updateView();
        playFlagRotateAnim();
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onMusicPause(LocalMusicInfo localMusicInfo) {
        updateView();

    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onMusicStop() {
        updateView();
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onCurrentMusicUpdate(LocalMusicInfo localMusicInfo) {
        updateView();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == volumeSeekBar) {
            CoreManager.getCore(IPlayerCore.class).seekVolume(progress);
        } else {
            CoreManager.getCore(IPlayerCore.class).seekRecordingVolume(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
