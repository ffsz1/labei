//
//  HJTurntableMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJTurntableMessageView.h"

@implementation HJTurntableMessageView

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJTurntableMessageView" owner:self options:nil].lastObject;
}

@end
