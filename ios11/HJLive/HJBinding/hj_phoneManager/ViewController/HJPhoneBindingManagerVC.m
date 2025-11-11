//
//  HJPhoneBindingManagerVC.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPhoneBindingManagerVC.h"

#import "HJUserCoreHelp.h"

#import "HJBindingPhoneVC.h"

#import "HJBindingWXVC.h"
#import "HJUnbindingWXVC.h"

//#import <ShareSDK/ShareSDK.h>

#import "HJAlterShower.h"

@interface HJPhoneBindingManagerVC ()
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *phoneLabel;
@property (weak, nonatomic) IBOutlet UILabel *qqStatusLabel;
@property (weak, nonatomic) IBOutlet UILabel *wxStatusLabel;


@end

@implementation HJPhoneBindingManagerVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    __weak typeof(self)weakSelf = self;
    [GetCore(HJUserCoreHelp) getUserInfo:GetCore(HJAuthCoreHelp).getUid.userIDValue refresh:YES success:^(UserInfo *info) {
        
        weakSelf.phoneLabel.text = info.phone;
        weakSelf.nameLabel.text = info.nick;
        weakSelf.wxStatusLabel.text = info.hasWx?@"解绑":@"去绑定";
        weakSelf.qqStatusLabel.text = info.hasQq?@"解绑":@"去绑定";
    }];
    
}

- (IBAction)exchangeBindingAction:(id)sender {
    
    HJBindingPhoneVC *vc = (HJBindingPhoneVC *)HJWalletStoryBoard(@"HJBindingPhoneVC");
    vc.isPush = YES;
    vc.isBindPhone = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)qqBindingAction:(id)sender {
    
    if ([self.qqStatusLabel.text isEqualToString:@"去绑定"]) {
        
        [self getQQToken];
        
    }else{
        HJUnbindingWXVC *vc = HJBindingStoryBoard(@"HJUnbindingWXVC");
        vc.isQQ = YES;
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}

- (IBAction)wxBindingAction:(id)sender {
    
    if ([self.wxStatusLabel.text isEqualToString:@"去绑定"]) {
        [self getWxToken];
    }else{
        HJUnbindingWXVC *vc = HJBindingStoryBoard(@"HJUnbindingWXVC");
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
//            HJQqUserInfo *info = [HJQqUserInfo yy_modelWithJSON:user.rawData];
//            info.openID = user.credential.rawData[@"openid"];
//
////            [self loginWithOpenID:info.openID andUnionID:nil access_token:user.credential.token andType:XCThirdPartLoginQQ];
//
//            HJBindingWXVC *vc = HJBindingStoryBoard(@"HJBindingWXVC");
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
//            HJWeChatUserInfo *info = [HJWeChatUserInfo yy_modelWithDictionary:user.rawData];
////            [self loginWithOpenID:info.openid andUnionID:info.unionid access_token:user.credential.token andType:XCThirdPartLoginWechat];
//
//            HJBindingWXVC *vc = HJBindingStoryBoard(@"HJBindingWXVC");
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
