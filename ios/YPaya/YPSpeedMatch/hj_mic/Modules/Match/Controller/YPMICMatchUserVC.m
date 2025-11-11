//
//  YPMICMatchUserVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMICMatchUserVC.h"
#import "YPMICRecordVC.h"
#import "YPRoomViewControllerCenter.h"

#import "YPMICMatchUserView.h"
#import "YPMICMatchRecordView.h"
#import "YPMICMatchChosenView.h"
#import "YPJCCardView.h"

#import "YPMICUserInfo.h"
#import "YPMICPlayInfoModel.h"

#import "YPFileCore.h"
#import "HJFileCoreClient.h"
#import "YPImRoomCoreV2.h"
#import "YPMediaCore.h"
#import "HJMediaCoreClient.h"
#import "YPPraiseCore.h"
#import "HJPraiseCoreClient.h"
#import "YPMICCore.h"
#import "HJMICCoreClient.h"
#import "UIView+Toast.h"
#import "YPFileCore.h"
#import "YPNavgationTopView.h"

#import "YPHttpRequestHelper+MICCore.h"

#import "YPHttpRequestHelper+User.h"
#import "YPYYActionSheetViewController.h"
#import "YPImFriendCore.h"
#import "YPMySpaceVC.h"
@interface NSArray (XCMIC)

- (void)updateWithUid:(UserID)uid attention:(BOOL)isAttention;

@end

@implementation NSArray (XCMIC)

- (void)updateWithUid:(UserID)uid attention:(BOOL)isAttention {
    for (YPMICUserInfo *userInfo in self) {
        if (userInfo.uid == uid) {
            userInfo.isLike = isAttention;
        }
    }
}

@end


@interface YPMICMatchUserVC () <HJPraiseCoreClient, HJMICCoreClient, HJMediaCoreClient, HJFileCoreClient>

@property (nonatomic, strong) UIView *firstBackgroundView;
@property (nonatomic, strong) UIView *secondBckgroundView;
@property (nonatomic, strong) YPMICMatchChosenView *dislikeButton;
@property (nonatomic, strong) YPMICMatchChosenView *likeButton;

@property (nonatomic, strong) YPMICPlayInfoModel *playInfo;


@property (nonatomic,strong) YPJCCardView *cardView;

//累计remove掉的个数
@property (nonatomic,assign) int cutCount;


@property (nonatomic, assign) BOOL didDrag;

@property (nonatomic, strong) YPMICMatchUserView *currentView;




@end

@implementation YPMICMatchUserVC

//- (BOOL)preferredNavigationBarHidden {
//    return YES;
//}
- (UIStatusBarStyle)preferredStatusBarStyle {
  return UIStatusBarStyleDefault;
}
- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
//    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDarkContent;
    YPBlackStatusBar
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self stopPlay];
}

#pragma mark - Life cycle
- (void)dealloc {
    [self removeCores];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self commonInit];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor colorWithHexString:@"#FAFAFA"];
   
    [self setupNaivigation];
    [self addControls];
    [self layoutControls];
    [self addCores];
    [self updateControls];
    [self preloadUsers];
   
    
    self.cardView.canPan = ^{
        [MBProgressHUD showError:@"没有更多咯~"];
    };
    __weak typeof(self)weakSelf = self;
    self.cardView.cardItemGetBlock = ^__kindof UIView * _Nullable(YPJCCardView * _Nonnull cardView, NSInteger idx) {
        
        if (idx>=(self.users.count+self.cutCount)) {
            return [UIView new];
        }
        
        YPMICUserInfo *model = weakSelf.users[idx-self.cutCount];
        
        YPMICMatchUserView *view = [[YPMICMatchUserView alloc] initWithFrame:CGRectZero];
        [view confifureWithUserInfo:model];
        [weakSelf configureUserView:view atIndex:idx-self.cutCount];
        
        
        return view;
    };
    
    self.cardView.cardItemDidApearBlock = ^(YPJCCardView * _Nonnull cardView, __kindof UIView * _Nonnull itemView, NSInteger idx) {
        weakSelf.currentView = itemView;
        
        if (self.users.count>0) {
            YPMICUserInfo *userInfo = weakSelf.users[0];
            [weakSelf resetPlayInfo];
            weakSelf.playInfo.duration = userInfo.voiceDura;
            weakSelf.playInfo.voicePath = userInfo.userVoice;
            
            [itemView confifureWithUserInfo:userInfo];
            [itemView configureWithPlayInfo:weakSelf.playInfo];
        }
        

//        [weakSelf.cardView resumeCardItems];
        if (weakSelf.users.count<=1) {
            weakSelf.cardView.canPanState = NO;
        }
        
    };
    
    self.cardView.cardItemAnimationWillBeginBlock = ^(YPJCCardView * _Nonnull cardView, __kindof UIView * _Nonnull itemView, NSInteger idx, JCCardViewSwipeDirection direction) {
        if (weakSelf.users.count>0) {
            [weakSelf stopPlay];
            weakSelf.cutCount += 1;
            [weakSelf.users removeObjectAtIndex:0];
        }
    };
    
    
}

