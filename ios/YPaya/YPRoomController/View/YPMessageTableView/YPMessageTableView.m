//
//  YPMessageTableView.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//


//用户验证
#import "YPAuthCoreHelp.h"
//即时通讯
#import <NIMSDK/NIMSDK.h>
#import "YPAttachment.h"
//用户
#import "YPUserCoreHelp.h"
//UI控件
#import "YPMessageTableView.h"
#import "YPNewMessageCell.h"
#import "YPPlayingFaceMessageCell2.h"
#import "YPYYActionSheetViewController.h"

//工厂方法
#import "YPUserViewControllerFactory.h"
//UI属性
#import "YPYYDefaultTheme.h"

//礼物
#import "YPGiftCore.h"
#import "HJGiftCoreClient.h"
#import "YPLiveGiftShowCustom.h"
//表情
#import "YPFaceCore.h"
#import "YPFaceReceiveInfo.h"
#import "YPFaceSendInfo.h"

//房间
#import "YPMeetingCore.h"
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPRoomQueueCoreV2Help.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"


//拍卖相关




//Category
#import "NSObject+YYModel.h"
#import "UIImageView+YYWebImage.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "SDWebImageDownloader.h"
#import "SDWebImageManager.h"
#import "YPRoomViewControllerCenter.h"
#import "HJBalanceErrorClient.h"
#import "TYAlertController.h"
#import "YPShareSendInfo.h"
#import "RoomUIClient.h"
#import "YPGiftSendAllMicroAvatarInfo.h"
#import <SDImageCache.h>
#import "NSString+HMRoundNumberString.h"

#import <YYAnimatedImageView.h>

#import "MMAlertView.h"
#import "MMSheetView.h"
#import "YPRoomLongZhuMsgModel.h"
#import "NSString+JsonToDic.h"
#import "YPGiftSecretInfo.h"
#import "YPIMMessage.h"

#import "YPMessagePKCell.h"
#import "YPRoomPKMsgModel.h"
#import "UIView+getTopVC.h"

//用户卡片
#import "YPSpaceCardView.h"

static int kMessagTipsBtnDistance = 0;

@interface YPMessageTableView ()
<
    UIScrollViewDelegate,
    UITableViewDelegate,
    UITableViewDataSource,
    HJRoomCoreClient,
    HJImRoomCoreClient,
    HJBalanceErrorClient,
    RoomUIClient
>

@property(nonatomic, strong)YPChatRoomInfo *roomInfo;

@property (strong, nonatomic) TYAlertController *giftAlertView;

@property (nonatomic, strong) YPYYActionSheetViewController* startAuctionSheet;//开启拍卖
@property (nonatomic, strong) UIImageView * imageView;
@property (nonatomic, assign) BOOL _isForbiddenRefreshChannelTableView;
@property (weak, nonatomic) IBOutlet UIButton *messagTipsBtn;

@property (nonatomic, strong) NSMutableDictionary *_contentAttributedStringForIndexPath;
@property (nonatomic, strong) NSMutableDictionary *userAttributedStringForIndexPath;
@property (nonatomic, assign) BOOL currentIsInBottom;
@property (nonatomic, copy) NSString *currentMsgId;

@end

@implementation YPMessageTableView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPMessageTableView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJBalanceErrorClient, self);
    AddCoreClient(RoomUIClient, self);
    self.messagTipsBtn.hidden = YES;
    [self updateInfo];
    
    self.currentIsInBottom = YES;
    self.tableView.bounces = NO;
    [self.tableView registerClass:[YPNewMessageCell class] forCellReuseIdentifier:@"YPNewMessageCell"];
    [self.tableView registerNib:[UINib nibWithNibName:@"YPPlayingFaceMessageCell2" bundle:nil] forCellReuseIdentifier:@"YPPlayingFaceMessageCell2"];
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPMessagePKCell" bundle:nil] forCellReuseIdentifier:@"YPMessagePKCell"];

    
    
    self.tableView.showsHorizontalScrollIndicator = NO;
    self.tableView.showsVerticalScrollIndicator = NO;
    [self _configureContentAttributedMap];
    [self updateMessage];
    
    self.talkMessageType = HJMessageTypeAll;
    //消息区分通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(notificationSelectTalkMessage:) name:@"NotificationSelectTalkMessage" object:nil];
}

//响应消息区分
- (void)notificationSelectTalkMessage:(NSNotification *)notification{
    
    NSString *msgType = notification.userInfo[@"messageType"];
    if ([msgType isEqualToString:[NSString stringWithFormat:@"%ld",HJMessageTypeAll]]) {
        self.talkMessageType = HJMessageTypeAll;
        [self handleMessageTypeWith:HJMessageTypeAll];
    }
    if ([msgType isEqualToString:[NSString stringWithFormat:@"%ld",HJMessageTypeTalk]]) {
        self.talkMessageType = HJMessageTypeTalk;
        [self handleMessageTypeWith:HJMessageTypeTalk];
    }
    
    if ([msgType isEqualToString:[NSString stringWithFormat:@"%ld",HJMessageTypeGift]]) {
        self.talkMessageType = HJMessageTypeGift;
        [self handleMessageTypeWith:HJMessageTypeGift];
    }
    
    if ([msgType isEqualToString:[NSString stringWithFormat:@"%ld",HJMessageTypeDaCall]]) {
        self.talkMessageType = HJMessageTypeDaCall;
        [self handleMessageTypeWith:HJMessageTypeDaCall];
    }
}

//处理不不同的类型的消息
- (void)handleMessageTypeWith:(HJMessageType)messageType{
    self.messages = [NSMutableArray arrayWithArray:GetCore(YPRoomCoreV2Help).messages];
    NSMutableArray *tempArr = [NSMutableArray array];
    
    if (messageType == HJMessageTypeAll) {
        tempArr = [self.messages mutableCopy];
    }
    
    if (messageType == HJMessageTypeTalk) {
        for (YPIMMessage * msg in self.messages) {
            if (msg.messageType == HJIMMessageTypeText) {
                [tempArr addObject:msg];
            }
            if (msg.messageType == HJIMMessageTypeCustom) {
                if (msg.messageObject.attachment.first == Custom_Noti_Header_Face) {
                    [tempArr addObject:msg];
                }
            }
        }
    }
    
    if (messageType == HJMessageTypeGift) {
        for (YPIMMessage * msg in self.messages) {
            if (msg.messageType == HJIMMessageTypeCustom) {
                if (msg.messageObject.attachment.first == Custom_Noti_Header_Gift || msg.messageObject.attachment.first == Custom_Noti_Header_ALLMicroSend) {
                    [tempArr addObject:msg];
                }
            }
        }
    }
    
    if (messageType == HJMessageTypeDaCall) {
        for (YPIMMessage * msg in self.messages) {
            if (msg.messageType == HJIMMessageTypeCustom) {
                if (msg.messageObject.attachment.first == Custom_Noti_Header_Playcall) {
                    [tempArr addObject:msg];
                }
            }
        }
    }
    self.messages = [tempArr mutableCopy];
    [self.tableView reloadData];
    [self reloadData];
}


- (void)dealloc {
    RemoveCoreClientAll(self);
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"NotificationSelectTalkMessage" object:self];
}

- (void)configLevelWithArrtibuted:(NSMutableAttributedString *)attributed withExperLevel:(NSInteger)experLevel {
    if (experLevel >= 0) {
        YYAnimatedImageView * imageView = [[YYAnimatedImageView alloc] init];
        imageView.contentMode = UIViewContentModeScaleAspectFit;
        UIImage *image = [UIImage imageNamed:[NSString getMoneyLevelImageName:experLevel]];
        CGFloat width = 0;
        CGFloat height = 18;
        if (image.size.height) {
            width = image.size.width / image.size.height * height;
        }
        imageView.bounds = CGRectMake(0, 0, width, height);
        imageView.image = image;
        NSMutableAttributedString * imageString1 = [NSMutableAttributedString yy_attachmentStringWithContent:imageView contentMode:UIViewContentModeScaleAspectFit attachmentSize:imageView.frame.size alignToFont:[UIFont systemFontOfSize:12.0] alignment:YYTextVerticalAlignmentCenter];
        [attributed insertAttributedString:[[NSAttributedString alloc] initWithString:@" "] atIndex:0];
        [attributed insertAttributedString:imageString1 atIndex:0];
    }
}

