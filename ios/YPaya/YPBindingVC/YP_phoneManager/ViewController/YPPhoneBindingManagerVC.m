//
//  YPPhoneBindingManagerVC.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPhoneBindingManagerVC.h"

#import "YPUserCoreHelp.h"

#import "YPBindingPhoneVC.h"

#import "YPBindingWXVC.h"
#import "YPUnbindingWXVC.h"

//#import <ShareSDK/ShareSDK.h>

#import "YPAlterShower.h"

@interface YPPhoneBindingManagerVC ()
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *phoneLabel;
@property (weak, nonatomic) IBOutlet UILabel *qqStatusLabel;
@property (weak, nonatomic) IBOutlet UILabel *wxStatusLabel;


@end

@implementation YPPhoneBindingManagerVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    __weak typeof(self)weakSelf = self;
    [GetCore(YPUserCoreHelp) getUserInfo:GetCore(YPAuthCoreHelp).getUid.userIDValue refresh:YES success:^(UserInfo *info) {
        
        weakSelf.phoneLabel.text = info.phone;
        weakSelf.nameLabel.text = info.nick;
        weakSelf.wxStatusLabel.text = info.hasWx?@"解绑":@"去绑定";
        weakSelf.qqStatusLabel.text = info.hasQq?@"解绑":@"去绑定";
    }];
    
}

- (IBAction)exchangeBindingAction:(id)sender {
    
    YPBindingPhoneVC *vc = (YPBindingPhoneVC *)YPWalletStoryBoard(@"YPBindingPhoneVC");
    vc.isPush = YES;
    vc.isBindPhone = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)qqBindingAction:(id)sender {
    
    if ([self.qqStatusLabel.text isEqualToString:@"去绑定"]) {
        
        [self getQQToken];
        
    }else{
        YPUnbindingWXVC *vc = YPBindingStoryBoard(@"YPUnbindingWXVC");
        vc.isQQ = YES;
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}

- (IBAction)wxBindingAction:(id)sender {
    
    if ([self.wxStatusLabel.text isEqualToString:@"去绑定"]) {
        [self getWxToken];
    }else{
        YPUnbindingWXVC *vc = YPBindingStoryBoard(@"YPUnbindingWXVC");
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}

- (void)getQQToken
{
//    [MBProgressHUD showMessage:@"授权中..."];
//    [ShareSDK cancelAuthorize:SSDKPlatformTypeQQ];
//
//    [ShareSDK getUserInfo:SSDKPlatformTypeQQ onStateChanged:^(SSDKResponseState state, SSDKUser *user, NSError *error) {
//        NSLog(@"user:%lu",(unsigned long)state);
//        [MBProgressHUD hideHUD];
//        if (state == SSDKResponseStateSuccess) {
//            NSLog(@"%@",user);
//            YPQqUserInfo *info = [YPQqUserInfo yy_modelWithJSON:user.rawData];
//            info.openID = user.credential.rawData[@"openid"];
//
////            [self loginWithOpenID:info.openID andUnionID:nil access_token:user.credential.token andType:XCThirdPartLoginQQ];
//
//            YPBindingWXVC *vc = YPBindingStoryBoard(@"YPBindingWXVC");
//            vc.isQQ = YES;
//            vc.unionId = @"";
//            vc.openId = info.openID;
//            vc.accessToken = user.credential.token;
//
//            [self.navigationController pushViewController:vc animated:YES];
//
//        } else if (state == SSDKResponseStateCancel) {
//            [MBProgressHUD showSuccess:@"用户取消授权"];
//
//        } else if (state == SSDKResponseStateFail) {
//            [MBProgressHUD showSuccess:@"授权失败"];
//        }
//        //        user.city;
//    }];
}

- (void)getWxToken
{
//    [MBProgressHUD showMessage:@"授权中..."];
//    [ShareSDK cancelAuthorize:SSDKPlatformTypeWechat];
//
//    [ShareSDK getUserInfo:SSDKPlatformTypeWechat onStateChanged:^(SSDKResponseState state, SSDKUser *user, NSError *error) {
//        NSLog(@"user:%lu",(unsigned long)state);
//        [MBProgressHUD hideHUD];
//        if (state == SSDKResponseStateSuccess) {
//            NSLog(@"%@",user);
//            YPWeChatUserInfo *info = [YPWeChatUserInfo yy_modelWithDictionary:user.rawData];
////            [self loginWithOpenID:info.openid andUnionID:info.unionid access_token:user.credential.token andType:XCThirdPartLoginWechat];
//
//            YPBindingWXVC *vc = YPBindingStoryBoard(@"YPBindingWXVC");
//            vc.unionId = info.unionid;
//            vc.openId = info.openid;
//            vc.accessToken = user.credential.token;
//
//            [self.navigationController pushViewController:vc animated:YES];
//
//        } else if (state == SSDKResponseStateCancel) {
//            [MBProgressHUD showSuccess:@"用户取消授权"];
//
//        } else if (state == SSDKResponseStateFail) {
//            [MBProgressHUD showSuccess:@"授权失败"];
//        }
//    }];
}


@end
