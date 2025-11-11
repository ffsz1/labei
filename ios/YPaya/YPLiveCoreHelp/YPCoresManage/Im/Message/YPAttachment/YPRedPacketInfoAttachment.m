//
//  YPRedPacketInfoAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRedPacketInfoAttachment.h"

@implementation YPRedPacketInfoAttachment

- (NSString *)cellContent:(NIMMessage *)message {
    return @"YPRecPacketConentMessageView";
}


- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    
    return CGSizeMake(200, 50);
}


@end
