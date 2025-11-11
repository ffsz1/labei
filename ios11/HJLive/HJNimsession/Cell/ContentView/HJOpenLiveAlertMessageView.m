//
//  HJOpenLiveAlertMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJOpenLiveAlertMessageView.h"

@implementation HJOpenLiveAlertMessageView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle] loadNibNamed:@"HJOpenLiveAlertMessageView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}


@end
