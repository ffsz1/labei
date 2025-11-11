//
//  CommonSideMenuViewController.m
//  MySideMenu
//
//  Created by daixiang on 13-4-15.
//  Copyright (c) 2013年 YY.Inc. All rights reserved.
//

#import "CommonSideViewController.h"
#import <objc/runtime.h>
#import <QuartzCore/QuartzCore.h>

#if ! __has_feature(objc_arc)
#error This file must be compiled with ARC. Use -fobjc-arc flag (or convert project to ARC).
#endif


typedef enum
{
    SideViewStateHidden,
    SideViewStateLeftShowed,
    SideViewStateRightShowed,
} SideViewState;

// Make a private category so that we can have the setter
@interface UIViewController (CommonSideViewControllerPrivate)
- (void)setSideViewController:(CommonSideViewController *)controller;
@end


@interface CommonSideViewController () <UIGestureRecognizerDelegate>
{
    SideViewState _state;
    CGPoint _panOrigin;
    //BOOL _panDisabled;
    NSMutableArray *_panExcludeViews;
}

@property (nonatomic, strong) UIView *gestureView;
@property (nonatomic, strong) UIView *maskView;
@property (nonatomic, assign) CommonSideViewOptions options;
@property (nonatomic, strong) UITapGestureRecognizer *tapGesture;
@property (nonatomic, strong) UIPanGestureRecognizer *panGesture;

@end

@implementation CommonSideViewController

#pragma mark - view life cycle

- (id)initWithRootViewController:(UIViewController *)controller options:(CommonSideViewOptions)options
{
    if (self = [super init])
    {
        _state = SideViewStateHidden;
        _options = options;
        _leftWidth = [UIScreen mainScreen].bounds.size.width - 60;
        _rightWidth = [UIScreen mainScreen].bounds.size.width - 60;
        _showAnimationDuration = 0.3;
        _gestureView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 480)];
        //_gestureView.backgroundColor = [UIColor redColor];
        _tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesture:)];
        [_gestureView addGestureRecognizer:_tapGesture];
        _maskView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 480)];
        _centerBlackAlpha = 0.6;
        _sideBlackAlpha = 0.8;
        _sideScale = 0.95;
        
        _panExcludeViews = [[NSMutableArray alloc] init];
        
        _rootViewController = controller;
        _rootViewController.sideViewController = self;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    //NSLog(@"CommonSideViewController viewDidLoad %f %f %f %f", self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.view.frame.size.height);
    
    self.view.clipsToBounds = NO;
    [self setRootViewController:_rootViewController];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    //NSLog(@"CommonSideViewController viewWillAppear animated %d frame %f %f %f %f", animated, self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.view.frame.size.height);
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    //NSLog(@"CommonSideViewController viewDidAppear animated %d", animated);
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    //NSLog(@"CommonSideViewController viewWillDisappear animated %d", animated);
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    //NSLog(@"CommonSideViewController viewDidDisappear animated %d", animated);
}

- (void)willMoveToParentViewController:(UIViewController *)parent
{
    [super willMoveToParentViewController:parent];
    //NSLog(@"CommonSideViewController willMoveToParentViewController %p", parent);
}

- (void)didMoveToParentViewController:(UIViewController *)parent
{
    [super didMoveToParentViewController:parent];
    //NSLog(@"CommonSideViewController didMoveToParentViewController %p", parent);
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskAllButUpsideDown;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if (interfaceOrientation == UIInterfaceOrientationPortrait)
        return YES;
    
    if (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown)
        return self.rotationEnabled;
    else
        return NO;
}

- (BOOL)shouldAutorotate
{
    return self.rotationEnabled;
}

//ios 7
- (UIViewController *)childViewControllerForStatusBarStyle
{
    if (self.rootViewController)
        return self.rootViewController;
    else
        return nil;
}

