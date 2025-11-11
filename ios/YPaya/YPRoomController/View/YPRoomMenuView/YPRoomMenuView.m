//
//  YPRoomMenuView.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomMenuView.h"

#import "YPverticalButton.h"

#import "YPRoomQueueCoreV2Help.h"
#import "YPImRoomCoreV2.h"

@interface YPRoomMenuView ()

@property (weak, nonatomic) IBOutlet UIView *upview;

@property (weak, nonatomic) IBOutlet UIView *menuView;
@property (weak, nonatomic) IBOutlet YPverticalButton *openMsgBtn;
@property (weak, nonatomic) IBOutlet YPverticalButton *openCarBtn;
@property (weak, nonatomic) IBOutlet YPverticalButton *openGiftBtn;
@property (weak, nonatomic) IBOutlet YPverticalButton *managerBtn;
@property (weak, nonatomic) IBOutlet UIView *visitorMenuView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_managerBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_managerView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_visitor;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *upview_top_layout;
@property (copy, nonatomic) RoomMenuBlock menuBlock;

@property (weak, nonatomic) IBOutlet YPverticalButton *fangjiansettingBtn;

@property (weak, nonatomic) IBOutlet YPverticalButton *shareBtn;

@property (weak, nonatomic) IBOutlet YPverticalButton *jubaoBtn;

@property (weak, nonatomic) IBOutlet YPverticalButton *tuifangBtn;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *upview_left_layout;

@end

@implementation YPRoomMenuView

+ (void)show:(RoomMenuBlock)menuBlock
{
    YPRoomMenuView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPRoomMenuView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    
    
    BOOL isRoomOwner = GetCore(YPRoomQueueCoreV2Help).myMember.is_creator;
    BOOL isManager =  GetCore(YPRoomQueueCoreV2Help).myMember.is_manager;
    
    if (isManager) {
        shareView.left_managerBtn.constant = -kScreenWidth/4;
        shareView.managerBtn.hidden = YES;
    }
    
    shareView.openMsgBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.publicChatSwitch;
    shareView.openCarBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.giftCardSwitch;
    shareView.openGiftBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.giftEffectSwitch;
    
    shareView.menuBlock = menuBlock;
    shareView.bottom_managerView.constant = -224;
    shareView.bottom_visitor.constant = -112;
    shareView.upview_top_layout.constant = -200;
    [shareView layoutIfNeeded];
    
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
        self.upview_top_layout.constant = -180;
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
    self.menuBlock(XBDRoomMenuTypeMusic);
    [self close];
}

- (IBAction)openCarAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeOpenChat);
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

- (IBAction)managerAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeManager);
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

//房间设置
- (IBAction)fangjiansettingBtnAction:(id)sender {
    
    self.menuBlock(XBDRoomMenuTypeFangjianCome);
    [self close];
}

- (IBAction)shareBtnAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeShare);
    [self close];
}

- (IBAction)jubaoBtnAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeReport);
    [self close];
}

- (IBAction)tuifangBtnAction:(id)sender {
    self.menuBlock(XBDRoomMenuTypeOut);
    [self close];
}







+ (void)showUpViewForRoom:(RoomMenuBlock)menuBlock
{
    YPRoomMenuView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPRoomMenuView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    
    
    BOOL isRoomOwner = GetCore(YPRoomQueueCoreV2Help).myMember.is_creator;
    BOOL isManager =  GetCore(YPRoomQueueCoreV2Help).myMember.is_manager;
    
    if (isManager) {
        shareView.left_managerBtn.constant = -kScreenWidth/4;
        shareView.managerBtn.hidden = YES;
    }
    if (isRoomOwner || isManager) {
    }else{
        shareView.upview_left_layout.constant = -170;
    }
    
    [shareView.fangjiansettingBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [shareView.shareBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [shareView.jubaoBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [shareView.tuifangBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
//    shareView.openMsgBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.publicChatSwitch;
//    shareView.openCarBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.giftCardSwitch;
//    shareView.openGiftBtn.selected = GetCore(YPImRoomCoreV2).currentRoomInfo.giftEffectSwitch;
    
    shareView.menuBlock = menuBlock;
    shareView.bottom_managerView.constant = -224;
    shareView.bottom_visitor.constant = -112;
    shareView.upview_top_layout.constant = -180;
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        
        if (isRoomOwner || isManager) {
//            shareView.bottom_managerView.constant = 0;
        }else{
            shareView.upview_left_layout.constant = -170;
//            shareView.bottom_visitor.constant = 0;
        }
        shareView.upview_top_layout.constant = 30;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
    }];
}
@end
