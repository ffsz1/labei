//
//  HJHeadMallViewController.m
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHeadMallViewController.h"
#import "ZJScrollPageView.h"
#import "HJCarViewController.h"

@interface HJHeadMallViewController ()<ZJScrollPageViewDelegate>
@property(strong, nonatomic)NSArray<NSString *> *titles;
@property(strong, nonatomic)NSArray<UIViewController *> *childVcs;
@property (nonatomic, strong) ZJScrollPageView *scrollPageView;
@end

@implementation HJHeadMallViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationController.navigationBar.translucent = NO;
    
    self.title = NSLocalizedString(XCCarSysTitle, nil);

    [[UINavigationBar appearance] setBackgroundImage:[[UIImage alloc] init] forBarPosition:UIBarPositionAny barMetrics:UIBarMetricsDefault];
    [[UINavigationBar appearance] setShadowImage:[[UIImage alloc] init]];
    
    ZJSegmentStyle *style = [[ZJSegmentStyle alloc] init];
    //显示滚动条
    style.showLine = YES;
    // 颜色渐变
    style.gradualChangeTitleColor = YES;
    style.normalTitleColor = UIColorHex(666666);
    style.selectedTitleColor = UIColorHex(1a1a1a);
    style.titleFont = [UIFont systemFontOfSize:15];
    style.segmentViewComponent = SegmentViewComponentAdjustCoverOrLineWidth;
    style.scrollTitle = false;
    style.scrollLineColor = UIColorHex(00C2FF);
    
    self.titles = @[NSLocalizedString(XCCarSysHant, nil),
                    NSLocalizedString(XCCarSysCar, nil),
                    ];
    // 初始化
    _scrollPageView = [[ZJScrollPageView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight-(iPhoneX ? 104: 64)) segmentStyle:style titles:self.titles parentViewController:self delegate:self];

    [self.view addSubview:_scrollPageView];
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)numberOfChildViewControllers {
    return self.titles.count;
}

- (UIViewController<ZJScrollPageViewChildVcDelegate> *)childViewController:(UIViewController<ZJScrollPageViewChildVcDelegate> *)reuseViewController forIndex:(NSInteger)index {
    UIViewController<ZJScrollPageViewChildVcDelegate> *childVc = reuseViewController;
    
    if (index == 0) {
        HJCarViewController *childVc = (HJCarViewController *)reuseViewController;
        if (childVc == nil) {
            childVc = [[HJCarViewController alloc] init];
            childVc.isCarSys = false;
        }
        return childVc;
        
    } else {
        HJCarViewController *childVc = (HJCarViewController *)reuseViewController;
        if (childVc == nil) {
            childVc = [[HJCarViewController alloc] init];
            childVc.isCarSys = YES;
        }
        return childVc;
    }
}


- (BOOL)shouldAutomaticallyForwardAppearanceMethods {
    return NO;
}

@end
