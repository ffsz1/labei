//
//  YPIMMessage.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPIMMessage.h"

@implementation JXIMChatroomNotificationContent : YPBaseObject

@end


@implementation JXIMCustomObject : YPBaseObject

@end


@implementation JXIMSession

@end


@implementation YPIMMessage

- (instancetype)init {
    self = [super init];
    if (self) {
        _messageId = [NSString stringWithFormat:@"%lu", (unsigned long)[self hash]];
    }
    return self;
}

@end
