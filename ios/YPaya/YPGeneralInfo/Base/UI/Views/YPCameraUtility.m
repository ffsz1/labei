//
//  YPCameraUtility.m
//  YYMobile
//
//  Created by James Pend on 14-8-29.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPCameraUtility.h"
#import "YYUtility.h"
#import "YPYYAlertView.h"

@implementation YPCameraUtility

+ (void)checkCameraAvailable:(void(^)(void))available
{
    void(^showPrivacyDeniedAlert)() = ^{
        YPYYAlertView *alertView = [[YPYYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCamaralCantUse, nil) message:NSLocalizedString(XCPhotoCamaralCantUseTip1, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    void(^showAccessibilityAlert)() = ^{
        YPYYAlertView *alertView = [[YPYYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCamaralCantUse, nil) message:NSLocalizedString(XCPhotoCamaralCantUseTip2, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    [YYUtility checkCameraAvailable:available denied:showPrivacyDeniedAlert restriction:showAccessibilityAlert];
}


@end
