//
//  HJIMDefines.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMDefines.h"

/// 公屏房间Id(测试 版本)
NSString *const JXIMPublicDebugAuditRoomId = @"1";
/// 公屏房间Id(测试普通版本)
NSString *const JXIMPublicDebugNormalRoomId = @"2";
/// 公屏房间Id(线上 版本)
NSString *const JXIMPublicReleaseAuditRoomId = @"3";
/// 公屏房间Id(线上普通版本)
NSString *const JXIMPublicReleaseNormalRoomId = @"4";


#pragma mark - 登录/注销
/// 客户端登录IM服务器
NSString *const JX_IM_ROUTE_LOGIN = @"login";

#pragma mark - 房间管理
/// 进入聊天室
NSString *const JX_IM_ROUTE_ENTER_ROOM = @"enterChatRoom";
/// 退出聊天室
NSString *const JX_IM_ROUTE_EXIT_ROOM = @"exitChatRoom";

/// Http 获取聊天室成员信息
NSString *const JX_IM_ROUTE_ROOM_MEMBERS = @"imroom/v1/fetchRoomMembers";
/// Http 获取聊天室黑名单列表
NSString *const JX_IM_ROUTE_ROOM_BLACK_MEMBERS = @"imroom/v1/fetchRoomBlackList";
/// Http 获取聊天室管理员列表
NSString *const JX_IM_ROUTE_ROOM_MANAGERS = @"imroom/v1/fetchRoomManagers";
/// Http 根据ID获取聊天室成员信息
NSString *const JX_IM_ROUTE_ROOM_MEMBERS_BY_IDS = @"imroom/v1/fetchRoomMembersByIds";
/// Http 添加移除聊天室黑名单
NSString *const JX_IM_ROUTE_ROOM_MARK_BLACKLIST = @"imroom/v1/markChatRoomBlackList";
/// Http 设置/取消聊天室管理员
NSString *const JX_IM_ROUTE_ROOM_MARK_ROOM_MANAGER = @"imroom/v1/markChatRoomManager";
/// Http 踢除特定成员
NSString *const JX_IM_ROUTE_ROOM_KICK_MEMBER = @"imroom/v1/kickMember";
/// Http 添加房间用户禁言
NSString *const JX_IM_ROUTE_ROOM_MARK_MUTE_MEMBER = @"imroom/v1/markChatRoomMute";
/// Http 查询房间禁言用户列表
NSString *const JX_IM_ROUTE_ROOM_GET_MUTE_MEMBERS = @"imroom/v1/fetchChatRoomMuteList";
/// Http 查询房间禁言用户列表
NSString *const JX_IM_ROUTE_ROOM_REPORT_HLS_ADDRESS = @"imroom/v1/reportHls";


#pragma mark - 房间检查是否可以推流接口
/// Http 房间检查是否可以推流接口
NSString *const JX_IM_ROUTE_ROOM_CHECK_PUSH_AUTH = @"imroom/v1/checkPushAuth";


#pragma mark - 房间队列管理
/// 获取队列
NSString *const JX_IM_ROUTE_Room_Queue_AllMembers = @"fetchQueue";
/// 加入或更新队列
NSString *const JX_IM_ROUTE_Room_Queue_Update = @"updateQueue";
/// 删除队列
NSString *const JX_IM_ROUTE_Room_Queue_Poll = @"pollQueue";

#pragma mark - 房间消息管理
/// 发送非文本消息
NSString *const JX_IM_ROUTE_SEND_MESSAGE = @"sendMessage";
/// 发送文本消息
NSString *const JX_IM_ROUTE_SEND_TEXT = @"sendText";

#pragma mark - 公屏管理
/// 进入公屏聊天室
NSString *const JX_IM_ROUTE_ENTER_PUBLIC_ROOM = @"enterPublicRoom";
/// 发送公屏聊天室消息
NSString *const JX_IM_ROUTE_SEND_PUBLIC_ROOM_MESSAGE = @"sendPublicMsg";
/// 获取公聊大厅信息
NSString *const JX_IM_ROUTE_PUBLIC_ROOM_INFO = @"imroom/v1/publicTitle";





#pragma mark - Server -> Client
#pragma mark - 登录/注销
/// 通知：服务器提示将断开socket通知
NSString *const JX_IM_RECEIVE_KICK_OFF = @"kickoff";

#pragma mark - 房间管理
/// 进入聊天室
NSString *const JX_IM_RECEIVE_ENTER_ROOM = @"chatRoomMemberIn";
/// 退出聊天室
NSString *const JX_IM_RECEIVE_EXIT_ROOM = @"chatRoomMemberExit";
/// 通知：聊天室信息更新通知
NSString *const JX_IM_RECEIVE_ROOM_INFO_UPDATE = @"ChatRoomInfoUpdated";
/// 通知：聊天室黑名单添加通知
NSString *const JX_IM_RECEIVE_ROOM_BLACKLIST_ADD = @"ChatRoomMemberBlackAdd";
/// 通知：聊天室黑名单删除通知
NSString *const JX_IM_RECEIVE_ROOM_BLACKLIST_REMOVE = @"ChatRoomMemberBlackRemove";
/// 通知：管理员添加通知
NSString *const JX_IM_RECEIVE_ROOM_MANAGER_ADD = @"ChatRoomManagerAdd";
/// 通知：管理员删除通知
NSString *const JX_IM_RECEIVE_ROOM_MANAGER_REMOVE = @"ChatRoomManagerRemove";
/// 通知：踢除特定成员通知
NSString *const JX_IM_RECEIVE_ROOM_MEMBER_KICKED = @"ChatRoomMemberKicked";
/// 通知：禁言用户添加通知
NSString *const JX_IM_RECEIVE_ROOM_MUTE_ADD = @"ChatRoomMemberMute";
/// 通知：禁言用户移除通知
NSString *const JX_IM_RECEIVE_ROOM_MUTE_REMOVE = @"ChatRoomMemberMuteCancel";

#pragma mark - 房间队列管理
/// 通知：更新坑位信息
NSString *const JX_IM_RECEIVE_ROOM_MIC_UPDATE = @"QueueMicUpdateNotice";
/// 通知：队列成员被更新通知
NSString *const JX_IM_RECEIVE_ROOM_QUEUE_UPDATE = @"QueueMemberUpdateNotice";

#pragma mark - 房间消息管理
/// 通知：非文本消息转发通知
NSString *const JX_IM_RECEIVE_SEND_MESSAGE_REPORT = @"sendMessageReport";
/// 通知：文本消息转发通知
NSString *const JX_IM_RECEIVE_SEND_TEXT_REPORT = @"sendTextReport";

#pragma mark - 公屏管理
/// 通知：公平消息转发通知(含全服消息)
NSString *const JX_IM_RECEIVE_PUBLIC_ROOM_MESSAGRE_NOTICE = @"sendPublicMsgNotice";
