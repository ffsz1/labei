//
//  YYLeagueManagerHeader.m
//  YYMobile
//
//  Created by Liuyuxiang on 16/5/10.
//  Copyright © 2016年 YY.inc. All rights reserved.
//

#import "YYSectionTitleViewHeader.h"

@implementation YYSectionTitleViewHeader

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
+ (instancetype)loadFromXib
{
    return [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([YYSectionTitleViewHeader class]) owner:nil options:nil] lastObject];
}
@end