- (void)configNewUserTagWithArrtibuted:(NSMutableAttributedString *)attributed {
    [attributed insertAttributedString:[[NSAttributedString alloc] initWithString:@" "] atIndex:0];
    YYAnimatedImageView * imageView = [[YYAnimatedImageView alloc] init];
    imageView.contentMode = UIViewContentModeScaleAspectFill;
    UIImage *image = [UIImage imageNamed:@"mengxin"];
    CGFloat width = 0;
    CGFloat height = 20;
    if (image.size.height) {
        width = image.size.width / image.size.height * height +5;
    }
    imageView.bounds = CGRectMake(0, 0, width, height);
//    imageView.bounds = CGRectMake(0, 0, image.size.width/2, image.size.height/2);
    imageView.image = image;
    NSMutableAttributedString * imageString1 = [NSMutableAttributedString yy_attachmentStringWithContent:imageView contentMode:UIViewContentModeScaleAspectFit attachmentSize:imageView.frame.size alignToFont:[UIFont systemFontOfSize:12.0] alignment:YYTextVerticalAlignmentCenter];
    [attributed insertAttributedString:imageString1 atIndex:0];
}

#pragma mark - handleCellForCell
- (void)handleNotificationCell:(YPNewMessageCell *)newCell Message:(YPIMMessage *)msg indexPath:(NSIndexPath *)indexPath{
    
    JXIMChatroomNotificationContent *content = msg.messageObject.notificationContent;
    
    newCell.bgImage.image = [UIImage imageNamed:@"msg_bg_come"];
    
    if (content.eventType == NIMChatroomEventTypeEnter) {
        
        NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
        if (!attributed) {
            
            

            
            attributed = [self getEnterNotificationAttributedStringWithMessage:msg];
            
            NSString *name = (msg.member.nick==nil?@"":[NSString stringWithFormat:@"%@",msg.member.nick]);
            [attributed yy_insertString:name atIndex:0];
            
            //白色字体
            [attributed addAttribute:NSFontAttributeName
                               value:[UIFont systemFontOfSize:12.0f]
                               range:NSMakeRange(0, attributed.length)];
            [attributed addAttribute:NSForegroundColorAttributeName
                               value:[UIColor whiteColor]
                               range:NSMakeRange(0, attributed.length)];
            
            //配置等级图片
            [self configLevelWithArrtibuted:attributed withExperLevel:msg.member.exper_level];
            
            //配置萌新图标
            if (msg.member.is_new_user) {
                [self configNewUserTagWithArrtibuted:attributed];
            }
        
            
            
            [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];


            
        }
        newCell.messageLabel.attributedText = attributed;
    }
}

- (void)handleZDCell:(YPNewMessageCell *)newCell Message:(YPIMMessage *)msg indexPath:(NSIndexPath *)indexPath {
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    NSString *nick = @"";
    NSString *giftName = @"";
    
    
    JXIMCustomObject *obj = msg.messageObject;
    YPAttachment *attachment = obj.attachment;
    NSDictionary *dic = attachment.data;
    if ([dic objectForKey:@"params"]) {
        NSDictionary *paramsDic = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
        CGFloat price = [paramsDic[@"goldPrice"] doubleValue];
        
        if (price>=13140){
            newCell.bgImage.image = [UIImage imageNamed:@"msg_bg_ching"];
        }else if (price>=1888){
//            newCell.bgImage.image = [UIImage imageNamed:@"msg_bg_blue"];
        }else if (price>=1000){
//            newCell.bgImage.image = [UIImage imageNamed:@"msg_bg_purple"];
        }else if (price>=520) {
//            newCell.bgImage.image = [UIImage imageNamed:@"msg_bg_red"];
        }else{
            newCell.bgImage.image = nil;
        }
        
        
    }else{
        newCell.bgImage.image = nil;
    }
    
    
    NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
    if (!attributed) {
        if ([msg.from isEqualToString:userId]) {
            YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;
            nick = myMember.nick;
        } else {
            nick = msg.member.nick;
        }
        
        JXIMCustomObject *obj = msg.messageObject;
        YPAttachment *attachment = obj.attachment;
        NSDictionary *dic = attachment.data;
        NSString *gitName = @"";
        NSString *gitCount = @"";
        NSString *giftPrice = @"";
        if ([dic objectForKey:@"params"]) {
            NSDictionary *paramsDic = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
            gitName = paramsDic[@"giftName"];
            gitCount = [NSString stringWithFormat:@"%@", paramsDic[@"count"]];
            giftPrice = [NSString stringWithFormat:@"（%@金币）", [NSString hm_stringFromDouble:[paramsDic[@"goldPrice"] doubleValue] roundingScale:0]];
            if (giftPrice != nil) {
                     gitName = [gitName stringByAppendingString:giftPrice];
            }else{
                gitName = [gitName stringByAppendingString:@""];
            }
            
            if (nick == nil) {
                nick = paramsDic[@"nick"];
            }
        }
        
        newCell.messageLabel.textColor = [UIColor whiteColor];
//          NSString *text = [NSString stringWithFormat:@"恭喜 %@ 捡海螺获得礼物 “%@” x%@", nick, gitName, gitCount];
        NSString *text = [NSString stringWithFormat:@"恭喜 %@ 捡海螺获得礼物 %@ x%@", nick, gitName, gitCount];
        UIColor *redColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#2ACEF5" alpha:1.0];
          UIColor *giftColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#FFCA28" alpha:1.0];
        
        UIColor *color1 = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:1.0];
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:text];
        [str addAttribute:NSForegroundColorAttributeName
                    value:color1
                    range:text.rangeOfAll];
        [str addAttribute:NSForegroundColorAttributeName
                    value:redColor
                    range:NSMakeRange(3,nick.length)];
        [str addAttribute:NSForegroundColorAttributeName
                    value:giftColor
                    range:[text rangeOfString:[NSString stringWithFormat:@"%@", gitName]]];
        
        [str addAttribute:NSForegroundColorAttributeName
                           value:giftColor
                           range:[text rangeOfString:[NSString stringWithFormat:@"x%@", gitCount]]];
        
        [str addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:13]
                    range:text.rangeOfAll];
        [str addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:14]
                    range:NSMakeRange(3,nick.length)];
        [str addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:14]
                    range:[text rangeOfString:[NSString stringWithFormat:@"%@", gitName]]];
        attributed = str;
        [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];
        
        if (msg.member.is_new_user) {
            [self configNewUserTagWithArrtibuted:attributed];
        }
    }
    newCell.messageLabel.attributedText = attributed;
    
    

}

#pragma mark - handleCellForSecretCell
- (void)handleSecretCell:(YPNewMessageCell *)newCell Message:(YPIMMessage *)msg indexPath:(NSIndexPath *)indexPath{
    
    NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
    if (!attributed) {
        JXIMCustomObject *obj = msg.messageObject;
        YPAttachment *attachment = (YPAttachment *)obj.attachment;
        NSDictionary *dic = attachment.data;
        YPGiftSecretInfo *info = [YPGiftSecretInfo yy_modelWithDictionary:dic];
        NSString *gitName = info.giftName.length ? info.giftName : @" ";
        NSString *gitCount = [@(info.giftNum) description];
        NSString *nick = info.nick.length ? info.nick : @" ";
        NSString *sendNick = info.sendNick.length ? info.sendNick : @" ";
        
        newCell.messageLabel.textColor = [UIColor whiteColor];
        NSString *text = [NSString stringWithFormat:@"神秘爆出~%@ 通过 %@ 送礼意外获得 %@ X%@", nick, sendNick, gitName, gitCount];
        UIColor *redColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#FFD800" alpha:1.0];
        UIColor *color1 = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#F2F2F2" alpha:1];
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:text];
        [str addAttribute:NSForegroundColorAttributeName
                    value:color1
                    range:text.rangeOfAll];
        [str addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:13]
                    range:text.rangeOfAll];
        [str addAttribute:NSForegroundColorAttributeName
                    value:redColor
                    range:[text rangeOfString:nick]];
        [str addAttribute:NSForegroundColorAttributeName
                    value:redColor
                    range:[text rangeOfString:sendNick]];
        [str addAttribute:NSForegroundColorAttributeName
                    value:redColor
                    range:[text rangeOfString:gitName]];
        attributed = str;
        [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];
    }
    newCell.messageLabel.attributedText = attributed;
}

