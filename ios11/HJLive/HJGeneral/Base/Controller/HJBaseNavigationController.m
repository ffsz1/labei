//
//  HJBaseNavigationController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBaseNavigationController.h"
#import "HJBaseViewController.h"
#import "UIColor+UIColor_Hex.h"

@interface XCMessageInterceptor : NSObject

@property (nonatomic, weak) id receiver;
@property (nonatomic, weak) id middleMan;

@end

@implementation XCMessageInterceptor

- (id)forwardingTargetForSelector:(SEL)aSelector {
    if ([_middleMan respondsToSelector:aSelector]) return _middleMan;
    if ([_receiver respondsToSelector:aSelector]) return _receiver;
    
    return [super forwardingTargetForSelector:aSelector];
}

- (BOOL)respondsToSelector:(SEL)aSelector {
    if ([_middleMan respondsToSelector:aSelector]) return YES;
    if ([_receiver respondsToSelector:aSelector]) return YES;
    
    return [super respondsToSelector:aSelector];
}

@end

@interface HJBaseNavigationController () <UIGestureRecognizerDelegate>

@property (nonatomic, strong) XCMessageInterceptor *delegateInterceptor;

@end

@implementation HJBaseNavigationController

+(void)initialize
{
    UINavigationBar *bar = [UINavigationBar appearance];
    //设置title
    [bar setTitleTextAttributes:@{NSFontAttributeName:[UIFont boldSystemFontOfSize:18],NSForegroundColorAttributeName:[UIColor blackColor]}] ;
    bar.backgroundColor = [UIColor clearColor];
    // 设置item
    UIBarButtonItem *item = [UIBarButtonItem appearance];
    // UIControlStateNormal
    NSMutableDictionary *itemAttrs = [NSMutableDictionary dictionary];
    itemAttrs[NSForegroundColorAttributeName] = UIColorFromRGB(0x666666);
    itemAttrs[NSFontAttributeName] = [UIFont systemFontOfSize:16];
    [item setTitleTextAttributes:itemAttrs forState:UIControlStateNormal];
    // UIControlStateDisabled
    NSMutableDictionary *itemDisabledAttrs = [NSMutableDictionary dictionary];
    itemDisabledAttrs[NSForegroundColorAttributeName] = UIColorFromRGB(0x666666);;
    [item setTitleTextAttributes:itemDisabledAttrs forState:UIControlStateDisabled];
    //去除系统的透明效果 ： 代码会影响登陆的导航？
//    [bar setTranslucent:NO];
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    @weakify(self)
    if ([self respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.interactivePopGestureRecognizer.delegate = self;
    }
    
    if (self.delegate == nil) {
        self.delegate = nil;
        _delegateInterceptor = [[XCMessageInterceptor alloc] init];
        _delegateInterceptor.middleMan = self;
        [super setDelegate:(id)_delegateInterceptor];
    }
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (void)initNavWithVC:(UIViewController *)viewController {
    UIBarButtonItem *leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"hj_nav_bar_back"] style:UIBarButtonItemStylePlain target:self action:@selector(backClick)];
    leftBarButtonItem.tintColor = [UIColor colorWithHexString:@"#1a1a1a"];
    viewController.navigationItem.leftBarButtonItem = leftBarButtonItem;
}

- (void)setDelegate:(id<UINavigationControllerDelegate>)delegate {
    if (_delegateInterceptor == nil) {
        _delegateInterceptor = [[XCMessageInterceptor alloc] init];
        _delegateInterceptor.middleMan = self;
    }
    
    [super setDelegate:nil];
    [_delegateInterceptor setReceiver:delegate];
    [super setDelegate:(id)_delegateInterceptor];
}

- (id)realDelegate {
    return _delegateInterceptor.receiver;
}

- (void)setNavigationBarHidden:(BOOL)navigationBarHidden {
    super.navigationBarHidden = navigationBarHidden;
}

#pragma mark - 可在此方法中拦截所有push 进来的控制器
-(void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated{
    
    if (self.childViewControllers.count>0) {
        viewController.hidesBottomBarWhenPushed = YES;
        
        [self initNavWithVC:viewController];
    }
    // 这句super的push要放在后面, 让viewController可以覆盖上面设置的leftBarButtonItem
    [super pushViewController:viewController animated:animated];
    
}

- (UIViewController *)childViewControllerForStatusBarStyle{
    return self.visibleViewController;
}

- (void)backClick{
    [self popViewControllerAnimated:YES];
}

//支持旋转
-(BOOL)shouldAutorotate
{
    return [self.topViewController shouldAutorotate];
}

//支持的方向
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.topViewController supportedInterfaceOrientations];
}

#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    if (self.childViewControllers.count <= 1) return NO;
    
    return YES;
}

#pragma mark - <UINavigationControllerDelegate>
- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
    if (self != navigationController) return;
    
    if (navigationController.viewControllers.firstObject == viewController) {
        [navigationController.interactivePopGestureRecognizer setEnabled:NO];
    } else {
        [navigationController.interactivePopGestureRecognizer setEnabled:YES];
    }
    
    if ([[self realDelegate] respondsToSelector:@selector(navigationController:didShowViewController:animated:)]) {
        [[self realDelegate] navigationController:self didShowViewController:viewController animated:animated];
    }
}

@end
