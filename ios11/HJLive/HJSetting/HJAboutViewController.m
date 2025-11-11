//
//  HJAboutViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAboutViewController.h"
#import "YYUtility.h"
#import "HJWKWebViewController.h"
@interface HJAboutViewController ()
@property (weak, nonatomic) IBOutlet UILabel *versionLabel;
@property (nonatomic, strong) UILabel *contactLabel;

@end

@implementation HJAboutViewController

#pragma mark - Life cycle
- (instancetype)init {
    self = [super init];
    if (self) {
        [self commonInit];
    }
    return self;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setupNavigation];
    [self addControls];
    [self layoutControls];
    [self updateControls];
    
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    HJBlackStatusBar
}

#pragma mark - Event

#pragma mark - Public methods

#pragma mark - Private methods
- (void)setupNavigation {
    self.title = NSLocalizedString(@"关于我们", nil);
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
//    return UIStatusBarStyleLightContent; //返回白色
    return UIStatusBarStyleDefault;    //返回黑色
}
- (void)commonInit {
    
}

- (void)updateControls {
    self.versionLabel.text = [NSString stringWithFormat:@"%@:%@",@"内测版本号",[YYUtility appVersion]];
//    self.contactLabel.text = [NSString stringWithFormat:@"官方QQ群 : 780261882\n客服微信：yumengnb5\n%@", [UIApplication sharedApplication].appBuildVersion];
}

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.contactLabel];
}

- (void)layoutControls {
    [self.contactLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.view);
        if (@available(iOS 11.0, *)) {
            make.bottom.equalTo(self.view.mas_safeAreaLayoutGuideBottom).offset(-25);
        } else {
            make.bottom.equalTo(self.view).offset(-25);
        }
    }];
}
//用户协议
- (IBAction)userProtocolBtnAction:(id)sender {
    HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
            NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/agreement.html",[HJHttpRequestHelper getHostUrl]];
            vc.url = [NSURL URLWithString:urlSting];
            [self.navigationController pushViewController:vc animated:YES];
}
//隐私协议
- (IBAction)privacyProtocolBtnAction:(id)sender {
    HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
                  NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/privacy.html",[HJHttpRequestHelper getHostUrl]];
                  vc.url = [NSURL URLWithString:urlSting];
                  [self.navigationController pushViewController:vc animated:YES];
}
//直播协议
- (IBAction)livePrototcolBtnAction:(id)sender {
    HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/live.html",[HJHttpRequestHelper getHostUrl]];
    vc.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:vc animated:YES];
}


#pragma mark - setters/getters
- (UILabel *)contactLabel {
    if (!_contactLabel) {
        _contactLabel = [UILabel new];
        _contactLabel.font = [UIFont systemFontOfSize:14];
        _contactLabel.textColor = [UIColor colorWithHexString:@"#999999"];
        _contactLabel.numberOfLines = 0;
        _contactLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _contactLabel;
}

@end
