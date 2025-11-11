//
//  HJGiftCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GiftReceiveInfo.h"
#import "HJGiftAllMicroSendInfo.h"
#import "HJGiftSecretInfo.h"
@protocol HJGiftCoreClient <NSObject>
@optional
- (void)updateGiftList:(NSMutableArray *)giftInfos;
- (void)onRequestGiftList:(NSArray *)giftInfos;
- (void)onReceiveGift:(HJGiftAllMicroSendInfo *)giftReceiveInfo isALLChannelSend:(BOOL)isALLChannelSend;
- (void)onGiftIsOffLine:(NSString *)message;
- (void)onGiftIsRefresh;
- (void)onSendGiftFail;

// 送礼物爆出神秘礼物
- (void)didReceiveSecretGiftWithInfo:(HJGiftSecretInfo *)giftInfo;
@end
