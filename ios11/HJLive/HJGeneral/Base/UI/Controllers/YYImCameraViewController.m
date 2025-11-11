//
//  YYImCameraViewController.m
//  YYMobile
//
//  Created by yangmengjun on 15/4/9.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YYImCameraViewController.h"

@implementation YYImCameraViewController

- (void)currentContextPresentingNeedDismissing:(BOOL)animated
{
    [self dismissViewControllerAnimated:animated completion:NULL];
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

@end
