//
//  YPAssistiveTouch.h
//  YYMobile
//
//  Created by Liuyuxiang on 16-06-27.
//  Copyright (c) 2016年 Liuyuxiang. All rights reserved.
//

#import "YPAssistiveTouch.h"
#define WIDTH self.frame.size.width
#define HEIGHT self.frame.size.height
#define kScreenWidth [[UIScreen mainScreen] bounds].size.width
#define kScreenHeight [[UIScreen mainScreen] bounds].size.height

@implementation YPAssistiveTouch

- (id)initWithFrame:(CGRect)frame view:(UIView *)view
{
    if(self = [super initWithFrame:frame]){
        self.windowLevel = UIWindowLevelStatusBar-1;
        [self makeKeyAndVisible];
        
        YPYYViewController *vc = [[YPYYViewController alloc] init];
        vc.view.backgroundColor = [UIColor clearColor];
        UIImageView *floatingView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"floating_ball"]];
        [vc.view addSubview:floatingView];
        self.rootViewController = vc;
        
//        self.view = view;
//        [self addSubview:view];
        UIPanGestureRecognizer *pan = [[UIPanGestureRecognizer alloc]initWithTarget:self action:@selector(locationChange:)];
        pan.delaysTouchesBegan = YES;
        [self addGestureRecognizer:pan];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(click:)];
        self.userInteractionEnabled = YES;
        [self addGestureRecognizer:tap];
        
    }
    
    return self;
}
//改变位置
-(void)locationChange:(UIPanGestureRecognizer*)p
{
    CGPoint panPoint = [p locationInView:[[UIApplication sharedApplication] keyWindow]];
    if(p.state == UIGestureRecognizerStateBegan)
    {
        [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(changeColor) object:nil];
    }
    else if (p.state == UIGestureRecognizerStateEnded)
    {
        [self performSelector:@selector(changeColor) withObject:nil afterDelay:4.0];
    }
    if(p.state == UIGestureRecognizerStateChanged)
    {
        self.center = CGPointMake(panPoint.x, panPoint.y);
    }
    else if(p.state == UIGestureRecognizerStateEnded)
    {
        if(panPoint.x <= kScreenWidth/2)
        {
            if(panPoint.y <= 40+HEIGHT/2 && panPoint.x >= 20+WIDTH/2)
            {
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(panPoint.x, HEIGHT/2+25);
                }];
            }
            else if(panPoint.y >= kScreenHeight-HEIGHT/2-40 && panPoint.x >= 20+WIDTH/2)
            {
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(panPoint.x, kScreenHeight-HEIGHT/2-25);
                }];
            }
            else if (panPoint.x < WIDTH/2+15 && panPoint.y > kScreenHeight-HEIGHT/2)
            {
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(WIDTH/2+25, kScreenHeight-HEIGHT/2-25);
                }];
            }
            else
            {
                CGFloat pointy = panPoint.y < HEIGHT/2 ? HEIGHT/2 :panPoint.y;
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(WIDTH/2+25, pointy);
                }];
            }
        }
        else if(panPoint.x > kScreenWidth/2)
        {
            if(panPoint.y <= 40+HEIGHT/2 && panPoint.x < kScreenWidth-WIDTH/2-20 )
            {
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(panPoint.x, HEIGHT/2 + 25);
                }];
            }
            else if(panPoint.y >= kScreenHeight-40-HEIGHT/2 && panPoint.x < kScreenWidth-WIDTH/2-20)
            {
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(panPoint.x, kScreenHeight-HEIGHT/2 - 25);
                }];
            }
            else if (panPoint.x > kScreenWidth-WIDTH/2 - 15 && panPoint.y < HEIGHT/2)
            {
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(kScreenWidth-WIDTH/2 - 25, HEIGHT/2 + 25);
                }];
            }
            else
            {
                CGFloat pointy = panPoint.y > kScreenHeight-HEIGHT/2 ? kScreenHeight-HEIGHT/2 :panPoint.y;
                [UIView animateWithDuration:0.15f animations:^{
                    self.center = CGPointMake(kScreenWidth-WIDTH/2 - 25, pointy);
                }];
            }
        }
    }
}

-(void)setHidden:(BOOL)hidden
{
    [super setHidden:hidden];
    if(_assistiveDelegate && [_assistiveDelegate respondsToSelector:@selector(assistiveVisibility:)])
    {
        [_assistiveDelegate assistiveVisibility:hidden];
    }
}

//点击事件
-(void)click:(UITapGestureRecognizer*)t
{
    if(_assistiveDelegate && [_assistiveDelegate respondsToSelector:@selector(assistiveTocuhs)])
    {
        [_assistiveDelegate assistiveTocuhs];
    }
}
-(void)changeColor
{
//    [UIView animateWithDuration:2.0 animations:^{
//        _imageView.alpha = 0.3;
//    }];
}

@end
