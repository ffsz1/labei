//
//  ImagePickerUtils.m
//  YYMobile
//
//  Created by Liuyuxiang on 16/6/8.
//  Copyright © 2016年 YY.inc. All rights reserved.
//
#import "UIApplication+JXBase.h"
#import "ImagePickerUtils.h"
#import "PhotoAssetsUtility.h"
#import "CameraUtility.h"
#import "YYCurrentContextPresentingController.h"
#import "YYViewControllerCenter.h"
#import "YYImCameraViewController.h"
#import "YYUtility.h"
#import <AssetsLibrary/AssetsLibrary.h>

@implementation ImagePickerUtils

+ (YYActionSheetViewController*)showImagePickerSystemLibSheet:(void (^)(void))cancel title:(NSString *)title view:(UIView *)view tailor:(BOOL)tailo delegate:(id<UINavigationControllerDelegate,UIImagePickerControllerDelegate>)delegate
{
//    NSString *sendPicStr = getLocalizedStringFromTable(@"IM_Tip_SendPic", @"IMessages", @"发送图片");
    NSString *cancelStr = NSLocalizedString(XCRoomCancel, nil);
    NSString *selectedPicStr = @"本地照片";
    NSString *photoStr = @"拍照上传";
    YYActionSheetViewController *actionSheet = [[YYActionSheetViewController alloc] init];
    if(title != nil) {
        [actionSheet addTitleText:title];
        
    }
    
    [actionSheet addButtonWithTitle:selectedPicStr block:^(YYActionSheetViewController *controller){
        [PhotoAssetsUtility checkPhtotAssetsAvailable:^{
            YYImCameraViewController *imagePicker = [[YYImCameraViewController alloc] init];
            imagePicker.delegate = delegate;
            imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
            imagePicker.allowsEditing = tailo;
//            UIViewController* controller = [YYViewControllerCenter currentVisiableRootViewController];
            UIViewController* controller = [UIApplication sharedApplication].jx_topViewController;
            [YYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
            
            [controller presentViewController:imagePicker animated:YES completion:NULL];
            
            
        }];
    }];

    [actionSheet addButtonWithTitle:photoStr block:^(YYActionSheetViewController *controller){
        [CameraUtility checkCameraAvailable:^{
            YYImCameraViewController *imagePicker = [[YYImCameraViewController alloc] init];
            imagePicker.delegate = delegate;
            imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
            imagePicker.allowsEditing = tailo;
//            UIViewController* controller = [YYViewControllerCenter currentVisiableRootViewController];
             UIViewController* controller = [UIApplication sharedApplication].jx_topViewController;
            [YYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
            
            [controller presentViewController:imagePicker animated:YES completion:NULL];
            
            
        }];

    }];
    
    [actionSheet addCancelButtonWithTitle:cancelStr block:^(YYActionSheetViewController *controller){
        cancel();
    }];
    
    return actionSheet;
}

+ (void)openCameroController:(id<UINavigationControllerDelegate,UIImagePickerControllerDelegate>)cameroDelegate
{
    [CameraUtility checkCameraAvailable:^{
        YYImCameraViewController *imagePicker = [[YYImCameraViewController alloc] init];
        imagePicker.delegate = cameroDelegate;
        imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
        imagePicker.allowsEditing = YES;
        UIViewController* controller = [YYViewControllerCenter currentVisiableRootViewController];
        
        [YYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
        
        [controller presentViewController:imagePicker animated:YES completion:NULL];
        
        
    }];

}

@end
