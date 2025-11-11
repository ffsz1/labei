//
//  HJGameRoomContainerVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomContainerVC.h"
#import "HJRoomPlayerView.h"
#import "HJRoomViewControllerCenter.h"
#import "HJRoomViewControllerFactory.h"

#import "HJGameRoomVC.h"
#import "HJGameRoomVC+Alert.h"
#import "HJRoomSettingVC.h"

#import "HJRoomCoreV2Help.h"
#import "HJImRoomCoreV2.h"
#import "RoomUIClient.h"
#import "HJGiftCoreClient.h"

#import "UIView+XCToast.h"
#import "HJGiftSpringAnimateView.h"

#import <SVGAPlayer.h>
#import <SVGAParser.h>
#import <SVGAImageView.h>

#import "MMAlertView.h"
#import "MMSheetView.h"

#import "SDImageCacheConfig.h"
#import "HJVersionCoreHelp.h"
#import "HJAllChannelGiftSpringAnimateView.h"
#import "HJRoomViewControllerCenter.h"

#import "HJUserCoreClient.h"
#import "HJUserCoreHelp.h"
#import "HJImMessageCore.h"

#import "NSObject+AutoCoding.h"
#import "HJRoomQueueCustomAttachment.h"
#import "HJGameRoomVC+ToolBar.h"
#import "HJUserCoreClient.h"
#import "UIView+XCToast.h"
#import "HJRoomPopupView.h"

#import "HJRoomMenuView.h"
#import "HJManagerSettingController.h"

#import "HJHttpRequestHelper+Room.h"
#import "HJAlerTipView.h"
#import "HJSpaceCardView.h"

static BOOL SDImageCacheOldShouldDecompressImages = YES;
static BOOL SDImagedownloderOldShouldDecompressImages = YES;

@interface HJGameRoomContainerVC ()<HJGameRoomVCDelagte,UIScrollViewDelegate,HJImRoomCoreClient, HJRoomQueueCoreClient,HJRoomCoreClient,HJGiftCoreClient,UIGestureRecognizerDelegate,UINavigationControllerDelegate,SVGAPlayerDelegate,CAAnimationDelegate,RoomUIClient,HJUserCoreClient>


@property (weak, nonatomic) IBOutlet UIImageView *roomBg;//bg
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
//vc
@property (strong, nonatomic) HJGameRoomVC *roomView; //room

//animate view
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *pageContorlHeightConstraint;
@property (weak, nonatomic) IBOutlet UIView *giftDisplayView;
@property (weak, nonatomic) IBOutlet SVGAImageView *svgaDisplayView;
@property (strong, nonatomic) SVGAParser *parser;
@property (strong, nonatomic) NSMutableArray *animateQueue;
@property (assign, nonatomic) BOOL isAnimating;
//animate model
@property (strong, nonatomic) NSMutableArray<HJGiftAllMicroSendInfo *> *giftAnimateQueue;//gift
@property (nonatomic, strong) HJGiftAllMicroSendInfo *currentInfo;

@property (strong, nonatomic) UserInfo *roomOwner;
@property (nonatomic ,strong)dispatch_source_t timer;

@property (nonatomic, assign) BOOL isUpdatePublic;




@end

@implementation HJGameRoomContainerVC
#pragma mark - life cycle

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
    
    
    SDImageCacheConfig *config = [SDImageCache sharedImageCache].config;
    config.shouldDecompressImages = NO;
    
    SDWebImageDownloader *downloder = [SDWebImageDownloader sharedDownloader];
    SDImagedownloderOldShouldDecompressImages = downloder.shouldDecompressImages;
    downloder.shouldDecompressImages = NO;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent; //返回白色
//    return UIStatusBarStyleDefault;    //返回黑色
}

- (void)dealloc {
    RemoveCoreClientAll(self);
    SDImageCacheConfig *config = [SDImageCache sharedImageCache].config;
    config.shouldDecompressImages = YES;
    
    SDWebImageDownloader *downloder = [SDWebImageDownloader sharedDownloader];
    downloder.shouldDecompressImages = SDImagedownloderOldShouldDecompressImages;
    [self.animateQueue removeAllObjects];
    [[SDImageCache sharedImageCache] clearMemory];
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    
    self.isAnimating = NO;
    for (UIView *view in self.giftDisplayView.subviews) {
        [view removeFromSuperview];
    }
    [self.animateQueue removeAllObjects];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
//    self.scrollWidth.constant = [UIScreen mainScreen].bounds.size.width * 2;
    [self addcore];
    self.svgaDisplayView.delegate = self;
    self.svgaDisplayView.alpha = 0;
    [self initView];
//    self.pageControl.currentPage = 0;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(beginAnimation) name:kApplicationDidBecomeActiveNotification object:nil];
    
}
- (void)initView {
    self.automaticallyAdjustsScrollViewInsets = NO;
    
//    [self.view addSubview:self.pageControl];
//    [self.pageControl mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.leading.mas_equalTo(self.view.mas_leading);
//        make.trailing.mas_equalTo(self.view.mas_trailing);
//        make.top.mas_equalTo(self.navTitleView.mas_bottom);
//        make.bottom.mas_equalTo(self.scrollView.mas_top);
//    }];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    for (UIView *item in self.giftDisplayView.subviews) {
        [item removeFromSuperview];
    }
}


#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString: @"HJGameRoomVC"]) {
        HJGameRoomVC * vc = (HJGameRoomVC *)segue.destinationViewController;
        self.roomView = vc;
        vc.delegate = self;
        vc.view.backgroundColor = [UIColor clearColor];
    }
}

#pragma mark - HJGameRoomVCDelagte
//退出房间
- (void)roomExit {
    
    self.scrollView.contentOffset = CGPointMake(0, 0);
    self.pageContorlHeightConstraint = 0;
    self.pageControl.hidden = YES;
    self.scrollView.scrollEnabled = NO;
}


