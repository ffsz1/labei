//
//  HJGiftSendInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Attachment.h"
#import <NIMSDK/NIMSDK.h>

@interface HJGiftSendInfo : Attachment
@property(nonatomic, assign)UserID uid;
@property(nonatomic, assign)UserID targetUid;
@property(nonatomic, assign)NSInteger giftId;
@property(nonatomic, strong)NSString *nick;
@property(nonatomic, strong)NSString *avatar;
@property(nonatomic, copy) NSString  *targetNick;
@property(nonatomic, copy) NSString *targetAvatar;
@property(nonatomic, assign)NSInteger giftNum;
@property(nonatomic, strong)NSDictionary *encodeAttachemt;
@end
