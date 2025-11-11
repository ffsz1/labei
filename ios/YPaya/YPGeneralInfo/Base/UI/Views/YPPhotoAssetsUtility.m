//
//  YPPhotoAssetsUtility.m
//  YYMobile
//
//  Created by zhangji on 5/20/16.
//  Copyright © 2016 YY.inc. All rights reserved.
//

#import "YPPhotoAssetsUtility.h"
#import "YYUtility.h"
#import "YPYYAlertView.h"

@implementation YPPhotoAssetsUtility

+ (void)checkPhtotAssetsAvailable:(void(^)(void))available
{
    void(^showPrivacyDeniedAlert)() = ^{
        YPYYAlertView *alertView = [[YPYYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCantUse, nil) message:NSLocalizedString(XCPhotoCantUseTip1, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    void(^showAccessibilityAlert)() = ^{
        YPYYAlertView *alertView = [[YPYYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCantUse, nil) message:NSLocalizedString(XCPhotoCantUseTip2, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    [YYUtility checkAssetsLibrayAvailable:available denied:showPrivacyDeniedAlert restriction:showAccessibilityAlert];
}

@end
