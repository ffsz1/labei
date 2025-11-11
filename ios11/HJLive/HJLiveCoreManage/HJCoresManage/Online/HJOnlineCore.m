//
//  OnlineCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJOnlineCore.h"
#import "HJOnlineCoreClient.h"

@implementation HJOnlineCore
- (void)showGitToUidFromOnline:(UserID)uid withName:(NSString *)name {
    NotifyCoreClient(HJOnlineCoreClient, @selector(showGitToUidFromOnline:withName:), showGitToUidFromOnline:uid withName:name);
}
@end
