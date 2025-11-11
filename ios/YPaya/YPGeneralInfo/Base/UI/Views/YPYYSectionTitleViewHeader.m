//
//  YYLeagueManagerHeader.m
//  YYMobile
//
//  Created by Liuyuxiang on 16/5/10.
//  Copyright © 2016年 YY.inc. All rights reserved.
//

#import "YPYYSectionTitleViewHeader.h"

@implementation YPYYSectionTitleViewHeader

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
+ (instancetype)loadFromXib
{
    return [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([YPYYSectionTitleViewHeader class]) owner:nil options:nil] lastObject];
}
@end
