//
//  YPNIMUnsupportContentConfig.m
//  YPNIMKit
//
//  Created by amao on 9/15/15.
//  Copyright (c) 2015 NetEase. All rights reserved.
//

#import "YPNIMUnsupportContentConfig.h"
#import "YPNIMKit.h"

@implementation YPNIMUnsupportContentConfig
- (CGSize)contentSize:(CGFloat)cellWidth message:(NIMMessage *)message
{
    return CGSizeMake(100.f, 40.f);
}

- (NSString *)cellContent:(NIMMessage *)message
{
    return @"YPNIMSessionUnknowContentView";
}

- (UIEdgeInsets)contentViewInsets:(NIMMessage *)message
{
    NIMKitSettings *settings = message.isOutgoingMsg? [YPNIMKit sharedKit].config.rightBubbleSettings : [YPNIMKit sharedKit].config.leftBubbleSettings;
    return settings.unsupportSetting.contentInsets;
}

@end
