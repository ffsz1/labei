//
//  YPHomeHeaderView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeHeaderView.h"

#import "YPNotiFriendVC.h"

#import "YPIMMessage.h"
#import "NSString+YPNIMKit.h"
#import "UIView+getTopVC.h"


@implementation YPHomeHeaderView

- (void)awakeFromNib {
    [super awakeFromNib];
    self.bannerContainerView.placeholderImage = [UIImage imageNamed:@"yp_home_weekStar"];
   self.yp_jiaoyou_trailing_layout.constant= iPhoneX?92:55;
      if (kScreenWidth>380) {
           self.yp_jiaoyou_trailing_layout.constant= 92;
      }
    
    
    CAGradientLayer *gradient = [CAGradientLayer layer];
    gradient.colors = [NSArray arrayWithObjects:
                       (id)UIColorHex(667CFE).CGColor,
                       (id)UIColorHex(FF57B3).CGColor, nil];
    gradient.startPoint = CGPointMake(0, 0);
    gradient.endPoint = CGPointMake(1, 0);
    
}

- (void)startTimer
{
    NSTimer *timeManager = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(scrollNextMsgView) userInfo:nil repeats:YES];
    
//    [NSTimer timerWithTimeInterval:5 repeats:YES block:^(NSTimer * _Nonnull timer) {
//        [self scrollNextMsgView];
//    }]
    
    self.timer = timeManager;
    
    [[NSRunLoop currentRunLoop] addTimer:self.timer forMode:NSRunLoopCommonModes];
}

- (void)scrollNextMsgView
{
    
    if (self.index<self.imRoomMsgArr.count) {
        
        YPIMMessage *message = [self.imRoomMsgArr safeObjectAtIndex:self.index];
        
        [self setIMMessageModel3:message];
        
        NSInteger nextIndex = self.index+1;
        if (nextIndex>=self.imRoomMsgArr.count) {
            nextIndex = 0;
        }
        
        YPIMMessage *nexMessage = [self.imRoomMsgArr safeObjectAtIndex:nextIndex];
        [self setIMMessageModel4:nexMessage];
        
        self.index = nextIndex+1;
        if (self.index>=self.imRoomMsgArr.count) {
            self.index = 0;
        }
        
        
        self.avatar1.image = self.avatarImage3.image;
        self.avatarImageView1.image = self.avatarImage3.image;
    }
    
    
    self.msgView1.alpha = 1;
    self.msgView2.alpha = 0;
    
//    self.centerY_msgView1.constant = 0;
//    self.centerY_msgView2.constant = 25;
    self.centerY_msgView1.constant = 15;
       self.centerY_msgView2.constant = 65;
    
    [UIView animateWithDuration:0.2 animations:^{
        self.msgView1.alpha = 0;
    }];
    
    
    [UIView animateWithDuration:0.3 animations:^{
        
        self.msgView2.alpha = 1;
        
//        self.centerY_msgView1.constant = -50;
//        self.centerY_msgView2.constant = 0;
        self.centerY_msgView1.constant = -35;
        self.centerY_msgView2.constant = 15;
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            
            
            self.avatarImageView2.image = self.avatar2.image;
            self.avatarImage3.image = self.avatar3.image;
            self.nameLabel1.attributedText = self.name1.attributedText;
            self.nameLabel2.attributedText = self.name2.attributedText;
            
//            self.avatarImage3.image = self.avatar1.image;

            self.msgView1.alpha = 1;
            self.msgView2.alpha = 0;
            
//            self.centerY_msgView1.constant = 0;
//            self.centerY_msgView2.constant = 50;
            self.centerY_msgView1.constant = 15;
            self.centerY_msgView2.constant = 65;
        });
        
    }];
}

- (void)setData
{
    
    if (self.index<self.imRoomMsgArr.count) {
        YPIMMessage *message = [self.imRoomMsgArr safeObjectAtIndex:self.index];
        
        [self setIMMessageModel1:message];
        
        NSInteger nextIndex = self.index+1;
        if (nextIndex>=self.imRoomMsgArr.count) {
            nextIndex = 0;
        }
        
        YPIMMessage *nexMessage = [self.imRoomMsgArr safeObjectAtIndex:nextIndex];
        [self setIMMessageModel2:nexMessage];
        
        self.index = nextIndex+1;
        if (self.index>=self.imRoomMsgArr.count) {
            self.index = 0;
        }
    }
    
}

