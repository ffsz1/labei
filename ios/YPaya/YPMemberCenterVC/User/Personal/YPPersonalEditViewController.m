//
//  YPPersonalEditViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPersonalEditViewController.h"
#import "YPYYActionSheetViewController.h"
#import "YPSingleLineTextModifyVC.h"
#import "YPMultiLineTextModifyVC.h"
#import "YPPersonalVoiceEditVC.h"
#import "YPUserViewControllerFactory.h"
#import "YPImagePickerUtils.h"
#import "YPAvatarControl.h"
#import "UserInfo.h"
#import "YPUserCoreHelp.h"
#import "YPAuthCoreHelp.h"
#import "YPFileCore.h"
#import "HJFileCoreClient.h"
#import "YPMediaCore.h"
#import "HJMediaCoreClient.h"
#import "UIView+Toast.h"
#import "UIViewController+Cloudox.h"
#import "UINavigationController+Cloudox.h"
#import "YPEditPersonalPhotosVC.h"
#import "YPPersonalPhotoCell.h"
#import "HJUserCoreClient.h"
#import "UIColor+UIColor_Hex.h"

#import "YPVersionCoreHelp.h"

#import "YPMICRecordVC.h"


@interface YPPersonalEditViewController () <HJFileCoreClient, UINavigationControllerDelegate, UIImagePickerControllerDelegate,HJUserCoreClient>
@property (nonatomic, strong) UserInfo *userInfo;
@property (weak, nonatomic) IBOutlet YPAvatarControl *avatar;
@property (weak, nonatomic) IBOutlet UILabel *birth;
@property (weak, nonatomic) IBOutlet UILabel *selfDesc;
@property (weak, nonatomic) IBOutlet UILabel *nickLabel;
@property (weak, nonatomic) IBOutlet UILabel *sexLabel;

@property (nonatomic, strong) NSString *filePath;
@end

@implementation YPPersonalEditViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    AddCoreClient(HJFileCoreClient, self);
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJUserCoreClient, self);
    self.title = NSLocalizedString(XCPhotosEditTitle, nil);
    self.view.backgroundColor = [UIColor whiteColor];
//    self.navigationItem.leftBarButtonItem.tintColor = [UIColor blackColor];

//    self.userInfo = [GetCore(UserCore) getUserInfo:[GetCore(AuthCore) getUid].userIDValue refresh:YES];

    [self initView];
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
//    return UIStatusBarStyleLightContent; //返回白色
    return UIStatusBarStyleDefault;    //返回黑色
}
- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    if([GetCore(YPMediaCore) isPlaying]) {
        [GetCore(YPMediaCore) stopPlay];
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void) initView
{
  
    self.avatar.layer.cornerRadius = 50.f;
    self.avatar.layer.masksToBounds = YES;
    [self updateUserInfo];
   
}

- (void) onVoicePlayOrStopBtnClicked:(UITapGestureRecognizer *)recognizer
{
    if (self.filePath == nil) {
        [GetCore(YPFileCore) downloadVoice:self.userInfo.userVoice];
    } else {
        if ([GetCore(YPMediaCore) isPlaying]) {
            [GetCore(YPMediaCore) stopPlay];
        } else {
            [GetCore(YPMediaCore) play:self.filePath];
        }
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)showAvatarController {
    YPYYActionSheetViewController *actionSheet = [YPImagePickerUtils showImagePickerSystemLibSheet:^{
        
    } title:nil view:self.view tailor:YES delegate:self];
    [actionSheet show];
}

-(void)showDateController{
    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc] init];
    UIDatePicker *datePicker = [[UIDatePicker alloc] init];
    datePicker.datePickerMode = UIDatePickerModeDate;
    NSLocale *locale = [[NSLocale alloc]initWithLocaleIdentifier:@"zh_CN"];
    datePicker.locale = locale;
    datePicker.date = [NSDate dateWithTimeIntervalSince1970:self.userInfo.birth / 1000];
    datePicker.backgroundColor = [UIColor whiteColor];
    [sheet addTitleView:datePicker];
    
    @weakify(self)
    [sheet addButtonWithTitle:NSLocalizedString(XCRoomConfirm, nil) block:^(YPYYActionSheetViewController *controller) {
        @strongify(self)
        NSDate *theDate = datePicker.date;
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        dateFormatter.dateFormat = @"YYYY-MM-dd";
        NSString *dateStr = [dateFormatter stringFromDate:theDate];
        if (GetCore(YPVersionCoreHelp).checkIn) {
            int numer = [self intervalFromLastDate:theDate toTheDate:[NSDate date]];
            if (numer < 6575) {
                UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:NSLocalizedString(@"未满18周岁禁止使用", nil) preferredStyle:UIAlertControllerStyleAlert];
                UIAlertAction *sure = [UIAlertAction actionWithTitle:NSLocalizedString(MMPopViewConfirm, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
                    
                }];
                [alert addAction:sure];
                [self presentViewController:alert animated:YES completion:nil];
                return;
            }
        }
        
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [self updateUserInfo:@"birth" value:dateStr];
    }];
    [sheet show];
}

