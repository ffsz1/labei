//
//  HJGiftSpringAnimateView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGiftSpringAnimateView.h"
#import "HJGiftCore.h"
#import "GiftInfo.h"
#import <UIImageView+WebCache.h>

@interface HJGiftSpringAnimateView()
@property (weak, nonatomic) IBOutlet UIImageView *senderAvatar;
@property (weak, nonatomic) IBOutlet UILabel *senderNick;
@property (weak, nonatomic) IBOutlet UIImageView *reveiverAvatar;
@property (weak, nonatomic) IBOutlet UILabel *receiverNick;

@property (weak, nonatomic) IBOutlet UIImageView *giftView;
@property (weak, nonatomic) IBOutlet UILabel *giftName;
@property (weak, nonatomic) IBOutlet UILabel *giftNum;
@property (weak, nonatomic) IBOutlet UIImageView *giftAnimateBg;
@property (weak, nonatomic) IBOutlet UIImageView *songImage;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *giftAnimateBgHeightConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *giftSenderAvatarCenterYConstranint;

@end

@implementation HJGiftSpringAnimateView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJGiftSpringAnimateView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    self.giftCycleView.image = [UIImage imageNamed:@"game_room_cycle_animate"];
    self.senderAvatar.layer.cornerRadius = self.senderAvatar.frame.size.width / 2;
    self.reveiverAvatar.layer.cornerRadius = self.reveiverAvatar.frame.size.width / 2;
    self.senderAvatar.layer.masksToBounds = YES;
    self.reveiverAvatar.layer.masksToBounds = YES;
    [self rotateImageView];
}
 

- (void)dealloc {
    [self.giftCycleView.layer removeAllAnimations];
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

- (void)setGiftReceiveInfo:(HJGiftAllMicroSendInfo *)giftReceiveInfo {
    _giftReceiveInfo = giftReceiveInfo;
    if (giftReceiveInfo.targetUids.count > 0) {
        GiftInfo *info = [GetCore(HJGiftCore)findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeNormal];
        if (info == nil) {
            info = [GetCore(HJGiftCore)findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeMystic];
        }
        self.giftNum.text = [NSString stringWithFormat:@"X%ld",giftReceiveInfo.giftNum];
        [self.senderAvatar sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.avatar]];
        [self.senderNick setText:giftReceiveInfo.nick];
        [self.reveiverAvatar setImage:[UIImage imageNamed:@"logo"]];
        [self.receiverNick setText:NSLocalizedString(XCRoomAllMic, nil)];
        [self.giftView sd_setImageWithURL:[NSURL URLWithString:info.giftUrl]];
        
        NSInteger giftTotal = giftReceiveInfo.giftNum * info.goldPrice * giftReceiveInfo.targetUids.count;
        
        UIImage *giftAnimateBgImage = nil;
        CGFloat centerYOffset = 0;
        if (giftTotal >= 520 && giftTotal < 4999) {
            giftAnimateBgImage = [UIImage imageNamed:@"game_room_animate_1"];
            self.songImage.image = [UIImage imageNamed:@"game_room_animate_song"];
            centerYOffset = -5;
        }else if (giftTotal >= 4999 && giftTotal < 9999) {
            giftAnimateBgImage = [UIImage imageNamed:@"game_room_animate_2"];
            self.songImage.image = [UIImage imageNamed:@"game_room_animate_song"];
            centerYOffset = -5;
        }else if (giftTotal >= 9999) {
            giftAnimateBgImage = [UIImage imageNamed:@"game_room_animate_3"];
            self.songImage.image = [UIImage imageNamed:@"game_room_animate_song"];
            centerYOffset = -5;
        }
        self.giftAnimateBg.image = giftAnimateBgImage;
        if (giftAnimateBgImage) {
            if (giftAnimateBgImage.size.width && self.size.width) {
                CGFloat height = giftAnimateBgImage.size.height / giftAnimateBgImage.size.width * self.size.width;
                self.giftAnimateBgHeightConstraint.constant = height;
                self.giftSenderAvatarCenterYConstranint.constant = centerYOffset;
                [self setNeedsUpdateConstraints];
            }
        }
    }else {
        GiftInfo *info = [GetCore(HJGiftCore)findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeNormal];
        if (info == nil) {
            info = [GetCore(HJGiftCore)findGiftInfoByGiftId:giftReceiveInfo.giftId giftyType:GiftTypeMystic];
        }
        self.giftNum.text = [NSString stringWithFormat:@"X%ld",giftReceiveInfo.giftNum];
        [self.senderAvatar sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.avatar]];
        [self.senderNick setText:giftReceiveInfo.nick];
        [self.reveiverAvatar sd_setImageWithURL:[NSURL URLWithString:giftReceiveInfo.targetAvatar]];
        [self.receiverNick setText:giftReceiveInfo.targetNick];
        [self.giftView sd_setImageWithURL:[NSURL URLWithString:info.giftUrl]];
        
        NSInteger giftTotal = giftReceiveInfo.giftNum * info.goldPrice;

        UIImage *giftAnimateBgImage = nil;
        CGFloat centerYOffset = 0;
        if (giftTotal >= 520 && giftTotal < 4999) {
            giftAnimateBgImage = [UIImage imageNamed:@"game_room_animate_1"];
            self.songImage.image = [UIImage imageNamed:@"game_room_animate_song"];
            centerYOffset = -5;
        }else if (giftTotal >= 4999 && giftTotal < 9999) {
            giftAnimateBgImage = [UIImage imageNamed:@"game_room_animate_2"];
            self.songImage.image = [UIImage imageNamed:@"game_room_animate_song"];
            centerYOffset = -5;
        }else if (giftTotal >= 9999) {
            giftAnimateBgImage = [UIImage imageNamed:@"game_room_animate_3"];
            self.songImage.image = [UIImage imageNamed:@"game_room_animate_song"];
            centerYOffset = -5;
        }
        self.giftAnimateBg.image = giftAnimateBgImage;
        if (giftAnimateBgImage) {
            if (giftAnimateBgImage.size.width && self.size.width) {
                CGFloat height = giftAnimateBgImage.size.height / giftAnimateBgImage.size.width * self.size.width;
                self.giftAnimateBgHeightConstraint.constant = height;
                self.giftSenderAvatarCenterYConstranint.constant = centerYOffset;
                [self setNeedsUpdateConstraints];
            }
        }
    }
    
}

@end
