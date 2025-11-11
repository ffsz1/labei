//
//  YPOpenRoomViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPOpenRoomViewController.h"

#import "YPRoomViewControllerFactory.h"
#import "UIViewController+Cloudox.h"
#import "UINavigationController+Cloudox.h"
#import "YPStateButton.h"
#import "YPYYDefaultTheme.h"

#import "YPAuthCoreHelp.h"
#import "YPVersionCoreHelp.h"
#import "YPPurseCore.h"
#import "HJPurseCoreClient.h"
#import "YPFileCore.h"
#import "HJFileCoreClient.h"
#import "HJRoomCoreClient.h"

#import "UIView+Toast.h"
#import "SZTextView.h"
#import "YPYYActionSheetViewController.h"
#import "YPImagePickerUtils.h"

#import "YPRoomViewControllerCenter.h"
#import "UIImageView+YYWebImage.h"

#import "YPUserProtocolView.h"


@interface YPOpenRoomViewController ()<HJRoomCoreClient,UINavigationControllerDelegate, UIImagePickerControllerDelegate, HJFileCoreClient,UserProtocolViewDelegate,UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *coverBg;
@property (weak, nonatomic) IBOutlet UITextField *titleField;
@property (weak, nonatomic) IBOutlet SZTextView *noticeTextView;
@property (weak, nonatomic) IBOutlet YPStateButton *openRoomBtn;
@property (nonatomic, assign) BOOL isSetCover;
@property (nonatomic, strong) NSString* coverUrl;
@property (nonatomic, strong) YPChatRoomInfo *lastRoomInfo;
@property (nonatomic, strong) YPUserProtocolView *userProtocolView;
- (IBAction)onOpenRoomBtnClicked:(id)sender;

@end

@implementation YPOpenRoomViewController

#pragma mark - life cycle
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

- (void)viewDidLoad {
    [super viewDidLoad];
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJFileCoreClient, self);
    UIColor *color = [UIColor whiteColor];
    NSDictionary *dict = [NSDictionary dictionaryWithObject:color forKey:NSForegroundColorAttributeName];
    self.navigationController.navigationBar.titleTextAttributes = dict;
    self.title = NSLocalizedString(XCRoomGameRoome, nil);
    

    [self initView];
}

- (void)dealloc
{
    RemoveCoreClient(HJRoomCoreClient, self);
    RemoveCoreClient(HJFileCoreClient, self);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)initView
{
    UIImageView *bg = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    bg.image = [UIImage imageNamed:@"open_room_bg"];
    
    self.coverBg.userInteractionEnabled = YES;
    UITapGestureRecognizer *tapGestureRecognizer1 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onImageClicked:)];
    [self.coverBg addGestureRecognizer:tapGestureRecognizer1];
    
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(keyboardHide:)];
    //设置成NO表示当前控件响应后会传播到其他控件上，默认为YES。
    tapGestureRecognizer.cancelsTouchesInView = NO;
    //将触摸事件添加到当前view
    [self.view addGestureRecognizer:tapGestureRecognizer];
    
    [self.view insertSubview:bg atIndex:0];
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithTitle:@"" style:(UIBarButtonItemStyleDone) target:self action:nil];
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"close_room_logo"] style:UIBarButtonItemStyleDone target:self action:@selector(onRightBtnClicked)];
    self.navigationItem.rightBarButtonItem = item;
    
    self.titleField.delegate = self;
    [self.titleField setValue:[[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#cccccc" alpha:1.0] forKeyPath:@"_placeholderLabel.textColor"];
    
    [self.noticeTextView setPlaceholder:NSLocalizedString(XCRoomFillInRoomAd, nil)];
    
    self.userProtocolView = [YPUserProtocolView loadFromNIB];
    self.userProtocolView.delegate = self;
    self.userProtocolView.nav = self.navigationController;
    self.userProtocolView.center = CGPointMake(self.view.frame.size.width/2, self.view.frame.size.height - 40);
    [self.view addSubview:self.userProtocolView];
    
    self.userProtocolView.hidden = NO;


    
    [[GetCore(YPRoomCoreV2Help) requestRoomInfo:[GetCore(YPAuthCoreHelp) getUid].userIDValue] subscribeNext:^(id x) {
    }];
}

#pragma mark - UITextFielDelegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    
    NSInteger existedLength = textField.text.length;
    NSInteger selectedLength = range.length;
    NSInteger replaceLength = string.length;
    
    if (existedLength - selectedLength + replaceLength > 20) {
        return NO;
    }
   
    return YES;
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    __weak __typeof__(self) wself = self;
    [picker dismissViewControllerAnimated:YES completion:^{
        UIImage *selectedPhoto = [info objectForKey:UIImagePickerControllerOriginalImage];
        if (selectedPhoto) {
            __strong __typeof (wself) sSelf = wself;
            if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
                UIImageWriteToSavedPhotosAlbum(selectedPhoto, nil, nil, nil);
            }
            //            [sSelf.viewModel uploadAvatar:selectedPhoto];
            [sSelf.coverBg setImage:selectedPhoto];
            self.isSetCover = YES;
        }
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:^{
        
    }];
}


#pragma -mark UserProtocolViewDelegate
- (void)onSelectBtnClicked:(BOOL)select
{
    [self updateOpenRoomBtnState];
}

#pragma mark - FileCoreClient
- (void)onUploadCoverSuccess:(NSString *)url
{
    [GetCore(YPRoomCoreV2Help) openRoom:[GetCore(YPAuthCoreHelp) getUid].userIDValue type:self.roomtype title:self.titleField.text roomDesc:self.noticeTextView.text backPic:url rewardId:nil];
}

- (void)onUploadCoverFailth:(NSError *)error
{
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"操作失败，请检查网络" duration:3 position:CSToastPositionCenter];
}