- (UIViewController *)childViewControllerForStatusBarHidden
{
    if (self.rootViewController)
        return self.rootViewController;
    else
        return nil;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - public

- (void)setRootViewController:(UIViewController *)controller
{
    //非第一次
    if (self.rootViewController && self.rootViewController.view.superview && controller == self.rootViewController)
    {
        if (_state == SideViewStateLeftShowed)
            [self hideSideView];
        return;
    }
    
    CGRect frame = self.view.bounds;
    
    if (self.rootViewController && controller != self.rootViewController)
    {
        [self removeViewController:self.rootViewController];
    }
    
    if (controller)
    {
        [self addChildViewController:controller];
        controller.view.frame = frame;
        
        if (self.options & CommonSideViewOptionsSideAboveCenter)
        {
            [self.view insertSubview:controller.view atIndex:0];
            if ((_state != SideViewStateHidden) && (self.options & CommonSideViewOptionsSideScaleInOut))
                controller.view.transform = CGAffineTransformMakeScale(self.sideScale, self.sideScale);
        }
        else
        {
            [self.view addSubview:controller.view];
        }
        [controller didMoveToParentViewController:self];
        
        if (self.options & CommonSideViewOptionsPanOpenCloseSide)
        {
            if (!self.panGesture)
            {
                self.panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panGesture:)];
                self.panGesture.delegate = self;
                self.panGesture.maximumNumberOfTouches = 1;
            }
            
            UIView *rootView = controller.view;
            if ([controller isKindOfClass:[UINavigationController class]])
            {
                UINavigationController *nav = (UINavigationController *)controller;
                if ([nav.viewControllers count] > 0)
                {
                    rootView = ((UIViewController *)[nav.viewControllers objectAtIndex:0]).view;
                }
            }
//            else if ([controller isKindOfClass:[UITabBarController class]])
//            {
//                rootView = ((UITabBarController *)controller).selectedViewController
//            }
            
            if (![rootView.gestureRecognizers containsObject:self.panGesture])
            {
                [rootView addGestureRecognizer:self.panGesture];
                
            }
        }
    }
    
    _rootViewController = controller;
    [_rootViewController setSideViewController:self];
    
    if (_state == SideViewStateLeftShowed)
    {
        [self hideSideView];
        
    }
}

- (void)setLeftViewController:(UIViewController *)leftViewController
{
    _leftViewController = leftViewController;
    
    if (_leftViewController)
    {
        //预加载view
        __unused UIView *view = _leftViewController.view;
    }
    
    _leftViewController.sideViewController = self;
}

- (void)setLeftWidth:(CGFloat)leftWidth
{
    _leftWidth = leftWidth;
}

- (void)setRightViewController:(UIViewController *)rightViewController
{
    _rightViewController = rightViewController;
    if (_rightViewController)
    {
        //预加载view
        __unused UIView *view = _rightViewController.view;
    }
    
    _rightViewController.sideViewController = self;
}

- (void)setRightWidth:(CGFloat)rightWidth
{
    _rightWidth = rightWidth;
}

- (void)showLeftSide
{
    if (!self.leftViewController)
        return;
    
    //[self addSideViewController:self.leftViewController];

//    if (self.options & CommonSideViewOptionsSideAboveCenter)
//    {
//        [self show:self.leftViewController withStartOffset:-self.leftWidth endOffset:self.leftWidth animated:YES];
//    }
//    else
    {
        [self show:self.leftViewController withStartOffset:0 endOffset:self.leftWidth animated:YES];
    }
    _state = SideViewStateLeftShowed;
}

- (void)showRightSide
{
    if (!self.rightViewController)
        return;
    
    //[self addSideViewController:self.rightViewController];
    
//    if (self.options & CommonSideViewOptionsSideAboveCenter)
//    {
//        [self show:self.rightViewController withStartOffset:self.rightWidth endOffset:-self.rightWidth animated:YES];
//    }
//    else
    {
        [self show:self.rightViewController withStartOffset:0 endOffset:-self.rightWidth animated:YES];
    }
    _state = SideViewStateRightShowed;
}

- (void)hideSideView
{
    if (_state == SideViewStateLeftShowed)
    {
        [self hideSideView:self.leftViewController withEndOffset:self.leftWidth];
    }
    else if (_state == SideViewStateRightShowed)
    {
        [self hideSideView:self.rightViewController withEndOffset:-self.rightWidth];
    }
    
    
}

- (void)setPanDisabled:(BOOL)panDisabled
{
    _panDisabled = panDisabled;
    if (panDisabled)
    {
        self.panGesture.enabled = NO;
    }
    else
    {
        self.panGesture.enabled = YES;
    }
}

- (void)addPanExcludeView:(UIView *)view
{
    [_panExcludeViews addObject:view];
}

- (void)removePanExclueViews
{
    [_panExcludeViews removeAllObjects];
}

