//
//  CameraUtility.m
//  YYMobile
//
//  Created by James Pend on 14-8-29.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "CameraUtility.h"
#import "YYUtility.h"
#import "YYAlertView.h"

@implementation CameraUtility

+ (void)checkCameraAvailable:(void(^)(void))available
{
    void(^showPrivacyDeniedAlert)() = ^{
        YYAlertView *alertView = [[YYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCamaralCantUse, nil) message:NSLocalizedString(XCPhotoCamaralCantUseTip1, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    void(^showAccessibilityAlert)() = ^{
        YYAlertView *alertView = [[YYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCamaralCantUse, nil) message:NSLocalizedString(XCPhotoCamaralCantUseTip2, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    [YYUtility checkCameraAvailable:available denied:showPrivacyDeniedAlert restriction:showAccessibilityAlert];
}


@end
