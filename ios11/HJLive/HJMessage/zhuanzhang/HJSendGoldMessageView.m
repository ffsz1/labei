//
//  HJSendGoldMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/7/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSendGoldMessageView.h"

@implementation HJSendGoldMessageView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJSendGoldMessageView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}


@end
