//
//  YPYYNavigationBar.h
//  YYMobile
//
//  Created by wuwei on 14/7/9.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YYNavigationBarAppearance : NSObject

- (instancetype)initWithContext:(id)context;

@property (nonatomic, assign, readonly) void *context;

@property (nonatomic, assign) BOOL hidden;
@property (nonatomic, assign) BOOL translucent;
@property (nonatomic, assign) UIBarStyle barStyle;
@property (nonatomic, strong) UIColor *tintColor;
@property (nonatomic, strong) UIColor *barTintColor;
//@property (nonatomic, strong) UIBarButtonItem *backBarButtonItem;
@property (nonatomic, strong) UIImage *backgroundImageDefault;
@property (nonatomic, strong) UIImage *backgroundImageLandscapePhone;
@property (nonatomic, strong) NSDictionary *titleTextAttributes;
//@property (nonatomic, assign) BOOL tran

@end

@interface YPYYNavigationBar : UINavigationBar

@end
