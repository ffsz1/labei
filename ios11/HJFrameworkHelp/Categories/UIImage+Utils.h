//
//  UIImage+Utils.h
//  YYMobileFramework
//
//  Created by wuwei on 14/6/20.
//  Copyright (c) 2014年 YY Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (Utils)

- (UIImage *)grayscaleImage;

- (UIImage *)imageBlendInGray;

- (UIImage *)imageWithBlendMode:(CGBlendMode)blendMode;

+ (UIImage *)imageWithColor:(UIColor *)color;

+ (UIImage *)fixOrientation:(UIImage *)aImage;

- (UIImage *)imageWithColor:(UIColor *)color;
@end
