//
//  YPRoomViewControllerCenter.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomViewControllerCenter.h"

#import "YPYYViewControllerCenter.h"
#import "YPRoomViewControllerFactory.h"
//c
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPRoomQueueCoreV2Help.h"
#import "HJRoomQueueCoreClient.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"

#import "YPImLoginCore.h"
#import "HJImLoginCoreClient.h"
#import "YPMeetingCore.h"
#import "HJMeetingCoreClient.h"
#import "YPLinkedMeCore.h"
#import "HJLinkedMeClient.h"
#import "YPAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "YPVersionCoreHelp.h"
#import "HJVersionCoreClient.h"

#import "YPPurseCore.h"
#import "HJAPNSCoreClient.h"
#import "HJActivityCoreClient.h"

#import "YPAppDelegate.h"




#import "YPGameRoomContainerVC.h"

#import "YPOpenRoomViewController.h"
#import "TYAlertController.h"

//view
#import "YPEnterRoomWithPwdView.h"
#import "YPRedPacketView.h"
//m
#import "YPRedInfo.h"

//t

#import "UIView+XCToast.h"
#import "YPUserCoreHelp.h"
#import "HJUserCoreClient.h"

#import "YPYYActionSheetViewController.h"
#import "HJAdCoreClient.h"
#import "HJAdUIClient.h"

#import "YPXCTurntableAlertView.h"
#import "YPWKWebViewController.h"
#import "YPMusicCore.h"
#import "YPFaceCore.h"

#import "YPTopAlertViewTool.h"
#import "YPGiftCore.h"
#import "MMAlertView.h"
#import "MMSheetView.h"

#import "YPJXAuthorizationAlertHelper.h"

//#import <JXCategories/UIApplication+JXBase.h>
#import "UIApplication+JXBase.h"
@interface YPRoomViewControllerCenter()
<
    HJRoomCoreClient,
    HJRoomQueueCoreClient,
    HJImRoomCoreClient,
    HJImRoomCoreClientV2,
    HJImLoginCoreClient,
    HJMeetingCoreClient,
    HJVersionCoreClient,
    HJAuthCoreClient,
    HJLinkedMeClient,
    HJAdCoreClient,
    HJAPNSCoreClient,
    HJAdUIClient,
    HJEnterRoomWithPwdViewDelegate,
    HJActivityCoreClient,
    RedPacketViewDelegate,
    UIGestureRecognizerDelegate,
    UINavigationControllerDelegate,
    HJUserCoreClient
>


@property(nonatomic, assign) UserID notificationUid;
@property(nonatomic, assign) RoomType needOpenRoomType;
@property(nonatomic, assign) BOOL needOpen;
@property(nonatomic, strong) YPEnterRoomWithPwdView *pwdView;
@property(nonatomic, strong) TYAlertController *pwdAlertView;
@property(nonatomic, strong) YPRedPacketView *redView;
@property(nonatomic, strong) TYAlertController *redAlert;

@property (nonatomic, strong) YPRedInfo *info;

@end

@implementation YPRoomViewControllerCenter
+ (instancetype)defaultCenter
{
    static dispatch_once_t onceToken = 0;
    static id instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    
    return instance;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJRoomCoreClient, self);
        AddCoreClient(HJRoomQueueCoreClient, self);
        AddCoreClient(HJImRoomCoreClient, self);
        AddCoreClient(HJImRoomCoreClientV2, self);
        AddCoreClient(HJImLoginCoreClient, self);
        AddCoreClient(HJMeetingCoreClient, self);
        AddCoreClient(HJLinkedMeClient, self);
        AddCoreClient(HJActivityCoreClient, self);
        AddCoreClient(HJAuthCoreClient, self);
        AddCoreClient(HJVersionCoreClient, self);
        AddCoreClient(HJAdCoreClient, self);
        AddCoreClient(HJAdUIClient, self);
        AddCoreClient(HJUserCoreClient, self);
        [[NSNotificationCenter defaultCenter] addObserver : self selector:@selector (saveTopStatusBarHeight:) name:UIApplicationDidChangeStatusBarFrameNotification object:nil];
    }
    return self;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)saveTopStatusBarHeight:(NSNotification *)noti {
    NSLog(@"saveTopStatusBarHeight ----->>>%@",noti);
    if (noti) {
        if (noti.userInfo) {
            NSValue *statusBarFrameValue = [noti.userInfo objectForKey:UIApplicationStatusBarFrameUserInfoKey];
            CGRect rect = [statusBarFrameValue CGRectValue];
            if (rect.size.height == 40) {
                self.systemOperationStatusBarIsShow = YES;
            }else {
                self.systemOperationStatusBarIsShow = NO;
            }
        }
    }
}