#pragma mark - handleCellForCell
- (void)handleTextCell:(YPNewMessageCell *)newCell Message:(YPIMMessage *)msg indexPath:(NSIndexPath *)indexPath{
    
    NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
    if (!attributed) {
        attributed = [self getTextAttributedStringWithMessage:msg];
        
        NSString *name = (msg.member.nick==nil?@"":[NSString stringWithFormat:@"%@:",msg.member.nick]);
        [attributed yy_insertString:name atIndex:0];
        
        //白色字体
        [attributed addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:12.0f]
                    range:NSMakeRange(0, attributed.length)];
        [attributed addAttribute:NSForegroundColorAttributeName
                    value:[UIColor whiteColor]
                    range:NSMakeRange(0, attributed.length)];
        
        
        //配置等级图片
        [self configLevelWithArrtibuted:attributed withExperLevel:msg.member.exper_level];
        
        //配置萌新图标
        if (msg.member.is_new_user) {
            [self configNewUserTagWithArrtibuted:attributed];
        }
        
        [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];

    }
    newCell.messageLabel.attributedText = attributed;
}

/** 送礼物*/
- (void)handleGiftCell:(YPNewMessageCell *)newCell  Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath{
    NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
    
    if (!attributed) {
        YPGiftReceiveInfo *info = [YPGiftReceiveInfo yy_modelWithDictionary:attachment.data];
        YPGiftInfo *giftInfo = [GetCore(YPGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeNormal];
        if (giftInfo == nil) {
            [GetCore(YPGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeMystic];
        }
        
        
        NSString *text = [NSString stringWithFormat:@"%@送%@%@X%ld", info.nick,info.targetNick,giftInfo.giftName,(long)info.giftNum];
        
        UIColor *yellowColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#FFEC00" alpha:1.0];
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:text];
        

        
        
        //白色字体
        [str addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:12.0f]
                    range:NSMakeRange(0, str.length)];
        [str addAttribute:NSForegroundColorAttributeName
                    value:[UIColor whiteColor]
                    range:NSMakeRange(0, str.length)];

        //黄色 发送者昵称
        [str addAttribute:NSForegroundColorAttributeName
                    value:yellowColor
                    range:NSMakeRange(0, info.nick.length)];
        
        //黄色 接受者昵称
        [str addAttribute:NSForegroundColorAttributeName
                    value:yellowColor
                    range:NSMakeRange(info.nick.length+1, info.targetNick.length)];
        
        
        //黄色 礼物数量
        NSString * giftNumStr = [NSString stringWithFormat:@"%ld",(long)info.giftNum];
        [str addAttribute:NSForegroundColorAttributeName
                    value:yellowColor
                    range:NSMakeRange(str.length - giftNumStr.length-1, giftNumStr.length+1)];
        
        
        YYAnimatedImageView * imageView = [[YYAnimatedImageView alloc] init];
        imageView.bounds = CGRectMake(0, -10, 24, 24);
        [imageView qn_setImageImageWithUrl:giftInfo.giftUrl placeholderImage:nil type:(ImageType)ImageTypeRoomGift];
        NSMutableAttributedString * imageString1 = [NSMutableAttributedString yy_attachmentStringWithContent:imageView contentMode:UIViewContentModeScaleAspectFit attachmentSize:imageView.frame.size alignToFont:[UIFont systemFontOfSize:14.0] alignment:YYTextVerticalAlignmentCenter];
        [str insertAttributedString:imageString1 atIndex:str.length - giftNumStr.length - 1];
        
        attributed = str;
        
        
        //配置等级图片
        [self configLevelWithArrtibuted:attributed withExperLevel:info.experLevel];
        
//        //配置萌新图标
//        if (msg.member.is_new_user) {
//            [self configNewUserTagWithArrtibuted:attributed];
//        }
        
        [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];

        
    }
    newCell.messageLabel.attributedText = attributed;
}

/** 全麦送*/
- (void)handleWholeMicSendCell:(YPNewMessageCell *)newCell  Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath{
    
    if (attachment.second == Custom_Noti_Sub_AllMicroSend) {
        NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
        if (!attributed) {
            YPGiftAllMicroSendInfo *info = [YPGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data];
            info.targetUids = attachment.data[@"targetUids"];
            YPGiftInfo *giftInfo = [GetCore(YPGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeNormal];
            if (giftInfo == nil) {
                [GetCore(YPGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeMystic];
            }
            if (info.targetUids.count > 0) {
                NSString *text = [NSString stringWithFormat:@"%@全麦送出%@X%ld", info.nick,giftInfo.giftName,(long)info.giftNum];
                
                UIColor *yellowColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#FFEC00" alpha:1.0];
                
                NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:text];

                //白色字体
                [str addAttribute:NSFontAttributeName
                            value:[UIFont systemFontOfSize:12.0f]
                            range:NSMakeRange(0, str.length)];
                [str addAttribute:NSForegroundColorAttributeName
                            value:[UIColor whiteColor]
                            range:NSMakeRange(0, str.length)];
                
                //黄色 发送者昵称
                [str addAttribute:NSForegroundColorAttributeName
                            value:yellowColor
                            range:NSMakeRange(0, info.nick.length)];
                
                //黄色 礼物数量
                NSString * giftNumStr = [NSString stringWithFormat:@"%ld",(long)info.giftNum];
                [str addAttribute:NSForegroundColorAttributeName
                            value:yellowColor
                            range:NSMakeRange(str.length - giftNumStr.length-1, giftNumStr.length+1)];
                
                
                YYAnimatedImageView * imageView = [[YYAnimatedImageView alloc] init];
                imageView.bounds = CGRectMake(0, -10, 24, 24);
                [imageView qn_setImageImageWithUrl:giftInfo.giftUrl placeholderImage:nil type:(ImageType)ImageTypeRoomGift];
                NSMutableAttributedString * imageString1 = [NSMutableAttributedString yy_attachmentStringWithContent:imageView contentMode:UIViewContentModeScaleAspectFit attachmentSize:imageView.frame.size alignToFont:[UIFont systemFontOfSize:14.0] alignment:YYTextVerticalAlignmentCenter];
                [str insertAttributedString:imageString1 atIndex:str.length - giftNumStr.length - 1];

                attributed = str;
                
                //配置等级图片
                [self configLevelWithArrtibuted:attributed withExperLevel:info.experLevel];
                
//                //配置萌新图标
//                if (msg.member.is_new_user) {
//                    [self configNewUserTagWithArrtibuted:attributed];
//                }
                
                [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];

            }
        }
        newCell.messageLabel.attributedText = attributed;
    }
}

- (void)handlechangeRoomNameSendCell:(YPNewMessageCell *)newCell  Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath{
    
    if (attachment.second == Custom_Noti_Header_ChargeRoomName) {
         NSDictionary *data = attachment.data[@"params"];
        NSString *roomName = [NSString stringWithFormat:@"%@",data[@"roomName"]];
        YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
        UserID uid = [data[@"uid"] integerValue];
        NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
        NSString* content = [NSString stringWithFormat:@"系统消息：%@更改房名为%@",managerStr,roomName];
        newCell.messageLabel.textColor = [UIColor whiteColor];
        newCell.messageLabel.text = content;
        
    }
}
/** 打Call*/
- (void)handlePlayCallSendCell:(YPNewMessageCell *)newCell  Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath{
    
    if (attachment.second == Custom_Noti_Header_Playcall) {
        NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
        if (!attributed) {
              NSDictionary *data = attachment.data[@"params"];
          
            NSString *targetName = [NSString stringWithFormat:@"%@",data[@"targetName"]];
                       NSString *sendName = [NSString stringWithFormat:@"%@",data[@"sendName"]];
            NSString *giftName = [NSString stringWithFormat:@"%@",data[@"giftName"]];
              NSString *giftUrl = [NSString stringWithFormat:@"%@",data[@"giftUrl"]];
             NSString *uid = [NSString stringWithFormat:@"%@",data[@"uid"]];
           
                NSString *text = [NSString stringWithFormat:@"%@为%@祈福送出%@X%@",sendName,targetName,giftName,@"1"];
                UIColor *yellowColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#FFEC00" alpha:1.0];
                NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:text];

                //白色字体
                [str addAttribute:NSFontAttributeName
                            value:[UIFont systemFontOfSize:12.0f]
                            range:NSMakeRange(0, str.length)];
                [str addAttribute:NSForegroundColorAttributeName
                            value:[UIColor whiteColor]
                            range:NSMakeRange(0, str.length)];
                
                //黄色 发送者昵称
                [str addAttribute:NSForegroundColorAttributeName
                            value:yellowColor
                            range:NSMakeRange(0, sendName.length)];
                
            [str addAttribute:NSForegroundColorAttributeName
                                       value:yellowColor
                                       range:NSMakeRange(sendName.length+1, targetName.length)];
            
                //黄色 礼物数量
                NSString * giftNumStr = [NSString stringWithFormat:@"%@",@"1"];
                [str addAttribute:NSForegroundColorAttributeName
                            value:yellowColor
                            range:NSMakeRange(str.length - giftNumStr.length-1, giftNumStr.length+1)];
                
                
                YYAnimatedImageView * imageView = [[YYAnimatedImageView alloc] init];
                imageView.bounds = CGRectMake(0, -10, 24, 24);
                [imageView qn_setImageImageWithUrl:giftUrl placeholderImage:nil type:(ImageType)ImageTypeRoomGift];
                NSMutableAttributedString * imageString1 = [NSMutableAttributedString yy_attachmentStringWithContent:imageView contentMode:UIViewContentModeScaleAspectFit attachmentSize:imageView.frame.size alignToFont:[UIFont systemFontOfSize:14.0] alignment:YYTextVerticalAlignmentCenter];
                [str insertAttributedString:imageString1 atIndex:str.length - giftNumStr.length - 1];

                attributed = str;
                       
                [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];

         
        }
        newCell.messageLabel.attributedText = attributed;
    }
}


