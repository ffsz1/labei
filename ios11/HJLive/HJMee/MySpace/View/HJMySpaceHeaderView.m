//
//  HJMySpaceHeaderView.m
//  HJLive
//
//  Created by feiyin on 2020/5/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMySpaceHeaderView.h"

@interface HJMySpaceHeaderView ()
@property (weak, nonatomic) IBOutlet UIButton *giftBtn;
@property (weak, nonatomic) IBOutlet UIButton *headerWearBtn;
@property (weak, nonatomic) IBOutlet UIButton *carBtn;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_arrow;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_giftBtn;

@end

@implementation HJMySpaceHeaderView

- (void)updateNum:(NSInteger)num
{
    [self.giftBtn setTitle:[NSString stringWithFormat:@"收到的礼物（%ld）",(long)num] forState:UIControlStateNormal];
    
    CGSize size = [self.giftBtn.titleLabel.text boundingRectWithSize:CGSizeMake(0, 22) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.giftBtn.titleLabel.font} context:nil].size;
    
    self.width_giftBtn.constant = size.width + 5;
}

- (void)setSelIndex:(NSInteger)index
{
    if (index == 1) {
        
        
        [self giftBtnAction:nil];
    }
    
    if (index == 2) {
        [self headWearBtnAction:nil];
    }
    
    if (index == 3) {
        [self carBtnAction:nil];
    }
}


- (IBAction)giftBtnAction:(id)sender {
    [self resetBtnStytle];
    
    [self setSelBtn:self.giftBtn];
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.left_arrow.constant = 57;
        [self layoutIfNeeded];
    }];
    
    if (self.clickBlock) {
        self.clickBlock(0);
    }
    
}

- (IBAction)headWearBtnAction:(id)sender {
    [self resetBtnStytle];
    [self setSelBtn:self.headerWearBtn];
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.left_arrow.constant = 30+self.width_giftBtn.constant +5+19 -11;
        [self layoutIfNeeded];
    }];
    if (self.clickBlock) {
        self.clickBlock(1);
    }
}

- (IBAction)carBtnAction:(id)sender {
    [self resetBtnStytle];
    [self setSelBtn:self.carBtn];
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.left_arrow.constant = 30+self.width_giftBtn.constant +5+38+5+19 -11;
        [self layoutIfNeeded];
    }];
    if (self.clickBlock) {
        self.clickBlock(2);
    }
}

- (void)setSelBtn:(UIButton *)btn
{
//    btn.selected = YES;
    UIColor *color = [UIColor colorWithRed:51/255.0 green:51/255.0 blue:51/255.0 alpha:1];

    [btn setTitleColor:color forState:UIControlStateNormal];

    UIFont *font = JXFontPingFangSCMedium(16);

    btn.titleLabel.font = font;
}

- (void)resetBtnStytle
{
    
//    self.giftBtn.selected = NO;
//    self.headerWearBtn.selected = NO;
//    self.carBtn.selected = NO;

    UIColor *color = [UIColor colorWithRed:51/255.0 green:51/255.0 blue:51/255.0 alpha:0.5];

    [self.giftBtn setTitleColor:color forState:UIControlStateNormal];
    [self.headerWearBtn setTitleColor:color forState:UIControlStateNormal];
    [self.carBtn setTitleColor:color forState:UIControlStateNormal];

    UIFont *font = [UIFont fontWithName:@"PingFang SC" size: 14];

    self.giftBtn.titleLabel.font = font;
    self.headerWearBtn.titleLabel.font = font;
    self.carBtn.titleLabel.font = font;

}

@end
