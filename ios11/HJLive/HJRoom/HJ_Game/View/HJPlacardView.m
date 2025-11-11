//
//  HJPlacardView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPlacardView.h"

@implementation HJPlacardView

- (IBAction)btnClick:(UIButton *)sender {
    if (self.closeClick) {
        self.closeClick();
    }
}

@end
