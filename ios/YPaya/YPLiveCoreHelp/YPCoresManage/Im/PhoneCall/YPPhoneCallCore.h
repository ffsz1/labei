//
//  PhoneCallCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"

@interface YPPhoneCallCore : YPBaseCore

- (BOOL) isBusyLine;
- (UInt64)currentCallID;
- (void) startPhoneCall:(UserID)uid extendMsg:(NSString *)extendMsg;
- (void) responsePhoneCall:(BOOL)accept;
- (void) hangup;
- (BOOL) setMute:(BOOL)mute;
- (BOOL) setSpeaker:(BOOL)speaker;
@end
