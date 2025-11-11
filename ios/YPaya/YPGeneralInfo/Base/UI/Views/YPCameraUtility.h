//
//  YPCameraUtility.h
//  YYMobile
//
//  Created by James Pend on 14-8-29.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPCameraUtility : NSObject

+ (void)checkCameraAvailable:(void(^)(void))available;

@end
