package com.tongdaxing.xchat_framework.im;

public interface IMReportRoute {
    //通知
    String ChatRoomMemberBlackRemove = "ChatRoomMemberBlackRemove";
    String ChatRoomMemberBlackAdd = "ChatRoomMemberBlackAdd";
    String chatRoomMemberIn = "chatRoomMemberIn";
    String ChatRoomTip = "ChatRoomTip";
    String kickoff = "kickoff";
    String chatRoomMemberExit = "chatRoomMemberExit";
    String ChatRoomInfoUpdated = "ChatRoomInfoUpdated";
    String ChatRoomMemberKicked = "ChatRoomMemberKicked";
    String QueueMemberUpdateNotice = "QueueMemberUpdateNotice";
    String QueueMicUpdateNotice = "QueueMicUpdateNotice";
    String sendMessageReport = "sendMessageReport";
    String updateQueue = "updateQueue";
    String ChatRoomManagerAdd = "ChatRoomManagerAdd";
    String ChatRoomManagerRemove = "ChatRoomManagerRemove";
    String sendTextReport = "sendTextReport";
    String enterPublicRoom = "enterPublicRoom";
    String sendPublicMsgNotice = "sendPublicMsgNotice";
    String sendPublicMsg = "sendPublicMsg";

}