- (void)removePanExclueView:(UIView*)view
{
    [_panExcludeViews removeObject:view];
}

//- (void)setSideFullScreen
//{
//    CGRect frame = self.rootViewController.view.frame;
//    
//    [UIView animateWithDuration:0.1 animations:^{
//        if (_state == SideViewStateLeftShowed)
//        {
//            self.rootViewController.view.frame = CGRectMake(self.view.frame.size.width, 0, frame.size.width, frame.size.height);
//        }
//        else
//        {
//            self.rootViewController.view.frame = CGRectMake(-self.view.frame.size.width, 0, frame.size.width, frame.size.height);
//        }
//    }];
//}

- (void)rightSidePushController:(UIViewController *)controller
{
    if (_state == SideViewStateRightShowed && controller && [self.rootViewController isKindOfClass:[UINavigationController class]])
    {
        UINavigationController *navController = (UINavigationController *)self.rootViewController;
        
        CGRect layerFrame = self.view.bounds;
        UIView *view = [[UIView alloc] initWithFrame:layerFrame];
        
        UIGraphicsBeginImageContextWithOptions(layerFrame.size, YES, 0);
        CGContextRef ctx = UIGraphicsGetCurrentContext();
        [self.view.layer renderInContext:ctx];
        UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        view.layer.contents = (id)image.CGImage;
        [self.view addSubview:view];
        
        [navController pushViewController:controller animated:NO];
        CGRect frame = self.rootViewController.view.frame;
        frame.origin.x = frame.size.width;
        self.rootViewController.view.frame = frame;
        
        self.gestureView.hidden = YES;
        frame.origin.x = 0;
        [UIView animateWithDuration:0.25 animations:^{
            view.frame = CGRectMake(-view.frame.size.width, 0, view.frame.size.width, view.frame.size.height);
            self.rootViewController.view.frame = frame;
        } completion:^(BOOL finished) {
            [view removeFromSuperview];
        }];
        
        _panDisabled = YES;
    }
}

- (void)rightSidePopToRoot
{
    UINavigationController *navController = (UINavigationController *)self.rootViewController;
    
    CGRect layerFrame = self.view.bounds;
    UIView *view = [[UIView alloc] initWithFrame:layerFrame];
    
    UIGraphicsBeginImageContextWithOptions(layerFrame.size, YES, 0);
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    [self.view.layer renderInContext:ctx];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    view.layer.contents = (id)image.CGImage;
    [self.view addSubview:view];
    
    [navController popViewControllerAnimated:NO];
    
    CGRect frame = self.rootViewController.view.frame;
    frame.origin.x = -self.rightWidth - frame.size.width;
    self.rootViewController.view.frame = frame;
    self.gestureView.hidden = NO;
    frame.origin.x = -frame.size.width;
    self.rightViewController.view.frame = frame;
    
    frame.origin.x = 0;
    [UIView animateWithDuration:0.25 animations:^{
        view.frame = CGRectMake(view.frame.size.width, 0, view.frame.size.width, view.frame.size.height);
        self.rootViewController.view.frame = CGRectMake(-self.rightWidth, 0, frame.size.width, frame.size.height);
        self.rightViewController.view.frame = frame;
    } completion:^(BOOL finished) {
        [view removeFromSuperview];
        _panDisabled = NO;
    }];
}

#pragma mark - private

