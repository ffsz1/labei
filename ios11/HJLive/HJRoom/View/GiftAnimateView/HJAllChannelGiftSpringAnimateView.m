//
//  HJAllChannelGiftSpringAnimateView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAllChannelGiftSpringAnimateView.h"
#import "HJGiftCore.h"
#import "UILabel+ColorNumber.h"

#import "HJGiftAllMicroSendInfo.h"

@implementation HJAllChannelGiftSpringAnimateView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (void)dealloc {
    [self.giftCycleView.layer removeAllAnimations];
}

- (void)awakeFromNib {
    [super awakeFromNib];
//    self.giftCycleView.image = [UIImage imageNamed:@"game_room_cycle_animate"];
    
    self.senderAvatarView.layer.masksToBounds = YES;
    self.receiverAvaterView.layer.masksToBounds = YES;
    self.sendImageView.image = [UIImage imageNamed:@"game_room_animate_song"];
    
//    [self rotateImageView];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.senderAvatarView.layer.cornerRadius = self.senderAvatarView.bounds.size.height / 2.f;
    self.senderAvatarView.layer.borderColor = [UIColor colorWithHexString:@"#7327FC"].CGColor;
    self.senderAvatarView.layer.borderWidth = 1;
    self.receiverAvaterView.layer.cornerRadius = self.receiverAvaterView.bounds.size.height / 2.f;
}

- (void)rotateImageView {
    
    CABasicAnimation *animation =  [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    //默认是顺时针效果，若将fromValue和toValue的值互换，则为逆时针效果
    animation.fromValue = [NSNumber numberWithFloat:0.f];
    animation.toValue =  [NSNumber numberWithFloat: 2 * M_PI];
    animation.duration  = 20;
    animation.autoreverses = NO;
    animation.fillMode =kCAFillModeForwards;
    animation.repeatCount = MAXFLOAT; //如果这里想设置成一直自旋转，可以设置为MAXFLOAT，否则设置具体的数值则代表执行多少次
    animation.removedOnCompletion = NO;
    [self.giftCycleView.layer addAnimation:animation forKey:nil];
}

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJAllChannelGiftSpringAnimateView" owner:self options:nil].lastObject;
}

- (void)setGiftReceiveInfo:(HJGiftAllMicroSendInfo *)giftReceiveInfo {
    _giftReceiveInfo = giftReceiveInfo;
    
    self.countLabel.text = [NSString stringWithFormat:@"X%ld",giftReceiveInfo.count];
    GiftInfo *info = [GetCore(HJGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeNormal];
    if (info == nil) {
        info = [GetCore(HJGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeMystic];
    }
//    [self.gitImageView sd_setImageWithURL:[NSURL URLWithString:info.giftUrl]];
    [self.gitImageView sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.giftUrl]];
    
    [self.senderAvatarView sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.avatar]];
    self.senderNameLabel.text = giftReceiveInfo.nick;
    self.receiverNameLabel.text = giftReceiveInfo.giftName;
//    [self.receiverAvaterView sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.targetAvatar]];
//    self.receiverNameLabel.text = giftReceiveInfo.targetNick;
//    if ([giftReceiveInfo.targetNick isEqualToString:@"全麦"]) {
//           self.receiverAvaterView.image = [UIImage imageNamed:@"hj_room_quanmai"];
//           self.receiverNameLabel.text = @"";
//        self.quanmai_width_layout.constant = 70;
//       }
}

@end
