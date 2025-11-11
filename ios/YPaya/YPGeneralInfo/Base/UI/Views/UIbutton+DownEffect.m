////////////////////////////////////////////////////////////////////////////

//
//  UIButton+DownEffect.m
//  JCiOSProjectSample
//
//  Created by jimple on 14/7/28.
//  Copyright (c) 2014年 JimpleChen. All rights reserved.
//

#import "UIbutton+DownEffect.h"


@implementation UIButton (DownEffect)


- (void)initEffectTouch
{
    [self addTarget:self action:@selector(springTouchBtnTouchDown:) forControlEvents:UIControlEventTouchDown];
    [self addTarget:self action:@selector(springTouchBtnTouchUp:) forControlEvents:UIControlEventTouchUpInside|UIControlEventTouchUpOutside|UIControlEventTouchCancel];
    
//    [self setBackgroundImage:[UIImage imageNamed:@"shenqu_button_touch_began"] forState:UIControlStateHighlighted];
}

- (void)removeEffectTouch
{
    [self removeTarget:self action:@selector(springTouchBtnTouchDown:) forControlEvents:UIControlEventTouchDown];
    [self removeTarget:self action:@selector(springTouchBtnTouchUp:) forControlEvents:UIControlEventTouchUpInside|UIControlEventTouchUpOutside];
}

- (void)springTouchBtnTouchDown:(id)sender
{
    UIView *btn = (UIView *)sender;
    btn.alpha = 0.1;
    btn.backgroundColor = [UIColor blackColor];
}

- (void)springTouchBtnTouchUp:(id)sender
{
    UIView *btn = (UIView *)sender;
    btn.alpha = 1;
    btn.backgroundColor = [UIColor clearColor];
}

- (void)dealloc
{
    [self removeTarget:self action:@selector(springTouchBtnTouchDown:) forControlEvents:UIControlEventTouchDown];
    [self removeTarget:self action:@selector(springTouchBtnTouchUp:) forControlEvents:UIControlEventTouchUpInside|UIControlEventTouchUpOutside];
    self.alpha = 1;
    self.backgroundColor = [UIColor clearColor];
}


@end