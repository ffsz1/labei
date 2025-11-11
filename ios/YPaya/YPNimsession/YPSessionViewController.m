//
//  YPSessionViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSessionViewController.h"

#import "YPSessionViewController.h"
#import "YPSessionConfig.h"
#import "YPOpenLiveAlertMessageContentView.h"
#import "YPRecPacketConentMessageView.h"
#import "YPNewsNoticeContentMessageView.h"
#import "YPGiftContentMessageView.h"
#import "YPGiftAttachment.h"
#import "YPTurntableAttachment.h"
#import "YPRedPacketInfoAttachment.h"
#import "YPNewsInfoAttachment.h"
#import "YPWKWebViewController.h"
#import "YPSDPhotoBrowser.h"
#import "NTESGlobalMacro.h"
#import "TYAlertController.h"
#import "YPUserCoreHelp.h"
#import "YPPurseViewControllerFactory.h"
#import "YPImagePreViewVC.h"
#import "YPAlertControllerCenter.h"
#import "YPRoomViewControllerCenter.h"
#import "YPUserViewControllerFactory.h"
#import "YPMySpaceVC.h"
#import "YPNTESVideoViewController.h"
#import "HJBalanceErrorClient.h"
#import "YPVersionCoreHelp.h"
#import "YPImFriendCore.h"
#import "YPGiftCore.h"
#import "HJImMessageCoreClient.h"
#import "YPImMessageCore.h"
#import "UIView+XCToast.h"

#import "YPYYActionSheetViewController.h"

#import "YPUserHandler.h"
#import "YPPurseCore.h"

#import "YPGiftBoxView.h"
#import "YPNIMMessageMaker.h"
@interface YPSessionViewController ()<NIMMessageContentViewDelegate,NIMMessageCellDelegate,SDPhotoBrowserDelegate,HJImMessageCoreClient>
@property (nonatomic,strong)    YPSessionConfig       *sessionConfig;
@property (nonatomic, strong) TYAlertController *giftAlert;

@property (nonatomic, strong) UINavigationController *temNav;
@property (nonatomic, strong) NIMMessage *currentMsg;
@property (nonatomic, copy) NSString *text;
@end

@implementation YPSessionViewController

- (void)viewDidLoad {
    [super viewDidLoad];
//    UIImageView *backImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"yp_nav_bar_back"]];
//
//    UIBarButtonItem *backBarBtn = [[UIBarButtonItem alloc] initWithCustomView:backImageView];
//    self.navigationItem.backBarButtonItem = backBarBtn;
    self.navigationItem.backBarButtonItem.tintColor = [UIColor redColor];
//    backBarBtn.customView = backImageView;
//    for (UIView *view in backBarBtn.customView.subviews) {
//        if ([view isKindOfClass:[UIImageView class]]) {
//            [UIImage imageNamed:@""]
//        }
//    }
    
    if (self.session.sessionType != 1) {
        UIBarButtonItem *rigthItem = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"yp_home_hot_more"] style:UIBarButtonItemStylePlain target:self action:@selector(showReport)];
        self.navigationItem.rightBarButtonItem = rigthItem;
    }
   
    AddCoreClient(HJBalanceErrorClient, self);
    AddCoreClient(HJImMessageCoreClient, self);
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [GetCore(YPGiftCore) requestGiftList];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    YPBlackStatusBar
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)showReport {
    
    [YPUserHandler showReport:self.sessionConfig.session.sessionId.userIDValue cancelFollowBlock:^{
//        weakSelf.isAttentioned = NO;
//        weakSelf.attentBtn.userInteractionEnabled = YES;
    }];
    
//    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc] init];
//
//    __weak typeof(self) weakSelf = self;
//
//    if ([GetCore(YPImFriendCore) isUserInBlackList:[NSString stringWithFormat:@"%@",self.sessionConfig.session.sessionId]]) {
//        [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoOutBlackList, nil) block:^(YPYYActionSheetViewController *controller) {
//            [weakSelf removeAlerView];
//        }];
//    } else {
//        [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoInBlackList, nil) block:^(YPYYActionSheetViewController *controller) {
//            [weakSelf showAlerView];
//        }];
//    }
//
//    [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoJubao, nil) block:^(YPYYActionSheetViewController *controller) {
//        [weakSelf report];
//    }];
//
//
//
//    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YPYYActionSheetViewController *controller) {
//
//    }];
//
//    [sheet show];
    
}

