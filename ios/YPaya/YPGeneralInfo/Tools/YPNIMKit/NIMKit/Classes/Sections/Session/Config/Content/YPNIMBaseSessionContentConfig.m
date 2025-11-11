//
//  YPNIMBaseSessionContentConfig.m
//  YPNIMKit
//
//  Created by amao on 9/15/15.
//  Copyright (c) 2015 NetEase. All rights reserved.
//

#import "YPNIMBaseSessionContentConfig.h"
#import "YPNIMTextContentConfig.h"
#import "YPNIMImageContentConfig.h"
#import "YPNIMAudioContentConfig.h"
#import "YPNIMVideoContentConfig.h"
#import "YPNIMFileContentConfig.h"
#import "YPNIMNotificationContentConfig.h"
#import "YPNIMLocationContentConfig.h"
#import "YPNIMUnsupportContentConfig.h"
#import "YPNIMTipContentConfig.h"
#import "YPNIMRobotContentConfig.h"

@interface NIMSessionContentConfigFactory ()
@property (nonatomic,strong)    NSDictionary                *dict;
@property (nonatomic,strong)    YPNIMUnsupportContentConfig   *unsupportConfig;
@end

@implementation NIMSessionContentConfigFactory

+ (instancetype)sharedFacotry
{
    static NIMSessionContentConfigFactory *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[NIMSessionContentConfigFactory alloc] init];
    });
    return instance;
}

- (instancetype)init
{
    if (self = [super init])
    {
        _dict = @{@(NIMMessageTypeText)         :       [YPNIMTextContentConfig new],
                  @(NIMMessageTypeImage)        :       [YPNIMImageContentConfig new],
                  @(NIMMessageTypeAudio)        :       [YPNIMAudioContentConfig new],
                  @(NIMMessageTypeVideo)        :       [YPNIMVideoContentConfig new],
                  @(NIMMessageTypeFile)         :       [YPNIMFileContentConfig new],
                  @(NIMMessageTypeLocation)     :       [YPNIMLocationContentConfig new],
                  @(NIMMessageTypeNotification) :       [YPNIMNotificationContentConfig new],
                  @(NIMMessageTypeTip)          :       [YPNIMTipContentConfig new],
                  @(NIMMessageTypeRobot)        :       [YPNIMRobotContentConfig new]};
        _unsupportConfig = [[YPNIMUnsupportContentConfig alloc] init];
    }
    return self;
}

- (id<NIMSessionContentConfig>)configBy:(NIMMessage *)message
{
    NIMMessageType type = message.messageType;
    id<NIMSessionContentConfig>config = [_dict objectForKey:@(type)];
    if (config == nil)
    {
        config = _unsupportConfig;
    }
    return config;
}

@end
