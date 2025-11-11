//
//  YPYYLoadingToastView.h
//  YYMobile
//
//  Created by 武帮民 on 14-8-13.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPYYLoadingToastView : UIView

+ (instancetype)instantiateLoadingToast;

+ (instancetype)instantiateLoadingToastWithText:(NSString *)msg;

@end
