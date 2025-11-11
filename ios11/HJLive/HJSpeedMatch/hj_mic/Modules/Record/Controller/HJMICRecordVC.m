//
//  HJMICRecordVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMICRecordVC.h"

#import "HJMICRecordView.h"

#import "HJMICRecordInfoModel.h"

#import "HJFileCore.h"
#import "HJFileCoreClient.h"
#import "HJMediaCore.h"
#import "HJMediaCoreClient.h"
#import "HJMICRecordTransition.h"
#import "UIVIew+Toast.h"
#import "HJUserCoreHelp.h"
#import "HJUserCoreClient.h"
#import "HJMICRecordVC+Private.h"
#import "NavgationTopView.h"

@interface HJMICRecordVC () <HJFileCoreClient, HJMediaCoreClient, HJUserCoreClient, UIViewControllerTransitioningDelegate>

@property (nonatomic, strong) HJMICRecordView *recordView;

@property (nonatomic, strong) HJMICRecordInfoModel *recordInfo;

@property (nonatomic, strong) NavgationTopView *navgationTopView;
@property (nonatomic, strong) UIImageView *bgImg;
@property (nonatomic, strong) NSArray* textArray;
@property (nonatomic, assign) NSInteger textIndex;

@end

@implementation HJMICRecordVC

- (BOOL)preferredNavigationBarHidden {
    return YES;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    if ([GetCore(HJMediaCore) isRecording]) {
        [GetCore(HJMediaCore) stopRecord];
    }
}

- (UIImageView *)bgImg {
    if (!_bgImg) {
        _bgImg = [UIImageView new];
        _bgImg.image = [UIImage imageNamed:@"hj_match_recordBg"];
    }
    return _bgImg;
}

- (NavgationTopView *)navgationTopView {
    if (!_navgationTopView) {
        _navgationTopView = [[NSBundle mainBundle] loadNibNamed:@"NavgationTopView" owner:nil options:nil][0];
        _navgationTopView.title.text = @"录制声音";
        @weakify(self);
        [_navgationTopView setGoBackBlock:^{
            @strongify(self);
            [self.navigationController popViewControllerAnimated:YES];
        }];
    }
    return _navgationTopView;
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
    
    [self addControls];
    [self layoutControls];
    [self addCores];
//    [self setupDatas];
    [self updateControls];
    [self getVoiceText];
    
    
    self.textArray = @[@"愿你三冬暖，愿你春不寒\n\n愿你天黑有灯,下雨有伞\n\n愿你一路上，有良人相伴",
                         @"一只大恐龙嗷呜嗷呜就出现了\n\n然后把你muamuamua吧唧吧唧吧唧\n\n一口一口吃掉了\n\n          然后大恐龙嗷呜嗷呜嗷呜飞走了",
                         @"我对你付出的青春这么多年\n\n换来了一句谢谢你的成全\n\n成全了你的潇洒与冒险\n\n成全了我的碧海蓝天",
                         @"做一个很酷的人\n\n闹钟一响就起\n\n走了就不回头\n\n连告别都是两手插兜",
                         @"浮世三千，吾爱有三\n\n日月与卿\n\n日为朝,月为暮\n\n卿为朝朝暮暮"];
      self.recordView.text = self.textArray[0];
      self.textIndex = 0;

}

#pragma mark - <FileCoreClient>
- (void)onUploadVoiceSuccess:(NSString *)url {
    if (url.length > 0) {
        UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
        NSInteger duration = self.recordInfo.time;
        NSMutableDictionary *dic = [NSMutableDictionary dictionary];
        [dic setObject:url forKey:@"userVoice"];
        [dic setObject:@(duration) forKey:@"voiceDura"];
        @weakify(self)
        [[GetCore(HJUserCoreHelp) saveUserInfoWithUserID:uid userInfos:dic.copy] subscribeNext:^(id x) {
            @strongify(self);
            [MBProgressHUD showSuccess:@"上传成功"];
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [self.navigationController popViewControllerAnimated:YES];
            });
            
        } error:^(NSError *error) {
            @strongify(self);
            
            [MBProgressHUD showError:@"录音上传失败，请重试"];

        }];
    } else {
        [MBProgressHUD showError:@"录音上传失败，请重试"];

//        [MBProgressHUD hideHUD];
//        [self.view makeToast:@"录音上传失败，请重试" duration:1 position:CSToastPositionCenter];
    }
}

