//
//  YYDefaultTheme.m
//  YYMobile
//
//  Created by wuwei on 14/6/26.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYDefaultTheme.h"

@implementation YYDefaultTheme

+ (NSDictionary *) colorMap
{
    static NSDictionary* colorMap = nil;
    static dispatch_once_t token;
    dispatch_once(&token,^{
        colorMap = @{
                     COLOR_1 : RGBCOLOR(55, 160, 241),          // 主色调
                     COLOR_2 : RGBCOLOR(241, 241, 241),         // 灰色
                     COLOR_3 : RGBCOLOR(255, 172, 50),
                     COLOR_4 : RGBCOLOR(255, 84, 0),
                     COLOR_5 : RGBCOLOR(78, 180, 7),
                     COLOR_6 : RGBCOLOR(255, 241, 226),
                     COLOR_7 : RGBCOLOR(36, 36, 36),
                     COLOR_8 : RGBCOLOR(63, 63, 63),
                     COLOR_9 : RGBCOLOR(153, 153, 153),
                     COLOR_10: RGBCOLOR(224, 224, 224),
                     COLOR_11: RGBCOLOR(255, 255, 255),
                     COLOR_12: RGBCOLOR(185, 185, 185),
                     COLOR_13: RGBCOLOR(0, 179, 254),
                     COLOR_14: RGBCOLOR(0, 0, 0),
                     COLOR_15: RGBCOLOR(0, 121, 251),//键盘return
                     COLOR_16: RGBCOLOR(0xFE, 0xEA, 0xD2),
                     COLOR_17: RGBCOLOR(255, 255, 0),
                     COLOR_18: RGBCOLOR(153, 153, 153),
                     COLOR_19: RGBCOLOR(0xFF, 0x72, 0x35),
                     COLOR_20: RGBCOLOR(18, 142, 238),// color_1兼容顶部毛玻璃后的颜色
                     COLOR_21: RGBCOLOR(255, 255, 255)
                     };
    });
    return colorMap;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.globalTintColor = self.tabBarTintColor = [self getColorWithColorString:COLOR_21];
        self.navigationBarTintColor = self.navigationBarTitleTextColor = [self getColorWithColorString:COLOR_18];
        self.navigationBarBarTintColor = [self getColorWithColorString:COLOR_21];
        self.viewControllerBackgroundColor = [UIColor whiteColor];
    }
    return self;
}

- (UIColor*) getColorWithColorString:(NSString*)colorString;
{
    NSDictionary * colorMap = [YYDefaultTheme colorMap];
    return [colorMap objectForKey:colorString];
}

@end
