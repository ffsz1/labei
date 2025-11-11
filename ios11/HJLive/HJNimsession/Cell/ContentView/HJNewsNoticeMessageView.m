//
//  HJNewsNoticeMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNewsNoticeMessageView.h"

@implementation HJNewsNoticeMessageView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJNewsNoticeMessageView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}


@end
