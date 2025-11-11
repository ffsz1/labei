//
//  XCWeakTimerTarget.m
//  HJLive
//
//  Created by FF on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "XBDWeakTimer.h"

@interface XCWeakTimerTarget ()

@property (nonatomic, weak) id target;
@property (nonatomic, weak) NSTimer* timer;
@property (nonatomic, assign) SEL selector;
@end

@implementation XCWeakTimerTarget

- (void) fire:(NSTimer *)timer {
    if(self.target) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
        [self.target performSelector:self.selector withObject:timer.userInfo afterDelay:0.0f];
#pragma clang diagnostic pop
    } else {
        [self.timer invalidate];
    }
}

@end


@implementation XBDWeakTimer

+ (NSTimer *)scheduledTimerWithTimeInterval:(NSTimeInterval)interval target:(id)aTarget selector:(SEL)aSelector userInfo:(id)userInfo repeats:(BOOL)repeats{
    XCWeakTimerTarget* timerTarget = [[XCWeakTimerTarget alloc] init];
    timerTarget.target = aTarget;
    timerTarget.selector = aSelector;
    timerTarget.timer = [NSTimer scheduledTimerWithTimeInterval:interval target:timerTarget selector:@selector(fire:) userInfo:userInfo repeats:repeats];
    return timerTarget.timer;
}

+ (NSTimer *)scheduledTimerWithTimeInterval:(NSTimeInterval)interval block:(XCWeakTimerHandler)block userInfo:(id)userInfo repeats:(BOOL)repeats{
    NSMutableArray *userInfoArray = [NSMutableArray arrayWithObject:[block copy]];
    if (userInfo != nil) {
        [userInfoArray addObject:userInfo];
    }
    return [self scheduledTimerWithTimeInterval:interval target:self selector:@selector(timerBlockInvoke:) userInfo:[userInfoArray copy] repeats:repeats];
}

+ (void)timerBlockInvoke:(NSArray*)userInfo {
    XCWeakTimerHandler block = userInfo[0];
    id info = nil;
    if (userInfo.count == 2) {
        info = userInfo[1];
    }
    if (block) {
        block(info);
    }
}

@end
