//
//  HJNotiFriendInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJNotiFriendInfo : BaseObject
@property NSInteger uid;
@property NSString *nick;
@property NSString *content;
@property NSString *avatar;
@end

NS_ASSUME_NONNULL_END