//玩法数据处理
- (void)handleRoomWanfaCell:(YPNewMessageCell *)newCell  Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath{
    if (attachment.second == Custom_Noti_Header_Wanfa) {
        
        NSAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
        if (!attributed) {
            NSDictionary *data = attachment.data[@"params"];
            NSString *title = [NSString stringWithFormat:@"%@",data[@"roomDesc"]];
            NSString *content = [NSString stringWithFormat:@"%@",data[@"roomNotice"]];
            NSString *contentStr = [NSString stringWithFormat:@"%@:\n%@",title,content];
            UIColor *redColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#07CFFF" alpha:1.0];
            NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:contentStr];
            [str addAttribute:NSForegroundColorAttributeName value:redColor range:NSMakeRange(0, contentStr.length)];
            [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:12] range:NSMakeRange(0, contentStr.length)];
            attributed = str;
            [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];
        }
         newCell.messageLabel.attributedText = attributed;
    }
}


- (void)handleRoomTipCell:(YPNewMessageCell *)newCell  Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath{
    if (attachment.second == Custom_Noti_Header_Room_Tip_ShareRoom) {
        
        NSAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
        if (!attributed) {
            NSDictionary *data = attachment.data[@"data"];
            NSString *text = [NSString stringWithFormat:@"%@分享了房间",data[@"nick"]];
            NSString *nick = [NSString stringWithFormat:@"%@",data[@"nick"]];
            UIColor *redColor = [[YPYYDefaultTheme defaultTheme]colorWithHexString:@"#FF3A30" alpha:1.0];
            UIColor *whiteColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:1];
            NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:text];
            [str addAttribute:NSForegroundColorAttributeName value:redColor range:NSMakeRange(0, nick.length)];
            [str addAttribute:NSForegroundColorAttributeName value:whiteColor range:NSMakeRange(nick.length, 5)];
            [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14] range:NSMakeRange(0, nick.length)];
            [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14] range:NSMakeRange(nick.length, 5)];
            attributed = str;
            
            [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];
        }
        newCell.messageLabel.attributedText = attributed;
    }
}

- (void)handleLongZhuCell:(YPNewMessageCell *)newCell  Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath {
    
    NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
    if (!attributed) {
        attributed = [self getLongZhuAttributedStringWithMessage:msg attachment:attachment];
        
        YPRoomLongZhuMsgModel *info = [YPRoomLongZhuMsgModel yy_modelWithDictionary:attachment.data];

        
        NSMutableAttributedString *name = [[NSMutableAttributedString alloc] initWithString:info.nick];
        
        [name addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:12] range:NSMakeRange(0, name.length)];
        [name addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithHexString:@"#F2F2F2"] range:NSMakeRange(0, name.length)];

        [attributed insertAttributedString:name atIndex:0];
        
        
        
        [self configLevelWithArrtibuted:attributed withExperLevel:info.level];

        
        
        [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];
    }
    newCell.messageLabel.attributedText = attributed;
}

