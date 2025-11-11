//
//  YPLoginViewController.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPLoginViewController.h"
#import "YPWKWebViewController.h"

#import "YPLoginPhoneView.h"
#import "YPLoginRegisterView.h"
#import "YPLoginResetView.h"
#import "YPLoginBindingPhoneView.h"

#import "HJAuthCoreClient.h"
#import "YPYYViewControllerCenter.h"
#import "SVGAImageView.h"
#import "UINavigationController+JXBase.h"
#import "YPSignnAlterView.h"


#define XBDLoginsBottomViewHeight1 308
#define XBDLoginsBottomViewHeight2 320//435//380

#define XBDLoginsDuration 0.8

typedef NS_ENUM(NSUInteger, XBDLoginType) {
    XBDLoginTypeNormal = 0,
    XBDLoginTypePhone,
    XBDLoginTypeRegister,
    XBDLoginTypeResetPassword,
    XBDLoginTypeBindingPhone
};


@interface YPLoginViewController ()<HJAuthCoreClient>
@property (weak, nonatomic) IBOutlet UIImageView *bgImageView;
@property (weak, nonatomic) IBOutlet UIView *bottomView;
@property (weak, nonatomic) IBOutlet UIButton *backBtn;
@property (weak, nonatomic) IBOutlet UIButton *jumpBtn;
@property (weak, nonatomic) IBOutlet GGButton *wechatBtn;
@property (weak, nonatomic) IBOutlet GGButton *qqBtn;
@property (weak, nonatomic) IBOutlet GGButton *phoneBtn;
@property (weak, nonatomic) IBOutlet UIButton *selBtn;

//约束
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_logo;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_logoBgView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *top_qqBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *top_phoneBtn;


@property (nonatomic, strong) SVGAImageView *animationView;
@property (nonatomic,strong) YPLoginPhoneView *loginView;
@property (nonatomic,strong) YPLoginRegisterView *registerView;
@property (nonatomic,strong) YPLoginResetView *resetView;
@property (nonatomic,strong) YPLoginBindingPhoneView *bindingView;


//当前bottomView的类型
@property (nonatomic,assign) XBDLoginType type;

@end

@implementation YPLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setBottomViewStytle];
    
    AddCoreClient(HJAuthCoreClient, self);

    [self.view insertSubview:self.animationView belowSubview:self.bgImageView];
    [self.animationView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.bgImageView);
    }];
    
    [self checkQQandWx];
    
    [self.animationView startAnimation];
    
    [self setUI];
    
    self.view.backgroundColor = [UIColor whiteColor];
    [self phoneAction:nil];
    
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
    self.animationView = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    YPLightStatusBar
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
}

#pragma mark private mothed
- (void)setBottomViewStytle
{
    
//    CGRect frame = CGRectMake(0, 0, kScreenWidth-32, XBDLoginsBottomViewHeight1);
//    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(30, 30)];
//
//    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
//    maskLayer.frame = frame;
//    maskLayer.path = maskPath.CGPath;
//    self.bottomView.layer.mask = maskLayer;
}

- (void)setUI
{
    [self.view addSubview:self.loginView];
}


- (void)checkQQandWx{
    
    BOOL hasWx = [WXApi isWXAppInstalled];
    BOOL hasQQ = ([TencentOAuth iphoneQQInstalled] || [TencentOAuth iphoneTIMInstalled]);

    self.wechatBtn.hidden = !hasWx;
    self.qqBtn.hidden = !hasQQ;
    
//    self.top_qqBtn.constant = hasQQ?5:-22;
//    self.top_phoneBtn.constant = hasWx?5:-22;

}

- (void)registerAction
{
    self.type = XBDLoginTypeRegister;
    self.registerView.hidden = NO;
    [self showRegisterViewAnimation:YES];
}

- (void)forgetAction
{
    self.type = XBDLoginTypeResetPassword;
    self.resetView.hidden = NO;
    [self showResetViewAnimation:YES];
}

- (void)showSmallLogoAnimation:(BOOL)isSamll
{
//    if (!isSamll) {
//        self.backBtn.hidden = YES;
//    }

    [UIView animateWithDuration:XBDLoginsDuration delay:0 usingSpringWithDamping:0.9 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseOut animations:^{
        
        self.bottom_logoBgView.constant = isSamll?XBDLoginsBottomViewHeight2 -XBDLoginsBottomViewHeight1-55:0;
        self.height_logo.constant = isSamll?74:100;
        [self.view layoutIfNeeded];
    } completion:^(BOOL finished) {
//        if (isSamll) {
//            self.backBtn.hidden = NO;
//        }
    }];
    
}

- (void)showPhoneViewAnimation:(BOOL)isShow
{
    [UIView animateWithDuration:XBDLoginsDuration delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:isShow?UIViewAnimationOptionCurveEaseOut:UIViewAnimationOptionCurveEaseIn animations:^{
        
        self.loginView.frame = CGRectMake(0, isShow?kScreenHeight - XBDLoginsBottomViewHeight2:kScreenHeight, kScreenWidth, XBDLoginsBottomViewHeight2);
        
    } completion:^(BOOL finished) {
        
    }];
}

