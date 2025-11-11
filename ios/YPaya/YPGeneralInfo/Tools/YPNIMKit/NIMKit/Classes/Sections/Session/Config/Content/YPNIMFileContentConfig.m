//
//  YPNIMFileContentConfig.m
//  YPNIMKit
//
//  Created by amao on 9/15/15.
//  Copyright (c) 2015 NetEase. All rights reserved.
//

#import "YPNIMFileContentConfig.h"
#import "YPNIMKit.h"

@implementation YPNIMFileContentConfig

- (CGSize)contentSize:(CGFloat)cellWidth message:(NIMMessage *)message
{
    return CGSizeMake(220, 110);
}

- (NSString *)cellContent:(NIMMessage *)message
{
    return @"YPNIMSessionFileTransContentView";
}

- (UIEdgeInsets)contentViewInsets:(NIMMessage *)message
{
    return [[YPNIMKit sharedKit].config setting:message].contentInsets;
}



@end