- (void)show:(UIViewController *)controller withStartOffset:(CGFloat)startOffset endOffset:(CGFloat)endOffset animated:(BOOL)animated
{
    if (!controller)
        return;
    
    if ((_state != SideViewStateHidden) && (startOffset == endOffset))
        return;
    
    if (!controller.view.superview)
    {
        [self addSideViewController:controller];
    }
    
    controller.view.hidden = NO;
    //controllerToHide.view.hidden = YES;
    
    
    
    
    if (self.options & CommonSideViewOptionsSideAboveCenter)
    {
        CGFloat width = -endOffset;
//        if (controller == self.leftViewController)
//        {
//            width = -self.leftWidth;
//        }
//        else
//        {
//            width = self.rightWidth;
//        }
        startOffset += width;
        endOffset += width;
        
        CGRect frame = controller.view.frame;
        frame.origin.x = startOffset;
        controller.view.frame = frame;
        
        if (self.gestureView.superview != controller.view)
        {
            CGRect frame = controller.view.frame;
            if (controller == self.leftViewController)
            {
                frame.origin.x = self.leftWidth;
                frame.size.width = self.view.frame.size.width - self.leftWidth;
            }
            else
            {
                frame.origin.x = 0;
                frame.size.width = self.view.frame.size.width - self.rightWidth;
            }
            self.gestureView.frame = frame;
            [controller.view addSubview:self.gestureView];
        }
        
        if (self.options & CommonSideViewOptionsCenterBlackWhenHidden)
        {
            if (self.maskView.superview != self.rootViewController.view)
            {
                self.maskView.frame = self.rootViewController.view.bounds;
                [self.rootViewController.view addSubview:self.maskView];
                self.maskView.backgroundColor = [UIColor blackColor];
            }
            
            self.maskView.alpha = self.centerBlackAlpha * (1-startOffset/width);
        }
        
        if (self.options & CommonSideViewOptionsSideScaleInOut)
        {
            CGFloat scale = self.sideScale + (1-self.sideScale)*startOffset/width;
            self.rootViewController.view.transform = CGAffineTransformMakeScale(scale, scale);
        }
        
        if (animated)
        {
            frame.origin.x = endOffset;
            [UIView animateWithDuration:MAX(0.15, self.showAnimationDuration*startOffset/width) animations:^{
                controller.view.frame = frame;
                if (self.options & CommonSideViewOptionsCenterBlackWhenHidden)
                {
                    self.maskView.alpha = self.centerBlackAlpha;
                }
                if (self.options & CommonSideViewOptionsSideScaleInOut)
                {
                    self.rootViewController.view.transform = CGAffineTransformMakeScale(self.sideScale, self.sideScale);
                }
            } completion:^(BOOL finished) {
                //[self.maskView removeFromSuperview];
                //TODO: 菜单在上面的话，pan处理有问题，暂时屏蔽
                if (self.panGesture && ![self.gestureView.gestureRecognizers containsObject:self.panGesture])
                {
                    [self.gestureView addGestureRecognizer:self.panGesture];
                }
            }];
            
            if (controller == self.leftViewController)
                _state = SideViewStateLeftShowed;
            else
                _state = SideViewStateRightShowed;
        }
    }
    else
    {
        [self showShadow];
        if (!self.gestureView.superview)
        {
            self.gestureView.frame = self.rootViewController.view.bounds;
            [self.rootViewController.view addSubview:self.gestureView];
        }
        
        if (self.options & CommonSideViewOptionsCenterBlackWhenHidden)
        {
            self.gestureView.backgroundColor = [UIColor blackColor];
            self.gestureView.alpha = 0;
        }
        CGRect frame = self.rootViewController.view.frame;
        frame.origin.x = startOffset;
        self.rootViewController.view.frame = frame;
        
        if (self.options & CommonSideViewOptionsSideBlackWhenHidden)
        {
            if (self.maskView.superview != controller.view)
            {
                self.maskView.frame = controller.view.bounds;
                [controller.view addSubview:self.maskView];
                self.maskView.backgroundColor = [UIColor blackColor];
            }
            self.maskView.alpha = self.sideBlackAlpha * (endOffset-startOffset)/endOffset;
        }
        
        if (self.options & CommonSideViewOptionsSideScaleInOut)
        {
            CGFloat scale = self.sideScale + (1-self.sideScale)*startOffset/endOffset;
            controller.view.transform = CGAffineTransformMakeScale(scale, scale);
        }
        
        if (animated)
        {
            frame.origin.x = endOffset;
            [UIView animateWithDuration:MAX(0.15, self.showAnimationDuration*(endOffset-startOffset)/endOffset) animations:^{
                self.rootViewController.view.frame = frame;
                if (self.options & CommonSideViewOptionsCenterBlackWhenHidden)
                {
                    self.gestureView.alpha = self.centerBlackAlpha;
                }
                if (self.options & CommonSideViewOptionsSideBlackWhenHidden)
                {
                    self.maskView.alpha = 0;
                }
                if (self.options & CommonSideViewOptionsSideScaleInOut)
                {
                    controller.view.transform = CGAffineTransformIdentity;
                }
            } completion:^(BOOL finished) {
                [self.maskView removeFromSuperview];
                if (self.panGesture)
                {
                    [self.gestureView addGestureRecognizer:self.panGesture];
                }
            }];
            
            if (controller == self.leftViewController)
                _state = SideViewStateLeftShowed;
            else
                _state = SideViewStateRightShowed;
        }
//        else
//        {
            //nothing to do
//        }
    }
}

