//
//  YPNIMNotificationContentConfig.m
//  YPNIMKit
//
//  Created by amao on 9/15/15.
//  Copyright (c) 2015 NetEase. All rights reserved.
//

#import "YPNIMNotificationContentConfig.h"
#import "M80AttributedLabel+YPNIMKit.h"
#import "YPNIMKitUtil.h"
#import "YPNIMUnsupportContentConfig.h"
#import "YPNIMKit.h"

@implementation YPNIMNotificationContentConfig
- (CGSize)contentSize:(CGFloat)cellWidth message:(NIMMessage *)message
{
    NIMNotificationObject *object = message.messageObject;
    NSAssert([object isKindOfClass:[NIMNotificationObject class]], @"message should be notification");
    
    CGSize contentSize = CGSizeZero;
    
    switch (object.notificationType) {
        case NIMNotificationTypeTeam:
        case NIMNotificationTypeChatroom:
        {
            CGFloat TeamNotificationMessageWidth  = cellWidth;
            UILabel *label = [[UILabel alloc] init];
            label.text  = [YPNIMKitUtil messageTipContent:message];
            label.font = [UIFont boldSystemFontOfSize:NIMKit_Notification_Font_Size];
            label.numberOfLines = 0;
            CGFloat padding =   [YPNIMKit sharedKit].config.maxNotificationTipPadding;
            CGSize size = [label sizeThatFits:CGSizeMake(cellWidth - 2 * padding, CGFLOAT_MAX)];
            CGFloat cellPadding = 11.f;
            contentSize = CGSizeMake(TeamNotificationMessageWidth, size.height + 2 * cellPadding);
            break;
        }
        case NIMNotificationTypeNetCall:{
            M80AttributedLabel *label = [[M80AttributedLabel alloc] initWithFrame:CGRectZero];
            label.autoDetectLinks = NO;
            label.font = [UIFont systemFontOfSize:NIMKit_Message_Font_Size];
            NSString *text = [YPNIMKitUtil messageTipContent:message];
            [label nim_setText:text];
            
            CGFloat msgBubbleMaxWidth    = (cellWidth - 130);
            CGFloat bubbleLeftToContent  = 14;
            CGFloat contentRightToBubble = 14;
            CGFloat msgContentMaxWidth = (msgBubbleMaxWidth - contentRightToBubble - bubbleLeftToContent);
            contentSize = [label sizeThatFits:CGSizeMake(msgContentMaxWidth, CGFLOAT_MAX)];
            break;
        }
        default:
        {
            YPNIMUnsupportContentConfig *config = [[YPNIMUnsupportContentConfig alloc] init];
            contentSize = [config contentSize:cellWidth message:message];
            NSAssert(0, @"not supported notification type %zd",object.notificationType);
        }
            break;
    }
    return contentSize;
}

- (NSString *)cellContent:(NIMMessage *)message
{
    NIMNotificationObject *object = message.messageObject;
    NSAssert([object isKindOfClass:[NIMNotificationObject class]], @"message should be notification");
    
    switch (object.notificationType) {
        case NIMNotificationTypeTeam:
        case NIMNotificationTypeChatroom:
            return @"YPNIMSessionNotificationContentView";
        case NIMNotificationTypeNetCall:
            return @"YPNIMSessionNetChatNotifyContentView";
        case NIMNotificationTypeUnsupport:
            return @"YPNIMSessionUnknowContentView";
        default:
             return @"YPNIMSessionNotificationContentView";
            break;
    }
}

- (UIEdgeInsets)contentViewInsets:(NIMMessage *)message
{
    return [[YPNIMKit sharedKit].config setting:message].contentInsets;
}

@end
