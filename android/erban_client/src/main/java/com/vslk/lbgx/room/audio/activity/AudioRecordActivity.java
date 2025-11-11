package com.vslk.lbgx.room.audio.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Chronometer;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityAudiorecordBinding;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.audio.AudioPlayAndRecordManager;
import com.tongdaxing.xchat_core.audio.AudioRecordPresenter;
import com.tongdaxing.xchat_core.audio.IAudioRecordView;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.file.IFileCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.vslk.lbgx.utils.PermissionUtils;

import java.io.File;

import static com.vslk.lbgx.utils.PermissionUtils.isVoicePermission;

/**
 * @author zhouxiangfeng
 * @date 2017/5/25
 */
@CreatePresenter(AudioRecordPresenter.class)
public class AudioRecordActivity extends BaseMvpActivity<IAudioRecordView, AudioRecordPresenter> implements View.OnClickListener, IAudioRecordView {

    private static final String TAG = "AudioRecordActivity";

    public static final String AUDIO_FILE = "AUDIO_FILE";
    public static final String AUDIO_DURA = "AUDIO_DURA";


    private String audioUrl;
    private AudioPlayer audioPlayer;
    private AudioPlayAndRecordManager audioManager;
    private AudioRecorder recorder;
    private ActivityAudiorecordBinding mBinding;


    private String[] tips = new String[]{"愿你三冬暖，愿你春不寒\n\n" +
            "愿你天黑有灯，下雨有伞\n\n" +
            "愿你一路上，有良人相伴",
            "一只大恐龙嗷呜嗷呜就出现了\n\n" +
                    "然后把你muamuamua吧唧吧唧吧唧\n\n" +
                    "一口一口吃掉了\n\n" +
                    "然后大恐龙嗷呜嗷呜嗷呜飞走了",
            "我对你付出的青春这么多年\n\n" +
                    "换来了一句谢谢你的成全\n\n" +
                    "成全了你的潇洒与冒险\n\n" +
                    "成全了我的碧海蓝天",
            "做一个很酷的人\n\n" +
                    "闹钟一响就起\n\n" +
                    "走了就不回头\n\n" +
                    "连告别都是两手插兜",
            "浮世三千，吾爱有三\n\n" +
                    "日月与卿\n\n" +
                    "日为朝，月为暮\n\n" +
                    "卿为朝朝暮暮"};

