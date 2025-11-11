package com.vslk.lbgx.ui.me.user.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.vslk.lbgx.room.audio.activity.AudioRecordActivity;
import com.vslk.lbgx.ui.common.permission.PermissionActivity;
import com.vslk.lbgx.ui.me.user.adapter.UserPhotoAdapter;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.UIHelper;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.hncxco.library_ui.widget.AppToolBar;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.audio.AudioPlayAndRecordManager;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.file.IFileCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;
import com.tongdaxing.xchat_framework.util.util.file.JXFileUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 编辑页面
 *
 * @author zhouxiangfeng
 * @date 2017/5/23
 */
public class UserInfoModifyActivity extends TakePhotoActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, UserPhotoAdapter.ImageClickListener {

    private static final String CAMERA_PREFIX = "picture_";
    private String avatar;
    private ImageView civAvatar;
    private DatePickerDialog datePickerDialog;
    private TextView tvBirth;
    private TextView tvNick;
    private TextView tvSex;
    private TextView tvDesc;
    private UserInfo mUserInfo;
    private long userId;
    private TextView tvVoice;
    private String audioFileUrl;
    private AudioPlayAndRecordManager audioManager;
    private AudioPlayer audioPlayer;
    private String birth;
    private LinearLayout tvRecordMyVoice;
    private AppToolBar mToolBar;

