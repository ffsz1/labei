//
//  YPAllChannelGiftSpringAnimateView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAllChannelGiftSpringAnimateView.h"
#import "YPGiftCore.h"
#import "UILabel+ColorNumber.h"

#import "YPGiftAllMicroSendInfo.h"

@implementation YPAllChannelGiftSpringAnimateView

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
    self.giftCycleView.image = [UIImage imageNamed:@"game_room_cycle_animate"];
    
    self.senderAvatarView.layer.masksToBounds = YES;
    self.receiverAvaterView.layer.masksToBounds = YES;
    self.sendImageView.image = [UIImage imageNamed:@"game_room_animate_song"];
    
    [self rotateImageView];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.senderAvatarView.layer.cornerRadius = self.senderAvatarView.bounds.size.height / 2.f;
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
    return [[NSBundle mainBundle]loadNibNamed:@"YPAllChannelGiftSpringAnimateView" owner:self options:nil].lastObject;
}

- (void)setGiftReceiveInfo:(YPGiftAllMicroSendInfo *)giftReceiveInfo {
    _giftReceiveInfo = giftReceiveInfo;
    
    self.countLabel.text = [NSString stringWithFormat:@"X%ld",giftReceiveInfo.giftNum];
    YPGiftInfo *info = [GetCore(YPGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeNormal];
    if (info == nil) {
        info = [GetCore(YPGiftCore) findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeMystic];
    }
    [self.gitImageView sd_setImageWithURL:[NSURL URLWithString:info.giftUrl]];
    
    [self.senderAvatarView sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.avatar]];
    self.senderNameLabel.text = giftReceiveInfo.nick;
    
    [self.receiverAvaterView sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.targetAvatar]];
    self.receiverNameLabel.text = giftReceiveInfo.targetNick;
    if ([giftReceiveInfo.targetNick isEqualToString:@"全麦"]) {
           self.receiverAvaterView.image = [UIImage imageNamed:@"yp_room_quanmai"];
           self.receiverNameLabel.text = @"";
        self.quanmai_width_layout.constant = 30;
        self.quanmai_height_layout.constant = 40;
        
       }
}

@end