//开启指定类型的房间
- (void)openRoonWithType:(RoomType)type {
    [YYLogger info:@"打开自己的房间" message:@""];
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    
    @weakify(self);
    [YPHttpRequestHelper setupShouldShowErrorHUD:NO];
    [GetCore(YPRoomCoreV2Help) getRoomInfo:[GetCore(YPAuthCoreHelp) getUid].userIDValue success:^(YPChatRoomInfo *roomInfo) {
        if (roomInfo == nil) { //当前没开房
            [YYLogger info:@"当前没开房" message:@""];
            UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[GetCore(YPAuthCoreHelp) getUid].userIDValue];
            NSString *title = [NSString stringWithFormat:@"%@的房间",info.nick];
            [MBProgressHUD hideHUD];
            if (type == RoomType_Game) {
                [GetCore(YPRoomCoreV2Help) openRoom:[GetCore(YPAuthCoreHelp)getUid].userIDValue type:type title:title roomDesc:@"" backPic:@"" rewardId:nil];
            }else {
                UINavigationController *nav = [[YPRoomViewControllerFactory sharedFactory] instantiateRoomNavigationController];
                YPOpenRoomViewController *vc = (YPOpenRoomViewController *)[[YPRoomViewControllerFactory sharedFactory]instantiateOpenRoomViewController];
                vc.roomtype = type;
                nav.viewControllers = @[vc];
                [[self getCurrentVC] presentViewController:nav animated:YES completion:nil];
            }
            
        }else { //当前有开房
            [MBProgressHUD hideHUD];
            [YYLogger info:@"当前有开房" message:@""];
            YPChatRoomInfo *info = roomInfo;
            if (type == info.type) { //要开的房间跟已经开的房间一样
                if (info.valid) { //房间有效
                    [self presentRoomViewWithRoomInfo:info]; //直接进入房间
                }else{
                    if (type == RoomType_Game) {
                        [GetCore(YPRoomCoreV2Help) openRoom:[GetCore(YPAuthCoreHelp) getUid].userIDValue type:type title:info.title roomDesc:info.roomDesc backPic:@"" rewardId:nil];
                    }else {
                        UINavigationController *nav = [[YPRoomViewControllerFactory sharedFactory] instantiateRoomNavigationController];
                        YPOpenRoomViewController *vc = (YPOpenRoomViewController *)[[YPRoomViewControllerFactory sharedFactory]instantiateOpenRoomViewController];
                        vc.roomtype = type;
                        nav.viewControllers = @[vc];
                        [[self getCurrentVC] presentViewController:nav animated:YES completion:nil];
                    }
                }
                
            } else { //要开的房间跟已经开的房间不一样，需要提示用户是否要关闭之前的房间
                if (info.valid) {
                    NSString *name = [[NSString alloc]init];
                    name = NSLocalizedString(XCRoomHongpa, nil);
                    self.needOpenRoomType = type;
                    [self showTheRoomIsOpen:name];
                }else {
                    UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[GetCore(YPAuthCoreHelp) getUid].userIDValue];
                    NSString *title = [NSString stringWithFormat:@"%@的房间",info.nick];
                    [GetCore(YPRoomCoreV2Help) openRoom:[GetCore(YPAuthCoreHelp) getUid].userIDValue type:type title:title roomDesc:@"" backPic:@"" rewardId:nil];
                }
            }
            
        }
    } failure:^(NSInteger resCode, NSString *message) {
        @strongify(self);
        [MBProgressHUD hideHUD];
        [self showAuthorizationAlertWithCode:resCode message:message];
    }];
}

