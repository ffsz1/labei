package com.juxiao.xchat.manager.external.netease.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Option {
    /**
     1. roam: 该消息是否需要漫游，默认true（需要app开通漫游消息功能）； 
     2. history: 该消息是否存云端历史，默认true；
      3. sendersync: 该消息是否需要发送方多端同步，默认true；
      4. push: 该消息是否需要APNS推送或安卓系统通知栏推送，默认true；
      5. route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能);
      6. badge:该消息是否需要计入到未读计数中，默认true;
     7. needPushNick: 推送文案是否需要带上昵称，不设置该参数时默认true;
     8. persistent: 是否需要存离线消息，不设置该参数时默认true。
     */

    private boolean badge;//该消息是否需要计入到未读计数中，默认true
    private boolean needPushNick;//推送文案是否需要带上昵称，不设置该参数时默认false(ps:注意与sendMsg.action接口有别)
    private boolean route;//该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能)
}
