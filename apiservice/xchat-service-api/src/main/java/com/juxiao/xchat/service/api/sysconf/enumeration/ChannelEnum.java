package com.juxiao.xchat.service.api.sysconf.enumeration;

/**
 * APP 渠道审核
 * @author BigCat
 */
public enum ChannelEnum {
    xbd(1),
    appstore(1),
    oppo(1),
    hwsc(1),
    ybsc(1),
    vivo(1),
    alsc(1),
    bdsc(1),
    xmsc(1),
    sxsc(1),
    mzsc(1),
    sgsc(1),
    azsc(1),
    lxsc(1),
    m360(1);

    /** 审核期间的布局 */
    private int viewType;

    ChannelEnum(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public static ChannelEnum nameOf(String channel) {
        ChannelEnum channelEnum;
        try {
            channelEnum = ChannelEnum.valueOf(channel);
        } catch (Exception e) {
            channelEnum = ChannelEnum.xbd;
        }
        return channelEnum;
    }
}
