//
//  YPFYCashAndExchangeVC.m
//  XBD
//
//  Created by feiyin on 2019/11/1.
//

#import "YPFYCashAndExchangeVC.h"

#import "YPPurseViewControllerFactory.h"

#import "UIColor+UIColor_Hex.h"
//#import "XBDVersionCore.h"

#import "YPPurseSwitchTitleView.h"

#import "YPFYGetCashVC.h"
#import "YPFYRegularVC.h"
//#import "FYExchangeVC.h"
#import "YPFYExchangeGoldVC.h"
#import "YPWKWebViewController.h"
#import "UINavigationController+Cloudox.h"
@interface YPFYCashAndExchangeVC ()<UIScrollViewDelegate>

@property (nonatomic, weak) YPPurseSwitchTitleView *titleVeiw;
@property (nonatomic, weak) UIScrollView *scrollView;





@end

@implementation YPFYCashAndExchangeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
   
//    [self.navigationController setNeedsNavigationBackground:0];
//    self.navigationController.navigationBar.backgroundColor = [UIColor redColor];
    YPLightStatusBar
     self.navigationItem.rightBarButtonItem =  [[UIBarButtonItem alloc] initWithTitle:@"规则" style:UIBarButtonItemStyleDone target:self action:@selector(regulationAction)];
    
    [self setupChildenController];
    [self setupScrollView];
    
    [self initView];
    [self setupTitleView];
    UIImageView *tmp=[self findNavBarBottomLine: self.navigationController.navigationBar];
    tmp.hidden=YES;
    
}
-(void)addNavBtn{
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];

        [rightBtn setTitle:@"规则" forState:UIControlStateNormal];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:14];
    rightBtn.frame = CGRectMake(kScreenWidth-70, iPhoneX?40:30, 50, 30);

        [rightBtn addTarget:self action:@selector(regulationAction) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:rightBtn];
    
    UIButton *backBtn = [UIButton buttonWithType:UIButtonTypeCustom];

        [backBtn setImage:[UIImage imageNamed:@"yp_space_back"] forState:UIControlStateNormal];
  
    backBtn.frame = CGRectMake(5, iPhoneX?40:30, 50, 30);

        [backBtn addTarget:self action:@selector(onClickBackBtn) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:backBtn];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self addNavBtn];
    if (_isDuiHuanPage) {
        self.titleVeiw.currentButton = 1;
            [self.scrollView setContentOffset:CGPointMake(XC_SCREE_W, 0) animated:YES];
       }
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];

}
- (UIImageView *)findNavBarBottomLine:(UIView *)view{
    if ([view isKindOfClass:[UIImageView class]]&&view.bounds.size.height<1) {
        return (UIImageView *)view;
    }
    for (UIView *subView in view.subviews) {
        UIImageView *imageView=[self findNavBarBottomLine:subView];
        if (imageView) {
            return imageView;
        }
    }
    return nil;
}
- (void)initView {
    
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
    normalStyle1.norTextColor = [UIColor colorWithWhite:1 alpha:0.7];
    normalStyle1.selTextColor = UIColorHex(FFFFFF);
    normalStyle1.norTextFont = [UIFont boldSystemFontOfSize:18];
    normalStyle1.selTextFont = [UIFont boldSystemFontOfSize:18];
    normalStyle1.lineColor = UIColorHex(8A58FF);
//    normalStyle1.lineHeight = 2;
    normalStyle1.buttonWidth = 80;
    normalStyle1.textAlignment = UIControlContentHorizontalAlignmentCenter;
    
    
    NSArray *titleArr = @[
//                          NSLocalizedString(XCPurseBillIncomeGift, nil),
                            NSLocalizedString(@" 兑换金币 ", nil),
                            NSLocalizedString(@" 我的收益 ", nil),
                         
//                          NSLocalizedString(XCPurseBillInviete, nil),
                          ];
    
    NSArray *styleArr = @[
//                          normalStyle1,
                          normalStyle1,
                          normalStyle1,
//                          normalStyle1,
                          ];
    
    __weak typeof(self) weakSelf = self;
    YPPurseSwitchTitleView *titleView = [[YPPurseSwitchTitleView alloc] initWithFrame:CGRectMake(kScreenWidth/2-80, iPhoneX?40:30, 170, 44) titleArr:titleArr styleArr:styleArr currentButton:0 didClickButtonBlock:^(NSInteger index) {
        
//        [weakSelf.scrollView setContentOffset:CGPointMake(index * XC_SCREE_W, 30) animated:YES];
    }];
    
//    [self.view addSubview:titleView];
    titleView.backgroundColor = [UIColor clearColor];
    self.titleVeiw = titleView;
//      self.navigationItem.titleView = titleView;
    [self.view addSubview:titleView];
}

- (void)setupScrollView {
    
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H)];
    scrollView.contentSize = CGSizeMake(self.view.frame.size.width * self.childViewControllers.count, 0);
    scrollView.showsVerticalScrollIndicator = NO;
    scrollView.showsHorizontalScrollIndicator = NO;
    scrollView.pagingEnabled = YES;
    scrollView.delegate = self;
    scrollView.backgroundColor = [UIColor colorWithHexString:@"#8A58FF"];
    
    [self.view addSubview:scrollView];
    self.scrollView = scrollView;
    
    for (int i = 0; i < self.childViewControllers.count; i++) {
        UIViewController *vc = self.childViewControllers[i];
        vc.view.frame = CGRectMake(i *scrollView.frame.size.width, 0, scrollView.frame.size.width, scrollView.frame.size.height);
        
        [self.scrollView addSubview:vc.view];
    }
}

- (void)setupChildenController {
    
        YPFYGetCashVC *vc3 = (YPFYGetCashVC *)[[YPPurseViewControllerFactory sharedFactory]instantiateFYGetCashVC];
      
     YPFYExchangeGoldVC *vc4 = (YPFYExchangeGoldVC *)[[YPPurseViewControllerFactory sharedFactory] instantiateFYExchangeGoldVC];
    vc4.changeGoldBlock = ^(NSString * _Nonnull diamondNum) {
        vc3.amountLabel.text = diamondNum;
    };
        [self addChildViewController:vc4];
      [self addChildViewController:vc3];

}


- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    
    NSInteger index = (scrollView.contentOffset.x + 1) / scrollView.frame.size.width;
    
    self.titleVeiw.currentButton = index;
    
}

//MARK: - Action
//账单Action
-(void)regulationAction{
//         YPFYRegularVC *VC = (YPFYRegularVC *)[[YPPurseViewControllerFactory sharedFactory] instantiateFYRegularVC];
//            [self.navigationController pushViewController:VC animated:YES];
    
    YPWKWebViewController *viewController = [[YPWKWebViewController alloc] init];
      NSString *URLString = [NSString stringWithFormat:@"%@/front/diamond/index.html", [YPHttpRequestHelper getHostUrl]];
      viewController.url = [NSURL URLWithString:URLString];
      [self.navigationController pushViewController:viewController animated:YES];

     }


-(void)onClickBackBtn{
    [self.navigationController popViewControllerAnimated:NO];
}
@end