- (void)handleSystemNewsCell:(YPNewMessageCell *)newCell Message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath{
    
    NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
    if (!attributed) {
        
        NSDictionary *dataDict = (NSDictionary *)attachment.data;
        if ([dataDict isKindOfClass:[NSDictionary class]]) {
            
            NSString *nick = [dataDict[@"handleNick"] description].length ? [dataDict[@"handleNick"] description] : @" ";
            NSString *targetNick = [dataDict[@"targetNick"] description].length ? [dataDict[@"targetNick"] description] : @" ";
            NSInteger micPosition = [dataDict[@"micPosition"] integerValue] + 1;
            NSString *text = nil;
            NSMutableAttributedString *str = nil;
            if (attachment.second == Custom_Noti_Sub_Gift_Effect_Close) {
                // 屏蔽礼物特效
                YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                UserID uid = [dataDict[@"uid"] integerValue];
                NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";

                text = [NSString stringWithFormat:@"系统消息：%@已开启该房间小礼物特效",managerStr];
                str = [[NSMutableAttributedString alloc] initWithString:text];
                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];

            }
            else if (attachment.second == Custom_Noti_Sub_Gift_Effect_Open) {
                // 屏蔽礼物特效
                YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                UserID uid = [dataDict[@"uid"] integerValue];
                NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
//                NSString *name = [NSString stringWithFormat:@"“%@”",nick];
//                NSString *text = [NSString stringWithFormat:@"系统消息：%@%@开启过滤小额礼物特效",managerStr,name];
//                if (!nick.length) {
//                }
                text = [NSString stringWithFormat:@"系统消息：%@已屏蔽该房间小礼物特效",managerStr];
                str = [[NSMutableAttributedString alloc] initWithString:text];
                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
//                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:1]} range:[text rangeOfString:name]];
            }else if (attachment.second == Custom_Noti_Sub_Car_Effect_Open) {
                // 屏蔽坐骑特效
                YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                UserID uid = [dataDict[@"uid"] integerValue];
                NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";

                text = [NSString stringWithFormat:@"系统消息：%@已开启该房间坐骑礼物特效",managerStr];
                str = [[NSMutableAttributedString alloc] initWithString:text];
                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
            }else if (attachment.second == Custom_Noti_Sub_Car_Effect_Close) {
                // 开启坐骑特效
                YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                UserID uid = [dataDict[@"uid"] integerValue];
                NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
                
                text = [NSString stringWithFormat:@"系统消息：%@已屏蔽该房间坐骑礼物特效",managerStr];
                str = [[NSMutableAttributedString alloc] initWithString:text];
                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
            }
            else if (attachment.second == Custom_Noti_Sub_Message_Open) {
                // 屏蔽礼物特效
                YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                UserID uid = [dataDict[@"uid"] integerValue];
                NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
//                NSString *name = [NSString stringWithFormat:@"“%@”",nick];
//                NSString *text = [NSString stringWithFormat:@"系统消息：%@%@打开了房间内聊天",managerStr,name];
//                if (!nick.length) {
//                }
                text = [NSString stringWithFormat:@"系统消息：%@打开了房间内聊天",managerStr];
                str = [[NSMutableAttributedString alloc] initWithString:text];
                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
//                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:1]} range:[text rangeOfString:name]];
            }
            else if (attachment.second == Custom_Noti_Sub_Message_Close) {
                // 屏蔽礼物特效
                YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                UserID uid = [dataDict[@"uid"] integerValue];
                NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
                NSString *name = [NSString stringWithFormat:@"“%@”",nick];
//                NSString *text = [NSString stringWithFormat:@"系统消息：%@%@关闭了房间内聊天",managerStr,name];
//                if (!nick.length) {
//                }
                text = [NSString stringWithFormat:@"系统消息：%@关闭了房间内聊天",managerStr];
                str = [[NSMutableAttributedString alloc] initWithString:text];
                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
//                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:1]} range:[text rangeOfString:name]];
            }else if (attachment.second == Custom_Noti_Header_Set_Second_Manager_Close) {
                            NSString *micName = [dataDict[@"micName"] description].length ? [dataDict[@"micName"] description] : @" ";
                            YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                            UserID uid = [dataDict[@"uid"] integerValue];
                            NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
                            NSString *name = [NSString stringWithFormat:@"“%@”",micName];
       
                             text = [NSString stringWithFormat:@"系统消息：%@移除了 %@ 的管理员权限",managerStr,name];
                            str = [[NSMutableAttributedString alloc] initWithString:text];
                            [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
           
                        }else if (attachment.second == Custom_Noti_Header_Set_Second_Manager_Open) {
                                          NSString *micName = [dataDict[@"micName"] description].length ? [dataDict[@"micName"] description] : @" ";
                                         YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                                         UserID uid = [dataDict[@"uid"] integerValue];
                                         NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
                                         NSString *name = [NSString stringWithFormat:@"“%@”",micName];
                    
                                         text = [NSString stringWithFormat:@"系统消息：%@把 %@ 设置为管理员",managerStr,name];
                                         str = [[NSMutableAttributedString alloc] initWithString:text];
                                         [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
                        
                                     }else if (attachment.second == Custom_Noti_Header_ClearCharmValue) {
                                                      
                                                      YPChatRoomMember *member = GetCore(YPImRoomCoreV2).roomOwner;
                                                      UserID uid = [dataDict[@"uid"] integerValue];
                                                      NSString *managerStr = (uid == [member.account longLongValue]) ? @"房主" : @"管理员";
                                                      text = [NSString stringWithFormat:@"系统消息：%@清除了麦上用户魅力值",managerStr];
                                                      str = [[NSMutableAttributedString alloc] initWithString:text];
                                                      [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
                                     
                                                  }else if (attachment.second == Custom_Noti_Header_CHANGE_ROOM_LOCK) {
                                                                   
                                                                   text = [NSString stringWithFormat:@"系统消息：房间被锁住了，重新进入房间需要输密码"];
                                                                   str = [[NSMutableAttributedString alloc] initWithString:text];
                                                                   [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
                                                  
                                                               }else if (attachment.second == Custom_Noti_Header_CHANGE_ROOM_NO_LOCK) {
                                                                                
                                                                            
                                                                                text = [NSString stringWithFormat:@"系统消息：房间已解锁"];
                                                                                str = [[NSMutableAttributedString alloc] initWithString:text];
                                                                                [str addAttributes:@{NSForegroundColorAttributeName : [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFFFFF" alpha:0.6f]} range:NSMakeRange(0, text.length)];
                                                               
                                                                            }
            
            newCell.messageLabel.attributedText = str;
        }
            
    }
}



- (void)handleFaceCell:(YPNewMessageCell *)cell message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment indexPath:(NSIndexPath *)indexPath {
    NSMutableAttributedString *userAttributed = [self.userAttributedStringForIndexPath objectForKey:msg.messageId];
    if (!userAttributed) {
        userAttributed = [self getUserAttributedStringWithMessage:msg];
        [self.userAttributedStringForIndexPath safeSetObject:userAttributed forKey:msg.messageId];
    }
    cell.userLabel.attributedText = userAttributed;
    
    NSMutableAttributedString *attributed = [self._contentAttributedStringForIndexPath objectForKey:msg.messageId];
    if (!attributed) {
        attributed = [self getFaceAttributedStringWithMessage:msg];
        [self._contentAttributedStringForIndexPath safeSetObject:attributed forKey:msg.messageId];
    }
    cell.messageLabel.attributedText = attributed;
 }

- (void)configPKCell:(YPMessagePKCell *)cell message:(YPIMMessage *)msg attachment:(YPAttachment *)attachment
{
//    YPGiftReceiveInfo *info = [YPGiftReceiveInfo yy_modelWithDictionary:attachment.data];

    cell.attachment = attachment;

    
    
    
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.messages.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *identifier = @"YPNewMessageCell";
    
    YPIMMessage *msg = [self.messages safeObjectAtIndex:indexPath.row];
    
    if (msg.messageType == HJIMMessageTypeCustom) {
        JXIMCustomObject *obj = msg.messageObject;
        YPAttachment *attachment = (YPAttachment *)obj.attachment;
        
        if (attachment.first == Custom_Noti_Header_Mora) {
            YPMessagePKCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPMessagePKCell"];
            [self configPKCell:cell message:msg attachment:attachment];
            return cell;
        }
    }

    YPNewMessageCell * newCell = [tableView dequeueReusableCellWithIdentifier:identifier];
    newCell.userTopOffset = 0;
    newCell.userLabel.attributedText = nil;
    newCell.bgImage.image = nil;
    [newCell updatePreferredMaxLayoutWidth];
    [newCell.messageLabel setLineBreakMode:NSLineBreakByCharWrapping];
    NSString *nick = @"";
    if (msg.messageType == HJIMMessageTypeText) { //文字信息
        [self handleTextCell:newCell Message:msg indexPath:indexPath];
    } else if (msg.messageType == NIMMessageTypeNotification){ //房间人员进出
        [self handleNotificationCell:newCell Message:msg indexPath:indexPath];
    } else if (msg.messageType == HJIMMessageTypeCustom) { // custom
        JXIMCustomObject *obj = msg.messageObject;
        YPAttachment *attachment = (YPAttachment *)obj.attachment;
        if (attachment.first == Custom_Noti_Header_Winning) {//auction
            [self handleZDCell:newCell Message:msg indexPath:indexPath];
        } else if (attachment.first == Custom_Noti_Header_Gift) { //gift
            //送礼物
            [self handleGiftCell:newCell Message:msg attachment:attachment indexPath:indexPath];
        } else if (attachment.first == Custom_Noti_Header_Face) {
            if (attachment.second == Custom_Noti_Sub_Face_Send) {
                [self handleFaceCell:newCell message:msg attachment:attachment indexPath:indexPath];
            }
        }else if (attachment.first == Custom_Noti_Header_Room_Tip) {
            
            [self handleRoomTipCell:newCell Message:msg attachment:attachment indexPath:indexPath];
            
        }else if (attachment.first == Custom_Noti_Header_ALLMicroSend) {
            
            [self handleWholeMicSendCell:newCell Message:msg attachment:attachment indexPath:indexPath];
        }
        else if (attachment.first == Custom_Noti_Header_LongZhu) {
            [self handleLongZhuCell:newCell Message:msg attachment:attachment indexPath:indexPath];
        }
        else if (attachment.first == Custom_Noti_Header_PK) {
        }
        else if (attachment.first == Custom_Noti_Header_SecretGift && attachment.second == Custom_Noti_Header_SecretGift) {
            [self handleSecretCell:newCell Message:msg indexPath:indexPath];
        }else if (attachment.first == Custom_Noti_Header_Wanfa) {
            if (attachment.second == Custom_Noti_Header_Wanfa) {
                 [self handleRoomWanfaCell:newCell Message:msg attachment:attachment indexPath:indexPath];
            }
        }else if (attachment.first == Custom_Noti_Header_Queue) {//清空魅力值
            if (attachment.second == Custom_Noti_Header_ClearCharmValue) {
                 [self handleRoomWanfaCell:newCell Message:msg attachment:attachment indexPath:indexPath];
            }
        }else if (attachment.first == Custom_Noti_Header_Playcall) {//打call
            if (attachment.second == Custom_Noti_Header_Playcall) {
                  [self handlePlayCallSendCell:newCell Message:msg attachment:attachment indexPath:indexPath];
            }
        }else if (attachment.first == Custom_Noti_Header_ChargeRoomName) {//房间改变名字
            if (attachment.second == Custom_Noti_Header_ChargeRoomName) {
                  [self handlechangeRoomNameSendCell:newCell Message:msg attachment:attachment indexPath:indexPath];
            }
        }
    }
    
    JXIMCustomObject *obj = msg.messageObject;
    if ([obj isKindOfClass:[JXIMCustomObject class]]) {

        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Queue) {
                [self handleSystemNewsCell:newCell Message:msg attachment:attachment indexPath:indexPath];
            }
        }
    }
    


    
    
    if (msg.messageType == Custom_Noti_Header_Winning ||
        msg.messageType == NIMMessageTypeNotification){
        newCell.labelContentView.backgroundColor = [UIColor clearColor];
    }else{
        newCell.labelContentView.backgroundColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#050505" alpha:0.17];

    }
    
    newCell.labelContentView.layer.cornerRadius = 5;
    
    return newCell;
}


#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    YPIMMessage *msg = [self.messages safeObjectAtIndex:indexPath.row];
    UserID myUid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    if (msg.messageType == HJIMMessageTypeText) {//text
        
        if (myUid != msg.from.userIDValue) {
            if (myUid == self.roomInfo.uid) { //房主
                [self showMsgRoomOwnerOperation:msg.from.userIDValue name:msg.member.nick];
            } else if(myMember.is_manager) {//管理
                [self showMsgRoomOwnerOperation:msg.from.userIDValue name:msg.member.nick];
            }else {
//                [self showPersonalView:msg.from.userIDValue withName:msg.member.nick];
                if ([_delegate respondsToSelector:@selector(showUserInfoCardWithUid:)]) {
                    [_delegate showUserInfoCardWithUid:msg.from.userIDValue];
                }
            }
        }else{
            [self showPersonalView:myUid withName:msg.member.nick];
        }
    }else if (msg.messageType == NIMMessageTypeNotification) {//notification
        JXIMChatroomNotificationContent *content = msg.messageObject.notificationContent;
        if (content.eventType == JXIMChatroomEventTypeEnter) {//enter room
            YPChatRoomMember *member = msg.member;
            if (myUid != [member.account longLongValue]) {
                if (myMember.is_creator) {
                    [self showMsgRoomOwnerOperation:msg.from.userIDValue name:msg.member.nick];
                } else if(myMember.is_manager) {
                    [self showMsgRoomOwnerOperation:msg.from.userIDValue name:msg.member.nick];
                }else {
                    [self showPersonalView:msg.from.userIDValue withName:msg.member.nick];
                }
            }else {
                [self showPersonalView:msg.from.userIDValue withName:msg.member.nick];
            }
        }
    }else if (msg.messageType == HJIMMessageTypeCustom){
        JXIMCustomObject *customObject = msg.messageObject;
        if (customObject.attachment) {
            YPAttachment *attachment = (YPAttachment *)customObject.attachment;
            if (attachment.first == Custom_Noti_Header_Gift) {
                 [self showPersonalViewForFrom:msg.member.account.userIDValue withName:msg.member.nick];
//                [self showPersonalView:msg.from.userIDValue withName:msg.member.nick];
            }else if (attachment.first == Custom_Noti_Header_Winning ||attachment.first == Custom_Noti_Header_ALLMicroSend){
                
                if (msg.member) {
                     [self showPersonalViewForFrom:msg.member.account.userIDValue withName:msg.member.nick];
                }else{
                     NSDictionary *data = attachment.data[@"params"];
                     NSString *uid = [NSString stringWithFormat:@"%@",data[@"uid"]];
                     [self showPersonalViewForFrom:uid.userIDValue withName:msg.member.nick];
                }
               
            } if (attachment.first == Custom_Noti_Header_Playcall) {
                
                NSDictionary *data = attachment.data[@"params"];
                         NSString *uid = [NSString stringWithFormat:@"%@",data[@"uid"]];
                 [self showPersonalViewForFrom:uid.userIDValue withName:msg.member.nick];
            }
        }
    }
}

#pragma mark - ScrollViewDelegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    CGFloat height = scrollView.frame.size.height;
    CGFloat contentYoffset = scrollView.contentOffset.y;
    CGFloat distance = scrollView.contentSize.height - height;
    NSLog(@"%f",distance - contentYoffset);
    if (distance - contentYoffset <= 20) {
        self.currentIsInBottom = YES;
        NSLog(@"底");
    } else {
        self.currentIsInBottom = NO;
        NSLog(@"非");
    }
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView{
//    NSLog(@"scrollViewWillBeginDragging");
    [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(scrollTableViewMessageListToBottoWithAnimated) object:nil];
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
//    NSLog(@"scrollViewDidEndDecelerating");
    if (self.currentIsInBottom) {
        self.messagTipsBtn.hidden = YES;
        [self reloadChatList];
    }
}

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView{
//    NSLog(@"scrollViewWillBeginDecelerating");
    [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(scrollTableViewMessageListToBottoWithAnimated) object:nil];
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    if (self.currentIsInBottom) {
        self.messagTipsBtn.hidden = YES;
        [self reloadChatList];
    }
}

#pragma mark - ImRoomCoreClient
- (void)onMeInterChatRoomSuccess {
    [self updateInfo];
    [self updateMessage];
}

#pragma mark - RoomUICoreClient
- (void)roomVCWillDisappear {
    [self.giftAlertView dismissViewControllerAnimated:YES];
}

#pragma mark - BalanceErrorClient
- (void)onBalanceNotEnough {
    [self.giftAlertView dismissViewControllerAnimated:NO];
}



#pragma mark - Event

- (IBAction)gotoBottom:(UIButton *)messageTipButton {
//    NSLog(@"gotoBottom");
    self.messagTipsBtn.hidden = YES;
    [self reloadChatList];
}

#pragma mark - NSAttributedUtil
- (NSMutableAttributedString *)getUserAttributedStringWithMessage:(YPIMMessage *)msg {
    return [self getUserAttributedStringWithMessage:msg isShowMyName:NO];
}

- (NSMutableAttributedString *)getUserAttributedStringWithMessage:(YPIMMessage *)msg isShowMyName:(BOOL)isShowMyName {
    if (!msg) return nil;
    
    NSString *nick = @"";
    NSInteger experLevel = 0;
    BOOL is_new_user = NO;
    
    YPChatRoomMember *member = msg.member;
    if (!member) {
        if (msg.messageType == HJIMMessageTypeCustom) {
            JXIMCustomObject *obj = msg.messageObject;
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Face) {
                if (attachment.second == Custom_Noti_Sub_Face_Send) {
                    YPFaceSendInfo *faceattachement = [YPFaceSendInfo yy_modelWithJSON:attachment.data];
                    YPFaceReceiveInfo *info = [faceattachement.data firstObject];
                    member = [YPChatRoomMember new];
                    member.account = [NSString stringWithFormat:@"%lld", info.uid];
                    member.nick = [NSString stringWithFormat:@"%@", JX_STR_AVOID_nil(info.nick)];
                    member.exper_level = info.experLevel;
                }
            } else if (attachment.first == Custom_Noti_Header_LongZhu) {
                YPRoomLongZhuMsgModel *info = [YPRoomLongZhuMsgModel yy_modelWithDictionary:attachment.data];
                member = [YPChatRoomMember new];
                member.account = [NSString stringWithFormat:@"%@", info.uid];
                member.nick = [NSString stringWithFormat:@"%@", JX_STR_AVOID_nil(info.nick)];
                member.exper_level = info.level;
            }
        }
    }
    
    NSString *from = msg.from ? msg.from : member.account;
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    if ([from isEqualToString:userId] && !isShowMyName) {
        nick = NSLocalizedString(XCRoomMe, nil);
        nick = [nick stringByAppendingString:@"："];
        experLevel = member.exper_level;
        is_new_user = member.is_new_user;
    } else {
        nick = member.nick;
        experLevel = member.exper_level;
        is_new_user = member.is_new_user;
    }
    
    NSMutableAttributedString *userAttributed = [[NSMutableAttributedString alloc] initWithString:JX_STR_AVOID_nil(nick)];
    [userAttributed yy_setFont:[UIFont boldSystemFontOfSize:14] range:userAttributed.yy_rangeOfAll];
    [userAttributed yy_setColor:[UIColor colorWithHexString:@"#FFFFFF"] range:userAttributed.yy_rangeOfAll];
    
    [self configLevelWithArrtibuted:userAttributed withExperLevel:experLevel];
    if (is_new_user) {
        [self configNewUserTagWithArrtibuted:userAttributed];
    }
    
    return userAttributed;
}

- (NSMutableAttributedString *)getTextAttributedStringWithMessage:(YPIMMessage *)msg {
    if (!msg) return nil;
    
    NSMutableAttributedString *attributed = [[NSMutableAttributedString alloc] initWithString:msg.text];
    [attributed yy_setFont:[UIFont systemFontOfSize:12] range:attributed.yy_rangeOfAll];
    [attributed yy_setColor:[UIColor colorWithHexString:@"#F2F2F2"] range:attributed.yy_rangeOfAll];
    return attributed;
}

- (NSMutableAttributedString *)getEnterNotificationAttributedStringWithMessage:(YPIMMessage *)msg {
    if (!msg) return nil;
    
    NSString *user_car_name = @"";
    if (msg.member.car_name.length) {
        user_car_name = msg.member.car_name;
    }
    
    NSString *text = nil;
    NSMutableAttributedString *str = nil;
    UIColor *color = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFD800" alpha:1];
    UIColor *color2 = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#F2F2F2" alpha:1];
    NSString *carName = [user_car_name stringByReplacingOccurrencesOfString:@"\"" withString:@""];
    carName = [carName stringByReplacingOccurrencesOfString:@"\"" withString:@""];
    if (carName.length) {
        text = [NSString stringWithFormat:@"驾着%@来了", user_car_name];
        str = [[NSMutableAttributedString alloc] initWithString:text];
        [str addAttribute:NSForegroundColorAttributeName
                    value:color2
                    range:text.rangeOfAll];
        [str addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:13.f]
                    range:text.rangeOfAll];
        [str addAttribute:NSForegroundColorAttributeName
                    value:color
                    range:[text rangeOfString:user_car_name]];
    }
    else {
        text = [NSString stringWithFormat:@"来了"];
        str = [[NSMutableAttributedString alloc] initWithString:text];
        [str addAttribute:NSForegroundColorAttributeName
                    value:color2
                    range:text.rangeOfAll];
        [str addAttribute:NSFontAttributeName
                    value:[UIFont systemFontOfSize:13.f]
                    range:text.rangeOfAll];
        
    }
    return str;
}

- (NSMutableAttributedString *)getFaceAttributedStringWithMessage:(YPIMMessage *)message {
    if (message) {
        JXIMCustomObject *obj = message.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            YPFaceSendInfo *faceattachement = [YPFaceSendInfo yy_modelWithJSON:attachment.data];
            NSMutableArray *arr = [faceattachement.data mutableCopy];
            if (arr.count > 0) {
                NSMutableAttributedString *wholeStr = [[NSMutableAttributedString alloc]init];
                for (int i = 0; i < arr.count; i++) {
                    YPFaceReceiveInfo *item = arr[i];
                    NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@""];
                    for (int j = 0; j < item.resultIndexes.count; j++) {
                        NSNumber *index = item.resultIndexes[j];
                        UIImage *face = [GetCore(YPFaceCore)findFaceImageById:item.faceId index:[index integerValue]];
                        YYAnimatedImageView * imageView = [[YYAnimatedImageView alloc] init];
                    
                        imageView.contentMode = UIViewContentModeScaleAspectFit;
                        if (j == 0) {
                            imageView.bounds = CGRectMake(0, 0, 30, 30);
                        }else {
                            imageView.bounds = CGRectMake(0, 0, 30, 30);
                        }
                        
                        imageView.image = face;
                        NSMutableAttributedString * imageString1 = [NSMutableAttributedString yy_attachmentStringWithContent:imageView contentMode:UIViewContentModeScaleAspectFit attachmentSize:CGSizeMake(imageView.frame.size.width, imageView.frame.size.height) alignToFont:[UIFont systemFontOfSize:14.0] alignment:YYTextVerticalAlignmentCenter];
                        [str appendAttributedString:imageString1];
                    }
    
                    if (arr.count > 1 && i != arr.count - 1) {
                        NSString *returnStr = @"\n";
                        NSMutableAttributedString *returnAttStr = [[NSMutableAttributedString alloc] initWithString:returnStr];
                        [str appendAttributedString:returnAttStr];
                    }
                    str.yy_lineSpacing = 5;
                    [wholeStr appendAttributedString:str];
                }
                
                return wholeStr;
            }
        }
    }
    return nil;
}


