//
//  HJRoomMenuView.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomMenuView.h"

#import "HJverticalButton.h"

#import "HJRoomQueueCoreV2Help.h"
#import "HJImRoomCoreV2.h"

@interface HJRoomMenuView ()
@property (weak, nonatomic) IBOutlet UIView *menuView;
@property (weak, nonatomic) IBOutlet HJverticalButton *openMsgBtn;
@property (weak, nonatomic) IBOutlet HJverticalButton *openCarBtn;
@property (weak, nonatomic) IBOutlet HJverticalButton *openGiftBtn;
//@property (weak, nonatomic) IBOutlet HJverticalButton *managerBtn;

@property (weak, nonatomic) IBOutlet HJverticalButton *userMusicBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *userMusic_leftPad;

@property (weak, nonatomic) IBOutlet HJverticalButton *managerMisicBtn;

@property (weak, nonatomic) IBOutlet UIView *visitorMenuView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_managerBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_managerView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_visitor;

@property (copy, nonatomic) RoomMenuBlock menuBlock;

@end

@implementation HJRoomMenuView

+ (void)show:(RoomMenuBlock)menuBlock ordinaryUserIsOnMic:(BOOL)IsOnMic
{
    HJRoomMenuView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJRoomMenuView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    
    
    BOOL isRoomOwner = GetCore(HJRoomQueueCoreV2Help).myMember.is_creator;
    BOOL isManager =  GetCore(HJRoomQueueCoreV2Help).myMember.is_manager;
    
//    if (isManager) {
//        shareView.left_managerBtn.constant = -kScreenWidth/4;
//        shareView.managerBtn.hidden = YES;
//    }
    
    shareView.openMsgBtn.selected = GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch;
    shareView.openCarBtn.selected = GetCore(HJImRoomCoreV2).currentRoomInfo.giftCardSwitch;
    shareView.openGiftBtn.selected = GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch;
    
    shareView.menuBlock = menuBlock;
    shareView.bottom_managerView.constant = -224;
    shareView.bottom_visitor.constant = -112;
    
    [shareView layoutIfNeeded];
    
    
    if (isRoomOwner || isManager) {
    }else{
        if (!IsOnMic) {
            shareView.userMusicBtn.hidden = YES;
            shareView.userMusic_leftPad.constant = -kScreenWidth/3;
        }
    }
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        
        if (isRoomOwner || isManager) {
            shareView.bottom_managerView.constant = 0;
        }else{
            shareView.bottom_visitor.constant = 0;
        }
        
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBgRaduis];
    
}

- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.2 animations:^{
        
        self.bottom_managerView.constant = -224;
        self.bottom_visitor.constant = -112;
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}

- (void)setBgRaduis
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 224);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.menuView.layer.mask = maskLayer;

    
    CGRect frame2 = CGRectMake(0, 0, kScreenWidth, 112);
    UIBezierPath *maskPath2 = [UIBezierPath bezierPathWithRoundedRect:frame2 byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];

    CAShapeLayer * maskLayer2 = [[CAShapeLayer alloc]init];
    maskLayer2.frame = frame2;
    maskLayer2.path = maskPath2.CGPath;
    self.visitorMenuView.layer.mask = maskLayer2;
    
}

- (IBAction)tapAction:(id)sender {
    [self close];
}

- (IBAction)roomSetAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeSet);
    [self close];
}

- (IBAction)openMsgAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeOpenChat);
    [self close];
}

- (IBAction)openCarAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeOpenCar);
    [self close];
}

- (IBAction)openGiftAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeOpenGift);
    [self close];

}

- (IBAction)roomTipAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeTip);
    [self close];

}

//- (IBAction)managerAction:(id)sender {
//    self.menuBlock(XBDRoomMenuTypeManager);
//    [self close];
//
//}
- (IBAction)musicAction:(id)sender
{
    self.menuBlock(XBDRoomMenuTypeMusic);
    [self close];
}

- (IBAction)minAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeMin);
    [self close];

}

- (IBAction)outAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeOut);
    [self close];

}
- (IBAction)reportAction:(id)sender {
    
    self.menuBlock(XBDRoomMenuTypeReport);
    [self close];
    
}
@end
