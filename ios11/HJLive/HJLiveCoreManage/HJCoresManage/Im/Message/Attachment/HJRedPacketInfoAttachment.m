//
//  HJRedPacketInfoAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRedPacketInfoAttachment.h"

@implementation HJRedPacketInfoAttachment

- (NSString *)cellContent:(NIMMessage *)message {
    return @"HJRecPacketConentMessageView";
}


- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    
    return CGSizeMake(200, 50);
}


@end
