//
//  HJAdMusicView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAdMusicView.h"

@implementation HJAdMusicView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (IBAction)BtnClick:(UIButton *)sender {
    if (self.senderBlock) {
        self.senderBlock();
    }
}

@end