- (int)intervalFromLastDate: (NSDate *) d1 toTheDate:(NSDate *) d2

{
    
    NSTimeInterval late1=[d1 timeIntervalSince1970]*1;
    
    NSTimeInterval late2=[d2 timeIntervalSince1970]*1;
    
    NSTimeInterval cha=late2-late1;
    
    NSString *timeString=@"";
    
    int day = (int)cha/3600/24;
    
    return day;
    
}

-(void)showNickModifyController
{
    YPSingleLineTextModifyVC *vc = (YPSingleLineTextModifyVC *)[[YPUserViewControllerFactory sharedFactory] instantiateSingleLineTextModifyViewController];
    vc.userID = self.userInfo.uid;
    vc.defaultText = self.nickLabel.text;
    vc.maxLength = 15;
    vc.pageTitle = NSLocalizedString(XCEditChangeNick, nil);
    vc.key = @"nick";
    [self.navigationController pushViewController:vc animated:YES];
}



- (void) showSexController
{
    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc] init];
    
    __weak __typeof__(self) wself = self;
    [sheet addButtonWithTitle:NSLocalizedString(XCEditChangeBoy, nil) block:^(YPYYActionSheetViewController *controller) {
        __strong __typeof (wself) sSelf = wself;
        sSelf.sexLabel.text = NSLocalizedString(XCEditChangeBoy, nil);
        [self updateUserInfo:@"gender" value:@"1"];
    }];
    
    [sheet addButtonWithTitle:NSLocalizedString(XCEditChangeGirl, nil) block:^(YPYYActionSheetViewController *controller) {
        __strong __typeof (wself) sSelf = wself;
        sSelf.sexLabel.text = NSLocalizedString(XCEditChangeGirl, nil);
        [self updateUserInfo:@"gender" value:@"2"];
    }];
    
    [sheet addCancelButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:^(YPYYActionSheetViewController *controller) {
        
    }];
    [sheet show];
}

