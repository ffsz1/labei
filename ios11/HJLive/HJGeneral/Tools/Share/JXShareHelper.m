//
//  JXShareHelper.m
//  XChat
//
//  Created by gzlx on 2019/3/18.
//  Copyright © 2019年 XC. All rights reserved.
//

#import "JXShareHelper.h"

#import "HJRoomViewControllerCenter.h"
#import "HJAlertControllerCenter.h"
#import "HJFansListViewController.h"


//#import "JXShareView.h"

#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "HJShareSendInfo.h"
#import "Attachment.h"
#import "HJImRoomCoreV2.h"
#import "HJImMessageCore.h"
#import "HJShareCoreClient.h"
#import "HJHttpRequestHelper+Share.h"

@implementation JXShareHelper

+ (void)shareH5WithTitle:(NSString *)title url:(NSString *)url imgUrl:(NSString *)imgUrl desc:(NSString *)desc platform:(JXShareType)platform{
    
    GetCore(HJAuthCoreHelp).didShareSuccess = nil;
    GetCore(HJAuthCoreHelp).didShareFailed = nil;
    
    @weakify(self);
    [GetCore(HJAuthCoreHelp) setDidShareSuccess:^(NSInteger shareType) {
        @strongify(self);
        if ([url containsString:@"double12"]) {
            [self postShareSuccessDataShareType:platform sharePageId:888 targetUid:0];
        }else {
            [self postShareSuccessDataShareType:platform sharePageId:2 targetUid:0];
        }
        NotifyCoreClient(HJShareCoreClient, @selector(onShareH5Success), onShareH5Success);
    }];
    
    [GetCore(HJAuthCoreHelp) setDidShareFailed:^{
        @strongify(self);
        NotifyCoreClient(HJShareCoreClient, @selector(onShareH5Failth:), onShareH5Failth:@"分享失败");
    }];

    switch (platform) {
        case JXShareTypeWXFriend:
        {
            [GetCore(HJAuthCoreHelp) shareToWechatFriendsWithTitle:title description:desc webpageUrl:url imageUrl:imgUrl];
        }
            break;
        case JXShareTypeWXCircle:
        {
            [GetCore(HJAuthCoreHelp) shareToWechatFriendsCircleWithTitle:title description:desc webpageUrl:url imageUrl:imgUrl];
        }
            break;
        case JXShareTypeQQFriend:
        {
            [GetCore(HJAuthCoreHelp) shareToQQFriendsWithTitle:title description:desc photoUrlStr:imgUrl urlStr:url];
        }
            break;
        case JXShareTypeQQZone:
        {
            [GetCore(HJAuthCoreHelp) shareToQQQzoneWithTitle:title description:desc photoUrlStr:imgUrl urlStr:url];
        }
            break;
            
        default:
            break;
    }
    
}

+ (void)shareRoom:(UserID)uid roomUid:(UserID)roomUid platform:(JXShareType)platform {
    @weakify(self);
    
    [GetCore(HJUserCoreHelp) getUserInfo:roomUid refresh:YES success:^(UserInfo *userInfo) {
        @strongify(self);
        UserInfo *myInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:uid];
        HJShareSendInfo *info = [[HJShareSendInfo alloc]init];
        info.uid = uid;
        info.targetNick = userInfo.nick.length > 0 ? userInfo.nick : @"";
        info.targetUid = roomUid;
        info.nick = myInfo.nick.length > 0 ? myInfo.nick : @"";
        
        Attachment *attachment = [[Attachment alloc]init];
        attachment.first = Custom_Noti_Header_Room_Tip;
        attachment.second = Custom_Noti_Header_Room_Tip_ShareRoom;
        attachment.data = info.encodeAttachemt;
        
        NSString *content = [NSString stringWithFormat:@"%@", NSLocalizedString(ShareCoreDes, nil)];
        NSString * titleShare = [NSString stringWithFormat:@"%@%@",userInfo.nick, NSLocalizedString(ShareCoreTitle, nil)];
        NSString *urlSting = [NSString stringWithFormat:@"%@/front/share/share.html?shareUid=%lld&uid=%lld",[HJHttpRequestHelper getHostUrl],uid,roomUid];
        
        
        
        
        switch (platform) {
            case JXShareTypeWXFriend:
            {
                [GetCore(HJAuthCoreHelp) shareToWechatFriendsWithTitle:titleShare description:content webpageUrl:urlSting imageUrl:userInfo.avatar];
            }
                break;
            case JXShareTypeWXCircle:
            {
                [GetCore(HJAuthCoreHelp) shareToWechatFriendsCircleWithTitle:titleShare description:content webpageUrl:urlSting imageUrl:userInfo.avatar];
            }
                break;
            case JXShareTypeQQFriend:
            {
                [GetCore(HJAuthCoreHelp) shareToQQFriendsWithTitle:titleShare description:content photoUrlStr:userInfo.avatar urlStr:urlSting];
            }
                break;
            case JXShareTypeQQZone:
            {
                [GetCore(HJAuthCoreHelp) shareToQQQzoneWithTitle:titleShare description:content photoUrlStr:userInfo.avatar urlStr:urlSting];
            }
                break;
                
            default:
                break;
        }
        
        @weakify(self);
        [GetCore(HJAuthCoreHelp) setDidShareSuccess:^(NSInteger shareType) {
            @strongify(self);
            NSString *sessionId = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
            
            [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachment sessionId:sessionId type:JXIMSessionTypeChatroom];
            
            [self postShareSuccessDataShareType:platform sharePageId:1 targetUid:roomUid];
            
            NotifyCoreClient(HJShareCoreClient, @selector(onShareRoomSuccess), onShareRoomSuccess);
        }];
        
        [GetCore(HJAuthCoreHelp) setDidShareFailed:^{
            @strongify(self);
            NotifyCoreClient(HJShareCoreClient, @selector(onShareRoomFailth), onShareRoomFailth);
        }];
    }];
}