- (void)setIMMessageModel1:(YPIMMessage *)message
{
    
    if (message == nil) {
        return;
    }
     YPAttachment *attachment = [YPAttachment yy_modelWithJSON:message];

//    if (message.messageType == NIMMessageTypeCustom) {
//        JXIMCustomObject *obj = message.messageObject;
//        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
//            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            NSDictionary *dic = attachment.data;
            
            NSString *msgStr = @"";
            NSString *nickStr = @"";

            if (dic) {
                if ([dic containsObjectForKey:@"msg"]) {
                    msgStr = dic[@"msg"];
                }
                if ([dic containsObjectForKey:@"params"]) {
                    NSDictionary *dicParams = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
                    
                    if ([dicParams containsObjectForKey:@"avatar"]) {
                        [self.avatarImageView2 sd_setImageWithURL:[NSURL URLWithString:[dicParams[@"avatar"] cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
                    }
                    if ([dicParams containsObjectForKey:@"nick"]) {
                        nickStr = dicParams[@"nick"];
                    }
                    
                    NSInteger uid = [dicParams[@"uid"] integerValue];
                    
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
                        self.avatarImageView2.image = [UIImage imageNamed:@"yp_guanfang"];
                    }
                }
                
                
                self.nameLabel1.text = [NSString stringWithFormat:@"%@:%@",nickStr,msgStr];
                
                
                [self setAttStr:self.nameLabel1 name:nickStr msg:msgStr];
                
            }
//        }
//    }
}

- (void)setIMMessageModel2:(YPIMMessage *)message
{
    if (message == nil) {
        return;
    }
    YPAttachment *attachment = [YPAttachment yy_modelWithJSON:message];

//    if (message.messageType == NIMMessageTypeCustom) {
//        JXIMCustomObject *obj = message.messageObject;
//        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
//            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            NSDictionary *dic = attachment.data;
            
            NSString *msgStr = @"";
            NSString *nickStr = @"";
            
            if (dic) {
                if ([dic containsObjectForKey:@"msg"]) {
                    msgStr = dic[@"msg"];
                }
                if ([dic containsObjectForKey:@"params"]) {
                    NSDictionary *dicParams = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
                    
                    if ([dicParams containsObjectForKey:@"avatar"]) {
                        [self.avatarImage3 sd_setImageWithURL:[NSURL URLWithString:[dicParams[@"avatar"] cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
                    }
                    if ([dicParams containsObjectForKey:@"nick"]) {
                        nickStr = dicParams[@"nick"];
                    }
                    
                    NSInteger uid = [dicParams[@"uid"] integerValue];
                    
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
                        // HJ官方
                        self.avatarImage3.image = [UIImage imageNamed:@"yp_guanfang"];
                    }
                }
                
                
                self.nameLabel2.text = [NSString stringWithFormat:@"%@:%@",nickStr,msgStr];
                
                
                [self setAttStr:self.nameLabel2 name:nickStr msg:msgStr];

                
            }
//        }
//    }
}

- (void)setIMMessageModel3:(YPIMMessage *)message
{
    if (message == nil) {
        return;
    }
    YPAttachment *attachment = [YPAttachment yy_modelWithJSON:message];

//    if (message.messageType == NIMMessageTypeCustom) {
//        JXIMCustomObject *obj = message.messageObject;
//        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
//            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            NSDictionary *dic = attachment.data;
            
            NSString *msgStr = @"";
            NSString *nickStr = @"";
            
            if (dic) {
                if ([dic containsObjectForKey:@"msg"]) {
                    msgStr = dic[@"msg"];
                }
                if ([dic containsObjectForKey:@"params"]) {
                    NSDictionary *dicParams = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
                    
                    if ([dicParams containsObjectForKey:@"avatar"]) {
                        [self.avatar2 sd_setImageWithURL:[NSURL URLWithString:[dicParams[@"avatar"] cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
                    }
                    if ([dicParams containsObjectForKey:@"nick"]) {
                        nickStr = dicParams[@"nick"];
                    }
                    
                    NSInteger uid = [dicParams[@"uid"] integerValue];
                    
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
                        self.avatar2.image = [UIImage imageNamed:@"yp_guanfang"];
                    }
                }
                
                
                self.name1.text = [NSString stringWithFormat:@"%@:%@",nickStr,msgStr];
                
                
                [self setAttStr:self.name1 name:nickStr msg:msgStr];

                
            }
//        }
//    }
}


- (void)setIMMessageModel4:(YPIMMessage *)message
{
    if (message == nil) {
        return;
    }
    YPAttachment *attachment = [YPAttachment yy_modelWithJSON:message];

//    if (message.messageType == HJIMMessageTypeText) {
//        JXIMCustomObject *obj = message.messageObject;
//        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
//            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            NSDictionary *dic = attachment.data;
            
            NSString *msgStr = @"";
            NSString *nickStr = @"";
            
            if (dic) {
                if ([dic containsObjectForKey:@"msg"]) {
                    msgStr = dic[@"msg"];
                }
                if ([dic containsObjectForKey:@"params"]) {
                    NSDictionary *dicParams = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
                    
                    if ([dicParams containsObjectForKey:@"avatar"]) {
                        [self.avatar3 sd_setImageWithURL:[NSURL URLWithString:[dicParams[@"avatar"] cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
                    }
                    if ([dicParams containsObjectForKey:@"nick"]) {
                        nickStr = dicParams[@"nick"];
                    }
                    
                    NSInteger uid = [dicParams[@"uid"] integerValue];
                    
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
                        self.avatar3.image = [UIImage imageNamed:@"yp_guanfang"];
                    }
                }
                
                
                self.name2.text = [NSString stringWithFormat:@"%@:%@",nickStr,msgStr];
                
                [self setAttStr:self.name2 name:nickStr msg:msgStr];
                
            }
//        }
//    }
}

- (void)setAttStr:(UILabel *)label name:(NSString *)nameStr msg:(NSString *)msgStr
{
    label.text = [NSString stringWithFormat:@"%@:%@",nameStr,msgStr];
    NSMutableAttributedString *textFont = [[NSMutableAttributedString alloc] initWithString:label.text];
//    [textFont addAttribute:NSFontAttributeName
//                     value:JXFontPingFangSCRegular(13)
//                     range:NSMakeRange(nameStr.length+1, msgStr.length)];
//
//    [textFont addAttribute:NSForegroundColorAttributeName
//                     value:UIColorHex(666666)
//                     range:NSMakeRange(nameStr.length+1, msgStr.length)];
    label.attributedText = textFont;
}


- (void)setImRoomMsgArr:(NSMutableArray *)imRoomMsgArr
{
    _imRoomMsgArr = imRoomMsgArr;
    
    [self.timer invalidate];
    self.timer = nil;
    
    self.index = 0;
    [self setData];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self startTimer];
    });
    
}

- (IBAction)friendRoomAction:(id)sender {
    
    YPNotiFriendVC *viewController = [[YPNotiFriendVC alloc] init];
    [viewController updateData];
    [[self topViewController].navigationController pushViewController:viewController animated:YES];
    
}

@end
