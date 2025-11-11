//
//  HJFamilyCreatVC.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyCreatVC.h"

#import "YYActionSheetViewController.h"
#import "ImagePickerUtils.h"

#import "HJFileCore.h"
#import "HJWKWebViewController.h"
#import "HJHttpRequestHelper+Family.h"
#import "HJFileCoreClient.h"
#import "HJFamilyCore.h"
#import "HJFamilyCoreClient.h"

typedef NS_ENUM(NSInteger, XCFamilyCreatePhotoType) {
    XCFamilyCreatePhotoTypeUndefine = 0,
    XCFamilyCreatePhotoTypeLogo,
    XCFamilyCreatePhotoTypeBackground,
};

@interface HJFamilyCreatVC ()<UITextFieldDelegate,UITextViewDelegate, HJFamilyCoreClient>
@property (weak, nonatomic) IBOutlet UIImageView *topBgImageView;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UITextField *nameTextF;
@property (weak, nonatomic) IBOutlet UILabel *nameNumLabel;
@property (weak, nonatomic) IBOutlet UILabel *noticTipLabel;
@property (weak, nonatomic) IBOutlet UILabel *noticNumLabel;
@property (weak, nonatomic) IBOutlet UITextView *noticTextView;
@property (weak, nonatomic) IBOutlet UIButton *creatBtn;
@property (weak, nonatomic) IBOutlet UIButton *delegateBtn;

@property (nonatomic, strong) UIImage *selectPhoto;
@property (nonatomic, strong) UIImage *selectBackgroundImage;
@property (nonatomic, copy) NSString *logoUrlStr;
@property (nonatomic, copy) NSString *backgroundURLString;

@property (nonatomic, assign) XCFamilyCreatePhotoType createPhotoType;


@end

@implementation HJFamilyCreatVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self updateCreatBtnUserEnabled:NO];
    
    [self.nameTextF addTarget:self action:@selector(nameTextDidChange) forControlEvents:UIControlEventEditingChanged];
    
    AddCoreClient(HJFileCoreClient, self);
    AddCoreClient(HJFamilyCoreClient, self);
    
}

-(void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}



- (void)updateCreatBtnUserEnabled:(BOOL)enable
{
    self.creatBtn.userInteractionEnabled = enable;
    [self.creatBtn setBackgroundImage:[UIImage imageNamed:enable?@"hj_family_creat_bottom":@"hj_family_creat_bottom_unused"] forState:UIControlStateNormal];
}

- (void)checkData
{
    BOOL enable = YES;
    
    if (self.selectPhoto == nil) {
        enable = NO;
    }
    
    if (self.selectBackgroundImage == nil) {
        enable = NO;
    }
    
    if (self.nameTextF.text.length == 0) {
        enable = NO;
    }
    
    if (self.noticTextView.text.length == 0) {
        enable = NO;
    }
    
    if (self.delegateBtn.selected == NO) {
        enable = NO;
    }
    
    [self updateCreatBtnUserEnabled:enable];
}

- (IBAction)photoBtnAction:(id)sender {
    self.createPhotoType = XCFamilyCreatePhotoTypeBackground;
    YYActionSheetViewController *actionSheet = [ImagePickerUtils showImagePickerSystemLibSheet:^{
        
    } title:nil view:self.view tailor:YES delegate:self];
    [actionSheet show];
    
}
- (IBAction)avatarTapAction:(id)sender {
    self.createPhotoType = XCFamilyCreatePhotoTypeLogo;
    YYActionSheetViewController *actionSheet = [ImagePickerUtils showImagePickerSystemLibSheet:^{
        
    } title:nil view:self.view tailor:YES delegate:self];
    [actionSheet show];
    
}
- (IBAction)creatBtnAction:(id)sender {
    self.logoUrlStr = nil;
    self.backgroundURLString = nil;
    
    [MBProgressHUD showMessage:@"上传中..."];
    [self uploadLogo];
    
}