    private List<ButtonItem> buttonItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_modify);
        setStatusBar();
        findViews();
        init();
        onSetListener();

        userId = getIntent().getLongExtra("userId", 0);
        mUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(userId);
        if (mUserInfo != null) {
            initData(mUserInfo);
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        if (info.getUid() == userId) {
            mUserInfo = info;
            initData(mUserInfo);
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo info) {
        if (info.getUid() == userId) {
            mUserInfo = info;
            initData(mUserInfo);
            getDialogManager().dismissDialog();
        }
    }

    private int playState = PlayState.NORMAL;

    @Override
    public void click(int position, UserPhoto userPhoto) {
        if (userPhoto != null) {
            UIHelper.showModifyPhotosAct(UserInfoModifyActivity.this, userId);
        }
    }

    @Override
    public void addClick() {

    }


    private interface PlayState {
        int PLAYING = 1;
        int NORMAL = 0;
    }

    private void onSetListener() {
        tvVoice.setOnClickListener(v -> {
            TextView tvVoice = (TextView) v;
            voiceClicked(tvVoice);
        });
        back(mToolBar);
    }

    private void voiceClicked(TextView tvVoice) {
        if (playState == PlayState.NORMAL) {
            playState = PlayState.PLAYING;
            setTextViewLeftDrawable(tvVoice, R.drawable.icon_play_stop);
            if (!StringUtils.isEmpty(audioFileUrl)) {
                audioPlayer.setDataSource(audioFileUrl);
                audioManager.play();
            }

        } else if (playState == PlayState.PLAYING) {
            playState = PlayState.NORMAL;
            setTextViewLeftDrawable(tvVoice, R.drawable.icon_play);
            audioManager.stopPlay();

        }
    }

    private void setTextViewLeftDrawable(TextView tvVoice, int drawId) {
        Drawable drawablePlay = getResources().getDrawable(drawId);
        drawablePlay.setBounds(0, 0, drawablePlay.getMinimumWidth(), drawablePlay.getMinimumHeight());
        tvVoice.setCompoundDrawables(drawablePlay, null, null, null);
    }

    private void initData(UserInfo userInfo) {
        if (null != userInfo) {
            audioFileUrl = userInfo.getUserVoice();
            ImageLoadUtils.loadCircleImage(this, userInfo.getAvatar(), civAvatar, R.drawable.ic_default_avatar);
            birth = TimeUtil.getDateTimeString(userInfo.getBirth(), "yyyy-MM-dd");
            tvBirth.setText(birth);
            tvNick.setText(userInfo.getNick());
            tvDesc.setText(userInfo.getUserDesc());
            if (userInfo.getGender() == 1) {
                tvSex.setText("男");
            } else {
                tvSex.setText("女");
            }
            if (userInfo.getVoiceDura() > 0) {
                tvVoice.setVisibility(View.VISIBLE);
                tvVoice.setText(MessageFormat.format("{0}''", userInfo.getVoiceDura()));
            } else {
                tvVoice.setVisibility(View.GONE);
            }
        }
    }

    private void findViews() {
        tvRecordMyVoice = (LinearLayout) findViewById(R.id.tv_record_my_voice);
        civAvatar = (ImageView) findViewById(R.id.civ_avatar);
        tvBirth = (TextView) findViewById(R.id.tv_birth);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvVoice = (TextView) findViewById(R.id.tv_voice);
        tvRecordMyVoice.setOnClickListener(this);
        findViewById(R.id.layout_avatar).setOnClickListener(this);
        findViewById(R.id.layout_birth).setOnClickListener(this);
        findViewById(R.id.layout_nick).setOnClickListener(this);
        findViewById(R.id.layout_desc).setOnClickListener(this);
        findViewById(R.id.tv_record_my_album).setOnClickListener(this);
        findViewById(R.id.layout_audiorecord).setOnClickListener(this);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
    }

    private void init() {
        audioManager = AudioPlayAndRecordManager.getInstance();
        audioPlayer = audioManager.getAudioPlayer(UserInfoModifyActivity.this, null, mOnPlayListener);


        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
    }

    private void showByPlayState() {
        if (playState == PlayState.NORMAL) {
            setTextViewLeftDrawable(tvVoice, R.drawable.icon_play);
        } else if (playState == PlayState.PLAYING) {
            setTextViewLeftDrawable(tvVoice, R.drawable.icon_play_stop);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String monthstr;
        if ((month + 1) < 10) {
            monthstr = "0" + (month + 1);
        } else {
            monthstr = String.valueOf(month + 1);
        }
        String daystr;
        if ((day) < 10) {
            daystr = "0" + (day);
        } else {
            daystr = String.valueOf(day);
        }
        String birth = String.valueOf(year) + "-" + monthstr + "-" + daystr;
        UserInfo user = new UserInfo();
        user.setUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        user.setBirthStr(birth);
        requestUpdateUserInfo(user);
    }

    private void requestUpdateUserInfo(UserInfo user) {
        CoreManager.getCore(IUserCore.class).requestUpdateUserInfo(user);
    }

    public interface Method {
        /**
         * 录音
         */
        int AUDIO = 2;
        /**
         * 昵称
         */
        int NICK = 3;
        /**
         * 个人介绍
         */
        int DESC = 4;
    }


    PermissionActivity.CheckPermListener checkPermissionListener = this::takePhoto;

    private void takePhoto() {
        String mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
        File cameraOutFile = JXFileUtils.getTempFile(UserInfoModifyActivity.this, mCameraCapturingName);
        if (!cameraOutFile.getParentFile().exists()) {
            cameraOutFile.getParentFile().mkdirs();
        }
        Uri uri = Uri.fromFile(cameraOutFile);
        CompressConfig compressConfig = new CompressConfig.Builder().create();
        getTakePhoto().onEnableCompress(compressConfig, false);
        CropOptions options = new CropOptions.Builder().setWithOwnCrop(true).create();
        getTakePhoto().onPickFromCaptureWithCrop(uri, options);
    }

    private void checkPermissionAndStartCamera() {
        //低版本授权检查
        checkPermission(checkPermissionListener, R.string.ask_camera, android.Manifest.permission.CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            MLog.info(this, "return is not ok,resultCode=%d", resultCode);
            return;
        }

        if (requestCode == Method.NICK) {
            String stringExtra = data.getStringExtra(ModifyInfoActivity.CONTENTNICK);
            tvNick.setText(stringExtra);
            UserInfo user = new UserInfo();
            user.setUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
            user.setNick(stringExtra);
            CoreManager.getCore(IUserCore.class).requestUpdateUserInfo(user);

        }

        if (requestCode == Method.DESC) {
            String stringExtra = data.getStringExtra(ModifyInfoActivity.CONTENT);
            tvDesc.setText(stringExtra);
            UserInfo user = new UserInfo();
            user.setUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
            user.setUserDesc(stringExtra);
            CoreManager.getCore(IUserCore.class).requestUpdateUserInfo(user);
        }

        if (requestCode == Method.AUDIO) {
            audioFileUrl = data.getStringExtra(AudioRecordActivity.AUDIO_FILE);
            int audioDura = data.getIntExtra(AudioRecordActivity.AUDIO_DURA, 0);
            if (audioDura > 1) {
                tvVoice.setText(audioDura + "'");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_record_my_voice:
                AudioRecordActivity.start(this);
                break;
            case R.id.tv_record_my_album:
                UIHelper.showModifyPhotosAct(this, userId);
                break;
            case R.id.layout_avatar:
                initButtonItems();
                getDialogManager().showCommonPopupDialog(buttonItems, "取消", false);
                isAvatar = true;
                break;
            case R.id.layout_birth:
                if (mUserInfo != null) {
                    int year = TimeUtils.getYear(mUserInfo.getBirth());
                    int month = TimeUtils.getMonth(mUserInfo.getBirth());
                    int day = TimeUtils.getDayOfMonth(mUserInfo.getBirth());
                    datePickerDialog = DatePickerDialog.newInstance(this, year, (month - 1), day, true);
                }
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(Constants.START_BIRTH, Constants.END_BIRTH);
                datePickerDialog.show(getSupportFragmentManager(), "DATEPICKER_TAG_1");
                break;
            case R.id.layout_nick:
                UIHelper.showModifyInfoAct(this, Method.NICK, "昵称");
                break;
            case R.id.layout_desc:
                UIHelper.showModifyInfoAct(this, Method.DESC, "个性签名");
                break;
            case R.id.layout_audiorecord:
                checkPermission(() -> {
                    Intent intent = new Intent(this, AudioRecordActivity.class);
                    UserInfoModifyActivity.this.startActivityForResult(intent, Method.AUDIO);
                    isAvatar = false;
                }, R.string.ask_again, Manifest.permission.RECORD_AUDIO);

                break;
            default:
        }
    }

    private void initButtonItems() {
        if (ListUtils.isListEmpty(buttonItems)) {
            ButtonItem buttonItem = new ButtonItem("拍照上传", this::checkPermissionAndStartCamera);
            ButtonItem buttonItem1 = new ButtonItem("本地相册", () -> {
                String mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
                File cameraOutFile = JXFileUtils.getTempFile(UserInfoModifyActivity.this, mCameraCapturingName);
                if (!cameraOutFile.getParentFile().exists()) {
                    cameraOutFile.getParentFile().mkdirs();
                }
                Uri uri = Uri.fromFile(cameraOutFile);
                CompressConfig compressConfig = new CompressConfig.Builder().create();
                getTakePhoto().onEnableCompress(compressConfig, true);
                CropOptions options = new CropOptions.Builder().setWithOwnCrop(true).create();
                getTakePhoto().onPickFromGalleryWithCrop(uri, options);
            });
            buttonItems.add(buttonItem);
            buttonItems.add(buttonItem1);
        }
    }


    private boolean isAvatar = false;

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUpload(String url) {
        if (isAvatar) {
            UserInfo user = new UserInfo();
            avatar = url;
            user.setUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
            user.setAvatar(avatar);
            CoreManager.getCore(IUserCore.class).requestUpdateUserInfo(user);
        }
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadFail() {
        toast("上传失败");
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfoUpdate(UserInfo userInfo) {
        CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(userId, true);
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfoUpdateError(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @Override
    public void takeSuccess(TResult result) {
        getDialogManager().showProgressDialog(UserInfoModifyActivity.this, "请稍后");
        CoreManager.getCore(IFileCore.class).upload(new File(result.getImage().getCompressPath()));
    }

    @Override
    public void takeFail(TResult result, String msg) {
//        toast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOnPlayListener = null;
        audioManager.release();
    }

    private OnPlayListener mOnPlayListener = new OnPlayListener() {

        @Override
        public void onPrepared() {

        }

        @Override
        public void onCompletion() {
            playState = PlayState.NORMAL;
            showByPlayState();
        }

        @Override
        public void onInterrupt() {
            playState = PlayState.NORMAL;
            showByPlayState();
        }

        @Override
        public void onError(String s) {
            playState = PlayState.NORMAL;
            showByPlayState();
        }

        @Override
        public void onPlaying(long l) {

        }
    };
}
