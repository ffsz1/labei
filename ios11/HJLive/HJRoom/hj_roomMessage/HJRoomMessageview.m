//
//  HJRoomMessageview.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomMessageview.h"

@implementation HJRoomMessageview

- (void)awakeFromNib {
    [super awakeFromNib];
//    self.layer.cornerRadius = 8;
//    self.layer.masksToBounds = YES;
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (IBAction)closeBtnAction:(id)sender {
    if (self.closeBtnActionBlock) {
        self.closeBtnActionBlock();
    }
}


@end
