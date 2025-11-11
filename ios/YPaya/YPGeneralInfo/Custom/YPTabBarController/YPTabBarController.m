//
//  YPTabBarController.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPTabBarController.h"
#import "YPFloatingView.h"

#import "YPBaseNavigationController.h"
#import "YPTabBar.h"
#import "YPRoomViewControllerCenter.h"

#import "YPAppDelegate.h"
#import "HJRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"

#import "YPRoomCoreV2Help.h"
#import "UIView+getTopVC.h"

#import "YPYYViewControllerCenter.h"
#import "YPImRoomCoreV2.h"

@interface YPTabBarController ()<UITabBarControllerDelegate>

@property (nonatomic,strong) YPTabBar *tabbar;

@property (nonatomic,assign) NSInteger lastIndex;

@property (nonatomic,strong) YPFloatingView *floatingView;


@end

@implementation YPTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _tabbar = [[YPTabBar alloc] init];
    [_tabbar.centerBtn addTarget:self action:@selector(buttonAction:) forControlEvents:UIControlEventTouchUpInside];

    //透明设置为NO，显示白色，view的高度到tabbar顶部截止，YES的话到底部
    _tabbar.translucent = YES;
    
    //利用KVC 将自己的tabbar赋给系统tabBar
    [self setValue:_tabbar forKeyPath:@"tabBar"];
    
    self.delegate = self;
    
    [self addChildViewControllers];
    
    [self setStytle];
    
    self.hidesBottomBarWhenPushed = YES;
    
    self.floatingView.hidden = YES;
    
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJImRoomCoreClient, self);
    
}

//- (void)viewDidLayoutSubviews{
//    [super viewDidLayoutSubviews];
//
//    for (UITabBarItem *item in self.tabbar.items) {
//        //调整title水平或垂直
//        item.titlePositionAdjustment = UIOffsetMake(0, 0);
//        //需要调整图标距离
//        item.imageInsets = UIEdgeInsetsMake(-5, 0, 5, 0);
////        item.imageInsets = UIEdgeInsetsMake(-3, 0, 5, 0);
//
//    }
//}


-(void)setStytle
{
    [[UITabBarItem appearance] setTitleTextAttributes:@{ NSForegroundColorAttributeName : [UIColor colorWithRed:51/255.0 green:51/255.0 blue:51/255.0 alpha:1.0],NSFontAttributeName:[UIFont fontWithName:@"PingFang SC" size: 9]} forState:UIControlStateNormal];
    
    [[UITabBarItem appearance] setTitleTextAttributes:@{ NSForegroundColorAttributeName : [UIColor colorWithRed:194/255.0 green:128/255.0 blue:255/255.0 alpha:1.0],NSFontAttributeName:[UIFont fontWithName:@"PingFang SC" size: 9]} forState:UIControlStateSelected];
//    [[UITabBarItem appearance] setTitleTextAttributes:@{ NSForegroundColorAttributeName :  [UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_home_peopleBg"]],NSFontAttributeName:[UIFont fontWithName:@"PingFang SC" size: 9]} forState:UIControlStateSelected];
    
    [[UITabBarItem appearance] setTitlePositionAdjustment:UIOffsetMake(0, -4)];
    
    [self.tabBar setBackgroundImage:[[UIImage alloc] init]];
    [self.tabBar setShadowImage:[[UIImage alloc] init]];
    //    [self.tabBar setLayerShadow:[UIColor clearColor] offset:CGSizeZero radius:0];
    
    UIImageView *image = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"yp_tabBar_bg"]];
    
    
    image.contentMode = UIViewContentModeScaleAspectFill ;
    image.frame = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, self.tabBar.frame.size.height-8);
    image.backgroundColor = [UIColor clearColor];
    [self.tabBar insertSubview:image atIndex:0];
    // 适配iOS13导致的bug
      if (@available(iOS 13.0, *)) {
          // iOS 13以上
//          self.tabBar.tintColor =  [UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_home_peopleBg"]];//#EE47B7   #752FB3
          self.tabBar.tintColor = [UIColor colorWithHexString:@"#A2A6FF"];
          self.tabBar.unselectedItemTintColor = [UIColor colorWithHexString:@"#999999"];
          UITabBarItem *item = [UITabBarItem appearance];
          item.titlePositionAdjustment = UIOffsetMake(0, -2);
          [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:iPhoneX? 12:9]} forState:UIControlStateNormal];
          [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:iPhoneX? 12:9]} forState:UIControlStateSelected];
      } else {
          // iOS 13以下
          UITabBarItem *item = [UITabBarItem appearance];
          item.titlePositionAdjustment = UIOffsetMake(0, -2);
          [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:iPhoneX? 12:9], NSForegroundColorAttributeName:[UIColor colorWithHexString:@"#999999"]} forState:UIControlStateNormal];