- (void)showRegisterViewAnimation:(BOOL)isShow
{
    
    if (!isShow) {
        self.backBtn.hidden = YES;
    }

    self.registerView.isShow = isShow;

    [UIView animateWithDuration:XBDLoginsDuration delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:isShow?UIViewAnimationOptionCurveEaseOut:UIViewAnimationOptionCurveEaseIn animations:^{
        
        self.registerView.frame = CGRectMake(0, isShow?kScreenHeight - XBDLoginsBottomViewHeight2-100:kScreenHeight, kScreenWidth, XBDLoginsBottomViewHeight2+100);
        
    } completion:^(BOOL finished) {
        if (isShow) {
            self.backBtn.hidden = NO;
        }
        
    }];
}

- (void)showResetViewAnimation:(BOOL)isShow
{
    
    if (!isShow) {
        self.backBtn.hidden = YES;
    }
    self.resetView.isShow = isShow;
    
    
    [UIView animateWithDuration:XBDLoginsDuration delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:isShow?UIViewAnimationOptionCurveEaseOut:UIViewAnimationOptionCurveEaseIn animations:^{
        
        self.resetView.frame = CGRectMake(0, isShow?kScreenHeight - XBDLoginsBottomViewHeight2-90:kScreenHeight, kScreenWidth, XBDLoginsBottomViewHeight2+90);
        
    } completion:^(BOOL finished) {
        if (isShow) {
            self.backBtn.hidden = NO;
        }
        
    }];
}

- (void)showBindingViewAnimation
{
    //检测是否已经跳过绑定手机
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    NSArray * array = [defaults objectForKey:@"BindingPhoneMissList"];
    if (array != nil) {
        for (NSString *uidStr in array) {
            if ([uidStr isEqualToString:GetCore(YPAuthCoreHelp).getUid]) {
                
                [self checkDataComplete];
                
                return;
            }
        }
    }
    
    
    self.backBtn.hidden = YES;
    self.jumpBtn.hidden = NO;
    self.bindingView.isShow = YES;
    
    [UIView animateWithDuration:XBDLoginsDuration delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseOut animations:^{
        
        self.bindingView.frame = CGRectMake(0, kScreenHeight - XBDLoginsBottomViewHeight2, kScreenWidth, XBDLoginsBottomViewHeight2);
        
    } completion:^(BOOL finished) {
        
    }];
}

//检查是否已完善资料
- (void)checkDataComplete
{
    __weak typeof(self)weakSelf = self;
    [GetCore(YPUserCoreHelp) getUserInfo:GetCore(YPAuthCoreHelp).getUid.unsignedIntegerValue refresh:NO success:^(UserInfo * info) {
        if (info.nick.length <= 0 || info.avatar.length <= 0 || info == nil) {
            
            [weakSelf.navigationController pushViewController:YPLoginStoryBoard(@"YPLoginFillDataVC") animated:YES];
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [weakSelf.navigationController jx_removeViewControllerFromClassName:@"YPLoginViewController"];
            });
            
        } else {
//            [YPSignnAlterView show];

            [weakSelf.navigationController dismissViewControllerAnimated:YES completion:nil];
        }
    }];
}

#pragma mark IBAction

//返回上一步操作
- (IBAction)backAction:(id)sender {
    
    if (self.type == XBDLoginTypeRegister) {
        self.type = XBDLoginTypePhone;
        [self showRegisterViewAnimation:NO];
        [GetCore(YPAuthCoreHelp) stopCountDown];
    }

    if (self.type == XBDLoginTypeResetPassword) {
        self.type = XBDLoginTypePhone;
        [self showResetViewAnimation:NO];
        [GetCore(YPAuthCoreHelp) stopCountDown];
    }
    
}

//跳过
- (IBAction)jumpBtnAction:(id)sender {
    
    //保存跳过绑定的UID,下次不会弹出绑定窗口
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    NSArray * array = [defaults objectForKey:@"BindingPhoneMissList"];
    NSMutableArray *operateArr = [[NSMutableArray alloc] init];
    if (array != nil) {
        [operateArr addObjectsFromArray:array];
    }
    [operateArr addObject:GetCore(YPAuthCoreHelp).getUid];
    [defaults setObject:operateArr forKey:@"BindingPhoneMissList"];
    [defaults synchronize];
    
    [self checkDataComplete];
}

//wechat 登陆
- (IBAction)wechatAction:(id)sender {
    
    if (!self.selBtn.selected) {
        [MBProgressHUD showError:@"请同意并勾选用户协议"];
        return;
    }
    
    [MBProgressHUD showMessage:@"正在登陆中..."];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [GetCore(YPAuthCoreHelp) loginWithWc];
    });
    
}

//qq登陆
- (IBAction)qqAction:(id)sender {
    
    if (!self.selBtn.selected) {
        [MBProgressHUD showError:@"请同意并勾选用户协议"];
        return;
    }
    
    [MBProgressHUD showMessage:@"正在登陆中..."];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [GetCore(YPAuthCoreHelp) loginWithQQ];
    });
    
}

