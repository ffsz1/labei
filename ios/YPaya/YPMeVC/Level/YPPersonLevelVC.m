//
//  YPPersonLevelVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPersonLevelVC.h"
//vc
//other
#import "UIColor+UIColor_Hex.h"
#import "UIImage+_1x1Color.h"
#import "YPUserCoreHelp.h"
#import "YPZJScrollPageView.h"
#import "UIView+XCToast.h"
#import "YPYYDefaultTheme.h"
#import "YPRankConsTopView.h"
#import "YPLevelModel.h"
#import "YPPersonalLevelChirldVC.h"
#import "HJUserCoreClient.h"
#import "YPShareInfo.h"
#import "YPShareView.h"
#define getRectNavAndStatusHight  self.navigationController.navigationBar.frame.size.height+[[UIApplication sharedApplication] statusBarFrame].size.height
#define RGBA(r,g,b,a) [UIColor colorWithRed:r/255.0f green:g/255.0f blue:b/255.0f alpha:a]

@interface YPPersonLevelVC ()<ZJScrollPageViewDelegate,HJUserCoreClient>
@property (nonatomic, strong) YPZJSegmentStyle *style;
@property (nonatomic, strong) NSMutableArray<NSString *> *titles;
@property (nonatomic, strong) YPPersonalLevelChirldVC *richVC;
@property (nonatomic, strong) YPPersonalLevelChirldVC *chamVC;


@property (weak, nonatomic) YPZJScrollSegmentView *segmentView;
@property (weak, nonatomic) YPZJContentView *contentView;


@property(strong, nonatomic)NSArray<UIViewController<ZJScrollPageViewChildVcDelegate> *> *childVcs;

@property (strong, nonatomic) YPRankConsTopView *chatsConsTopView;

@end

@implementation YPPersonLevelVC

- (YPRankConsTopView *)chatsConsTopView {
    if (!_chatsConsTopView) {
        _chatsConsTopView = [[NSBundle mainBundle] loadNibNamed:@"YPRankConsTopView" owner:nil options:nil][0];
        _chatsConsTopView.backgroundColor = [UIColor whiteColor];
        [_chatsConsTopView.caifuBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [_chatsConsTopView.meiliBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [_chatsConsTopView.backBtn setImage:[UIImage imageNamed:@"yp_nav_bar_back"] forState:UIControlStateNormal];
        @weakify(self);
        [_chatsConsTopView setGoBackBlock:^{
            @strongify(self);
            [self.navigationController popViewControllerAnimated:YES];
        }];
        
        [_chatsConsTopView setSendBlock:^(NSInteger tag) {
            @strongify(self);
            [self.contentView setContentOffSet:CGPointMake(kScreenWidth * tag, 0.0) animated:YES];
        }];
    }
    return _chatsConsTopView;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)updateSegment:(NSNotification *)notification {
    NSInteger index = [notification.object[@"index"] integerValue];
    if (index == 0) {
        [self.chatsConsTopView tabClick:self.chatsConsTopView.caifuBtn];
    } else {
        [self.chatsConsTopView tabClick:self.chatsConsTopView.meiliBtn];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateSegment:) name:@"ZJContentViewScrollViewDidScrollNotification" object:nil];
    
    self.childVcs = [self setupChildVc];
    self.view.backgroundColor = [UIColor whiteColor];
    [self initViews];
    
    // 初始化
    [self setupSegmentView];
    [self setupContentView];
    
    [self.view addSubview:self.chatsConsTopView];
    [self.chatsConsTopView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.view);
        if (@available(iOS 9.0, *)) {
            make.height.equalTo(@44);
        }else{
            make.height.equalTo(@44);
        }
        if (@available(iOS 11.0, *)) {
            make.top.equalTo(self.view.mas_safeAreaLayoutGuideTop);
        } else {
            make.top.equalTo(self.view);
        }
    }];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self setShareItem];
    });
}

- (void)setShareItem
{
    BOOL hasWx = [WXApi isWXAppInstalled];
    BOOL hasQQ = ([TencentOAuth iphoneQQInstalled] || [TencentOAuth iphoneTIMInstalled]);

    if (!hasWx && !hasQQ) {
        return;
    }
    
    UIButton *shareBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [shareBtn setImage:[UIImage imageNamed:@"yp_sign_share"] forState:UIControlStateNormal];
    CGFloat topPad = iPhoneX? 44 : 20;
    shareBtn.frame = CGRectMake(XC_SCREE_W-50, topPad, 40, 40);
    [shareBtn addTarget:self action:@selector(shareAction) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:shareBtn];
    
//    UIBarButtonItem * shareItem = [[UIBarButtonItem alloc] initWithCustomView:shareBtn];
//    self.navigationItem.rightBarButtonItem = shareItem;
}

