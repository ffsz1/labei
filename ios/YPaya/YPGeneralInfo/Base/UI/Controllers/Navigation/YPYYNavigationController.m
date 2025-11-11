//
//  YPYYNavigationController.m
//  YYMobile
//
//  Created by wuwei on 14/7/2.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPYYNavigationController.h"
#import "UIViewController+YYViewControllers.h"

@interface MessageInterceptor : NSObject {
    __weak id receiver;
    __weak id middleMan;
}

@property (nonatomic, weak) id receiver;
@property (nonatomic, weak) id middleMan;

@end

@interface YPYYNavigationController () <UIGestureRecognizerDelegate, YYNavigationControllerDelegate>

@property (nonatomic, strong) NSMutableArray *appearanceStack;

@property (nonatomic, strong) UIScreenEdgePanGestureRecognizer *interactivePopGesture;

@property (nonatomic, assign) BOOL isPushingOrPoping; // 是否在做 Push 或者 Pop，如果是，则忽略其他的 Push 或者 Pop
@property (nonatomic, assign) NSTimeInterval lastPushOrPopBeginTimestamp; // 上次开始做 Push 或者 Pop 时的时间戳

@end

@implementation YPYYNavigationController
{
    MessageInterceptor *_delegateInterceptor;
    UIViewController *_shouldApplyNavigationBarAppearanceWhenDidShow;
}

@synthesize interativePopTransition = _interativePopTransition;
@synthesize isPushingOrPoping = _isPushingOrPoping;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
//    if (self) {
        // Custom initialization
//    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    if (_appearanceStack == nil) {
        _appearanceStack = [NSMutableArray array];
    }
    
    if (self.delegate == nil)
    {
        self.delegate = nil;
        _delegateInterceptor = [[MessageInterceptor alloc] init];
        _delegateInterceptor.middleMan = self;
        [super setDelegate:(id)_delegateInterceptor];
    }
    
//    self.interactivePopGesture = [[UIScreenEdgePanGestureRecognizer alloc] initWithTarget:self action:@selector(handleNavigationTransition:)];
//    self.interactivePopGesture.edges = UIRectEdgeLeft;
//    self.interactivePopGesture.delegate = self;
//    [self.view addGestureRecognizer:self.interactivePopGesture];
    
    if (self.viewControllers.count == 1) {
        [self applyNavigationBarAppearanceForViewController:[self.viewControllers firstObject] animated:NO];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)setDelegate:(id<UINavigationControllerDelegate>)delegate
{
    if (_delegateInterceptor == nil)
    {
        _delegateInterceptor = [[MessageInterceptor alloc] init];
        _delegateInterceptor.middleMan = self;
    }
    
    [super setDelegate:nil];
    [_delegateInterceptor setReceiver:delegate];
    [super setDelegate:(id)_delegateInterceptor];
}

- (id)realDelegate {
    return _delegateInterceptor.receiver;
}

- (BOOL)shouldAutorotate
{
    return self.topViewController.shouldAutorotate;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return self.topViewController.supportedInterfaceOrientations;
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation
{
    return self.topViewController.preferredInterfaceOrientationForPresentation;
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
    return [self.topViewController preferredStatusBarStyle];
}

- (BOOL)prefersStatusBarHidden {
    return [self.topViewController prefersStatusBarHidden];
}

- (YPYYNavigationBar *)navigationBar
{
    return (YPYYNavigationBar *)[super navigationBar];
}

- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    if (self.isPushingOrPoping) {
        __weak typeof(self) weakSelf = self;
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [weakSelf pushViewController:viewController animated:animated];
        });

        return;
    }
    
    viewController.hidesBottomBarWhenPushed = YES;
    [super pushViewController:viewController animated:animated];
    [self afterPushViewController:viewController animated:animated];
}

- (void)pushBottomBarViewController:(UIViewController *)viewController animated:(BOOL)animated {
    viewController.hidesBottomBarWhenPushed = NO;
    [super pushViewController:viewController animated:animated];
    [self afterPushViewController:viewController animated:animated];
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated
{
    if (self.isPushingOrPoping) {
        
        __block UIViewController *viewController = nil;
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            viewController = [super popViewControllerAnimated:animated];
        });
        return viewController;
    }
    
    UIViewController *viewController = [super popViewControllerAnimated:animated];
    _shouldApplyNavigationBarAppearanceWhenDidShow = self.topViewController;
    
    return viewController;
}

