//
//  YPAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
typedef enum {
    Custom_Noti_Header_Room_Tip = 2, //房间提示
    Custom_Noti_Header_Gift = 3,//礼物
    Custom_Noti_Header_Micro = 4,//麦序
    Custom_Noti_Header_Account = 5,//账户
    Custom_Noti_Header_CustomMsg = 6, //云信自定义消息
    Custom_Noti_Header_PhoneCall = 7,//密聊订单
    Custom_Noti_Header_News = 10,//推文
    Custom_Noti_Header_RedPacket = 11, //福利
    Custom_Noti_Header_Queue = 8, //队列
    Custom_Noti_Header_Face = 9, //表情
    Custom_Noti_Header_ALLMicroSend = 12, //全麦送
    Custom_Noti_Header_Turntable = 13, //zhuanpan
    Custom_Noti_Header_ALLChannelSend = 14, //全服推送
    Custom_Noti_Header_NotiFriendChat = 15, //公屏聊天
    Custom_Noti_Header_Winning = 16, //中奖
    Custom_Noti_Header_WaitQueue = 17, //重置麦序队列
    Custom_Noti_Header_LongZhu = 18, //龙珠
    Custom_Noti_Header_PK = 19, //PK
    Custom_Noti_Header_NotiInviteRoom = 20, //邀请进去房间
    Custom_Noti_Header_SecretGift = 31, //爆出神秘礼物
    Custom_Noti_Header_UploadDaily = 52, //上传日志
    Custom_Noti_Header_Charm = 33, //魅力值
    Custom_Noti_Header_Mora = 34, //猜拳
    Custom_Noti_Header_Wanfa = 35, //玩法
    Custom_Noti_Header_ClearCharmValue = 161,//清空魅力值
    Custom_Noti_Header_Set_Second_Manager_Open = 83, //设置管理员
    Custom_Noti_Header_Set_Second_Manager_Close = 84, //取消管理员
    Custom_Noti_Header_Playcall = 163, //打call类型
    Custom_Noti_Header_ChargeRoomName = 162, //房间名字
    Custom_Noti_Header_CHANGE_ROOM_LOCK = 159,//锁房
    Custom_Noti_Header_CHANGE_ROOM_NO_LOCK = 160,//取消锁房
    
    
} CustomNotificationHeader;

typedef enum {
    Custom_Noti_Header_Room_Tip_ShareRoom = 21, //分享房间
    Custom_Noti_Header_Room_Tip_Attentent_Owner = 22, //关注房主
}CutomRoomSubTip;

typedef enum {
    Custom_Noti_Sub_NewRedPacket = 111, //福利
}CustomNotificationRedPacket;

typedef enum {
    Custom_Noti_Sub_Micro_Invite = 411,//房主邀请上麦
    Custom_Noti_Sub_Micro_Accept = 412,//用户同意上麦
    Custom_Noti_Sub_Micro_OwnerKickUser = 413,//房主踢用户下麦
    Custom_Noti_Sub_Micro_UserLeft = 415,//麦序更新
}CustomNotificationSubMicro;

typedef enum {
    Custom_Noti_Sub_Account_Changed = 51,//账户余额金币变更
}CustomNotificationSubAccount;

typedef enum {
    Custom_Noti_Sub_News = 101,//推文消息
}CustomNotificationSubNews;

typedef enum {
    Custom_Noti_Sub_Queue_Invite = 81,//邀请上麦
    Custom_Noti_Sub_Queue_Kick = 82, //踢下麦
}CustomNotificationSubQueue;

typedef enum {
    Custom_Noti_Sub_Gift_Send = 31,//发送礼物
}CustomNotificationSubGift;

typedef enum {
    Custom_Noti_Sub_Face_Send = 91, //表情
}CustomNotificationSubFace;

typedef enum {
    Custom_Noti_Sub_Online_alert = 61, //主播上线
}CustomNotificationSubOnline;

typedef enum {
    Custom_Noti_Sub_AllMicroSend = 121, //全麦送
}CustomNotificationSubAllMicroSend;

typedef enum {
    Custom_Noti_Sub_Turntable = 131, //zhuanpan推送
}CustomNotificationSubTurntable;


typedef enum {
    Custom_Noti_Sub_LongZhu_Supei = 23, //速配
    Custom_Noti_Sub_LongZhu_Choose = 24, //选择
    Custom_Noti_Sub_LongZhu_cancel = 25, //取消解签

}CustomNotificationSubLongZhu;

typedef enum {
    Custom_Noti_Sub_Mora_send = 1, //发起猜拳
    Custom_Noti_Sub_Mora_result = 2, //猜拳结果
    Custom_Noti_Sub_Mora_ping = 3, //猜拳结果平
}CustomNotificationSubMora;

typedef enum {
    Custom_Noti_Header_Room_PK_Start = 27, //pk发起
    Custom_Noti_Header_Room_PK_End = 28, //pk结束
    Custom_Noti_Header_Room_PK_Cancel = 25, //pk取消
    Custom_Noti_Header_Room_PK_Add = 26, //加pk值
}CutomRoomSubPK;

typedef NS_ENUM(NSUInteger, Custom_Noti_Sub_Message) {
    Custom_Noti_Sub_Message_Open = 153, //开启公屏聊天
    Custom_Noti_Sub_Message_Close = 154,//关闭公屏聊天
};

typedef NS_ENUM(NSUInteger, Custom_Noti_Sub_Gift_Effect) {
    Custom_Noti_Sub_Gift_Effect_Open = 155, //屏蔽低价值礼物特效
    Custom_Noti_Sub_Gift_Effect_Close = 156,//开启低价值礼物特效
};

typedef NS_ENUM(NSUInteger, Custom_Noti_Sub_Car_Effect) {
    Custom_Noti_Sub_Car_Effect_Close = 157, //屏蔽坐骑特效
    Custom_Noti_Sub_Car_Effect_Open = 158,//开启坐骑特效
};

typedef NS_ENUM(NSUInteger, Custom_Noti_Header_Charm_Status) {
    Custom_Noti_Header_Charm_Update = 1,//魅力值更新
};


@interface YPAttachment : NSObject<NIMCustomAttachment>
@property (nonatomic,assign) int first;
@property (nonatomic,assign) int second;
@property (nonatomic,assign) int experLevel;
@property (nonatomic,assign) int charmLevel;
@property (nonatomic, strong) id data;

@end
