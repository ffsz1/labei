//
//  YPImagePickerUtils.m
//  YYMobile
//
//  Created by Liuyuxiang on 16/6/8.
//  Copyright © 2016年 YY.inc. All rights reserved.
//
#import "UIApplication+JXBase.h"
#import "YPImagePickerUtils.h"
#import "YPPhotoAssetsUtility.h"
#import "YPCameraUtility.h"
#import "YPYYCurrentContextPresentingController.h"
#import "YPYYViewControllerCenter.h"
#import "YPYYImCameraViewController.h"
#import "YYUtility.h"
#import <AssetsLibrary/AssetsLibrary.h>

@implementation YPImagePickerUtils

+ (YPYYActionSheetViewController*)showImagePickerSystemLibSheet:(void (^)(void))cancel title:(NSString *)title view:(UIView *)view tailor:(BOOL)tailo delegate:(id<UINavigationControllerDelegate,UIImagePickerControllerDelegate>)delegate
{
//    NSString *sendPicStr = getLocalizedStringFromTable(@"IM_Tip_SendPic", @"IMessages", @"发送图片");
    NSString *cancelStr = NSLocalizedString(XCRoomCancel, nil);
    NSString *selectedPicStr = @"本地照片";
    NSString *photoStr = @"拍照上传";
    YPYYActionSheetViewController *actionSheet = [[YPYYActionSheetViewController alloc] init];
    if(title != nil) {
        [actionSheet addTitleText:title];
        
    }
    
    [actionSheet addButtonWithTitle:selectedPicStr block:^(YPYYActionSheetViewController *controller){
        [YPPhotoAssetsUtility checkPhtotAssetsAvailable:^{
            YPYYImCameraViewController *imagePicker = [[YPYYImCameraViewController alloc] init];
            imagePicker.delegate = delegate;
            imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
            imagePicker.allowsEditing = tailo;
//            UIViewController* controller = [YPYYViewControllerCenter currentVisiableRootViewController];
            UIViewController* controller = [UIApplication sharedApplication].jx_topViewController;
            [YPYYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
            
            [controller presentViewController:imagePicker animated:YES completion:NULL];
            
            
        }];
    }];

    [actionSheet addButtonWithTitle:photoStr block:^(YPYYActionSheetViewController *controller){
        [YPCameraUtility checkCameraAvailable:^{
            YPYYImCameraViewController *imagePicker = [[YPYYImCameraViewController alloc] init];
            imagePicker.delegate = delegate;
            imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
            imagePicker.allowsEditing = tailo;
//            UIViewController* controller = [YPYYViewControllerCenter currentVisiableRootViewController];
             UIViewController* controller = [UIApplication sharedApplication].jx_topViewController;
            [YPYYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
            
            [controller presentViewController:imagePicker animated:YES completion:NULL];
            
            
        }];

    }];
    
    [actionSheet addCancelButtonWithTitle:cancelStr block:^(YPYYActionSheetViewController *controller){
        cancel();
    }];
    
    return actionSheet;
}

+ (void)openCameroController:(id<UINavigationControllerDelegate,UIImagePickerControllerDelegate>)cameroDelegate
{
    [YPCameraUtility checkCameraAvailable:^{
        YPYYImCameraViewController *imagePicker = [[YPYYImCameraViewController alloc] init];
        imagePicker.delegate = cameroDelegate;
        imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
        imagePicker.allowsEditing = YES;
        UIViewController* controller = [YPYYViewControllerCenter currentVisiableRootViewController];
        
        [YPYYCurrentContextPresentingController shareController].currentContextPresentedViewController = imagePicker;
        
        [controller presentViewController:imagePicker animated:YES completion:NULL];
        
        
    }];

}

@end
