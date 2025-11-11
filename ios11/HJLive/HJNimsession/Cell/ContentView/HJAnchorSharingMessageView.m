//
//  HJAnchorSharingMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAnchorSharingMessageView.h"

@implementation HJAnchorSharingMessageView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)awakeFromNib {
    [super awakeFromNib];
    self.enterRoomBtn.layer.borderColor = UIColorHex(0BCDA8).CGColor;
    self.enterRoomBtn.layer.borderWidth = 0.5;
    self.bgView.layer.cornerRadius = 10;
    self.bgView.layer.masksToBounds = YES;
    self.viEffect.layer.cornerRadius = 10;
    self.viEffect.layer.masksToBounds = YES;
    
}

@end
