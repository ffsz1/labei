//
//  YPFamilyModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserInfo.h"
#import "FamilyDefines.h"

@interface YPFamilyModel : NSObject

///被邀请⼈人同意⽅方式，0-需要同意(默认),1-不不需要同意
@property (nonatomic, assign) NSInteger beinvitemode;
///家族背景图
@property (nonatomic, copy) NSString *bgimg;
///家族创建时间
@property (nonatomic, assign) NSTimeInterval createTime;
///家族ID
@property (nonatomic, copy) NSString *familyId;
///家族logo
@property (nonatomic, copy) NSString *familyLogo;
///家族名称
@property (nonatomic, copy) NSString *familyName;
///家族公告
@property (nonatomic, copy) NSString *familyNotice;
/// 家族成员(前6位)
@property (nonatomic, strong) NSArray<UserInfo *> *familyUsersDTOS;
///谁可以邀请他⼈人⼊入群，0-管理理员(默认),1-所有⼈人
@property (nonatomic, assign) NSInteger invitemode;
///人数
@property (nonatomic, copy) NSString *member;
///禁⾔言类型 0:解除禁⾔言，1:禁⾔言普通成员 3:禁⾔言整个群(包括群主)
@property (nonatomic, assign) NSInteger muteType;
///族长昵称
@property (nonatomic, copy) NSString *nick;
///威望
@property (nonatomic, copy) NSString *prestige;
///排名
@property (nonatomic, assign) NSInteger ranking;
///⻆色状态
@property (nonatomic, assign) NSInteger roleStatus;
///群聊房间Id
@property (nonatomic, copy) NSString *roomId;
///时间(时间戳)
@property (nonatomic, copy) NSString *times;
///uid
@property (nonatomic, copy) NSString *uid;
///谁可以修改群资料料，0-管理理员(默认),1-所有⼈人 备注:只有群主才能修改
@property (nonatomic, assign) NSInteger uptinfomode;
/// 申请加⼊入验证 0不不⽤用验证，1需要验证,2不不允许任何⼈人加⼊入
@property (nonatomic, assign) NSInteger verification;









//个性签名
//@property (nonatomic, copy) NSString *userDesc;
///** 0不不需要被邀请⼈人同意加⼊入群，1需要被邀请⼈人同意才可以加⼊入群*/
//@property (nonatomic, assign) NSInteger magree;
//
///**1:关闭消息提醒，2:打开消息提醒，其他值⽆无效*/
//@property (nonatomic, assign) NSInteger ope;
//
///**1-禁⾔言，0-解禁*/
//@property (nonatomic, assign) NSInteger mute;
//
///**房间Id*/
//@property (nonatomic, copy) NSString *roomId;
//
///**uid*/
//@property (nonatomic, copy) NSString *uid;
//
///**申请加⼊入验证 0不不⽤用验证，1需要验证,2不不允许任何⼈人加⼊入*/
//@property (nonatomic,assign) NSInteger verification;

@end
