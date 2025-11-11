//
//  NIMSessionDefaultConfig.m
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import "YPNIMCellLayoutConfig.h"
#import "YPNIMSessionMessageContentView.h"
#import "YPNIMSessionUnknowContentView.h"
#import "M80AttributedLabel+YPNIMKit.h"
#import "YPNIMKitUtil.h"
#import "UIImage+YPNIMKit.h"
#import "YPNIMMessageModel.h"
#import "YPNIMBaseSessionContentConfig.h"
#import "YPNIMKit.h"

@interface YPNIMCellLayoutConfig()

@end

@implementation YPNIMCellLayoutConfig

- (CGSize)contentSize:(YPNIMMessageModel *)model cellWidth:(CGFloat)cellWidth{
    id<NIMSessionContentConfig>config = [[NIMSessionContentConfigFactory sharedFacotry] configBy:model.message];
    return [config contentSize:cellWidth message:model.message];
}

- (NSString *)cellContent:(YPNIMMessageModel *)model{
    id<NIMSessionContentConfig>config = [[NIMSessionContentConfigFactory sharedFacotry] configBy:model.message];
    NSString *cellContent = [config cellContent:model.message];
    return cellContent.length ? cellContent : @"YPNIMSessionUnknowContentView";
}


- (UIEdgeInsets)contentViewInsets:(YPNIMMessageModel *)model{
    id<NIMSessionContentConfig>config = [[NIMSessionContentConfigFactory sharedFacotry] configBy:model.message];    
    return [config contentViewInsets:model.message];
}


- (UIEdgeInsets)cellInsets:(YPNIMMessageModel *)model
{
    if ([[self cellContent:model] isEqualToString:@"YPNIMSessionNotificationContentView"]) {
        return UIEdgeInsetsZero;
    }
    CGFloat cellTopToBubbleTop           = 3;
    CGFloat otherNickNameHeight          = 20;
    CGFloat otherBubbleOriginX           = [self shouldShowAvatar:model]? 55 : 0;
    CGFloat cellBubbleButtomToCellButtom = 13;
    if ([self shouldShowNickName:model])
    {
        //要显示名字
        return UIEdgeInsetsMake(cellTopToBubbleTop + otherNickNameHeight ,otherBubbleOriginX,cellBubbleButtomToCellButtom, 0);
    }
    else
    {
        return UIEdgeInsetsMake(cellTopToBubbleTop,otherBubbleOriginX,cellBubbleButtomToCellButtom, 0);
    }

}

- (BOOL)shouldShowAvatar:(YPNIMMessageModel *)model
{
    return [[YPNIMKit sharedKit].config setting:model.message].showAvatar;
}


- (BOOL)shouldShowNickName:(YPNIMMessageModel *)model{
    NIMMessage *message = model.message;
    if (message.messageType == NIMMessageTypeNotification)
    {
        NIMNotificationType type = [(NIMNotificationObject *)message.messageObject notificationType];
        if (type == NIMNotificationTypeTeam) {
            return NO;
        }
    }
    if (message.messageType == NIMMessageTypeTip) {
        return NO;
    }
    return (!message.isOutgoingMsg && message.session.sessionType == NIMSessionTypeTeam);
}


- (BOOL)shouldShowLeft:(YPNIMMessageModel *)model
{
    return !model.message.isOutgoingMsg;
}

- (CGPoint)avatarMargin:(YPNIMMessageModel *)model
{
    return CGPointMake(8.f, 0.f);
}

- (CGSize)avatarSize:(YPNIMMessageModel *)model
{
    return CGSizeMake(42, 42);
}

- (CGPoint)nickNameMargin:(YPNIMMessageModel *)model
{
    return [self shouldShowAvatar:model] ? CGPointMake(57.f, -3.f) : CGPointMake(10.f, -3.f);
}


- (NSArray *)customViews:(YPNIMMessageModel *)model
{
    return nil;
}

- (BOOL)disableRetryButton:(YPNIMMessageModel *)model
{
    if (!model.message.isReceivedMsg)
    {
        return model.message.deliveryState != NIMMessageDeliveryStateFailed;
    }
    else
    {
        return model.message.attachmentDownloadState != NIMMessageAttachmentDownloadStateFailed;
    }
}

@end
