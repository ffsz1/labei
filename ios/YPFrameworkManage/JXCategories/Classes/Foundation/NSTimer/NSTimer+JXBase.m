//
//  NSTimer+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSTimer+JXBase.h"

@implementation NSTimer (JXBase)

#pragma mark - Base
+ (NSTimer *)jx_scheduledTimerWithTimeInterval:(NSTimeInterval)seconds block:(void (^)(NSTimer *timer))block repeats:(BOOL)repeats {
    return [NSTimer scheduledTimerWithTimeInterval:seconds target:self selector:@selector(_jx_executeBlock:) userInfo:[block copy] repeats:repeats];
}

+ (NSTimer *)jx_timerWithTimeInterval:(NSTimeInterval)seconds block:(void (^)(NSTimer *timer))block repeats:(BOOL)repeats {
    return [NSTimer timerWithTimeInterval:seconds target:self selector:@selector(_jx_executeBlock:) userInfo:[block copy] repeats:repeats];
}

+ (void)_jx_executeBlock:(NSTimer *)timer {
    // [timer userInfo] == NSTimerExecutionWhileFiring block
    if ([timer userInfo])  {
        void (^block)(NSTimer *timer) = (void (^)(NSTimer *timer))[timer userInfo];
        block(timer);
    }
}

@end
