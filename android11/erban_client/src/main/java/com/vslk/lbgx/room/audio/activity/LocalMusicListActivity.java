package com.vslk.lbgx.room.audio.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.room.audio.adapter.LocalMusicListAdapter;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityLocalMusicListBinding;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.im.login.IIMLoginClient;
import com.tongdaxing.xchat_core.player.IPlayerCore;
import com.tongdaxing.xchat_core.player.IPlayerCoreClient;
import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

/**
 * Created by chenran on 2017/10/28.
 */

public class LocalMusicListActivity extends BaseActivity implements View.OnClickListener {
    private String imgBgUrl;
    private LocalMusicListAdapter adapter;
    private ActivityLocalMusicListBinding musicListBinding;
    public static void start(Context context, String imgBgUrl) {
        Intent intent = new Intent(context, LocalMusicListActivity.class);
        intent.putExtra("imgBgUrl", imgBgUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicListBinding = DataBindingUtil.setContentView(this,R.layout.activity_local_music_list);
        imgBgUrl = getIntent().getStringExtra("imgBgUrl");
        initView();
        initData();

        if (CoreManager.getCore(IPlayerCore.class).isRefresh()) {
            playFlagRotateAnim();
        }
    }

    @Override
    public boolean blackStatusBar() {
        return false;
    }

    private void initView() {
        musicListBinding.setClick(this);
    }

    private void initData() {
        List<LocalMusicInfo> localMusicInfoList = CoreManager.getCore(IPlayerCore.class).requestLocalMusicInfos();
        adapter = new LocalMusicListAdapter(this);
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
    }

    private void playFlagRotateAnim() {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_fast_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        musicListBinding.refreshBtn.startAnimation(operatingAnim);
    }

    private void stopFlagRotateAnim() {
        musicListBinding.refreshBtn.clearAnimation();
    }

    @Override
    protected boolean needSteepStateBar() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh_btn:
                if (!CoreManager.getCore(IPlayerCore.class).isRefresh()) {
                    playFlagRotateAnim();
                    toast("开始扫描...");
                    CoreManager.getCore(IPlayerCore.class).refreshLocalMusic(null);
                } else {
                    toast("正在扫描，请稍后...");
                }
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.empty_layout_music_add:
                if (!CoreManager.getCore(IPlayerCore.class).isRefresh()) {
                    playFlagRotateAnim();
                    toast("开始扫描...");
                    CoreManager.getCore(IPlayerCore.class).refreshLocalMusic(null);
                } else {
                    toast("正在扫描，请稍后...");
                }
                break;
            default:
        }
    }

    @CoreEvent(coreClientClass = IPlayerCoreClient.class)
    public void onRefreshLocalMusic(List<LocalMusicInfo> localMusicInfoList) {
        stopFlagRotateAnim();
        toast(ListUtils.isListEmpty(localMusicInfoList) ? "扫描完成，暂未发现歌曲" : "扫描完成");
        if (localMusicInfoList == null || localMusicInfoList.size() == 0) {
            musicListBinding.recyclerView.setVisibility(View.GONE);
            musicListBinding.emptyBg.setVisibility(View.VISIBLE);
            adapter.setLocalMusicInfos(null);
            adapter.notifyDataSetChanged();
        } else {
            musicListBinding.recyclerView.setVisibility(View.VISIBLE);
            musicListBinding.emptyBg.setVisibility(View.GONE);
            adapter.setLocalMusicInfos(localMusicInfoList);
            adapter.notifyDataSetChanged();
        }
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