//根据房间所有者id。获取房间信息
- (void)presentRoomViewWithRoomOwnerUid:(UserID)ownerUid succ:(void (^)(YPChatRoomInfo *))succBlock fail:(void (^)(NSString *))failBlock{
    
    [YYLogger info:@"开始进入房间" message:@"%lld",ownerUid];
    if (ownerUid > 0) {
        @weakify(self);
        [YPHttpRequestHelper setupShouldShowErrorHUD:NO];
        [GetCore(YPRoomCoreV2Help) getRoomInfo:ownerUid success:^(YPChatRoomInfo *roomInfo) {
            [YYLogger info:@"开始进入房间--获取房间信息成功" message:@"%@",roomInfo];
            if ([GetCore(YPRoomCoreV2Help) isBeKickedWithRoomid:[NSString stringWithFormat:@"%ld",(long)roomInfo.roomId]]) {
                if (roomInfo.roomPwd.length > 0 && [GetCore(YPAuthCoreHelp) getUid].userIDValue != roomInfo.uid) {
                    [MBProgressHUD hideHUD];
                    [YYLogger info:@"进入房间--有密码" message:@"要弹密码框"];
                    YPEnterRoomWithPwdView *pwdView = [YPEnterRoomWithPwdView loadFromNib];
                    self.pwdView = pwdView;
                    self.pwdView.uid = roomInfo.uid;
                    self.pwdView.pwd = roomInfo.roomPwd;
//                    self.pwdView.layer.cornerRadius = 10;
//                    self.pwdView.layer.masksToBounds = YES;
                    self.pwdView.roomInfo = roomInfo;
                    self.pwdView.frame = CGRectMake(0, 0, 255, 246);
                    self.pwdView.delegate = self;
                    self.pwdAlertView = [TYAlertController alertControllerWithAlertView:self.pwdView preferredStyle:TYAlertControllerStyleAlert transitionAnimation:TYAlertTransitionAnimationFade];
                    if (![[self getCurrentVC] isKindOfClass:[TYAlertController class]]) {
                        [[self getCurrentVC] presentViewController:self.pwdAlertView animated:YES completion:nil];
                    }
                }else {
                    if (roomInfo) {
                        [MBProgressHUD hideHUD];
                        [YYLogger info:@"进入房间成功" message:@""];
                        !succBlock ?: succBlock(roomInfo);
                    }else {
                        [YYLogger info:@"进入房间失败" message:@"原因：没有获取到房间信息"];
                        [MBProgressHUD showError:NSLocalizedString(XCRoomTheRoomNotExit, nil)];
                    }
                }
            } else {
                [YYLogger info:@"进入房间失败" message:@"原因：已被拉黑"];
                [MBProgressHUD showSuccess:NSLocalizedString(XCRoomTheRoomNotExit, nil)];
            }
        } failure:^(NSInteger resCode, NSString *message) {
            @strongify(self);
            [MBProgressHUD hideHUD];
            !failBlock ?: failBlock(message);
            [self showAuthorizationAlertWithCode:resCode message:message];
        }];
    }else {
        [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
    }
}
//根据房间信息开房
- (void)presentRoomViewWithRoomInfo:(YPChatRoomInfo *)roomInfo{
    
    if (roomInfo == nil) {
        return;
    }
    
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    UserInfo *userInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:uid];
    
    if (uid == 0 || userInfo.nick.length == 0) {
        [YYLogger info:@"进入房间--uid为空或昵称为空" message:@"uid:%lld--nick:%@",uid,userInfo.nick];
        return;
    }

    GetCore(YPFaceCore).hideFaceList = roomInfo.hideFace;

    //当前房间信息
    YPChatRoomInfo *currentRoomInfo = [GetCore(YPRoomCoreV2Help) getCurrentRoomInfo];
    NSLog(@"%@",currentRoomInfo);
    if (self.current == nil) {//导航为nil
        [YYLogger info:@"进入房间" message:@"type:%lld",roomInfo.type];
        UIViewController *vc;
        if (roomInfo.type == RoomType_Game) {//哄趴
            BOOL isReback = NO;
            if (currentRoomInfo.roomId == roomInfo.roomId) {
                isReback = YES;
            }
            if (!isReback) {
                [GetCore(YPRoomCoreV2Help) saveCharmInfoMap:nil];
                GetCore(YPRoomCoreV2Help).messages = nil;
                GetCore(YPImRoomCoreV2).micQueue = nil;
                
            }
            
//            GetCore(YPImRoomCoreV2).currentRoomInfo =
            
            vc = (YPGameRoomContainerVC *)[[YPRoomViewControllerFactory sharedFactory]instantiateGameRoomContainerViewController];
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCRoomNoTopicJustNow, nil)];
            return;
        }

        
        UINavigationController *nav = [[YPRoomViewControllerFactory sharedFactory] instantiateRoomNavigationController];
        if (vc != nil) {
           nav.viewControllers = @[vc];
            //life
            if (@available(iOS 13.0, *)) {
                 nav.modalPresentationStyle = UIModalPresentationFullScreen;
            }
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCRoomTheRoomNotExit, nil)];
            return;
        }
        
        @weakify(self)
        //获取当前根控制器。present  包装了导航控制器的房间
        [MBProgressHUD hideHUD];
        [GetCore(YPUserCoreHelp) getUserInfo:roomInfo.uid refresh:YES success:^(UserInfo *info) {
            [YYLogger info:@"进入房间--获取房主信息" message:@"info:%@",info];
            GetCore(YPImRoomCoreV2).roomOwnerInfo = info;
            

            UIViewController *presentingViewController = [YPYYViewControllerCenter currentVisiableRootViewController];
            if ([presentingViewController isKindOfClass:[YPGameRoomContainerVC class]]) return;
            
            if ([presentingViewController isKindOfClass:[UINavigationController class]]) {
                if ([[(UINavigationController *)presentingViewController topViewController] isKindOfClass:[YPGameRoomContainerVC class]]) return;
            }
            //life
            UIViewController* currentVC = presentingViewController;
            
            if (!currentVC) {
                currentVC = [YPRoomViewControllerCenter currentVisiableRootViewController];
            }
//            [[YPYYViewControllerCenter currentVisiableRootViewController] presentViewController:nav animated:YES completion:^{
             [currentVC presentViewController:nav animated:YES completion:^{
                @strongify(self)
                
                //解决最小化，进别的房间的缓存问题
                GetCore(YPImRoomCoreV2).enterRoomInfo = roomInfo;

                
                self.current = nav;
                if (currentRoomInfo == nil) { //点击进入房间
                    GetCore(YPGiftCore).currentGiftMsgArr = [NSMutableArray array];
                    GetCore(YPGiftCore).lastGiftIsAll = NO;
                    GetCore(YPGiftCore).lastGiftInfo = nil;
                    GetCore(YPImRoomCoreV2).currentRoomInfo = roomInfo;

                    [YYLogger info:@"进入房间--进入聊天室" message:@"roomInfo.roomId:%zd",roomInfo.roomId];
                    [GetCore(YPImRoomCoreV2) enterChatRoom:roomInfo.roomId];
                } else {
                    if (currentRoomInfo.roomId != roomInfo.roomId) { //最小化a房间，点击进入b房间
                        self.currentRoomInfo = roomInfo;
                        GetCore(YPGiftCore).currentGiftMsgArr = [NSMutableArray array];
                        GetCore(YPGiftCore).lastGiftIsAll = NO;
                        GetCore(YPGiftCore).lastGiftInfo = nil;
                        [YYLogger info:@"进入房间--退出聊天室" message:@"最小化a房间，点击进入b房间--roomInfo.roomId:%zd----currentRoomInfo.roomId：%zd",roomInfo.roomId,GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
                        [GetCore(YPImRoomCoreV2) exitChatRoom:GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];//退出a房间
                    }else{
                        
                        NotifyCoreClient(HJImRoomCoreClient, @selector(minimizeEnterSuccess), minimizeEnterSuccess);
                        
                    }
                }
            }];
        }];
    } else {//在房间里面的时候nav才不会空（在房间点击查看个人资料，找到ta）
            if (currentRoomInfo.roomId != roomInfo.roomId) { //不在本房间，已经离开了房间
                [YYLogger info:@"进入房间--退出聊天室" message:@"最小化a房间，点击进入b房间--roomInfo.roomId:%zd----currentRoomInfo.roomId：%zd",roomInfo.roomId,GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
                [GetCore(YPImRoomCoreV2) exitChatRoom:GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
                @weakify(self);
                [self.current dismissViewControllerAnimated:YES completion:^{
                    @strongify(self);
                    self.current = nil;
                    [self presentRoomViewWithRoomInfo:roomInfo];
                }];
            } else { //在本房间
                [self.current popToRootViewControllerAnimated:YES];
            }
    
    }
}
//退出当前房间
- (void)dismissChannelViewWithQuitCurrentRoom:(BOOL)isQuit animation:(BOOL)animation
{
    [MMAlertView hideAll];
    [MMSheetView hideAll];
    if ([[YPYYViewControllerCenter currentViewController] isKindOfClass:[TYAlertController class]]) {
        [(TYAlertController *)[YPYYViewControllerCenter currentViewController] dismissViewControllerAnimated:NO];
//        return;
    }
    if ([[YPYYViewControllerCenter currentViewController] isKindOfClass:[YPYYActionSheetViewController class]]) {
        [(YPYYActionSheetViewController *)[YPYYViewControllerCenter currentViewController]dismiss];
    }
    
    if ([[YPYYViewControllerCenter currentViewController] isKindOfClass:[UIAlertController class]]) {
        [(UIAlertController *)[YPYYViewControllerCenter currentViewController]dismissViewControllerAnimated:NO completion:nil];
    }
    
    if (self.current != nil) {
        [self.current dismissViewControllerAnimated:animation completion:^{
            self.current = nil;
        }];
    } else {
        
    }
    if (isQuit) {
        [GetCore(YPImRoomCoreV2) exitChatRoom:GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
        [GetCore(YPMusicCore) stopMusic];
        [GetCore(YPMeetingCore) leaveMeeting:[NSString stringWithFormat:@"%ld", GetCore(YPImRoomCoreV2).currentRoomInfo.roomId]];
        GetCore(YPImRoomCoreV2).currentRoomInfo = nil;
    } else {
        
    }
}


//退出当前房间
- (void)dismissChannelViewWithQuitCurrentRoom:(BOOL)isQuit
{
    [[NSNotificationCenter defaultCenter] postNotificationName:@"dismissChannelViewNotification" object:@{@"isQuit" : @(isQuit)}];
    [self dismissChannelViewWithQuitCurrentRoom:isQuit animation:YES];
    
    if (isQuit) {
        GetCore(YPImRoomCoreV2).currentRoomInfo = nil;
    }
}

- (void)onUserBeKicked:(NSString *)roomid
{
    if (self.current != nil) {
        YPGameRoomContainerVC *vc = self.current.viewControllers[0];
        [vc beKicked];
    }
    else if (GetCore(YPImRoomCoreV2).currentRoomInfo.roomId != 0) {
        [GetCore(YPImRoomCoreV2) exitChatRoom:GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
        [GetCore(YPMusicCore) stopMusic];
    }
    
}

- (void)onMeInterChatRoomInBlackList
{
    if (self.current != nil) {
        YPGameRoomContainerVC *vc = self.current.viewControllers[0];
        [vc beInBlackList];
    }
}

- (void)onMeExitChatRoomSuccessV2
{
    if (self.currentRoomInfo != nil) {
        [GetCore(YPImRoomCoreV2) enterChatRoom:self.currentRoomInfo.roomId];
        self.currentRoomInfo = nil;
    }
}

#pragma mark - LinkMeClient
- (void)jumpInRoomWithRoomid:(NSString *)uid {
    if (uid.length > 0) {
        self.notificationUid = uid.userIDValue;
        if ([GetCore(YPImLoginCore)isImLogin] && GetCore(YPVersionCoreHelp).versionInfo) {
            dispatch_time_t delayTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0/*延迟执行时间*/ * NSEC_PER_SEC));
            @weakify(self);
            dispatch_after(delayTime, dispatch_get_main_queue(), ^{
                @strongify(self);
                [self presentRoomViewWithRoomOwnerUid:self.notificationUid succ:^(YPChatRoomInfo *roomInfo) {
                    [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
                } fail:^(NSString *errorMsg) {
                }];
                self.notificationUid = 0;
            });
        }

    }
}

#pragma mark - VersionCoreClient

- (void)onRequestVersionStatusSuccess:(YPVersionInfo *)versionInfo {
    if (versionInfo.status != Version_Notice) {
        if (self.notificationUid > 0) {
            if ([GetCore(YPImLoginCore)isImLogin]) {
                dispatch_time_t delayTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0/*延迟执行时间*/ * NSEC_PER_SEC));
                @weakify(self);
                dispatch_after(delayTime, dispatch_get_main_queue(), ^{
                    @strongify(self);
                    [self presentRoomViewWithRoomOwnerUid:self.notificationUid succ:^(YPChatRoomInfo *roomInfo) {
                        [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
                    } fail:^(NSString *errorMsg) {
                    }];
                    self.notificationUid = 0;

                });
            }
        }
    }
}

#pragma mark - ImLoginCoreClient
- (void)onImLoginSuccess
{
    if (self.notificationUid > 0) {
        if (GetCore(YPVersionCoreHelp).versionInfo.status != Version_Notice) {
            [self presentRoomViewWithRoomOwnerUid:self.notificationUid succ:^(YPChatRoomInfo *roomInfo) {
                [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
            } fail:^(NSString *errorMsg) {
            }];
            self.notificationUid = 0;

        }
    }
}

#pragma mark - RoomQueueCoreClient
- (void)thereIsNoFreePosition {
    [UIView showToastInKeyWindow:NSLocalizedString(XCRoomNoEmptySeat, nil) duration:2.0 position:YYToastPositionCenter];
}

#pragma mark - RoomCoreClient

- (void)thereIsNoMicoPrivacy {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip, nil) message:NSLocalizedString(XCRoomHaveToAgreeMic, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomSetting, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
        if (iOS10){
            
            if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                [[UIApplication sharedApplication]openURL:url options:@{}completionHandler:^(BOOL        success) {
                }];
            }
        }else {
            if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                [[UIApplication sharedApplication]openURL:url];
            }
        }
    }];
    
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {}];
    
    [alert addAction:cancel];
    [alert addAction:enter];
    if (![[[YPRoomViewControllerCenter defaultCenter]getCurrentVC] isKindOfClass:[UIAlertController class]] || ![[[YPRoomViewControllerCenter defaultCenter]getCurrentVC] isKindOfClass:[TYAlertController class]]) {
        [[[YPRoomViewControllerCenter defaultCenter]getCurrentVC]presentViewController:alert animated:YES completion:nil];
    }
}

