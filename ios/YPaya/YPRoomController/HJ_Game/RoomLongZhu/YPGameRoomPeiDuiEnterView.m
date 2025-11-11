//
//  YPGameRoomPeiDuiEnterView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomPeiDuiEnterView.h"

#import "HJLongZhuCoreClient.h"

@interface YPGameRoomPeiDuiEnterView ()<HJLongZhuCoreClient>

@property (weak, nonatomic) IBOutlet UIButton *supeiBtn;
@property (weak, nonatomic) IBOutlet UIButton *chooseBtn;


@end

@implementation YPGameRoomPeiDuiEnterView

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    AddCoreClient(HJLongZhuCoreClient, self);
    
    self.supeiBtn.hidden = YES;
    self.chooseBtn.hidden = YES;
    
}

- (void)close {
    
    self.supeiOpenBtn.selected = NO;
    
    if (!self.supeiBtn.hidden) {

        @weakify(self);
        [UIView animateWithDuration:0.3 animations:^{
            @strongify(self);
            [self layoutIfNeeded];
        } completion:^(BOOL finished) {
            @strongify(self);
            self.supeiBtn.hidden = YES;
            self.chooseBtn.hidden = YES;
        }];
        if (self.peiduiBtnActionBlock) {
            self.peiduiBtnActionBlock(NO);
        }
    }
}

- (IBAction)peiduibtnAction:(id)sender {
    
//    UIButton *btn = sender;
//    btn.selected = !btn.selected;
    
    if (!self.didChoose) {
        
        if (self.supeiBtn.hidden) {
            self.supeiBtn.hidden = NO;
            self.chooseBtn.hidden = NO;
            self.supeiOpenBtn.selected = YES;
            
            self.right_red.constant = 25;
            self.ringht_yellow.constant = 25;
            @weakify(self);
            
            [UIView animateWithDuration:0.3 animations:^{
                @strongify(self);
                                 self.right_red.constant = 75;//25
                                 self.ringht_yellow.constant = 117;//25
                                 
                                 [self layoutIfNeeded];
                                 
                             } completion:nil];
            
        }
        else {
            
            self.right_red.constant = 75;//25
            self.ringht_yellow.constant = 117;//25
            self.supeiOpenBtn.selected = NO;

            @weakify(self);
            [UIView animateWithDuration:0.3 animations:^{
                @strongify(self);
                
                self.right_red.constant = 25;
                self.ringht_yellow.constant = 25;
                
                [self layoutIfNeeded];
            } completion:^(BOOL finished) {
                @strongify(self);
                self.supeiBtn.hidden = YES;
                self.chooseBtn.hidden = YES;
            }];
        }
        if (self.peiduiBtnActionBlock) {
            self.peiduiBtnActionBlock(!self.supeiBtn.hidden);
        }
    }
    else {
        
        if (!self.supeiBtn.hidden) {
            
            self.right_red.constant = 75;//25
            self.ringht_yellow.constant = 117;//25
            self.supeiOpenBtn.selected = NO;

            @weakify(self);
            [UIView animateWithDuration:0.3 animations:^{
                @strongify(self);
                
                self.right_red.constant = 25;
                self.ringht_yellow.constant = 25;
                
                [self layoutIfNeeded];
            } completion:^(BOOL finished) {
                @strongify(self);
                self.supeiBtn.hidden = YES;
                self.chooseBtn.hidden = YES;
            }];
        }
        
        if (self.peiduiBtnActionBlock) {
            self.peiduiBtnActionBlock(NO);
        }
    }
    
    
    
}

- (IBAction)supeiBtnAction:(id)sender {
    
    if (self.supeiBtnActionBlock) {
        self.supeiBtnActionBlock();
    }
}

- (IBAction)chooseBtnAction:(id)sender {
    
    if (self.chooseBtnActionBlock) {
        self.chooseBtnActionBlock();
    }
}

#pragma mark - LongZhuCoreClient
- (void)getStateSuccessWithResult:(NSDictionary *)result; {
    
    NSInteger status = [result[@"status"] integerValue];
    
    if (status == 0) {
        self.didChoose = NO;
    }
    else {
        self.didChoose = YES;
    }
}


@end
