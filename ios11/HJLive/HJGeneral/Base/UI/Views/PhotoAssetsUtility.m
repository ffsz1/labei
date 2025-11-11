//
//  PhotoAssetsUtility.m
//  YYMobile
//
//  Created by zhangji on 5/20/16.
//  Copyright © 2016 YY.inc. All rights reserved.
//

#import "PhotoAssetsUtility.h"
#import "YYUtility.h"
#import "YYAlertView.h"

@implementation PhotoAssetsUtility

+ (void)checkPhtotAssetsAvailable:(void(^)(void))available
{
    void(^showPrivacyDeniedAlert)() = ^{
        YYAlertView *alertView = [[YYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCantUse, nil) message:NSLocalizedString(XCPhotoCantUseTip1, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    void(^showAccessibilityAlert)() = ^{
        YYAlertView *alertView = [[YYAlertView alloc] initWithTitle:NSLocalizedString(XCPhotoCantUse, nil) message:NSLocalizedString(XCPhotoCantUseTip2, nil)];
        [alertView addButtonWithTitle:NSLocalizedString(XCPhotoUnderstand, nil) block:nil];
        [alertView show];
    };
    
    [YYUtility checkAssetsLibrayAvailable:available denied:showPrivacyDeniedAlert restriction:showAccessibilityAlert];
}

@end
