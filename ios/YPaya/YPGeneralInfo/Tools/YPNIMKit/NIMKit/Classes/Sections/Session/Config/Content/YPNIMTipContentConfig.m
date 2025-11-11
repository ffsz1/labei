//
//  YPNIMTipContentConfig.m
//  YPNIMKit
//
//  Created by chris on 16/1/21.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import "YPNIMTipContentConfig.h"
#import "YPNIMKitUtil.h"
#import "YPNIMKit.h"

@implementation YPNIMTipContentConfig

- (CGSize)contentSize:(CGFloat)cellWidth message:(NIMMessage *)message
{
    CGFloat messageWidth  = cellWidth;
    UILabel *label = [[UILabel alloc] init];
    label.text  = [YPNIMKitUtil messageTipContent:message];
    label.font = [UIFont boldSystemFontOfSize:NIMKit_Notification_Font_Size];
    label.numberOfLines = 0;
    CGFloat padding = [YPNIMKit sharedKit].config.maxNotificationTipPadding;
    CGSize size = [label sizeThatFits:CGSizeMake(cellWidth - 2 * padding, CGFLOAT_MAX)];
    CGFloat cellPadding = 11.f;
    CGSize contentSize = CGSizeMake(messageWidth, size.height + 2 * cellPadding);;
    return contentSize;
}

- (NSString *)cellContent:(NIMMessage *)message
{
    return @"YPNIMSessionNotificationContentView";
}

- (UIEdgeInsets)contentViewInsets:(NIMMessage *)message
{
    return [[YPNIMKit sharedKit].config setting:message].contentInsets;
}

@end