- (NSArray *)popToRootViewControllerAnimated:(BOOL)animated
{
    NSArray *viewControllers = [super popToRootViewControllerAnimated:animated];
    _shouldApplyNavigationBarAppearanceWhenDidShow = self.topViewController;
    
    return viewControllers;
}

- (NSArray *)popToViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    NSArray *viewControllers = [super popToViewController:viewController animated:animated];
    _shouldApplyNavigationBarAppearanceWhenDidShow = self.topViewController;
    
    return viewControllers;
}

- (void)handleNavigationTransition:(UIScreenEdgePanGestureRecognizer *)recognizer
{
    CGFloat progress = [recognizer translationInView:self.view].x / (self.view.bounds.size.width * 1.0);
    progress = MIN(1.0, MAX(0.0, progress));
    
    if (recognizer.state == UIGestureRecognizerStateBegan) {
        // Create a interactive transition and pop the view controller
        _interativePopTransition = [[YPYYInteractivePopTransition alloc] init];
        [self popViewControllerAnimated:YES];
    }
    else if (recognizer.state == UIGestureRecognizerStateChanged) {
        // Update the interactive transition's progress
        [_interativePopTransition updateInteractiveTransition:progress];
    }
    else if (recognizer.state == UIGestureRecognizerStateEnded || recognizer.state == UIGestureRecognizerStateCancelled) {
        // Finish or cancel the interactive transition
        if (progress > 0.5) {
            [_interativePopTransition finishInteractiveTransition];
        }
        else {
            [_interativePopTransition cancelInteractiveTransition];
        }
        
        _interativePopTransition = nil;
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (YYNavigationBarAppearance *)topNavigationBarAppearance
{
    NSParameterAssert([NSThread isMainThread]);
    return [[self appearanceStack] lastObject];
}

- (BOOL)pushAppearanceStateWithContext:(id)context
{
    NSParameterAssert(context != nil);
    NSParameterAssert([NSThread isMainThread]);
    
    if ([self.topNavigationBarAppearance context] == (__bridge void *)(context)) {
        return NO;
    }
    
    YYNavigationBarAppearance *appearance = [[YYNavigationBarAppearance alloc] initWithContext:context];
    appearance.hidden = self.navigationBarHidden;
    appearance.translucent = self.navigationBar.translucent;
    appearance.tintColor = self.navigationBar.tintColor;
    appearance.barTintColor = self.navigationBar.barTintColor;
    appearance.barStyle = self.navigationBar.barStyle;
    appearance.backgroundImageDefault = [self.navigationBar backgroundImageForBarMetrics:UIBarMetricsDefault];
    appearance.backgroundImageLandscapePhone = [self.navigationBar backgroundImageForBarMetrics:UIBarMetricsLandscapePhone];
    appearance.titleTextAttributes = [self.navigationBar.titleTextAttributes copy];
    
    [self.appearanceStack addObject:appearance];
    
    return YES;
}

- (BOOL)popAppearanceStateWithContext:(id)context animated:(BOOL)animated
{
    NSParameterAssert(context != nil);
    NSParameterAssert([NSThread isMainThread]);
    
    YYNavigationBarAppearance *appearance = self.topNavigationBarAppearance;
    if (appearance == nil || appearance.context != (__bridge void *)(context)) {
        return NO;
    }
    
    [self.appearanceStack removeLastObject];
    
    // 恢复NavigationBar
    [self setNavigationBarHidden:appearance.hidden animated:animated];
    [self.navigationBar setTintColor:appearance.tintColor];
    [self.navigationBar setBarTintColor:[UIColor redColor]];
    [self.navigationBar setBarStyle:appearance.barStyle];

    [self.navigationBar setBackgroundImage:nil forBarMetrics:UIBarMetricsDefault];
    
//    [UIView animateWithDuration:0.3 animations:^{
    
        [self.navigationBar setShadowImage:nil];
        
        [self.navigationBar setBackgroundImage:nil forBarMetrics:UIBarMetricsLandscapePhone];
        [self.navigationBar setTitleTextAttributes:appearance.titleTextAttributes];
//    } completion:^(BOOL finished) {
//        [self.navigationBar setTranslucent:appearance.translucent];
        [self.navigationBar setTranslucent:NO]; //iOS7以上去掉默认透明
//    }];
    
    return YES;
}

#pragma mark - YYNavigationControllerDelegate

- (void)applyNavigationBarAppearanceForViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    BOOL preferredNavigationBarHidden = [viewController preferredNavigationBarHidden];
    BOOL preferredNavigationBarTranslucent = [viewController preferredNavigationBarTranslucent];
    
    [self setNavigationBarHidden:preferredNavigationBarHidden animated:animated];
    [self.navigationBar setTranslucent:preferredNavigationBarTranslucent];
    
    [self.navigationBar setBackgroundColor:[viewController preferredNavigationBarBackgroundColor]];

    [self.navigationBar setBackgroundImage:[viewController preferredNavigationBarBackgroundImageForBarMetrics:UIBarMetricsDefault] forBarMetrics:UIBarMetricsDefault];
    [self.navigationBar setBackgroundImage:[viewController preferredNavigationBarBackgroundImageForBarMetrics:UIBarMetricsLandscapePhone] forBarMetrics:UIBarMetricsLandscapePhone];

    [self.navigationBar setShadowImage:[viewController preferredNavigationBarShadowImage]];
    
    [self pushAppearanceStateWithContext:self.topViewController];
}

- (void)afterPushViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    [self applyNavigationBarAppearanceForViewController:viewController animated:animated];
    viewController.navigationItem.backBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStylePlain target:nil action:nil];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    if (self != navigationController) {
        return;
    }
    
    self.isPushingOrPoping = YES;
    
    if (viewController == _shouldApplyNavigationBarAppearanceWhenDidShow) {
        [self setNavigationBarHidden:[viewController preferredNavigationBarHidden] animated:animated];
    }
    
    if ([[self realDelegate] respondsToSelector:@selector(navigationController:willShowViewController:animated:)]) {
        [[self realDelegate] navigationController:self willShowViewController:viewController animated:animated];
    }
}

