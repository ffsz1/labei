//
//  YYTheme.m
//  YYMobile
//
//  Created by wuwei on 14/6/26.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYTheme.h"

#import "YYNavigationController.h"
#import "YYDefaultTheme.h"
#import "UIImage+_1x1Color.h"

@implementation YYTheme

+ (instancetype)defaultTheme
{
    static id defaultTheme;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        defaultTheme = [[YYDefaultTheme alloc] init];
    });
    return defaultTheme;
}

- (void)apply
{
    [self _applyGlobalConfiguration];
    [self _applyNavigationConfiguration];
    [self _applyTabBarConfiguration];
    [self _applyGlobalStatusStyle];
    [[UIApplication sharedApplication].keyWindow.rootViewController setNeedsStatusBarAppearanceUpdate];
}

- (void)_applyGlobalConfiguration
{
    [UIApplication sharedApplication].keyWindow.tintColor = self.globalTintColor;
}

- (void)_applyGlobalStatusStyle {
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
}

- (void)_applyNavigationConfiguration
{
    [[UINavigationBar appearance] setBarTintColor:self.navigationBarBarTintColor];
    [[UINavigationBar appearance] setTintColor:self.navigationBarTintColor];

    NSDictionary *textAttributes = @{NSForegroundColorAttributeName:
                                         self.navigationBarTitleTextColor ? : [UIColor blackColor],
                                     NSFontAttributeName:
                                         self.navigationBarTitleTextFont ? : [UIFont boldSystemFontOfSize:18]};
    [[UINavigationBar appearance] setTitleTextAttributes:textAttributes];
    
    UIImage *backIndicator = [UIImage imageNamed:@"hj_sex_female_logo"];
    [[UINavigationBar appearanceWhenContainedIn:[YYNavigationController class], nil] setBackIndicatorImage:backIndicator];
    [[UINavigationBar appearanceWhenContainedIn:[YYNavigationController class], nil] setBackIndicatorTransitionMaskImage:backIndicator];
}

- (void)_applyTabBarConfiguration
{
    [[UITabBar appearance] setBarTintColor:self.tabBarBarTintColor];
    [[UITabBar appearance] setTintColor:self.tabBarTintColor];
}

- (UIColor*) getColorWithColorString:(NSString*)colorString
{
    return [UIColor whiteColor];
}


#define R_VALUE(argb) ((argb >> 16) & 0x000000FF)
#define G_VALUE(argb) ((argb >> 8) & 0x000000FF)
#define B_VALUE(argb) (argb & 0x000000FF)

- (UIColor *)colorWithHexString:(NSString *)colorString alpha:(CGFloat)alpha
{
    const char *cStr = [colorString cStringUsingEncoding:NSASCIIStringEncoding];
    long x = strtol(cStr + 1, NULL, 16);
    
    UIColor *color =  [UIColor colorWithRed:(float)R_VALUE(x) / 255.0f green:(float)G_VALUE(x) / 255.0f blue:(float)B_VALUE(x) / 255.0f alpha:alpha];
    
    return color;
}

@end