- (IBAction)delegateBtnAction:(id)sender {
    
    self.delegateBtn.selected = !self.delegateBtn.selected;
    
    [self checkData];
    
}
- (IBAction)delegateLabelTagAction:(id)sender {
    
    HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/family/xieyi.html",[HJHttpRequestHelper getHostUrl]];
    vc.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)uploadLogo {
    [GetCore(HJFileCore) qiNiuUploadImage:self.selectPhoto uploadType:UploadImageTypeAvtor];
}

- (void)uploadBackground {
    [GetCore(HJFileCore) qiNiuUploadImage:self.selectBackgroundImage uploadType:UploadImageTypeAvtor];
}

- (void)nameTextDidChange
{
    self.nameNumLabel.text = [NSString stringWithFormat:@"%lu/8",(unsigned long)self.nameTextF.text.length];
    
    [self checkData];
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    if (textField.text.length > 7 && ![string isEqualToString:@""]) {
        return NO;
    }
    
    NSString *result= [textField.text stringByReplacingCharactersInRange:range withString:string];
    
    if (result.length > 8) {
        return NO;
    }
    
//    self.nameNumLabel.text = [NSString stringWithFormat:@"%lu/8",(unsigned long)result.length];
    
    
    return YES;
}

- (void)textViewDidChange:(UITextView *)textView
{
    self.noticNumLabel.text = [NSString stringWithFormat:@"%lu/50",(unsigned long)self.noticTextView.text.length];
    
    [self checkData];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    if (textView.text.length > 49 && ![text isEqualToString:@""]) {
        return NO;
    }
    
    NSString *result= [textView.text stringByReplacingCharactersInRange:range withString:text];
    
    if (result.length > 50) {
        return NO;
    }
    
    
    if (result.length>0) {
        self.noticTipLabel.hidden = YES;
    }else self.noticTipLabel.hidden = NO;
    
    return YES;
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [picker dismissViewControllerAnimated:YES completion:^{
        UIImage *selectedPhoto = [info objectForKey:UIImagePickerControllerEditedImage];
        switch (self.createPhotoType) {
            case XCFamilyCreatePhotoTypeUndefine:
                break;
            case XCFamilyCreatePhotoTypeLogo:
            {
                self.selectPhoto = selectedPhoto;
                self.avatarImageView.image = self.selectPhoto;
                self.logoUrlStr = nil;
            }
                break;
            case XCFamilyCreatePhotoTypeBackground:
            {
                self.selectBackgroundImage = selectedPhoto;
                self.topBgImageView.image = self.selectBackgroundImage;
                self.backgroundURLString = nil;
            }
                break;
        }
        
        if (selectedPhoto) {
            if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
                UIImageWriteToSavedPhotosAlbum(selectedPhoto, nil, nil, nil);
            }
        }
        
        [self checkData];
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:nil];
}



#pragma mark - FileCoreClient
//上传图片成功
- (void)didUploadAvtorImageSuccessUseQiNiu:(NSString *)key{
    if (!self.logoUrlStr.length) {
        NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,key];
        self.logoUrlStr = [url copy];
        [self uploadBackground];
        return;
    }
    
    if (!self.backgroundURLString.length) {
        NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,key];
        self.backgroundURLString = [url copy];
    }
    [self creatPost];
}

//上传图片失败
- (void)didUploadAvtorImageFailUseQiNiu:(NSString *)message{
    self.logoUrlStr = nil;
    self.backgroundURLString = nil;
    [MBProgressHUD showError:@"图片上传失败"];
}

- (void)creatPost
{
    [GetCore(HJFamilyCore) familyCreateFamilyTeamWithBgImg:self.backgroundURLString logo:self.logoUrlStr name:self.nameTextF.text notice:self.noticTextView.text hall:@""];
}

- (void)familyCreateFamilyTeamSuccess {
    [MBProgressHUD showSuccess:@"申请成功" toView:[UIApplication sharedApplication].keyWindow];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)familyCreateFamilyTeamFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

@end
