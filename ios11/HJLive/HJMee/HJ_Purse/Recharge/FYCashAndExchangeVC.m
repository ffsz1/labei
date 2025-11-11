//
//  FYCashAndExchangeVC.m
//  XBD
//
//  Created by feiyin on 2019/11/1.
//

#import "FYCashAndExchangeVC.h"

#import "HJPurseViewControllerFactory.h"

#import "UIColor+UIColor_Hex.h"
//#import "XBDVersionCore.h"

#import "HJPurseSwitchTitleView.h"

#import "FYGetCashVC.h"
#import "FYRegularVC.h"
//#import "FYExchangeVC.h"
#import "FYExchangeGoldVC.h"
#import "HJWKWebViewController.h"

@interface FYCashAndExchangeVC ()<UIScrollViewDelegate>

@property (nonatomic, weak) HJPurseSwitchTitleView *titleVeiw;
@property (nonatomic, weak) UIScrollView *scrollView;





@end

@implementation FYCashAndExchangeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
     self.navigationItem.rightBarButtonItem =  [[UIBarButtonItem alloc] initWithTitle:@"规则" style:UIBarButtonItemStyleDone target:self action:@selector(regulationAction)];
    
    [self setupChildenController];
    [self setupScrollView];
    
    [self initView];
    [self setupTitleView];
    UIImageView *tmp=[self findNavBarBottomLine: self.navigationController.navigationBar];
    tmp.hidden=YES;
   
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (_isDuiHuanPage) {
        self.titleVeiw.currentButton = 1;
            [self.scrollView setContentOffset:CGPointMake(XC_SCREE_W, 0) animated:YES];
       }
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
    normalStyle1.norTextColor = UIColorHex(999999);
    normalStyle1.selTextColor = UIColorHex(333333);
    normalStyle1.norTextFont = [UIFont boldSystemFontOfSize:16];
    normalStyle1.selTextFont = [UIFont boldSystemFontOfSize:16];
    normalStyle1.lineColor = UIColorHex(333333);
    normalStyle1.lineHeight = 2;
    normalStyle1.buttonWidth = 75;
    normalStyle1.textAlignment = UIControlContentHorizontalAlignmentCenter;
    
    
    NSArray *titleArr = @[
//                          NSLocalizedString(XCPurseBillIncomeGift, nil),
                            NSLocalizedString(@" 兑换开心 ", nil),
                            NSLocalizedString(@" 兑换收益 ", nil),
                         
//                          NSLocalizedString(XCPurseBillInviete, nil),
                          ];
    
    NSArray *styleArr = @[
//                          normalStyle1,
                          normalStyle1,
                          normalStyle1,
//                          normalStyle1,
                          ];
    
    __weak typeof(self) weakSelf = self;
    HJPurseSwitchTitleView *titleView = [[HJPurseSwitchTitleView alloc] initWithFrame:CGRectMake(0, 0, 150, 44) titleArr:titleArr styleArr:styleArr currentButton:0 didClickButtonBlock:^(NSInteger index) {
        
        [weakSelf.scrollView setContentOffset:CGPointMake(index * XC_SCREE_W, 0) animated:YES];
    }];
    
//    [self.view addSubview:titleView];
    titleView.backgroundColor = [UIColor whiteColor];
    self.titleVeiw = titleView;
      self.navigationItem.titleView = titleView;
}

- (void)setupScrollView {
    
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H -(iPhoneX ? 88: 64))];
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
    
        FYGetCashVC *vc3 = (FYGetCashVC *)[[HJPurseViewControllerFactory sharedFactory]instantiateFYGetCashVC];
      
     FYExchangeGoldVC *vc4 = (FYExchangeGoldVC *)[[HJPurseViewControllerFactory sharedFactory] instantiateFYExchangeGoldVC];
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
//         FYRegularVC *VC = (FYRegularVC *)[[HJPurseViewControllerFactory sharedFactory] instantiateFYRegularVC];
//            [self.navigationController pushViewController:VC animated:YES];
    
    HJWKWebViewController *viewController = [[HJWKWebViewController alloc] init];
      NSString *URLString = [NSString stringWithFormat:@"%@/front/diamond/index.html", [HJHttpRequestHelper getHostUrl]];
      viewController.url = [NSURL URLWithString:URLString];
      [self.navigationController pushViewController:viewController animated:YES];

     }

@end