- (void)roomCloseButtonClick {
//    [self roomCloseButtonClick:nil];
}

- (void)roomMoreShowMenuActionIsOnMic:(BOOL)isMic {
    BOOL isManager = (GetCore(HJRoomQueueCoreV2Help).myMember.is_creator || GetCore(HJRoomQueueCoreV2Help).myMember.is_manager);
        
        __weak typeof(self)weakSelf = self;
        
    //    HJRoomMenuView
        
        [HJRoomMenuView show:^(XBDRoomMenuType type) {
            
            switch (type) {
                case XBDRoomMenuTypeMusic:
                {
                    [HJRoomPlayerView show];
                }
                    break;
                case XBDRoomMenuTypeSet:
                {
                    [weakSelf roomSetAction];
                }
                    break;
                case XBDRoomMenuTypeOpenChat:
                {
                    [weakSelf openChatAction];
                }
                    break;
                case XBDRoomMenuTypeOpenCar:
                {
                    [weakSelf openCarEffect];
                }
                    break;
                case XBDRoomMenuTypeOpenGift:
                {
                    [weakSelf openGiftEffect];
                }
                    break;
                case XBDRoomMenuTypeTip:
                {
                    [weakSelf showRoomTip];
                }
                    break;
                case XBDRoomMenuTypeManager:
                {
                    [weakSelf showManagerList];
                }
                    break;
                case XBDRoomMenuTypeMin:
                {
                    //先弹屏
                    [self showAlerTipViewForClearCharmValue];
                }
                    break;
                case XBDRoomMenuTypeOut:
                {
                    [weakSelf outAction];
                }
                    break;
                case XBDRoomMenuTypeReport:
                {
                    [weakSelf onReportButtonClick];
                }
                    break;
                default:
                    break;
            }
            
        } ordinaryUserIsOnMic:isMic];
}

//更新背景图与在线人数
- (void)updateBackPicWith:(ChatRoomInfo *)info userInfo:(UserInfo *)userInfo {
    self.roomOwner = userInfo;
//    if (info.roomPwd.length > 0) { //房间有密码
//        NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:info.title];
//        NSTextAttachment *imageAttachment = [[NSTextAttachment alloc]init];
//        imageAttachment.bounds = CGRectMake(0, 0, 10, 15);
//        imageAttachment.image = [UIImage imageNamed:@"hj_home_lock"];
//        NSAttributedString *imageString = [NSAttributedString attributedStringWithAttachment:imageAttachment];
//        [str appendAttributedString:imageString];
//        self.roomNameLabel.attributedText = str;
//    }else {
//        self.roomNameLabel.text = info.title;
//    }
//
//
//    NSString *title = [NSString stringWithFormat:@"ID:%@ %ld人在线",userInfo.erbanNo,GetCore(ImRoomCoreV2).onlineNumber];
//    [self.memberCountBtn setTitle:title forState:UIControlStateNormal];

    if (info.backPic.length > 0) {
        self.effectView.hidden = YES;
        [self.roomBg qn_setImageImageWithUrl:info.backPic placeholderImage:@"room_bg" type:ImageTypeUserLibaryDetail];
    }else {
        self.effectView.hidden = NO;
        [self.roomBg qn_setImageImageWithUrl:userInfo.avatar placeholderImage:@"room_bg" type:ImageTypeRoomBg];
    }
    
    
//    [self.roomNameLabel sizeToFit];
}


#pragma mark - UIScrollViewDelegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    if (scrollView.contentOffset.x == 0) {
        [self.roomView beginAppearanceTransition:YES animated:NO];
        self.pageControl.currentPage = 0;
    } else if (scrollView.contentOffset.x == [UIScreen mainScreen].bounds.size.width) {
        [self.roomView beginAppearanceTransition:NO animated:NO];
        self.pageControl.currentPage = 1;
    }
}


#pragma mark - SVGAPlayerDelegate

- (void)svgaPlayerDidFinishedAnimation:(SVGAPlayer *)player {
    [UIView animateWithDuration:0.5 animations:^{
        self.svgaDisplayView.alpha = 0;
    }];
    
}

#pragma mark - UIGestureRecognizerDelegate

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    //   BOOL ok = YES; // 默认为支持右滑反回
    return YES;
}

#pragma mark - RoomCoreClient
- (void)onManagerAdd:(ChatRoomMember *)member {
    if ([member.account longLongValue] == [GetCore(HJAuthCoreHelp) getUid].userIDValue) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyBeManager, nil) duration:2 position:YYToastPositionCenter];
    }
}

- (void)onManagerRemove:(ChatRoomMember *)member {
    if ([member.account longLongValue] == [GetCore(HJAuthCoreHelp) getUid].userIDValue) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomYouAlreadyOutBeManager, nil) duration:2 position:YYToastPositionCenter];
    }
}

- (void)onGameRoomInfoUpdateSuccess:(ChatRoomInfo *)info isFromMessage:(BOOL)isFromMessage{
    [self updateRoomInfo:info];
    if (!isFromMessage) {
        
        [self onGameRoomInfoUpdateSuccessV2:info];
    }
}

