//
//  YPRedPackeMessagetView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRedPackeMessagetView.h"

@implementation YPRedPackeMessagetView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPRedPackeMessagetView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}


@end