- (void)onCloseRoomSuccess {
    [MBProgressHUD hideHUD];
    if (self.needOpen) {
        if (self.needOpenRoomType != 0) {
            UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[GetCore(YPAuthCoreHelp) getUid].userIDValue];
            NSString *title = [NSString stringWithFormat:@"%@的房间",info.nick];
            [GetCore(YPRoomCoreV2Help)openRoom:[GetCore(YPAuthCoreHelp)getUid].userIDValue type:self.needOpenRoomType title:title roomDesc:@"" backPic:@"" rewardId:nil];
        }
    }
}

- (void)onOpenRoomSuccess:(YPChatRoomInfo *)roomInfo {
    [MBProgressHUD hideHUD];
    self.needOpen = NO;
    if (roomInfo.type == RoomType_Game) {
        [self presentRoomViewWithRoomInfo:roomInfo];
    }
    
}

- (void)onOpenRoomFailth:(NSNumber *)resCode message:(NSString *)message {
    [MBProgressHUD hideHUD];
    [self showAuthorizationAlertWithCode:resCode.integerValue message:message];
}

#pragma mark - ActivityCoreClient
-(void)onReceiveP2PRedPacket:(YPRedInfo *)info {
//    if (self.redAlert) {
//        return;
//    }
    
    
//        if (info.uid == [GetCore(YPAuthCoreHelp) getUid].userIDValue) {
//
//            if (info.type == 1) {
//                self.info = info;
//                // 第一次注册
//                [GetCore(YPUserCoreHelp) roomRcmdGet];
//            }
//            else {
//
//                [self showRedPacketWithInfo:info];
//            }
//        }
}

