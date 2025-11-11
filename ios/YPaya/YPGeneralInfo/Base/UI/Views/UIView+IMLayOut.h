//
//  UIView+IMLayOut.h
//  YYMobile
//
//  Created by James Pend on 14-8-20.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (IMLayOut)
- (void)im_pinSubview:(UIView *)subview toEdge:(NSLayoutAttribute)attribute;
- (void)im_pinAllEdgesOfSubview:(UIView *)subview;

@end