#pragma mark - <FileCoreClient>
- (void)onDownloadVoiceSuccess:(NSString *)filePath {
    [MBProgressHUD hideHUD];
    self.playInfo.localVoicePath = filePath;
    if (![GetCore(YPMediaCore) isPlaying]) {
        [GetCore(YPMediaCore) play:filePath];
    }
}

- (void)onDownloadVoiceFailth:(NSError *)error {
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"播放失败" duration:1.0 position:CSToastPositionCenter];
}

//getSoundMatchCharmUserWithSuccess

#pragma mark - <MICCoreClient>
- (void)getMICLinkUserSuccess:(YPMICUserInfo *)userInfo {
    [MBProgressHUD hideHUD];
//    if (userInfo) {
//        [self.users addObject:userInfo];
//    }
}

- (void)getMICLinkUserFailthWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

#pragma mark - <MediaCoreClient>
- (void)onPlayAudioBegan:(NSString *)filePath {
    if (self.playInfo.localVoicePath != nil && [self.playInfo.localVoicePath isEqualToString:filePath]) {
        self.playInfo.playState = MICPlayStateBegin;
        
        [self.currentView configureWithPlayInfo:self.playInfo];
        
//        YPMICMatchUserView *userView = [[self.cardView.item objectOrNilAtIndex:0] valueForKey:@"contentView"];
//        [userView configureWithPlayInfo:self.playInfo];
    }
}

- (void)onPlayAudioComplete:(NSString *)filePath {
    self.playInfo.playState = MICPlayStateUndefine;
//    YPMICMatchUserView *userView = [[self.swipeView.visibleCardViews objectOrNilAtIndex:0] valueForKey:@"contentView"];
    [self.currentView configureWithPlayInfo:self.playInfo];
}

- (void)onPlayAudio:(NSString *)filePath failure:(NSError *)error {
    self.playInfo.playState = MICPlayStateUndefine;
//    YPMICMatchUserView *userView = [[self.swipeView.visibleCardViews objectOrNilAtIndex:0] valueForKey:@"contentView"];
    [self.currentView configureWithPlayInfo:self.playInfo];
    [self.view makeToast:@"播放失败" duration:1.5 position:CSToastPositionCenter];
}

#pragma mark - <PraiseCoreClient>
- (void)onPraiseSuccess:(UserID)uid {
    [MBProgressHUD hideHUD];
    [MBProgressHUD showSuccess:@"关注成功"];
    YPMICUserInfo *userInfo = [self.users objectOrNilAtIndex:0];
    userInfo.isLike = YES;
    [self.users updateWithUid:userInfo.uid attention:userInfo.isLike];
//    YPMICMatchUserView *userView = [[self.swipeView.visibleCardViews objectOrNilAtIndex:0] valueForKey:@"contentView"];
    [self.currentView configureWithAttenion:userInfo.isLike];
}

- (void)onPraiseFailth:(NSString *)msg {
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"请求失败，请检查网络" duration:3 position:CSToastPositionCenter];
}

- (void)onCancelSuccess:(UserID)uid; {
    [MBProgressHUD hideHUD];
     [MBProgressHUD showSuccess:@"取消关注成功"];
    YPMICUserInfo *userInfo = [self.users objectOrNilAtIndex:0];
    userInfo.isLike = NO;
    [self.users updateWithUid:userInfo.uid attention:userInfo.isLike];
//    YPMICMatchUserView *userView = [[self.swipeView.visibleCardViews objectOrNilAtIndex:0] valueForKey:@"contentView"];
    [self.currentView configureWithAttenion:userInfo.isLike];
}

- (void)onCancelFailth:(NSString *)msg {
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"请求失败，请检查网络" duration:3 position:CSToastPositionCenter];
}

