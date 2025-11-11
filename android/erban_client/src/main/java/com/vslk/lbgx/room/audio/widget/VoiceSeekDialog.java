package com.vslk.lbgx.room.audio.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.player.IPlayerCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Created by chenran on 2017/12/18.
 */

public class VoiceSeekDialog extends BottomSheetDialog implements SeekBar.OnSeekBarChangeListener {
    private Context context;
    private SeekBar musicVoiceSeek;
    private SeekBar voiceSeek;
    private TextView musicVoiceNum;
    private TextView voiceNum;
    private ImageView ivCclose;

    public VoiceSeekDialog(@NonNull Context context) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_voice_seek);
        setCanceledOnTouchOutside(true);

        musicVoiceSeek = (SeekBar) findViewById(R.id.music_voice_seek);
        musicVoiceSeek.setMax(100);
        musicVoiceSeek.setProgress(CoreManager.getCore(IPlayerCore.class).getCurrentVolume());
        musicVoiceSeek.setOnSeekBarChangeListener(this);

        voiceSeek = (SeekBar) findViewById(R.id.voice_seek);
        voiceSeek.setMax(100);
        voiceSeek.setProgress(CoreManager.getCore(IPlayerCore.class).getCurrentRecordingVolume());
        voiceSeek.setOnSeekBarChangeListener(this);

        musicVoiceNum = (TextView) findViewById(R.id.music_voice_number);
        voiceNum = (TextView) findViewById(R.id.voice_number);

        musicVoiceNum.setText(CoreManager.getCore(IPlayerCore.class).getCurrentVolume() + "%");
        voiceNum.setText(CoreManager.getCore(IPlayerCore.class).getCurrentRecordingVolume() + "%");

        FrameLayout bottomSheet = (FrameLayout) findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(
                    (int) context.getResources().getDimension(R.dimen.voice_seek_dialog) +
                            (Utils.hasSoftKeys(context) ? Utils.getNavigationBarHeight(context) : 0));
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        ivCclose = ((ImageView) findViewById(R.id.iv_music_more_close));

        ivCclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceSeekDialog.this.dismiss();
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == musicVoiceSeek) {
            CoreManager.getCore(IPlayerCore.class).seekVolume(progress);
            musicVoiceNum.setText(progress + "%");
        } else {
            CoreManager.getCore(IPlayerCore.class).seekRecordingVolume(progress);
            voiceNum.setText(progress + "%");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
