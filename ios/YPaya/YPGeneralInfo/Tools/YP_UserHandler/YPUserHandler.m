//
//  YPUserHandler.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPUserHandler.h"
#import <NIMSDK/NIMSDK.h>

#import "YPHttpRequestHelper+User.h"
#import "YPHttpRequestHelper+Praise.h"

#import "YPImRoomCoreV2.h"
#import "YPShareSendInfo.h"
#import "YPImMessageCore.h"
@implementation YPUserHandler

+ (void)showReport:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock
{
    BOOL isInBlackList = [[NIMSDK sharedSDK].userManager isUserInBlackList:[NSString stringWithFormat:@"%lld",tagUID]];
    
    __weak typeof(self) weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"举报" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf report:tagUID];
    }]];
    
    if (isInBlackList) {
        [alter addAction:[UIAlertAction actionWithTitle:@"移出黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [weakSelf removeBlackList:tagUID];
        }]];
    }else{
        [alter addAction:[UIAlertAction actionWithTitle:@"加入黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [weakSelf showAddBlackList:tagUID cancelFollowBlock:cancelFollowBlock];
        }]];
    }
    
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [[self delaultRootViewController] presentViewController:alter animated:YES completion:nil];
    
    

}


+ (void)report:(UserID)tagUID{
    
    __weak typeof(self) weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"举报头像" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf reportAvatar:tagUID];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"举报昵称" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf reportPost:tagUID reportType:6 type:1];
    }]];
    
    if ([self isOnMySpaceVC]) {
        [alter addAction:[UIAlertAction actionWithTitle:@"举报相册" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [weakSelf reportPhoto:tagUID];
        }]];
    }

    [alter addAction:[UIAlertAction actionWithTitle:@"政治敏感" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf reportPost:tagUID reportType:1 type:1];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"色情低俗" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf reportPost:tagUID reportType:2 type:1];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"广告骚扰" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf reportPost:tagUID reportType:3 type:1];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"人身攻击" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf reportPost:tagUID reportType:4 type:1];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [[self delaultRootViewController] presentViewController:alter animated:YES completion:nil];
}

+ (void)reportPost:(NSInteger)uid
        reportType:(NSInteger)reportType
              type:(NSInteger)type
{
    [YPHttpRequestHelper userReportSaveWithUid:uid reportType:reportType type:type phoneNo:@"" success:^{
        [MBProgressHUD showSuccess:@"举报成功，我们将尽快为你处理" toView:[UIApplication sharedApplication].keyWindow];

    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
    }];
}


+ (void)reportAvatar:(NSInteger)uid
{
    [YPHttpRequestHelper reportAvatar:uid success:^{
        [MBProgressHUD showSuccess:@"举报成功，我们将尽快为你处理" toView:[UIApplication sharedApplication].keyWindow];
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
    }];
}

+ (void)reportPhoto:(NSInteger)uid
{
    [YPHttpRequestHelper reportAlbum:uid success:^{
        [MBProgressHUD showSuccess:@"举报成功，我们将尽快为你处理" toView:[UIApplication sharedApplication].keyWindow];
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
    }];
}


+ (void)removeBlackList:(UserID)tagUID{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"是否确定移出黑名单？" message:@"移出黑名单后你能收到用户发送的消息" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [MBProgressHUD showMessage:@"" toView:[UIApplication sharedApplication].keyWindow];
        [[NIMSDK sharedSDK].userManager removeFromBlackBlackList:[NSString stringWithFormat:@"%lld",tagUID] completion:^(NSError * _Nullable error) {
            [MBProgressHUD hideHUD];
            if (error == nil) {
                [MBProgressHUD showSuccess:@"移出黑名单成功" toView:[UIApplication sharedApplication].keyWindow];
            } else {
                [MBProgressHUD showSuccess:@"移出黑名单失败" toView:[UIApplication sharedApplication].keyWindow];
            }
        }];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [[self delaultRootViewController] presentViewController:alert animated:YES completion:nil];
}




//加入黑名单
+ (void)showAddBlackList:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock{
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"是否确定加入黑名单？" message:@"加入黑名单后你不能收到用户发送的消息" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf addBlackListPost:tagUID cancelFollowBlock:cancelFollowBlock];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [[self delaultRootViewController] presentViewController:alert animated:YES completion:nil];
}

+ (void)addBlackListPost:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock
{
    [MBProgressHUD showMessage:@"请稍后..."];
    __weak typeof(self)weakSelf = self;
    
    [[NIMSDK sharedSDK].userManager addToBlackList:[NSString stringWithFormat:@"%lld",tagUID] completion:^(NSError * _Nullable error) {
        if (error == nil) {
            
            if (cancelFollowBlock) {
                [weakSelf cancelAttendPost:tagUID cancelFollowBlock:cancelFollowBlock];
            }else{
                [MBProgressHUD showSuccess:@"加入黑名单成功"];

            }
            
        } else {
            
            [MBProgressHUD showSuccess:@"加入黑名单失败"];
            
        }
    }];
}

+ (void)cancelAttendPost:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock
{
    NSString* mine = [GetCore(YPAuthCoreHelp) getUid];
    
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper cancel:mine.userIDValue beCanceledUid:tagUID success:^{
        
        [MBProgressHUD hideHUD];
        
        if (cancelFollowBlock) {
            cancelFollowBlock();
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD showSuccess:message];
    }];
}