#pragma mark - Event
- (void)onRightButtonItemClicked {
    if (self.didDrag) return;
    
    [self stopPlay];
    [self skipToRecord];
}

- (void)skipToRecord {
    YPMICRecordVC *MICRecordViewController = [YPMICRecordVC new];
    [self.navigationController pushViewController:MICRecordViewController animated:YES];
}

- (void)skipToRoom:(YPMICUserInfo *)userInfo {
    [MBProgressHUD showMessage:@"请稍后"];
    [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:userInfo.uid succ:^(YPChatRoomInfo *roomInfo) {
        [MBProgressHUD hideHUD];
        if (roomInfo != nil) {
            GetCore(YPImRoomCoreV2).preferredEnterType = ImRoomCoreEnterTypeMIC;
            [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
        } else {
            [MBProgressHUD showError:@"网络异常"];
        }
    } fail:^(NSString *errorMsg) {
        [MBProgressHUD hideHUD];
    }];
}

- (void)stopPlay {
    if (self.playInfo.voicePath.length) {
        [GetCore(YPFileCore) cancelVoiceTask:self.playInfo.voicePath];
    }
    
    [GetCore(YPMediaCore) stopPlay];
}

#pragma mark - Private methods
- (void)commonInit {
    self.didDrag = NO;
}

- (void)setupNaivigation {
    self.title = @"匹配";
    UIBarButtonItem *rightButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"录语音" style:UIBarButtonItemStylePlain target:self action:@selector(onRightButtonItemClicked)];
    rightButtonItem.tintColor = [UIColor colorWithHexString:@"#1a1a1a"];
    self.navigationItem.rightBarButtonItem = rightButtonItem;
}

- (void)configureUserView:(YPMICMatchUserView *)userView atIndex:(NSUInteger)index {
    YPMICUserInfo *userInfo = [self.users objectOrNilAtIndex:index];
    
//    [self resetPlayInfo];
//    self.playInfo.duration = userInfo.voiceDura;
//    self.playInfo.voicePath = userInfo.userVoice;
    
    [userView confifureWithUserInfo:userInfo];
//    [userView configureWithPlayInfo:self.playInfo];
    
    @weakify(self);
    userView.didTapRecordHandler = ^(YPMICMatchRecordView* matchRecordView){
        @strongify(self);
        switch (self.playInfo.playState) {
            case MICPlayStateBegin:
            case MICPlayStateProgress:
            {
                [self stopPlay];
                matchRecordView.playView.image = [UIImage imageNamed:@"yp_mic_icon_play"];
                matchRecordView.playView.hidden = YES;
            }
                break;
            case MICPlayStateUndefine:
            case MICPlayStatePause:
            case MICPlayStateCancel:
            case MICPlayStateFinished:
            {
                if (self.playInfo.localVoicePath) {
                    if (![GetCore(YPMediaCore) isPlaying]) {
                        [GetCore(YPMediaCore) play:self.playInfo.localVoicePath];
                         matchRecordView.playView.image = [UIImage imageNamed:@"yp_mic_icon_pause"];
                        matchRecordView.playView.hidden = YES;
                    }
                } else {
                    if (self.playInfo.voicePath.length) {
                        if (![GetCore(YPMediaCore) isPlaying]) {
                            [MBProgressHUD showMessage:@"请稍后"];
                            [GetCore(YPFileCore) downloadVoice:self.playInfo.voicePath];
                        }
                    }
                }
            }
                break;
        }
    };
    userView.didTapAttentionHandler = ^{
        @strongify(self);
        YPMICUserInfo *currentUserInfo = [self.users objectOrNilAtIndex:0];
        //主页
        YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
                   vc.userID = currentUserInfo.uid;
                   [self.navigationController pushViewController:vc animated:YES];
        
        
        //关注
//         NSString *mine = [GetCore(YPAuthCoreHelp) getUid];
//        if (currentUserInfo.isLike) {
//            [MBProgressHUD showMessage:@"请稍后"];
//            [GetCore(YPPraiseCore) cancel:mine.userIDValue beCanceledUid:currentUserInfo.uid];
//        } else {
//            [MBProgressHUD showMessage:@"请稍后"];
//            [GetCore(YPPraiseCore) praise:mine.userIDValue bePraisedUid:currentUserInfo.uid];
//        }
    };
    
    
    
    userView.blackListBlock = ^{
        
        [YPHttpRequestHelper userReportSaveWithUid:userInfo.uid reportType:1 type:1 phoneNo:nil success:^{
            
            [MBProgressHUD showSuccess:@"举报成功"];
            
        } failure:^(NSNumber *resCode, NSString *message) {
            
        }];
        
    };
    
    userView.reportBlock = ^{
        [MBProgressHUD showSuccess:@"拉黑成功"];
        [GetCore(YPImFriendCore) addToBlackList:[NSString stringWithFormat:@"%lld",userInfo.uid]];
    };
    
}




