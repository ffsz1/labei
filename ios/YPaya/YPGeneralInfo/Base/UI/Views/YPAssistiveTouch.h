//
//  YPAssistiveTouch.h
//  YYMobile
//
//  Created by Liuyuxiang on 16-06-27.
//  Copyright (c) 2016年 Liuyuxiang. All rights reserved.
//悬浮窗

#import <UIKit/UIKit.h>
@protocol AssistiveTouchDelegate;
@interface YPAssistiveTouch : UIWindow
@property (nonatomic, strong) UIView *view;
- (id)initWithFrame:(CGRect)frame view:(UIView *)view;
@property(nonatomic,unsafe_unretained)id<AssistiveTouchDelegate> assistiveDelegate;
@end

@protocol AssistiveTouchDelegate <NSObject>
@optional
//悬浮窗点击事件
-(void)assistiveTocuhs;
-(void)assistiveVisibility:(BOOL) visible;
@end
