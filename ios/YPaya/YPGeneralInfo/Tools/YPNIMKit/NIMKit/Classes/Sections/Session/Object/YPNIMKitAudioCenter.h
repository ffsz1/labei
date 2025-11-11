//
//  YPNIMKitAudioCenter.h
//  YPNIMKit
//
//  Created by chris on 2017/1/13.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>
@class NIMMessage;

@interface YPNIMKitAudioCenter : NSObject

@property (nonatomic,strong) NIMMessage *currentPlayingMessage;

+ (instancetype)instance;

- (void)play:(NIMMessage *)message;

@end