// 关闭/开启公屏
- (void)onGameRoomInfoUpdateSuccessV2:(ChatRoomInfo *)info {
    GetCore(HJImRoomCoreV2).currentRoomInfo = info;
    
    if (GetCore(HJRoomCoreV2Help).isInRoom) {
        
        if (self.isUpdatePublic) {
            
            ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;;
            
            HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
            roomQueueAttachment.uid = [myMember.account longLongValue];
            
            Attachment *attachement = [[Attachment alloc]init];
            attachement.first = Custom_Noti_Header_Queue;
            if (info.publicChatSwitch) {
                attachement.second = Custom_Noti_Sub_Message_Close;
            }
            else {
                attachement.second = Custom_Noti_Sub_Message_Open;
            }
            attachement.data = roomQueueAttachment.encodeAttachment;
            
            [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
            
            
            [self.roomView updateToolBar];
            
            self.isUpdatePublic = NO;
        }
        
    }
}



#pragma mark - GiftCoreClient
//接受到礼物
- (void)onReceiveGift:(HJGiftAllMicroSendInfo *)giftReceiveInfo isALLChannelSend:(BOOL)isALLChannelSend {
   
    if (giftReceiveInfo.roomId == 0) {
        [self handleFaceMessage:giftReceiveInfo]; //扫描礼物队列
    }
    
    GiftInfo *info = [GetCore(HJGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeNormal];
    if (info == nil) {
        info = [GetCore(HJGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeMystic];
    }
    
    NSInteger giftTotal = 0;
    if (giftReceiveInfo.messagetype == 16) {
       
            giftTotal = giftReceiveInfo.count * giftReceiveInfo.goldPrice;
        
    }else{
        if (giftReceiveInfo.targetUids.count > 0) {
            giftTotal = giftReceiveInfo.giftNum * info.goldPrice * giftReceiveInfo.targetUids.count;
        }else {
            giftTotal = giftReceiveInfo.giftNum * info.goldPrice;
        }
    }
    
    [self.roomView receiveMicGift:giftReceiveInfo];
    if (giftTotal >= 500) {
        if (self.animateQueue.count == 0 || self.isAnimating == NO) {
            [self creatSpringView:giftReceiveInfo];//创建特效动画
            [self.animateQueue addObject:giftReceiveInfo];
        }else {
//            [self.animateQueue addObject:giftReceiveInfo];//1116不需要队列，
        }
    }
    
}

//创建特效动画
- (void)creatSpringView:(HJGiftAllMicroSendInfo *)info {
    GiftInfo *giftInfo = [GetCore(HJGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeNormal];
    if (giftInfo == nil) {
        giftInfo = [GetCore(HJGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeMystic];
    }
    NSInteger giftTotal = 0;
    if (info.messagetype == 16) {
       
            giftTotal = info.count * info.goldPrice;
        
    }else{
        if (info.targetUids.count > 0) {
            giftTotal = info.giftNum * giftInfo.goldPrice * info.targetUids.count;
        }else {
            giftTotal = info.giftNum * giftInfo.goldPrice;
        }
    }
    
    
    NSInteger stayTime = 0;
    if (giftTotal >= 500 && giftTotal < 4999) {
        stayTime = 1;
    }else if (giftTotal >= 4999 && giftTotal < 9999) {
        stayTime = 1;
    }else if (giftTotal >= 9999) {
        stayTime = 1;
    }
    
    
    CGFloat y_giftView = 100.0f;

    if (giftTotal >= 500) {
        self.isAnimating = YES;
        NSInteger roomuid = GetCore(HJImRoomCoreV2).currentRoomInfo.uid;
        if (info.roomUid == roomuid ) {
//        if (info.roomId == 0) {
            __block HJGiftSpringAnimateView *view = [HJGiftSpringAnimateView loadFromNib];
            [view setGiftReceiveInfo:info];
            view.frame = CGRectMake(-self.view.frame.size.width, y_giftView, self.view.frame.size.width, 215);
            if (info.messagetype == Custom_Noti_Header_Winning) {
                [self.giftDisplayView addSubview:view];
                [self.giftDisplayView bringSubviewToFront:view];
            }
           
            @weakify(self);
            
            [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.5 initialSpringVelocity:1 options:UIViewAnimationOptionCurveEaseInOut animations:^{
                @strongify(self);
                
                if (GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch) {
                      view.frame = CGRectMake(0, y_giftView, self.view.frame.size.width, 215);//life
                                   return;
                               }
                if (giftInfo.hasEffect) {
                    if (!giftInfo.vggUrl || giftInfo.vggUrl.length == 0) {return;}
                    [self.parser parseWithURL:[NSURL URLWithString:giftInfo.vggUrl] completionBlock:^(SVGAVideoEntity * _Nullable videoItem) {
                        if (videoItem != nil) {
                            dispatch_async(dispatch_get_main_queue(), ^{
                                self.svgaDisplayView.contentMode = UIViewContentModeScaleAspectFit;
                                self.svgaDisplayView.alpha = 1;
                            });
                            self.svgaDisplayView.loops = 1;
                            self.svgaDisplayView.clearsAfterStop = YES;
                            self.svgaDisplayView.videoItem = videoItem;
                            [self.svgaDisplayView startAnimation];
                        }
                    } failureBlock:^(NSError * _Nullable error) {
                        
                    }];
                }
                
                view.frame = CGRectMake(0, y_giftView, self.view.frame.size.width, 215);
            } completion:^(BOOL finished) {
                if (finished) {
                    [UIView animateWithDuration:0.5 delay:stayTime options:UIViewAnimationOptionLayoutSubviews animations:^{
                        @strongify(self);
                        view.frame = CGRectMake(self.view.frame.size.width, y_giftView, self.view.frame.size.width, 215);
                    } completion:^(BOOL finished) {
                        @strongify(self);
                        if (finished) {
                            [self.animateQueue removeObjectAtSafeIndex:0];
                            if (self.animateQueue.count > 0) {
                                [self creatSpringView:self.animateQueue[0]];
                            }
                            [view removeFromSuperview];
                        }
                    }];
                }
            }];
        } else {
            
            __block HJAllChannelGiftSpringAnimateView *view = [HJAllChannelGiftSpringAnimateView loadFromNib];
            [view setGiftReceiveInfo:info];
            view.frame = CGRectMake(-self.view.frame.size.width, y_giftView, self.view.frame.size.width, 215);
            if (info.messagetype == Custom_Noti_Header_Winning) {
                [self.view addSubview:view];
                [self.view bringSubviewToFront:view];
            }
           
            
            UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
                //个人信息
//                NSString *uid = [NSString stringWithFormat:@"%@",data[@"uid"]];
         
                [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationMangheGift" object:@{@"infouid" : @(info.uid)}];
                
                
                
//                 [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:info.roomId
//                [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:info.roomUid succ:^(ChatRoomInfo *roomInfo) {
//                    if (roomInfo != nil) {
//                        [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
//                    }else {
//                        [MBProgressHUD showError:NSLocalizedString(XCDiscoverEnterRoomFailedTip, nil)];
//                    }
//                } fail:^(NSString *errorMsg) {
//                    [MBProgressHUD showError:NSLocalizedString(XCDiscoverEnterRoomFailedTip, nil)];
//                }];
                
                
//                [self showAlerTipView:info];
                
            }];
//            [view addGestureRecognizer:tap];
            [view.tapView addGestureRecognizer:tap];
            
            [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.5 initialSpringVelocity:1 options:UIViewAnimationOptionCurveEaseInOut|UIViewAnimationOptionAllowUserInteraction animations:^{
                view.left = 0;
            } completion:^(BOOL finished) {
                @weakify(self);
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(stayTime * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    @strongify(self);
                    [UIView animateWithDuration:0.5 animations:^{
                        view.left = self.view.width;
                    } completion:^(BOOL finished) {
                        if (finished) {
                            [self.animateQueue removeObjectAtSafeIndex:0];
                            if (self.animateQueue.count > 0) {
                                [self creatSpringView:self.animateQueue[0]];
                            }
                            [view removeFromSuperview];
                        }
                    }];
                });
            }];
        }
    }else {
        self.isAnimating = NO;
    }
}



-(void)showAlerTipView:(HJGiftAllMicroSendInfo*)info{
    
    NSString* contentString = [NSString stringWithFormat:@"是否确定去找%@?",info.nick];
    [HJAlerTipView show:^(HJAlerTipType type) {
               switch (type) {
                          case HJAlerTipfirmType:
                          {
                          [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:info.roomUid succ:^(ChatRoomInfo *roomInfo) {
                                            if (roomInfo != nil) {
                                                [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
                                            }else {
                                                [MBProgressHUD showError:NSLocalizedString(XCDiscoverEnterRoomFailedTip, nil)];
                                            }
                                        } fail:^(NSString *errorMsg) {
                                            [MBProgressHUD showError:NSLocalizedString(XCDiscoverEnterRoomFailedTip, nil)];
                                        }];
                              
                          }
                              break;
                          case HJAlerTipCancelType:
                          {
    
    
                          }
                              break;
                       default:
                        break;
               }
    
    
    } content:contentString nick:info.nick isAttribute:YES];
}



#pragma mark - Event response


- (void)beInBlackList
{
    [YYLogger info:@"XCBaseRoomViewController" message:@"beInBlackList"];
    [self.roomView beInBlackList];
}

- (void)beKicked {
    self.scrollView.contentOffset = CGPointMake(0, 0);
    self.scrollView.scrollEnabled = NO;
    [self.roomView beKicked];
    //
}

//在线人数
- (IBAction)memberCountBtn:(UIButton *)sender {
    [UIView animateWithDuration:0.3 animations:^{
        self.scrollView.contentOffset = CGPointMake([UIScreen mainScreen].bounds.size.width, 0);
    }];
}

////close
//- (IBAction)roomCloseButtonClick:(UIButton *)sender {
//
//
//    BOOL isManager = (GetCore(HJRoomQueueCoreV2Help).myMember.is_creator || GetCore(HJRoomQueueCoreV2Help).myMember.is_manager);
//
//    __weak typeof(self)weakSelf = self;
//
////    HJRoomMenuView
//
//    [HJRoomMenuView show:^(XBDRoomMenuType type) {
//
//        switch (type) {
//            case XBDRoomMenuTypeSet:
//            {
//                [weakSelf roomSetAction];
//            }
//                break;
//            case XBDRoomMenuTypeOpenChat:
//            {
//                [weakSelf openChatAction];
//            }
//                break;
//            case XBDRoomMenuTypeOpenCar:
//            {
//                [weakSelf openCarEffect];
//            }
//                break;
//            case XBDRoomMenuTypeOpenGift:
//            {
//                [weakSelf openGiftEffect];
//            }
//                break;
//            case XBDRoomMenuTypeTip:
//            {
//                [weakSelf showRoomTip];
//            }
//                break;
//            case XBDRoomMenuTypeManager:
//            {
//                [weakSelf showManagerList];
//            }
//                break;
//            case XBDRoomMenuTypeMin:
//            {
//                //先弹屏
//                [self showAlerTipViewForClearCharmValue];
//            }
//                break;
//            case XBDRoomMenuTypeOut:
//            {
//                [weakSelf outAction];
//            }
//                break;
//            case XBDRoomMenuTypeReport:
//            {
//                [weakSelf onReportButtonClick];
//            }
//                break;
//            default:
//                break;
//        }
//
//    } ordinaryUserIsOnMic:NO];
//
//}

-(void)showAlerTipViewForClearCharmValue{
    
    NSString* contentString = [NSString stringWithFormat:@"确定将麦上用户的魅力值清零吗？"];
    [HJAlerTipView show:^(HJAlerTipType type) {
               switch (type) {
                          case HJAlerTipfirmType:
                          {
                               [self clearCharmValue];
                          }
                              break;
                          case HJAlerTipCancelType:
                          {
                          }
                              break;
                       default:
                        break;
               }
    } content:contentString nick:@"" isAttribute:NO];
}




//清空魅力值
-(void)clearCharmValue{
    ChatRoomInfo *roomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
                   [GetCore(HJRoomCoreV2Help) userReceiveRoomMicMsg:roomInfo.uid roomUid:roomInfo.roomId];
}
- (void)roomSetAction
{
    HJRoomSettingVC *vc = (HJRoomSettingVC *)[[HJRoomViewControllerFactory sharedFactory]instantiateGameRoomSettingViewController];
    
//    BOOL isManager = (GetCore(RoomQueueCoreV2).myMember.is_creator || GetCore(RoomQueueCoreV2).myMember.is_manager);
//    vc.isManager = isManager;
    
    [self.navigationController pushViewController:vc animated:YES];
}

//打开公屏
- (void)openChatAction
{
    if (GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch) {
        self.isUpdatePublic = YES;
        ChatRoomInfo *roomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
        if (GetCore(HJImRoomCoreV2).myMember.is_creator) {
            [GetCore(HJRoomCoreV2Help) updateGameRoomInfo:roomInfo.uid backPic:GetCore(HJImRoomCoreV2).currentRoomInfo.backPic title:roomInfo.title roomTopic:nil roomNotice:nil roomPassword:roomInfo.roomPwd tag:roomInfo.tagId playInfo:roomInfo.playInfo giftEffectSwitch:roomInfo.giftEffectSwitch giftCardSwitch:roomInfo.giftEffectSwitch publicChatSwitch:NO];
        }else if (GetCore(HJImRoomCoreV2).myMember.is_manager) {
            [GetCore(HJRoomCoreV2Help) managerUpdateGameRoomInfo:[GetCore(HJAuthCoreHelp)getUid].userIDValue backPic:GetCore(HJImRoomCoreV2).currentRoomInfo.backPic  title:roomInfo.title  roomTopic:nil roomNotice:nil roomPassword:roomInfo.roomPwd tag:roomInfo.tagId playInfo:roomInfo.playInfo giftEffectSwitch:roomInfo.giftEffectSwitch giftCardSwitch:roomInfo.giftEffectSwitch publicChatSwitch:NO];
            
            
        }
    }
    else {
        self.isUpdatePublic = YES;
        ChatRoomInfo *roomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
        if (GetCore(HJImRoomCoreV2).myMember.is_creator) {
            [GetCore(HJRoomCoreV2Help) updateGameRoomInfo:roomInfo.uid backPic:GetCore(HJImRoomCoreV2).currentRoomInfo.backPic title:roomInfo.title roomTopic:nil roomNotice:nil roomPassword:roomInfo.roomPwd tag:roomInfo.tagId playInfo:roomInfo.playInfo giftEffectSwitch:roomInfo.giftEffectSwitch giftCardSwitch:roomInfo.giftEffectSwitch publicChatSwitch:YES];
        }else if (GetCore(HJImRoomCoreV2).myMember.is_manager) {
            [GetCore(HJRoomCoreV2Help) managerUpdateGameRoomInfo:[GetCore(HJAuthCoreHelp)getUid].userIDValue backPic:GetCore(HJImRoomCoreV2).currentRoomInfo.backPic  title:roomInfo.title   roomTopic:nil roomNotice:nil roomPassword:roomInfo.roomPwd tag:roomInfo.tagId playInfo:roomInfo.playInfo giftEffectSwitch:roomInfo.giftEffectSwitch giftCardSwitch:roomInfo.giftEffectSwitch publicChatSwitch:YES];
        }
    }
    
    
    [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:false];
}

- (void)openCarEffect
{
    BOOL openCar = !GetCore(HJImRoomCoreV2).currentRoomInfo.giftCardSwitch;
    
    ChatRoomInfo *currentRoomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
    
    UserID uid = GetCore(HJAuthCoreHelp).getUid.userIDValue;
    NSString *title = currentRoomInfo.title;
    NSString *desStr = currentRoomInfo.roomDesc;
    NSString *notice = currentRoomInfo.roomNotice;
    NSString *bakcPic = currentRoomInfo.backPic;
    NSString *psw = currentRoomInfo.roomPwd;
//    int roomTag = currentRoomInfo.roomTag.intValue;
    int roomTag = currentRoomInfo.tagId;
    NSString *playInfo = currentRoomInfo.playInfo;
    BOOL giftEffect = currentRoomInfo.giftEffectSwitch;
    BOOL chatSwithc = currentRoomInfo.publicChatSwitch;
    
    if (GetCore(HJImRoomCoreV2).myMember.is_creator) {
        __weak typeof(self)weakSelf = self;
        [HJHttpRequestHelper updateRoomInfo:uid title:title roomDesc:desStr roomNotice:notice backPic:bakcPic roomPassword:psw tag:roomTag playInfo:playInfo giftEffectSwitch:!giftEffect giftCardSwitch:!openCar publicChatSwitch:chatSwithc success:^(ChatRoomInfo *roomInfo) {
            [GetCore(HJImRoomCoreV2) setCurrentRoomInfo:roomInfo];
            [weakSelf sendCarSwitchRoomIMMsg:roomInfo];
            
        } failure:^(NSNumber *resCode, NSString *message) {
            
        }];

    }else if (GetCore(HJImRoomCoreV2).myMember.is_manager){
        
        
        __weak typeof(self)weakSelf = self;
        [HJHttpRequestHelper managerUpdateRoomInfo:uid title:title roomDesc:desStr roomNotice:notice backPic:bakcPic roomPassword:psw playInfo:playInfo tag:roomTag giftEffectSwitch:!giftEffect giftCardSwitch:!openCar publicChatSwitch:chatSwithc success:^(ChatRoomInfo *roomInfo) {
            [GetCore(HJImRoomCoreV2) setCurrentRoomInfo:roomInfo];
            [weakSelf sendCarSwitchRoomIMMsg:roomInfo];
            
        } failure:^(NSNumber *resCode, NSString *message) {
            
        }];
    }
    
}

- (void)sendCarSwitchRoomIMMsg:(ChatRoomInfo *)info
{
    ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;;
    
    HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
    roomQueueAttachment.uid = [myMember.account longLongValue];
    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_Queue;
    if (info.giftCardSwitch) {
//        attachement.second = Custom_Noti_Sub_Car_Effect_Open;
          attachement.second = Custom_Noti_Sub_Car_Effect_Close;
    }
    else {
         attachement.second = Custom_Noti_Sub_Car_Effect_Open;
      
    }
    attachement.data = roomQueueAttachment.encodeAttachment;
    
    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
}

- (void)sendGiftSwitchRoomIMMsg:(ChatRoomInfo *)info
{
    ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;;
    
    HJRoomQueueCustomAttachment *roomQueueAttachment = [[HJRoomQueueCustomAttachment alloc]init];
    roomQueueAttachment.uid = [myMember.account longLongValue];
    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_Queue;
    if (!info.giftEffectSwitch) {
//        attachement.second = Custom_Noti_Sub_Gift_Effect_Open;
        attachement.second = Custom_Noti_Sub_Gift_Effect_Close;
    }
    else {
        
         attachement.second = Custom_Noti_Sub_Gift_Effect_Open;
    }
    attachement.data = roomQueueAttachment.encodeAttachment;
    
    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
}

- (void)openGiftEffect
{
    
    BOOL openGift = !GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch;
    
    ChatRoomInfo *currentRoomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;
    
    UserID uid = GetCore(HJAuthCoreHelp).getUid.userIDValue;
    NSString *title = currentRoomInfo.title;
    NSString *desStr = currentRoomInfo.roomDesc;
    NSString *notice = currentRoomInfo.roomNotice;
    NSString *bakcPic = currentRoomInfo.backPic;
    NSString *psw = currentRoomInfo.roomPwd;
//    int roomTag = currentRoomInfo.roomTag.intValue;
    int roomTag = currentRoomInfo.tagId;
    NSString *playInfo = currentRoomInfo.playInfo;
    BOOL carEffect = currentRoomInfo.giftCardSwitch;
    BOOL chatSwithc = currentRoomInfo.publicChatSwitch;
    
    if (GetCore(HJImRoomCoreV2).myMember.is_creator) {
        __weak typeof(self)weakSelf = self;

        [HJHttpRequestHelper updateRoomInfo:uid title:title roomDesc:desStr roomNotice:notice backPic:bakcPic roomPassword:psw tag:roomTag playInfo:playInfo giftEffectSwitch:!openGift giftCardSwitch:!carEffect publicChatSwitch:chatSwithc success:^(ChatRoomInfo *roomInfo) {
            
            [GetCore(HJImRoomCoreV2) setCurrentRoomInfo:roomInfo];
            [weakSelf sendGiftSwitchRoomIMMsg:roomInfo];
            
        } failure:^(NSNumber *resCode, NSString *message) {
            
        }];
        
    }else if (GetCore(HJImRoomCoreV2).myMember.is_manager) {
        __weak typeof(self)weakSelf = self;

        
        [HJHttpRequestHelper managerUpdateRoomInfo:uid title:title roomDesc:desStr roomNotice:notice backPic:bakcPic roomPassword:psw playInfo:playInfo tag:roomTag giftEffectSwitch:!openGift giftCardSwitch:!carEffect publicChatSwitch:chatSwithc success:^(ChatRoomInfo *roomInfo) {
            [GetCore(HJImRoomCoreV2) setCurrentRoomInfo:roomInfo];
            [weakSelf sendGiftSwitchRoomIMMsg:roomInfo];

        } failure:^(NSNumber *resCode, NSString *message) {
            
        }];
    }
}

- (void)showRoomTip
{
    UIViewController *vc = [[HJRoomViewControllerFactory sharedFactory] instantiateHJRoomSettingPlayInfoVC];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)showManagerList
{
//    HJManagerSettingController *vc = (HJManagerSettingController *)[[HJRoomViewControllerFactory sharedFactory]instantiateManagerSettingViewController];
//    vc.type = 2;
//    [self.navigationController pushViewController:vc animated:YES];
    
    UIViewController *vc = HJRoomStoryBoard(@"HJRoomManagerListVC");
    [self.navigationController pushViewController:vc animated:YES];
    
}

//推出房间
- (void)outAction
{
    [GetCore(HJRoomQueueCoreV2Help) sendLeaveRoomSuccess:^{
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
        });
    } failure:^(NSString *message) {
        [[HJRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
    }];
}

//report
- (void)onReportButtonClick {
    
    __weak typeof(self)weakSelf = self;
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"举报房名" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:8 type:2 phoneNo:nil requestId:@"HJGameRoomContainerVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"政治敏感" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:1 type:2 phoneNo:nil requestId:@"HJGameRoomContainerVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"色情低俗" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:2 type:2 phoneNo:nil requestId:@"HJGameRoomContainerVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"广告骚扰" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:3 type:2 phoneNo:nil requestId:@"HJGameRoomContainerVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"人身攻击" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(HJUserCoreHelp) userReportSaveWithUid:GetCore(HJImRoomCoreV2).roomOwnerInfo.uid reportType:4 type:2 phoneNo:nil requestId:@"HJGameRoomContainerVC"];
    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [self presentViewController:alter animated:YES completion:nil];
    
}

//举报用户/房间
- (void)userReportSaveSuccessWithType:(NSInteger)type requestId:(NSString *)requestId {
    if ([requestId isEqualToString:@"HJGameRoomContainerVC"]) {
        [MBProgressHUD showSuccess:@"举报成功，我们将尽快为你处理" toView:self.view];
        
    }
}
- (void)userReportSaveFailth:(NSString *)message type:(NSInteger)type requestId:(NSString *)requestId {
    if ([requestId isEqualToString:@"HJGameRoomContainerVC"]) {
        
    }
}




#pragma mark - private
- (void)addcore{
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJRoomQueueCoreClient, self);
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJGiftCoreClient, self);
    AddCoreClient(RoomUIClient, self);
    AddCoreClient(HJUserCoreClient, self);
}


//后台回前台后 被通知执行动画
- (void)beginAnimation{
    for (UIView *view in self.giftDisplayView.subviews) {
        [view removeFromSuperview];
    }
    [self creatSpringView:self.currentInfo];
}

//收到礼物 扫描礼物队列
- (void)handleFaceMessage:(HJGiftAllMicroSendInfo *)allMicroSendInfo {
    if (GetCore(HJGiftCore).isOpenAnimation) {
        [self.giftAnimateQueue addObject:allMicroSendInfo];
    }
    if (self.timer== nil) {
        [self startTheGiftQueueScanner];//addtimer
    }
}
//扫描礼物队列
- (void)startTheGiftQueueScanner {
    NSTimeInterval period = 0.15; //设置时间间隔
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), period * NSEC_PER_SEC, 0); //每秒执行
    @weakify(self);
    dispatch_source_set_event_handler(_timer, ^{
        @strongify(self);
        if (self.giftAnimateQueue.count > 0) {
            
            @weakify(self);
            dispatch_sync(dispatch_get_main_queue(), ^{
                @strongify(self);
                HJGiftAllMicroSendInfo * info = self.giftAnimateQueue.firstObject;
                [self creatFlyGiftIcon:info];//计算坐标
                [self.giftAnimateQueue removeObject:info];
            });
            
        } else {
            dispatch_source_cancel(_timer);
            self.timer = nil;
        }
        
    });
    
    dispatch_resume(_timer);
    self.timer = _timer;
}

