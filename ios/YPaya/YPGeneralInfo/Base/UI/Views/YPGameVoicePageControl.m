//
//  YPGameVoicePageControl.m
//  YYMobile
//
//  Created by zill on 16/3/24.
//  Copyright © 2016年 YY.inc. All rights reserved.
//

#import "YPGameVoicePageControl.h"

@interface YPGameVoicePageControl()
{
    UIColor *_activeColor;
    UIColor *_inactiveColor;
}
@end

@implementation YPGameVoicePageControl

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    _activeColor = [UIColor colorWithRed:63/255.0 green:151/255.0 blue:240/255.0 alpha:1];
    _inactiveColor = [UIColor colorWithRed:63/255.0 green:151/255.0 blue:240/255.0 alpha:0.2];
    return self;
}

- (void)updateDots
{
    for (int i = 0; i< [self.subviews count]; i++) {
        UIImageView* dot = [self.subviews objectAtIndex:i];
        
        if (i == self.currentPage){
            dot.backgroundColor = _activeColor;
        }
        else
            dot.backgroundColor = _inactiveColor;
    }
}

- (void)setCurrentPage:(NSInteger)currentPage
{
    [super setCurrentPage:currentPage];
    [self updateDots];
}
@end
