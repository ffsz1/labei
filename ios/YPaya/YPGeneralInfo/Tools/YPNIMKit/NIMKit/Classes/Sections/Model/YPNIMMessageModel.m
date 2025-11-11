//
//  YPNIMMessageModel.m
//  YPNIMKit
//
//  Created by NetEase.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import "YPNIMMessageModel.h"
#import "YPNIMKit.h"

@interface YPNIMMessageModel()

@property (nonatomic,strong) NSMutableDictionary *contentSizeInfo;

@end

@implementation YPNIMMessageModel

@synthesize contentViewInsets  = _contentViewInsets;
@synthesize bubbleViewInsets   = _bubbleViewInsets;
@synthesize shouldShowAvatar   = _shouldShowAvatar;
@synthesize shouldShowNickName = _shouldShowNickName;
@synthesize shouldShowLeft     = _shouldShowLeft;
@synthesize avatarMargin       = _avatarMargin;
@synthesize nickNameMargin     = _nickNameMargin;
@synthesize avatarSize         = _avatarSize;

- (instancetype)initWithMessage:(NIMMessage*)message
{
    if (self = [self init])
    {
        _message = message;
        _messageTime = message.timestamp;
        _contentSizeInfo = [[NSMutableDictionary alloc] init];
    }
    return self;
}

- (void)cleanCache
{
    [_contentSizeInfo removeAllObjects];
    _contentViewInsets = UIEdgeInsetsZero;
    _bubbleViewInsets = UIEdgeInsetsZero;
}

- (NSString*)description{
    return self.message.text;
}

- (BOOL)isEqual:(id)object
{
    if (![object isKindOfClass:[YPNIMMessageModel class]])
    {
        return NO;
    }
    else
    {
        YPNIMMessageModel *model = object;
        return [self.message isEqual:model.message];
    }
}

- (CGSize)contentSize:(CGFloat)width
{
    CGSize size = [self.contentSizeInfo[@(width)] CGSizeValue];
    if (CGSizeEqualToSize(size, CGSizeZero))
    {
        [self updateLayoutConfig];
        id<YPNIMCellLayoutConfig> layoutConfig = [[YPNIMKit sharedKit] layoutConfig];
        size = [layoutConfig contentSize:self cellWidth:width];
        [self.contentSizeInfo setObject:[NSValue valueWithCGSize:size] forKey:@(width)];
    }
    return size;
}


- (UIEdgeInsets)contentViewInsets{
    if (UIEdgeInsetsEqualToEdgeInsets(_contentViewInsets, UIEdgeInsetsZero))
    {
        id<YPNIMCellLayoutConfig> layoutConfig = [[YPNIMKit sharedKit] layoutConfig];
        _contentViewInsets = [layoutConfig contentViewInsets:self];
    }
    return _contentViewInsets;
}

- (UIEdgeInsets)bubbleViewInsets{
    if (UIEdgeInsetsEqualToEdgeInsets(_bubbleViewInsets, UIEdgeInsetsZero))
    {
        id<YPNIMCellLayoutConfig> layoutConfig = [[YPNIMKit sharedKit] layoutConfig];
        _bubbleViewInsets = [layoutConfig cellInsets:self];
    }
    return _bubbleViewInsets;
}

- (void)updateLayoutConfig
{
    id<YPNIMCellLayoutConfig> layoutConfig = [[YPNIMKit sharedKit] layoutConfig];
    
    _shouldShowAvatar       = [layoutConfig shouldShowAvatar:self];
    _shouldShowNickName     = [layoutConfig shouldShowNickName:self];
    _shouldShowLeft         = [layoutConfig shouldShowLeft:self];
    _avatarMargin           = [layoutConfig avatarMargin:self];
    _nickNameMargin         = [layoutConfig nickNameMargin:self];
    _avatarSize             = [layoutConfig avatarSize:self];
}


- (BOOL)shouldShowReadLabel
{
    return _shouldShowReadLabel && self.message.isRemoteRead;
}

@end