//计算礼物动画坐标
- (void)creatFlyGiftIcon:(HJGiftAllMicroSendInfo *)giftReceiveInfo {
    if (giftReceiveInfo.targetUids.count == 0 && giftReceiveInfo.targetUid <= 0 ) {
        return;
    }
    GiftInfo *info = [GetCore(HJGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeNormal];
    if (info == nil) {
        info = [GetCore(HJGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeMystic];
    }
    if (giftReceiveInfo.targetUids.count > 0) {//全麦
        for (NSString *targetUid in giftReceiveInfo.targetUids) {
            CGPoint starPoint;
            CGPoint destinationPoint;
            if ([targetUid isKindOfClass:[NSString class]] || [targetUid isKindOfClass:[NSNumber class]]) {
                if (targetUid.userIDValue == self.roomOwner.uid && giftReceiveInfo.uid == self.roomOwner.uid) { //房主自己刷给自己
                    starPoint = CGPointMake([UIScreen mainScreen].bounds.size.width / 2, self.roomView.avatar.center.y + self.pageControl.frame.size.height);
                    CGPoint ownerAvatar = GetCore(HJRoomCoreV2Help).avatarPosition;
                    destinationPoint = ownerAvatar;
                    
                } else {
                    NSString *destinationPosition = [GetCore(HJRoomQueueCoreV2Help)findThePositionByUid:targetUid.userIDValue];
                    NSString *originPosition = [GetCore(HJRoomQueueCoreV2Help) findThePositionByUid:giftReceiveInfo.uid];
                    if (targetUid.userIDValue == self.roomOwner.uid) {
                        destinationPoint = CGPointMake(self.roomView.avatar.center.x, [UIApplication sharedApplication].statusBarFrame.size.height + self.pageControl.frame.size.height+self.roomView.avatar.center.y);
                    }
                    else if (destinationPosition == nil) {
                        continue;
                    }
//                    else if (destinationPosition.intValue == -1) { //收礼物的人不在麦上 那就可能是送给房主的
//                        destinationPoint = CGPointMake(self.roomView.avatar.center.x, self.roomView.avatar.center.y + self.navTitleView.frame.origin.y + self.navTitleView.frame.size.height + self.pageControl.frame.size.height);
//                    }
                    else {
                        NSMutableArray *arr = GetCore(HJRoomCoreV2Help).positionArr;
                        NSValue *pointValue = arr[[destinationPosition integerValue]];
                        CGPoint point = [pointValue CGPointValue];
                        destinationPoint = CGPointMake(point.x + self.roomView.positionView.frame.origin.x, point.y + self.roomView.positionView.frame.origin.y +  self.pageControl.frame.size.height);
                    }
                    
                    if (originPosition == nil || originPosition.intValue == -1) { //发送人不在麦序上
                        if (giftReceiveInfo.uid == self.roomOwner.uid) {
                            starPoint = CGPointMake(self.roomView.avatar.center.x, self.roomView.avatar.center.y +  self.pageControl.frame.size.height);
                        } else {
                            starPoint = CGPointMake([UIScreen mainScreen].bounds.size.width / 2, 64);
                        }
                    }else {
                        NSMutableArray *arr = GetCore(HJRoomCoreV2Help).positionArr;
                        NSValue *pointValue = [arr safeObjectAtIndex:[originPosition integerValue]];
                        CGPoint point = [pointValue CGPointValue];
                        starPoint = CGPointMake(point.x + self.roomView.positionView.frame.origin.x, point.y + self.roomView.positionView.frame.origin.y +  self.pageControl.frame.size.height);
                    }
                }
                
                dispatch_time_t timer = dispatch_time(DISPATCH_TIME_NOW, 0.5 * NSEC_PER_SEC);
                @weakify(self);
                dispatch_after(timer, dispatch_get_main_queue(), ^{
                    @strongify(self);
                    [self addTheGiftAnimationWith:starPoint destinationPoint:destinationPoint withGiftPic:[NSURL URLWithString:info.giftUrl]];
                });
            }else {
                break;
            }
        }
    }else {
        
        CGPoint starPoint;
        CGPoint destinationPoint;
        if (giftReceiveInfo.targetUid == self.roomOwner.uid && giftReceiveInfo.uid == self.roomOwner.uid) { //房主自己刷给自己
            starPoint = CGPointMake([UIScreen mainScreen].bounds.size.width / 2, self.roomView.avatar.center.y + self.pageControl.frame.size.height);
            
            destinationPoint = CGPointMake([UIScreen mainScreen].bounds.size.width / 2, self.roomView.avatar.center.y +  self.pageControl.frame.size.height);
        }
        else {
            NSString *destinationPosition = [GetCore(HJRoomQueueCoreV2Help)findThePositionByUid:giftReceiveInfo.targetUid];
            NSString *originPosition = [GetCore(HJRoomQueueCoreV2Help) findThePositionByUid:giftReceiveInfo.uid];
            if (giftReceiveInfo.targetUid == self.roomOwner.uid) {
                destinationPoint = CGPointMake(self.roomView.avatar.center.x, [UIApplication sharedApplication].statusBarFrame.size.height +  self.pageControl.frame.size.height+self.roomView.avatar.center.y);
            }
            else if (destinationPosition == nil) {
                return;
            }
//            else if (destinationPosition.intValue == -1) { //收礼物的人不在麦上 那就可能是送给房主的
//                destinationPoint = CGPointMake(self.roomView.avatar.center.x, [UIApplication sharedApplication].statusBarFrame.size.height + self.navTitleView.frame.size.height + self.pageControl.frame.size.height+self.roomView.avatar.center.y);
//            }
            else {
                NSMutableArray *arr = GetCore(HJRoomCoreV2Help).positionArr;
                NSValue *pointValue = [arr safeObjectAtIndex:[destinationPosition integerValue]];
                CGPoint point = [pointValue CGPointValue];
                
                destinationPoint = CGPointMake(point.x + self.roomView.positionView.frame.origin.x,  point.y + self.roomView.positionView. frame.origin.y + [UIApplication sharedApplication].statusBarFrame.size.height +  self.pageControl.frame.size.height);
            }
            
            if (originPosition == nil || originPosition.intValue == -1) { //发送人不在麦序上
                if (giftReceiveInfo.uid == self.roomOwner.uid) {//房主位置送礼物
                    starPoint = CGPointMake(self.roomView.avatar.center.x, self.roomView.avatar.center.y + [UIApplication sharedApplication].statusBarFrame.size.height +  self.pageControl.frame.size.height);
                }else {//游客送礼物
                    starPoint = CGPointMake([UIScreen mainScreen].bounds.size.width / 2, 64);
                }
            }else {
                NSMutableArray *arr = GetCore(HJRoomCoreV2Help).positionArr;
                NSValue *pointValue =  [arr safeObjectAtIndex:[originPosition integerValue]];
                CGPoint point = [pointValue CGPointValue];
                starPoint = CGPointMake(point.x + self.roomView.positionView.frame.origin.x, point.y + self.roomView.positionView.frame.origin.y + [UIApplication sharedApplication].statusBarFrame.size.height +  self.pageControl.frame.size.height);
            }
        }
        [self addTheGiftAnimationWith:starPoint destinationPoint:destinationPoint withGiftPic:[NSURL URLWithString:info.giftUrl]];
    }
}

//创建执行动画
- (void)addTheGiftAnimationWith:(CGPoint)orginPoint destinationPoint:(CGPoint)destinationPoint withGiftPic:(NSURL *)giftPic{
    
    __block UIImageView *giftImageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0 , 55, 55)];
    giftImageView.center = orginPoint;
    
    [giftImageView qn_setImageImageWithUrl:giftPic.absoluteString placeholderImage:nil type:ImageTypeRoomGift];
    giftImageView.alpha = 1;
    
    [self.giftDisplayView addSubview:giftImageView];
    [self.giftDisplayView bringSubviewToFront:giftImageView];
    @weakify(self);
    [UIView animateWithDuration:0.8 delay:0.5 options:UIViewAnimationOptionLayoutSubviews animations:^{
        @strongify(self);
        giftImageView.frame = CGRectMake(self.view.center.x - 82.5, self.view.center.y, 165, 165);
        
    } completion:^(BOOL finished) {
        
        [UIView animateWithDuration:0.8 delay:1 options:UIViewAnimationOptionLayoutSubviews animations:^{
            giftImageView.frame = CGRectMake(0, 0 , 55, 55);
            giftImageView.center = destinationPoint;
        } completion:^(BOOL finished) {
            [giftImageView removeFromSuperview];
        }];
    }];
}



//更新房间信息
- (void)updateRoomInfo:(ChatRoomInfo *)info {

    [self.roomView updateToolBar];
}

#pragma mark - Getter & Setter
- (SVGAParser *)parser {
    if (_parser == nil) {
        _parser = [[SVGAParser alloc]init];
    }
    return _parser;
}

- (NSMutableArray *)giftAnimateQueue {
    if (_giftAnimateQueue == nil) {
        _giftAnimateQueue = [NSMutableArray array];
    }
    return _giftAnimateQueue;
}
- (NSMutableArray *)animateQueue {
    if (_animateQueue == nil) {
        _animateQueue = [NSMutableArray array];
    }
    return _animateQueue;
}

@end
