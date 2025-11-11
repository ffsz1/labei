//
//  HJMessageListView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMessageListView.h"


@implementation HJMessageListView

- (void)awakeFromNib {
    [super awakeFromNib];
//    self.layer.cornerRadius = 10;
//    self.layer.masksToBounds = YES;
}


//- (IBAction)closeBtnAction:(id)sender {
//    if (self.closeBtnActionBlock) {
//        self.closeBtnActionBlock();
//    }
//}

- (IBAction)squareAction:(id)sender {
    
    self.squareBtn.selected = YES;
    self.msgBtn.selected = NO;
    self.contentView.hidden = YES;
    
    self.squareIcon.hidden = NO;
    self.msgIcon.hidden = YES;
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
//        self.center_arrow.constant = 60;
        [self layoutIfNeeded];
    }];
    
    
}
- (IBAction)msgAction:(id)sender {
    
    self.squareBtn.selected = NO;
    self.msgBtn.selected = YES;
    self.contentView.hidden = NO;
    
    self.msgIcon.hidden = NO;
    self.squareIcon.hidden = YES;
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
//        self.center_arrow.constant = -60;
        [self layoutIfNeeded];
    }];
}

@end
