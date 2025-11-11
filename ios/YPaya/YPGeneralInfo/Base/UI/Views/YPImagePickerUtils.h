//
//  YPImagePickerUtils.h
//  YYMobile
//
//  Created by Liuyuxiang on 16/6/8.
//  Copyright © 2016年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPYYActionSheetViewController.h"
@interface YPImagePickerUtils : NSObject

+ (YPYYActionSheetViewController*) showImagePickerSystemLibSheet:(void(^)(void))cancel title:(NSString *)title view:(UIView *)view tailor:(BOOL)tailor delegate:(id<UINavigationControllerDelegate, UIImagePickerControllerDelegate>)delegate;

+ (void) openCameroController:(id<UINavigationControllerDelegate, UIImagePickerControllerDelegate>)cameroDelegate;
@end
