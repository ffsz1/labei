//
//  YPSignerCell.m
//  HJLive
//
//  Created by apple on 2019/5/22.
//

#import "YPSignerCell.h"

#import "YPJXMCDefines.h"
#import "YPHttpRequestHelper+Sign.h"

#import "YPEditPersonalPhotosVC.h"
#import "YPUserViewControllerFactory.h"

#import "UIView+getTopVC.h"
#import "YPImRoomCoreV2.h"
#import "YPRoomViewControllerCenter.h"

#import "YPBindingPhoneVC.h"
#import "YPNotiFriendVC.h"
#import "YPPurseViewControllerFactory.h"
#import "YPMallViewController.h"

#import "YPJXShareHelper.h"

#import "YPShareInfo.h"
#import "YPWKWebViewController.h"
#import "YPMultiLineTextModifyVC.h"
#import "YPUserViewControllerFactory.h"

@implementation YPSignerCell

- (IBAction)signBtnAction:(id)sender {
    
    JXMCHomeItemStatus status = [_model.missionStatus integerValue];

    
    if (status == JXMCHomeItemStatusReceive) {
        [self receivePost];
    }else if (status == JXMCHomeItemStatusToDo){
        [self handleTask];
    }
}


//领取点点币
- (void)receivePost
{
    __weak typeof(self)weakSelf = self;
    [MBProgressHUD showMessage:@"请稍后"];
    [YPHttpRequestHelper requestRecevieMengCoinWithMissionId:self.model.missionId success:^{
        
        if (weakSelf.updateBlock) {
            weakSelf.updateBlock();
        }
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        [MBProgressHUD hideHUD];
    }];
}

- (void)handleTask
{
    JXMCHomeItemType type = [_model.missionId integerValue];

    
    switch (type) {
        case JXMCHomeItemTypeShareVChatTimeline:
        {
            [self shareToPlatform:JXShareTypeWXCircle];
        }
            break;
        case JXMCHomeItemTypeShareQZone:
        {
            [self shareToPlatform:JXShareTypeQQZone];
        }
            break;
        case JXMCHomeItemTypeUploadImages:
        {
            [self skipToUploadImages];
        }
            break;
        case JXMCHomeItemTypeSendPublic:
        {
            [self skipToPublicHall];
        }
            break;
        case JXMCHomeItemTypeBindingPhone:
        {
            [self skipToBindingPhone];
        }
            break;
        case JXMCHomeItemTypeSetupUserDescription:
        {
            [self setSignDes];
        }
            break;
        case JXMCHomeItemTypeAuth:
        {
            [self pushAuthWebView];
        }
            break;
        case JXMCHomeItemTypeRecharge:
        {
            [self skipToRecharge];
        }
            break;
        case JXMCHomeItemTypeAttentedActor:
        case JXMCHomeItemTypeDrawEgg:
        case JXMCHomeItemTypeSendGift:
        case JXMCHomeItemTypeStayRoom:
        {
            [self skipToRoom];
        }
            break;
        case JXMCHomeItemTypeSign1:
        case JXMCHomeItemTypeSign2:
        case JXMCHomeItemTypeSign3:
        case JXMCHomeItemTypeSign4:
        case JXMCHomeItemTypeSign5:
        case JXMCHomeItemTypeSign6:
        case JXMCHomeItemTypeSign7:
            break;
            
        default:
            break;
    }
}

- (void)setSignDes
{
    YPMultiLineTextModifyVC *vc = (YPMultiLineTextModifyVC *)[[YPUserViewControllerFactory sharedFactory] instantiateMultiLineTextModifyViewController];
    vc.userID = [GetCore(YPAuthCoreHelp).getUid userIDValue];
    vc.maxLength = 60;
    vc.pageTitle = @"设置个性签名";
    vc.key = @"userDesc";
    [[self topViewController].navigationController pushViewController:vc animated:YES];
}

- (void)pushAuthWebView
{
    NSString *urlSting = [NSString stringWithFormat:@"%@%@",[YPHttpRequestHelper getHostUrl],@"/front/real_name/index.html"];
    
    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:urlSting];
    [[self topViewController].navigationController pushViewController:webView animated:YES];
}


/// 跳转上传图片
- (void)skipToUploadImages {
    YPEditPersonalPhotosVC *vc = (YPEditPersonalPhotosVC *)[[YPUserViewControllerFactory sharedFactory]instantiateEditPersonalPhotosViewController];
    vc.uid = [GetCore(YPAuthCoreHelp) getUid].integerValue;
    [[self topViewController].navigationController pushViewController:vc animated:YES];
}

