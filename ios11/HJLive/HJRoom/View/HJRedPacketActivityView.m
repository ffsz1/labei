//
//  HJRedPacketActivityView.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRedPacketActivityView.h"

@implementation HJRedPacketActivityView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJRedPacketActivityView" owner:self options:nil].lastObject;
}

@end
