//
//  HJLoginFillDataVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJLoginFillDataVC.h"

#import "HJWeChatUserInfo.h"
#import "HJQqUserInfo.h"

#import "YYActionSheetViewController.h"

#import "HJLoginSetAvatarVC.h"
#import "HJImageUploader.h"
#import "HJLinkedMeCore.h"
#import "HJSignnAlterView.h"
#import "YYActionSheetViewController.h"
#import "ImagePickerUtils.h"
#import "AvatarControl.h"
#import "UserInfo.h"
#import "HJUserCoreHelp.h"
#import "HJAuthCoreHelp.h"
#import "HJFileCore.h"
#import "HJFileCoreClient.h"
@interface HJLoginFillDataVC ()<UINavigationControllerDelegate, UIImagePickerControllerDelegate>

@property (weak, nonatomic) IBOutlet GGButton *avatarBtn;
@property (weak, nonatomic) IBOutlet UITextField *nameTextF;
@property (weak, nonatomic) IBOutlet UITextField *birthdayTextF;
@property (weak, nonatomic) IBOutlet UIButton *manBtn;
@property (weak, nonatomic) IBOutlet UIButton *womanBtn;
@property (weak, nonatomic) IBOutlet UIButton *enterBtn;

@property (strong, nonatomic) HJWeChatUserInfo *info;
@property (strong, nonatomic) HJQqUserInfo *qqInfo;

@property (strong,nonatomic) UIImage *uploadImage;

@end

@implementation HJLoginFillDataVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self checkThirdLogin];
      [self.nameTextF addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    self.uploadImage = [UIImage imageNamed:@"lab_login_woman"];
    [self.avatarBtn setImage:self.uploadImage forState:UIControlStateNormal];
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

#pragma mark private mothed
//判断是否第三方登录
- (void)checkThirdLogin {
    
    __weak typeof(self) weakSelf = self;
    if (GetCore(HJAuthCoreHelp).info.openid) {
        self.qqInfo = nil;
        self.info = GetCore(HJAuthCoreHelp).info;
        GetCore(HJAuthCoreHelp).isNewRegister = YES;
        
        [self loadAvatarUrl:self.info.headimgurl];
        
        self.nameTextF.text = self.info.nickname;
        if ([self.info.sex isEqualToString:@"1"]) {
            self.manBtn.selected = YES;
            self.womanBtn.selected = NO;
        }else {
            self.womanBtn.selected = YES;
            self.manBtn.selected = NO;
        }
    } else if (GetCore(HJAuthCoreHelp).qqInfo.openID) {
        self.info = nil;
        self.qqInfo = GetCore(HJAuthCoreHelp).qqInfo;
        GetCore(HJAuthCoreHelp).isNewRegister = YES;
        
        [self loadAvatarUrl:self.qqInfo.figureurl_qq_2];

        self.nameTextF.text = self.qqInfo.nickname;
        
        
    }
}

- (void)loadAvatarUrl:(NSString *)url
{
    __weak typeof(self)weakSelf = self;
    [[SDWebImageManager sharedManager].imageDownloader downloadImageWithURL:[NSURL URLWithString:url] options:SDWebImageDownloaderLowPriority progress:nil completed:^(UIImage * _Nullable image, NSData * _Nullable data, NSError * _Nullable error, BOOL finished) {
        
        if (finished) {
            if (image != nil) {
                [weakSelf.avatarBtn setImage:image forState:UIControlStateNormal];
                weakSelf.uploadImage = image;
            }
        }
        
    }];
}

- (void)updateEnterBtnStatus
{
    if (self.uploadImage!=nil && self.nameTextF.text.length>0 && self.birthdayTextF.text.length>0) {
        self.enterBtn.enabled = YES;
    }else{
        self.enterBtn.enabled = NO;
    }
}

- (void)textFieldDidChange :(UITextField *)theTextField{

    [self updateEnterBtnStatus];
}

#pragma mark IBAction

- (IBAction)backAction:(id)sender {
    
    [GetCore(HJAuthCoreHelp) logout];
    [self.navigationController dismissViewControllerAnimated:YES completion:nil];
    
}

- (IBAction)avatarBtnAction:(id)sender {
    
//    HJLoginSetAvatarVC *vc = HJLoginStoryBoard(@"HJLoginSetAvatarVC");
//    vc.isMan = self.manBtn.selected;
//    __weak typeof(self)weakSelf = self;
//    vc.setAvatarBlock = ^(UIImage * _Nullable image) {
//        [weakSelf.avatarBtn setImage:image forState:UIControlStateNormal];
//        weakSelf.uploadImage = image;
//
//        [weakSelf updateEnterBtnStatus];
//    };
//    [self.navigationController pushViewController:vc animated:YES];
    YYActionSheetViewController *actionSheet = [ImagePickerUtils showImagePickerSystemLibSheet:^{
        
    } title:nil view:self.view tailor:YES delegate:self];
    [actionSheet show];
    
}

- (IBAction)birthdayBtnAction:(id)sender {
    
    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc] init];
    UIDatePicker *datePicker = [[UIDatePicker alloc] init];
    datePicker.datePickerMode = UIDatePickerModeDate;
    NSLocale *locale = [[NSLocale alloc]initWithLocaleIdentifier:@"zh_CN"];
    datePicker.locale = locale;
    NSString *dateStr = @"";
    
    if (self.birthdayTextF.text.length > 0) {
        dateStr = self.birthdayTextF.text;
    } else {
        dateStr = @"1990-01-01";
    }
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"YYYY-MM-dd";
    NSDate *date = [dateFormatter dateFromString:dateStr];
    datePicker.date = date;
    datePicker.backgroundColor = [UIColor whiteColor];
    [sheet addTitleView:datePicker];
    
    @weakify(self)
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomConfirm, nil) block:^(YYActionSheetViewController *controller) {
        @strongify(self)
        NSDate *theDate = datePicker.date;
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        dateFormatter.dateFormat = @"YYYY-MM-dd";
        NSString *dateStr = [dateFormatter stringFromDate:theDate];
        self.birthdayTextF.text = dateStr;
        [self updateEnterBtnStatus];
        
    }];
    [sheet show];
    
}