- (NSMutableAttributedString *)getLongZhuAttributedStringWithMessage:(YPIMMessage *)message attachment:(YPAttachment *)attachment {
    if (!message) return nil;
    if (!attachment) return nil;
    
    YPRoomLongZhuMsgModel *info = [YPRoomLongZhuMsgModel yy_modelWithDictionary:attachment.data];
    
    NSString *text = nil;
    NSString *oneStr = nil;
    NSString *twoStr = nil;
    
    if (attachment.second == Custom_Noti_Sub_LongZhu_Choose) {
        // 选择
        if (info.isShowd) {
            oneStr = @"心动选择了 ";
            text = [NSString stringWithFormat:@"%@",oneStr];
        }
        else {
            oneStr = @"已选择了 ";
            twoStr = @" 等待TA的命中注定";
            text = [NSString stringWithFormat:@"%@%@",oneStr, twoStr];
        }
    }
    else if (attachment.second == Custom_Noti_Sub_LongZhu_Supei) {
        // 速配
        if (info.isShowd) {
            oneStr = @"解签 ";
            text = [NSString stringWithFormat:@"%@",oneStr];
        }
        else {
            oneStr = @" 开始求签了 ";
            text = [NSString stringWithFormat:@"%@",oneStr];
        }
    }else if (attachment.second == Custom_Noti_Sub_LongZhu_cancel) {
        // 速配
        oneStr = @" 放弃了解签 ";
        text = [NSString stringWithFormat:@"%@",oneStr];
    }
    
    NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:text];
    
    [str addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithHexString:@"#F2F2F2"] range:NSMakeRange(0, text.length)];
    [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:12] range:NSMakeRange(0, text.length)];
    
    if (info.isShowd) {
        [str addAttribute:NSForegroundColorAttributeName value:UIColorHex(FFD800) range:[text rangeOfString:oneStr]];
    }
    
    for (int i = 2; i >= 0; i--) {
        if (i < info.numArr.count && i >= 0) {
            
            NSInteger num =  [info.numArr[i] integerValue];
            YYAnimatedImageView * imageView = [[YYAnimatedImageView alloc] init];
            imageView.bounds = CGRectMake(0, 0, 24, 38);
            if (info.isShowd) {
                
                imageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num]];
            }
            else {
                imageView.image = [UIImage imageNamed:@"room_game_longzhu_help"];
            }
            
            NSMutableAttributedString * imageString1 = [NSMutableAttributedString yy_attachmentStringWithContent:imageView contentMode:UIViewContentModeScaleAspectFit attachmentSize:imageView.frame.size alignToFont:[UIFont systemFontOfSize:12] alignment:YYTextVerticalAlignmentCenter];
            [str insertAttributedString:imageString1 atIndex:oneStr.length];
        }
    }
    return str;
}

