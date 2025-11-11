//
//  YPYYWindow.m
//  YYMobile
//
//  Created by wuwei on 14/7/16.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPYYWindow.h"

NSString * const MotionShakeDetectedNotification = @"MotionShakeDetectedNotification";

@implementation YPYYWindow

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
//    if (self) {
        // Initialization code
//    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event
{
    if (event.type == UIEventTypeMotion && event.subtype == UIEventSubtypeMotionShake) {
		[[NSNotificationCenter defaultCenter] postNotificationName:MotionShakeDetectedNotification object:self];
	}
}

@end

@implementation UIResponder (MotionShakeRecognizer)

- (void)addMotionRecognizerWithAction:(SEL)action
{
    if (action) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:action name:MotionShakeDetectedNotification object:nil];
    }
}

- (void)removeMotionRecognizer
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:MotionShakeDetectedNotification object:nil];
}

@end