-(void)showSelfDescModifyController
{
    YPMultiLineTextModifyVC *vc = (YPMultiLineTextModifyVC *)[[YPUserViewControllerFactory sharedFactory] instantiateMultiLineTextModifyViewController];
    vc.userID = self.userInfo.uid;
    vc.defaultText = self.selfDesc.text;
    vc.maxLength = 60;
    vc.pageTitle = NSLocalizedString(XCEditChangeIntroduction, nil);
    vc.key = @"userDesc";
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)showVoiceEditViewController
{
    YPPersonalVoiceEditVC *vc = (YPPersonalVoiceEditVC *)[[YPUserViewControllerFactory sharedFactory] instantiatePersonalVoiceEditViewController];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)updateUserInfo:(NSString *)key value:(NSString *)value;
{
    NSMutableDictionary *userinfos = [NSMutableDictionary dictionary];
    [userinfos setObject:value forKey:key];
    
    [[GetCore(YPUserCoreHelp) saveUserInfoWithUserID:self.userInfo.uid userInfos:userinfos] subscribeNext:^(id x) {
        [MBProgressHUD hideHUD];
        [self.view makeToast:NSLocalizedString(XCEditChangeSuccess, nil) duration:3 position:CSToastPositionCenter];
    } error:^(NSError *error) {
        [MBProgressHUD hideHUD];
        [self.view makeToast:NSLocalizedString(XCHudNetError, nil) duration:3 position:CSToastPositionCenter];
    }];
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [picker dismissViewControllerAnimated:YES completion:^{
        UIImage *selectedPhoto = [info objectForKey:UIImagePickerControllerEditedImage];
        if (selectedPhoto) {
            if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
                UIImageWriteToSavedPhotosAlbum(selectedPhoto, nil, nil, nil);
            }
            [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
//            [GetCore(FileCore) uploadAvatar:selectedPhoto];
            [GetCore(YPFileCore) qiNiuUploadImage:selectedPhoto uploadType:UploadImageTypeAvtor];
        }
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - MediaCoreClient
- (void) onPlayAudioBegan:(NSString *)filePath {
}

- (void)onPlayAudioComplete:(NSString *)filePath {
}

#pragma mark - FileCoreClient
- (void)onDownloadVoiceSuccess:(NSString *)filePath
{
    self.filePath = filePath;
    
    if ([GetCore(YPMediaCore) isPlaying]) {
        [GetCore(YPMediaCore) stopPlay];
    }
    
    [GetCore(YPMediaCore) play:filePath];
}

- (void)onDownloadVoiceFailth:(NSError *)error
{
    [self.view makeToast:@"播放失败，请检查网络" duration:3 position:CSToastPositionCenter];
}

- (void)onUploadSuccess:(NSString *)url
{
    [self updateUserInfo:@"avatar" value:url];
}

- (void)onUploadFailth:(NSError *)error
{
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"请求失败，请检查网络" duration:3 position:CSToastPositionCenter];
}

#pragma mark - FileCoreClient user 7N
- (void)didUploadAvtorImageSuccessUseQiNiu:(NSString *)key{
    NSLog(@"didUploadAvtorImageSuccessUseQiNiu");
    NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,key];
    [self updateUserInfo:@"avatar" value:url];
}
- (void)didUploadAvtorImageFailUseQiNiu:(NSString *)message{
    NSLog(@"didUploadAvtorImageFailUseQiNiu:%@",message);
    [MBProgressHUD hideHUD];
    [self.view makeToast:@"请求失败，请检查网络" duration:3 position:CSToastPositionCenter];
}


#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0) {
        [self showAvatarController];
    } else if (indexPath.row == 1) {
        [self showNickModifyController];
    } else if (indexPath.row == 2) {
//        [self showSexController];
    } else if (indexPath.row == 3) {
        [self showDateController];
    } else if (indexPath.row == 4) {
        [self showSelfDescModifyController];
    }else if (indexPath.row == 5){
        YPMICRecordVC *MICRecordViewController = [YPMICRecordVC new];
        [self.navigationController pushViewController:MICRecordViewController animated:YES];
        
    }else if (indexPath.row == 6){
     //相册管理
        YPEditPersonalPhotosVC *vc = (YPEditPersonalPhotosVC *)[[YPUserViewControllerFactory sharedFactory]instantiateEditPersonalPhotosViewController];
        vc.uid = self.userInfo.uid;
        [self.navigationController pushViewController:vc animated:YES];
        
    }
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.00001f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0) {
        return 150;
    } else  {
        return 56;
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    return [super tableView:tableView viewForFooterInSection:section];
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row == 2) {
        return NO;
    }
    return YES;
}

#pragma mark - UserCoreClient
- (void)onCurrentUserInfoUpdate:(UserInfo *)userInfo {
    [self updateUserInfo];
}

#pragma mark - Update User Info
- (void)updateUserInfo {
    @weakify(self);
    [GetCore(YPUserCoreHelp) getUserInfo:[GetCore(YPAuthCoreHelp) getUid].userIDValue refresh:YES success:^(UserInfo *info) {
        @strongify(self);
        self.userInfo = info;
        self.nickLabel.text = self.userInfo.nick;
        if (self.userInfo.gender == UserInfo_Female) {
            self.sexLabel.text = NSLocalizedString(XCEditChangeGirl, nil);
        }else {
            self.sexLabel.text = NSLocalizedString(XCEditChangeBoy, nil);
        }
        if (self.userInfo.userDesc.length) {
            self.selfDesc.text = self.userInfo.userDesc;
            self.selfDesc.textColor = [UIColor colorWithHexString:@"#333333"];
        }else {
            self.selfDesc.text = NSLocalizedString(XCEditChangeSign, nil);
            self.selfDesc.textColor = [UIColor colorWithHexString:@"#999999"];
        }
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        dateFormatter.dateFormat = @"YYYY-MM-dd";
        NSString *dateStr = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:self.userInfo.birth/1000]];
        self.birth.text = dateStr;
        
        NSLog(@"%ld",self.userInfo.birth);
        
        [self.avatar setImageURL:[NSURL URLWithString:self.userInfo.avatar]];

        self.filePath = nil;
    }];
}

@end