- (IBAction)manBtnAction:(UIButton *)sender {
    self.manBtn.selected = YES;
    self.womanBtn.selected = NO;
    self.uploadImage = [UIImage imageNamed:@"lab_login_man"];
    [self.avatarBtn setImage:self.uploadImage forState:UIControlStateNormal];
}

- (IBAction)womanBtnAction:(UIButton *)sender {
    self.manBtn.selected = NO;
    self.womanBtn.selected = YES;
    self.uploadImage = [UIImage imageNamed:@"lab_login_woman"];
    [self.avatarBtn setImage:self.uploadImage forState:UIControlStateNormal];
}

- (IBAction)enterBtnAction:(id)sender {
    
    [MBProgressHUD showMessage:@"上传中..."];
    
    [HJImageUploader uploadImage:self.uploadImage succeed:^(NSString *url) {
        
        [self updateInfoData:url];
        
    } failed:^(int code, NSString *msg) {
        [MBProgressHUD hideHUD];
//         [self updateInfoData:nil];//life-rh
    }];
}

- (void)updateInfoData:(NSString *)url
{
    if (url != nil) {
//     if (url == nil) { //life-rh
        
        UserID userId = GetCore(HJAuthCoreHelp).getUid.userIDValue;
        NSMutableDictionary *userinfos = [NSMutableDictionary dictionary];
        [userinfos setObject:url forKey:@"avatar"]; //life-rh
        [userinfos setObject:self.nameTextF.text forKey:@"nick"];
        
        //去除生日
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        dateFormatter.dateFormat = @"YYYY-MM-dd";
        [userinfos setObject:self.birthdayTextF.text forKey:@"birth"];
        
        NSInteger gender;
        if (self.womanBtn.selected) {
            gender = 2;
        }else {
            gender = 1;
        }
        [userinfos setObject:@(gender) forKey:@"gender"];
        if (GetCore(HJLinkedMeCore).H5URL){
            [GetCore(HJAuthCoreHelp) statisticsWith:[NSURL URLWithString:GetCore(HJLinkedMeCore).H5URL]];
        }
        
        @weakify(self);
        [[GetCore(HJUserCoreHelp) saveUserInfoWithUserID:userId userInfos:userinfos] subscribeNext:^(id x) {
            @strongify(self);
            [MBProgressHUD hideHUD];
            GetCore(HJAuthCoreHelp).info = nil;
            GetCore(HJAuthCoreHelp).qqInfo = nil;
            
//            [HJSignnAlterView show];
            [self.navigationController dismissViewControllerAnimated:YES completion:nil];
            
        } error:^(NSError *error) {
            [MBProgressHUD hideHUD];
        }];
    }
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    __weak __typeof__(self) wself = self;
    [picker dismissViewControllerAnimated:YES completion:^{
        UIImage *selectedPhoto = [info objectForKey:UIImagePickerControllerEditedImage];
        if (selectedPhoto) {
            __strong __typeof (wself) sSelf = wself;
            if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
                UIImageWriteToSavedPhotosAlbum(selectedPhoto, nil, nil, nil);
            }
//            [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
//            [GetCore(FileCore) uploadAvatar:selectedPhoto];
//            [GetCore(HJFileCore) qiNiuUploadImage:selectedPhoto uploadType:UploadImageTypeAvtor];
            
            
            [sSelf.avatarBtn setImage:selectedPhoto forState:UIControlStateNormal];
            sSelf.uploadImage = selectedPhoto;
          
                  [sSelf updateEnterBtnStatus];
            
        }
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:nil];
}
@end
