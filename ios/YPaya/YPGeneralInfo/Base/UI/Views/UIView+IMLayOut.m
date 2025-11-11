//
//  UIView+IMLayOut.m
//  YYMobile
//
//  Created by James Pend on 14-8-20.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "UIView+IMLayOut.h"

@implementation UIView (IMLayOut)

- (void)im_pinSubview:(UIView *)subview toEdge:(NSLayoutAttribute)attribute
{
    [self addConstraint:[NSLayoutConstraint constraintWithItem:self
                                                     attribute:attribute
                                                     relatedBy:NSLayoutRelationEqual
                                                        toItem:subview
                                                     attribute:attribute
                                                    multiplier:1.0f
                                                      constant:0.0f]];

}


- (void)im_pinAllEdgesOfSubview:(UIView *)subview
{
    [self im_pinSubview:subview toEdge:NSLayoutAttributeBottom];
    [self im_pinSubview:subview toEdge:NSLayoutAttributeTop];
    [self im_pinSubview:subview toEdge:NSLayoutAttributeLeading];
    [self im_pinSubview:subview toEdge:NSLayoutAttributeTrailing];
}


@end
