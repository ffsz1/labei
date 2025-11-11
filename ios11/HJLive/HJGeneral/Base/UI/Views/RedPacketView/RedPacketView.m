//
//  RedPacketView.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "RedPacketView.h"
#import "HJPurseViewControllerFactory.h"
#import "HJRoomViewControllerCenter.h"
#import "TYAlertController.h"
//#import <MMDrawerController/MMDrawerController.h>

@interface RedPacketView() <CALayerDelegate,CAAnimationDelegate>
//已打开
@property (weak, nonatomic) IBOutlet UIImageView *redPacketBg;
@property (weak, nonatomic) IBOutlet UIButton *openButton;

//打开后
@property (weak, nonatomic) IBOutlet UIImageView *redPacketOpenedBg;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;
@property (weak, nonatomic) IBOutlet UILabel *sysbolLabel;
@property (weak, nonatomic) IBOutlet UIButton *goCheckButton;
@property (weak, nonatomic) IBOutlet UILabel *checkLabel;


@end

@implementation RedPacketView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"RedPacketView" owner:self options:nil].lastObject;
}

- (void)setPacketNum:(double)packetNum {
    _packetNum = packetNum;
    NSString *unitText = @"¥ ";
    NSMutableAttributedString *atr = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@%.2lf", unitText, self.packetNum]];
    NSDictionary *dic = @{NSFontAttributeName : [UIFont boldSystemFontOfSize:26]};
    [atr addAttributes:dic range:[atr.string rangeOfString:unitText]];
    self.priceLabel.attributedText = atr;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
//    self.redPacketOpenedBg.alpha = 0;
//    self.priceLabel.alpha = 0;
////    self.sysbolLabel.alpha = 0;
//    self.goCheckButton.alpha = 0;
//    self.checkLabel.alpha = 0;
    
    self.openButton.alpha = 0;
    self.redPacketBg.alpha = 0;
    self.redPacketNameLabel.alpha = 0;
    self.redPacketOpenedBg.alpha = 1;
    self.priceLabel.alpha = 1;
    self.checkLabel.alpha = 1;
    //        self.sysbolLabel.alpha = 1;
    self.goCheckButton.alpha = 1;
}

- (IBAction)openRedPacketButton:(UIButton *)sender {
    
//    CABasicAnimation* rotationAnimation;
//    //绕哪个轴，那么就改成什么：这里是绕y轴 ---> transform.rotation.y
//    rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.y"];
//    //旋转角度
//    rotationAnimation.toValue = [NSNumber numberWithFloat: 2 * M_PI];
//    //每次旋转的时间（单位秒）
//    rotationAnimation.duration = 1;
//    rotationAnimation.cumulative = YES;
//    //重复旋转的次数，如果你想要无数次，那么设置成MAXFLOAT
//    rotationAnimation.repeatCount = 2;
//    rotationAnimation.removedOnCompletion = NO;
//    rotationAnimation.delegate = self;
////    self.openButton.layer.delegate = self;
//    self.openButton.layer.zPosition = self.openButton.layer.frame.size.width / 2;
//    [self.openButton.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];
//
//    [UIView animateWithDuration:1 delay:2 options:UIViewAnimationOptionTransitionNone animations:^{
//        self.openButton.alpha = 0;
//        self.redPacketBg.alpha = 0;
//        self.redPacketNameLabel.alpha = 0;
//        self.redPacketOpenedBg.alpha = 1;
//        self.priceLabel.alpha = 1;
//        self.checkLabel.alpha = 1;
////        self.sysbolLabel.alpha = 1;
//        self.goCheckButton.alpha = 1;
//        self.priceLabel.text = [NSString stringWithFormat:@"¥ %@",self.packetNum];
//
//    } completion:^(BOOL finished) {
//
//    }];
}

- (IBAction)goCheckButtonClick:(UIButton *)sender {
    UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
    NSArray *controllers = self.navigationController.viewControllers;
    BOOL needPop = NO;
    for (UIViewController *tempVc in controllers) {
        if ([vc.class isKindOfClass:tempVc.class]) {
            needPop = YES;
        }
    }
    TYAlertController *alert = (TYAlertController *)[[HJRoomViewControllerCenter defaultCenter]getCurrentVC];
    if (needPop) {
        [alert dismissViewControllerAnimated:YES completion:^{
            UINavigationController *nav = [[HJRoomViewControllerCenter defaultCenter]currentNavigationController];
            [nav popViewControllerAnimated:YES];
        }];
    } else {
        [alert dismissViewControllerAnimated:YES completion:^{

            UINavigationController *nav = [[HJRoomViewControllerCenter defaultCenter]currentNavigationController];
            [nav pushViewController:vc animated:YES];
        }];
    }
    
    
}

- (IBAction)closeButtonClick:(UIButton *)sender {
    if ([_delagate respondsToSelector:@selector(dismissRedPacketView)]) {
        [_delagate dismissRedPacketView];
    }
}

//- (void)animationDidStop:(CAAnimation *)anim finished:(BOOL)flag {
//    if (anim == [self.openButton.layer animationForKey:@"rotationAnimation"]) {
//
//        [self.openButton.layer removeAllAnimations];
//
//    }
//}

@end