//取消关注
+ (void)showCancelAttentionAlert:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"是否确定取消关注？" message:@"取消关注后将会收不到主播开播通知" preferredStyle:UIAlertControllerStyleAlert];
    __weak typeof(self)weakSelf = self;
    UIAlertAction *enter = [UIAlertAction actionWithTitle:@"取消关注" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        NSString* mine = [GetCore(YPAuthCoreHelp) getUid];
        [MBProgressHUD showMessage:@"取消关注中..."];
        [weakSelf cancelAttendPost:tagUID cancelFollowBlock:cancelFollowBlock];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    
    
    
    [[self delaultRootViewController] presentViewController:alert animated:YES completion:nil];
}

+ (void)cancelFollow:(UserID)tagUID cancelFollowBlock:(void (^)())cancelFollowBlock
{
    [MBProgressHUD showMessage:@"取消关注中..."];
    [self cancelAttendPost:tagUID cancelFollowBlock:cancelFollowBlock];
}

+ (void)follow:(UserID)followUID followSucceed:(void (^)())followBlock
{
    [MBProgressHUD showMessage:@"关注中..."];

    __block UserID tagUID = followUID;
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper praise:[GetCore(YPAuthCoreHelp) getUid].userIDValue bePraisedUid:followUID success:^{
        
        [MBProgressHUD hideHUD];
        
        if (tagUID  == GetCore(YPImRoomCoreV2).currentRoomInfo.uid) {
            [weakSelf sendIMMsg_FollowRoomOwner:tagUID];
        }
        
        if (followBlock) {
            followBlock();
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD showSuccess:message];
    }];
}

//需求：如果关注了房主，在房间发送关注房主的消息
+ (void)sendIMMsg_FollowRoomOwner:(UserID)bePraisedUid
{
    UserInfo *userInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:bePraisedUid];
    UserInfo *myInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:[GetCore(YPAuthCoreHelp) getUid].userIDValue];
    YPShareSendInfo *info = [[YPShareSendInfo alloc]init];
    info.uid = myInfo.uid;
    info.targetNick = userInfo.nick;
    info.targetUid = bePraisedUid;
    info.nick = myInfo.nick;
    
    YPAttachment *attachment = [[YPAttachment alloc]init];
    attachment.first = Custom_Noti_Header_Room_Tip;
    attachment.second = Custom_Noti_Header_Room_Tip_Attentent_Owner;
    attachment.data = info.encodeAttachemt;
    
    NSString *sessionId = [NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
    [GetCore(YPImMessageCore)sendCustomMessageAttachement:attachment sessionId:sessionId type:NIMSessionTypeChatroom];
}

//检测是否在空间页面
+ (BOOL)isOnMySpaceVC
{
    UIViewController *vc = self.delaultRootViewController;
    
    BOOL hasPhotoReport = NO;
    if ([vc.className isEqualToString:@"YPBaseNavigationController"]) {
        UINavigationController *navi = (UINavigationController *)vc;
        
        for (UIViewController *childVC in navi.childViewControllers) {
            if ([childVC.className isEqualToString:@"YPMySpaceVC"]) {
                hasPhotoReport = YES;
            }
        }
        
    }else if ([vc.className isEqualToString:@"YPTabBarController"]){
        
        UITabBarController *tab = (UITabBarController *)vc;
        for (UIViewController *childVC in tab.selectedViewController.childViewControllers) {
            if ([childVC.className isEqualToString:@"YPMySpaceVC"]) {
                hasPhotoReport = YES;
            }
        }
        
    }
    return hasPhotoReport;
}

+ (UIViewController *)delaultRootViewController
{
    __block UIViewController *result = nil;
    // Try to find the root view controller programmically
    // Find the top window (that is not an alert view or other window)
    UIWindow *topWindow = [[UIApplication sharedApplication] keyWindow];
    if (topWindow.windowLevel != UIWindowLevelNormal)
    {
        NSArray *windows = [[UIApplication sharedApplication] windows];
        for(topWindow in windows)
        {
            if (topWindow.windowLevel == UIWindowLevelNormal)
                break;
        }
    }
    
    NSArray *windowSubviews = [topWindow subviews];
    
    [windowSubviews enumerateObjectsWithOptions:NSEnumerationReverse usingBlock:
     ^(id obj, NSUInteger idx, BOOL *stop) {
         UIView *rootView = obj;
         
         if ([NSStringFromClass([rootView class]) isEqualToString:@"UITransitionView"]) {
             
             NSArray *aSubViews = rootView.subviews;
             
             [aSubViews enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
                 UIView *tempView = obj;
                 
                 id nextResponder = [tempView nextResponder];
                 
                 if ([nextResponder isKindOfClass:[UIViewController class]]) {
                     result = nextResponder;
                     *stop = YES;
                 }
             }];
             *stop = YES;
         } else {
             
             id nextResponder = [rootView nextResponder];
             
             if ([nextResponder isKindOfClass:[UIViewController class]]) {
                 result = nextResponder;
                 *stop = YES;
             }
         }
     }];
    
    if (result == nil && [topWindow respondsToSelector:@selector(rootViewController)] && topWindow.rootViewController != nil) {
        result = topWindow.rootViewController;
    }
    
    return result;
    
}

@end
