package com.vslk.lbgx.room.audio.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.SeekBar;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.room.audio.adapter.AddMusicListAdapter;
import com.vslk.lbgx.room.audio.widget.VoiceSeekDialog;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityAddMusicListBinding;
import com.tongdaxing.xchat_core.im.login.IIMLoginClient;
import com.tongdaxing.xchat_core.player.IPlayerCore;
import com.tongdaxing.xchat_core.player.IPlayerCoreClient;
import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

/**
 * @author chenran
 * @date 2017/10/28
 */

public class AddMusicListActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private AddMusicListAdapter adapter;
    private String imgBgUrl;
    private LocalMusicInfo current;

    private ActivityAddMusicListBinding musicListBinding;

    public static void start(Context context, String imgBgUrl) {
        Intent intent = new Intent(context, AddMusicListActivity.class);
        intent.putExtra("imgBgUrl", imgBgUrl);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicListBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_music_list);
        imgBgUrl = getIntent().getStringExtra("imgBgUrl");
        initView();
        initData();

//        if (!StringUtil.isEmpty(imgBgUrl)) {
//            ImageLoadUtils.loadImageWithBlurTransformation(this, imgBgUrl, musicListBinding.imageBg);
//        }

//        View content = findViewById(android.R.id.content);
//        ViewGroup.LayoutParams params = content.getLayoutParams();
//        params.height = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean blackStatusBar() {
        return false;
    }

    private void initView() {
        musicListBinding.setClick(this);
//        voiceSeek = (SeekBar) findViewById(R.id.voice_seek);
        musicListBinding.voiceSeek.setOnSeekBarChangeListener(this);
        musicListBinding.voiceSeek.setMax(100);
        musicListBinding.voiceSeek.setProgress(CoreManager.getCore(IPlayerCore.class).getCurrentVolume());
    }

    private void initData() {
        List<LocalMusicInfo> localMusicInfoList = CoreManager.getCore(IPlayerCore.class).requestPlayerListLocalMusicInfos();
        adapter = new AddMusicListAdapter(this);
        adapter.setLocalMusicInfos(localMusicInfoList);
        musicListBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicListBinding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (localMusicInfoList == null || localMusicInfoList.size() == 0) {
            musicListBinding.recyclerView.setVisibility(View.GONE);
            musicListBinding.emptyBg.setVisibility(View.VISIBLE);
        } else {
            musicListBinding.recyclerView.setVisibility(View.VISIBLE);
            musicListBinding.emptyBg.setVisibility(View.GONE);
        }

        current = CoreManager.getCore(IPlayerCore.class).getCurrent();
        updateView();
    }

    @Override
    protected boolean needSteepStateBar() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_music_btn:
                LocalMusicListActivity.start(this, imgBgUrl);
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.music_play_pause_btn:
                int state = CoreManager.getCore(IPlayerCore.class).getState();
                if (state == IPlayerCore.STATE_PLAY) {
                    CoreManager.getCore(IPlayerCore.class).pause();
                } else if (state == IPlayerCore.STATE_PAUSE) {
                    int result = CoreManager.getCore(IPlayerCore.class).play(null);
                    if (result < 0) {
                        toast("播放失败，文件异常");
                    }
                } else {
                    int result = CoreManager.getCore(IPlayerCore.class).playNext();
                    if (result < 0) {
                        if (result == -3) {
                            toast("播放列表中还没有歌曲哦！");
                        } else {
                            toast("播放失败，文件异常");
                        }
                    }
                }
                break;
            case R.id.empty_layout_music_add:
                LocalMusicListActivity.start(this, imgBgUrl);
                break;
            case R.id.music_adjust_voice:
                VoiceSeekDialog voiceSeekDialog = new VoiceSeekDialog(this);
                voiceSeekDialog.show();
                break;
            default:
        }
    }

    private void updateView() {
        if (current != null) {

            musicListBinding.voiceSeek.setVisibility(View.VISIBLE);

            musicListBinding.songName.setText(current.getSongName());

            if (current.getArtistNames() != null && current.getArtistNames().size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < current.getArtistNames().size(); i++) {
                    String artistName = current.getArtistNames().get(i);
                    sb.append(artistName);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
//                musicListBinding.artistName.setText(sb.toString());
            } else {
//                musicListBinding.artistName.setText("");
            }

            int state = CoreManager.getCore(IPlayerCore.class).getState();
            if (state == IPlayerCore.STATE_PLAY) {
                musicListBinding.musicPlayPauseBtn.setImageResource(R.mipmap.icon_music_play_big);
            } else {
                musicListBinding.musicPlayPauseBtn.setImageResource(R.mipmap.icon_music_pause);
            }
        } else {
            musicListBinding.voiceSeek.setVisibility(View.GONE);
            musicListBinding.songName.setText("暂无歌曲播放");
//            musicListBinding.artistName.setText("");
            musicListBinding.musicPlayPauseBtn.setImageResource(R.mipmap.icon_music_pause);
        }
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onMusicPlaying(LocalMusicInfo localMusicInfo) {
        this.current = localMusicInfo;
        updateView();
        adapter.notifyDataSetChanged();
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onMusicPause(LocalMusicInfo localMusicInfo) {
        this.current = localMusicInfo;
        updateView();
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onMusicStop() {
        this.current = null;
        updateView();
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onCurrentMusicUpdate(LocalMusicInfo localMusicInfo) {
        this.current = localMusicInfo;
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onRefreshPlayerList(List<LocalMusicInfo> playerListMusicInfoList) {
        if (playerListMusicInfoList == null || playerListMusicInfoList.size() == 0) {
            musicListBinding.recyclerView.setVisibility(View.GONE);
            musicListBinding.emptyBg.setVisibility(View.VISIBLE);
            adapter.setLocalMusicInfos(null);
            adapter.notifyDataSetChanged();
        } else {
            musicListBinding.recyclerView.setVisibility(View.VISIBLE);
            musicListBinding.emptyBg.setVisibility(View.GONE);
            adapter.setLocalMusicInfos(playerListMusicInfoList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        CoreManager.getCore(IPlayerCore.class).seekVolume(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void updateVoiceValue() {
        musicListBinding.voiceSeek.setProgress(CoreManager.getCore(IPlayerCore.class).getCurrentVolume());
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onBeKickOut(ChatRoomKickOutEvent.ChatRoomKickOutReason reason) {
        finish();
    }

    @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onKickedOut(StatusCode code) {
        finish();
    }
}
