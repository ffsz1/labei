//
//  XCWeakTimerTarget.h
//  HJLive
//
//  Created by FF on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^XCWeakTimerHandler) (id userInfo);


@interface XCWeakTimerTarget : NSObject

@end

@interface XBDWeakTimer : NSObject

+ (NSTimer *)scheduledTimerWithTimeInterval:(NSTimeInterval)interval block:(XCWeakTimerHandler)block userInfo:(id)userInfo repeats:(BOOL)repeats;

+ (NSTimer *)scheduledTimerWithTimeInterval:(NSTimeInterval)interval target:(id)aTarget selector:(SEL)aSelector userInfo:(id)userInfo repeats:(BOOL)repeats;
@end
