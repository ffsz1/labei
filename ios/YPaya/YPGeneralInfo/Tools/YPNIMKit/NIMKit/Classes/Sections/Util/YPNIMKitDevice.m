//
//  YPNIMKitDevice.m
//  NIM
//
//  Created by chris on 15/9/18.
//  Copyright © 2015年 Netease. All rights reserved.
//

#import "YPNIMKitDevice.h"
#import "NIMGlobalMacro.h"

#define NIMKitNormalImageSize       (1280 * 960)


@interface YPNIMKitDevice ()

@end

@implementation YPNIMKitDevice

- (instancetype)init
{
    if (self = [super init])
    {
        
    }
    return self;
}


+ (YPNIMKitDevice *)currentDevice{
    static YPNIMKitDevice *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[YPNIMKitDevice alloc] init];
    });
    return instance;
}


//图片/音频推荐参数
- (CGFloat)suggestImagePixels{
    return NIMKitNormalImageSize;
}

- (CGFloat)compressQuality{
    return 0.5;
}


- (CGFloat)statusBarHeight{
    CGFloat height = [UIApplication sharedApplication].statusBarFrame.size.height;
    return height;
}


@end
