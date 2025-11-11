package com.tongdaxing.xchat_core.manager.zego;

import android.content.Context;
import android.media.AudioManager;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/14
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 */
public class AudioManagerStatus {

    public static void getState() {
        AudioManager audiomanage = (AudioManager) BasicConfig.INSTANCE.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        if (audiomanage != null) {
            int mode = audiomanage.getMode();
            LogUtil.d("AudioManager", mode + "");
        }
    }

}
