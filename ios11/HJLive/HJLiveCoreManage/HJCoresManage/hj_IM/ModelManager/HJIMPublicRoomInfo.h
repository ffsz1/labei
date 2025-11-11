//
//  HJIMPublicRoomInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "UserInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJIMPublicRoomInfo : BaseObject

@property (nonatomic, assign) NSInteger count;            ///< 总用户数量
@property (nonatomic, strong) NSArray<UserInfo *> *ulist; ///< 用户头像

@end

NS_ASSUME_NONNULL_END