- (void)onUploadVoiceFailth:(NSError *)error {
//    [MBProgressHUD hideHUD];
    [MBProgressHUD showError:@"录音上传失败，请重试"];

//    [self.view makeToast:@"录音上传失败，请重试" duration:1 position:CSToastPositionCenter];
}

- (void)onDownloadVoiceSuccess:(NSString *)filePath {
    [MBProgressHUD hideHUD];
    self.recordInfo.localPath = filePath;
    [self updateControls];
    if (![GetCore(HJMediaCore) isPlaying]) {
        [GetCore(HJMediaCore) play:filePath];
    }
}

- (void)onDownloadVoiceFailth:(NSError *)error {
    [MBProgressHUD showError:@"下载失败，请重试"];

//    [MBProgressHUD hideHUD];
//    [self.view makeToast:@"播放失败" duration:1.0 position:CSToastPositionCenter];
}

#pragma mark - <MediaCoreClient>
- (void)onRecordAudioBegan:(NSString *)filePath {
    if (filePath.length > 0) {
        self.recordInfo.filePath = nil;
        self.recordInfo.localPath = filePath;
        self.recordInfo.time = 0;
        self.recordInfo.state = MICRecordStateBegin;
        [self updateControls];
    }
}

- (void)onRecordAudioProgress:(NSTimeInterval)currentTime {
    self.recordInfo.time = currentTime;
    self.recordInfo.state = MICRecordStateProgress;
    [self updateControls];
}

- (void)onRecordAudioCancel {
    self.recordInfo.time = 0;
    self.recordInfo.state = MICRecordStateCancel;
    self.recordInfo.filePath = nil;
    self.recordInfo.localPath = nil;
    [self updateControls];
}

- (void)onRecordAudioComplete:(NSString *)filePath {
    if ([filePath isEqualToString:self.recordInfo.localPath]) {
        if (self.recordInfo.time <= 1) {
            self.recordInfo.time = 0;
            self.recordInfo.filePath = nil;
            self.recordInfo.localPath = nil;
            self.recordInfo.state = MICRecordStateCancel;
        } else {
            self.recordInfo.state = MICRecordStateFinished;
            self.recordView.saveState = YES;
        }
        [self updateControls];
    }
}

- (void)onPlayAudioBegan:(NSString *)filePath {
    self.recordInfo.state = MICRecordStatePlaying;
    [self updateControls];
}

- (void)onPlayAudioComplete:(NSString *)filePath {
    self.recordInfo.state = MICRecordStateFinished;
    [self updateControls];
}

- (void)onPlayAudio:(NSString *)filePath failure:(NSError *)error {
    self.recordInfo.state = MICRecordStateFinished;
    [self updateControls];
    [self.view makeToast:@"播放失败" duration:1.0 position:CSToastPositionCenter];
}

#pragma mark - <UserCoreClient>
- (void)onGetVoiceTextSuccess:(NSString *)text {
    self.recordView.text = text;
}

- (void)onGetVoiceTextFailth:(NSString *)message {
    
}

#pragma mark - Event
- (void)dismiss {
    [self stopPlay];
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)backgroundViewDidTapped:(UIImageView *)backgroundView {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)showForDeleteVoice {
//    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"是否删除录音？" message:nil preferredStyle:UIAlertControllerStyleAlert];
//    
//    @weakify(self);
//    UIAlertAction *doneAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
//        @strongify(self);
        [self stopPlay];
        [self removeRecordInServer];
        [self removeRecord];
        [self updateControls];
        [self.recordView reset];
//    }];
//
//    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
//
//    [alertController addAction:doneAction];
//    [alertController addAction:cancelAction];
//
//    [self presentViewController:alertController animated:YES completion:nil];
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)setupDatas {
    UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
    UserInfo *userInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:uid];
    if (userInfo.userVoice.length) {
        self.recordInfo.state = MICRecordStateFinished;
        self.recordInfo.time = userInfo.voiceDura;
        self.recordInfo.filePath = userInfo.userVoice;
        self.recordInfo.localPath = nil;
        self.recordView.saveState = YES;
    } else {
        self.recordInfo.state = MICRecordStateUndefine;
        self.recordInfo.time = 0;
        self.recordInfo.filePath = nil;
        self.recordInfo.localPath = nil;
        self.recordView.saveState = NO;
    }
}

