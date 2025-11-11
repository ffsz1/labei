//
//  YPFYCashBindingView.m
//  XBD
//
//  Created by feiyin on 2019/10/31.
//
#import "YPverticalButton.h"
#import "YPFYCashBindingView.h"






@interface YPFYCashBindingView()
@property (weak, nonatomic) IBOutlet UIView *cashBindView;
//XBDverticalButton *wxCashButton;

@property (weak, nonatomic) IBOutlet YPverticalButton *wxCashBtn;

@property (weak, nonatomic) IBOutlet YPverticalButton *bankButton;


@property (weak, nonatomic) IBOutlet UIView *effectView;


@property (weak, nonatomic) IBOutlet UIButton *cancelButton;


@property (weak, nonatomic) IBOutlet NSLayoutConstraint *wxBottomLayout2;



@property (copy, nonatomic) RoomMenuBlock menuBlock;
@end
@implementation YPFYCashBindingView


+ (void)show:(RoomMenuBlock)menuBlock
{
    YPFYCashBindingView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPFYCashBindingView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
//
   
     shareView.wxCashBtn.titleSpacing = 5;
    shareView.bankButton.titleSpacing = 5;
    
//    if (isManager) {
//        shareView.left_managerBtn.constant = -kScreenWidth/4;
//        shareView.managerBtn.hidden = YES;
//    }
    
//    shareView.openMsgBtn.selected = GetCore(XBDImRoomCoreV2).currentRoomInfo.publicChatSwitch;
//    shareView.openCarBtn.selected = GetCore(XBDImRoomCoreV2).currentRoomInfo.giftCardSwitch;
//    shareView.openGiftBtn.selected = GetCore(XBDImRoomCoreV2).currentRoomInfo.giftEffectSwitch;
    
    shareView.menuBlock = menuBlock;
    shareView.wxBottomLayout2.constant = -183;
    shareView.wxCashBtn.selected = YES;
    
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        
    
         shareView.wxBottomLayout2.constant = 0;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
        
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBgRaduis];
    
   UITapGestureRecognizer* tapGest = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction)];
    [self.effectView addGestureRecognizer:tapGest];
    
}

- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.2 animations:^{
        
        self.wxBottomLayout2.constant = -183;
   
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}

- (void)setBgRaduis
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 183);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.cashBindView.layer.mask = maskLayer;

    
//    CGRect frame2 = CGRectMake(0, 0, kScreenWidth, 112);
//    UIBezierPath *maskPath2 = [UIBezierPath bezierPathWithRoundedRect:frame2 byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
//
//    CAShapeLayer * maskLayer2 = [[CAShapeLayer alloc]init];
//    maskLayer2.frame = frame2;
//    maskLayer2.path = maskPath2.CGPath;
//    self.visitorMenuView.layer.mask = maskLayer2;
    
}


- (IBAction)wxCashButtonAction:(id)sender {
    self.menuBlock(FYCashType);
        [self close];
}

- (IBAction)bankCashButtonAction:(id)sender {
    self.menuBlock(FYBankCashType);
           [self close];
    
    
}


- (IBAction)cancelButtonAction:(id)sender {
    self.menuBlock(FYCancelType);
    [self close];
    
}




-(void)tapAction{
     [self close];
    
}


@end