#pragma mark - private method
- (void) _configureContentAttributedMap{
    
    self._contentAttributedStringForIndexPath = [NSMutableDictionary dictionary];
}

//获取房间信息
- (void)updateInfo {
    self.roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
}
//刷新
- (void)updateMessage {
    self.messages = [NSMutableArray arrayWithArray:GetCore(YPRoomCoreV2Help).messages];
    [self reloadChatList];
}
//刷新
- (void) reloadChatList {
//    NSLog(@"reloadChatList");
    [self handleMessageTypeWith:self.talkMessageType];
    if(self.messages.count == 0) {
        [self.tableView reloadData];
        return;
    }
    YPIMMessage *msg = self.messages.lastObject;
    if ([self.currentMsgId isEqualToString:msg.messageId]) return;
    [self reloadData];
}

#pragma mark - RoomCoreClient
//房间信息改变
- (void)onCurrentRoomMsgUpdate:(NSMutableArray *)messages {

    self.messages = [messages mutableCopy];
    NSLog(@"onCurrentRoomMsgUpdate:%d",(int)self.messages.count);
    if (self.currentIsInBottom) {
        self.messagTipsBtn.hidden = YES;
        [self reloadChatList];
    }else{
        self.messagTipsBtn.hidden = NO;
    }
}

- (void)reloadData {

    [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(scrollTableViewMessageListToBottoWithAnimated) object:nil];
    
    [self performSelector:@selector(scrollTableViewMessageListToBottoWithAnimated) withObject:nil afterDelay:0.25];
}

