
//
//  YPYYMainTabBarController.m
//  YYMobile
//
//  Created by wuwei on 14/6/18.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPYYMainTabBarController.h"

#import "YPYYTheme.h"
#import "YPYYDefaultTheme.h"
#import "UIImage+_1x1Color.h"
#import "UIView+MultipleIndicator.h"

#import "YPGameVoicePageControl.h"
#import "FBKVOController.h"
#import "YYMainTabBarItem.h"
#import "YPAppDelegate.h"
#import "YPAssistiveTouch.h"
const NSInteger guideBtmTag = 555555;

@interface YYMainTabBar : UITabBar
@end

@implementation YYMainTabBar

#pragma mark - Override

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    self.backgroundColor = [UIColor clearColor];
    
//    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showMainToast:) name:showYYToastKey object:nil];
}

//- (void)showMainToast:(NSNotification *)notify
//{
//    NSString *content = [notify.userInfo objectForKey:toastYYContentKey];
//    [UIView showToastInKeyWindow:content];
//}

- (void)layoutSubviews
{
    [super layoutSubviews];
}

- (void)setHidden:(BOOL)hidden
{
    [super setHidden:hidden];
}

- (UIImageView *)findTopLine:(UIView *)view
{
    // 符合条件返回控件
    if ([view isKindOfClass:UIImageView.class] && view.bounds.size.height <= 1.0) {
        return (UIImageView *)view;
    }
    // 递归查找
    for (UIView *subview in view.subviews) {
        UIImageView *imageView = [self findTopLine:subview];
        if (imageView) {
            return imageView;
        }
    }
    return nil;
}

- (void)setShadowColor:(UIColor *)color
{
    UIImageView *imageView = [self findTopLine:self];
    imageView.backgroundColor = color;
//    imageView.hidden = YES;
}

@end


@interface YPYYMainTabBarController () <YYNavigationControllerDelegate, UITabBarControllerDelegate, UIScrollViewDelegate, AssistiveTouchDelegate>
{
    NSTimer *_checkBrokenNetworkTimer;
}

@property (nonatomic, strong) YPYYNavigationController        *selectChatNavController;
@property (nonatomic, strong) YPYYNavigationController        *squareNavController;
@property (nonatomic, strong) YPYYNavigationController        *recentNavController;
@property (nonatomic, strong) YPYYNavigationController        *mineNavController;

@property (nonatomic, weak, readonly) YYMainTabBar          *mainTabBar;

@property (nonatomic, strong) UIScrollView                  *scrollDisplay;
@property (nonatomic, strong) YPGameVoicePageControl          *pageControl;
@property (nonatomic) BOOL                                  statusBarHidden;

@property (weak, nonatomic) IBOutlet UIView *floatingView;
//@property (strong, nonatomic) IBOutlet UILabel *labelChannelName;
@property (weak, nonatomic) IBOutlet UIImageView *floatingViewImage;


@property (assign, nonatomic) BOOL isNeedShowFloatingView;

@end


@implementation YPYYMainTabBarController


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.statusBarHidden = YES;
    }
    
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.statusBarHidden = YES;
    }
    
    return self;
}

#pragma mark - Life Cycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.delegate = self;
    self.edgesForExtendedLayout = UIRectEdgeNone;
    self.modalTransitionStyle = UIModalPresentationCustom;
    
//    self.viewControllers = @[self.selectChatNavController, self.squareNavController, self.recentNavController,self.mineNavController];
    
    [self configTabBarItem];
    [self initFloatingView];
    //监听TabBarController高度变化
    [self.view addObserver:self forKeyPath:@"frame" options:NSKeyValueObservingOptionNew | NSKeyValueObservingOptionInitial context:nil];
}

- (void)initFloatingView
{
    
//    YPAppDelegate *deleage = (YPAppDelegate *)[UIApplication sharedApplication].delegate;
//    deleage.assistiveTouch = [[YPAssistiveTouch alloc] initWithFrame:CGRectMake(100, 100, self.floatingView.frame.size.width, self.floatingView.frame.size.height) view:self.floatingView];
//    [deleage.assistiveTouch makeKeyAndVisible];
//    deleage.assistiveTouch.assistiveDelegate = self;
//    deleage.assistiveTouch.hidden = YES;
}

