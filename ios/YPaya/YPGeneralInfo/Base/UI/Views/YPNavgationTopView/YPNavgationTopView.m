//
//  YPNavgationTopView.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNavgationTopView.h"

@implementation YPNavgationTopView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (IBAction)goBackClick:(UIButton *)sender {
    if (self.goBackBlock) {
        self.goBackBlock();
    }
}

- (IBAction)rightClick:(UIButton *)sender {
    if (self.rightClickBlock) {
        self.rightClickBlock();
    }
}
@end
