//
//  YPGGFamilyHomeCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGGFamilyHomeCell.h"

@implementation YPGGFamilyHomeCell


- (IBAction)tap1Action:(id)sender {
    
    if (self.tap1) {
        self.tap1();
    }
    
    
}

- (IBAction)tap2Action:(id)sender {
    if (self.tap2) {
        self.tap2();
    }
}

@end
