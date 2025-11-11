//
//  HJLoginSetAvatarVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJLoginSetAvatarVC.h"

#import "CameraUtility.h"
#import "YYImCameraViewController.h"
#import "PhotoAssetsUtility.h"
#import "YYViewControllerCenter.h"
#import "YYCurrentContextPresentingController.h"

@interface HJLoginSetAvatarVC ()<UIImagePickerControllerDelegate,UINavigationControllerDelegate>

@property (weak, nonatomic) IBOutlet UIButton *btn1;
@property (weak, nonatomic) IBOutlet UIButton *btn2;
@property (weak, nonatomic) IBOutlet UIButton *btn3;
@property (weak, nonatomic) IBOutlet UIButton *btn4;
@property (weak, nonatomic) IBOutlet UIButton *btn5;
@property (weak, nonatomic) IBOutlet UIButton *btn6;

@property (nonatomic,assign) NSInteger sel;

@end

@implementation HJLoginSetAvatarVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setUI];
}

#pragma mark private method
- (void)resetBtnStatus
{
    self.btn1.selected = NO;
    self.btn2.selected = NO;
    self.btn3.selected = NO;
    self.btn4.selected = NO;
    self.btn5.selected = NO;
    self.btn6.selected = NO;
}

- (void)setUI
{
    [self.btn1 setImage:[UIImage imageNamed:[self getImageByNum:0]] forState:UIControlStateNormal];
    [self.btn2 setImage:[UIImage imageNamed:[self getImageByNum:1]] forState:UIControlStateNormal];
    [self.btn3 setImage:[UIImage imageNamed:[self getImageByNum:2]] forState:UIControlStateNormal];
    [self.btn4 setImage:[UIImage imageNamed:[self getImageByNum:3]] forState:UIControlStateNormal];
    [self.btn5 setImage:[UIImage imageNamed:[self getImageByNum:4]] forState:UIControlStateNormal];
    [self.btn6 setImage:[UIImage imageNamed:[self getImageByNum:5]] forState:UIControlStateNormal];
    
    [self.btn1 setImage:[UIImage imageNamed:[self getSelImageByNum:0]] forState:UIControlStateSelected];
    [self.btn2 setImage:[UIImage imageNamed:[self getSelImageByNum:1]] forState:UIControlStateSelected];
    [self.btn3 setImage:[UIImage imageNamed:[self getSelImageByNum:2]] forState:UIControlStateSelected];
    [self.btn4 setImage:[UIImage imageNamed:[self getSelImageByNum:3]] forState:UIControlStateSelected];
    [self.btn5 setImage:[UIImage imageNamed:[self getSelImageByNum:4]] forState:UIControlStateSelected];
    [self.btn6 setImage:[UIImage imageNamed:[self getSelImageByNum:5]] forState:UIControlStateSelected];
}

- (NSString *)getImageByNum:(NSInteger)num
{
    if (num ==0) return _isMan?@"hj_login_setAvatar_man1":@"hj_login_setAvatar_woman1";
    if (num ==1) return _isMan?@"hj_login_setAvatar_man2":@"hj_login_setAvatar_woman2";
    if (num ==2) return _isMan?@"hj_login_setAvatar_man3":@"hj_login_setAvatar_woman3";
    if (num ==3) return _isMan?@"hj_login_setAvatar_man4":@"hj_login_setAvatar_woman4";
    if (num ==4) return _isMan?@"hj_login_setAvatar_man5":@"hj_login_setAvatar_woman5";
    return _isMan?@"hj_login_setAvatar_man6":@"hj_login_setAvatar_woman6";
}

- (NSString *)getSelImageByNum:(NSInteger)num
{
    if (num ==0) return _isMan?@"hj_login_setAvatar_man1_sel":@"hj_login_setAvatar_woman1_sel";
    if (num ==1) return _isMan?@"hj_login_setAvatar_man2_sel":@"hj_login_setAvatar_woman2_sel";
    if (num ==2) return _isMan?@"hj_login_setAvatar_man3_sel":@"hj_login_setAvatar_woman3_sel";
    if (num ==3) return _isMan?@"hj_login_setAvatar_man4_sel":@"hj_login_setAvatar_woman4_sel";
    if (num ==4) return _isMan?@"hj_login_setAvatar_man5_sel":@"hj_login_setAvatar_woman5_sel";
    return _isMan?@"hj_login_setAvatar_man6_sel":@"hj_login_setAvatar_woman6_sel";
}



#pragma mark IBAction
- (IBAction)backAction:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

- (IBAction)avatarAction1:(id)sender {
    [self resetBtnStatus];
    self.btn1.selected = YES;
    self.sel = 0;
}
- (IBAction)avatarAction2:(id)sender {
    [self resetBtnStatus];
    self.btn2.selected = YES;
    self.sel = 1;

}

- (IBAction)avatarAction3:(id)sender {
    [self resetBtnStatus];
    self.btn3.selected = YES;
    self.sel = 2;

}

- (IBAction)avatarAction4:(id)sender {
    [self resetBtnStatus];
    self.btn4.selected = YES;
    self.sel = 3;

}

- (IBAction)avatarAction5:(id)sender {
    [self resetBtnStatus];
    self.btn5.selected = YES;
    self.sel = 4;

}
- (IBAction)avatarAction6:(id)sender {
    [self resetBtnStatus];
    self.btn6.selected = YES;
    self.sel = 5;

}

- (IBAction)photoBtnAction:(id)sender {
    
    [CameraUtility checkCameraAvailable:^{
        YYImCameraViewController *imagePicker = [[YYImCameraViewController alloc] init];
        imagePicker.delegate = self;
        imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
        imagePicker.allowsEditing = YES;
        UIViewController* controller = [YYViewControllerCenter currentVisiableRootViewController];
        
        [YYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
        
        [controller presentViewController:imagePicker animated:YES completion:NULL];
        
        
    }];
    
}

- (IBAction)photoAlbum:(id)sender {
    
    [PhotoAssetsUtility checkPhtotAssetsAvailable:^{
        YYImCameraViewController *imagePicker = [[YYImCameraViewController alloc] init];
        imagePicker.delegate = self;
        imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        imagePicker.allowsEditing = YES;
        UIViewController* controller = [YYViewControllerCenter currentVisiableRootViewController];
        [YYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
        [controller presentViewController:imagePicker animated:YES completion:NULL];
    }];
    
}

- (IBAction)finishBtnAction:(id)sender {
    
    UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"hj_login_setAvatar_woman%ld",self.sel+1]];
//     if (self.isMan) image = [UIImage imageNamed:[NSString stringWithFormat:@"hj_login_setAvatar_man%ld_sel",self.sel+1]];
    if (self.isMan) image = [UIImage imageNamed:[NSString stringWithFormat:@"hj_login_setAvatar_man%ld",self.sel+1]];
    
    self.setAvatarBlock(image);
    
    [self.navigationController popViewControllerAnimated:YES];
    
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
            sSelf.setAvatarBlock(selectedPhoto);
            [sSelf.navigationController popViewControllerAnimated:YES];
        }
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:^{
    }];
}

@end
