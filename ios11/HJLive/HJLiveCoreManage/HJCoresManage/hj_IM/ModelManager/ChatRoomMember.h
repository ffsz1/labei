//
//  ChatRoomMember.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"
#import "HJIMDefines.h"

NS_ASSUME_NONNULL_BEGIN
/**
 *  聊天室用户
 */
@interface ChatRoomMember : BaseObject
/**
 *  房间id
 */
@property (nullable,nonatomic,copy)   NSString *room_id;

/**
 *  成员账号uid
 */
@property (nullable,nonatomic,copy)   NSString *account;

/**
 *  成员昵称
 */
@property (nullable,nonatomic,copy)   NSString *nick;

/**
 *  成员头像
 */
@property (nullable,nonatomic,copy)  NSString *avatar;

/**
 *  是否在线
 */
@property (nonatomic,assign)  BOOL is_online;

/**
 *  是否禁言
 */
@property (nonatomic,assign)  BOOL is_mute;

/**
 *  最近进入聊天室时间戳（豪秒）
 */
@property (nonatomic,assign) NSTimeInterval enter_time;

/**
 是否管理
 */
@property (nonatomic,assign) BOOL is_manager;

/**
 是否是创建者
 */
@property (nonatomic,assign) BOOL is_creator;

/**
 头饰
 */
@property (nonatomic,copy) NSString *headwear_url;

/**
头饰昵称
 */
@property (nonatomic,copy) NSString *headwear_name;

/**
 座驾url
 */
@property (nonatomic,copy) NSString *car_url;

/**
 座驾名
 */
@property (nonatomic,copy) NSString *car_name;

/**
 财富等级
 */
@property (nonatomic,assign) NSInteger exper_level;

/**
 魅力等级
 */
@property (nonatomic,assign) NSInteger charm_level;

/**
 性别
 */
@property (nonatomic,assign) JXIMUserGenderType gender;

/**
 是否萌新用户（创建时间少于3天为萌新），1为萌新，0为非萌新
 */
@property (nonatomic,assign) BOOL is_new_user;

/**
 是否拉黑, 1则该用户为黑名单，0则该用户为正常
 */
@property (nonatomic,assign) BOOL is_black_list;

/**
 魅力值(本地)
 */
@property (nonatomic,copy) NSString *charmValue;

/**
 是否带帽（魅力值最高的麦位带帽）
 */
@property (nonatomic,assign) BOOL hasHat;


/**
 机器人类型
 1普通用户  2官方  3机器人
 */
@property (nonatomic,assign) NSInteger defUser;

/**
 在线人数
 */
@property (nonatomic,assign) NSInteger online_num;


@end

NS_ASSUME_NONNULL_END
