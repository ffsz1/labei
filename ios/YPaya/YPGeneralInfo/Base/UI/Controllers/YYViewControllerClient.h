//
//  YYViewControllerClient.h
//  YYMobile
//
//  Created by wubangmin on 15/11/6.
//  Copyright © 2015年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol YYViewControllerClient <NSObject>

@optional

- (void)viewContrllerWillTrunOrientation:(UIViewController *)viewContrller;

- (void)viewContrller:(UIViewController *)viewContrller orientation:(UIInterfaceOrientation)fromInterfaceOrientation;


@end

