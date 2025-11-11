//
//  YPPlacardView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPlacardView.h"

@implementation YPPlacardView

- (IBAction)btnClick:(UIButton *)sender {
    if (self.closeClick) {
        self.closeClick();
    }
}

@end
