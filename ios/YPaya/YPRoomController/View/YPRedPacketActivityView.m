//
//  YPRedPacketActivityView.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRedPacketActivityView.h"

@implementation YPRedPacketActivityView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPRedPacketActivityView" owner:self options:nil].lastObject;
}

@end
