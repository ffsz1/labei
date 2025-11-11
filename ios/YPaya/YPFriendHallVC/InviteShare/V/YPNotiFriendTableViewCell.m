//
//  YPNotiFriendTableViewCell.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNotiFriendTableViewCell.h"
#import "YPAttachment.h"

#import "NSString+YPNIMKit.h"

@interface YPNotiFriendTableViewCell ()
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *deautLabelBetweenName;
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *miImgBetweenUserHead;
@property (weak, nonatomic) IBOutlet UIImageView *textLabelBackgroundView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_level;

@end

@implementation YPNotiFriendTableViewCell

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
    self.bgView.layer.cornerRadius = 8;
    self.bgView.layer.masksToBounds = YES;
    
    self.userHeadImg.layer.cornerRadius = 22;
    self.userHeadImg.layer.masksToBounds = YES;
    
//    {10,25,25,25}
    UIImage *image = [[UIImage imageNamed:@"yp_noti_icon_tuzi"] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{10,15,25,35}") resizingMode:UIImageResizingModeStretch];
    [self.textLabelBackgroundView setImage:image];
}

- (void)resetControl {
    self.LvImg.hidden = YES;
    self.deautLabel.text = nil;
    self.nickLabel.text = nil;
    [self.userHeadImg sd_setImageWithURL:nil placeholderImage:[UIImage imageNamed:default_avatar]];
    self.MlImg.hidden = YES;
}

- (void)setMessage:(YPIMMessage *)message {
    _message = message;
    [self resetControl];
    self.deautLabel.textColor = UIColorHex(316AFF);//1A1A1A
    if (message.messageType == NIMMessageTypeCustom) {
        JXIMCustomObject *obj = message.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            NSDictionary *dic = attachment.data;
//            self.LvImg.image = [UIImage imageNamed:[NSString stringWithFormat:@"Lv%ld",(long)message.member.exper_level]];
//            self.LvImg.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:message.member.exper_level]];
//            if (message.member.exper_level == 0) {
//                self.LvImg.image = nil;
//                self.LvImg.hidden = YES;
//                self.miImgBetweenUserHead.constant = 15;
//            } else {
//                self.lvWidCons.constant = 30;
//                self.LvImg.hidden = false;
//                self.miImgBetweenUserHead.constant = 45;
//            }
            if (dic) {
                if ([dic containsObjectForKey:@"msg"]) {
                    self.deautLabel.text = dic[@"msg"];
                }
                if ([dic containsObjectForKey:@"params"]) {
                    NSDictionary *dicParams = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
                    
                    if ([dicParams containsObjectForKey:@"avatar"]) {
                        [self.userHeadImg sd_setImageWithURL:[NSURL URLWithString:[dicParams[@"avatar"] cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
                    }
                    if ([dicParams containsObjectForKey:@"nick"]) {
                        self.nickLabel.text = dicParams[@"nick"];
                    }
                    
                    NSInteger uid = [dicParams[@"uid"] integerValue];
                    
                    NSString *txtColor = [dicParams[@"txtColor"] description];
                    
                    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
                    NSString *ID = [[userDefaults objectForKey:EnvID] description];
                    
                    NSInteger miamiaoUid = 0;
                    if ([ID isEqualToString:@"0"]) {
                        miamiaoUid = 100712;
                    }
                    else {
                        miamiaoUid = 1094804;
                    }
                    
                    if (uid == miamiaoUid) {
                        // hj官方
                        self.LvImg.image = [UIImage imageNamed:@"yp_guanfang"];
                        self.lvWidCons.constant = 30;
                        self.LvImg.hidden = false;
                        self.miImgBetweenUserHead.constant = 45;
                        self.mlWidCons.constant = 0;
                        self.MlImg.hidden = YES;
                        if (txtColor.length) {
                            self.deautLabel.textColor = [[YPYYTheme defaultTheme] colorWithHexString:txtColor alpha:1.f];
                        }
                        else {
                            
                            self.deautLabel.textColor = UIColorHex(FF0030);
                        }
                    }
                    else {
                        if ([dicParams containsObjectForKey:@"experLevel"]) {
                            NSInteger experLevel = [dicParams[@"experLevel"] integerValue];
                            self.LvImg.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:experLevel]];
                            if (experLevel == 0) {
                                self.LvImg.image = nil;
                                self.LvImg.hidden = YES;
//                                self.miImgBetweenUserHead.constant = 15;
                                self.width_level.constant = 0;
                            } else {
                                self.lvWidCons.constant = 30;
                                self.LvImg.hidden = false;
//                                self.miImgBetweenUserHead.constant = 45;
                                self.width_level.constant = 49.66;
                            }
                        }else{
                            self.width_level.constant = 0;
                        }
                       
                        if ([dicParams containsObjectForKey:@"charmLevel"]) {
                            NSInteger charmLevel = [dicParams[@"charmLevel"] integerValue];
                            self.MlImg.image = [UIImage imageNamed:[NSString getCharmLevelImageName:charmLevel]];
                            if (charmLevel == 0) {
//                                self.mlWidCons.constant = 0;
                                self.MlImg.image = nil;
                                self.MlImg.hidden = YES;
                            } else {
//                                self.mlWidCons.constant = 30;
                                self.MlImg.hidden = NO;
                            }
//                            self.MlImg.image = [UIImage imageNamed:[NSString stringWithFormat:@"ml%@",dicParams[@"charmLevel"]]];
                        }
                        else {
//                            self.mlWidCons.constant = 0;
                            self.MlImg.hidden = YES;
                        }
                    }
                }
            }
        }
    }
    
    if (self.LvImg.hidden && self.MlImg.hidden) {
        self.deautLabelBetweenName.constant = 18;
    }
    else {
        self.deautLabelBetweenName.constant = 40;
    }
    
    [self layoutIfNeeded];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
