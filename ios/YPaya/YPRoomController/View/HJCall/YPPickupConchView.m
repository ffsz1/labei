//
//  YPPickupConchView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPickupConchView.h"

@implementation YPPickupConchView

- (void)awakeFromNib
{
    [super awakeFromNib];
    [self setBgRaduis];

     
 
}
+ (void)showCall:(TapConchTypeBlock)tapConchTypeBlock{
    
     YPPickupConchView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPPickupConchView" owner:self options:nil][0];
        shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
        
        [[UIApplication sharedApplication].keyWindow addSubview:shareView];
        
        
        
    //    BOOL isRoomOwner = GetCore(YPRoomQueueCoreV2Help).myMember.is_creator;
    //    BOOL isManager =  GetCore(YPRoomQueueCoreV2Help).myMember.is_manager;
    //
    //    if (isManager) {
    //        shareView.left_managerBtn.constant = -kScreenWidth/4;
    //        shareView.managerBtn.hidden = YES;
    //    }
        
    //    shareView.openMsgBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.publicChatSwitch;
    //    shareView.openCarBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.giftCardSwitch;
    //    shareView.openGiftBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.giftEffectSwitch;
        
//        shareView.menuBlock = menuBlock;
       
       shareView.bottom_view_layout.constant = -236;
        
        [shareView layoutIfNeeded];
        
        [UIView animateWithDuration:0.7 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
            
            shareView.bottom_view_layout.constant = 0;
            
            [shareView layoutIfNeeded];
            
        } completion:^(BOOL finished) {
            
        }];
    
    
    
    
    
    
    
}
- (void)setBgRaduis
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 236);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.conchView.layer.mask = maskLayer;
 
}

- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.3 animations:^{
        
      
        self.bottom_view_layout.constant = -236;
        
       
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}

- (IBAction)tapUpviewGestures:(id)sender {
     [self close];
}

- (IBAction)conchCloseBtn:(id)sender {
     [self close];
}

- (IBAction)pickBigBtnAction:(id)sender {
    
}

- (IBAction)pickMiddelBtnAction:(id)sender {
}

- (IBAction)pickSmallBtnAction:(id)sender {
}

- (IBAction)getMoreAction:(id)sender {
}

- (IBAction)rankAction:(id)sender {
}
- (IBAction)giftAction:(id)sender {
}

- (IBAction)recordAction:(id)sender {
}

- (IBAction)rulesAction:(id)sender {
}




@end