- (void)showRedPacketWithInfo:(YPRedInfo *)info {
    if (!info.needAlert) return;
    
    YPRedPacketView *redView = [YPRedPacketView loadFromNib];
    redView.packetNum = info.packetNum;
    redView.redPacketNameLabel.text = [NSString stringWithFormat:@"收到%@福利",info.packetName];
    redView.delagate = self;
    TYAlertController *redAlert = [TYAlertController alertControllerWithAlertView:redView preferredStyle:TYAlertControllerStyleAlert transitionAnimation:TYAlertTransitionAnimationFade];
    redAlert.backgoundTapDismissEnable = YES;
    
    dispatch_time_t delayTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0/*延迟执行时间*/ * NSEC_PER_SEC));
    @weakify(self);
    dispatch_after(delayTime, dispatch_get_main_queue(), ^{
        
        @strongify(self);
        
        if (![[self getCurrentVC] isKindOfClass:[TYAlertController class]]) {
            self.redAlert = redAlert;
            [[self getCurrentVC] presentViewController:redAlert animated:YES completion:^{
                
            }];
        }
    });
}

- (void)dismissRedPacketView {
    [self.redAlert dismissViewControllerAnimated:YES];
    self.info = nil;
}

#pragma mark - UserCoreClient
//获取新人房间推荐
- (void)roomRcmdGetSuccessWithResult:(NSDictionary *)result {
    
    NSString *title = [result[@"title"] description];
    NSString *avatar = [result[@"avatar"] description];
    NSInteger roomId = [result[@"roomId"] integerValue];
    NSInteger uid = [result[@"uid"] integerValue];
    
    if (roomId == 0) {
        [self showRedPacketWithInfo:self.info];
    }
    else {
        self.info = nil;
        [[YPTopAlertViewTool shareHJTopAlertViewTool] showNewUserAlertWithViewController:[self getCurrentVC] roomId:roomId uid:uid title:title avatar:avatar];
    }
}