/// 跳转首页
- (void)skipToHomePage {
    [self topViewController].tabBarController.selectedIndex = 0;
    [[self topViewController].navigationController popViewControllerAnimated:YES];
}

/// 跳转房间
- (void)skipToRoom {
    if (GetCore(YPImRoomCoreV2).isInRoom) {
        [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:GetCore(YPImRoomCoreV2).currentRoomInfo];
    } else {
        [self skipToHomePage];
    }
}

/// 跳转绑定手机
- (void)skipToBindingPhone {
    
    [MBProgressHUD showMessage:@"请稍后..."];

    [MBProgressHUD hideHUD];
    YPBindingPhoneVC *vc = (YPBindingPhoneVC *)YPWalletStoryBoard(@"YPBindingPhoneVC");
    vc.isPush = YES;
    vc.isBindPhone = NO;
    [[self topViewController].navigationController pushViewController:vc animated:YES];
    
}

/// 跳转公聊大厅
- (void)skipToPublicHall {
    YPNotiFriendVC *vc = [[YPNotiFriendVC alloc] init];
    [vc updateData];
    [[self topViewController].navigationController pushViewController:vc animated:YES];
}

/// 跳转充值
- (void)skipToRecharge {
    UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
    [[self topViewController].navigationController pushViewController:vc animated:YES];
}

/// 跳转商城
- (void)skipToCarSys {
    YPMallViewController *car = [[YPMallViewController alloc] init];
    [[self topViewController].navigationController pushViewController:car animated:YES];
}


/// 分享
- (void)shareToPlatform:(JXShareType)platform {
    YPShareInfo *info = [YPShareInfo new];
    info.desc = NSLocalizedString(ShareCoreDes2, nil);
    info.imgUrl = @"https://pic.hnyueqiang.com/logo.png";
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/download/download.html?",[YPHttpRequestHelper getHostUrl]];
    info.showUrl = urlSting;
    info.title = NSLocalizedString(ShareCoreTitle2, nil);
    NSString *url = [NSString stringWithFormat:@"%@?uid=%@",info.showUrl,[GetCore(YPAuthCoreHelp) getUid]];
    
    [YPJXShareHelper shareH5WithTitle:info.title url:url imgUrl:info.imgUrl desc:info.desc platform:platform];
    
    
    
}



#pragma mark -setter/getter

- (void)setModel:(YPMMHomeItemModel *)model
{
    _model = model;
    if (model) {
        self.nameLabel.text = model.missionName;
        self.coinLabel.text = [NSString stringWithFormat:@"点点币 +%@",model.mcoinAmount];
        
        JXMCHomeItemStatus status = [model.missionStatus integerValue];
        JXMCHomeItemType type = [model.missionId integerValue];
        
        
        
        switch (status) {
            case JXMCHomeItemStatusToDo:
                self.btn.enabled = YES;
                self.btn.selected = NO;
                break;
            case JXMCHomeItemStatusReceive:
                self.btn.enabled = YES;
                self.btn.selected = YES;
                break;
            case JXMCHomeItemStatusFinished:
                self.btn.enabled = NO;
                self.btn.selected = NO;
                break;
            default:
                break;
        }
        
        
//        NSDictionary *dict = @{
//          @(JXMCHomeItemTypeUploadImages):@"yp_sign_photo",
//          @(JXMCHomeItemTypeAttentedActor):@"yp_sign_follow",
//          @(JXMCHomeItemTypeSendPublic):@"yp_sign_chat",
//          @(JXMCHomeItemTypeBindingPhone):@"yp_sign_phone",
//          @(JXMCHomeItemTypeSetupUserDescription):@"yp_sign_setSign",
//          @(JXMCHomeItemTypeAuth):@"yp_sign_setSign",
//
//          @(JXMCHomeItemTypeShareVChatTimeline):@"yp_sign_shareCircle",
//          @(JXMCHomeItemTypeShareQZone):@"yp_sign_qqzone",
//          @(JXMCHomeItemTypeRecharge):@"yp_sign_money",
//          @(JXMCHomeItemTypeSendGift):@"yp_sign_sendGift",
//          @(JXMCHomeItemTypeDrawEgg):@"yp_sign_zadan",
//          @(JXMCHomeItemTypeStayRoom):@"yp_sign_icon_tingliu",
//          };
        
        [self.logoImageView qn_setImageImageWithUrl:model.picUrl placeholderImage:@"" type:ImageTypeUserIcon];
    }
}


@end
