//
//  YPYYLoadingToastView.m
//  YYMobile
//
//  Created by 武帮民 on 14-8-13.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPYYLoadingToastView.h"

#import "YYViewControllerClient.h"

const NSInteger interval = 1.5f;

@interface YPYYLoadingToastView () <YYViewControllerClient>

@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, strong) UIView * animationView;

@property (nonatomic, strong) NSString *loadingMsg;

@end

@implementation YPYYLoadingToastView

+ (instancetype)instantiateLoadingToast {
    YPYYLoadingToastView *view = [[YPYYLoadingToastView alloc] initWithLoadingView:nil];
    
    return view;
}

+ (instancetype)instantiateLoadingToastWithText:(NSString *)msg {
    YPYYLoadingToastView *view = [[YPYYLoadingToastView alloc] initWithLoadingView:msg];
    
    return view;
}

- (instancetype)initWithLoadingView:(NSString *)msg {
    self = [super init];
    
    if (self) {
        
        self.loadingMsg = msg;
        
        [self loadLoadingView];
        [self animationWithView:self.animationView];
        
        _timer = [NSTimer scheduledTimerWithTimeInterval:interval target:self selector:@selector(timerFireMethod:) userInfo:nil repeats:YES];
        
        AddCoreClient(YYViewControllerClient, self);
    }
    
    return self;
}

- (void)loadLoadingView {
    
    UIView *wrapperView = [[UIView alloc] init];
    wrapperView.backgroundColor = [UIColor clearColor];
    
//    UIImageView *centerImag = [[UIImageView alloc] init];
//    centerImag.image = [UIImage imageNamed:@"load_center_img"];
//    [centerImag sizeToFit];
    
    UIImageView *circleImag = [[UIImageView alloc] init];
    circleImag.image = [UIImage imageNamed:@"icon_loading_circle"];
    [circleImag sizeToFit];
    
    wrapperView.frame = CGRectMake(0.0, 0.0, 200, 200);
    
    CGPoint viewCenter = wrapperView.center;
    
    circleImag.center = viewCenter;
    
    [wrapperView addSubview:circleImag];
    
    if (self.loadingMsg && self.loadingMsg.length) {
        UILabel *label = [UILabel new];
        label.backgroundColor = [UIColor clearColor];
        label.text = self.loadingMsg;
        label.textAlignment = NSTextAlignmentCenter;
        label.textColor = [UIColor whiteColor];
        label.font = [UIFont systemFontOfSize:13];
        label.frame = CGRectMake(0, viewCenter.y + 20, 200, 20);
        [wrapperView addSubview:label];
    }
    
    self.animationView = circleImag;
    // 动画
    //[self animationWithView:circleImag];
    
    self.frame = wrapperView.frame;
    wrapperView.frame = self.bounds;
    
    [self addSubview:wrapperView];
}

- (void)animationWithView:(UIView *)view {
    
    CABasicAnimation* rotationAnimation;
    rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    rotationAnimation.toValue = [NSNumber numberWithFloat:M_PI * 2.0];
    rotationAnimation.duration = interval;
    rotationAnimation.cumulative = YES;
    rotationAnimation.repeatCount = 1;
    
    [view.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];
    
}

- (void)timerFireMethod:(id)sender {
    
    if (self.superview == nil) {
        [self.timer invalidate];
    } else {
        [self animationWithView:self.animationView];
    }
    
}

- (void)dealloc {
    RemoveCoreClientAll(self);

    [_timer invalidate];
    _timer = nil;
}

#pragma mark -
- (void)viewContrller:(UIViewController *)viewContrller orientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    
    self.center = [[[UIApplication sharedApplication] keyWindow] center];
}

@end
