//
//  HJBillMenuViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBillMenuViewController.h"
#import "HJBillMenuCell.h"
#import "HJPurseViewControllerFactory.h"
#import "HJBillListController.h"
#import "UIColor+UIColor_Hex.h"
#import "HJVersionCoreHelp.h"
#import "HJPurseSwitchTitleView.h"

@interface HJBillMenuViewController ()<UIScrollViewDelegate>

@property (nonatomic, weak) HJPurseSwitchTitleView *titleVeiw;
@property (nonatomic, weak) UIScrollView *scrollView;

@end

@implementation HJBillMenuViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    
    
    [self setupChildenController];
    [self setupScrollView];
    
    [self initView];
    [self setupTitleView];
}

- (void)initView {
    self.title = @"开心账单";
    if (@available(iOS 11.0, *)) {
        self.scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAutomatic;
    } else {
        if ([self respondsToSelector:@selector(edgesForExtendedLayout)])
        {
            self.edgesForExtendedLayout = UIRectEdgeNone;
        }
    }
    
}

- (void)setupTitleView {
    
    XCPurseSwitchModel *normalStyle1 = [XCPurseSwitchModel new];
    normalStyle1.norTextColor = UIColorHex(99989B);
    normalStyle1.selTextColor = UIColorHex(6E42D2);
    normalStyle1.norTextFont = [UIFont boldSystemFontOfSize:18];
    normalStyle1.selTextFont = [UIFont boldSystemFontOfSize:18];
    normalStyle1.lineColor = UIColorHex(6E42D2);
    normalStyle1.lineHeight = 2;
    normalStyle1.buttonWidth = XC_SCREE_W / 2;
    normalStyle1.textAlignment = UIControlContentHorizontalAlignmentCenter;
    
    
    NSArray *titleArr = @[
//                          NSLocalizedString(XCPurseBillIncomeGift, nil),
                          NSLocalizedString(XCPurseBillGiftOut, nil),
                          NSLocalizedString(XCPurseBillReChargeList, nil),
//                          NSLocalizedString(XCPurseBillInviete, nil),
                          ];
    
    NSArray *styleArr = @[
//                          normalStyle1,
                          normalStyle1,
                          normalStyle1,
//                          normalStyle1,
                          ];
    
    __weak typeof(self) weakSelf = self;
    HJPurseSwitchTitleView *titleView = [[HJPurseSwitchTitleView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 44) titleArr:titleArr styleArr:styleArr currentButton:0 didClickButtonBlock:^(NSInteger index) {
        
        [weakSelf.scrollView setContentOffset:CGPointMake(index * XC_SCREE_W, 0) animated:YES];
    }];
    
    [self.view addSubview:titleView];
    
    self.titleVeiw = titleView;
}

- (void)setupScrollView {
    
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 44, XC_SCREE_W, XC_SCREE_H -(iPhoneX ? 88: 64) - 44)];
    scrollView.contentSize = CGSizeMake(self.view.frame.size.width * self.childViewControllers.count, 0);
    scrollView.showsVerticalScrollIndicator = NO;
    scrollView.showsHorizontalScrollIndicator = NO;
    scrollView.pagingEnabled = YES;
    scrollView.delegate = self;
    scrollView.backgroundColor = [UIColor clearColor];
    
    [self.view addSubview:scrollView];
    self.scrollView = scrollView;
    
    for (int i = 0; i < self.childViewControllers.count; i++) {
        UIViewController *vc = self.childViewControllers[i];
        vc.view.frame = CGRectMake(i *scrollView.frame.size.width, 0, scrollView.frame.size.width, scrollView.frame.size.height);
        
        [self.scrollView addSubview:vc.view];
    }
}

- (void)setupChildenController {
    
    
    HJBillListController *vc2 = (HJBillListController *)[[HJPurseViewControllerFactory sharedFactory] instantiateBillListViewController];
    vc2.type = BillType_GiftOut;
    [self addChildViewController:vc2];
    
    HJBillListController *vc3 = (HJBillListController *)[[HJPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
    vc3.type = BillType_Recharge;
    [self addChildViewController:vc3];
    

    
}


- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    
    NSInteger index = (scrollView.contentOffset.x + 1) / scrollView.frame.size.width;
    
    self.titleVeiw.currentButton = index;
    
}

@end
