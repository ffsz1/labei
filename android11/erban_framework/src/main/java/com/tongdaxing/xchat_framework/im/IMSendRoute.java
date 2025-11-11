package com.tongdaxing.xchat_framework.im;

public interface IMSendRoute {
    String login = "login";
    String heartbeat = "heartbeat";
    String enterChatRoom = "enterChatRoom";
    String exitChatRoom = "exitChatRoom";
    String exitPublicRoom = "exitPublicRoom";
    String fetchRoomInfo = "fetchRoomInfo";
    String fetchRoomMembers = "fetchRoomMembers";
    String fetchRoomMembersByIds = "fetchRoomMembersByIds";
    String markChatRoomBlackList = "markChatRoomBlackList";
    String markChatRoomManager = "markChatRoomManager";
    String kickMember = "kickMember";
    String updateQueue = "updateQueue";
    String pollQueue = "pollQueue";
    String fetchQueue = "fetchQueue";
    String sendMessage = "sendMessage";
    String sendText = "sendText";
    String enterPublicRoom = "enterPublicRoom";
    String sendPublicMsg = "sendPublicMsg";

  }
