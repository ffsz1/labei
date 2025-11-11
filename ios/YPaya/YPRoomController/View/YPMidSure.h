//
//  YPMidSure.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserID.h"

@interface YPMidSure : NSObject
+ (void)kickUser:(UserID)beKickedUid didKickFinish:(void(^)())didKickFinish;
@end
