//
//  GGLabel.m
//  HJLive
//
//  Created by FF on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "GGLabel.h"

@implementation GGLabel

- (void)setConnerRadius:(CGFloat)connerRadius {
    _connerRadius = connerRadius;
    self.layer.cornerRadius = _connerRadius;
    self.layer.masksToBounds = YES;
}
- (void)setBorderWidth:(CGFloat)borderWidth {
    _borderWidth = borderWidth;
    self.layer.borderWidth = _borderWidth;
}
- (void)setBorderColor:(UIColor *)borderColor {
    _borderColor = borderColor;
    self.layer.borderColor = _borderColor.CGColor;
}

- (void)setMasksToBounds:(BOOL)masksToBounds
{
    _masksToBounds = masksToBounds;
    self.layer.masksToBounds = _masksToBounds;
    
}

@end
