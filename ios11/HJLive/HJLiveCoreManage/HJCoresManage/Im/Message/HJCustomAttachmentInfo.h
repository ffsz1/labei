//
//  HJCustomAttachmentInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>


@class NIMKitBubbleStyleObject;


typedef NS_ENUM(NSInteger,XCCustomMessageType){
    CustomMessageTypeOpenLiveAlert  = 1, //开播提醒
    CustomMessageTypeGift   = 2, //礼物
    CustomMessageTypeNews   = 3, //推文
    CustomMessageTypeRedPacket  = 4, // 福利消息
    CustomMessageTypeInviteMic = 5,//邀请上麦
    CustomMessageTypeInviteToRoom  = 6,
};

@protocol HJCustomAttachmentInfo <NSObject>

@optional

- (NSString *)cellContent:(NIMMessage *)message;

- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;

- (UIEdgeInsets)contentViewInsets:(NIMMessage *)message;

- (NSString *)formatedMessage;

- (UIImage *)showCoverImage;

- (BOOL)shouldShowAvatar;

- (void)setShowCoverImage:(UIImage *)image;

- (BOOL)canBeRevoked;

- (BOOL)canBeForwarded;

@end
