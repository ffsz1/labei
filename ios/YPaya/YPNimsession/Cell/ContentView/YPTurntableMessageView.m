//
//  YPTurntableMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPTurntableMessageView.h"

@implementation YPTurntableMessageView

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPTurntableMessageView" owner:self options:nil].lastObject;
}

@end