//导航栏右侧按钮点击方法
- (void)report {
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"政治敏感" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:1 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"色情低俗" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:2 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"广告骚扰" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:3 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"人身攻击" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPUserCoreHelp) userReportSaveWithUid:[self.sessionConfig.session.sessionId integerValue] reportType:4 type:1 phoneNo:nil requestId:@"XBDPersonVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [self presentViewController:alter animated:YES completion:nil];
    
    

}


//移出黑名单
- (void)removeAlerView {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPersonalInfoOutBlackListTitle, nil) message:NSLocalizedString(XCPersonalInfoOutBlackListMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPImFriendCore) removeFromBlackList:[NSString stringWithFormat:@"%@",self.sessionConfig.session.sessionId]];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}

//加入黑名单
- (void)showAlerView {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPersonalInfoInBlackListTitle, nil) message:NSLocalizedString(XCPersonalInfoInBlackListMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPImFriendCore) addToBlackList:[NSString stringWithFormat:@"%@",self.sessionConfig.session.sessionId]];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}

- (void)showMessage:(NSString *)msg {
    [MBProgressHUD showSuccess:msg];
}

- (id<NIMSessionConfig>)sessionConfig
{
    if (_sessionConfig == nil) {
        _sessionConfig = [[YPSessionConfig alloc] init];
        _sessionConfig.session = self.session;
    }
    return _sessionConfig;
}

- (NSDictionary *)cellActions
{
    static NSDictionary *actions = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        actions = @{@(NIMMessageTypeImage) :    @"showImage:",
                    @(NIMMessageTypeVideo) :    @"showVideo:",
                    @(NIMMessageTypeLocation) : @"showLocation:",
                    @(NIMMessageTypeFile)  :    @"showFile:",
                    @(NIMMessageTypeCustom):    @"showCustom:"};
    });
    return actions;
}

- (BOOL)onTapCell:(YPNIMKitEvent *)event {
    BOOL handled = [super onTapCell:event];
    NSString *eventName = event.eventName;
    if ([eventName isEqualToString:NIMKitEventNameTapContent])
    {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        NSDictionary *actions = [self cellActions];
        NSString *value = actions[@(message.messageType)];
        if (value) {
            SEL selector = NSSelectorFromString(value);
            if (selector && [self respondsToSelector:selector]) {
                SuppressPerformSelectorLeakWarning([self performSelector:selector withObject:message]);
                handled = YES;
            }
        }
    }else if ([eventName isEqualToString:@"HJNewsNoticeContentMessageViewClick"]) {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        [self showCustom:message];
    }else if ([eventName isEqualToString:@"XCOnRedPacketNoticClick"]) {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        [self showCustom:message];
    }else if ([eventName isEqualToString:@"HJTurntableMessageViewClick"]) {
        if (self.roomMessageListDidSelectCell) {
            self.temNav = self.roomMessageListDidSelectCell();
        }
        else {
            self.temNav = self.navigationController;
        }
        NIMMessage *message = event.messageModel.message;
        [self showCustom:message];
    }
    return handled;
}

- (void)showImage:(NIMMessage *)message {
    NIMImageObject *object = (NIMImageObject *)message.messageObject;
    YPImagePreViewVC *vc = [[YPImagePreViewVC alloc]init];
    vc.ImageUrl = [object url];
    [self.temNav pushViewController:vc animated:YES];
    
}

- (void)showCustom:(NIMMessage *)message
{
    
    NIMCustomObject *customObject = (NIMCustomObject*)message.messageObject;
    
    //普通的自定义消息点击事件可以在这里做哦~
    if ([customObject.attachment isKindOfClass:[YPNewsInfoAttachment class]]) {
        YPNewsInfoAttachment *info = (YPNewsInfoAttachment *)customObject.attachment;
        YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
        webView.url = [NSURL URLWithString:info.skipUrl];
        [self.temNav pushViewController:webView animated:YES];
    }else if ([customObject.attachment isKindOfClass:[YPRedPacketInfoAttachment class]]) {
        

    }else if ([customObject.attachment isKindOfClass:[YPTurntableAttachment class]]) {
        YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
        NSString *urlSting = [NSString stringWithFormat:@"%@/front/luckdraw/index.html",[YPHttpRequestHelper getHostUrl]];
        webView.url = [NSURL URLWithString:urlSting];
        [self.temNav pushViewController:webView animated:YES];
    }
}

