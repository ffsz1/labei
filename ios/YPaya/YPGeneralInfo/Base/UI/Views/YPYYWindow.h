//
//  YPYYWindow.h
//  YYMobile
//
//  Created by wuwei on 14/7/16.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

extern NSString * const MotionShakeDetectedNotification;

@interface YPYYWindow : UIWindow

@end

@interface UIResponder (MotionShakeRecognizer)

- (void)addMotionRecognizerWithAction:(SEL)action;

- (void)removeMotionRecognizer;

@end