//
//  HJReportItemView.m
//  HJReportItemView
//
//  Created by apple on 2019/6/26.
//  Copyright © 2019 apple. All rights reserved.
//

#import "HJReportItemView.h"

#import "HJUserHandler.h"


@interface HJReportItemView ()

@property (weak, nonatomic) IBOutlet UIButton *moreBtn;
@property (weak, nonatomic) IBOutlet UIButton *reportBtn;
@property (weak, nonatomic) IBOutlet UIButton *blackBtn;
@property (weak, nonatomic) IBOutlet UIImageView *lineImageView1;
@property (weak, nonatomic) IBOutlet UIImageView *lineImageView2;

@end

@implementation HJReportItemView

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self resetStatus:NO];
    
}

- (void)show
{
    [self resetStatus:YES];
    
    self.reportBtn.alpha = 0;
    self.blackBtn.alpha = 0;
    self.lineImageView1.alpha = 0;
    self.lineImageView2.alpha = 0;
    
    
    [self mas_updateConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(28);
    }];
    
    self.userInteractionEnabled = NO;
    

    [self.superview layoutIfNeeded];
    
    [UIView animateWithDuration:HJReportItemViewAnimationTime animations:^{
        
        self.reportBtn.alpha = 1;
        self.blackBtn.alpha = 1;
        self.lineImageView1.alpha = 1;
        self.lineImageView2.alpha = 1;
        
        [self mas_updateConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(28*3);
        }];
        [self.superview layoutIfNeeded];


    } completion:^(BOOL finished) {
        self.userInteractionEnabled = YES;
    }];
}

- (void)close
{
    
    [self resetStatus:NO];

    self.userInteractionEnabled = NO;
    
    [self.superview layoutIfNeeded];
    
    [UIView animateWithDuration:HJReportItemViewAnimationTime animations:^{
        
        [self mas_updateConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(28);
        }];
        [self.superview layoutIfNeeded];

        
    } completion:^(BOOL finished) {
        self.userInteractionEnabled = YES;
    }];
}

- (void)resetStatus:(BOOL)isShow
{
    self.moreBtn.selected = isShow;
    self.reportBtn.hidden = !isShow;
    self.blackBtn.hidden = !isShow;
    self.lineImageView1.hidden = !isShow;
    self.lineImageView2.hidden = !isShow;
    
}

- (IBAction)moreAction:(id)sender {
    
    if (self.moreBtn.selected) {
        [self close];
    }else{
        [self show];
        
    }
    
}

- (IBAction)reportAction:(id)sender {
    
    
    __weak typeof(self)weakSelf = self;
    [HJUserHandler showReport:self.uid cancelFollowBlock:^{
        
        if (weakSelf.roomOwnerFollowBlock) {
            weakSelf.roomOwnerFollowBlock(NO);
        }
        
        if (weakSelf.spaceCardViewCancelBlock) {
            weakSelf.spaceCardViewCancelBlock(NO);
        }
        
    }];
    
}

- (IBAction)blackListAction:(id)sender {
    __weak typeof(self)weakSelf = self;

    [HJUserHandler showAddBlackList:self.uid cancelFollowBlock:^{
        if (weakSelf.roomOwnerFollowBlock) {
            weakSelf.roomOwnerFollowBlock(NO);
        }
        
        if (weakSelf.spaceCardViewCancelBlock) {
            weakSelf.spaceCardViewCancelBlock(NO);
        }
    }];
}

@end
