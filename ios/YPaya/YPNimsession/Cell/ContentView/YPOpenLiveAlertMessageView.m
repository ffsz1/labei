//
//  YPOpenLiveAlertMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPOpenLiveAlertMessageView.h"

@implementation YPOpenLiveAlertMessageView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle] loadNibNamed:@"YPOpenLiveAlertMessageView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib{
    [super awakeFromNib];
    self.userInteractionEnabled = NO;
}


@end
