//
//  GGFamilyMemberModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPFamilyInfoDetail.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPFamilyMemberModel : NSObject

/**家族ID,群聊tid*/
@property (nonatomic,copy) NSString *familyId;

/**家族logo*/
@property (nonatomic,copy) NSString *familyLogo;

/**家族名称*/
@property (nonatomic,copy) NSString *familyName;

/**家族公告*/
@property (nonatomic,copy) NSString *familyNotice;




/**家族威望*/
@property (nonatomic,assign) NSInteger prestige;

/**家族成员*/
@property (nonatomic,assign) NSInteger member;

/**时间戳*/
@property (nonatomic,assign) NSInteger times;

@property (nonatomic,assign) NSInteger openTime;

/**排名*/
@property (nonatomic,assign) NSInteger ranking;

/**群聊tid*/
@property (nonatomic,assign) NSInteger roomId;

/**申请加⼊入验证 0不不⽤用验证，1需要验证,2不不允许任何⼈人加⼊入*/
@property (nonatomic,assign) NSInteger verification;

/**禁⾔言类型 0:解除禁⾔言，1:禁⾔言普通成员 3:禁⾔言整个群(包括群主)*/
@property (nonatomic,assign) NSInteger muteType;

; /**被邀请⼈人同意⽅方式，0-需要同意(默认),1-不不需要同意*/
@property (nonatomic,assign) NSInteger beinvitemode;

/**谁可以邀请他⼈人⼊入群，0-管理理员(默认),1-所有⼈人*/
@property (nonatomic,assign) NSInteger invitemode;

/**谁可以修改群资料料，0-管理理员(默认),1-所有⼈人 备注:只有群主才能修改*/
@property (nonatomic,assign) NSInteger uptinfomode;

@property (nonatomic,strong) NSArray <YPFamilyInfoDetail *>* familyTeamJoinDTOS;



@end

NS_ASSUME_NONNULL_END
