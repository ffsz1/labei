//
//  YPPersonalVoiceEditVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPersonalVoiceEditVC.h"
#import "UIViewController+Cloudox.h"
#import "UINavigationController+Cloudox.h"
#import "YPMediaCore.h"
#import "HJMediaCoreClient.h"
#import "HJFileCoreClient.h"
#import "YPFileCore.h"
#import "YPUserCoreHelp.h"
#import "YPAuthCoreHelp.h"
#import "UIVIew+Toast.h"

@interface YPPersonalVoiceEditVC ()<HJMediaCoreClient, HJFileCoreClient>
@property (weak, nonatomic) IBOutlet UIButton *recordButton;
@property (weak, nonatomic) IBOutlet UIButton *rerecordButton;
@property (weak, nonatomic) IBOutlet UILabel *rerecordLabel;
@property (weak, nonatomic) IBOutlet UIButton *tryListenButton;
@property (weak, nonatomic) IBOutlet UILabel *tryListenLabel;
@property (weak, nonatomic) IBOutlet UIButton *saveButton;
@property (weak, nonatomic) IBOutlet UILabel *pressSpeakLabel;
@property (weak, nonatomic) IBOutlet UILabel *recordProgressLabel;

@property (nonatomic, strong) NSString *filePath;
@property (nonatomic, assign) NSInteger time;

- (IBAction)onSaveBtnClicked:(id)sender;
- (IBAction)onRerecordBtnClicked:(id)sender;
- (IBAction)onTryListenBtnClicked:(id)sender;


@end

@implementation YPPersonalVoiceEditVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJFileCoreClient, self);
    [self initView];
    [self.recordButton addTarget:self action:@selector(onRecordBtnDown:) forControlEvents:UIControlEventTouchDown];
    [self.recordButton addTarget:self action:@selector(onRecordBtnUp:) forControlEvents:UIControlEventTouchUpInside];
    [self.recordButton addTarget:self action:@selector(onRecordBtnUp:) forControlEvents:UIControlEventTouchUpOutside];
}

- (void)dealloc
{
    RemoveCoreClient(HJMediaCoreClient, self);
    RemoveCoreClient(HJFileCoreClient, self);
}

- (void)onRecordBtnDown:(id)target
{
    if (![GetCore(YPMediaCore) isRecording]) {
        [GetCore(YPMediaCore) record];
        self.time = 0;
    }
}

- (void)onRecordBtnUp:(id)target
{
    if ([GetCore(YPMediaCore) isRecording]) {
        [GetCore(YPMediaCore) stopRecord];
        if (self.time <= 1) {
            [GetCore(YPMediaCore) cancelRecord];
        } else {
            [GetCore(YPMediaCore) stopRecord];
        }
    }
}

- (IBAction)onSaveBtnClicked:(id)sender {
    if (self.filePath.length > 0) {
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [GetCore(YPFileCore) uploadVoice:self.filePath];
    }
}

- (IBAction)onRerecordBtnClicked:(id)sender {
    [self showRecordView];
    self.recordProgressLabel.hidden = YES;
    self.filePath = nil;
    self.time = 0;
}

- (IBAction)onTryListenBtnClicked:(id)sender {
    if (self.filePath.length > 0) {
        [GetCore(YPMediaCore) play:self.filePath];
    }
}

- (void) initView
{
    UIImageView *bg = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
//    bg.image = [UIImage imageNamed:@"yp_voice_record_bg"];
    [self.view insertSubview:bg atIndex:0];
    [self showRecordView];
    self.recordProgressLabel.hidden = YES;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navBarBgAlpha = @"0.0";
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.navBarBgAlpha = @"1.0";
}

- (void)showRecordView
{
    self.recordButton.hidden = NO;
    self.pressSpeakLabel.hidden = NO;
    self.rerecordButton.hidden = YES;
    self.rerecordLabel.hidden = YES;
    self.tryListenButton.hidden = YES;
    self.tryListenLabel.hidden = YES;
    self.saveButton.hidden = YES;
}

- (void)showSaveView
{
    self.recordButton.hidden = YES;
    self.pressSpeakLabel.hidden = YES;
    self.rerecordButton.hidden = NO;
    self.rerecordLabel.hidden = NO;
    self.tryListenButton.hidden = NO;
    self.tryListenLabel.hidden = NO;
    self.saveButton.hidden = NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - MediaCoreClient
- (void)onRecordAudioBegan:(NSString *)filePath
{
    if (filePath.length > 0) {
        self.filePath = filePath;
        self.recordProgressLabel.hidden = NO;
        self.recordProgressLabel.text = @"00:00";
    }
}

- (void)onRecordAudioCancel
{
    self.recordProgressLabel.hidden = YES;
    self.recordProgressLabel.text = @"00:00";
}

- (void)onRecordAudioComplete:(NSString *)filePath
{
    if ([filePath isEqualToString:self.filePath]) {
        [self showSaveView];
        self.recordProgressLabel.hidden = YES;
        self.recordProgressLabel.text = @"00:00";
    }
}

- (void)onRecordAudioProgress:(NSTimeInterval)currentTime
{
    NSInteger t = (NSInteger) currentTime;
    NSString *time = @"";
    if (currentTime == 10) {
        time = @"00:10";
    } else {
        time = [NSString stringWithFormat:@"00:%02ld", t];
    }
    self.time = t;
    self.recordProgressLabel.text = time;
}

#pragma mark -FileCoreClient
- (void)onUploadVoiceSuccess:(NSString *)url
{
    if (url.length > 0) {
        UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
        NSMutableDictionary *dic = [NSMutableDictionary dictionary];
        [dic setObject:url forKey:@"userVoice"];
        [dic setObject:@(self.time) forKey:@"voiceDura"];
        @weakify(self)
        [[GetCore(YPUserCoreHelp) saveUserInfoWithUserID:uid userInfos:dic.copy] subscribeNext:^(id x) {
            @strongify(self);
            [MBProgressHUD hideHUD];
            [self.navigationController popViewControllerAnimated:YES];
        } error:^(NSError *error) {
            @strongify(self);
            [MBProgressHUD hideHUD];
            [self.navigationController popViewControllerAnimated:YES];
        }];
    } else {
        [MBProgressHUD hideHUD];
        [self.view makeToast:@"录音上传失败，请重试" duration:3 position:CSToastPositionCenter];
    }
}

- (void)onUploadVoiceFailth:(NSError *)error
{
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"录音上传失败，请重试" duration:3 position:CSToastPositionCenter];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
@end
