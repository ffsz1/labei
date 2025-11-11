//
//  YPYYTheme.h
//  YYMobile
//
//  Created by wuwei on 14/6/26.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPYYColorString.h"

@interface YPYYTheme : NSObject

+ (instancetype)defaultTheme;

- (void)apply;

// Global
@property (nonatomic, strong) UIColor *globalTintColor;

// Navigation Bar
@property (nonatomic, strong) UIColor *navigationBarBarTintColor;
@property (nonatomic, strong) UIColor *navigationBarTintColor;

@property (nonatomic, strong) UIColor *navigationBarTitleTextColor;
@property (nonatomic, strong) UIFont *navigationBarTitleTextFont;

// TabBar
@property (nonatomic, strong) UIColor *tabBarBarTintColor;
@property (nonatomic, strong) UIColor *tabBarTintColor;

// Common View Controller background
@property (nonatomic, strong) UIColor *viewControllerBackgroundColor;

- (UIColor*) getColorWithColorString:(NSString*)colorString;

//example:   colorWithHexString:@"#0e0e0e"
- (UIColor *)colorWithHexString:(NSString *)colorString alpha:(CGFloat)alpha;
@end