- (void)shareAction {
    
    YPShareInfo *shareInfo = [[YPShareInfo alloc] init];
    shareInfo.type = HJShareTypeNormol;
    shareInfo.desc = @"看见你一次，就喜欢你一次";//NSLocalizedString(ShareCoreDes2, nil);
//    shareInfo.imgUrl = @"https://pic.hnyueqiang.com/logo.png";
    shareInfo.imgUrl = [NSString stringWithFormat:@"%@/home/images/logo.png",[YPHttpRequestHelper getHostUrl]];
//    http://pic.zoudewu.com/home/images/logo.png
//    http://test.haijiaoxingqiu.cn:80/home/images/logo.png
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/download/download.html?",[YPHttpRequestHelper getHostUrl]];
    shareInfo.showUrl = urlSting;
    shareInfo.title = @"喜翻鸭";//NSLocalizedString(ShareCoreTitle2, nil);
    
    [YPShareView show:shareInfo];
    
}




- (void)setupSegmentView {
    YPZJSegmentStyle *style = [[YPZJSegmentStyle alloc] init];
    
    style.showCover = YES;
    
    style.scrollTitle = NO;
    
    style.gradualChangeTitleColor = YES;
    
    style.coverBackgroundColor = [UIColor clearColor];
    
    style.normalTitleColor = RGBA(255,255,255,0.5);
    
    style.selectedTitleColor = RGBA(255,255,255,1);
    
    style.titleFont = [UIFont systemFontOfSize:17];
    
    style.scrollLineColor = UIColorHex(ffffff);
    
    style.scrollLineHeight = 2;
    
    __weak typeof(self) weakSelf = self;
    YPZJScrollSegmentView *segment = [[YPZJScrollSegmentView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, getRectNavAndStatusHight) segmentStyle:style delegate:self titles:self.titles titleDidClick:^(YPZJTitleView *titleView, NSInteger index) {
        [weakSelf.contentView setContentOffSet:CGPointMake(weakSelf.contentView.bounds.size.width * index, 0.0) animated:YES];
    }];
    self.segmentView = segment;
    
    [self.view addSubview:self.segmentView];
    [self.segmentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.equalTo(self.view);
        make.height.equalTo(@(getRectNavAndStatusHight));
    }];
}

- (void)setupContentView {
    YPZJContentView *content = [[YPZJContentView alloc] initWithFrame:CGRectMake(0, getRectNavAndStatusHight, kScreenWidth, kScreenHeight-getRectNavAndStatusHight) segmentView:self.segmentView parentViewController:self delegate:self];
    self.contentView = content;
    [self.view addSubview:self.contentView];
    
}

- (NSArray *)setupChildVc {
    NSArray *childVcs = [NSArray arrayWithObjects:self.richVC, self.chamVC, nil];
    return childVcs;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
}

#pragma mark - UI
- (void)initViews {
    self.automaticallyAdjustsScrollViewInsets = NO;
}


- (void)goback {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - ZJScrollPageViewDelegate

- (BOOL)shouldAutomaticallyForwardAppearanceMethods {
    return NO;
}

- (NSInteger)numberOfChildViewControllers {
    return self.titles.count ;
}

- (UIViewController<ZJScrollPageViewChildVcDelegate> *)childViewController:(UIViewController<ZJScrollPageViewChildVcDelegate> *)reuseViewController forIndex:(NSInteger)index {
    NSLog(@"%ld---------", index);
    if (index == 0) {
        YPPersonalLevelChirldVC *vc = (YPPersonalLevelChirldVC *)reuseViewController;
        if (vc == nil) {
            vc = self.richVC;
        }
        return vc;
    } else {
        YPPersonalLevelChirldVC *vc = (YPPersonalLevelChirldVC *)reuseViewController;
        if (vc == nil) {
            vc = self.chamVC;
        }
        return vc;
    }
}

- (NSMutableArray<NSString *> *)titles{
    if (!_titles) {
        _titles = [NSMutableArray array];
        [_titles addObjectsFromArray:@[@"财富等级", @"魅力等级"]];
    }
    return _titles;
}

- (YPPersonalLevelChirldVC *)richVC {
    if (!_richVC) {
        _richVC = [[YPPersonalLevelChirldVC alloc]init];
        _richVC.type = 0;
        //        _richVC.tipLabel.text = @"财富值每消耗1金币收到1点财富值";
    }
    return _richVC;
}

- (YPPersonalLevelChirldVC *)chamVC {
    if (!_chamVC) {
        _chamVC = [[YPPersonalLevelChirldVC alloc]init];
        _chamVC.type = 1;
        //        _chamVC.tipLabel.text = @"魅力值每收到1金币收到1点魅力值";
        
    }
    return _chamVC;
}

@end
