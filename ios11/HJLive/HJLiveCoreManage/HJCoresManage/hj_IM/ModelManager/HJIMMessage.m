//
//  HJIMMessage.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMMessage.h"

@implementation JXIMChatroomNotificationContent : BaseObject

@end


@implementation JXIMCustomObject : BaseObject

@end


@implementation JXIMSession

@end


@implementation HJIMMessage

- (instancetype)init {
    self = [super init];
    if (self) {
        _messageId = [NSString stringWithFormat:@"%lu", (unsigned long)[self hash]];
    }
    return self;
}

@end
