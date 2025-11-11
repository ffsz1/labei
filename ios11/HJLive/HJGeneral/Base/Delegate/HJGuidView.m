//
//  HJGuidView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#define SCREEN_FRAME ([UIScreen mainScreen].bounds)
#define SCREEN_WIDTH ([UIScreen mainScreen].bounds.size.width)
#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)

#import "HJGuidView.h"

@interface HJGuidView()<UIScrollViewDelegate>

@end
@implementation HJGuidView
{
    UIPageControl *pageControl; //指示当前处于第几个引导页
    UIScrollView *scrollView; //用于存放并显示引导页
    UIImageView *imageViewOne;
    UIImageView *imageViewTwo;
    UIImageView *imageViewThree;
}


- (instancetype)init
{
    self = [super init];
    if (self) {
        [self initView];
    }
    return self;
}

- (void)initView
{
    scrollView=[[UIScrollView alloc]initWithFrame:SCREEN_FRAME];
    scrollView.pagingEnabled=YES;
    scrollView.bounces = NO;
    scrollView.pagingEnabled = YES;
    scrollView.showsHorizontalScrollIndicator = NO;
    scrollView.contentSize = CGSizeMake(SCREEN_WIDTH * 3, SCREEN_HEIGHT);
    scrollView.delegate = self;
    [self addSubview:scrollView];
    
    
    pageControl=[[UIPageControl alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-50, SCREEN_WIDTH, 10)];
    pageControl.numberOfPages=3;
    [self addSubview:pageControl];
    
    [self createViewOne];
    [self createViewTwo];
    [self createViewThree];
}

- (void)onImageClicked:(UITapGestureRecognizer *) rec
{
    [self removeFromSuperview];
}

-(void)createViewOne{
    
    UIView *view = [[UIView alloc] initWithFrame:SCREEN_FRAME];
    imageViewOne = [[UIImageView alloc] initWithFrame:SCREEN_FRAME];
    imageViewOne.contentMode = UIViewContentModeScaleToFill;
    imageViewOne.image = [UIImage imageNamed:guidImage_0];
    [view addSubview:imageViewOne];
    [scrollView addSubview:view];
    
}

-(void)createViewTwo{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    imageViewTwo = [[UIImageView alloc] initWithFrame:SCREEN_FRAME];
    imageViewTwo.contentMode = UIViewContentModeScaleToFill;
    imageViewTwo.image = [UIImage imageNamed:guidImage_1];
    [view addSubview:imageViewTwo];
    [scrollView addSubview:view];
    
}

-(void)createViewThree{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH*2, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    imageViewThree = [[UIImageView alloc] initWithFrame:SCREEN_FRAME];
    imageViewThree.contentMode = UIViewContentModeScaleToFill;
    imageViewThree.image = [UIImage imageNamed:guidImage_2];
    [view addSubview:imageViewThree];
    
    UIImageView *startLogo = [[UIImageView alloc] initWithImage:[UIImage imageNamed:guid_start_logo]];
    startLogo.center = CGPointMake(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 90);
    startLogo.userInteractionEnabled = YES;
    UITapGestureRecognizer *rec = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onStartBtnClicked:)];
    [startLogo addGestureRecognizer:rec];
    [view addSubview:startLogo];
    [scrollView addSubview:view];
}

- (void)onStartBtnClicked:(id)tap
{
    [UIView animateWithDuration:0.7f animations:^{
        scrollView.alpha = 0;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}

-(void)scrollViewDidScroll:(UIScrollView *)sv{
    // 计算当前在第几页
    pageControl.currentPage = (NSInteger)(sv.contentOffset.x / [UIScreen mainScreen].bounds.size.width);
}

@end