- (void)commonInit {
    self.transitioningDelegate = self;
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0) {
        self.modalPresentationStyle = UIModalPresentationOverFullScreen;
    } else {
        self.modalPresentationStyle = UIModalPresentationCustom;
    }
}

- (void)stopPlay {
    if (self.recordInfo.filePath.length) {
        [GetCore(HJFileCore) cancelVoiceTask:self.recordInfo.filePath];
    }
    
    if([GetCore(HJMediaCore) isPlaying]) {
        [GetCore(HJMediaCore) stopPlay];
    }
}

- (void)removeRecordInServer {
    if (self.recordInfo.filePath.length) {
        UserID uid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
        NSMutableDictionary *dic = [NSMutableDictionary dictionary];
        [dic setObject:@"" forKey:@"userVoice"];
        [dic setObject:@(0) forKey:@"voiceDura"];
        [[GetCore(HJUserCoreHelp) saveUserInfoWithUserID:uid userInfos:dic.copy] subscribeNext:^(id x) {
        } error:^(NSError *error) {
        }];
    }
}

- (void)removeRecord {
    self.recordInfo.localPath = nil;
    self.recordInfo.filePath = nil;
    self.recordInfo.time = 0;
    self.recordInfo.state = MICRecordStateUndefine;
}

- (void)updateControls {
    self.recordView.recordInfo = self.recordInfo;
}

- (void)getVoiceText {
//    UserID uid = [[GetCore(AuthCore) getUid] userIDValue];
//    [GetCore(UserCore) getVoiceText:uid];
}

- (void)addCores {
    AddCoreClient(HJFileCoreClient, self);
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJUserCoreClient, self);
}

- (void)removeCores {
    RemoveCoreClient(HJMediaCoreClient, self);
    RemoveCoreClient(HJFileCoreClient, self);
    RemoveCoreClient(HJUserCoreClient, self);
}

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.bgImg];
    [self.view addSubview:self.backgroundView];
    [self.view addSubview:self.recordView];
    [self.view addSubview:self.navgationTopView];
}

- (void)layoutControls {
    [self.bgImg mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.view);
        make.top.equalTo(self.view);
        make.height.equalTo(@(kScreenWidth));
    }];
    
    [self.navgationTopView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.view);
        if (@available(iOS 11.0, *)) {
            make.top.equalTo(self.view.mas_safeAreaLayoutGuideTop);
        } else {
            make.top.equalTo(self.view);
        }
        
        if (@available(iOS 9.0, *)) {
            make.height.equalTo(@(64));
        }else{
            make.height.equalTo(@(44));
        }
    }];
    
    [self.recordView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.bottom.right.equalTo(self.view);
        make.height.equalTo(self.view);
    }];
    
    [self.backgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.equalTo(self.view);
    }];
}

#pragma mark - setters/getters
- (UIImageView *)backgroundView {
    if (!_backgroundView) {
        _backgroundView = [UIImageView new];
        [_backgroundView setUserInteractionEnabled:YES];
        _backgroundView.exclusiveTouch = YES;
        [_backgroundView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(backgroundViewDidTapped:)]];
    }
    return _backgroundView;
}


- (HJMICRecordInfoModel *)recordInfo {
    if (!_recordInfo) {
        _recordInfo = [HJMICRecordInfoModel new];
        _recordInfo.state = MICRecordStateUndefine;
        _recordInfo.time = 0;
        _recordInfo.filePath = nil;
        _recordInfo.localPath = nil;
    }
    return _recordInfo;
}

