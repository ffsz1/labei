//
//  GGMaskView.m
//  HJLive
//
//  Created by apple on 2018/10/15.
//  Copyright © 2018年 XC. All rights reserved.
//

#import "GGMaskView.h"

@interface GGMaskView ()<UIGestureRecognizerDelegate>

@property (nonatomic,strong) UITapGestureRecognizer *tap;

@end


@implementation GGMaskView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
    }
    return self;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    [self setUI];
}

- (void)setUI
{
    self.tag = 9999;
    self.hidden = YES;
    self.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6];
    _tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dismiss)];
    _tap.delegate = self;
    [self addGestureRecognizer:_tap];
}

- (void)show
{
    self.hidden = NO;
    self.alpha = 0;
    [UIView animateWithDuration:0.3 animations:^{
        self.alpha = 1;
    } completion:^(BOOL finished) {
        
    }];
}

- (void)dismiss
{
    if (self.dismissBlock) {
        self.dismissBlock();
    }
    
    self.alpha = 1;
    [UIView animateWithDuration:0.3 animations:^{
        self.alpha = 0;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}

- (void)disableTap
{
    [self removeGestureRecognizer:_tap];
}

//拦截点击事件，如果不是点击背景，就不响应
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if (touch.view.tag != 9999){
        return NO;
    }
    return YES;
}


@end