    private int tipsCount = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, AudioRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_audiorecord);
        mBinding.setClick(this);
        onSetListner();
        init();
    }

    private void init() {
        audioManager = AudioPlayAndRecordManager.getInstance();
        audioPlayer = audioManager.getAudioPlayer(BasicConfig.INSTANCE.getAppContext(), null, onPlayListener);
        mBinding.topTips.setText(tips[tipsCount]);
        //设置可点击
        mBinding.refresh.setEnabled(true);
        //停止动画效果
        stopAnim();
    }


    private void onRefresh() {
        //禁止当前点击
        mBinding.refresh.setEnabled(false);
        //开启动画效果
        startAnim();
        tipsCount++;
        if (tipsCount >= tips.length) {
            tipsCount = 0;
        }
        mBinding.topTips.setText(tips[tipsCount]);
        mBinding.topTips.postDelayed(() -> {
            //设置可点击
            mBinding.refresh.setEnabled(true);
            stopAnim();
        }, 500);
    }

    private void startAnim() {
        Animation rotateAnimation = new RotateAnimation(0, -359, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        mBinding.icRefresh.startAnimation(rotateAnimation);
    }

    private void stopAnim() {
        mBinding.icRefresh.clearAnimation();
    }

    private void onSetListner() {
        mBinding.toolbar.setOnBackBtnListener(view -> {
            if (audioState == STATE_RECORD_RECORDING) {
                getDialogManager().showOkCancelDialog("当前音频正在录制中，是否结束录制?", true, new DialogManager.OkCancelDialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        audioManager.stopRecord(true);
                        finish();
                    }
                });
            } else {
                finish();
            }
        });
    }

    private void startVoice() {
        if (audioState == STATE_RECORD_RECORDING) {
            toast("已经在录音...");
        } else if (audioState == STATE_RECORD_NORMAL) {


            audioState = STATE_RECORD_RECORDING;
            showByState(audioState);
            startChronometer();
            recorder = audioManager.getAudioRecorder(BasicConfig.INSTANCE.getAppContext(), onRecordCallback);
            audioManager.startRecord();
        }
    }

    private void startChronometer() {
        mBinding.tvChronometer.setFormat("");
        value = -1;
        mBinding.tvChronometer.setOnChronometerTickListener(chronometerTickListener);
        mBinding.tvChronometer.setBase(0);
        mBinding.tvChronometer.start();
    }

    private int audioState = STATE_RECORD_NORMAL;

    public static final int STATE_RECORD_RECORDING = 1;
    public static final int STATE_RECORD_SUCCESS = 2;
    public static final int STATE_RECORD_NORMAL = 0;

    private File audioFile;

    private int audioDura;

    IAudioRecordCallback onRecordCallback = new IAudioRecordCallback() {
        @Override
        public void onRecordReady() {
            Log.d(TAG, "onRecordReady");
        }

        @Override
        public void onRecordStart(File file, RecordType recordType) {
            Log.d(TAG, "onRecordStart : " + file.getPath() + "type: " + recordType.name());
        }

        @Override
        public void onRecordSuccess(File file, long l, RecordType recordType) {
            double dura = (double) l / 1000;
            audioDura = (int) Math.round(dura);
            toast("录制完成");
            audioFile = file;
            audioState = STATE_RECORD_SUCCESS;
            showByState(audioState);
        }

        @Override
        public void onRecordFail() {
            if (!isVoicePermission()) {
//                toast("录制失败,请开启麦克风授权");
                PermissionUtils.requestPermission(AudioRecordActivity.this);
            } else {
                toast("录制失败,录音时间过短");
            }
            audioState = STATE_RECORD_NORMAL;
            showByState(audioState);
            mBinding.tvChronometer.stop();
        }

        @Override
        public void onRecordCancel() {
            audioState = STATE_RECORD_NORMAL;
            showByState(audioState);
        }

        @Override
        public void onRecordReachedMaxTime(int i) {
            double dura = (double) i / 1000;
            int max = (int) Math.round(dura);
            toast("录音时间过长");
        }
    };


    private void showByState(int state) {
        if (state == STATE_RECORD_NORMAL) {
            mBinding.tvState.setText("点击开始录制");
            mBinding.ivRecord.setVisibility(View.VISIBLE);
            mBinding.tryListen.setVisibility(View.GONE);
            mBinding.retryRecord.setVisibility(View.GONE);
            mBinding.ivRecording.setVisibility(View.GONE);
            mBinding.ivRecordSave.setVisibility(View.GONE);
            mBinding.ivMc.setVisibility(View.VISIBLE);
            mBinding.tvText.setVisibility(View.GONE);
            mBinding.tvChronometer.setVisibility(View.GONE);
            mBinding.tvTips.setVisibility(View.VISIBLE);
            mBinding.tvChronometer.setText("00:00");
            if (audioManager != null && audioManager.isPlaying()) {
                audioManager.stopPlay();
            }
        } else if (state == STATE_RECORD_RECORDING) {
            mBinding.tvState.setText("正在录制中...");
            mBinding.ivRecord.setVisibility(View.VISIBLE);
            mBinding.ivRecording.setVisibility(View.VISIBLE);
            mBinding.tryListen.setVisibility(View.GONE);
            mBinding.retryRecord.setVisibility(View.GONE);
            mBinding.ivRecordSave.setVisibility(View.GONE);
            mBinding.ivMc.setVisibility(View.GONE);
            mBinding.tvText.setVisibility(View.VISIBLE);
            mBinding.tvText.setText("停止");
            mBinding.tvChronometer.setVisibility(View.VISIBLE);
            mBinding.tvTips.setVisibility(View.GONE);
        } else if (state == STATE_RECORD_SUCCESS) {
            mBinding.tvChronometer.stop();
            setAudioDuration();
            mBinding.tvState.setText("已完成录制");
            mBinding.tryListen.setVisibility(View.VISIBLE);
            mBinding.ivRecord.setVisibility(View.GONE);
            mBinding.retryRecord.setVisibility(View.VISIBLE);
            mBinding.ivRecordSave.setVisibility(View.VISIBLE);
            mBinding.ivRecording.setVisibility(View.GONE);
            mBinding.ivMc.setVisibility(View.GONE);
            mBinding.tvText.setVisibility(View.VISIBLE);
            mBinding.tvText.setText("上传");
            mBinding.tvChronometer.setVisibility(View.VISIBLE);
            mBinding.tvTips.setVisibility(View.GONE);
        }
    }

    private void setAudioDuration() {
        String time;
        if (audioDura < 10) {
            time = "00:0" + audioDura;
        } else if (audioDura < 59) {
            time = "00:" + audioDura;
        } else {
            time = "01:00";
        }
        mBinding.tvChronometer.setText(time);
    }

    OnPlayListener onPlayListener = new OnPlayListener() {

        @Override
        public void onPrepared() {
            mBinding.ivTryListen.setImageResource(R.mipmap.icon_try_listen_pause);
            mBinding.tvTryListen.setText("试听中");
        }

        @Override
        public void onCompletion() {
            mBinding.ivTryListen.setImageResource(R.mipmap.icon_try_listen);
            mBinding.tvTryListen.setText("试听");
        }

        @Override
        public void onInterrupt() {
            mBinding.ivTryListen.setImageResource(R.mipmap.icon_try_listen_pause);
            mBinding.tvTryListen.setText("试听中");
        }

        @Override
        public void onError(String s) {
            mBinding.ivTryListen.setImageResource(R.mipmap.icon_try_listen);
            mBinding.tvTryListen.setText("试听");
        }

        @Override
        public void onPlaying(long l) {
            mBinding.ivTryListen.setImageResource(R.mipmap.icon_try_listen_pause);
            mBinding.tvTryListen.setText("试听中");
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_record:
                RoomInfo current = AvRoomDataManager.get().mCurrentRoomInfo;
                if (current != null) {
                    getDialogManager().showOkCancelDialog("当前正在房间无法录音，是否关闭房间？", true, new DialogManager.OkCancelDialogListener() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onOk() {
                            getMvpPresenter().exitRoom();
                        }
                    });
                } else {
                    startVoice();
                }
                break;
            case R.id.iv_recording:
                audioState = STATE_RECORD_SUCCESS;
                audioManager.stopRecord(false);
                showByState(audioState);
                break;
            case R.id.retry_record:
                audioState = STATE_RECORD_NORMAL;
                showByState(audioState);
                if (null != recorder) {
                    recorder.destroyAudioRecorder();
                    recorder = null;
                }
                break;
            case R.id.try_listen:
                if (!audioManager.isPlaying()) {
                    if (null != audioFile && audioFile.exists()) {
                        audioPlayer.setDataSource(audioFile.getPath());
                        audioManager.play();
                        mBinding.ivTryListen.setImageResource(R.mipmap.icon_try_listen_pause);
                        mBinding.tvTryListen.setText("试听中");
                    }
                } else {
                    audioManager.stopPlay();
                    mBinding.ivTryListen.setImageResource(R.mipmap.icon_try_listen);
                    mBinding.tvTryListen.setText("试听");
                }
                break;
            case R.id.iv_record_save:
                if (null != audioFile) {
                    getDialogManager().showProgressDialog(this, "请稍后...");
                    CoreManager.getCore(IFileCore.class).upload(audioFile);
                }
                break;

            case R.id.refresh:
                onRefresh();
                break;
            default:
        }
    }

    long value = -1;

    Chronometer.OnChronometerTickListener chronometerTickListener = new Chronometer.OnChronometerTickListener() {

        @Override
        public void onChronometerTick(Chronometer chronometer) {
            if (value == -1) {
                value = chronometer.getBase();
            } else {
                // timer add
                value++;
            }
            if (value > 60) {
                audioManager.stopRecord(false);
                return;
            }
            String time;
            if (value < 10) {
                time = "00:0" + value;
            } else if (value < 59) {
                time = "00:" + value;
            } else {
                time = "01:00";
            }
            chronometer.setText(time);
        }
    };

    @Override
    protected void onDestroy() {
        if (audioManager.isPlaying()) {
            audioManager.stopPlay();
        }
        if (recorder != null) {
            recorder = null;
        }
        if (onRecordCallback != null) {
            onRecordCallback = null;
        }
        if (onPlayListener != null) {
            onPlayListener = null;
        }
        if (audioPlayer != null) {
            audioPlayer.setOnPlayListener(null);
        }
        if (audioManager != null) {
            audioManager.release();
        }
        super.onDestroy();
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUpload(String url) {
        audioUrl = url;
        UserInfo user = new UserInfo();
        user.setUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        user.setUserVoice(audioUrl);
        user.setVoiceDura(audioDura);
        CoreManager.getCore(IUserCore.class).requestUpdateUserInfo(user);
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadFail() {
        toast("上传失败");
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfoUpdate(UserInfo userInfo) {
        audioState = STATE_RECORD_NORMAL;
        showByState(audioState);
        getDialogManager().dismissDialog();

        Intent intent = new Intent();
        intent.putExtra(AUDIO_FILE, audioUrl);
        intent.putExtra(AUDIO_DURA, audioDura);
        setResult(RESULT_OK, intent);
        toast("上传成功");
        finish();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfoUpdateError(String error) {
        toast(error);
        getDialogManager().dismissDialog();
    }


}