- (void)assistiveTocuhs
{
//    YPAppDelegate *deleage = (YPAppDelegate *)[UIApplication sharedApplication].delegate;
//    deleage.assistiveTouch.hidden = YES;
//    [[YYChannelControllerCenter shareInstance] presendVideoCallController];
}


#pragma mark - kvo window fame
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if (object == self.view && [keyPath isEqualToString:@"frame"])
    {
        CGRect rect = self.view.frame;
        
        CGRect srceenRect = [[UIScreen mainScreen] bounds];
        
        if (!CGRectEqualToRect(rect, srceenRect)) {
            self.view.frame = srceenRect;
        }
    }
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    if ([self checkNeedDisplayWelcome]) {
//        [self displayWelcomeView];
    }else {
        if (self.statusBarHidden) {
            self.statusBarHidden = NO;
            [self setNeedsStatusBarAppearanceUpdate];
        }
    }
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
}

#pragma mark - Private Method
- (void)configTabBarItem
{
    //激活主题
    [[YPYYTheme defaultTheme] apply];
    
    //TabBar显示效果
    [[UITabBarItem appearanceWhenContainedIn:[YYMainTabBar class], nil] setTitleTextAttributes:@{NSFontAttributeName: [UIFont systemFontOfSize:11],NSForegroundColorAttributeName:RGBCOLOR(0x80, 0x80, 0x80)} forState:UIControlStateNormal];
    [[UITabBarItem appearanceWhenContainedIn:[YYMainTabBar class], nil] setTitleTextAttributes:@{NSFontAttributeName: [UIFont systemFontOfSize:11],NSForegroundColorAttributeName:[[YPYYTheme defaultTheme] globalTintColor]} forState:UIControlStateSelected];
    
    [[UITabBarItem appearanceWhenContainedIn:[YYMainTabBar class], nil] setTitlePositionAdjustment:UIOffsetMake(0, -4)];

    self.selectChatNavController.tabBarItem = [[YYMainTabBarItem alloc] initWithTitle:@"抢聊" image:[[UIImage imageNamed:@"yp_selectchat_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] selectedImage:[[UIImage imageNamed:@"yp_selectchat_lighted_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
    [self.selectChatNavController.tabBarItem setImageInsets:UIEdgeInsetsMake(-2, 0, 2, 0)];

    self.squareNavController.tabBarItem = [[YYMainTabBarItem alloc] initWithTitle:@"广场" image:[[UIImage imageNamed:@"yp_square_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] selectedImage:[[UIImage imageNamed:@"yp_square_lighted_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
    [self.squareNavController.tabBarItem setImageInsets:UIEdgeInsetsMake(-2, 0, 2, 0)];
    
    self.recentNavController.tabBarItem = [[YYMainTabBarItem alloc] initWithTitle:@"消息" image:[[UIImage imageNamed:@"yp_recent_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] selectedImage:[[UIImage imageNamed:@"yp_recent_lighted_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
    [self.recentNavController.tabBarItem setImageInsets:UIEdgeInsetsMake(-2, 0, 2, 0)];
    
    self.mineNavController.tabBarItem = [[YYMainTabBarItem alloc] initWithTitle:@"我" image:[[UIImage imageNamed:@"yp_mine_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] selectedImage:[[UIImage imageNamed:@"mine_lighted_nav"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
    [self.mineNavController.tabBarItem setImageInsets:UIEdgeInsetsMake(-2, 0, 2, 0)];
}

- (BOOL)checkNeedDisplayWelcome
{
    BOOL isFirst = ![[NSUserDefaults standardUserDefaults] boolForKey:@"is_show_welcome"];
    return !self.scrollDisplay && isFirst;
}

- (void)hideWelcome:(UIButton*)button
{
    if(self.scrollDisplay)
    {
        self.statusBarHidden = NO;
        [self setNeedsStatusBarAppearanceUpdate];
        
        [button setBackgroundImage:[UIImage imageNamed:@"button_mashangtiyan_normal"] forState:UIControlStateNormal];
        
        [UIView animateWithDuration:0.5 animations:^{
            self.scrollDisplay.alpha = 0;
        } completion:^(BOOL finished) {
            [self.scrollDisplay removeFromSuperview];
            
            self.scrollDisplay = nil;
            
            [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"is_show_welcome"];
            [[NSUserDefaults standardUserDefaults] synchronize];
//            [self showUnionAlertView];
            
            [self.pageControl removeFromSuperview];
        }];
    }
}

- (void) closeWelcome
{
    if(self.scrollDisplay)
    {
        [self.scrollDisplay removeFromSuperview];
        
        self.scrollDisplay = nil;
    }
    
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"is_show_welcome"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
//    [self.view dismissGuideView];
    
    UIView *temp = [self.view viewWithTag:guideBtmTag];
    if (temp) {
        [temp removeFromSuperview];
    }
}

#pragma mark - UIScrollView Delegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    int currentPage = [self findCurrentPage:scrollView];
    self.pageControl.currentPage = currentPage;
    if(currentPage >= self.pageControl.numberOfPages){
        CGFloat pageWidth = scrollView.frame.size.width;
        CGFloat alpha = (((scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1 - self.pageControl.numberOfPages) * 2;
        self.scrollDisplay.alpha = 1 - alpha;
        self.pageControl.alpha = 1 - alpha;
    }else{
        self.scrollDisplay.alpha = 1;
        self.pageControl.alpha = 1
        ;
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    if([self findCurrentPage:scrollView] >= self.pageControl.numberOfPages){
        [self hideWelcome:nil];
    }
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    if(!decelerate){
        if([self findCurrentPage:scrollView] >= self.pageControl.numberOfPages){
            [self hideWelcome:nil];
        }
    }
}

- (int)findCurrentPage:(UIScrollView *)scrollView
{
    // 得到每页宽度
    CGFloat pageWidth = scrollView.frame.size.width;
    // 根据当前的x坐标和页宽度计算出当前页数
    int currentPage = floor((scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
    return currentPage;
}

- (BOOL)prefersStatusBarHidden
{
    return [self.selectedViewController prefersStatusBarHidden];
}

- (UIViewController *)childViewControllerForStatusBarHidden
{
    UIViewController *childViewController = nil;
    if (!self.statusBarHidden) {
        childViewController = self.selectedViewController;
    }
    
    return childViewController;
}

- (UINavigationController *)selectedNavigationController
{
    return (UINavigationController *)self.selectedViewController;
}

- (YYMainTabBar *)mainTabBar
{
    return (YYMainTabBar *)self.tabBar;
}


- (UIStatusBarStyle)preferredStatusBarStyle
{
    return [self.selectedViewController preferredStatusBarStyle];
}

- (BOOL)shouldAutorotate
{
    return [[self selectedViewController] shouldAutorotate];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return [[self selectedViewController] supportedInterfaceOrientations];
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation
{
    return [self selectedViewController].preferredInterfaceOrientationForPresentation;
}

@end

#pragma mark - YYMainTabBarItem

@implementation YYMainTabBarItem

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    _showsIndicator = NO;
    
    [self setBadgeValue:nil];
}

- (void)setShowsIndicator:(BOOL)showsIndicator
{
    [self __setBadgeValueInternal:nil];
    
    _showsIndicator = showsIndicator;
}

- (void)setBadgeValue:(NSString *)badgeValue
{
    self.showsIndicator = NO;
    
    if ([badgeValue isEqualToString:@"0"])
    {
        NSLog(@"Set badgeValue to \"0\" somewhere: %@", [NSThread callStackSymbols]);
    }
    
    [self __setBadgeValueInternal:badgeValue];
}

- (void)__setBadgeValueInternal:(NSString *)badgeValue
{
    [super setBadgeValue:badgeValue];
}

@end