- (void)navigationController:(YPYYNavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    if (self != navigationController) {
        return;
    }
    
    self.isPushingOrPoping = NO;
    
    if (viewController == _shouldApplyNavigationBarAppearanceWhenDidShow) {
        [self applyNavigationBarAppearanceForViewController:viewController animated:animated];
        _shouldApplyNavigationBarAppearanceWhenDidShow = nil;
    }
    
    if ([[self realDelegate] respondsToSelector:@selector(navigationController:didShowViewController:animated:)]) {
        [[self realDelegate] navigationController:self didShowViewController:viewController animated:animated];
    }
}




#pragma mark - PushingOrPoping

- (void)setIsPushingOrPoping:(BOOL)isPushingOrPoping
{
    _isPushingOrPoping = isPushingOrPoping;
    
    if (isPushingOrPoping) {
        self.lastPushOrPopBeginTimestamp = [[NSDate date] timeIntervalSince1970];
    } else {
        self.lastPushOrPopBeginTimestamp = 0;
    }
}

- (BOOL)isPushingOrPoping
{
    // 手势Pop时会出bug
    return NO;
//    return _isPushingOrPoping &&
//            ([[NSDate date] timeIntervalSince1970] - self.lastPushOrPopBeginTimestamp) < 1.0;
}


@end

@implementation MessageInterceptor

@synthesize receiver;
@synthesize middleMan;

- (id)forwardingTargetForSelector:(SEL)aSelector {
    if ([middleMan respondsToSelector:aSelector]) { return middleMan; }
    if ([receiver respondsToSelector:aSelector]) { return receiver; }
    return [super forwardingTargetForSelector:aSelector];
}

- (BOOL)respondsToSelector:(SEL)aSelector {
    if ([middleMan respondsToSelector:aSelector]) { return YES; }
    if ([receiver respondsToSelector:aSelector]) { return YES; }
    return [super respondsToSelector:aSelector];
}

@end