//          [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:iPhoneX? 12:9], NSForegroundColorAttributeName:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_home_peopleBg"]]} forState:UIControlStateSelected];// #752FB3
          
          
          
          
      }
}

- (void)buttonAction:(UIButton *)button{
    
    //拦截顶部push viewcontroller的手势
    if (self.selectedViewController.childViewControllers.count>0) {
        return;
    }
    
    button.userInteractionEnabled = NO;
    
    [[YPRoomViewControllerCenter defaultCenter] openRoonWithType:RoomType_Game];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        button.userInteractionEnabled = YES;
    });

    
}



- (void)addChildViewControllers{
    //图片大小建议32*32
    [self addChildrenViewController:[self controllerFromClassName:@"YPFirstHomeViewController"] andTitle:@"首页" andImageName:@"yp_tabBar_home" andSelectImage:@"yp_tabBar_home_sel"];
    [self addChildrenViewController:[self controllerFromClassName:@"YPJiaoYouViewController"] andTitle:@"交友" andImageName:@"yp_tabBar_find" andSelectImage:@"yp_tabBar_find_sel"];
    //中间这个不设置东西，只占位
//    [self addChildrenViewController:[[UIViewController alloc] init] andTitle:@"" andImageName:@"" andSelectImage:@""];
    [self addChildrenViewController:self.msgVC andTitle:@"消息" andImageName:@"yp_tabBar_msg" andSelectImage:@"yp_tabBar_msg_sel"];
    [self addChildrenViewController:self.mineVC andTitle:@"我的" andImageName:@"yp_tabBar_me" andSelectImage:@"yp_tabBar_me_sel"];
}

- (void)addChildrenViewController:(UIViewController *)childVC andTitle:(NSString *)title andImageName:(NSString *)imageName andSelectImage:(NSString *)selectedImage{
    childVC.tabBarItem.image = [[UIImage imageNamed:imageName] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    childVC.tabBarItem.selectedImage =  [[UIImage imageNamed:selectedImage] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    childVC.title = title;
    
//    UINavigationController *baseNav = [[UINavigationController alloc] initWithRootViewController:childVC];
    
    [self addChildViewController:childVC];
}

- (YPBaseNavigationController *)controllerFromClassName:(NSString *)className{
    //2。获取类名
    Class class = NSClassFromString(className);
    //3.创建对象
    UIViewController *VC  = [class new];
    
    YPBaseNavigationController *nav = [[YPBaseNavigationController alloc]initWithRootViewController:VC];
    nav.navigationBar.translucent = NO;
    return nav;
}

- (YPBaseNavigationController *)mineVC{
    UIViewController *vc = YPMeStoryBoard(@"YPMyViewController");
    YPBaseNavigationController *nav = [[YPBaseNavigationController alloc]initWithRootViewController:vc];
    nav.navigationBar.translucent = NO; //message
    return nav;
}

- (YPBaseNavigationController *)msgVC{
    UIViewController *vc = YPMessageBoard(@"YPMessageVC");
    YPBaseNavigationController *nav = [[YPBaseNavigationController alloc]initWithRootViewController:vc];
    nav.navigationBar.translucent = NO; //message
    return nav;
}


//tabbar选择时的代理

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController{
    self.lastIndex = tabBarController.selectedIndex;


//    if (tabBarController.selectedIndex == 2){//选中中间的按钮
//        [self buttonAction:nil];
//        [self setSelectedIndex:self.lastIndex];
//    }else{
//        self.lastIndex = tabBarController.selectedIndex;
//    }
}

- (void)showBadgeOnItemIndex:(int)index num:(NSInteger)num
{
    [self.tabbar showBadgeOnItemIndex:index num:num];
}


#pragma mark - ImRoomCoreClientV2
- (void)onCurrentRoomInfoChanged
{
    
    
    YPChatRoomInfo *roomInfo = [GetCore(YPRoomCoreV2Help) getCurrentRoomInfo];
    UserInfo *info = GetCore(YPImRoomCoreV2).roomOwnerInfo;
    if (roomInfo != nil && roomInfo.valid) {
        
        [self.floatingView.logoImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        self.floatingView.nickLabel.text = info.nick;
        [self.floatingView setHidden:NO];
//        [self.floatingView startPlayAnimation];
    } else {
        [self.floatingView closeAction];
    }
}


#pragma mark - ImRoomCoreClient
- (void)onUserBeKicked:(NSString *)roomid
{
    [self.floatingView closeAction];
}

- (void)onMeInterChatRoomBadNetWork {
    
    [self.floatingView closeAction];
}

- (YPFloatingView *)floatingView
{
    if (!_floatingView) {
        _floatingView = [[NSBundle mainBundle] loadNibNamed:@"YPFloatingView" owner:self options:nil][0];
        [self.view addSubview:_floatingView];
        _floatingView.frame = CGRectMake(kScreenWidth-74-5, kScreenHeight-XC_Height_TabBar-47-40, 74, 74);
    }
    return _floatingView;
}

@end
