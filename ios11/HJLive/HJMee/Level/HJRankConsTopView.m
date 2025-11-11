//
//  HJRankConsTopView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRankConsTopView.h"

@implementation HJRankConsTopView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (IBAction)goBack:(UIButton *)sender {
    if (self.goBackBlock) {
        self.goBackBlock();
    }
}

- (IBAction)tabClick:(UIButton *)sender {
    if ([sender isEqual:self.caifuBtn]) {
        self.caifuBtn.alpha = 1;
        self.meiliBtn.alpha = 0.5;
    } else {
        self.caifuBtn.alpha = 0.5;
        self.meiliBtn.alpha = 1;
    }
    
    [UIView animateWithDuration:0.3 animations:^{
        self.line.centerX = sender.centerX;
    }];
    
    if (self.sendBlock) {
        self.sendBlock(sender.tag);
    }
}

@end