#pragma mark - FileCoreClient 7N
- (void)didUploadChatRoomImageSuccessUseQiNiu:(NSString *)key{
    NSLog(@"didUploadChatRoomImageSuccessUseQiNiu");
    NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,key];
    [GetCore(YPRoomCoreV2Help) openRoom:[GetCore(YPAuthCoreHelp) getUid].userIDValue type:self.roomtype title:self.titleField.text roomDesc:self.noticeTextView.text backPic:url rewardId:nil];
}
- (void)didUploadChatRoomImageFailUseQiNiu:(NSString *)message{
    NSLog(@"didUploadChatRoomImageFailUseQiNiu:%@",message);
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"操作失败，请检查网络" duration:3 position:CSToastPositionCenter];
}

#pragma mark - RoomCoreClient
- (void)onOpenRoomSuccess:(YPChatRoomInfo *)roomInfo
{
    [MBProgressHUD hideHUD];
    [self.navigationController dismissViewControllerAnimated:YES completion:^{
        [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
    }];
}

- (void)onOpenRoomFailth:(NSNumber *)resCode message:(NSString *)message
{
    if (resCode.integerValue == 1500) {
        [self showIsInLiveAlert];
    } else {
        [self.view makeToast:@"操作失败，请检查网络" duration:3 position:CSToastPositionCenter];
    }
    [MBProgressHUD hideHUD];
}

#pragma mark - Event Response

- (void)onImageClicked:(UITapGestureRecognizer *)rec
{
    YPYYActionSheetViewController *actionSheet = [YPImagePickerUtils showImagePickerSystemLibSheet:^{
        
    } title:@"添加图片" view:self.view tailor:NO delegate:self];
    [actionSheet show];
}

- (IBAction)onOpenRoomBtnClicked:(id)sender {
    
    
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    
    if (self.isSetCover) {
        [GetCore(YPFileCore) qiNiuUploadImage:self.coverBg.image uploadType:UploadImageTypeChatRoot];
    } else {
        NSString *backPic = self.lastRoomInfo.backPic;
        [GetCore(YPRoomCoreV2Help) openRoom:[GetCore(YPAuthCoreHelp) getUid].userIDValue type:self.roomtype title:self.titleField.text roomDesc:self.noticeTextView.text backPic:backPic rewardId:nil];
    }
}


#pragma mark - private method
-(void)keyboardHide:(UITapGestureRecognizer*)tap{
    [self.noticeTextView resignFirstResponder];
    [self.titleField resignFirstResponder];
}

- (void) updateOpenRoomBtnState {
    if (self.userProtocolView.isSelect) {
        [self.openRoomBtn setButtonEnabled:YES];
    } else {
        [self.openRoomBtn setButtonEnabled:NO];
    }
}

- (void)onRightBtnClicked
{
    [self.navigationController dismissViewControllerAnimated:YES completion:nil];
    
}


- (void)showIsInLiveAlert {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCRoomAlreadyOpen, nil) message:NSLocalizedString(XCRoomIfEnterRoomAgain, nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomEnterRoom, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @weakify(self);
        [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:[GetCore(YPAuthCoreHelp) getUid].userIDValue succ:^(YPChatRoomInfo *roomInfo) {
            [MBProgressHUD hideHUD];
            @strongify(self);
            [self.navigationController dismissViewControllerAnimated:YES completion:^{
                [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
            }];
        } fail:^(NSString *errorMsg) {
            @strongify(self);
            [self.view makeToast:errorMsg duration:5 position:CSToastPositionCenter];
        }];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [MBProgressHUD hideHUD];
    }];
    [alert addAction:cancel];
    [alert addAction:enter];
    [self presentViewController:alert animated:YES completion:nil];
}

- (void)updateViewByLastRoomInfo:(YPChatRoomInfo *)roomInfo
{
    if (roomInfo.backPic.length > 0) {
        [self.coverBg yy_setImageWithURL:[NSURL URLWithString:roomInfo.backPic] placeholder:nil];
    }
    
    if (roomInfo.title.length > 0) {
        self.titleField.text = roomInfo.title;
    }
    
    if (roomInfo.roomDesc.length > 0) {
        self.noticeTextView.text = roomInfo.roomDesc;
    }
}


@end
