package com.erban.main.param.neteasepush;

/**
 * Created by liuguofu on 2017/7/13.
 */
public class Option {
    private boolean badge;//该消息是否需要计入到未读计数中，默认true
    private boolean needPushNick;//推送文案是否需要带上昵称，不设置该参数时默认false(ps:注意与sendMsg.action接口有别)
    private boolean route;//该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能)

    public boolean isBadge() {
        return badge;
    }

    public void setBadge(boolean badge) {
        this.badge = badge;
    }

    public boolean isNeedPushNick() {
        return needPushNick;
    }

    public void setNeedPushNick(boolean needPushNick) {
        this.needPushNick = needPushNick;
    }

    public boolean isRoute() {
        return route;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }
}