- (void)hideSideView:(UIViewController *)controller withEndOffset:(CGFloat)offset
{
    if (!controller)
        return;
    
    if (self.options & CommonSideViewOptionsSideAboveCenter)
    {
        offset = -offset;
        CGRect frame = controller.view.frame;
        CGFloat x = frame.origin.x;
        frame.origin.x = offset;
        [UIView animateWithDuration:MAX(0.15, self.showAnimationDuration*x/offset) animations:^{
            
            controller.view.frame = frame;

            if (self.options & CommonSideViewOptionsCenterBlackWhenHidden)
            {
                self.maskView.alpha = 0;
            }
            if (self.options & CommonSideViewOptionsSideScaleInOut)
            {
                self.rootViewController.view.transform = CGAffineTransformIdentity;
            }
        } completion:^(BOOL finished) {
            _state = SideViewStateHidden;
            
            [self hideShadow];
            [self removeViewController:controller];
            [self.gestureView removeFromSuperview];
            [self.maskView removeFromSuperview];
        }];
    }
    else
    {
        if (self.options & CommonSideViewOptionsSideBlackWhenHidden)
        {
            if (self.maskView.superview != controller.view)
                [controller.view addSubview:self.maskView];
        }
        CGRect frame = self.rootViewController.view.frame;
        
        [UIView animateWithDuration:MAX(0.15, self.showAnimationDuration*frame.origin.x/offset) animations:^{
            
            self.rootViewController.view.frame = CGRectMake(0, frame.origin.y, frame.size.width, frame.size.height);
            if (self.options & CommonSideViewOptionsSideBlackWhenHidden)
            {
                self.maskView.alpha = self.sideBlackAlpha;
            }
            if (self.options & CommonSideViewOptionsCenterBlackWhenHidden)
            {
                self.gestureView.alpha = 0;
            }
            if (self.options & CommonSideViewOptionsSideScaleInOut)
            {
                controller.view.transform = CGAffineTransformMakeScale(self.sideScale, self.sideScale);
            }
        } completion:^(BOOL finished) {
            _state = SideViewStateHidden;
            [self.gestureView removeFromSuperview];
            [self hideShadow];
            [self removeViewController:controller];
            
        }];
    }
    
    //检查一下是否需要重新加入手势，因为在显示侧边的时候，pan手势被加入了另一个view
    if (self.options & CommonSideViewOptionsPanOpenCloseSide)
    {
        UIView *rootView = self.rootViewController.view;
        if ([self.rootViewController isKindOfClass:[UINavigationController class]])
        {
            UINavigationController *nav = (UINavigationController *)self.rootViewController;
            if ([nav.viewControllers count] > 0)
            {
                rootView = ((UIViewController *)[nav.viewControllers objectAtIndex:0]).view;
            }
        }
        
        if (![rootView.gestureRecognizers containsObject:self.panGesture])
        {
            [rootView addGestureRecognizer:self.panGesture];
        }
    }
}

- (void)removeViewController:(UIViewController *)controller
{
    controller.view.transform = CGAffineTransformIdentity;
    [controller willMoveToParentViewController:nil];
    [controller.view removeFromSuperview];
    [controller removeFromParentViewController];
}

- (void)addSideViewController:(UIViewController *)controller
{
    if (controller)
    {
        [self addChildViewController:controller];
        
        if (self.options & CommonSideViewOptionsSideAboveCenter)
        {
            CGRect frame = controller.view.frame;
            if (controller == self.leftViewController)
            {
                frame.origin.x = -self.leftWidth;
            }
            else
            {
                frame.origin.x = self.rightWidth;
            }
            controller.view.frame = frame;
//            self.gestureView.frame = self.view.bounds;
//            [self.view addSubview:self.gestureView];
//            [self.gestureView addSubview:controller.view];
//            if (![controller.view.gestureRecognizers containsObject:self.tapGesture])
//            {
//                [controller.view addGestureRecognizer:self.tapGesture];
//            }

            [self.view addSubview:controller.view];
        }
        else
        {
            [self.view insertSubview:controller.view belowSubview:self.rootViewController.view];
        }
        [controller didMoveToParentViewController:self];
        
        [controller setSideViewController:self];
    }
}