- (HJMICRecordView *)recordView {
    if (!_recordView) {
        @weakify(self);
        _recordView = [HJMICRecordView new];
        _recordView.didTapChangeHandler = ^{
            @strongify(self);
//            [self getVoiceText];
            self.textIndex += 1;
                      if (self.textIndex>self.textArray.count-1) {
                          self.textIndex = 0;
                      }
                      self.recordView.text = self.textArray[self.textIndex];
            
            
        };
        
        _recordView.didTapDoneHandler = ^{
            @strongify(self);
            if (self.recordInfo.localPath.length && self.recordInfo.state == MICRecordStateFinished) {
                [self stopPlay];
                [MBProgressHUD showMessage:@"上传中..."];
                [GetCore(HJFileCore) uploadVoice:self.recordInfo.localPath];
            } else {
                [self.navigationController popViewControllerAnimated:YES];
            }
        };
        
        _recordView.didTapDeletedHandler = ^{
            @strongify(self);
            [self showForDeleteVoice];
        };
        
        _recordView.didTapPlayHandler = ^{
            @strongify(self);
            if (self.recordInfo.localPath) {
                if (self.recordInfo.state == MICRecordStatePlaying) {
                    [self stopPlay];
                } else {
                    if (![GetCore(HJMediaCore) isPlaying]) {
                        [GetCore(HJMediaCore) play:self.recordInfo.localPath];
                    }
                }
            } else {
                if (self.recordInfo.filePath.length) {
                    if (![GetCore(HJMediaCore) isPlaying]) {
                        [MBProgressHUD showMessage:@"缓存中..."];
                        [GetCore(HJFileCore) downloadVoice:self.recordInfo.filePath];
                    }
                }
            }
        };
        
        __weak typeof(self)weakSelf = self;
        
        _recordView.saveBlock = ^{
            
            [weakSelf stopPlay];
            [MBProgressHUD showMessage:@"上传中..."];
            [GetCore(HJFileCore) uploadVoice:weakSelf.recordInfo.localPath];
            
        };
        
        _recordView.didTouchRecordHandler = ^(BOOL isFinish) {
            
            if (isFinish) {
                
                if ([GetCore(HJMediaCore) isRecording]) {
                    [GetCore(HJMediaCore) stopRecord];
                    if (weakSelf.recordInfo.time <= 1) {
                        [GetCore(HJMediaCore) cancelRecord];
                    } else {
                        [GetCore(HJMediaCore) stopRecord];
                        weakSelf.recordView.saveState = YES;
                    }
                }else{
                    [weakSelf.recordView reset];

                }
                
            }else{
                
                [weakSelf stopPlay];
                if (![GetCore(HJMediaCore) isRecording]) {
                    [weakSelf removeRecord];
                    [GetCore(HJMediaCore) record];
                    weakSelf.recordInfo.time = 0;
                }else{
                    [weakSelf.recordView reset];
                }
                
            }
            
        };
        
//        _recordView.didTouchRecordHandler = ^(UIControlEvents event) {
//            @strongify(self);
//            switch (event) {
//                case UIControlEventTouchDown:
//                {
//                    if (self.recordView.saveState) {
//                        [self stopPlay];
//                        [MBProgressHUD showMessage:@"上传中..."];
//                        [GetCore(HJFileCore) uploadVoice:self.recordInfo.localPath];
//                    } else {
//                        [self stopPlay];
//                        if (![GetCore(HJMediaCore) isRecording]) {
//                            [self removeRecord];
//                            [GetCore(HJMediaCore) record];
//                            self.recordInfo.time = 0;
//                            self.recordView.saveState = false;
//                        }
//                    }
//                }
//                    break;
//                case UIControlEventTouchUpInside:
//                case UIControlEventTouchUpOutside:
//                {
//                    if ([GetCore(HJMediaCore) isRecording]) {
//                        [GetCore(HJMediaCore) stopRecord];
//                        if (self.recordInfo.time <= 1) {
//                            [GetCore(HJMediaCore) cancelRecord];
//                        } else {
//                            [GetCore(HJMediaCore) stopRecord];
//                            self.recordView.saveState = YES;
//                        }
//                    }
//                }
//                    break;
//
//                default:
//                    break;
//            }
//        };
    }
    return _recordView;
}

@end
