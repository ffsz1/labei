//
//  YPIMDefines.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#ifndef HJIMDefines_h
#define HJIMDefines_h

typedef NS_ENUM(NSInteger, JXIMPageType) { ///< socket所在页面
    JXIMPageTypeiOS                 = 1,   ///< iOS
    JXIMPageTypeAndroid             = 2,   ///< 安卓
    JXIMPageTypeiOSMiniPrograms     = 3,   ///< iOS小程序
    JXIMPageTypeAndroidMiniPrograms = 4,   ///< 安卓小程序
};

typedef NS_ENUM(NSInteger, JXIMBlacklistMarkActionType) { ///< 黑名单操作类型
    JXIMBlacklistMarkActionTypeRemove = 0,                ///< 移除
    JXIMBlacklistMarkActionTypeAdd    = 1,                ///< 添加
};

typedef NS_ENUM(NSInteger, JXIMRoomManagerMarkActionType) { ///< 聊天室管理员操作类型
    JXIMRoomManagerMarkActionTypeRemove = 0,                ///< 移除
    JXIMRoomManagerMarkActionTypeAdd    = 1,                ///< 添加
};

typedef NS_ENUM(NSInteger, JXIMRoomerStatus) { ///< 房主状态
    JXIMRoomerStatusOffline,                   ///< 不在线
    JXIMRoomerStatusOnline,                    ///< 在线
};

typedef NS_ENUM(NSInteger, JXIMRoomMicPostionState) { ///< 麦位状态
    JXIMRoomMicPostionStateUnlock = 0,                ///< 开锁
    JXIMRoomMicPostionStateLock   = 1,                ///< 闭锁
};

typedef NS_ENUM(NSInteger, JXIMRoomMicState) { ///< 麦状态
    JXIMRoomMicStateOpen  = 0,                 ///< 开麦
    JXIMRoomMicStateClose = 1,                 ///< 闭麦
};

typedef NS_ENUM(NSInteger, JXIMUserGenderType) { ///< 用户性别类型
    JXIMUserGenderTypeMale   = 1,                ///< 男
    JXIMUserGenderTypeFemale = 2,                ///< 女
};

typedef NS_ENUM(NSInteger, JXIMPushRoomMicUpdateActionType) { ///< 房间同步修改坑位操作类型
    JXIMPushRoomMicUpdateActionTypeUpdate = 1,                ///< 更新
    JXIMPushRoomMicUpdateActionTypeRemove = 2,                ///< 删除
};

typedef NS_ENUM(NSInteger, JXIMUserKickedReasonType) { ///< 踢出原因
    JXIMUserKickedReasonTypeRoomer    = 1,             ///< 管理员房主踢出房间
    JXIMUserKickedReasonTypeBlacklist = 2,             ///< 拉黑名单踢出
};

typedef NS_ENUM(NSInteger, JXIMRoomAudioLevel) { ///< 声音质量
    JXIMRoomAudioLevelNormal      = 1,           ///< 普通
    JXIMRoomAudioLevelHighQuality = 2,           ///< 高音质
};

typedef NS_ENUM(NSInteger, JXIMRoomAudioChannel) { ///< 声音频道
    JXIMRoomAudioChannelUndefined = 0,             ///< 未定义
    JXIMRoomAudioChannelAgora     = 1,             ///< 声网
    JXIMRoomAudioChannelZego      = 2,             ///< 即构
};

/// 公屏房间Id(测试 版本)
FOUNDATION_EXTERN NSString *const JXIMPublicDebugAuditRoomId;
/// 公屏房间Id(测试普通版本)
FOUNDATION_EXTERN NSString *const JXIMPublicDebugNormalRoomId;
/// 公屏房间Id(线上 版本)
FOUNDATION_EXTERN NSString *const JXIMPublicReleaseAuditRoomId;
/// 公屏房间Id(线上普通版本)
FOUNDATION_EXTERN NSString *const JXIMPublicReleaseNormalRoomId;


#pragma mark - Client -> Server
#pragma mark - 登录/注销
/// 客户端登录IM服务器
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_LOGIN;

#pragma mark - 房间管理
/// 进入聊天室
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ENTER_ROOM;
/// 退出聊天室
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_EXIT_ROOM;
/// Http 获取聊天室成员信息
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_MEMBERS;
/// Http 获取聊天室黑名单列表
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_BLACK_MEMBERS;
/// Http 获取聊天室管理员列表
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_MANAGERS;
/// Http 根据ID获取聊天室成员信息
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_MEMBERS_BY_IDS;
/// Http 添加移除聊天室黑名单
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_MARK_BLACKLIST;
/// Http 设置/取消聊天室管理员
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_MARK_ROOM_MANAGER;
/// Http 踢除特定成员
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_KICK_MEMBER;
/// Http 添加房间用户禁言
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_MARK_MUTE_MEMBER;
/// Http 查询房间禁言用户列表
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_GET_MUTE_MEMBERS;

/// Http 房间检查是否可以推流接口
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_CHECK_PUSH_AUTH;

#pragma mark - 房间队列管理
/// 获取队列
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_Room_Queue_AllMembers;
/// 加入或更新队列
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_Room_Queue_Update;
/// 删除队列
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_Room_Queue_Poll;

#pragma mark - 房间消息管理
/// 发送非文本消息
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_SEND_MESSAGE;
/// 发送文本消息
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_SEND_TEXT;

#pragma mark - 公屏管理
/// 进入公屏聊天室
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ENTER_PUBLIC_ROOM;
/// 发送公屏聊天室消息
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_SEND_PUBLIC_ROOM_MESSAGE;
/// 获取公聊大厅信息
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_PUBLIC_ROOM_INFO;





#pragma mark - Server -> Client
#pragma mark - 登录/注销
/// 通知：服务器提示将断开socket通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_KICK_OFF;

#pragma mark - 房间管理
/// 进入聊天室
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ENTER_ROOM;
/// 退出聊天室
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_EXIT_ROOM;
/// 通知：聊天室信息更新通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_INFO_UPDATE;
/// 通知：聊天室黑名单添加通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_BLACKLIST_ADD;
/// 通知：聊天室黑名单删除通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_BLACKLIST_REMOVE;
/// 通知：管理员添加通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_MANAGER_ADD;
/// 通知：管理员删除通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_MANAGER_REMOVE;
/// 通知：踢除特定成员通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_MEMBER_KICKED;
/// 通知：禁言用户添加通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_MUTE_ADD;
/// 通知：禁言用户移除通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_MUTE_REMOVE;
/// Http 查询房间禁言用户列表
FOUNDATION_EXTERN NSString *const JX_IM_ROUTE_ROOM_REPORT_HLS_ADDRESS;

#pragma mark - 房间队列管理
/// 通知：更新坑位信息
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_MIC_UPDATE;
/// 通知：队列成员被更新通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_ROOM_QUEUE_UPDATE;

#pragma mark - 房间消息管理
/// 通知：非文本消息转发通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_SEND_MESSAGE_REPORT;
/// 通知：文本消息转发通知
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_SEND_TEXT_REPORT;

#pragma mark - 公屏管理
/// 通知：公平消息转发通知(含全服消息)
FOUNDATION_EXTERN NSString *const JX_IM_RECEIVE_PUBLIC_ROOM_MESSAGRE_NOTICE;

#endif /* HJIMDefines_h */