- (void)scrollTableViewMessageListToBottoWithAnimated{
    if (self.messages.count < 1) {
        return;
    }
    [self.tableView reloadData];
    int rows = [self.tableView numberOfRowsInSection:0];
    NSLog(@"numberOfRowsInSection:%d",rows);
    dispatch_async(dispatch_get_main_queue(), ^{
        if (rows > 1 && self.messages.count > 0) {
            [self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:(rows-1) inSection:0] atScrollPosition:UITableViewScrollPositionBottom animated:NO];
        }
    });
    
    self.messagTipsBtn.hidden = YES;
    YPIMMessage *msg = self.messages.lastObject;
    self.currentMsgId = msg.messageId;
}

//底部弹框逻辑
- (void)showMsgRoomOwnerOperation:(UserID)userId name:(NSString *)name {

    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    
     @weakify(self);
    [[GetCore(YPImRoomCoreV2) rac_queryChartRoomMemberByUid:[NSString stringWithFormat:@"%lld",userId]] subscribeNext:^(id x) {
        @strongify(self);
        
        YPChatRoomMember *member = (YPChatRoomMember *)x;
        
        //检测弹卡片的情况
        if ([self isShowUserCard:member]) {
            [self showPersonalView:userId withName:nil];
            return;
        }
        
        // 房主只弹个人资料
        if (member.is_creator) {
            [self showPersonalView:userId withName:nil];
            return;
        }

        UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
        [alter addAction:[UIAlertAction actionWithTitle:@"送礼物" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            if (myMember.is_creator || myMember.is_manager) {
                [self showPersonalView:userId withName:name];
            }else{
                //点击消息都跳卡片
                if ([_delegate respondsToSelector:@selector(showUserInfoCardWithUid:)]) {
                    [_delegate showUserInfoCardWithUid:userId];
                }
            }
            
        }]];

        
        [alter addAction:[UIAlertAction actionWithTitle:@"查看资料" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            if ([_delegate respondsToSelector:@selector(showUserInfoCardWithUid:)]) {
                [_delegate showUserInfoCardWithUid:userId];
            }
        }]];
        
        [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
        
        [self setupCreatorSheet:alter userId:userId name:name member:member];
        //        //manager
        [self setupManagerSheet:alter userId:userId name:name member:member];
        
        
        [[self topViewController] presentViewController:alter animated:YES completion:nil];

    }];
    
}
//设置房主弹框
- (void)setupCreatorSheet:(UIAlertController *)alter userId:(UserID)userId name:(NSString *)name member:(YPChatRoomMember *)member{
    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    if (myMember.is_creator) {
        if (!member.is_creator) {
            
            if ([self userIsOnMicroWith:userId]) {

            } else if (![self userIsOnMicroWith:userId]) {

//                [alter addAction:[UIAlertAction actionWithTitle:@"抱Ta上麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//                    [GetCore(YPRoomQueueCoreV2Help) inviteUpFreeMic:userId];
//                }]];


            }
            
            [alter addAction:[UIAlertAction actionWithTitle:@"踢出房间" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [YPMidSure kickUser:userId didKickFinish:nil];
            }]];

            
            if (member.is_manager) {
                
                [alter addAction:[UIAlertAction actionWithTitle:@"移除管理员" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    [GetCore(YPImRoomCoreV2) markManagerList:userId enable:NO];
                }]];

            }else {
                [alter addAction:[UIAlertAction actionWithTitle:@"设置管理员" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    [GetCore(YPImRoomCoreV2) markManagerList:userId enable:YES];
                }]];


            }
            @weakify(self);
            
            [alter addAction:[UIAlertAction actionWithTitle:@"加入房间黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                @strongify(self);
                [self showAlertWithAddBlackList:member name:name uid:userId];
            }]];


        }
    }
}
//设置管理员弹框
- (void)setupManagerSheet:(UIAlertController *)alter userId:(UserID)userId name:(NSString *)name member:(YPChatRoomMember *)member{
    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    if (myMember.is_manager) {
      @weakify(self);
        if ([self userIsOnMicroWith:userId]) {

        } else if (![self userIsOnMicroWith:userId]) {
            
//            [alter addAction:[UIAlertAction actionWithTitle:@"抱Ta上麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//                [GetCore(YPRoomQueueCoreV2Help) inviteUpFreeMic:userId];
//            }]];


        }
        if (!member.is_creator && !member.is_manager) {

            [alter addAction:[UIAlertAction actionWithTitle:@"踢出房间" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [YPMidSure kickUser:userId didKickFinish:nil];
            }]];


            [alter addAction:[UIAlertAction actionWithTitle:@"加入房间黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                @strongify(self);
                [self showAlertWithAddBlackList:member name:name uid:userId];            }]];

        }
    }
}


//送礼物的人的资料信息
- (void)showPersonalViewForFrom:(UserID)uid withName:(NSString *)name  {
//    UserID uid2 = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    
    if ([_delegate respondsToSelector:@selector(showUserInfoCardWithUid:)]) {
            [_delegate showUserInfoCardWithUid:uid];
        }

}

//show个人信息
- (void)showPersonalView:(UserID)uid withName:(NSString *)name {
    UserID uid2 = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    if (uid == uid2) {
        if ([_delegate respondsToSelector:@selector(showUserInfoCardWithUid:)]) {
            [_delegate showUserInfoCardWithUid:uid];
        }
    } else {

        if (name.length) {

            //改弹出礼物
            [self showGiftViewWithUid:uid withName:name];
        }
        else {
            @weakify(self);
            [[GetCore(YPUserCoreHelp) getUserInfoByUidV2:uid] subscribeNext:^(id x) {
                @strongify(self);
                UserInfo *info = (UserInfo *)x;
                [self showGiftViewWithUid:uid withName:info.nick];
            }];
        }
    }
}

- (void)showGiftViewWithUid:(UserID)uid withName:(NSString *)name {
    
    if ([_delegate respondsToSelector:@selector(showGiftWithUid:withName:)]) {
        
        if (name) {
            
            [_delegate showGiftWithUid:uid withName:name];
        }
        else {
            @weakify(self);
            [[GetCore(YPUserCoreHelp) getUserInfoByUidV2:uid] subscribeNext:^(id x) {
                @strongify(self);
                UserInfo *info = (UserInfo *)x;
                [self.delegate showGiftWithUid:uid withName:name];
            }];
        }
    }
}

//黑名单
- (void)showAlertWithAddBlackList:(YPChatRoomMember *)member name:(NSString *)name uid:(NSInteger)uid {
    __block NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),name];
    
    [GetCore(YPUserCoreHelp) getUserInfo:[member.account longLongValue] refresh:NO success:^(UserInfo *info) {
        if (!member.nick.length) {
            title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),info.nick.length ? info.nick : name];
        }
    }];
   
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        [GetCore(YPImRoomCoreV2) markBlackList:uid enable:YES];
    }];
    
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    [alert addAction:enter];
    [alert addAction:cancel];
    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
}
//是否在麦上
- (BOOL)userIsOnMicroWith:(UserID)uid {
    
    NSArray *micMembers = [GetCore(YPImRoomCoreV2).micMembers copy];
    if (micMembers != nil && micMembers.count > 0) {
        for (int i = 0; i < micMembers.count; i ++) {
            YPChatRoomMember *chatRoomMember = micMembers[i];
            if ([chatRoomMember.account longLongValue] == uid) {
                return YES;
            }
        }
    }
    return NO;
}

- (BOOL)isShowUserCard:(YPChatRoomMember *) member{
    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    
    if (myMember.is_manager && [self userIsOnMicroWith:[member.account longLongValue]]) {
        
        if (member.is_manager || member.is_creator) {
            return YES;
        }else{
            return NO;
        }
    }else{
        return NO;
    }
    
    
    
}

- (YPRoomMsgHeaderView *)headerView
{
    if (!_headerView) {
        _headerView = [[YPRoomMsgHeaderView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 72)];
    }
    return _headerView;
}

- (NSMutableDictionary *)userAttributedStringForIndexPath {
    if (!_userAttributedStringForIndexPath) {
        _userAttributedStringForIndexPath = @{}.mutableCopy;
    }
    return _userAttributedStringForIndexPath;
}

@end
