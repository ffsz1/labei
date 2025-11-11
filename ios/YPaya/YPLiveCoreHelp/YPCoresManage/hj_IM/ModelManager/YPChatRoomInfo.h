//
//  RoomInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserInfo.h"

#import "YPBaseObject.h"

#import "YPIMDefines.h"

typedef enum {
    RoomType_Game = 3   //轰趴房
}RoomType;

@interface YPChatRoomInfo : YPBaseObject

@property(nonatomic, assign) UserID uid; ///< 房间创建者uid
/**
 * 是否砸蛋厅 1，是；2，否
 */
@property (nonatomic, assign) NSInteger giftDrawEnable;

@property(nonatomic, copy) NSString *title;     ///< 房间名称
@property(nonatomic, assign) NSInteger roomId;    ///< 房间id
@property(nonatomic, copy) NSString *roomPwd;     ///< 进房间密码
@property(nonatomic, assign) BOOL valid;          ///< 房间是否有效
@property(nonatomic, assign) RoomType type;       ///< 房间类型
@property (nonatomic, copy) NSString *avatar;     ///< 房间头像图（缩略图）
@property(nonatomic, copy) NSString *roomDesc;    ///< 房间话题
@property(nonatomic, copy) NSString *roomNotice;  ///< 房间公告
@property(nonatomic, copy) NSString *backPic;     ///< 背景图ID
@property(nonatomic, copy) NSString *defBackpic;  ///< 默认背景
@property(nonatomic, copy) NSString *backPicUrl;  ///< 背景图url
@property(nonatomic, assign) long openTime;       ///< 开房时间
@property(nonatomic, assign) long createTime;     ///< 创建时间
@property(nonatomic, assign) long updateTime;     ///< 更新时间
@property(nonatomic, assign) NSInteger onlineNum; ///< 房间实时在线人数，用户做排序，定时任务实时更新
@property (nonatomic, copy) NSString *playInfo;   ///< 房间玩法介绍

@property(nonatomic, assign) int tagId;       ///< 标签id
@property(nonatomic, copy) NSString *tagPict; ///< 标签图片
@property(nonatomic, copy) NSString *roomTag; ///< 房间标签

@property(nonatomic, assign) JXIMRoomerStatus operatorStatus; ///< 房主离开或者在房间状态
@property (nonatomic, assign) NSInteger isPermitRoom; ///< 是否是牌照房1是牌照房，2不是牌照房（只有牌照房才能上热门首页）

@property (nonatomic, assign) BOOL isExceptionClose;   ///< 是否是异常关闭
@property (nonatomic, assign) BOOL exceptionCloseTime; ///< 异常关闭时间

@property (nonatomic, assign) long recomSeq; ///< 推荐权重
@property (nonatomic, assign) BOOL canShow;  ///< 配置展示或者不展示，1：展示，0：不展示

@property (nonatomic, strong) NSArray<NSNumber *> *hideFace;; ///< 需要隐藏表情
@property (nonatomic, assign) BOOL giftEffectSwitch;          ///< 礼物特效，默认关闭过滤（0关1开）
@property (nonatomic, assign) BOOL giftCardSwitch;          ///< 坐骑特效，默认关闭过滤（0关1开）

@property (nonatomic, assign) BOOL publicChatSwitch;          ///< 公聊默认关闭过滤（0关1开）
@property (nonatomic, assign) JXIMRoomAudioLevel audioLevel;  ///< 声音质量
@property (nonatomic, assign) JXIMRoomAudioChannel audioChannel; ///< 声音频道

@property (nonatomic, copy) NSString *badge; ///< 角标
@property(nonatomic, copy) NSString *meetingName; ///< 会议名称，与网易云交互使用
@property (nonatomic, assign) int officialRoom;   ///< 1普通房间，2官方房间
@property (nonatomic, assign) NSInteger rewardId; ///< 悬赏记录ID
@property (nonatomic, assign) double rewardMoney; ///< 悬赏金额，悬赏房专属，做冗余
@property (nonatomic, assign) NSInteger servDura; ///< 服务时长，冗余



//==Undefine==//
@property(nonatomic, assign) AccountType defUser;
@property(nonatomic, assign) NSInteger officeUser;
@property(nonatomic, assign) UserGender gender;
@property(nonatomic, assign) BOOL canSpringGift;
@property (nonatomic, assign) int abChannelType;
@property(nonatomic, assign) BOOL mute; ///< 房间禁言
@property(nonatomic, assign) NSInteger seqNo;
@property (nonatomic, assign) NSInteger factor;
@property (nonatomic, copy) NSString *erbanNo;
@property (nonatomic, assign) NSInteger charmOpen;//用于控制魅力值开关[ 0.隐藏; 1.显示 ]life-hj

@end