//- (void)setSideViewController:(UIViewController * __strong*)sideController withController:(UIViewController *)controller
//{
////    if (controller == *sideController)
////        return;
//    
//    if (*sideController)
//    {
//        [self removeViewController:*sideController];
//    }
//    
//    if (controller)
//    {
//        [self addChildViewController:controller];
//        [self.view insertSubview:controller.view belowSubview:self.rootViewController.view];
//        [controller didMoveToParentViewController:self];
//    }
//    
//    *sideController = controller;
//    [*sideController setSideViewController:self];
//}

- (void)showShadow
{
    self.rootViewController.view.layer.shadowOpacity = 0.8f;
    self.rootViewController.view.layer.cornerRadius = 4.0f;
    self.rootViewController.view.layer.shadowOffset = CGSizeZero;
    self.rootViewController.view.layer.shadowRadius = 4.0f;
    self.rootViewController.view.layer.shadowPath = [UIBezierPath bezierPathWithRect:self.view.bounds].CGPath;
}

- (void)hideShadow
{
    self.rootViewController.view.layer.shadowOpacity = 0;
}

- (void)tapGesture:(UITapGestureRecognizer *)gesture
{
    [self hideSideView];
}

- (void)panGesture:(UIPanGestureRecognizer *)gesture
{
    UIView *gestureView = self.rootViewController.view;
    //CGRect frame = gestureView.frame;
    
    CGPoint point = [gesture translationInView:gestureView];
    CGPoint velocity = [gesture velocityInView:gestureView];
    
//    int deltaX = abs(point.x - _panOrigin.x);
//    int deltaY = abs(point.y - _panOrigin.y);
//    
//    if (deltaY > deltaX)
//        return;
    
    //NSLog(@"x = %f",point.x);
    CGFloat offset = _panOrigin.x + point.x;
    if (offset > self.leftWidth)
        offset = self.leftWidth;
    if (offset < -self.rightWidth)
        offset = -self.rightWidth;
    
    switch (gesture.state) {
        case UIGestureRecognizerStateBegan:
            _panOrigin = gestureView.frame.origin;
            //NSLog(@"origin %f point %f", _panOrigin.x, point.x);
            break;
        case UIGestureRecognizerStateChanged:
        {
            switch (_state)
            {
                case SideViewStateHidden:
                    if ((offset < 0 && self.leftViewController.view.superview) || (offset > 0 && self.rightViewController.view.superview))
                        return;
                        
                case SideViewStateLeftShowed:
                case SideViewStateRightShowed:
                    //frame.origin.x = _panOrigin.x + point.x;
                    //if (frame.origin.x > 0 && frame.origin.x < self.leftWidth)
            
                    if (offset > 0 && offset < self.leftWidth)
                    {
                        [self show:self.leftViewController withStartOffset:offset endOffset:self.leftWidth animated:NO];
                    }
                    //else if (frame.origin.x < 0 && frame.origin.x > -self.rightWidth)
                    else if (offset < 0 && offset > -self.rightWidth)
                    {
                        [self show:self.rightViewController withStartOffset:offset endOffset:-self.rightWidth animated:NO];
                    }
                    
                    break;
//                case SideViewStateLeftShowed:
//                    if (point.x < 0)
//                    {
//                        frame.origin.x = _panOrigin.x + point.x;
//                        gestureView.frame = frame;
//                        [self show:self.leftViewController withStartOffset:frame.origin.x endOffset:self.leftOffset animated:NO];
//                    }
//                    break;
//                case SideViewStateRightShowed:
//                    if (point.x > 0)
//                    {
//                        frame.origin.x = _panOrigin.x + point.x;
//                        gestureView.frame = frame;
//                    }
//                    break;
                default:
                    break;
            }
            break;
        }
        case UIGestureRecognizerStateEnded:
        case UIGestureRecognizerStateCancelled:
        {
#define PAN_SHOW_VELOCITY       600
#define PAN_NOT_SHOW_VELOCITY   300
#define PAN_SHOW_DISTANCE       80
#define PAN_HIDE_VELOCITY       50
#define PAN_HIDE_DISTANCE       80
            
            
            
            switch (_state) {
                case SideViewStateHidden:
                {
                    if (offset < 0 && self.leftViewController.view.superview)
                        offset = 0;
                    if (offset > 0 && self.rightViewController.view.superview)
                        offset = 0;
                    
                    if (offset >= 0 && self.leftViewController.view.superview)   //左边出现
                    {
                        if (velocity.x > PAN_SHOW_VELOCITY)
                        {
                            [self show:self.leftViewController withStartOffset:offset endOffset:self.leftWidth animated:YES];
                        }
                        else if (velocity.x < -PAN_NOT_SHOW_VELOCITY)
                        {
                            [self hideSideView:self.leftViewController withEndOffset:self.leftWidth];
                        }
                        else if (offset > PAN_SHOW_DISTANCE)
                        {
                            [self show:self.leftViewController withStartOffset:offset endOffset:self.leftWidth animated:YES];
                        }
                        else
                        {
                            [self hideSideView:self.leftViewController withEndOffset:self.leftWidth];
                        }
                    }
                    else if (offset <= 0 && self.rightViewController.view.superview)
                    {
                        if (velocity.x < -PAN_SHOW_VELOCITY)
                        {
                            [self show:self.rightViewController withStartOffset:offset endOffset:-self.rightWidth animated:YES];
                        }
                        else if (velocity.x > PAN_NOT_SHOW_VELOCITY)
                        {
                            [self hideSideView:self.rightViewController withEndOffset:-self.rightWidth];
                        }
                        else if (offset < -PAN_SHOW_DISTANCE)
                        {
                            [self show:self.rightViewController withStartOffset:offset endOffset:-self.rightWidth animated:YES];
                        }
                        else
                        {
                            [self hideSideView:self.rightViewController withEndOffset:-self.rightWidth];
                        }
                    }
                    break;
                }
                case SideViewStateLeftShowed:
                    
                    if (velocity.x < -PAN_HIDE_VELOCITY || offset < self.leftWidth - PAN_HIDE_DISTANCE)
                    {
                        [self hideSideView:self.leftViewController withEndOffset:self.leftWidth];
                    }
                    else
                    {
                        [self show:self.leftViewController withStartOffset:offset endOffset:self.leftWidth animated:YES];
                    }
                    break;
                case SideViewStateRightShowed:
                    if (velocity.x > PAN_HIDE_VELOCITY || offset > -self.rightWidth + PAN_HIDE_DISTANCE)
                    {
                        [self hideSideView:self.rightViewController withEndOffset:-self.rightWidth];
                    }
                    else
                    {
                        [self show:self.rightViewController withStartOffset:offset endOffset:-self.rightWidth animated:YES];
                    }
                    break;
                default:
                    break;
            }
            _panOrigin = CGPointMake(0, 0);
            break;
        }
        default:
            break;
    }
    
    //CGPoint point1 = [gesture locationInView:self.rootViewController.view];
    //CGPoint point2 = [gesture translationInView:self.rootViewController.view];
    //NSLog(@"pan state %d location %f %f translation %f %f velocity %f %f", gesture.state, point1.x, point1.y, point2.x, point2.y, velocity.x, velocity.y);
}

#pragma mark - UIGestureRecognizerDelegate

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
//    if (_panDisabled)
//        return NO;
//    else
        return YES;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer
{
    if (otherGestureRecognizer == self.tapGesture)
        return NO;
    
    if ([_panExcludeViews containsObject:otherGestureRecognizer.view])
        return NO;
    else
        return YES;
}

@end

#pragma mark -
@implementation UIViewController (CommonSideViewController)

static char sideViewControllerKey;

- (void)setSideViewController:(CommonSideViewController *)controller
{
    [self willChangeValueForKey:@"sideViewController"];
    objc_setAssociatedObject(self, &sideViewControllerKey, controller, OBJC_ASSOCIATION_ASSIGN);
    [self didChangeValueForKey:@"sideViewController"];
}

- (CommonSideViewController*)sideViewController
{
    id controller = objc_getAssociatedObject(self, &sideViewControllerKey);
    
    // because we can't ask the navigation controller to set to the pushed controller the sideViewController !
    if (!controller && self.navigationController)
        controller = self.navigationController.sideViewController;
    
    if (!controller && self.tabBarController)
        controller = self.tabBarController.sideViewController;
    
    return controller;
}

@end
