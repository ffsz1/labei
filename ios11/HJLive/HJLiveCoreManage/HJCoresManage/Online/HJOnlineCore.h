//
//  OnlineCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJOnlineCore : BaseCore
- (void)showGitToUidFromOnline:(UserID)uid withName:(NSString *)name;
@end