- (void)updateControls {
    [self.cardView reloadCardItems];
}


- (void)updatePlayInfo:(YPMICUserInfo *)userInfo {
    [self resetPlayInfo];
    self.playInfo.duration = userInfo.voiceDura;
    self.playInfo.voicePath = userInfo.userVoice;
}

- (void)resetPlayInfo {
    self.playInfo.playState = MICPlayStateUndefine;
    self.playInfo.duration = 0;
    self.playInfo.voicePath = nil;
    self.playInfo.localVoicePath = nil;
}

- (void)preloadUsers {
//    [GetCore(MICCore) getMICLinkUser];
    [self stopPlay];
    
    
    [YPHttpRequestHelper getSoundMatchCharmUserWithSuccess:^(NSArray *list) {
        NSArray *buffer = [NSArray yy_modelArrayWithClass:[YPMICUserInfo class] json:list];
        
        [MBProgressHUD hideHUD];
        [self.users removeAllObjects];
        [self.users addObjectsFromArray:buffer];
//        [self.cardView reloadCardItems];
        [self.cardView fillCardItems];
       
    } failure:^(NSNumber *code, NSString *message) {
        [MBProgressHUD hideHUD];

    }];
    
}

- (void)addCores {
    AddCoreClient(HJFileCoreClient, self);
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJPraiseCoreClient, self);
    AddCoreClient(HJMICCoreClient, self);
}

- (void)removeCores {
    RemoveCoreClientAll(self);
}

#pragma mark - Public methods

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.likeButton];
    [self.view addSubview:self.dislikeButton];
    [self.view addSubview:self.secondBckgroundView];
    [self.view addSubview:self.firstBackgroundView];
    [self.view addSubview:self.cardView];
    
}

- (void)layoutControls {
//    CGFloat width = XC_RATIO_WIDTH(341);
//    CGFloat height = width * 397 / 341.f + 30;
//    CGFloat firstWidth = XC_RATIO_WIDTH(324);
//    CGFloat secondWidth = XC_RATIO_WIDTH(307);
    CGFloat width = XC_RATIO_WIDTH(310);
    CGFloat height = width * 416 / 310.f;
    CGFloat firstWidth = XC_RATIO_WIDTH(300);
    CGFloat secondWidth = XC_RATIO_WIDTH(287);

    
//    667
    
    [self.cardView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(33);
        make.centerX.equalTo(self.view);
        make.width.equalTo(@(width));
        make.height.equalTo(@(height));
    }];
    
    [self.firstBackgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.width.equalTo(@(firstWidth));
        make.height.equalTo(@(height));
        make.bottom.equalTo(self.cardView).offset(10);
    }];
    
    [self.secondBckgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.width.equalTo(@(secondWidth));
        make.height.equalTo(@(height));
        make.bottom.equalTo(self.firstBackgroundView).offset(10);
    }];
    
//    UIView *trickView = nil;
//    if (XC_SCREE_H < 667.f) {
//        trickView = [UIView new];
//        [self.view addSubview:trickView];
//        [trickView mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.centerX.equalTo(self.view);
//            make.top.equalTo(self.swipeView.mas_bottom);
//            make.bottom.equalTo(self.view);
//            make.width.equalTo(@(1));
//        }];
//    }
    
    CGSize buttonSize = CGSizeMake(XC_RATIO_WIDTH(80), XC_RATIO_WIDTH(80));
    CGFloat buttonTop = XC_RATIO_WIDTH(31) + XC_RATIO_WIDTH(25);
    [self.dislikeButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(buttonSize));
//        if (trickView) {
//            make.centerY.equalTo(trickView).offset(10);
//        } else {
            make.top.equalTo(self.cardView.mas_bottom).offset(buttonTop);
//        }
        make.left.equalTo(self.view).offset(70);
    }];
    
    [self.likeButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(buttonSize));
