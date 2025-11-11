//
//  YPYYEmptyContentToastView.h
//  YYMobile
//
//  Created by wuwei on 14/7/30.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPYYEmptyContentToastView : UIView

@property (nonatomic, weak, readonly) UILabel *titleLabel;
@property (nonatomic, weak, readonly) UIImageView *imageView;

+ (instancetype)instantiateEmptyContentToast;
+ (instancetype)instantiateNetworkErrorToast;
+ (instancetype)instantiateEmptyContentToastWithImage:(UIImage*)image;

@end
