//
//  YPNIMLocationContentConfig.m
//  YPNIMKit
//
//  Created by amao on 9/15/15.
//  Copyright (c) 2015 NetEase. All rights reserved.
//

#import "YPNIMLocationContentConfig.h"
#import "YPNIMKit.h"
@implementation YPNIMLocationContentConfig

- (CGSize)contentSize:(CGFloat)cellWidth message:(NIMMessage *)message
{
    return CGSizeMake(110.f, 105.f);
}

- (NSString *)cellContent:(NIMMessage *)message
{
    return @"YPNIMSessionLocationContentView";
}

- (UIEdgeInsets)contentViewInsets:(NIMMessage *)message
{
    return [[YPNIMKit sharedKit].config setting:message].contentInsets;
}

@end
