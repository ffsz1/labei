//
//  YYNavigationBar.m
//  YYMobile
//
//  Created by wuwei on 14/7/9.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYNavigationBar.h"

@interface YYNavigationBarAppearance ()

@end

@interface YYNavigationBar ()
{
    UIView *_navigationBackgroundView;
}

@end

@implementation YYNavigationBar

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
//    if (self) {
        // Initialization code
//    }
    return self;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    _navigationBackgroundView = self.subviews[0];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end

@implementation YYNavigationBarAppearance

- (instancetype)initWithContext:(id)context
{
    self = [super init];
    if (self) {
        _context = (__bridge void *)context;
    }
    return self;
}

@end