- (void)roomRcmdGetFailedWithMessage:(NSString *)message {
//    [self showRedPacketWithInfo:self.info];
}

#pragma mark - APNSCoreClient

- (void)onRequestToOpenRoomWithUid:(UserID)uid {
    self.notificationUid = uid;
    if ([GetCore(YPImLoginCore) isImLogin]) {
        [self presentRoomViewWithRoomOwnerUid:self.notificationUid succ:^(YPChatRoomInfo *roomInfo) {
            [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
        } fail:^(NSString *errorMsg) {
        }];
        self.notificationUid = 0;
    }
}

- (void)onRecieveRemoteNotification:(NSDictionary *)payload
{
    NSNumber* type = [payload objectForKey:@"skiptype"];
    if (type.integerValue == 2) {
        
        NSNumber *uid;
        if ([payload[@"data"] isKindOfClass:[NSDictionary class]]) {
            uid = payload[@"data"][@"uid"];
        } else {
            uid = payload[@"data"];
        }
        
        self.notificationUid = uid.userIDValue;
        if ([GetCore(YPImLoginCore) isImLogin]) {
            [self presentRoomViewWithRoomOwnerUid:self.notificationUid succ:^(YPChatRoomInfo *roomInfo) {
                [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
            } fail:^(NSString *errorMsg) {
            }];
            self.notificationUid = 0;
        }
    }
}

#pragma mark - HJEnterRoomWithPwdViewDelegate

- (void)closePwdView {
    [YYLogger info:@"进入房间--验证密码失败" message:@""];
    [self.pwdAlertView dismissViewControllerAnimated:YES];
}

- (void)closePwdViewAndNeedPresent:(YPChatRoomInfo *)roomInfo {
    
    [YYLogger info:@"进入房间--验证密码成功" message:@"roomInfo:%@",roomInfo];
    [self.pwdAlertView dismissViewControllerAnimated:YES completion:^{
        [self presentRoomViewWithRoomInfo:roomInfo];
    }];
    
}

#pragma mark - ImLoginCoreClient

- (void)onMeInterChatRoomBadNetWork {
    self.currentRoomInfo = nil;
    [self dismissChannelViewWithQuitCurrentRoom:YES animation:YES];
    [UIView showToastInKeyWindow:NSLocalizedString(XCDiscoverEnterRoomFailedTip, nil) duration:1.0 position:(YYToastPosition)YYToastPositionCenter];
}

- (void)onImKick {
    [self dismissChannelViewWithQuitCurrentRoom:YES animation:NO];
}

#pragma mark - AuthCoreClient

- (void)onLogout {
    [self dismissChannelViewWithQuitCurrentRoom:YES animation:NO];
}

#pragma mark - ImRoomCoreClient



- (void)onMeInterChatRoomFailth {
//    [UIView showToastInKeyWindow:@"房间已关闭" duration:2.0 position:(YYToastPosition)YYToastPositionCenter];
//    [self dismissChannelViewWithQuitCurrentRoom:YES animation:NO];
}

#pragma mark - Alert

- (void)showAuthorizationAlertWithCode:(NSInteger)code message:(NSString *)message {
    UIViewController *previousViewController = [UIApplication sharedApplication].jx_topViewController;
    [YPJXAuthorizationAlertHelper showAuthorizationAlertWithViewController:[UIApplication sharedApplication].jx_topViewController code:code message:message didTapActionHandler:^(UIViewController * _Nullable toViewController) {
        
        if (toViewController) {
            [previousViewController.navigationController pushViewController:toViewController animated:YES];
        }
    }];
}

- (void)showTheRoomIsOpen:(NSString *)roomname {
    NSString *message = [NSString stringWithFormat:@"%@%@,%@",NSLocalizedString(XCRoomAlreadyInNow, nil),roomname,NSLocalizedString(XCRoomIfCloseRoom, nil)];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCRoomAlreadyOpen, nil) message:message preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomClose, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        self.needOpen = YES;
        [GetCore(YPRoomCoreV2Help) closeRoom:[GetCore(YPAuthCoreHelp)getUid].userIDValue];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [MBProgressHUD hideHUD];
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
//    [self presentViewController:alert animated:YES completion:nil];
    [[self getCurrentVC] presentViewController:alert animated:YES completion:nil];
    
}

- (UIViewController *)getCurrentVC{
    
    UIViewController *rootViewController = [UIApplication sharedApplication].keyWindow.rootViewController;
    
    UIViewController *currentVC = [self getCurrentVCFrom:rootViewController];
    
    return currentVC;
}

- (UIViewController *)getCurrentVCFrom:(UIViewController *)rootVC{
    
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

- (UIViewController *)currentViewController
{
    UIWindow *keyWindow  = [UIApplication sharedApplication].keyWindow;
    UIViewController *vc = keyWindow.rootViewController;
    while (vc.presentedViewController)
    {
        vc = vc.presentedViewController;
        
        if ([vc isKindOfClass:[UINavigationController class]])
        {
            vc = [(UINavigationController *)vc visibleViewController];
        }
        else if ([vc isKindOfClass:[UITabBarController class]])
        {
            vc = [(UITabBarController *)vc selectedViewController];
        }
    }
    return vc;
}

- (UINavigationController *)currentNavigationController
{
    return [self currentViewController].navigationController;
}


- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    BOOL ok = YES; // 默认为支持右滑反回

    return ok;
}

#pragma mark - MeetingCoreClient
- (void)onMeetingQualityDown {
    [UIView showToastInKeyWindow:NSLocalizedString(XCHudNetError, nil) duration:2.0 position:(YYToastPosition)YYToastPositionBottomWithRecordButton];
}

- (void)onMeetingQualityBad {
    [UIView showToastInKeyWindow:NSLocalizedString(XCHudNetError, nil) duration:1.0 position:(YYToastPosition)YYToastPositionBottomWithRecordButton];
}

#pragma mark - AdCoreClient
- (void)onReceiveTurntableMessage {
    YPXCTurntableAlertView *view = [YPXCTurntableAlertView loadFromNib];
    
    TYAlertController *alert = [TYAlertController alertControllerWithAlertView:view preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert transitionAnimation:(TYAlertTransitionAnimation)TYAlertTransitionAnimationScaleFade];
    
    [[self getCurrentVC]presentViewController:alert animated:YES completion:^{
        
    }];
}

#pragma mark - AdUIClient
- (void)onGoBtnClick {
    if ([[self getCurrentVC] isKindOfClass:[TYAlertController class]]) { //先递归把所有弹窗清干净
        @weakify(self);
        [(TYAlertController *)[self getCurrentVC] dismissViewControllerAnimated:NO
                                                                     completion:^{
                                                                         @strongify(self);
                                                                        [self onGoBtnClick];
                                                                    }];
        
        return;
    }
    
    if (self.current) { //当前界面在房间内
        
        [self findTheWkWebViewInNav:self.current];
        
    } else { //当前界面不在房间内
        YPTabBarController *vc = (YPTabBarController *)[UIApplication  sharedApplication].keyWindow.rootViewController;
        UINavigationController *nav = vc.selectedViewController;
        [self findTheWkWebViewInNav:nav];
    }
}

- (void)findTheWkWebViewInNav:(UINavigationController *)nav {
  
    for (UIViewController *item in nav.viewControllers) {
        if ([item isKindOfClass:[YPWKWebViewController class]] && ![[self getCurrentVC] isKindOfClass:[YPWKWebViewController class]]) { //在导航堆栈里面找到这个控制器并且不是当前控制器
            [nav popToViewController:item animated:YES];
            return;
        }
    }
    
    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/luckdraw/index.html",[YPHttpRequestHelper getHostUrl]];
    webView.url = [NSURL URLWithString:urlSting];
    [nav pushViewController:webView animated:YES];


}

+ (UIViewController*) currentVisiableRootViewController
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
             if (@available(iOS 13.0, *)) {
                 UIView* vieww =  aSubViews.firstObject;
                 aSubViews =  vieww.subviews;
                }

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


    return result;
}
@end