- (void)onTapSendGift:(YPNIMMediaItem *)item {
//    NSLog(@"%d",[GetCore(ImFriendCore) isUserInBlackList:[GetCore(AuthCore) getUid]]);
    
    [YPGiftBoxView showChat:self.session.sessionId.userIDValue];
    
}


#pragma mark - NIMMessageCellDelegate
- (BOOL)onTapAvatar:(NSString *)userId {
    
    if (self.roomMessageListDidSelectCell) {
        self.temNav = self.roomMessageListDidSelectCell();
    }
    else {
        self.temNav = self.navigationController;
    }

    YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
    vc.userID = userId.userIDValue;
    
    [self.temNav pushViewController:vc animated:YES];
    return YES;
}

- (void)showVideo:(NIMMessage *)message
{
    NIMVideoObject *object = (NIMVideoObject *)message.messageObject;
    YPNTESVideoViewController *playerViewController = [[YPNTESVideoViewController alloc] initWithVideoObject:object];
    [self.temNav pushViewController:playerViewController animated:YES];
    if(![[NSFileManager defaultManager] fileExistsAtPath:object.coverPath]){
        //如果封面图下跪了，点进视频的时候再去下一把封面图
        __weak typeof(self) wself = self;
        [[NIMSDK sharedSDK].resourceManager download:object.coverUrl filepath:object.coverPath progress:nil completion:^(NSError *error) {
            if (!error) {
                [wself uiUpdateMessage:message];
            }
        }];
    }
}

#pragma mark - BalanceErrorClient
- (void)onBalanceNotEnough {
    @weakify(self);
    [YPAlertControllerCenter defaultCenter].dismissComplete = ^{
        @strongify(self);
        [self showBalanceNotEnougth];
        
    };
    [[YPAlertControllerCenter defaultCenter]dismissAlertNeedBlock:YES];
    
}