//        if (trickView) {
//            make.centerY.equalTo(trickView).offset(10);
//        } else {
            make.top.equalTo(self.cardView.mas_bottom).offset(buttonTop);
//        }
        make.right.equalTo(self.view).offset(-70);
    }];
    
  
}


#pragma mark - setters/getters
- (YPMICPlayInfoModel *)playInfo {
    if (!_playInfo) {
        _playInfo = [YPMICPlayInfoModel new];
    }
    return _playInfo;
}

- (NSMutableArray *)users {
    if (!_users) {
        _users = @[].mutableCopy;
    }
    return _users;
}


- (UIView *)firstBackgroundView {
    if (!_firstBackgroundView) {
        _firstBackgroundView = [UIView new];
        _firstBackgroundView.backgroundColor = [UIColor colorWithHexString:@"#F8F8F8"];
        _firstBackgroundView.layer.borderColor = [UIColor colorWithHexString:@"#E5E5E5"].CGColor;
        _firstBackgroundView.layer.borderWidth = .5f;
        _firstBackgroundView.layer.cornerRadius = 8.f;
        _firstBackgroundView.layer.masksToBounds = YES;
    }
    return _firstBackgroundView;
}

- (UIView *)secondBckgroundView {
    if (!_secondBckgroundView) {
        _secondBckgroundView = [UIView new];
        _secondBckgroundView.backgroundColor = [UIColor colorWithHexString:@"#F3F3F3"];
        _secondBckgroundView.layer.borderColor = [UIColor colorWithHexString:@"#E5E5E5"].CGColor;
        _secondBckgroundView.layer.borderWidth = .5f;
        _secondBckgroundView.layer.cornerRadius = 8.f;
        _secondBckgroundView.layer.masksToBounds = YES;
    }
    return _secondBckgroundView;
}

//- (XCSwipeView *)swipeView {
//    if (!_swipeView) {
//        _swipeView = [[XCSwipeView alloc] initWithFrame:CGRectZero];
//        _swipeView.dataSource = self;
//        _swipeView.delegate = self;
//        _swipeView.preferredBackgroundCardsTopMargin = 0.f;
//        _swipeView.preferredAppearAnimationDuration = 0.25f;
//    }
//    return _swipeView;
//}

- (YPJCCardView *)cardView
{
    if (!_cardView) {
        _cardView = [[YPJCCardView alloc] initWithFrame:CGRectZero];
        _cardView.canPanState = YES;
        _cardView.maxCardItemCount = 1;
        _cardView.translationYOffset = 1;
        [_cardView registerCardItemViewClass:[YPMICMatchUserView class] forIdentifier:@"YPMICMatchUserView"];
    }
    return _cardView;
}

- (YPMICMatchChosenView *)dislikeButton {
    if (!_dislikeButton) {
        _dislikeButton = [[YPMICMatchChosenView alloc] initWithChosenType:XCMICMatchChosenTypeDislike];
        @weakify(self);
        _dislikeButton.didTapHandler = ^{
            @strongify(self);
            if (self.didDrag) return;
            
            if (self.users.count >= 2) {
                [self stopPlay];
                [self.cardView swipeCardItemToDirection:JCCardViewSwipeDirectionLeft];
            } else {
                [MBProgressHUD showError:@"没有更多了~"];
            }
        };
    }
    return _dislikeButton;
}

- (YPMICMatchChosenView *)likeButton {
    if (!_likeButton) {
        _likeButton = [[YPMICMatchChosenView alloc] initWithChosenType:XCMICMatchChosenTypeLike];;
        @weakify(self);
        _likeButton.didTapHandler = ^{
            @strongify(self);
            if (self.didDrag) return;
            if (self.users.count==0) return;
            
            [self stopPlay];
             [self attentionAction];
        };
    }
    return _likeButton;
}
-(void)attentionAction{
            //关注
     YPMICUserInfo *currentUserInfo = [self.users objectOrNilAtIndex:0];
             NSString *mine = [GetCore(YPAuthCoreHelp) getUid];
            if (currentUserInfo.isLike) {
//                [MBProgressHUD showMessage:@"请稍后"];
                [GetCore(YPPraiseCore) cancel:mine.userIDValue beCanceledUid:currentUserInfo.uid];
            } else {
//                [MBProgressHUD showMessage:@"请稍后"];
                [GetCore(YPPraiseCore) praise:mine.userIDValue bePraisedUid:currentUserInfo.uid];
            }
}
@end
