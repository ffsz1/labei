//
//  HJFamilyInfoDetail.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJFamilyInfoDetail : NSObject

/**uid*/
@property (nonatomic, assign) NSInteger uid;

/** 果果号*/
@property (nonatomic, assign) NSInteger erbanNo;

/**昵称*/
@property (nonatomic, copy) NSString *nike;

/**a头像*/
@property (nonatomic, copy) NSString *avatar;

@property (nonatomic, copy) NSString *roomId;

@property (nonatomic, copy) NSString *familyId;
@property (nonatomic, copy) NSString *familyLogo;
@property (nonatomic, copy) NSString *familyName;
@property (nonatomic, copy) NSString *familyNotice;

@property (nonatomic, copy) NSString *bgimg;
/***等级*/
@property (nonatomic, assign) NSInteger level;

/**威望*/
@property (nonatomic, assign) NSInteger prestige;

/**个性签名*/
@property (nonatomic, copy) NSString *userDesc;

/**⻆色状态 1、族长 2、管理员 3、普通成员*/
@property (nonatomic, assign) NSInteger roleStatus;

/** 0不不需要被邀请⼈人同意加⼊入群，1需要被邀请⼈人同意才可以加⼊入群*/
@property (nonatomic, assign) NSInteger magree;

/**1-禁⾔言，0-解禁*/
@property (nonatomic, assign) NSInteger mute;

/**1:关闭消息提醒，2:打开消息提醒，其他值⽆无效*/
@property (nonatomic, assign) NSInteger ope;

/// 家族成员(6位)
@property (nonatomic, strong) NSArray<UserInfo *> *familyUsersDTOS;

/// 家族成员人数
@property (nonatomic, copy) NSString *member;

/// 是否选中
@property (nonatomic, assign) BOOL isSelected;

@end

NS_ASSUME_NONNULL_END
