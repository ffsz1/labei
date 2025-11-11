package com.tongdaxing.xchat_core.manager.zego;

import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;

import java.util.List;

/**
 * 创建者      Created by Edwin
 * 创建时间    2018/11/4
 * 描述        语音公共基础实现类
 *
 * 更新者      Edwin
 * 更新时间
 * 更新描述
 *
 * @author Edwin
 */
public abstract class BaseAudioEngine implements IBaseAudioEvent {

    /**
     * 队列说话列表
     */
    protected List<ZegoSoundLevelInfo> speakZegoQueueMembersInfo;

    /**
     * 队列说话列表
     */
    protected List<Integer> speakQueueMembersPosition;
    /**
     * 声网为uid   即构为streamId
     */
    protected long uid;
    protected boolean isAudienceRole;
    /**
     * 麦上是否闭麦，true：闭麦，false：开麦
     */
    protected boolean isMute;
    protected boolean needRecord;
    protected boolean isRemoteMute;

    protected RoomInfo mCurrentRoomInfo;

    @Override
    public boolean isAudienceRole() {
        return isAudienceRole;
    }

    @Override
    public boolean isRemoteMute() {
        return isRemoteMute;
    }

    @Override
    public boolean isMute() {
        return isMute;
    }
}
