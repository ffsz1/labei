//
//  YY2ColoredButton.m
//  YY2
//
//  Created by wuwei on 13-10-28.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import "YPYYColoredButton.h"
#import "UIColor+LightAndDark.h"

@implementation YPYYColoredButton
{
    UIColor *_buttonBackgroundColor;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
//    if (self) {
        // Initialization code
//    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void)willMoveToSuperview:(UIView *)newSuperview
{
    [super willMoveToSuperview:newSuperview];
    
    self.layer.cornerRadius = 3.0;
    self.layer.masksToBounds = YES;
}

- (void)setBackgroundColor:(UIColor *)backgroundColor
{
    _buttonBackgroundColor = backgroundColor;
    [super setBackgroundColor:backgroundColor];
}

- (void)setHighlighted:(BOOL)highlighted
{
    if (highlighted)
    {
        self.layer.backgroundColor = [_buttonBackgroundColor darkerColor].CGColor;
    }
    else
    {
        self.layer.backgroundColor = _buttonBackgroundColor.CGColor;
    }
}

@end
