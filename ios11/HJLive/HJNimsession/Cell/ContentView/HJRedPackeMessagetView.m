//
//  HJRedPackeMessagetView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRedPackeMessagetView.h"

@implementation HJRedPackeMessagetView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJRedPackeMessagetView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}


@end