/**
 分享成功后调用
 
 @param shareType 分享类型，1微信好友，2微信朋友圈，3QQ好友，4QQ空间
 @param sharePageId 分享页面，1直播间，2H5
 @param targetUid 如果被分享房间，传被分享房间UID
 */
+ (void)postShareSuccessDataShareType:(NSInteger)shareType sharePageId:(NSInteger)sharePageId targetUid:(NSInteger)targetUid {
    [HJHttpRequestHelper postShareSuccessWithShareType:shareType sharePageId:sharePageId targetUid:targetUid success:^(NSString *packetNum) {
        NotifyCoreClient(HJShareCoreClient, @selector(onShareH5Success), onShareH5Success);
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}




+ (void)showShareViewWithType:(JXShareType)type WithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr showFriends:(BOOL)showFriends successBlock:(void(^)())successBlock failedBlock:(void(^)())failedBlock {
    switch (type) {
            case JXShareTypeWXFriend:
        {
            GetCore(HJAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(HJAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(HJAuthCoreHelp) shareToWechatFriendsWithTitle:title description:description webpageUrl:urlStr imageUrl:photoUrlStr];
        }
            break;
            case JXShareTypeWXCircle:
        {
            GetCore(HJAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(HJAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(HJAuthCoreHelp) shareToWechatFriendsCircleWithTitle:title description:description webpageUrl:urlStr imageUrl:photoUrlStr];
        }
            break;
            case JXShareTypeQQFriend:
        {
            GetCore(HJAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(HJAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(HJAuthCoreHelp) shareToQQFriendsWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr];
        }
            break;
            case JXShareTypeQQZone:
        {
            GetCore(HJAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(HJAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(HJAuthCoreHelp) shareToQQQzoneWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr];
        }
            break;
            case JXShareTypeFriend:
        {
            GetCore(HJAuthCoreHelp).didShareSuccess = nil;
            GetCore(HJAuthCoreHelp).didShareFailed = nil;
            
            HJFansListViewController *vc = HJMessageBoard(@"HJFansListViewController");
            
            [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
        }
            break;
            
        default:
            break;
    }
}

+ (UIViewController *)getCurrentVC{
    
    UIViewController *rootViewController = [UIApplication sharedApplication].keyWindow.rootViewController;
    
    UIViewController *currentVC = [self getCurrentVCFrom:rootViewController];
    
    return currentVC;
}

+ (UIViewController *)getCurrentVCFrom:(UIViewController *)rootVC{
    
    UIViewController *currentVC;
    
    if ([rootVC presentedViewController]) {
        // 视图是被presented出来的
        
        rootVC = [rootVC presentedViewController];
    }
    
    if ([rootVC isKindOfClass:[UITabBarController class]]) {
        // 根视图为UITabBarController
        
        currentVC = [self getCurrentVCFrom:[(UITabBarController *)rootVC selectedViewController]];
        
    } else if ([rootVC isKindOfClass:[UINavigationController class]]){
        // 根视图为UINavigationController
        
        currentVC = [self getCurrentVCFrom:[(UINavigationController *)rootVC visibleViewController]];
        
    } else {
        // 根视图为非导航类
        
        currentVC = rootVC;
    }
    
    return currentVC;
}

@end
