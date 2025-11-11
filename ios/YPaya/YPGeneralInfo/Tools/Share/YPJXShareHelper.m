//
//  YPJXShareHelper.m
//  XChat
//
//  Created by gzlx on 2019/3/18.
//  Copyright © 2019年 XC. All rights reserved.
//

#import "YPJXShareHelper.h"

#import "YPRoomViewControllerCenter.h"
#import "YPAlertControllerCenter.h"
#import "YPFansListViewController.h"


//#import "JXShareView.h"

#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"
#import "YPShareSendInfo.h"
#import "YPAttachment.h"
#import "YPImRoomCoreV2.h"
#import "YPImMessageCore.h"
#import "HJShareCoreClient.h"
#import "YPHttpRequestHelper+Share.h"

@implementation YPJXShareHelper

+ (void)shareH5WithTitle:(NSString *)title url:(NSString *)url imgUrl:(NSString *)imgUrl desc:(NSString *)desc platform:(JXShareType)platform{
    
    GetCore(YPAuthCoreHelp).didShareSuccess = nil;
    GetCore(YPAuthCoreHelp).didShareFailed = nil;
    
    @weakify(self);
    [GetCore(YPAuthCoreHelp) setDidShareSuccess:^(NSInteger shareType) {
        @strongify(self);
        if ([url containsString:@"double12"]) {
            [self postShareSuccessDataShareType:platform sharePageId:888 targetUid:0];
        }else {
            [self postShareSuccessDataShareType:platform sharePageId:2 targetUid:0];
        }
        NotifyCoreClient(HJShareCoreClient, @selector(onShareH5Success), onShareH5Success);
    }];
    
    [GetCore(YPAuthCoreHelp) setDidShareFailed:^{
        @strongify(self);
        NotifyCoreClient(HJShareCoreClient, @selector(onShareH5Failth:), onShareH5Failth:@"分享失败");
    }];

    switch (platform) {
        case JXShareTypeWXFriend:
        {
            [GetCore(YPAuthCoreHelp) shareToWechatFriendsWithTitle:title description:desc webpageUrl:url imageUrl:imgUrl];
        }
            break;
        case JXShareTypeWXCircle:
        {
            [GetCore(YPAuthCoreHelp) shareToWechatFriendsCircleWithTitle:title description:desc webpageUrl:url imageUrl:imgUrl];
        }
            break;
        case JXShareTypeQQFriend:
        {
            [GetCore(YPAuthCoreHelp) shareToQQFriendsWithTitle:title description:desc photoUrlStr:imgUrl urlStr:url];
        }
            break;
        case JXShareTypeQQZone:
        {
            [GetCore(YPAuthCoreHelp) shareToQQQzoneWithTitle:title description:desc photoUrlStr:imgUrl urlStr:url];
        }
            break;
            
        default:
            break;
    }
    
}

+ (void)shareRoom:(UserID)uid roomUid:(UserID)roomUid platform:(JXShareType)platform {
    @weakify(self);
    
    [GetCore(YPUserCoreHelp) getUserInfo:roomUid refresh:YES success:^(UserInfo *userInfo) {
        @strongify(self);
        UserInfo *myInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:uid];
        YPShareSendInfo *info = [[YPShareSendInfo alloc]init];
        info.uid = uid;
        info.targetNick = userInfo.nick.length > 0 ? userInfo.nick : @"";
        info.targetUid = roomUid;
        info.nick = myInfo.nick.length > 0 ? myInfo.nick : @"";
        
        YPAttachment *attachment = [[YPAttachment alloc]init];
        attachment.first = Custom_Noti_Header_Room_Tip;
        attachment.second = Custom_Noti_Header_Room_Tip_ShareRoom;
        attachment.data = info.encodeAttachemt;
        
        NSString *content = [NSString stringWithFormat:@"%@", NSLocalizedString(ShareCoreDes, nil)];
        NSString * titleShare = [NSString stringWithFormat:@"%@%@",userInfo.nick, NSLocalizedString(ShareCoreTitle, nil)];
        NSString *urlSting = [NSString stringWithFormat:@"%@/front/share/share.html?shareUid=%lld&uid=%lld",[YPHttpRequestHelper getHostUrl],uid,roomUid];
        
        
        
        
        switch (platform) {
            case JXShareTypeWXFriend:
            {
                [GetCore(YPAuthCoreHelp) shareToWechatFriendsWithTitle:titleShare description:content webpageUrl:urlSting imageUrl:userInfo.avatar];
            }
                break;
            case JXShareTypeWXCircle:
            {
                [GetCore(YPAuthCoreHelp) shareToWechatFriendsCircleWithTitle:titleShare description:content webpageUrl:urlSting imageUrl:userInfo.avatar];
            }
                break;
            case JXShareTypeQQFriend:
            {
                [GetCore(YPAuthCoreHelp) shareToQQFriendsWithTitle:titleShare description:content photoUrlStr:userInfo.avatar urlStr:urlSting];
            }
                break;
            case JXShareTypeQQZone:
            {
                [GetCore(YPAuthCoreHelp) shareToQQQzoneWithTitle:titleShare description:content photoUrlStr:userInfo.avatar urlStr:urlSting];
            }
                break;
                
            default:
                break;
        }
        
        @weakify(self);
        [GetCore(YPAuthCoreHelp) setDidShareSuccess:^(NSInteger shareType) {
            @strongify(self);
            NSString *sessionId = [NSString stringWithFormat:@"%ld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
            
            [GetCore(YPImMessageCore)sendCustomMessageAttachement:attachment sessionId:sessionId type:JXIMSessionTypeChatroom];
            
            [self postShareSuccessDataShareType:platform sharePageId:1 targetUid:roomUid];
            
            NotifyCoreClient(HJShareCoreClient, @selector(onShareRoomSuccess), onShareRoomSuccess);
        }];
        
        [GetCore(YPAuthCoreHelp) setDidShareFailed:^{
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
    [YPHttpRequestHelper postShareSuccessWithShareType:shareType sharePageId:sharePageId targetUid:targetUid success:^(NSString *packetNum) {
        NotifyCoreClient(HJShareCoreClient, @selector(onShareH5Success), onShareH5Success);
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}




+ (void)showShareViewWithType:(JXShareType)type WithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr showFriends:(BOOL)showFriends successBlock:(void(^)())successBlock failedBlock:(void(^)())failedBlock {
    switch (type) {
            case JXShareTypeWXFriend:
        {
            GetCore(YPAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(YPAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(YPAuthCoreHelp) shareToWechatFriendsWithTitle:title description:description webpageUrl:urlStr imageUrl:photoUrlStr];
        }
            break;
            case JXShareTypeWXCircle:
        {
            GetCore(YPAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(YPAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(YPAuthCoreHelp) shareToWechatFriendsCircleWithTitle:title description:description webpageUrl:urlStr imageUrl:photoUrlStr];
        }
            break;
            case JXShareTypeQQFriend:
        {
            GetCore(YPAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(YPAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(YPAuthCoreHelp) shareToQQFriendsWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr];
        }
            break;
            case JXShareTypeQQZone:
        {
            GetCore(YPAuthCoreHelp).didShareSuccess = successBlock;
            GetCore(YPAuthCoreHelp).didShareFailed = failedBlock;
            [GetCore(YPAuthCoreHelp) shareToQQQzoneWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr];
        }
            break;
            case JXShareTypeFriend:
        {
            GetCore(YPAuthCoreHelp).didShareSuccess = nil;
            GetCore(YPAuthCoreHelp).didShareFailed = nil;
            
            YPFansListViewController *vc = YPMessageBoard(@"YPFansListViewController");
            
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
