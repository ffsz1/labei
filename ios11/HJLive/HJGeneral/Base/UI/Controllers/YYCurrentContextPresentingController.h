//
//  YYCurrentContextPresentingController.h
//  YYMobile
//
//  Created by jianglinjie on 14-7-30.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YYCurrentContextPresentingControllerDelegate.h"


@interface YYCurrentContextPresentingController : NSObject
+ (instancetype)shareController;

@property (weak, nonatomic) id <YYCurrentContextPresentingControllerDelegate> currentContextPresentedViewController; //当前被上下文方式 present处理的viewController。

- (void)currentContextPresentWithSourceViewController:(UIViewController *)sourceViewController destinationViewController:(UIViewController *)destinationViewController
    animated:(BOOL) animated
    completion:(dispatch_block_t)completion;
- (void)dismissCurrentContextViewController:(BOOL)animated;
@end
