//
//  CommonSideMenuViewController.h
//  MySideMenu
//
//  简单的iphone两侧菜单栏实现
//  1）只适用于ios5.0及以上
//  2）支持转屏
//  3）需要作为window的rootViewController使用
//  4）未实现菜单隐藏时回弹效果
//
//  Created by daixiang on 13-4-15.
//  Copyright (c) 2013年 YY.Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

enum CommonSideViewOptions
{
    CommonSideViewOptionsCenterBlackWhenHidden = 1 << 0,
    CommonSideViewOptionsSideBlackWhenHidden = 1 << 1,
    CommonSideViewOptionsSideScaleInOut = 1 << 2,
    CommonSideViewOptionsPanOpenCloseSide = 1 << 3,
    CommonSideViewOptionsSideAboveCenter = 1 << 4,
};
typedef NSUInteger CommonSideViewOptions;

@interface CommonSideViewController : UIViewController

@property (nonatomic, strong) UIViewController *rootViewController;
@property (nonatomic, strong) UIViewController *leftViewController;
@property (nonatomic, strong) UIViewController *rightViewController;

//左侧显示宽度
@property (nonatomic, assign) CGFloat leftWidth;    //
//右侧显示宽度
@property (nonatomic, assign) CGFloat rightWidth;

@property (nonatomic, assign) NSTimeInterval showAnimationDuration;

@property (nonatomic, assign) CGFloat centerBlackAlpha;   //CommonSideViewOptionsCenterBlackWhenHidden使用，默认0.6
@property (nonatomic, assign) CGFloat sideBlackAlpha;     //CommonSideViewOptionsSideBlackWhenHidden使用，默认0.8
@property (nonatomic, assign) CGFloat sideScale;     //CommonSideViewOptionsSideScaleInOut使用，默认0.95

@property (nonatomic, assign) BOOL rotationEnabled;  //转屏
@property (nonatomic) BOOL panDisabled;  //如果设置了CommonSideViewOptionsPanOpenCloseSide，将此属性设为YES则禁止pan手势

//中间的controller，可以使用navigation或者tab
- (id)initWithRootViewController:(UIViewController *)controller options:(CommonSideViewOptions)options;
- (void)showLeftSide;
- (void)showRightSide;
- (void)hideSideView;
//- (void)setSideFullScreen;
//只用在右侧菜单显示以后，从右侧推入第一个controller。中间必须是一个navigation
- (void)rightSidePushController:(UIViewController *)controller;
//只用在右侧第一个controller的推出，恢复显示右侧菜单的打开状态
- (void)rightSidePopToRoot;

//pan手势在该view上不能跟其它pan手势同时识别
- (void)addPanExcludeView:(UIView *)view;
- (void)removePanExclueViews;
- (void)removePanExclueView:(UIView*)view;

@end

@interface UIViewController (CommonSideViewController)

@property (nonatomic, readonly, weak) CommonSideViewController *sideViewController;

@end