//手机登陆
- (IBAction)phoneAction:(id)sender {
    
    if (!self.selBtn.selected) {
        [MBProgressHUD showError:@"请同意并勾选用户协议"];
        return;
    }
    
    self.type = XBDLoginTypePhone;
    
    [self showSmallLogoAnimation:YES];
    [self showPhoneViewAnimation:YES];
    
    
}

//协议勾选
- (IBAction)selAction:(id)sender {
    
    self.selBtn.selected = !self.selBtn.selected;
    
}

//用户协议
- (IBAction)delegateAction:(id)sender {
    
    YPWKWebViewController *vc = [[YPWKWebViewController alloc]init];
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/agreement.html",[YPHttpRequestHelper getHostUrl]];
    vc.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:vc animated:YES];
    
}

- (IBAction)yinsiDelegateAction:(id)sender {
    YPWKWebViewController *vc = [[YPWKWebViewController alloc]init];
                    NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/privacy.html",[YPHttpRequestHelper getHostUrl]];
                    vc.url = [NSURL URLWithString:urlSting];
                    [self.navigationController pushViewController:vc animated:YES];
}


- (IBAction)zhiboDelegateAction:(id)sender {
    YPWKWebViewController *vc = [[YPWKWebViewController alloc]init];
       NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/live.html",[YPHttpRequestHelper getHostUrl]];
       vc.url = [NSURL URLWithString:urlSting];
       [self.navigationController pushViewController:vc animated:YES];
}



#pragma -mark AuthCoreClient
- (void)thirdPartLoginFailth {
    [MBProgressHUD showError:@"登录失败，请重试"];
}

- (void)thirdPartLoginCancel {
    [MBProgressHUD showError:@"用户取消操作"];
}

- (void)onLoginSuccess
{
    [MBProgressHUD hideHUD];
    
    if (GetCore(YPAuthCoreHelp).type == XCPhoneLogin) {
        [self checkDataComplete];
        return;
    }
    
    [GetCore(YPUserCoreHelp) getUserInfo:GetCore(YPAuthCoreHelp).getUid.userIDValue refresh:YES success:^(UserInfo *info) {
        
//        if (info.phone.length>0) {
            [self checkDataComplete];
//        }else{
//            [self showBindingViewAnimation];
//        }
    }];
}

- (void)onLoginFailth:(NSString *)errorMsg
{
    [MBProgressHUD hideHUD];
}


#pragma mark setter/getter
- (SVGAImageView *)animationView {
    if (!_animationView) {
        _animationView = [SVGAImageView new];
        _animationView.imageName = @"yp_LoginBG2";//life-hj
    }
    return _animationView;
}

- (YPLoginPhoneView *)loginView
{
    if (!_loginView) {
        _loginView = [[NSBundle mainBundle] loadNibNamed:@"YPLoginPhoneView" owner:self options:nil][0];
        _loginView.frame = CGRectMake(0, kScreenHeight, kScreenWidth, XBDLoginsBottomViewHeight2);
        
        __weak typeof(self)weakSelf = self;
        _loginView.registerBlock = ^{
            [weakSelf registerAction];
        };
        
        _loginView.forgetBlock = ^{
            [weakSelf forgetAction];
        };
    }
    return _loginView;
}

- (YPLoginRegisterView *)registerView
{
    if (!_registerView) {
        _registerView = [[NSBundle mainBundle] loadNibNamed:@"YPLoginRegisterView" owner:self options:nil][0];
        _registerView.frame = CGRectMake(0, kScreenHeight, kScreenWidth, XBDLoginsBottomViewHeight2);
        __weak typeof(self)weakSelf = self;
               _registerView.closeBlock = ^{
                   [weakSelf showRegisterViewAnimation:NO];
               };
        
        [self.view addSubview:_registerView];
    }
    return _registerView;
}

- (YPLoginResetView *)resetView
{
    if (!_resetView) {
        _resetView = [[NSBundle mainBundle] loadNibNamed:@"YPLoginResetView" owner:self options:nil][0];
        _resetView.frame = CGRectMake(0, kScreenHeight, kScreenWidth, XBDLoginsBottomViewHeight2);
        
        __weak typeof(self)weakSelf = self;
        _resetView.closeBlock = ^{
            [weakSelf showResetViewAnimation:NO];
        };
        
        
        
        
        [self.view addSubview:_resetView];
    }
    return _resetView;
}

- (YPLoginBindingPhoneView *)bindingView
{
    if (!_bindingView) {
        _bindingView = [[NSBundle mainBundle] loadNibNamed:@"YPLoginBindingPhoneView" owner:self options:nil][0];
        _bindingView.frame = CGRectMake(0, kScreenHeight, kScreenWidth, XBDLoginsBottomViewHeight2);
        
        __weak typeof(self)weakSelf = self;
        _bindingView.bindingBlock = ^{
            [weakSelf checkDataComplete];
        };
        [self.view addSubview:_bindingView];
    }
    return _bindingView;
}



@end
