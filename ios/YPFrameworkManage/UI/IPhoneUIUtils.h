//
//  IPhoneUIUtils.h
//  Commons
//
//  Created by daixiang on 13-3-28.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface IPhoneUIUtils : NSObject

+ (void)showHintViewWithText:(NSString *)text;

+ (void)showLoadingViewWithText:(NSString *)text;

+ (void)showLoadingViewWithText:(NSString *)text dismissBlock:(void (^)(BOOL isAutoDismissed))block;

+ (void)dismissLoadingView;

+ (void)showImageHintViewWithImageName:(NSString *)imageName hintText:(NSString *)text;

@end