- (void)showBalanceNotEnougth {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPurseNoMoneyTitle, nil) message:NSLocalizedString(XCPurseNoMoneyMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [self.temNav pushViewController:vc animated:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}
- (NSTimeInterval)audioDurationFromURL:(NSString *)url {
    AVURLAsset *audioAsset = nil;
    NSDictionary *dic = @{AVURLAssetPreferPreciseDurationAndTimingKey:@(YES)};
    if ([url hasPrefix:@"http://"]) {
        audioAsset = [AVURLAsset URLAssetWithURL:[NSURL URLWithString:url] options:dic];
    }else {
        audioAsset = [AVURLAsset URLAssetWithURL:[NSURL fileURLWithPath:url] options:dic];
    }
    CMTime audioDuration = audioAsset.duration;
    float audioDurationSeconds = CMTimeGetSeconds(audioDuration);
    return audioDurationSeconds;
}

- (BOOL)recordFileCanBeSend:(NSString *)filepath {
    //根据本地文件路径获取录音文件时长
    CGFloat time = [self audioDurationFromURL:filepath];
    if (time < 1) {
        [MBProgressHUD showError:@"说话时间太短"];
        return NO;
    }else {
        return YES;
    }
}

- (void)sendMessage:(NIMMessage *)message {
    
    if (message.messageType == NIMMessageTypeText) {
        if (message.text.length && !self.text.length) {
            
            self.currentMsg = message;
            self.text = [message.text copy];
            @weakify(self);
            [GetCore(YPImMessageCore) sensitiveWordRegexWithText:message.text requestId:NSStringFromClass([self class]) finishBlock:^(BOOL isCanSend, NSString *msg) {
                @strongify(self);
                if (isCanSend) {
                    [self sendMessage:self.currentMsg];
                    self.text = nil;
                }
                else {
                    [MBProgressHUD showError:msg toView:[UIApplication sharedApplication].keyWindow];
                    self.text = nil;
                }
            }];
        }
        else {
            [super sendMessage:message];
        }
    }
//    else if (message.messageType == NIMMessageTypeAudio){
//        NIMAudioObject *audioObject = (NIMAudioObject*)message.messageObject;
//        NSLog(@"message----%@",message);
//        NSLog(@"audioObject----%@",audioObject);
//        NSLog(@"audioObject.duration----%zd",audioObject.duration);
//        NSInteger time = (audioObject.duration+500)/1000;
////        audioObject.sourcePathP;
//        if (time == 0) {
//            [MBProgressHUD showError:@"说话时间太短"];
//        }else {
//            [super sendMessage:message];
//        }
//    }
    else {
        [super sendMessage:message];
    }
}
- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    
    //设置所有消息的右边气泡底图
    [self setNomralBubbleStytle];
       [self setLeftNomralBubbleStytle];
    [self setHighlightBubbleStytle];
}
- (void)setLeftNomralBubbleStytle{
    [YPNIMKit sharedKit].config.leftBubbleSettings.textSetting.textColor = [UIColor colorWithHexString:@"#316AFF"];
       [YPNIMKit sharedKit].config.leftBubbleSettings.audioSetting.textColor= [UIColor colorWithHexString:@"#316AFF"];
    [YPNIMKit sharedKit].config.leftBubbleSettings.textSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       [YPNIMKit sharedKit].config.leftBubbleSettings.audioSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       [YPNIMKit sharedKit].config.leftBubbleSettings.imageSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       [YPNIMKit sharedKit].config.leftBubbleSettings.fileSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.videoSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.locationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.tipSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.robotSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.unsupportSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.teamNotificationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.chatroomNotificationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
       
       [YPNIMKit sharedKit].config.leftBubbleSettings.netcallNotificationSetting.normalBackgroundImage =  [self getBubbleLeftImage];
}
//设置右气泡正常颜色
- (void)setNomralBubbleStytle
{
    [YPNIMKit sharedKit].config.rightBubbleSettings.textSetting.textColor = [UIColor colorWithHexString:@"#316AFF"];
       [YPNIMKit sharedKit].config.rightBubbleSettings.audioSetting.textColor= [UIColor colorWithHexString:@"#316AFF"];
    [YPNIMKit sharedKit].config.rightBubbleSettings.textSetting.normalBackgroundImage =  [self getBubbleImage];
    [YPNIMKit sharedKit].config.rightBubbleSettings.audioSetting.normalBackgroundImage =  [self getBubbleImage];
    [YPNIMKit sharedKit].config.rightBubbleSettings.imageSetting.normalBackgroundImage =  [self getBubbleImage];
    [YPNIMKit sharedKit].config.rightBubbleSettings.fileSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.videoSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.locationSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.tipSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.robotSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.unsupportSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.teamNotificationSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.chatroomNotificationSetting.normalBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.netcallNotificationSetting.normalBackgroundImage =  [self getBubbleImage];
}

//设置右气泡高亮颜色
- (void)setHighlightBubbleStytle
{
    [YPNIMKit sharedKit].config.rightBubbleSettings.textSetting.highLightBackgroundImage =  [self getBubbleImage];
    [YPNIMKit sharedKit].config.rightBubbleSettings.audioSetting.highLightBackgroundImage =  [self getBubbleImage];
    [YPNIMKit sharedKit].config.rightBubbleSettings.imageSetting.highLightBackgroundImage =  [self getBubbleImage];
    [YPNIMKit sharedKit].config.rightBubbleSettings.fileSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.videoSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.locationSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.tipSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.robotSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.unsupportSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.teamNotificationSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.chatroomNotificationSetting.highLightBackgroundImage =  [self getBubbleImage];
    
    [YPNIMKit sharedKit].config.rightBubbleSettings.netcallNotificationSetting.highLightBackgroundImage =  [self getBubbleImage];
}
- (UIImage *)getBubbleLeftImage
{
    return [[UIImage imageNamed:@"yp_message_session_bg_left"] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
}
- (UIImage *)getBubbleImage
{
    return [[UIImage imageNamed:@"yp_message_session_bg_right"] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
}

- (UIImage *)getBubbleImage_Sel
{
    return [[UIImage imageNamed:@"yp_message_session_bg_highlight_right"] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
}


@end
