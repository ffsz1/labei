//
//  OnlineCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPOnlineCore.h"
#import "HJOnlineCoreClient.h"

@implementation YPOnlineCore
- (void)showGitToUidFromOnline:(UserID)uid withName:(NSString *)name {
    NotifyCoreClient(HJOnlineCoreClient, @selector(showGitToUidFromOnline:withName:), showGitToUidFromOnline:uid withName:name);
}
@end
