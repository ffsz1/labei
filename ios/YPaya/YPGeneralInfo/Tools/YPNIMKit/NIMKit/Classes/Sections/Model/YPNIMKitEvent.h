//
//  YPNIMKitEvent.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "YPNIMMessageModel.h"

@interface YPNIMKitEvent : NSObject

@property (nonatomic,copy) NSString *eventName;

@property (nonatomic,strong) YPNIMMessageModel *messageModel;

@property (nonatomic,strong) id data;

@end




extern NSString *const NIMKitEventNameTapContent;
extern NSString *const NIMKitEventNameTapLabelLink;
extern NSString *const NIMKitEventNameTapAudio;
extern NSString *const NIMKitEventNameTapRobotLink;
extern NSString *const NIMKitEventNameTapRobotBlock;
extern NSString *const NIMKitEventNameTapRobotContinueSession;
